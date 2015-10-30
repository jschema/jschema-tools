package org.jschema.tools.converter;

import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;

import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateNode extends ConverterNode<Date>
{
  @Override
  protected Date _toJava( Node n ) throws Exception
  {
    check( n, LiteralNode.class, "string" );
    String string = ((LiteralNode)n).getString();
    Calendar calendar = DatatypeConverter.parseDateTime( string );
    return calendar.getTime();
  }

  @Override
  public StringBuilder prettyPrint( int offset, Integer indent, StringBuilder stringBuilder, Date value )
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(value);
    return stringBuilder.append(quote(DatatypeConverter.printDateTime( calendar )));
  }
}
