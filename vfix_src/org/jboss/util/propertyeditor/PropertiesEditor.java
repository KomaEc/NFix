package org.jboss.util.propertyeditor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import org.jboss.util.NestedRuntimeException;
import org.jboss.util.StringPropertyReplacer;

public class PropertiesEditor extends TextPropertyEditorSupport {
   public Object getValue() {
      try {
         String propsText = this.getAsText();
         Properties rawProps = new Properties(System.getProperties());
         ByteArrayInputStream bais = new ByteArrayInputStream(propsText.getBytes());
         rawProps.load(bais);
         Properties props = new Properties();
         Iterator keys = rawProps.keySet().iterator();

         while(keys.hasNext()) {
            String key = (String)keys.next();
            String value = rawProps.getProperty(key);
            String value2 = StringPropertyReplacer.replaceProperties(value, rawProps);
            props.setProperty(key, value2);
         }

         rawProps.clear();
         return props;
      } catch (IOException var9) {
         throw new NestedRuntimeException(var9);
      }
   }
}
