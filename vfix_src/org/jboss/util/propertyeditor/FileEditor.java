package org.jboss.util.propertyeditor;

import java.io.File;
import java.io.IOException;
import org.jboss.util.NestedRuntimeException;

public class FileEditor extends TextPropertyEditorSupport {
   public Object getValue() {
      try {
         return (new File(this.getAsText())).getCanonicalFile();
      } catch (IOException var2) {
         throw new NestedRuntimeException(var2);
      }
   }
}
