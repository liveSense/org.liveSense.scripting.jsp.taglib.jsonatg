<json:object>

  <json:array name="array1">
    <json:object name="foo">
      <json:property name="bar" value="baz"/>
    </json:object>
  </json:array>

</json:object>

<%--
JspEsception: json:object should not have name attribute when nested in json:array
--%>