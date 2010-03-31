<%-- Use atg.taglib.json.escapeXml attribute to set default to false --%>
<% pageContext.setAttribute("atg.taglib.json.escapeXml", new Boolean(false)); %>
<json:object>
  <json:property name="xmlEscaped1" value="<>" escapeXml="true"/>
  <json:property name="xmlEscaped2" escapeXml="true"><></json:property>
  <json:property name="notXmlEscaped1" value="<>" />
  <json:property name="notXmlEscaped2" ><></json:property>
</json:object>