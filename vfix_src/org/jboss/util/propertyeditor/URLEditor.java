package org.jboss.util.propertyeditor;

import java.net.MalformedURLException;
import org.jboss.util.NestedRuntimeException;
import org.jboss.util.Strings;

public class URLEditor extends TextPropertyEditorSupport {
   public Object getValue() {
      try {
         return Strings.toURL(this.getAsText());
      } catch (MalformedURLException var2) {
         throw new NestedRuntimeException(var2);
      }
   }
}
