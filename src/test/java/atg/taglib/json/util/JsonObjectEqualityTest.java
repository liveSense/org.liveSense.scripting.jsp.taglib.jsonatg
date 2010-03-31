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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test JSONObject equality
 *
 * @author James Wiltshire
 * @version $Id$
 */

public class JsonObjectEqualityTest
{
  @Test
  public void testObjectsAreEqual() throws JSONException
  {
    String s1="{foo:'bar',baz:'boz'}";
    String s2="{baz:'boz',foo:'bar'}";
    String s3="{foo:'bar'}";
    JSONObject o1= new JSONObject(s1);
    JSONObject o2= new JSONObject(s2);
    JSONObject o3= new JSONObject(s3);
    assertEquals(o1,o2);
    
    assertEquals(new JSONObject(),new JSONObject());
    
    assertEquals(new JSONObject("{}"),new JSONObject());
    assertEquals(new JSONObject("{   }"),new JSONObject());
    
    assertFalse(o1.equals(new JSONObject()));
    assertFalse((new JSONObject()).equals(o1));
    
    assertFalse(o1.equals(o3));
    assertFalse(o2.equals(o3));
    
    assertFalse(o1.equals(null));
  }
  
  @Test
  public void testNestedObjects() throws JSONException
  {
    String s1="{foo:{bar:\"baz\"},foo2:{biz:true,boz:7}}";
    JSONObject o1= new JSONObject(s1);
    
    assertTrue(o1.equals(new JSONObject(s1)));
    assertTrue(!o1.equals(new JSONObject()));
  }

}

