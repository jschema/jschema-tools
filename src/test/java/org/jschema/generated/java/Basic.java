package org.jschema.generated.java;
import java.util.*;

import org.jschema.parser.*;
public class Basic{
  private Map<String, Object> _fields = new HashMap<String, Object>();

  public static Basic parse(String jsonString){
    Basic newBasic = new Basic();
    Map<String, Object> jsonObject = (Map) new Parser(jsonString).parse();
    Iterator it = jsonObject.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry)it.next();
      if(pair.getValue() instanceof Map){
        Object obj = makeObject(newBasic, (String)pair.getKey(), (Map)pair.getValue());
        newBasic._fields.put((String) pair.getKey(), obj);
      }
      else if(pair.getValue() instanceof List){
        List list = makeList(newBasic, (String)pair.getKey(), (List)pair.getValue());
        newBasic._fields.put((String) pair.getKey(), list);
      }
      else{
        newBasic._fields.put((String) pair.getKey(), pair.getValue());
      }
    }
    return newBasic;
  }
  private static Object makeObject(Basic newBasic, String key, Map value){
    return null;
  }
  private static List makeList(Basic newBasic, String key, List value){
    List<Object> list = new ArrayList<>();
    for(int i = 0; i < value.size(); i++) {
      if(value.get(i) instanceof Map){
        Object result = makeObject(newBasic, key, (Map) value.get(i));
        list.add(result);
      }
      else if(value.get(i) instanceof List){
        List result = makeList(newBasic, key, (List) value.get(i));
        list.add(result);      }
      else{
        list.add(value.get(i));
      }
    }
    return list;
  }
  public java.lang.String toJSON(){return _fields.toString();}

  public java.lang.String getName(){return (java.lang.String) _fields.get("name");}
  public void setName(java.lang.String name){_fields.put("name", name);}

  public java.lang.Integer getAge(){return (java.lang.Integer) _fields.get("age");}
  public void setAge(java.lang.Integer age){_fields.put("age", age);}


}
