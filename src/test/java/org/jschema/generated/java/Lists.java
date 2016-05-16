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
  private static Object makeObject(Lists newLists, String key, Map value){
    if(key.equals("Science")){
      Lists.Science S = newLists.new Science();
      S = (Science) makeScience(S, key, value);
      return S;
    }
    return null;
  }
  private static Object makeScience(Science newScience, String key, Map value){
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
  private static Object makeMath(Science.Math newMath, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newMath._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newMath;
  }
  private static Object makePhysics(Science.Physics newPhysics, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newPhysics._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newPhysics;
  }
  private static Object makeProgramming(Science.Programming newProgramming, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newProgramming._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newProgramming;
  }
  private static List makeList(Lists newLists, String key, List value){
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

  public List<java.lang.String> getFAMILY(){return (List<java.lang.String>) _fields.get("FAMILY");}
  public void setFAMILY(List<java.lang.String> FAMILY){_fields.put("FAMILY", FAMILY);}

  public List<java.lang.Object> getSCIENCE(){return (List<java.lang.Object>) _fields.get("SCIENCE");}
  public void setSCIENCE(List<java.lang.Object> SCIENCE){_fields.put("SCIENCE", SCIENCE);}

  public class Science{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public Math getMATH(){return (MATH) _fields.get("MATH");}
    public void setMATH(MATH MATH){_fields.put("MATH", MATH);}

    public class Math{
      private Map<String, Object> _fields = new HashMap<String, Object>();

      public java.lang.Integer getALGEBRA(){return (java.lang.Integer) _fields.get("ALGEBRA");}
      public void setALGEBRA(java.lang.Integer ALGEBRA){_fields.put("ALGEBRA", ALGEBRA);}

      public java.lang.Integer getCALCULUS(){return (java.lang.Integer) _fields.get("CALCULUS");}
      public void setCALCULUS(java.lang.Integer CALCULUS){_fields.put("CALCULUS", CALCULUS);}


    }
    public Physics getPHYSICS(){return (PHYSICS) _fields.get("PHYSICS");}
    public void setPHYSICS(PHYSICS PHYSICS){_fields.put("PHYSICS", PHYSICS);}

    public class Physics{
      private Map<String, Object> _fields = new HashMap<String, Object>();

      public List<java.lang.String> getMECHANICS(){return (List<java.lang.String>) _fields.get("MECHANICS");}
      public void setMECHANICS(List<java.lang.String> MECHANICS){_fields.put("MECHANICS", MECHANICS);}

      public java.lang.String getOTHER(){return (java.lang.String) _fields.get("OTHER");}
      public void setOTHER(java.lang.String OTHER){_fields.put("OTHER", OTHER);}


    }
    public Programming getPROGRAMMING(){return (PROGRAMMING) _fields.get("PROGRAMMING");}
    public void setPROGRAMMING(PROGRAMMING PROGRAMMING){_fields.put("PROGRAMMING", PROGRAMMING);}

    public class Programming{
      private Map<String, Object> _fields = new HashMap<String, Object>();

      public java.lang.String getJAVA(){return (java.lang.String) _fields.get("JAVA");}
      public void setJAVA(java.lang.String JAVA){_fields.put("JAVA", JAVA);}

      public java.lang.String getC(){return (java.lang.String) _fields.get("C");}
      public void setC(java.lang.String C){_fields.put("C", C);}


    }

  }

}
