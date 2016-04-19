package org.jschema.generated.java;
import java.util.*;
public class Basic{
  private Map<String, Object> _fields = new HashMap<String, Object>();
  public String toJSON(){return _fields.toString();}
  public String getname(){return (String) _fields.get("name");}
  public void setname(Object name){_fields.put("name", name);}
  public int getage(){return (int) _fields.get("age");}
  public void setage(Object age){_fields.put("age", age);}

}
