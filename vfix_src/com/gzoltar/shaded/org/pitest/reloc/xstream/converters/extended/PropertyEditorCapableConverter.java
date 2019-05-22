package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.ThreadSafePropertyEditor;

public class PropertyEditorCapableConverter implements SingleValueConverter {
   private final ThreadSafePropertyEditor editor;
   private final Class type;

   public PropertyEditorCapableConverter(Class propertyEditorType, Class type) {
      this.type = type;
      this.editor = new ThreadSafePropertyEditor(propertyEditorType, 2, 5);
   }

   public boolean canConvert(Class type) {
      return this.type == type;
   }

   public Object fromString(String str) {
      return this.editor.setAsText(str);
   }

   public String toString(Object obj) {
      return this.editor.getAsText(obj);
   }
}
