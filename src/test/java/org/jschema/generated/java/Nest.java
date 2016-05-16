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
      else if(pair.getValue() instanceof List){
        List list = makeList(newNest, (String)pair.getKey(), (List)pair.getValue());
        newNest._fields.put((String) pair.getKey(), list);
      }
      else{
        newNest._fields.put((String) pair.getKey(), pair.getValue());
      }
    }
    return newNest;
  }
  private static Object makeObject(Nest newNest, String key, Map value){
    if(key.equals("School")){
      Nest.School S = newNest.new School();
      S = (School) makeSchool(S, key, value);
      return S;
    }
    else if(key.equals("Students")){
      Nest.Students S = newNest.new Students();
      S = (Students) makeStudents(S, key, value);
      return S;
    }
    return null;
  }
  private static Object makeSchool(School newSchool, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newSchool._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newSchool;
  }
  private static Object makeStudents(Students newStudents, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      if(pair.getKey().toString().equals("Student_Facts")){
        Students.Student_Facts S = newStudents.new Student_Facts();
        S = (Students.Student_Facts) makeStudent_Facts(S, (String) pair.getKey(), (Map) pair.getValue());
        newStudents._fields.put((String) pair.getKey(), S);
      }
      else newStudents._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newStudents;
  }
  private static Object makeStudent_Facts(Students.Student_Facts newStudent_Facts, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      if(pair.getKey().toString().equals("level")){
        Students.Student_Facts.Level l = newStudent_Facts.new Level();
        l = (Students.Student_Facts.Level) makeLevel(l, (String) pair.getKey(), (Map) pair.getValue());
        newStudent_Facts._fields.put((String) pair.getKey(), l);
      }
      else newStudent_Facts._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newStudent_Facts;
  }
  private static Object makeLevel(Students.Student_Facts.Level newLevel, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newLevel._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newLevel;
  }
  private static List makeList(Nest newNest, String key, List value){
    List<Object> list = new ArrayList<>();
    for(int i = 0; i < value.size(); i++) {
      if(value.get(i) instanceof Map){
        Object result = makeObject(newNest, key, (Map) value.get(i));
        list.add(result);
      }
      else if(value.get(i) instanceof List){
        List result = makeList(newNest, key, (List) value.get(i));
        list.add(result);      }
      else{
        list.add(value.get(i));
      }
    }
    return list;
  }
  public java.lang.String toJSON(){return _fields.toString();}

  public java.lang.String getName(){return (java.lang.String) _fields.get("Name");}
  public void setName(java.lang.String Name){_fields.put("Name", Name);}

  public java.lang.Integer getage(){return (java.lang.Integer) _fields.get("age");}
  public void setage(java.lang.Integer age){_fields.put("age", age);}

  public School getSchool(){return (School) _fields.get("School");}
  public void setSchool(School School){_fields.put("School", School);}

  public class School{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public java.lang.String getSchool_Name(){return (java.lang.String) _fields.get("School_Name");}
    public void setSchool_Name(java.lang.String School_Name){_fields.put("School_Name", School_Name);}

    public java.lang.String getCity(){return (java.lang.String) _fields.get("City");}
    public void setCity(java.lang.String City){_fields.put("City", City);}


  }
  public Students getStudents(){return (Students) _fields.get("Students");}
  public void setStudents(Students Students){_fields.put("Students", Students);}

  public class Students{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public java.lang.String getType(){return (java.lang.String) _fields.get("Type");}
    public void setType(java.lang.String Type){_fields.put("Type", Type);}

    public Student_Facts getStudent_Facts(){return (Student_Facts) _fields.get("Student_Facts");}
    public void setStudent_Facts(Student_Facts Student_Facts){_fields.put("Student_Facts", Student_Facts);}

    public class Student_Facts{
      private Map<String, Object> _fields = new HashMap<String, Object>();

      public java.lang.Integer getNumber(){return (java.lang.Integer) _fields.get("Number");}
      public void setNumber(java.lang.Integer Number){_fields.put("Number", Number);}

      public Level getlevel(){return (Level) _fields.get("level");}
      public void setlevel(Level level){_fields.put("level", level);}

      public class Level{
        private Map<String, Object> _fields = new HashMap<String, Object>();

        public java.lang.Integer getFreshmen(){return (java.lang.Integer) _fields.get("Freshmen");}
        public void setFreshmen(java.lang.Integer Freshmen){_fields.put("Freshmen", Freshmen);}

        public java.lang.Integer getSophomore(){return (java.lang.Integer) _fields.get("Sophomore");}
        public void setSophomore(java.lang.Integer Sophomore){_fields.put("Sophomore", Sophomore);}

        public java.lang.Integer getJunior(){return (java.lang.Integer) _fields.get("Junior");}
        public void setJunior(java.lang.Integer Junior){_fields.put("Junior", Junior);}

        public java.lang.Integer getSenior(){return (java.lang.Integer) _fields.get("Senior");}
        public void setSenior(java.lang.Integer Senior){_fields.put("Senior", Senior);}


      }

    }

  }

}
