/*
 *      This program is free software; you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation; either version 2 of the License, or
 *      (at your option) any later version.
 *      
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *      
 *      You should have received a copy of the GNU General Public License
 *      along with this program; if not, write to the Free Software
 *      Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 *      MA 02110-1301, USA.
 */
package ssc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/* DatatypeConverter do not exist under Android */
import android.util.Base64;

/**
 * This is our restful client used be Sense Smart City. Currently it only GET 
 * requests is implemented. No data can be sent upstreams.
 * 
 * It should only be used for Sense Smart City since it make a few assumptions 
 * about the server. The server is assumed to use a self-signed SSL certificate.
 * User name and password mandatory and not an option.
 * 
 * Note that JSON is not processed at all in this class.
 * 
 * Known errors is wrapped into SSCExceptions.
 * 
 * @author Jim Gunnarsson, di98jgu
 */
class RestfulClient {
   
   /** Username */
   private String user = "";
   /** Password */
   private String pwd = "";
   
   /**
    * Initializes this client for SSC server. Username and password is needed.
    *
    * @param user Username
    * @param pwd Password
    */
   public RestfulClient(String user, String pwd) {
   
      this.user = user;
      this.pwd = pwd;
   
   }
   
   /**
    * Make a GET request to server. User and password is always used.
    * 
    * @param url Address to host
    * @param args Query map
    * 
    * @return A JSON respons object
    * 
    * @throws SSCException used for all other exceptions
    * @throws SSCException.ConnectionFailed if a connection can't be establish.
    * @throws SSCException.ClientError
    * @throws SSCException.ConnectionFailed for response code 4xx
    * @throws SSCException.ServerError for response code 5xx
    * @throws SSCException.Mystery Exception at places no exception is expected
    */
   public String getData(String url, Map<String, String> args)
      throws SSCException {
         
      String result;
     
      try {
         
         // Connect to a self-signed HTTPS server.
         SSLContext ssl_ctxt = SSLContext.getInstance("SSL");
         ssl_ctxt.init(null, trustAll, new java.security.SecureRandom());
         HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctxt.getSocketFactory());
         
         // Please just ignore host
         HttpsURLConnection.setDefaultHostnameVerifier(ignoreHost);
         
         // Prepare https connection
         HttpsURLConnection request = 
            (HttpsURLConnection) new URL(url + queryString(args)).openConnection();
         request.setDoInput(true);
         request.setRequestMethod("GET");
         
         // Service is of type json
         request.setRequestProperty("Content-Type", "application/json");
         request.setRequestProperty(
            "Authorization", basicAuth(user, pwd));
         
         // Then connect to server
         request.connect();
         
         mangleResponse(request);
         
         // Get sensor list from server
         InputStream ssc_in = request.getInputStream();
         result = toString(ssc_in);
         ssc_in.close();
         
      } catch (java.net.UnknownHostException e) {
         // Most likely a incorrect url address.
         
         throw new SSCException.ConnectionFailed(e);
         
      } catch (java.net.MalformedURLException e) {
         // Raised by URL if an unknown protocol is specified.
         
         throw new SSCException.ConnectionFailed(e);

      } catch (java.security.NoSuchAlgorithmException e) {
         // This is raised  by SSLContext if a unknown algorithm 
         // is provided. Here only 'SSL' is used so this is unlikely.
         
         throw new SSCException.Mystery(e);
         
      } catch (java.security.KeyManagementException e) {
         // SSLContext mehthod init may throw this, unlikely in this 
         // case since it is null   
         
         throw new SSCException.Mystery(e);
         
      } catch (IOException e) {
         // Might be raised by any activity regarding IO activities. 
         
         throw new SSCException(e); 
         
      }
         
      return result;
         
   }
   
   /**
    * Mangle the response code. If everything is fine, code 200, this method
    * just return without any return value. If connection failed or server 
    * can't handle the request a error is thrown. 
    * 
    * @param connection The URLConnection 
    * 
    * @throws SSCException.ClientError for response code 4xx
    * @throws SSCException.ServerError for response code 5xx
    * @throws SSCException.Mystery Errors unknown to us
    * @throws java.io.IOException HttpsURLConnection may throw this one
    */
   private void mangleResponse(HttpsURLConnection connection) 
      throws java.io.IOException {
   
      int code = connection.getResponseCode();
      URL url = connection.getURL();
      String msg = connection.getResponseMessage();
   
      if (code == 200) {
         // Everything is fine, just return
         return;
      }
      if (code >= 400) {
         // Various errors, no details for now.
         throw new SSCException.ClientError(
            code + ": " + msg + " " + url);
      
      }
      if (code >= 500) {
         // Various errors on the server side
         throw new SSCException.ServerError(
            code + ": " + msg + " " + url);
      }
      
      // Should not get here...
      throw new SSCException.Mystery(code + ": " + msg + " " + url);
   
   }
   
   /**
    * Basic authentication using base 64 encoding.
    * 
    * @param user Username
    * @param pwd Password
    * 
    * @return Authentication string 
    */ 
   private String basicAuth(String user, String pwd) throws 
      java.io.UnsupportedEncodingException {
      
      /* DatatypeConverter don't exist under Android
      String secret = user + ":" + pwd;
      String auth = "Basic " + 
         DatatypeConverter.printBase64Binary(secret.getBytes());
      */
      
      String secret = user + ":" + pwd;
      String auth = "Basic " + 
            Base64.encodeToString(secret.getBytes("UTF-8"), Base64.NO_WRAP);
            
      return auth;
     
   }

   /**
    * This return GET request parameters, that everything after the
    * '?' including the '?' it self. This method do at this moment
    * a very crude work. No checks just line them up. Not critical
    * since we have almost absolute control over what goes here.
    * 
    * FIXME: Method encode in java.net.URLEncoder has been deprecated
    * 
    * @param request Mapped key value parameters
    * 
    * @return The GET request parameters as a string. 
    */
   private String queryString(Map<String, String> request) {
      
      String request_str = "";

      Iterator<Map.Entry<String, String>> i = request.entrySet().iterator();
      
      while (i.hasNext()) {
         
         Map.Entry<String, String> p = i.next();
         
         if (p.getValue() == null) {
            continue;
         }
         
         // Make sure the request parameters is in proper form.
         request_str += 
            "&" + 
            URLEncoder.encode(String.valueOf(p.getKey())) + 
            "=" + 
            URLEncoder.encode(String.valueOf(p.getValue()));
         
      }
      
      // At index 0 we have '&' should be '?'
      request_str = "?" + request_str.substring(1);

      return request_str;
      
   }

   /**
    * Read stream and return a UTF-8 encoded string. While not yet
    * confirmed it is likely that the returned JSON object is in 
    * UTF-8. This method make no assumptions about content of the 
    * stream and thus tries to encode anything as UTF-8.
    * 
    * @param stream A InputStream
    * 
    * @return String encoded in UTF-8
    * 
    * @throws java.io.UnsupportedEncodingException if this method
    * fails to encode received data as UTF-8
    * @throws java.io.IOException Any IO error
    */
   private String toString(InputStream stream) throws 
      java.io.UnsupportedEncodingException, 
      java.io.IOException {
     
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      
      byte[] buffer = new byte[1024];
      int length = 0;
         
      while( (length = stream.read(buffer)) > 0 ) {
            
         result.write(buffer, 0, length);
            
      }
      
      return result.toString("UTF-8");
      
   }
   
   /**
    * This class is needed to prevent the java.io.IOException "HTTPS 
    * hostname wrong error" due to self-signed SSL certificate.
    */
   private static HostnameVerifier ignoreHost = new HostnameVerifier() {
      
      public boolean verify(String urlHostName, SSLSession session) {
                    
         return true;
            
      }
   };
   /**
    * This is a trust all manager for SSL certificate. The server is 
    * self-signed and this is the standard solution. Note that 
    * getAcceptedIssuers should NOT return null according to 
    * official documentation but may return a empty array.
    */
   private static TrustManager[] trustAll = new TrustManager[] {
      
      new X509TrustManager() {
         
         public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            
            return new java.security.cert.X509Certificate[]{};
            
         }
         public void checkClientTrusted(
            java.security.cert.X509Certificate[] certs,
            String authType) {
               
         }
         public void checkServerTrusted(
            java.security.cert.X509Certificate[] certs,
            String authType) {
               
         }
      }
   };

}
