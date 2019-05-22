package org.jboss.util.propertyeditor;

import java.net.URISyntaxException;
import org.jboss.util.NestedRuntimeException;
import org.jboss.util.Strings;

public class URIEditor extends TextPropertyEditorSupport {
   public Object getValue() {
      try {
         return Strings.toURI(this.getAsText());
      } catch (URISyntaxException var2) {
         throw new NestedRuntimeException(var2);
      }
   }
}
