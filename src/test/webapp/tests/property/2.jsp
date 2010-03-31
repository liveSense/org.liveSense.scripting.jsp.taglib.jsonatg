<%-- Primitive values test --%>
<json:object>
  <json:property name="boolT" value="${true}"/>
  <json:property name="boolF" value="${false}"/>
  <json:property name="string1" value="true"/>
  <json:property name="string2" value="false"/>
  <json:property name="numeric1" value="${1+2}"/>
  <json:property name="numeric2" value="${-500}"/>
  <json:property name="numeric3" value="${123.456}"/>
</json:object>

<%-- Expected
{
  "boolT":true,
  "boolF":false,
  "string1":"true",
  "string2":"false",
  "numeric1":3,
  "numeric2":-500
  "numeric3":123.456
}
--%>