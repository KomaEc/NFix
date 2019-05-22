package com.gzoltar.shaded.org.pitest.reloc.xstream.core.util;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ObjectAccessException;
import java.beans.PropertyEditor;

public class ThreadSafePropertyEditor {
   private final Class editorType;
   private final Pool pool;

   public ThreadSafePropertyEditor(Class type, int initialPoolSize, int maxPoolSize) {
      if (!PropertyEditor.class.isAssignableFrom(type)) {
         throw new IllegalArgumentException(type.getName() + " is not a " + PropertyEditor.class.getName());
      } else {
         this.editorType = type;
         this.pool = new Pool(initialPoolSize, maxPoolSize, new Pool.Factory() {
            public Object newInstance() {
               try {
                  return ThreadSafePropertyEditor.this.editorType.newInstance();
               } catch (InstantiationException var2) {
                  throw new ObjectAccessException("Could not call default constructor of " + ThreadSafePropertyEditor.this.editorType.getName(), var2);
               } catch (IllegalAccessException var3) {
                  throw new ObjectAccessException("Could not call default constructor of " + ThreadSafePropertyEditor.this.editorType.getName(), var3);
               }
            }
         });
      }
   }

   public String getAsText(Object object) {
      PropertyEditor editor = this.fetchFromPool();

      String var3;
      try {
         editor.setValue(object);
         var3 = editor.getAsText();
      } finally {
         this.pool.putInPool(editor);
      }

      return var3;
   }

   public Object setAsText(String str) {
      PropertyEditor editor = this.fetchFromPool();

      Object var3;
      try {
         editor.setAsText(str);
         var3 = editor.getValue();
      } finally {
         this.pool.putInPool(editor);
      }

      return var3;
   }

   private PropertyEditor fetchFromPool() {
      PropertyEditor editor = (PropertyEditor)this.pool.fetchFromPool();
      return editor;
   }
}
