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

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;


/**
 * Tag to represent a JSON array. The body of this tag should declare the template that will
 * be used to represent each element in the array. Each element in the 'items' collection will
 * be iterated over, and the body of the tag used to render each item in the array.
 * <br/>
 * This tag should contain a single json:object element if each array element is an object,
 * otherwise it should contain textual content, which will create an array of Strings
 *
 * @author James Wiltshire
 * @version $Id$
 */

public class JsonArrayTag extends JsonBaseTag
{
  protected String mVar;

  /**
   * Gets the Var
   * @return the Var
   */
  public String getVar()
  {
    return mVar;
  }

  /**
   * Sets the Var
   *
   * @param pVar The Var to set
   */
  public void setVar(String pVar)
  {
    mVar = pVar;
  }
  
  protected Object mItems; // The raw items as set on the tag

  /**
   * Gets the Items
   * @return the Items
   */
  public Object getItems()
  {
    return mItems;
  }

  /**
   * Sets the Items
   *
   * @param pItems The Items to set
   */
  public void setItems(Object pItems)
  {
    mItems = pItems;
    mItemsPropertySet=true;
  }
  
  /**
   * Raw items coerced to a Collection
   */
  protected Collection mItemsCollection;
  
  /**
   * Flag to signify whether the items property was explicitly set
   */
  protected boolean mItemsPropertySet=false;  
  
  
  /**
   * Process the tag
   * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
   *
   * @throws JspException
   */
  public void doTag() throws JspException{
    JspFragment body = getJspBody();
    
    // Ensure that we have a 'var' attribute if we have a collection of items and a tag body
    if (mItemsPropertySet && getVar()==null && body !=null){
      throw new JspException(Messages.getString("atg.taglib.json.error.array.1"));
    }
        
    try{         
      // Create a new JSON Array and push it on the top of the stack
      JSONArray array = new JSONArray();
      JsonEntity entity = new JsonEntity(array);
      getEntityStack().push(entity);
      
      if (mItemsPropertySet){
        coerceItemsToCollection();
        
        // An items property has been set - if there's anything in it, then iterate over the items
        if (!mItemsCollection.isEmpty()){
          iterateOverItems(array);
        }      
      }
      else {
        // No collection, so just process the body which should add items to the array
        // Writer passed here is used to prevent any body content being written to output stream
        if (body!=null){
          StringWriter writer = new StringWriter();
          body.invoke(writer);
        }
      }
      
      // Pop the array entity off of the stack
      getEntityStack().pop();
      
      // Now add the newly created array to the parent JSON entity
      processTagEnd(entity);    
    }
    catch (JspException e){
      // Just rethrow if we got a JspException - it's probably from a nested tag - no new
      // info for us to add here.
      throw e;
    }
    catch (Exception e){
      throw new JspException(Messages.getString("atg.taglib.json.error.array.0"),e);
    }
  }  

  /**
   * Iterate over the items collection, adding each item to the array either directly or
   * by invoking the tag body.
   *
   * @param pArray The JSON array to add each item to
   * @throws JspException
   * @throws IOException
   */
  private void iterateOverItems(JSONArray pArray) throws JspException, IOException
  {
    JspFragment body = getJspBody();
    
    // Iterator over 'items' collection
    Iterator it = mItemsCollection.iterator();
    while (it.hasNext()){
      Object currentItem=it.next();
              
      if (body==null){
        // If no body has been specified, then just add each item in the 
        // collection directly to the array
        pArray.add(currentItem);
      }
      else{
        // A body has been set, so invoke the body for each item in the collection
      
        // Set the current item in page scope, using value of 'var' as the attribute name
        getJspContext().setAttribute(getVar(), currentItem);
        int arraySizeBeforeInvokingBody = pArray.size();
        
        // Invoke the tag body, capturing the output to a writer
        StringWriter writer = new StringWriter();
        body.invoke(writer);
               
        // Check to see whether any objects were added to the array by the tag body
        // If an object, property or data tag was encountered, it will have added a new 
        // element to the array (which is currently on the top of the stack)
        if (pArray.size() == arraySizeBeforeInvokingBody){
          // Size is the same - nothing has been added to the JSONArray. Treat the tag
          // body as text
          Object value=writer.toString();
          
          // Trim and xml-escape the value
          value=trimAndEscapeValue(value);
          
          // Add the trimmed string value to the array
          pArray.add(value);
        }
        
        // Cleanup 'var' attribute
        getJspContext().removeAttribute(getVar());
      }                   
    } // End of 'items' iteration
  }
  
  /**
   * Coerce the raw items object that has been set to a Collection that we
   * can subsequently iterate over
   * 
   * @throws JspException If unable to coerce the raw items to a Collection
   *
   */
  private void coerceItemsToCollection() throws JspException
  {
    Object o = getItems();
    Collection result;
    if (o==null){
      // Items property on the tag has been set, but the value set with is NULL, so just
      // use an empty list
      result = new ArrayList();
    }       
    else if (
        // Primitive arrays
        (o instanceof boolean[]) ||
        (o instanceof byte[]) ||
        (o instanceof char[]) ||
        (o instanceof short[]) ||
        (o instanceof int[]) ||
        (o instanceof long[]) ||
        (o instanceof float[]) ||
        (o instanceof double[])) {
      result = convertArrayToList(o);
    }
    else if (o instanceof Object[]){
      result = Arrays.asList((Object[])o);
    }
    else if (o instanceof Collection){
      result = (Collection)o;
    }
    else if (o instanceof Map){
      // Use the values of a map - ordering may be undetermined depending on concrete Map implementation used
      result = ((Map)o).values();
    }
    else if (o instanceof String){
      // Comma-separated string
      result = Arrays.asList(((String)o).split(","));
    }
    else {
      String msg=MessageFormat.format(Messages.getString("atg.taglib.json.error.array.2"), 
          new Object[]{o.getClass()});
      throw new JspException(msg);
    }
    
    mItemsCollection=result;
  }
  
  /**
   * Convert a primitive array to a Collection
   *
   * @param pArray The primitive array to convert
   * @return A Collection containing wrapper objects of all the primitive array elements
   */
  private Collection convertArrayToList(Object pArray){
    if (pArray==null){
      // Treat null as an empty list
      return new ArrayList();
    }
    int length = Array.getLength(pArray);
    if (length == 0) {
      // Empty array, so just return an empty list
      return new ArrayList();
    }
    Class wrapperType = Array.get(pArray, 0).getClass();
    Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
    for (int i = 0; i < length; i++) {
      newArray[i] = Array.get(pArray, i);
    }
    return Arrays.asList(newArray);
  }
}

