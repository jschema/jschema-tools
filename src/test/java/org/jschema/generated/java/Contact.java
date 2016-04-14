public class Contact{
private String _first_name;
private String _last_name;
private int _age;
private String[] _emails;
public enum type{
  friend,
  customer,
  supplier
}
private type _type;
public Contact(String first_name, String last_name, int age, type TYPE, String[] emails){
_first_name = first_name;
_last_name = last_name;
_age = age;
_type = TYPE;
_emails = emails;
}
public String getfirst_name(){return _first_name;}
public String getlast_name(){return _last_name;}
public int getage(){return _age;}
public type getTYPE(){return _type;}
public String[] getemails(){return _emails;}
public void setfirst_name(String first_name){_first_name = first_name;}
public void setlast_name(String last_name){_last_name = last_name;}
public void setage(int age){_age = age;}
public void settype(type TYPE){_type = TYPE;}
public void setemails(String[] emails){_emails = emails;}
}
