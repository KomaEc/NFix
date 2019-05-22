package org.jboss.util.propertyeditor;

import org.jboss.util.NestedRuntimeException;

public class ClassEditor extends TextPropertyEditorSupport {
   public Object getValue() {
      try {
         ClassLoader loader = Thread.currentThread().getContextClassLoader();
         String classname = this.getAsText();
         Class<?> type = loader.loadClass(classname);
         return type;
      } catch (Exception var4) {
         throw new NestedRuntimeException(var4);
      }
   }
}
