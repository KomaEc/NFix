package org.apache.maven.surefire.booter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Properties;
import org.apache.maven.surefire.util.ReflectionUtils;

public class TypeEncodedValue {
   String type;
   String value;

   public TypeEncodedValue(String type, String value) {
      this.type = type;
      this.value = value;
   }

   public boolean isTypeClass() {
      return Class.class.getName().equals(this.type);
   }

   public Object getDecodedValue() {
      return this.getDecodedValue(Thread.currentThread().getContextClassLoader());
   }

   public Object getDecodedValue(ClassLoader classLoader) {
      if (this.type.trim().length() == 0) {
         return null;
      } else if (this.type.equals(String.class.getName())) {
         return this.value;
      } else if (this.isTypeClass()) {
         return ReflectionUtils.loadClass(classLoader, this.value);
      } else if (this.type.equals(File.class.getName())) {
         return new File(this.value);
      } else if (this.type.equals(Boolean.class.getName())) {
         return Boolean.valueOf(this.value);
      } else if (this.type.equals(Integer.class.getName())) {
         return Integer.valueOf(this.value);
      } else if (this.type.equals(Properties.class.getName())) {
         Properties result = new Properties();

         try {
            ByteArrayInputStream bais = new ByteArrayInputStream(this.value.getBytes("8859_1"));
            result.load(bais);
            return result;
         } catch (Exception var4) {
            throw new RuntimeException("bug in property conversion", var4);
         }
      } else {
         throw new IllegalArgumentException("Unknown parameter type: " + this.type);
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TypeEncodedValue that = (TypeEncodedValue)o;
         if (this.type != null) {
            if (!this.type.equals(that.type)) {
               return false;
            }
         } else if (that.type != null) {
            return false;
         }

         boolean var10000;
         label51: {
            if (this.value != null) {
               if (!this.value.equals(that.value)) {
                  break label51;
               }
            } else if (that.value != null) {
               break label51;
            }

            var10000 = true;
            return var10000;
         }

         var10000 = false;
         return var10000;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.type != null ? this.type.hashCode() : 0;
      result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
      return result;
   }
}
