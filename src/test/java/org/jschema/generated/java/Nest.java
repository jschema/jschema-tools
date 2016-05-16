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
  private static Object makeLevel(Students.Student_Facts.Level newLevel, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newLevel._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newLevel;
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

  public java.lang.String getNAME(){return (java.lang.String) _fields.get("NAME");}
  public void setNAME(java.lang.String NAME){_fields.put("NAME", NAME);}

  public java.lang.Integer getAge(){return (java.lang.Integer) _fields.get("age");}
  public void setAge(java.lang.Integer age){_fields.put("age", age);}

  public School getSCHOOL(){return (SCHOOL) _fields.get("SCHOOL");}
  public void setSCHOOL(SCHOOL SCHOOL){_fields.put("SCHOOL", SCHOOL);}

  public class School{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public java.lang.String getSCHOOL_NAME(){return (java.lang.String) _fields.get("SCHOOL_NAME");}
    public void setSCHOOL_NAME(java.lang.String SCHOOL_NAME){_fields.put("SCHOOL_NAME", SCHOOL_NAME);}

    public java.lang.String getCITY(){return (java.lang.String) _fields.get("CITY");}
    public void setCITY(java.lang.String CITY){_fields.put("CITY", CITY);}


  }
  public Students getSTUDENTS(){return (STUDENTS) _fields.get("STUDENTS");}
  public void setSTUDENTS(STUDENTS STUDENTS){_fields.put("STUDENTS", STUDENTS);}

  public class Students{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public java.lang.String getTYPE(){return (java.lang.String) _fields.get("TYPE");}
    public void setTYPE(java.lang.String TYPE){_fields.put("TYPE", TYPE);}

    public Student_Facts getSTUDENT_FACTS(){return (STUDENT_FACTS) _fields.get("STUDENT_FACTS");}
    public void setSTUDENT_FACTS(STUDENT_FACTS STUDENT_FACTS){_fields.put("STUDENT_FACTS", STUDENT_FACTS);}

    public class Student_Facts{
      private Map<String, Object> _fields = new HashMap<String, Object>();

      public java.lang.Integer getNUMBER(){return (java.lang.Integer) _fields.get("NUMBER");}
      public void setNUMBER(java.lang.Integer NUMBER){_fields.put("NUMBER", NUMBER);}

      public Level getLevel(){return (Level) _fields.get("level");}
      public void setLevel(Level level){_fields.put("level", level);}

      public class Level{
        private Map<String, Object> _fields = new HashMap<String, Object>();

        public java.lang.Integer getFRESHMEN(){return (java.lang.Integer) _fields.get("FRESHMEN");}
        public void setFRESHMEN(java.lang.Integer FRESHMEN){_fields.put("FRESHMEN", FRESHMEN);}

        public java.lang.Integer getSOPHOMORE(){return (java.lang.Integer) _fields.get("SOPHOMORE");}
        public void setSOPHOMORE(java.lang.Integer SOPHOMORE){_fields.put("SOPHOMORE", SOPHOMORE);}

        public java.lang.Integer getJUNIOR(){return (java.lang.Integer) _fields.get("JUNIOR");}
        public void setJUNIOR(java.lang.Integer JUNIOR){_fields.put("JUNIOR", JUNIOR);}

        public java.lang.Integer getSENIOR(){return (java.lang.Integer) _fields.get("SENIOR");}
        public void setSENIOR(java.lang.Integer SENIOR){_fields.put("SENIOR", SENIOR);}


      }

    }

  }

}
