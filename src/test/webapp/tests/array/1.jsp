<%-- Primitives array test --%>
<json:object>

  <json:array name="array1">
    <json:property>abc</json:property>
    <json:property>def</json:property>
    <json:property>ghi</json:property>
  </json:array>
  
  <json:array name="array2">
    <json:property value="jkl"/>
    <json:property value="mno"/>
    <json:property value="pqr"/>
  </json:array>
  
  <json:array name="array3">
    <json:property value="${true}"/>
    <json:property value="${false}"/>
    <json:property value="${true}"/>
  </json:array>
  
  <json:array name="array4">
    <json:property value="${1}"/>
    <json:property value="${2}"/>
    <json:property value="${3}"/>
  </json:array>  
  
  <json:array name="emptyArray">
  </json:array>  
  
</json:object>

<%-- 
{
  "array1":["abc","def","ghi"],
  "array2":["jkl","mno","pqr"],
  "array3":[true,false,true],
  "array4":[1,2,3],
  "emptyArray":[]
}
--%>