
<json:object>
  <json:property name="outer" value="foo"/>
  <json:object name="inner">
    <json:property name="innerProp1" value="val1"/>
    <json:property name="innerProp2" value="val2"/>
    <json:object name="inner2">
      <json:property name="inner2Prop1" value="in2p1"/>
      <json:property name="inner2Prop2" value="in2p2"/>
    </json:object>
  </json:object>
</json:object>

<%--
{
  "outer":"foo",
  "inner":{
    "innerProp1":"val1",
    "innerProp2":"val2",
    "inner2":{
      "inner2Prop1":"in2p1",
      "inner2Prop2":"in2p2"
    }
  }
}

--%>