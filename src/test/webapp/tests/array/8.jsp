<%@page import="java.util.ArrayList" %>
<%-- Setup test data --%>
<%
pageContext.setAttribute("emptyList",new ArrayList());
pageContext.setAttribute("emptyArray",new Object[0]);
%>

<%-- Test empty lists with tag body --%>
<json:object> 
  <json:array name="emptyList1" items="${emptyList}" var="item">
    <json:property name="foo" value="bar"/>
  </json:array>
  <json:array name="emptyList2" items="${emptyList}" var="item">
  </json:array>
  <json:array name="emptyList3" items="${emptyList}" var="item"/>
  <json:array name="emptyList4" items="${emptyList}" var="item">
    <json:object>
      <json:property name="foo" value="bar"/>
    </json:object>
  </json:array>
  <json:array name="emptyList5" items="${emptyArray}" var="item">
    <json:object>
      <json:property name="foo" value="bar"/>
    </json:object>
  </json:array>
  <json:array name="emptyList6" items="${null}" var="item">
    <json:object>
      <json:property name="foo" value="bar"/>
    </json:object>
  </json:array>
</json:object>

<%--
{
  "emptyList1": [],
  "emptyList2": [],
  "emptyList3": [],
  "emptyList4": [],
  "emptyList5": [],
  "emptyList6": []
}
--%>