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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.MessageFormat;

import org.apache.commons.httpclient.HttpStatus;

/**
 * Helper class to run tag tests
 *
 * @author James Wiltshire
 * @version $Id$
 */

public class JsonTestRunner
{
  private String testType;
  private int testNum;
  
  public JsonTestRunner(String pTestType, int pTestNum)
  {
    this.testType=pTestType;
    this.testNum=pTestNum;
  }
  
  public void runTest() throws Exception
  {
    try {
      String msg;
      
      msg=MessageFormat.format("Running test {0}/{1}... ",testType,testNum);
      System.out.println(msg);
      
      ResponseData data = Helper.getData(testType,testNum);
      
      msg=MessageFormat.format("{0}/{1} - Status {2} is expected.",
          testType,testNum,data.expectedStatusCode);
      
      assertEquals(msg,data.expectedStatusCode,data.statusCode);
      
      if (data.statusCode==HttpStatus.SC_OK){
        // Compare JSON objects
        msg=MessageFormat.format("{0}/{1} - JSON Objects should match.",testType,testNum); 
        System.out.print(msg);
        assertEquals(msg,data.expectedJson, data.json);
      }
      else {  
        String expectedMsg=Messages.getString(data.expectedMsgKey);
        // If the expected Msg has a substitued value, just check the returned string
        // up to that point - we can't know what value will ctually be substituted
        if (expectedMsg.contains("{0}")){
          expectedMsg=expectedMsg.substring(0,expectedMsg.indexOf("{0}"));
        }
        msg=MessageFormat.format("{0}/{1} - Exception should contain key {2} - \"{3}\"",
            testType,testNum,data.expectedMsgKey,expectedMsg); 
        System.out.print(msg);
        assertTrue(msg,data.body.contains(expectedMsg));
      }
      System.out.println(" OK");
    }
    catch (Exception e){
      String msg=MessageFormat.format("{0}/{1} - Exception occurred during test.",
          testType,testNum);
      System.out.println(msg);
      System.out.println(e);
      e.printStackTrace(System.out);
      throw new Exception (msg,e);
    }
  }

}

