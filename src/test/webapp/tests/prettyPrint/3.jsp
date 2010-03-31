<% pageContext.setAttribute("atg.taglib.json.prettyPrint", new Boolean(false)); %>

<json:object>
  <json:property name="foo" value="bar"/>
  <json:property name="baz" value="boz"/>
</json:object>

<%-- Should NOT be pretty-printed --%>