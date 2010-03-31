<%-- Default escapeXml to false --%>
<json:object escapeXml="false">
  <json:property name="xmlEscaped1" value="<>" escapeXml="true"/>
  <json:property name="xmlEscaped2" escapeXml="true"><></json:property>
  <json:property name="notXmlEscaped1" value="<>" />
  <json:property name="notXmlEscaped2" ><></json:property>
</json:object>