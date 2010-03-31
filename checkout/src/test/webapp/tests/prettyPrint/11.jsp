<% pageContext.setAttribute("atg.taglib.json.prettyPrint", new Boolean(true)); %>

<json:array prettyPrint="false">
  <json:property name="foo" value="bar"/>
  <json:property name="baz" value="boz"/>
</json:array>

<%-- Should be NOT pretty-printed --%>