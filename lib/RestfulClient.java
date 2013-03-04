/**
 * 
 * Class: RestfulClient
 * 
 * This is our restful client used be Sense Smart City. Currently
 * it only GET requests is implemented. No data can be sent 
 * upstreams.
 * 
 * It should only be used for Sense Smart City since it make a few
 * assumptions about the server. The server is assumed to use a 
 * self-signed SSL certificate. User name and password mandatory and 
 * not an option.
 * 
 * Note that JSON is not processed at all in this class.
 * 
 * Known errors is wrapped into SSCExceptions.
 * 
 * @author Jim Gunnarsson
 */

import java.io.*;

import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.*;

import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.DatatypeConverter;

class RestfulClient {
   
   /** User credentials */
   private String user = "";
   private String pwd = "";
   
   
   public RestfulClient(String user, String pwd) {
   
      this.user = user;
      this.pwd = pwd;
   
   }

   public String getData(String url, Map args)
      throws SSCException {
     
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
         request.setRequestProperty (
            "Authorization", basicAuth(api_usr, api_pwd));
         
         // Then connect to server
         request.connect();
         
         // Get sensor list from server
         InputStream ssc_in = request.getInputStream();
         String result = toString(ssc_in);
         ssc_in.close();
         
         mangleResponse(request);
         
      } catch (IOException e) {
         // Might be raised by any activity regarding IO activities. 
         
         throw new SSCException(e);
         
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
         
      }
         
      return result;
         
   }
   
   /**
    * Mangle the response code. If everything is fine, code 200, null
    * is returned. If connection failed or server can't handle the
    * request a error is thrown.
    * 
    * @param connection The URLConnection 
    * 
    * @return Null if everything is fine else throw a error
    * @throws SSCException.ConnectionFailed Any code other then 
    * 200.
    * @throws SSCException.Mystery Errors unknown to us.
    */
   
   private void mangleResponse(HttpsURLConnection connection) {
   
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
      
      return;
   
   }
   
   /**
    * Basic authentication using base 64 encoding.
    * 
    * @param user Username
    * @param pwd Password
    * 
    * return Authentication string 
    */ 
   private void basicAuth(String user, String pwd) {
     
      String secret = user + ":" + pwd;
      String auth = "Basic " + 
         DatatypeConverter.printBase64Binary(secret.getBytes());
            
      return auth;
     
   }

   /**
    * This return GET request parameters, that everything after the
    * '?' including the '?' it self. This method do at this moment
    * a very crude work. No checks just line them up. Not critical
    * since we have almost absolute control over what goes here.
    * 
    * @param request Mapped key value parameters
    * 
    * @return The GET request parameters as a string. 
    */
   private String queryString(Map<String, String> request) {
      
      String request_str = "";

      Iterator i = request.entrySet().iterator();
      
      while (i.hasNext()) {
         
         Map.Entry p = (Map.Entry) i.next();
         
         if (p.getValue == null) {
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
    * @throws java.io.UnsupportedEncodingException if this method
    * fails to encode received data as UTF-8
    */
   private String toString(InputStream stream) 
      throws java.io.UnsupportedEncodingException {
     
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      
      byte[] buffer = new byte[1024];
      int length = 0;
         
      while( (length = stream.read(buffer)) > 0 ) {
            
         result.write(buffer, 0, length);
            
      }
      
      return result.toString("UTF-8");
      
   }

}
