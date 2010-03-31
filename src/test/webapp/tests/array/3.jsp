<%-- Object array test --%>
<json:object>

  <json:array name="objArray">
    <json:object>
      <json:property name="prop1" value="val1"/>
      <json:property name="prop2" value="val2"/>
    </json:object>
    <json:object>
      <json:property name="prop3" value="val3"/>
      <json:property name="prop4" value="val4"/>
    </json:object>
    <json:object>
      <json:property name="prop5" value="val5"/>
      <json:property name="prop6" value="val6"/>
    </json:object>
  </json:array>  
  
</json:object>

<%-- 
{
  "objArray":[
  {
    "prop1":"val1",
    "prop2":"val2"
  },
  {
    "prop3":"val3",
    "prop4":"val4"
  },
  {
    "prop5":"val5",
    "prop6":"val6"
  }
  ]
}
--%>