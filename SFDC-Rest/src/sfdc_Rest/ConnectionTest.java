package sfdc_Rest;


/*
* author:anwesh,
* Description:Salesforce rest api Connection Test with Java,
*
*/

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;

public class ConnectionTest {

   static final String USERNAME     = "anwesh@Sfdc.com";
   static final String PASSWORD     = "@@@@@@";
   static final String LOGINURL     = "https://login.salesforce.com";
   static final String GRANTSERVICE = "/services/oauth2/token?grant_type=password";
   static final String CLIENTID     = "@@@@@@@@";
   static final String CLIENTSECRET = "@@@@@@";

   public static void main(String[] args) {

   	HttpClient httpclient = HttpClientBuilder.create().build();
       // Assemble the login request URL
       String loginURL = LOGINURL + 
                         GRANTSERVICE + 
                         "&client_id=" + CLIENTID + 
                         "&client_secret=" + CLIENTSECRET +
                         "&username=" + USERNAME +
                         "&password=" + PASSWORD;

       // Login requests must be POSTs
       HttpPost httpPost = new HttpPost(loginURL);
       HttpResponse response = null;

       try {
           // Execute the login POST request
           response = httpclient.execute(httpPost);
       } catch (ClientProtocolException cpException) {
           // Handle protocol exception
       } catch (IOException ioException) {
           // Handle system IO exception
       }

       // verify response is HTTP OK
       final int statusCode = response.getStatusLine().getStatusCode();
       if (statusCode != HttpStatus.SC_OK) {
           System.out.println("Error authenticating to Force.com: "+statusCode);
           // Error is in EntityUtils.toString(response.getEntity()) 
           return;
       }

       String getResult = null;
       try {
           getResult = EntityUtils.toString(response.getEntity());
       } catch (IOException ioException) {
           // Handle system IO exception
       }
       JSONObject jsonObject = null;
       String loginAccessToken = null;
       String loginInstanceUrl = null;
       try {
           jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
           loginAccessToken = jsonObject.getString("access_token");
           loginInstanceUrl = jsonObject.getString("instance_url");
       } catch (JSONException jsonException) {
           // Handle JSON exception
       }
       System.out.println(response.getStatusLine());
       System.out.println("Successful login");
       System.out.println("  instance URL: "+loginInstanceUrl);
       System.out.println("  access token/session ID: "+loginAccessToken);

       // release connection
       httpPost.releaseConnection();
   }
   
}

