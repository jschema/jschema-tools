package org.jschema.generated.java;
import java.util.*;
public class Basic{
  private Map<String, Object> _fields = new HashMap<String, Object>();
  public String toJSON(){return _fields.toString();}
  public String getName(){return (String) _fields.get("name");}
  public void setName(Object name){_fields.put("name", name);}
  public int getAge(){return (int) _fields.get("age");}
  public void setAge(Object age){_fields.put("age", age);}

}
