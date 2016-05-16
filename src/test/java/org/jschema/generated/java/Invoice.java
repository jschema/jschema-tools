package org.jschema.generated.java;
import java.util.*;

import org.jschema.parser.*;
public class Invoice{
  private Map<String, Object> _fields = new HashMap<String, Object>();

  public static Invoice parse(String jsonString){
    Invoice newInvoice = new Invoice();
    Map<String, Object> jsonObject = (Map) new Parser(jsonString).parse();
    Iterator it = jsonObject.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry)it.next();
      if(pair.getValue() instanceof Map){
        Object obj = makeObject(newInvoice, (String)pair.getKey(), (Map)pair.getValue());
        newInvoice._fields.put((String) pair.getKey(), obj);
      }
      else if(pair.getValue() instanceof List){
        List list = makeList(newInvoice, (String)pair.getKey(), (List)pair.getValue());
        newInvoice._fields.put((String) pair.getKey(), list);
      }
      else{
        newInvoice._fields.put((String) pair.getKey(), pair.getValue());
      }
    }
    return newInvoice;
  }
  private static Object makeObject(Invoice newInvoice, String key, Map value){
    if(key.equals("customer")){
      Invoice.Customer c = newInvoice.new Customer();
      c = (Customer) makeCustomer(c, key, value);
      return c;
    }
    else if(key.equals("to_address")){
      Invoice.To_address t = newInvoice.new To_address();
      t = (To_address) makeTo_address(t, key, value);
      return t;
    }
    else if(key.equals("line_items")){
      Invoice.Line_items l = newInvoice.new Line_items();
      l = (Line_items) makeLine_items(l, key, value);
      return l;
    }
    return null;
  }
  private static Object makeCustomer(Customer newCustomer, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newCustomer._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newCustomer;
  }
  private static Object makeTo_address(To_address newTo_address, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newTo_address._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newTo_address;
  }
  private static Object makeLine_items(Line_items newLine_items, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newLine_items._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newLine_items;
  }
  private static List makeList(Invoice newInvoice, String key, List value){
    List<Object> list = new ArrayList<>();
    for(int i = 0; i < value.size(); i++) {
      if(value.get(i) instanceof Map){
        Object result = makeObject(newInvoice, key, (Map) value.get(i));
        list.add(result);
      }
      else if(value.get(i) instanceof List){
        List result = makeList(newInvoice, key, (List) value.get(i));
        list.add(result);      }
      else{
        list.add(value.get(i));
      }
    }
    return list;
  }
  public java.lang.String toJSON(){return _fields.toString();}

  public java.lang.String getid(){return (java.lang.String) _fields.get("id");}
  public void setid(java.lang.String id){_fields.put("id", id);}

  public java.util.Date getcreated_at(){return (java.util.Date) _fields.get("created_at");}
  public void setcreated_at(java.util.Date created_at){_fields.put("created_at", created_at);}

  public java.util.Date getupdated_at(){return (java.util.Date) _fields.get("updated_at");}
  public void setupdated_at(java.util.Date updated_at){_fields.put("updated_at", updated_at);}

  public java.lang.String getemail(){return (java.lang.String) _fields.get("email");}
  public void setemail(java.lang.String email){_fields.put("email", email);}

  public java.lang.Double gettotal(){return (java.lang.Double) _fields.get("total");}
  public void settotal(java.lang.Double total){_fields.put("total", total);}

  public java.lang.Double getsubtotal(){return (java.lang.Double) _fields.get("subtotal");}
  public void setsubtotal(java.lang.Double subtotal){_fields.put("subtotal", subtotal);}

  public java.lang.Double gettax(){return (java.lang.Double) _fields.get("tax");}
  public void settax(java.lang.Double tax){_fields.put("tax", tax);}

  public java.lang.String getnotes(){return (java.lang.String) _fields.get("notes");}
  public void setnotes(java.lang.String notes){_fields.put("notes", notes);}

  public Customer getcustomer(){return (Customer) _fields.get("customer");}
  public void setcustomer(Customer customer){_fields.put("customer", customer);}

  public class Customer{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public java.lang.String getemail(){return (java.lang.String) _fields.get("email");}
    public void setemail(java.lang.String email){_fields.put("email", email);}

    public java.lang.String getfirst_name(){return (java.lang.String) _fields.get("first_name");}
    public void setfirst_name(java.lang.String first_name){_fields.put("first_name", first_name);}

    public java.lang.String getlast_name(){return (java.lang.String) _fields.get("last_name");}
    public void setlast_name(java.lang.String last_name){_fields.put("last_name", last_name);}


  }
  public To_address getto_address(){return (To_address) _fields.get("to_address");}
  public void setto_address(To_address to_address){_fields.put("to_address", to_address);}

  public class To_address{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public java.lang.String getaddress1(){return (java.lang.String) _fields.get("address1");}
    public void setaddress1(java.lang.String address1){_fields.put("address1", address1);}

    public java.lang.String getaddress2(){return (java.lang.String) _fields.get("address2");}
    public void setaddress2(java.lang.String address2){_fields.put("address2", address2);}

    public java.lang.String getzip(){return (java.lang.String) _fields.get("zip");}
    public void setzip(java.lang.String zip){_fields.put("zip", zip);}

    public java.lang.String getstate(){return (java.lang.String) _fields.get("state");}
    public void setstate(java.lang.String state){_fields.put("state", state);}

    public java.lang.String getcountry(){return (java.lang.String) _fields.get("country");}
    public void setcountry(java.lang.String country){_fields.put("country", country);}


  }
  public List<Line_items> getline_items(){return (List<Line_items>) _fields.get("line_items");}
  public void setline_items(List<Line_items> line_items){_fields.put("line_items", line_items);}

  public class Line_items{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public java.lang.String getsku(){return (java.lang.String) _fields.get("sku");}
    public void setsku(java.lang.String sku){_fields.put("sku", sku);}

    public java.lang.String getdescription(){return (java.lang.String) _fields.get("description");}
    public void setdescription(java.lang.String description){_fields.put("description", description);}

    public java.lang.String getnotes(){return (java.lang.String) _fields.get("notes");}
    public void setnotes(java.lang.String notes){_fields.put("notes", notes);}

    public java.lang.Integer getcount(){return (java.lang.Integer) _fields.get("count");}
    public void setcount(java.lang.Integer count){_fields.put("count", count);}

    public java.lang.Double getprice(){return (java.lang.Double) _fields.get("price");}
    public void setprice(java.lang.Double price){_fields.put("price", price);}

    public java.lang.Double getsubtotal(){return (java.lang.Double) _fields.get("subtotal");}
    public void setsubtotal(java.lang.Double subtotal){_fields.put("subtotal", subtotal);}


  }

}
