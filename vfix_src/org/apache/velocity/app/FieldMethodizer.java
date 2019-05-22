package org.apache.velocity.app;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import org.apache.velocity.util.ClassUtils;

public class FieldMethodizer {
   private HashMap fieldHash = new HashMap();
   private HashMap classHash = new HashMap();

   public FieldMethodizer() {
   }

   public FieldMethodizer(String s) {
      try {
         this.addObject(s);
      } catch (Exception var3) {
         System.err.println("Could not add " + s + " for field methodizing: " + var3.getMessage());
      }

   }

   public FieldMethodizer(Object o) {
      try {
         this.addObject(o);
      } catch (Exception var3) {
         System.err.println("Could not add " + o + " for field methodizing: " + var3.getMessage());
      }

   }

   public void addObject(String s) throws Exception {
      this.inspect(ClassUtils.getClass(s));
   }

   public void addObject(Object o) throws Exception {
      this.inspect(o.getClass());
   }

   public Object get(String fieldName) {
      Object value = null;

      try {
         Field f = (Field)this.fieldHash.get(fieldName);
         if (f != null) {
            value = f.get(this.classHash.get(fieldName));
         }
      } catch (IllegalAccessException var4) {
         System.err.println("IllegalAccessException while trying to access " + fieldName + ": " + var4.getMessage());
      }

      return value;
   }

   private void inspect(Class clas) {
      Field[] fields = clas.getFields();

      for(int i = 0; i < fields.length; ++i) {
         int mod = fields[i].getModifiers();
         if (Modifier.isStatic(mod) && Modifier.isPublic(mod)) {
            this.fieldHash.put(fields[i].getName(), fields[i]);
            this.classHash.put(fields[i].getName(), clas);
         }
      }

   }
}
