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

package atg.taglib.json.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for JSONArray equality testing
 *
 * @author James Wiltshire
 * @version $Id$
 */

public class JsonArrayEqualityTest
{
  @Test
  public void testArrays() throws JSONException
  {
    String s1="{foo:[]}";
    String s2="{foo:[1,2,3]}";
    String s3="{foo:[\"a\",\"b\",\"c\"]}";
    String s4="{foo:[{bar:\"baz\",biz:\"boz\"},{bar2:\"baz2\",biz2:\"boz2\"}]}";
      
    JSONObject o1= new JSONObject(s1);
    JSONObject o2= new JSONObject(s2);
    JSONObject o3= new JSONObject(s3);
    JSONObject o4= new JSONObject(s4);
    
    assertFalse(o1.equals(o2));
    assertFalse(o1.equals(o3));
    assertFalse(o1.equals(o4));
    assertFalse(o2.equals(o3));
    assertFalse(o2.equals(o4));
    assertFalse(o3.equals(o4));
    
    
    assertFalse(o1.equals(new JSONObject()));
    assertFalse(o2.equals(new JSONObject()));
    assertFalse(o3.equals(new JSONObject()));
    assertFalse(o4.equals(new JSONObject()));
    
    assertTrue(o1.equals(new JSONObject(s1)));
    assertTrue(o2.equals(new JSONObject(s2)));
    assertTrue(o3.equals(new JSONObject(s3)));
    assertTrue(o4.equals(new JSONObject(s4)));
    
    assertFalse(o1.equals(null));
      
  }

}

