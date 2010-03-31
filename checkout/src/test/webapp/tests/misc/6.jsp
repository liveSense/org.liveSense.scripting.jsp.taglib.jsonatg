<%-- Explicitly set escapeXml to true --%>
<json:object escapeXml="true">
  <json:property name="escaped" value="<>"/>
  <json:object name="nested1" escapeXml="false">
    <json:property name="notEscaped" value="<>"/>
    <json:object name="nested2" escapeXml="true">
      <json:property name="escaped" value="<>"/>
      <json:object name="nested3" escapeXml="false">
        <json:property name="notEscaped" value="<>"/>
      </json:object>
    </json:object>
  </json:object>
</json:object>