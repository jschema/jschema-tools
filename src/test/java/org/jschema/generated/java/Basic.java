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
      if(pair.getValue() instanceof HashMap){
        newBasic._fields.put((String) pair.getKey(), parseInnerMap(newBasic, pair.getKey(), (Map) pair.getValue()));
      }
      else if(pair.getValue() instanceof ArrayList && ((ArrayList) pair.getValue()).get(0) instanceof HashMap){
        newBasic._fields.put((String) pair.getKey(), parseInnerList(newBasic, pair.getKey(), (List) pair.getValue()));
      }
      else newBasic._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newBasic;
  }
  public static Object parseInnerMap(Basic newBasic, Object key, Map value){
    return null;
}
  public static List parseInnerList(Basic newBasic, Object key, List value){
    List<Object> list = new ArrayList<>();
    for(int i = 0; i < value.size(); i++) {
      Object result = parseInnerMap(newBasic, key, (Map) value.get(i));
      list.add(result);
    }
    return list;
  }
  public String toJSON(){return _fields.toString();}

  public String getName(){return (String) _fields.get("name");}
  public void setName(String name){_fields.put("name", name);}

  public int getAge(){return (int) _fields.get("age");}
  public void setAge(int age){_fields.put("age", age);}


}
