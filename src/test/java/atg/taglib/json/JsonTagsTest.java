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

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * Main test class for running all tag tests. 
 *
 * @author James Wiltshire
 * @version $Id$
 */

public class JsonTagsTest
{
  private Server mContainer;
  private PrintStream mSystemError;
  
  
  /**
   * Run tests for each type of tag. This method will invoke the JSP in a Jetty container
   * for each test, and compare the results to the expected results.
   *
   * @throws Exception
   */
  @Test
  public void testTags() throws Exception
  { 
    try{
      startContainer();
      runTests("property",5);
      runTests("object",6);
      runTests("array",15); 
      runTests("misc",7);
      runPrettyPrintingTests(11);
    }
    catch (Exception e){
      // Just rethrow - will be handled by test container
      System.out.println(e);
      e.printStackTrace(System.out);
      throw e;
    }
    finally{
      stopContainer();
    }
  }
  
  private void runTests(String pType, int pNumTests) throws Exception{
    for (int i=1; i<=pNumTests; i++){
      JsonTestRunner runner = new JsonTestRunner(pType,i);
      runner.runTest();    
    }
  }
  
  private void runPrettyPrintingTests(int pNumTests) throws Exception
  {
    for (int i=1; i<=pNumTests; i++){     
      ResponseData data = Helper.getData("prettyPrint",i);
      // Tests 1-3 should contain no spaces, 4-5 should contain spaces,
      // 6,7,8 should NOT contain spaces, 9-10 should contain spaces
      boolean spacesExpected=(i==4||i==5||i==9||i==10);
      boolean result=(data.body.contains(" ")==spacesExpected);
      String msg="prettyPrint/"+i+" - should "+(spacesExpected?"":"NOT ")+"contain spaces";
      System.out.print(msg);
      assertTrue(msg, result );
      System.out.println(" ...OK");
    }
  }
  
  public void startContainer() throws Exception
  {
    String webappDir=System.getProperty("webappDir");
    String suppressContainerErrors=System.getProperty("suppressContainerErrors");
    int containerPort=Integer.parseInt(System.getProperty("containerPort"));
    
    System.out.println("Starting Container - using webapp dir ["+webappDir+"]");
    mContainer = new Server(containerPort);
    WebAppContext context = new WebAppContext(webappDir,"/json-taglib");
    mContainer.setHandler(context);
    mContainer.start();
    
    // Swallow all the errors that will be pumped out by the container. These are
    // expected by the tests
    mSystemError = System.err; // Hold on to the original value
    if (suppressContainerErrors!=null && 
        (suppressContainerErrors.equals("true")||suppressContainerErrors.equals("yes"))){
      System.setErr(new PrintStream(new ByteArrayOutputStream()));
    }
  }
  
  public void stopContainer() throws Exception
  {
    System.out.println("Stopping container");
    //  restore sys err
    System.setErr(mSystemError);
    mContainer.stop();
    // Give container a moment to close sockets before exiting (prevents occassional SocketAccept
    // interrupted exception in build log)
    Thread.sleep(500);
  }

}

