<%@page import="java.util.Arrays" %>
<%-- Setup test data --%>
<%
String[] days={"Mon","Tue","Wed","Thur","Fri","Sat","Sun"};
pageContext.setAttribute("days",Arrays.asList(days));
Boolean[] bools={new Boolean(true),new Boolean(false),new Boolean(true)};
pageContext.setAttribute("bools",Arrays.asList(bools));
Integer[] numbers={new Integer(1),new Integer(2),new Integer(3)};
pageContext.setAttribute("numbers",Arrays.asList(numbers));
%>

<%-- Test arrays using body as template --%>
<json:object> 

  <json:array name="array1" items="${days}" var="item">
    ${item}
  </json:array>
  
  <json:array name="array2" items="${days}" var="item">
    day_${item}
  </json:array>
  
  <json:array name="array3" items="${bools}" var="item">
    <json:property value="${item}"/>
  </json:array>
  
  <json:array name="array4" items="${numbers}" var="item">
    <json:property value="${item}"/>
  </json:array>
  
  <json:array name="array5" items="${numbers}" var="item">
    <json:object>
      <json:property name="id" value="${item}"/>
      <json:property name="foo">val${item}</json:property>
    </json:object>
  </json:array>
  
</json:object>

<%--
{
  "array1":["Mon","Tue","Wed","Thur","Fri","Sat","Sun"],
  "array2":["day_Mon","day_Tue","day_Wed","day_Thur","day_Fri","day_Sat","day_Sun"],
  "array3":[true,false,true],
  "array4":[1,2,3],
  "array5":[
    {"id":1,"foo":"val1"},
    {"id":2,"foo":"val2"},
    {"id":3,"foo":"val3"}
  ]
}
--%>