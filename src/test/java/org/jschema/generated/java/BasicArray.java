package org.jschema.generated.java;
import java.util.*;

import org.jschema.parser.*;
public class BasicArray{
  private List<Map> _list = new ArrayList();

  public static BasicArray parse(String jsonString){
    BasicArray newBasicArray = new BasicArray();
    List<Object> jsonList = (List) new Parser(jsonString).parse();
    for(int i = 0; i < jsonList.size(); i++) {
      if(jsonList.get(i) instanceof Map){
        Map<String, Object> innermap = new HashMap<>();
        Iterator it = ((Map) jsonList.get(i)).entrySet().iterator();
        while (it.hasNext()){
          Map.Entry pair = (Map.Entry) it.next();
          if(pair.getValue() instanceof Map){
            Object obj = makeObject(newBasicArray, (String)pair.getKey(), (Map)pair.getValue());
            innermap.put((String) pair.getKey(), obj);
          }
          else if(pair.getValue() instanceof List){
            List list = makeList(newBasicArray, (String)pair.getKey(), (List)pair.getValue());
            innermap.put((String) pair.getKey(), list);
          }
          else{
            innermap.put((String) pair.getKey(), pair.getValue());
          }
        }
        newBasicArray._list.add(innermap);
      }
    }
    return newBasicArray;
  }
  public static Object makeObject(BasicArray newBasicArray, String key, Map value){
    return null;
  }
  public static List makeList(BasicArray newBasicArray, String key, List value){
    List<Object> list = new ArrayList<>();
    for(int i = 0; i < value.size(); i++) {
      if(value.get(i) instanceof Map){
        Object result = makeObject(newBasicArray, key, (Map) value.get(i));
        list.add(result);
      }
      else if(value.get(i) instanceof List){
        List result = makeList(newBasicArray, key, (List) value.get(i));
        list.add(result);      }
      else{
        list.add(value.get(i));
      }
    }
    return list;
  }
  public java.lang.String toJSON(){return _list.toString();}

  public java.lang.String getName(int index){return (java.lang.String) _list.get(index).get("name");}
  public void setName(int index, java.lang.String name){_list.get(index).put("name", name);}

  public java.lang.Integer getAge(int index){return (java.lang.Integer) _list.get(index).get("age");}
  public void setAge(int index, java.lang.Integer age){_list.get(index).put("age", age);}


}
