package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;

public abstract class EnumeratedAttribute {
   protected String value;
   private int index = -1;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$types$EnumeratedAttribute;

   public abstract String[] getValues();

   protected EnumeratedAttribute() {
   }

   public static EnumeratedAttribute getInstance(Class clazz, String value) throws BuildException {
      if (!(class$org$apache$tools$ant$types$EnumeratedAttribute == null ? (class$org$apache$tools$ant$types$EnumeratedAttribute = class$("org.apache.tools.ant.types.EnumeratedAttribute")) : class$org$apache$tools$ant$types$EnumeratedAttribute).isAssignableFrom(clazz)) {
         throw new BuildException("You have to provide a subclass from EnumeratedAttribut as clazz-parameter.");
      } else {
         EnumeratedAttribute ea = null;

         try {
            ea = (EnumeratedAttribute)clazz.newInstance();
         } catch (Exception var4) {
            throw new BuildException(var4);
         }

         ea.setValue(value);
         return ea;
      }
   }

   public final void setValue(String value) throws BuildException {
      int idx = this.indexOfValue(value);
      if (idx == -1) {
         throw new BuildException(value + " is not a legal value for this attribute");
      } else {
         this.index = idx;
         this.value = value;
      }
   }

   public final boolean containsValue(String value) {
      return this.indexOfValue(value) != -1;
   }

   public final int indexOfValue(String value) {
      String[] values = this.getValues();
      if (values != null && value != null) {
         for(int i = 0; i < values.length; ++i) {
            if (value.equals(values[i])) {
               return i;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public final String getValue() {
      return this.value;
   }

   public final int getIndex() {
      return this.index;
   }

   public String toString() {
      return this.getValue();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
