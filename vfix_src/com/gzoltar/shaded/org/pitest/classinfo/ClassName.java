package com.gzoltar.shaded.org.pitest.classinfo;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.util.IsolationUtils;
import com.gzoltar.shaded.org.pitest.util.Log;
import java.util.logging.Logger;

public final class ClassName implements Comparable<ClassName> {
   private static final Logger LOG = Log.getLogger();
   private final String name;

   public ClassName(String name) {
      this.name = name.replace('.', '/').intern();
   }

   public ClassName(Class<?> clazz) {
      this(clazz.getName());
   }

   public static ClassName fromString(String clazz) {
      return new ClassName(clazz);
   }

   public static ClassName fromClass(Class<?> clazz) {
      return new ClassName(clazz);
   }

   public String asJavaName() {
      return this.name.replace('/', '.');
   }

   public String asInternalName() {
      return this.name;
   }

   public ClassName getNameWithoutPackage() {
      int lastSeparator = this.name.lastIndexOf(47);
      return lastSeparator != -1 ? new ClassName(this.name.substring(lastSeparator + 1, this.name.length())) : this;
   }

   public ClassName getPackage() {
      int lastSeparator = this.name.lastIndexOf(47);
      return lastSeparator != -1 ? new ClassName(this.name.substring(0, lastSeparator)) : new ClassName("");
   }

   public ClassName withoutPrefixChars(int prefixLength) {
      String nameWithoutPackage = this.getNameWithoutPackage().asJavaName();
      return new ClassName(this.getPackage().asJavaName() + "/" + nameWithoutPackage.substring(prefixLength, nameWithoutPackage.length()));
   }

   public ClassName withoutSuffixChars(int suffixLength) {
      String nameWithoutPacakge = this.getNameWithoutPackage().asJavaName();
      return new ClassName(this.getPackage().asJavaName() + "/" + nameWithoutPacakge.substring(0, nameWithoutPacakge.length() - suffixLength));
   }

   public static F<String, ClassName> stringToClassName() {
      return new F<String, ClassName>() {
         public ClassName apply(String clazz) {
            return ClassName.fromString(clazz);
         }
      };
   }

   public static F<ClassName, Option<Class<?>>> nameToClass() {
      return nameToClass(IsolationUtils.getContextClassLoader());
   }

   public static F<ClassName, Option<Class<?>>> nameToClass(final ClassLoader loader) {
      return new F<ClassName, Option<Class<?>>>() {
         public Option<Class<?>> apply(ClassName className) {
            try {
               Class<?> clazz = Class.forName(className.asJavaName(), false, loader);
               return Option.some(clazz);
            } catch (ClassNotFoundException var3) {
               ClassName.LOG.warning("Could not load " + className + " (ClassNotFoundException: " + var3.getMessage() + ")");
               return Option.none();
            } catch (NoClassDefFoundError var4) {
               ClassName.LOG.warning("Could not load " + className + " (NoClassDefFoundError: " + var4.getMessage() + ")");
               return Option.none();
            } catch (LinkageError var5) {
               ClassName.LOG.warning("Could not load " + className + " " + var5.getMessage());
               return Option.none();
            } catch (SecurityException var6) {
               ClassName.LOG.warning("Could not load " + className + " " + var6.getMessage());
               return Option.none();
            }
         }
      };
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         ClassName other = (ClassName)obj;
         if (this.name == null) {
            if (other.name != null) {
               return false;
            }
         } else if (!this.name.equals(other.name)) {
            return false;
         }

         return true;
      }
   }

   public String toString() {
      return this.asJavaName();
   }

   public int compareTo(ClassName o) {
      return this.asJavaName().compareTo(o.asJavaName());
   }
}
