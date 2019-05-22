package org.testng.xml;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import org.testng.reporters.XMLStringBuffer;

public class XmlUtils {
   public static void setProperty(Properties p, String name, String value, String def) {
      if (!def.equals(value) && value != null) {
         p.setProperty(name, value);
      }

   }

   public static void dumpParameters(XMLStringBuffer xsb, Map<String, String> parameters) {
      if (!parameters.isEmpty()) {
         Iterator i$ = parameters.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<String, String> para = (Entry)i$.next();
            Properties paramProps = new Properties();
            paramProps.setProperty("name", (String)para.getKey());
            paramProps.setProperty("value", (String)para.getValue());
            xsb.addEmptyElement("parameter", paramProps);
         }
      }

   }
}
