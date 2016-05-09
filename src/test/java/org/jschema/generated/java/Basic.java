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
      else{
        newBasic._fields.put((String) pair.getKey(), pair.getValue());
      }
    }
    return newBasic;
  }
  public static Object makeObject(Basic newBasic, String key, Map value){
    return null;
}
  public String toJSON(){return _fields.toString();}

  public String getName(){return (String) _fields.get("name");}
  public void setName(String name){_fields.put("name", name);}

  public int getAge(){return (int) _fields.get("age");}
  public void setAge(int age){_fields.put("age", age);}


}
