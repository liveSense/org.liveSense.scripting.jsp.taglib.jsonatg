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


import atg.taglib.json.util.JSONObject;

import java.io.StringWriter;
import java.util.Stack;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;


/**
 * Tag that will render a JSON object. This tag is just a wrapper that should contain
 * json:property and json:array tags.
 *
 * @author James Wiltshire
 * @version $Id$
 */

public class JsonObjectTag extends JsonBaseTag
{
  /**
   * Process the tag
   * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
   *
   * @throws JspException
   */
  public void doTag() throws JspException{
    JspFragment body = getJspBody();
    Stack stack=getEntityStack();
    
    // Name shouldn't be set if we're within an array tag
    if (!stack.isEmpty() && getCurrentEntity().isArray() && getName()!=null){
      throw new JspException(Messages.getString("atg.taglib.json.error.object.4")); //$NON-NLS-1$
    }
        
    try{    
      // Create a new JSON object and push it onto the top of the stack
      JsonEntity entity=new JsonEntity(new JSONObject());
      
      stack.push(entity);
      
      // Process the tag body - this will add properties (primitives, objects or arrays) to
      // the JSON Object on the top of the stack
      // Writer passed here is used to prevent any body content being written to output stream
      if (body!=null){
        StringWriter writer = new StringWriter();
        body.invoke(writer);
      }
      
      // Pop the object from the stack, the parent object is now on the top of the stack
      stack.pop();     
      
      // Ensure that we have a name set if the object on the stack is a JSONObject
      if (!stack.isEmpty() && getCurrentEntity().getWrappedObject() instanceof JSONObject &&
          (getName()==null||getName().trim().length()==0)){
        throw new JspException(Messages.getString("atg.taglib.json.error.object.2"));
      }
      
      // Set or add property on parent object - this may be either a JSONObject or a JSONArray
      processTagEnd(entity);
    }
    catch (JspException e){
      // Just rethrow if we got a JspException - it's probably from a nested tag - no new
      // info for us to add here.
      throw e;
    }
    catch (Exception e){
      throw new JspException(Messages.getString("atg.taglib.json.error.object.0"),e);
    }
  }

}

