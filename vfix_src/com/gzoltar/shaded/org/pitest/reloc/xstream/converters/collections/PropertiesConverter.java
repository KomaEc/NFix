package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.Fields;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Map.Entry;

public class PropertiesConverter implements Converter {
   private static final Field defaultsField = Fields.locate(Properties.class, Properties.class, false);
   private final boolean sort;

   public PropertiesConverter() {
      this(false);
   }

   public PropertiesConverter(boolean sort) {
      this.sort = sort;
   }

   public boolean canConvert(Class type) {
      return Properties.class == type;
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      Properties properties = (Properties)source;
      Map map = this.sort ? new TreeMap(properties) : properties;
      Iterator iterator = ((Map)map).entrySet().iterator();

      while(iterator.hasNext()) {
         Entry entry = (Entry)iterator.next();
         writer.startNode("property");
         writer.addAttribute("name", entry.getKey().toString());
         writer.addAttribute("value", entry.getValue().toString());
         writer.endNode();
      }

      if (defaultsField != null) {
         Properties defaults = (Properties)Fields.read(defaultsField, properties);
         if (defaults != null) {
            writer.startNode("defaults");
            this.marshal(defaults, writer, context);
            writer.endNode();
         }
      }

   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      Properties properties = new Properties();

      Properties defaults;
      for(defaults = null; reader.hasMoreChildren(); reader.moveUp()) {
         reader.moveDown();
         if (reader.getNodeName().equals("defaults")) {
            defaults = (Properties)this.unmarshal(reader, context);
         } else {
            String name = reader.getAttribute("name");
            String value = reader.getAttribute("value");
            properties.setProperty(name, value);
         }
      }

      if (defaults == null) {
         return properties;
      } else {
         Properties propertiesWithDefaults = new Properties(defaults);
         propertiesWithDefaults.putAll(properties);
         return propertiesWithDefaults;
      }
   }
}
