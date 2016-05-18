package org.jschema.generated.java;
import java.util.*;

import org.jschema.parser.*;
public class Person{
  private Map<String, Object> _fields = new HashMap<String, Object>();

  public static Person parse(String jsonString){
    Person newPerson = new Person();
    Map<String, Object> jsonObject = (Map) new Parser(jsonString).parse();
    Iterator it = jsonObject.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry)it.next();
      if(pair.getValue() instanceof Map){
        Object obj = makeObject(newPerson, (String)pair.getKey(), (Map)pair.getValue());
        newPerson._fields.put((String) pair.getKey(), obj);
      }
      else if(pair.getValue() instanceof List){
        List list = makeList(newPerson, (String)pair.getKey(), (List)pair.getValue());
        newPerson._fields.put((String) pair.getKey(), list);
      }
      else{
        newPerson._fields.put((String) pair.getKey(), pair.getValue());
      }
    }
    return newPerson;
  }
  private static Object makeObject(Person newPerson, String key, Map value){
    if(key.equals("age")){
      Person.Age a = newPerson.new Age();
      a = (Age) makeAge(a, key, value);
      return a;
    }
    return null;
  }
  private static Object makeAge(Age newAge, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      if(pair.getKey().toString().equals("year")){
        Age.Year y = newAge.new Year();
        y = (Age.Year) makeYear(y, (String) pair.getKey(), (Map) pair.getValue());
        newAge._fields.put((String) pair.getKey(), y);
      }
      else newAge._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newAge;
  }
  private static Object makeYear(Age.Year newYear, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newYear._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newYear;
  }
  private static List makeList(Person newPerson, String key, List value){
    List<Object> list = new ArrayList<>();
    for(int i = 0; i < value.size(); i++) {
      if(value.get(i) instanceof Map){
        Object result = makeObject(newPerson, key, (Map) value.get(i));
        list.add(result);
      }
      else if(value.get(i) instanceof List){
        List result = makeList(newPerson, key, (List) value.get(i));
        list.add(result);      }
      else{
        list.add(value.get(i));
      }
    }
    return list;
  }
  public java.lang.String toJSON(){return _fields.toString();}

  public java.lang.String getname(){return (java.lang.String) _fields.get("name");}
  public void setname(java.lang.String name){_fields.put("name", name);}

  public Age getage(){return (Age) _fields.get("age");}
  public void setage(Age age){_fields.put("age", age);}

  public class Age{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public java.lang.String getmonth(){return (java.lang.String) _fields.get("month");}
    public void setmonth(java.lang.String month){_fields.put("month", month);}

    public java.lang.Integer getday(){return (java.lang.Integer) _fields.get("day");}
    public void setday(java.lang.Integer day){_fields.put("day", day);}

    public Year getyear(){return (Year) _fields.get("year");}
    public void setyear(Year year){_fields.put("year", year);}

    public class Year{
      private Map<String, Object> _fields = new HashMap<String, Object>();

      public java.lang.Integer getdecade(){return (java.lang.Integer) _fields.get("decade");}
      public void setdecade(java.lang.Integer decade){_fields.put("decade", decade);}


    }

  }

}
