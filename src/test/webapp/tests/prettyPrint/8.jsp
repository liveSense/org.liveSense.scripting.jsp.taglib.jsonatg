<% pageContext.setAttribute("atg.taglib.json.prettyPrint", new Boolean(false)); %>

<json:array>
  <json:property name="foo" value="bar"/>
  <json:property name="baz" value="boz"/>
</json:array>

<%-- Should NOT be pretty-printed --%>