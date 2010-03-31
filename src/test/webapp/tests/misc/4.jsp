<%-- Explicitly set escapeXml to true --%>
<json:object escapeXml="true">
  <json:property name="xmlEscaped1" value="<>"/>
  <json:property name="xmlEscaped2"><></json:property>
  <json:property name="notXmlEscaped1" value="<>" escapeXml="false"/>
  <json:property name="notXmlEscaped2" escapeXml="false"><></json:property>
</json:object>