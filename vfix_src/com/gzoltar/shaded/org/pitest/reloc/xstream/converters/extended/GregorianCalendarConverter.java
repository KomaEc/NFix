package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class GregorianCalendarConverter implements Converter {
   public boolean canConvert(Class type) {
      return type.equals(GregorianCalendar.class);
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      GregorianCalendar calendar = (GregorianCalendar)source;
      ExtendedHierarchicalStreamWriterHelper.startNode(writer, "time", Long.TYPE);
      long timeInMillis = calendar.getTime().getTime();
      writer.setValue(String.valueOf(timeInMillis));
      writer.endNode();
      ExtendedHierarchicalStreamWriterHelper.startNode(writer, "timezone", String.class);
      writer.setValue(calendar.getTimeZone().getID());
      writer.endNode();
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      reader.moveDown();
      long timeInMillis = Long.parseLong(reader.getValue());
      reader.moveUp();
      String timeZone;
      if (reader.hasMoreChildren()) {
         reader.moveDown();
         timeZone = reader.getValue();
         reader.moveUp();
      } else {
         timeZone = TimeZone.getDefault().getID();
      }

      GregorianCalendar result = new GregorianCalendar();
      result.setTimeZone(TimeZone.getTimeZone(timeZone));
      result.setTime(new Date(timeInMillis));
      return result;
   }
}
