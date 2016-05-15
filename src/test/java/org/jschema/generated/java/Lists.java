package org.jschema.generated.java;
import java.util.*;

import org.jschema.parser.*;
public class Lists{
  private Map<String, Object> _fields = new HashMap<String, Object>();

  public static Lists parse(String jsonString){
    Lists newLists = new Lists();
    Map<String, Object> jsonObject = (Map) new Parser(jsonString).parse();
    Iterator it = jsonObject.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry)it.next();
      if(pair.getValue() instanceof Map){
        Object obj = makeObject(newLists, (String)pair.getKey(), (Map)pair.getValue());
        newLists._fields.put((String) pair.getKey(), obj);
      }
      else if(pair.getValue() instanceof List){
        List list = makeList(newLists, (String)pair.getKey(), (List)pair.getValue());
        newLists._fields.put((String) pair.getKey(), list);
      }
      else{
        newLists._fields.put((String) pair.getKey(), pair.getValue());
      }
    }
    return newLists;
  }
  public static Object makeObject(Lists newLists, String key, Map value){
    if(key.equals("Science")){
      Lists.Science S = newLists.new Science();
      S = (Science) makeScience(S, key, value);
      return S;
    }
    return null;
  }
  public static Object makeScience(Science newScience, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      if(pair.getKey().toString().equals("Math")){
        Science.Math M = newScience.new Math();
        M = (Science.Math) makeMath(M, (String) pair.getKey(), (Map) pair.getValue());
        newScience._fields.put((String) pair.getKey(), M);
      }
      else if(pair.getKey().toString().equals("Physics")){
        Science.Physics P = newScience.new Physics();
        P = (Science.Physics) makePhysics(P, (String) pair.getKey(), (Map) pair.getValue());
        newScience._fields.put((String) pair.getKey(), P);
      }
      else if(pair.getKey().toString().equals("Programming")){
        Science.Programming P = newScience.new Programming();
        P = (Science.Programming) makeProgramming(P, (String) pair.getKey(), (Map) pair.getValue());
        newScience._fields.put((String) pair.getKey(), P);
      }
      else newScience._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newScience;
  }
  public static Object makeMath(Science.Math newMath, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newMath._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newMath;
  }
  public static Object makePhysics(Science.Physics newPhysics, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newPhysics._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newPhysics;
  }
  public static Object makeProgramming(Science.Programming newProgramming, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newProgramming._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newProgramming;
  }
  public static List makeList(Lists newLists, String key, List value){
    List<Object> list = new ArrayList<>();
    for(int i = 0; i < value.size(); i++) {
      if(value.get(i) instanceof Map){
        Object result = makeObject(newLists, key, (Map) value.get(i));
        list.add(result);
      }
      else if(value.get(i) instanceof List){
        List result = makeList(newLists, key, (List) value.get(i));
        list.add(result);      }
      else{
        list.add(value.get(i));
      }
    }
    return list;
  }
  public java.lang.String toJSON(){return _fields.toString();}

  public List<java.lang.String> getFamily(){return (List<java.lang.String>) _fields.get("Family");}
  public void setFamily(List<java.lang.String> Family){_fields.put("Family", Family);}

  public List<Science> getScience(){return (List<Science>) _fields.get("Science");}
  public void setScience(List<Science> Science){_fields.put("Science", Science);}

  public class Science{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public Math getMath(){return (Math) _fields.get("Math");}
    public void setMath(Math Math){_fields.put("Math", Math);}

    public class Math{
      private Map<String, Object> _fields = new HashMap<String, Object>();

      public java.lang.Integer getAlgebra(){return (java.lang.Integer) _fields.get("Algebra");}
      public void setAlgebra(java.lang.Integer Algebra){_fields.put("Algebra", Algebra);}

      public java.lang.Integer getCalculus(){return (java.lang.Integer) _fields.get("Calculus");}
      public void setCalculus(java.lang.Integer Calculus){_fields.put("Calculus", Calculus);}


    }
    public Physics getPhysics(){return (Physics) _fields.get("Physics");}
    public void setPhysics(Physics Physics){_fields.put("Physics", Physics);}

    public class Physics{
      private Map<String, Object> _fields = new HashMap<String, Object>();

      public List<java.lang.String> getMechanics(){return (List<java.lang.String>) _fields.get("Mechanics");}
      public void setMechanics(List<java.lang.String> Mechanics){_fields.put("Mechanics", Mechanics);}

      public java.lang.String getOther(){return (java.lang.String) _fields.get("Other");}
      public void setOther(java.lang.String Other){_fields.put("Other", Other);}


    }
    public Programming getProgramming(){return (Programming) _fields.get("Programming");}
    public void setProgramming(Programming Programming){_fields.put("Programming", Programming);}

    public class Programming{
      private Map<String, Object> _fields = new HashMap<String, Object>();

      public java.lang.String getJava(){return (java.lang.String) _fields.get("Java");}
      public void setJava(java.lang.String Java){_fields.put("Java", Java);}

      public java.lang.String getC(){return (java.lang.String) _fields.get("C");}
      public void setC(java.lang.String C){_fields.put("C", C);}


    }

  }

}
