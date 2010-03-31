<%@page import="java.util.Date" %>
<%-- Setup test data --%>
<%
pageContext.setAttribute("date",new Date());
%>

<%-- Test empty lists with tag body --%>
<json:object> 
  <json:array name="date" items="${date}" var="item">
  </json:array>
</json:object>

<%--
{
JSP Exception - unable to coerce 'date' to a collection
}
--%>