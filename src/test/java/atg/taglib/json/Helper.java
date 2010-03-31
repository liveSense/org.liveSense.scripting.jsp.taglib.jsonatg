/**
 * Copyright 2007 Art Technology Group, Inc (ATG)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */


package atg.taglib.json;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Helper class for running taglib tests
 *
 * @author James Wiltshire
 * @version $Id$
 */

public class Helper
{
  private static final String SERVER_URL=
    "http://localhost:"+System.getProperty("containerPort")+"/json-taglib/";
  
  /**
   * Get a response from the server for a test
   *
   * @param pTestUrl The test url, relative to the tests context root
   * @return the <code>ResponseData</code> object for this test
   * @throws IOException 
   * @throws HttpException 
   * @throws JSONException 
   */
  public static ResponseData getData(String pType, int pTestNum) throws HttpException, IOException, JSONException{
    // Setup HTTP client
    HttpClient client = new HttpClient();
    HttpMethod method = new GetMethod(getTestUrl(pType,pTestNum));
    
    // Execute the GET request, capturing response status
    int statusCode=client.executeMethod(method);
    String responseBody=method.getResponseBodyAsString();
    method.releaseConnection();
    
    // Create response data object
    ResponseData data=new ResponseData();
    data.body=responseBody.trim();
    data.statusCode=statusCode;
    if (statusCode==HttpStatus.SC_OK){
      // Parse the JSON response
      data.json=parseJsonTextToObject(responseBody);
    }
    
    // Get the expected result
    method = new GetMethod(getStatus200ResultUrl(pType,pTestNum));
    data.expectedStatusCode=HttpStatus.SC_OK;
    
    // Execute the GET request, capturing response status
    statusCode=client.executeMethod(method);
    
    if (statusCode==HttpStatus.SC_NOT_FOUND){
      // Test result wasn't found - we must be expecting an error for this test
      method.releaseConnection();
      method = new GetMethod(getStatus500ResultUrl(pType,pTestNum));
      statusCode=client.executeMethod(method);
      data.expectedStatusCode=HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }
    
    responseBody=method.getResponseBodyAsString().trim();
    method.releaseConnection();
    
    // Parse the expected data   
    if (data.expectedStatusCode==HttpStatus.SC_OK){
      // Parse body into JSON object
      data.expectedJson=parseJsonTextToObject(responseBody);
    }
    else{
      // Exception is expected, set the expected key
      data.expectedMsgKey=responseBody.trim();
    }  

    return data;
  }
  
  /**
   * Get the URL to invoke for a particular test
   *
   * @param pType
   * @param pTestNum
   * @return
   */
  private static String getTestUrl(String pType, int pTestNum)
  {
    return SERVER_URL+"tests/"+pType+"/"+pTestNum+".jsp";
  }
  
  /**
   * Get the URL to receive the response for a test that has a status
   * of 200 OK
   *
   * @param pType
   * @param pTestNum
   * @return
   */
  private static String getStatus200ResultUrl(String pType, int pTestNum)
  {
    return SERVER_URL+"results/200_OK/"+pType+"/"+pTestNum+".jsp";
  }
  
  /**
   * Get the URL to receive the response for a test that has a status
   * of 500 SERVER_ERROR
   *
   * @param pType
   * @param pTestNum
   * @return
   */
  private static String getStatus500ResultUrl(String pType, int pTestNum)
  {
    return SERVER_URL+"results/500_ERROR/"+pType+"/"+pTestNum+".jsp";
  }
  
  private static Object parseJsonTextToObject(String pJsonText) throws JSONException
  {
    if (pJsonText.trim().startsWith("[")){
      // Json Array
      return new JSONArray(pJsonText);
    }

    // Assume JSON object
    return new JSONObject(pJsonText);
  }

}

