<%@page import="java.util.ArrayList" %>
<%
pageContext.setAttribute("aCollection", new ArrayList());
%>
<json:object>
  <json:array name="foo" items="${aCollection}">
    TAG BODY
  </json:array>
</json:object>