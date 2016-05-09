package org.jschema.generated.java;
import java.util.*;

import org.jschema.parser.*;
public class Nest{
  private Map<String, Object> _fields = new HashMap<String, Object>();

  public static Nest parse(String jsonString){
    Nest newNest = new Nest();
    Map<String, Object> jsonObject = (Map) new Parser(jsonString).parse();
    Iterator it = jsonObject.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry)it.next();
      if(pair.getValue() instanceof Map){
        Object obj = makeObject(newNest, (String)pair.getKey(), (Map)pair.getValue());
        newNest._fields.put((String) pair.getKey(), obj);
      }
      else{
        newNest._fields.put((String) pair.getKey(), pair.getValue());
      }
    }
    return newNest;
  }
  public static Object makeObject(Nest newNest, String key, Map value){
    if(key.equals("School")){
      Nest.School S = newNest.new School();
      Iterator it = value.entrySet().iterator();
      while (it.hasNext()){
        Map.Entry pair = (Map.Entry) it.next();
        if(pair.getValue() instanceof HashMap){
          Object obj = makeObject(newNest, (String) pair.getKey(), (Map)pair.getValue());
          S._fields.put((String) pair.getKey(), obj);
        }
        else{
          S._fields.put((String) pair.getKey(), pair.getValue());
        }
      }
      return S;
    }
    if(key.equals("Students")){
      Nest.Students S = newNest.new Students();
      Iterator it = value.entrySet().iterator();
      while (it.hasNext()){
        Map.Entry pair = (Map.Entry) it.next();
        if(pair.getValue() instanceof HashMap){
          Object obj = makeObject(newNest, (String) pair.getKey(), (Map)pair.getValue());
          S._fields.put((String) pair.getKey(), obj);
        }
        else{
          S._fields.put((String) pair.getKey(), pair.getValue());
        }
      }
      return S;
    }
    if(key.equals("Student_Facts")){
      Nest.Students S = newNest.new Students();
      Nest.Students.Student_Facts F = S.new Student_Facts();
      Iterator it = value.entrySet().iterator();
      while (it.hasNext()){
        Map.Entry pair = (Map.Entry) it.next();
        if(pair.getValue() instanceof HashMap){
          Object obj = makeObject(newNest, (String) pair.getKey(), (Map)pair.getValue());
          F._fields.put((String) pair.getKey(), obj);
        }
        else{
          F._fields.put((String) pair.getKey(), pair.getValue());
        }
      }
      return F;
    }
    return null;
}
  public String toJSON(){return _fields.toString();}

  public String getName(){return (String) _fields.get("Name");}
  public void setName(String Name){_fields.put("Name", Name);}

  public int getAge(){return (int) _fields.get("age");}
  public void setAge(int age){_fields.put("age", age);}

  public School getSchool(){return (School) _fields.get("School");}
  public void setSchool(School School){_fields.put("School", School);}

  public class School{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public String toJSON(){return _fields.toString();}

    public String getSchool_Name(){return (String) _fields.get("School_Name");}
    public void setSchool_Name(String School_Name){_fields.put("School_Name", School_Name);}

    public String getCity(){return (String) _fields.get("City");}
    public void setCity(String City){_fields.put("City", City);}


  }
  public Students getStudents(){return (Students) _fields.get("Students");}
  public void setStudents(Students Students){_fields.put("Students", Students);}

  public class Students{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public String toJSON(){return _fields.toString();}

    public String getType(){return (String) _fields.get("Type");}
    public void setType(String Type){_fields.put("Type", Type);}

    public Student_Facts getStudent_Facts(){return (Student_Facts) _fields.get("Student_Facts");}
    public void setStudent_Facts(Student_Facts Student_Facts){_fields.put("Student_Facts", Student_Facts);}

    public class Student_Facts{
      private Map<String, Object> _fields = new HashMap<String, Object>();

      public String toJSON(){return _fields.toString();}

      public int getNumber(){return (int) _fields.get("Number");}
      public void setNumber(int Number){_fields.put("Number", Number);}

      public String getLevel(){return (String) _fields.get("level");}
      public void setLevel(String level){_fields.put("level", level);}


    }

  }

}
