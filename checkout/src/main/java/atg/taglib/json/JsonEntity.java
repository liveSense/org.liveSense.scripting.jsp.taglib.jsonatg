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

/**
 * Wrapper object for JSON Entities. A JSON Entity can be either a JSONObject or
 * JSONArray. One of these object types will always be sitting on the top of the
 * object stack, so this class provides a wrapper that can be used to either add
 * a property to an object or append a property to an array.
 *
 * @author James Wiltshire
 * @version $Id$
 */

public class JsonEntity
{
  private Object mWrappedObject;
  
  /**
   * Create a new JsonEntity object
   *
   * @param pWrapped The underlying object to wrap
   */
  public JsonEntity(Object pWrapped){
    this.mWrappedObject=pWrapped;
  }
  
  /**
   * Add a named entity to a JSONObject or append an entity to a JSONArray
   * depending on whether the wrapped object is an object or array.
   *
   * @param pEntity The entity to add to the wrapped object
   * @param pName The property name of the entity, or <code>null</code> if not available
   * @throws JSONException
   */
  public void add(Object pEntity,String pName) throws JSONException{
    if (mWrappedObject instanceof JSONObject){
      if (pName==null){
        throw new JSONException("Unable to add to JSONObject - property name is required.");
      }
      ((JSONObject)mWrappedObject).put(pName, pEntity);
    }
    else if (mWrappedObject instanceof JSONArray){
      ((JSONArray)mWrappedObject).add(pEntity);
    }
  }
  
  /**
   * Get the underlying wrapped JSONObject or JSONArray object
   *
   * @return The wrapped object
   */
  public Object getWrappedObject()
  {
    return mWrappedObject;
  }
  
  /**
   * Is this entity wrapping a JSONArray?
   *
   * @return <code>true</code> if this entity wraps a JSONArray, <code>false</code> otherwise
   */
  public boolean isArray()
  {
    return (mWrappedObject instanceof JSONArray);
  }
  
  /**
   * Is this entity wrapping a JSONObject?
   *
   * @return <code>true</code> if this entity wraps a JSONObject, <code>false</code> otherwise
   */
  public boolean isObject()
  {
    return (mWrappedObject instanceof JSONObject);
  }
  
  /**
   * Serialize the underlying wrapped entity to a JSON string
   * @see java.lang.Object#toString()
   *
   * @return The wrapped JSON entity as a string
   */
  public String toString()
  {
    return mWrappedObject.toString();
  }
  
  /**
   * Serialize the underlying wrapped entity to a JSON string
   *
   * @param pIndentFactor The number of spaces to use to pretty-print the data
   * @return The wrapped JSON entity as a string
   */
  public String toString(int pIndentFactor) throws JSONException
  {
    if (isArray()){
      return ((JSONArray)mWrappedObject).toString(pIndentFactor);
    }
    else if (isObject()){
      return ((JSONObject)mWrappedObject).toString(pIndentFactor);
    }
    else{
      // Should never happen
      return "";
    }
  }

}

