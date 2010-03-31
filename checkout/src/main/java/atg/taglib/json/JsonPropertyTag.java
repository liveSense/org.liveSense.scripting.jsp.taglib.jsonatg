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


import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;

/**
 * Tag to render a JSON property. This is a simple string/value pair. The value may
 * be set either as a value= attribute of the tag, or it may be contained within the 
 * tag's body.
 * <br/>
 * If the value is set in the body, then it is always assumed to be a String. However,
 * if it is set with a value= attribute, then it may be a Boolean, Integer, Long, 
 *
 * @author James Wiltshire
 * @version $Id$
 */

public class JsonPropertyTag extends JsonBaseTag
{
  protected Object mValue;

  /**
   * Gets the Value
   * @return the Value
   */
  public Object getValue()
  {
    return mValue;
  }

  /**
   * Sets the Value
   *
   * @param pValue The Value to set
   */
  public void setValue(Object pValue)
  {
    mValue = pValue;
  }
  
  /**
   * Process the tag
   * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
   *
   * @throws JspException
   */
  public void doTag() throws JspException{
    JspFragment body = getJspBody();
    
    // json:property tags can't be nested in themselves
    if (getParent()!=null && 
        getParent().getClass().getName().equals("atg.taglib.json.JsonPropertyTag")){
      throw new JspException(Messages.getString("atg.taglib.json.error.property.2")); //$NON-NLS-1$
    }
    
    // property tag must have a name attribute unless within array tag
    if (!getCurrentEntity().isArray() && getName()==null){
      throw new JspException(Messages.getString("atg.taglib.json.error.property.3")); //$NON-NLS-1$
    }
    
    try{            
      // Get the current JSON object from the stack
      JsonEntity entity=getCurrentEntity();
      Object value;
      
      if (getValue()==null){
        // No value attribute has been set, so invoke the tag body. 
        // The tag body should contain textual data.
        
        if (body==null){
          // null value attribute and no tag body so set value to empty string
          value=new String();
        }
        else {
          // Invoke the tag body, capturing the output to a writer
          StringWriter writer = new StringWriter();
          body.invoke(writer);
          value=writer.toString();
        }
      }
      else{
        // Value attribute has been set, so ignore body and use whatever is set 
        // for the value
        value=getValue();
      }
      
      // Trim and xml-escape the value
      value=trimAndEscapeValue(value);
      
      // Put a new property into the JSON object. This performs some validation on the type passed and
      // will throw a JSONException if invalid. Any unrecognized object type will just have toString() 
      // called to obtain a String value.
      entity.add(value,getName());
      
      resetEscapeXmlValue();
      
    }
    catch (JspException e){
      // Just rethrow if we got a JspException - it's probably from a nested tag - no new
      // info for us to add here.
      throw e;
    }
    catch (Exception e){
      throw new JspException(Messages.getString("atg.taglib.json.error.property.1"),e); //$NON-NLS-1$
    }
  }

}

