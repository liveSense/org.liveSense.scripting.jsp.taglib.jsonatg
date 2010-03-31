<%@page import="java.util.*" %>
<%-- Setup test data --%>
<%
// Primitive Wrapper types
String[] days={"Mon","Tue","Wed","Thur","Fri","Sat","Sun"};
pageContext.setAttribute("days",Arrays.asList(days));
Boolean[] bools={new Boolean(true),new Boolean(false),new Boolean(true)};
pageContext.setAttribute("bools",Arrays.asList(bools));
Integer[] numbers={new Integer(1),new Integer(2),new Integer(3)};
pageContext.setAttribute("numbers",Arrays.asList(numbers));

// Object Array
pageContext.setAttribute("objectArray",days);

// Primitive Arrays
pageContext.setAttribute("booleanArray",new boolean[]{true,false,true});
pageContext.setAttribute("byteArray",new byte[]{1,2,3});
pageContext.setAttribute("charArray",new char[]{'a','b','c'});
pageContext.setAttribute("shortArray",new short[]{3,2,1});
pageContext.setAttribute("intArray",new int[]{3,2,1});
pageContext.setAttribute("longArray",new long[]{3,2,1});
pageContext.setAttribute("floatArray",new float[]{1.5f,2.5f,3.5f});
pageContext.setAttribute("doubleArray",new double[]{1.5,2.5,3.5});

// Maps
HashMap map= new LinkedHashMap();
map.put("key1","val1");
map.put("key2","val2");
map.put("key3","val3");
pageContext.setAttribute("map",map);

// CSV String
pageContext.setAttribute("csv","ab,cd,ef,gh");

%>

<%-- Test arrays using lists --%>
<json:object> 
  <json:array name="days" items="${days}"/>
  <json:array name="bools" items="${bools}"/>
  <json:array name="numbers" items="${numbers}"/>
  
  <json:array name="objectArray" items="${days}"/>
  
  <json:array name="booleanArray" items="${booleanArray}"/>
  <json:array name="byteArray" items="${byteArray}"/>
  <json:array name="charArray" items="${charArray}"/>
  <json:array name="shortArray" items="${shortArray}"/>
  <json:array name="intArray" items="${intArray}"/>
  <json:array name="longArray" items="${longArray}"/>
  <json:array name="floatArray" items="${floatArray}"/>
  <json:array name="doubleArray" items="${doubleArray}"/>
  
  <json:array name="map" items="${map}"/>
  
  <json:array name="csv" items="${csv}"/>
  
</json:object>

<%--
{ 
  "days": [ "Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun" ], 
  "bools": [ true, false, true ], 
  "numbers": [ 1, 2, 3 ], 
  "objectArray": [ "Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun" ], 
  "booleanArray": [ true, false, true ], 
  "byteArray": [ 1, 2, 3 ], 
  "charArray": [ "a", "b", "c" ], 
  "shortArray": [ 3, 2, 1 ], 
  "intArray": [ 3, 2, 1 ], 
  "longArray": [ 3, 2, 1 ], 
  "floatArray": [ 1.5, 2.5, 3.5 ], 
  "doubleArray": [ 1.5, 2.5, 3.5 ],
  "map": ["val1","val2","val3"],
  "csv": ["ab","cd","ef","gh"]
} 

--%>