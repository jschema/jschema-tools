package org.jschema.generated.java;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Invoice
{
  private Map<String, Object> _values = new HashMap<String, Object>();

  public String getId()
  {
    return (String) _values.get( "id" );
  }

  public void setId(String str)
  {
    _values.put( "id", str );
  }

  public Date getCreatedAt()
  {
    return (Date) _values.get( "created_at" );
  }

  public void setCreatedAt(Date d)
  {
    _values.put( "created_at", d );
  }

  // .. and so on

  // Note use of inner class
  public Customer getCustomer()
  {
    return (Customer) _values.get( "customer" );
  }

  public void setCustomer(Customer c)
  {
    _values.put( "customer", c );
  }


  // .. and to_address similar

  // .. finally, dealing with arrays, treat them as lists:
  public List<LineItem> getLineItems()
  {
    return (List<LineItem>) _values.get( "line_items" );
  }

  public void setLineItems(List<LineItem> l)
  {
    _values.put( "line_items", l );
  }

  // .. inner classes for inner types


  public class Customer {
    private Map _values = new HashMap();

    public String getEmail()
    {
      return (String) _values.get( "email" );
    }

    public void setEmail(String str)
    {
      _values.put( "email", str );
    }

    // .. and so on

  }

  // ... generate one for addresss

  // ... and one for the array items

  public class LineItem {
    private Map _values = new HashMap();

    public String getSku()
    {
      return (String) _values.get( "sku" );
    }

    public void setSku(String str)
    {
      _values.put( "sku", str );
    }

    // .. and so on
  }

  //public parse method (will eventually take file objects, etc)
  public static Invoice parse( String json )
  {
    return null;
  }
}
