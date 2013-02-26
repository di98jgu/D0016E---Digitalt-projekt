/**
 * 
 * Class: List Sensors
 * 
 * Sense Smart City
 * This class is used to list sensors in your domain (both public 
 * and private). It is also used to list sensors that are marked as 
 * public but owned by other domains. 
 * 
 * This class duplicate the result of ListSensor.py in Java. Native 
 * Java class libraries have been used as far as possible. This 
 * reduce dependence. The only exception is JSON management, it
 * does not exist in standard Java.
 * 
 * JSON is available at github.com/douglascrockford/JSON-java.git
 * 
 * This code have only been tested under Java standard Ed. 6. The 
 * two core classes HttpsURLConnection and JSONArray exist under 
 * Android. Thus the hope is that this code will work under Android 
 * with few or no changes.
 * 
 * The method getParam is very crude, we use raw strings as GET 
 * request parameters. Obviously this should be improved, but for
 * now this is left as is. 
 * 
 * @author Jim Gunnarsson
 */


import java.io.*;

import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import javax.xml.bind.DatatypeConverter;
import javax.net.ssl.*;

public class ListSensors {
   
   // Parameters (change username and password to your API user)
   private static String api_url = "https://ip30.csse.tt.ltu.se/ssc/api/basic/";
   private static String api_fnc = "sensor/list";
   private static String api_usr = "snowtest";
   private static String api_pwd = "ltusnowtester";

   /**
    * Get a list of sensors. Here is a list of forced exceptions and
    * other know possible exceptions. 
    * 
    * @param args Input arguments, not used
    * 
    * @throws java.net.UnknownHostException Most likely a incorrect 
    * url address. 
    * @throws java.net.MalformedURLException Raised by URL if an 
    * unknown protocol is specified.
    * @throws java.security.NoSuchAlgorithmException This is raised 
    * by SSLContext if a unknown algorithm is provided. Here only 
    * 'SSL' is used so this is unlikely.
    * @throws java.security.KeyManagementException SSLContext mehthod
    * init may throw this, unlikely in this case since it is null
    * @throws java.io.IOException Might be raised by any activity 
    * regarding IO activities. 
    */

   public static void main(String[] args) 
      throws 
      java.net.UnknownHostException,
      java.net.MalformedURLException, 
      java.io.IOException,
      java.security.NoSuchAlgorithmException,
      java.security.KeyManagementException 
      
      {
           
      JSONArray sensor_list = new JSONArray();
      Map<String, Object> api_data = new HashMap<String, Object>();
      
      sensor_list.put("12345");
      sensor_list.put("SKE-472478");
      sensor_list.put("SKE-824224");
      
      api_data.put("sensors", sensor_list);
      api_data.put("public", "no");
      api_data.put("format", "json");
      
      // Connect to a self-signed HTTPS server.
      SSLContext ssl_ctxt = SSLContext.getInstance("SSL");
      ssl_ctxt.init(null, trustAll, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctxt.getSocketFactory());
      
      // Please just ignore host
      HttpsURLConnection.setDefaultHostnameVerifier(ignoreHost);
      
      // Prepare https connection
      HttpsURLConnection request = 
         (HttpsURLConnection) new URL(api_url + api_fnc + getParam(api_data)).openConnection();
      request.setDoOutput(false);
      request.setDoInput(true);
      
      // Specify what kind of request we want
      request.setRequestMethod("GET");
      
      // Service is of type json
      request.setRequestProperty("Content-Type", "application/json");
      request.setRequestProperty (
         "Authorization", basicAuth(api_usr, api_pwd));
      
      // Then connect to server
      request.connect();
      
      // Get sensor list from server
      InputStream ssc_in = request.getInputStream();
      String result = streamToString(ssc_in);
      ssc_in.close();
      
      // Did we get it ??
      System.out.println("Response code: " + request.getResponseCode());
      System.out.println("Sensor list: " + result);
      
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
   private static String basicAuth(String user, String pwd) {
      
      
      String secret = user + ":" + pwd;
      String auth = "Basic " + 
         DatatypeConverter.printBase64Binary(secret.getBytes());
            
      return auth;
      
   }
   /**
    * This class is needed to ignore java.io.IOException: HTTPS 
    * hostname wrong error due to self-signed SSL certificate.
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
   
   /**
    * Read stream and return a UTF-8 encoded string. While not yet
    * confirmed it is likely that the returned JSON object is in 
    * UTF-8. This method make no assumptions about content of the 
    * stream and thus tries to encode anything as UTF-8.
    * 
    * @param is A InputStream
    * 
    * @return String encoded in UTF-8
    * @throws java.io.UnsupportedEncodingException if this method
    * fails to encode received data as UTF-8
    */
   private static String streamToString(InputStream is) 
      throws java.io.UnsupportedEncodingException {
      
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      
      byte[] buffer = new byte[1024];
      int length = 0;
      
      try {
         
         while( (length = is.read(buffer)) > 0 ) {
            
            result.write(buffer, 0, length);
            
         }
         
      } catch (IOException e) {
         
         e.printStackTrace();
         
      }
      
      return result.toString("UTF-8");
  
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
   private static String getParam(Map<String, Object> request) {
      
      String token = "?";
      String request_str = "";

      Iterator i = request.entrySet().iterator();
      
      while (i.hasNext()) {
         
         Map.Entry p = (Map.Entry) i.next();
         
         request_str += token + p.getKey() + "=" + p.getValue();
         
         if (token.equals("?")) {
            token = "&";
         }
         
      }
      
      return request_str;
      
   }
   
}
