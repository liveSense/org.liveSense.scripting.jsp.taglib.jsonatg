<%-- Trimming Test --%>
<json:object>
  <json:property name="trimmed1" value="   foo   "/>
  <json:property name="trimmed2">       foo       </json:property>
  <json:property name="trimmed3">
    foo
  </json:property>
  <json:property name="untrimmed1" trim="false" value="  foo  " />
  <json:property name="untrimmed2" trim="false">  foo  </json:property>
</json:object>

<%-- Expected
{
  "trimmed1":"foo",
  "trimmed2":"foo",
  "trimmed3":"foo",
  "untrimmed1":"  foo  ",
  "untrimmed2":"  foo  "
}

--%>