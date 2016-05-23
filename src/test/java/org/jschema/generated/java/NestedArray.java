package org.jschema.generated.java;
import java.util.*;

import org.jschema.parser.*;
public class NestedArray{
  private List<Map> _list = new ArrayList();

  public static NestedArray parse(String jsonString){
    NestedArray newNestedArray = new NestedArray();
    List<Object> jsonList = (List) new Parser(jsonString).parse();
    for(int i = 0; i < jsonList.size(); i++) {
      if(jsonList.get(i) instanceof Map){
        Map<String, Object> innermap = new HashMap<>();
        Iterator it = ((Map) jsonList.get(i)).entrySet().iterator();
        while (it.hasNext()){
          Map.Entry pair = (Map.Entry) it.next();
          if(pair.getValue() instanceof Map){
            Object obj = makeObject(newNestedArray, (String)pair.getKey(), (Map)pair.getValue());
            innermap.put((String) pair.getKey(), obj);
          }
          else if(pair.getValue() instanceof List){
            List list = makeList(newNestedArray, (String)pair.getKey(), (List)pair.getValue());
            innermap.put((String) pair.getKey(), list);
          }
          else{
            innermap.put((String) pair.getKey(), pair.getValue());
          }
        }
        newNestedArray._list.add(innermap);
      }
    }
    return newNestedArray;
  }
  private static Object makeObject(NestedArray newNestedArray, String key, Map value){
    if(key.equals("Address")){
      NestedArray.Address A = newNestedArray.new Address();
      A = (Address) makeAddress(A, key, value);
      return A;
    }
    return null;
  }
  private static Object makeAddress(Address newAddress, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      if(pair.getKey().toString().equals("Town")){
        Address.Town T = newAddress.new Town();
        T = (Address.Town) makeTown(T, (String) pair.getKey(), (Map) pair.getValue());
        newAddress._fields.put((String) pair.getKey(), T);
      }
      else newAddress._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newAddress;
  }
  private static Object makeTown(Address.Town newTown, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newTown._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newTown;
  }
  private static List makeList(NestedArray newNestedArray, String key, List value){
    List<Object> list = new ArrayList<>();
    for(int i = 0; i < value.size(); i++) {
      if(value.get(i) instanceof Map){
        Object result = makeObject(newNestedArray, key, (Map) value.get(i));
        list.add(result);
      }
      else if(value.get(i) instanceof List){
        List result = makeList(newNestedArray, key, (List) value.get(i));
        list.add(result);      }
      else{
        list.add(value.get(i));
      }
    }
    return list;
  }
  public java.lang.String toJSON(){return _list.toString();}

  public java.lang.String getname(int index){return (java.lang.String) _list.get(index).get("name");}
  public void setname(int index, java.lang.String name){_list.get(index).put("name", name);}

  public Address getAddress(int index){return (Address) _list.get(index).get("Address");}
  public void setAddress(int index, Address Address){_list.get(index).put("Address", Address);}

  public class Address{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public java.lang.Integer getnumber(){return (java.lang.Integer) _fields.get("number");}
    public void setnumber(java.lang.Integer number){_fields.put("number", number);}

    public java.lang.String getStreet(){return (java.lang.String) _fields.get("Street");}
    public void setStreet(java.lang.String Street){_fields.put("Street", Street);}

    public Town getTown(){return (Town) _fields.get("Town");}
    public void setTown(Town Town){_fields.put("Town", Town);}

    public class Town{
      private Map<String, Object> _fields = new HashMap<String, Object>();

      public java.lang.String getTown_Name(){return (java.lang.String) _fields.get("Town_Name");}
      public void setTown_Name(java.lang.String Town_Name){_fields.put("Town_Name", Town_Name);}


    }

  }

}
