package handbuilt;

import org.jschema.tools.JSchemaObject;
import org.jschema.tools.converter.ConverterNode;

import java.util.List;

public class Contact extends JSchemaObject
{
  // Structure Definition
  private static final ConverterNode<Contact> CONVERTER =
    struct( Contact::new,
            prop( "first_name", string() ),
            prop( "last_name", string() ),
            prop( "age", _int() ),
            prop( "type", _enum(Type.class) ),
            prop( "emails", array( string() ) )
    );


  // Getter/Setters
  public String getFirstName() { return (String) get( "first_name" ); }
  public void setFirstName(String val) { set( "first_name", val ); }

  public String getLastName() { return (String) get( "last_name" ); }
  public void setLastName(String val) { set( "last_name", val ); }

  public Long getAge() { return (Long) get( "age" ); }
  public void setAge(Long val) { set( "age", val ); }

  public Type getType() { return (Type) get( "type" ); }
  public void setType(Type val) { set( "type", val ); }

  public List<String> getEmails() { return (List) get( "emails" ); }

  // Inner Classes
  public enum Type {
    friend,
    customer,
    supplier,
  }

  // Conversion
  public static Contact parse(String str) {
    return convert(CONVERTER, str);
  }

  public String prettyPrint( Integer indent )
  {
    return CONVERTER.prettyPrint(0, indent, new StringBuilder(), this).toString();
  }
}
