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

import atg.taglib.json.util.JSONException;

import java.util.Stack;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Abstract base class for all JSON tags
 *
 * @author James Wiltshire
 * @version $Id$
 */

public abstract class JsonBaseTag extends SimpleTagSupport
{
  private static final String JSON_OBJECT_STACK_KEY = "atg.taglib.json.objectStack";
  private static final String PRETTY_PRINT_KEY = "atg.taglib.json.prettyPrint";
  private static final String ESCAPE_XML_KEY="atg.taglib.json.escapeXml";
  private static final String CURRENT_ESCAPE_XML_VALUE_KEY="atg.taglib.json.escapeXml.currentValue";
  
  private static final int JSON_OBJECT_STACK_SCOPE = PageContext.REQUEST_SCOPE;
  protected static final int PRETTY_PRINT_INDENT=2;
  
  private static final boolean ESCAPE_XML_DEFAULT=true;
  private static final boolean TRIM_DEFAULT = true;
  private static final boolean PRETTY_PRINT_DEFAULT=false;
  
  protected String mName;

  /**
   * Gets the Name
   * @return the Name
   */
  public String getName()
  {
    return mName;
  }

  /**
   * Sets the Name
   *
   * @param pName The Name to set
   */
  public void setName(String pName)
  {
    mName = pName;
  }
  
  protected boolean mTrim = TRIM_DEFAULT;

  /**
   * Gets the Trim
   * @return the Trim
   */
  public boolean getTrim()
  {
    return mTrim;
  }

  /**
   * Sets the Trim
   *
   * @param pTrim The Trim to set
   */
  public void setTrim(boolean pTrim)
  {
    mTrim = pTrim;
  } 
  
  protected boolean mNewStackCreated=false;
  
  /**
   * Sets the PrettyPrint flag. If set, then any JSON output should be nicely formatted
   *
   * @param pPrettyPrint The PrettyPrint to set
   */
  public void setPrettyPrint(boolean pPrettyPrint)
  {
    getJspContext().setAttribute(PRETTY_PRINT_KEY, Boolean.valueOf(pPrettyPrint));
  }
  
  /**
   * Get the number of spaces that should be used to indent the pretty-printed output,
   * or 0 if no pretty-printing should be used
   *
   * @return The number of spaces to indent when pretty printing
   */
  public int getPrettyPrintIndentFactor()
  {
    boolean prettyPrint=getBooleanDefaultValue(PRETTY_PRINT_KEY, Boolean.valueOf(PRETTY_PRINT_DEFAULT));
    return (prettyPrint?PRETTY_PRINT_INDENT:0);
  }

  protected Boolean mEscapeXmlOriginalValue=null;
  protected boolean mEscapeXmlValueSet=false;
  
  /**
   * Gets the value of the EscapeXml flag. This can be set on any tag by setting the
   * <code>escapeXml</code> attribute to <code>true</code>, or it can be set by setting
   * a page attribute <code>atg.taglib.json.escapeXml</code>. When set on a tag it will
   * recursively apply to all sub-tags, but may be overriden by any sub-tag.
   * <br/>
   * Defaults to <code>true</code>
   * 
   * @return the EscapeXml flag value
   */
  public boolean getEscapeXml()
  {
    Boolean escapeXml=(Boolean)getJspContext().getAttribute
      (CURRENT_ESCAPE_XML_VALUE_KEY,PageContext.REQUEST_SCOPE);
    if (escapeXml==null){
      escapeXml = getEscapeXmlDefault();
    }
    return escapeXml.booleanValue();
  }

  /**
   * Sets the EscapeXml flag. If set, then any output will be xml-escaped
   *
   * @param pEscapeXml The EscapeXml to set
   */
  public void setEscapeXml(boolean pEscapeXml)
  {
    mEscapeXmlValueSet=true;
    mEscapeXmlOriginalValue=(Boolean)getJspContext().getAttribute
      (CURRENT_ESCAPE_XML_VALUE_KEY,PageContext.REQUEST_SCOPE);
    getJspContext().setAttribute
      (CURRENT_ESCAPE_XML_VALUE_KEY, Boolean.valueOf(pEscapeXml),PageContext.REQUEST_SCOPE);
  }
  
  /**
   * Get the default value of the escapeXml property
   *
   * @return the default escapeXml value
   */
  public Boolean getEscapeXmlDefault()
  {
    return Boolean.valueOf(getBooleanDefaultValue(ESCAPE_XML_KEY, Boolean.valueOf(ESCAPE_XML_DEFAULT)));
  }
  
  /**
   * Replace the escapeXml value with the original value if this tag modified it
   *
   */
  protected void resetEscapeXmlValue()
  {
    if (mEscapeXmlValueSet){
      getJspContext().setAttribute
        (CURRENT_ESCAPE_XML_VALUE_KEY, mEscapeXmlOriginalValue, PageContext.REQUEST_SCOPE);
    }
  }
  
  
  /**
   * Get the JSON object stack. This is stored as an attribute in the
   * page context. Request scope is used so that jsp:include can be used
   * to nest json: tags in included pages.
   * <p>
   * If an entity stack does not exists, then this method will create a new one
   * 
   * 
   * @return The JSON Entity Stack
   * @exception JspException If there is a problem creating a new entity stack
   */
  protected Stack getEntityStack() throws JspException{
    Stack stack=(Stack)getJspContext().getAttribute(JSON_OBJECT_STACK_KEY, JSON_OBJECT_STACK_SCOPE);
    if (stack==null){
      stack = createEntityStack();
      mNewStackCreated=true;
    }
    return stack;
  }
  
  /**
   * Create a new entity stack and set it in pageContext
   *
   * @return The newly created JSON entity statck
   */
  protected Stack createEntityStack() throws JspException{      
    Stack stack = new Stack();
    getJspContext().setAttribute(JSON_OBJECT_STACK_KEY, stack, JSON_OBJECT_STACK_SCOPE);
    return stack;
  }
  
  /**
   * Remove the entity stack object from page context
   *
   */
  protected void removeEntityStack(){
    getJspContext().removeAttribute(JSON_OBJECT_STACK_KEY, JSON_OBJECT_STACK_SCOPE);
  }
  
  /**
   * Get the topmost JSONObject or JSONArray from the stack
   *
   * @return the topmost <code>JsonEntity</code> on the stack
   * @throws JspException if unable to get the object stack
   */
  protected JsonEntity getCurrentEntity() throws JspException{
    return (JsonEntity)getEntityStack().peek();
  }
  
  /**
   * Does an entity stack exist?
   *
   * @return <code>true</code> if an entity stack is found in pageContext, <code>false</code> otherwise
   */
  protected boolean entityStackExists()
  {
    return (getJspContext().getAttribute(JSON_OBJECT_STACK_KEY, JSON_OBJECT_STACK_SCOPE)!=null);
  }
  
  /**
   * Is this tag the root <code>json:</code> tag?
   *
   * @return <code>true</code> if this tag instance is the root tag, <code>false</code> otherwise
   */
  protected boolean isRootTag()
  {
    // If a new stack was created by this tag instance, then it's the top level tag
    return mNewStackCreated;
  }
  
  /**
   * Get the default value for a parameter. This will search each of the following locations in order,
   * returning the first value that it finds.
   * <ol>
   *   <li>page/request/session/application attributes. Uses findAttribute to try and find the attribute</li>
   *   <li>Servlet Context initParams - as set in a web.xml context-param</li>
   *   <li>The Default Constant value passed in to this method</li>
   * </ol>
   *
   * @param pParamKey The key of the param.
   * @return
   */
  protected Object getDefaultValue(String pParamKey, Object pDefaultConstant)
  {
    Object value;
    
    // Attribute
    value=getJspContext().findAttribute(pParamKey);
    if (value!=null){
      return value;
    }
    
    // Servlet Context init param
    ServletContext servletContext=((PageContext)getJspContext()).getServletContext();
    value=servletContext.getInitParameter(pParamKey);
    if (value!=null){
      return value;
    }
    
    // Default Constant
    return pDefaultConstant;
  }
  
  /**
   * Get the default value of a parameters as a boolean
   *
   * @param pParamKey
   * @param pDefaultConstant
   * @return
   */
  private boolean getBooleanDefaultValue(String pParamKey, Object pDefaultConstant)
  {
    Object defaultValue=getDefaultValue(pParamKey, pDefaultConstant);
    if (defaultValue instanceof String){
      return Boolean.valueOf((String)defaultValue).booleanValue();
    }
    else if (defaultValue instanceof Boolean){
      return ((Boolean)defaultValue).booleanValue();
    }
    else{
      return false;
    }
  }
  
  /**
   * Trim and EscapeXml chars in the value depending on whether the <code>trim</code> and
   * <code>escapeXml</code> attributes have been set
   *
   * @param pValue The value to trim and xml-escape
   * @return The processed value
   */
  protected Object trimAndEscapeValue(Object pValue)
  {
    // If the value isn't a string, just return as we can't process it
    if (pValue instanceof String == false){
      return pValue;
    }
    
    String result=(String)pValue;
    
    
    // Trim if trim attribute is set
    if (getTrim()){
      result=result.trim();
    } 
    
    // Escape all XML characters (as defined in JSTL1.1 spec, page 23)
    if (getEscapeXml()){
      result=result
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")        
        .replaceAll("'", "&#039;")
        .replaceAll("\"", "&#034;");
    }
    
    return result;
  }
  
  /**
   * Process the end of a tag. Check to see if this is the top level tag that created the
   * entity stack, and if so, render the JSON data to the output stream.
   * 
   * @param pNewEntity The entity that has just been created by this tag 
   * @throws JspException 
   * @throws JSONException 
   *
   */
  protected void processTagEnd(JsonEntity pNewEntity) throws JspException, JSONException
  {        
    if (isRootTag()){
      try{
        JspWriter out = getJspContext().getOut();
        
        // Render JSON data to output stream, pretty printing if prettyPrintIndentFactor is > 0
        String jsonText=(getPrettyPrintIndentFactor()>0)?
            pNewEntity.toString(getPrettyPrintIndentFactor()) : pNewEntity.toString();
        out.write(jsonText);
      }
      catch (Exception e){
        throw new JspException(Messages.getString("atg.taglib.json.error.base.0"),e);
      }
      finally{
        // Cleanup
        removeEntityStack();
      }      
    }
    else{
      // This tag instance isn't the top level tag, so add the newly created entity to the parent
      // object on the top of the entity stack
      getCurrentEntity().add(pNewEntity.getWrappedObject(), getName());
    }
    
    resetEscapeXmlValue();    
  }
}
