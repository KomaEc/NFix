package com.google.inject.name;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.lang.annotation.Annotation;

class NamedImpl implements Named, Serializable {
   private final String value;
   private static final long serialVersionUID = 0L;

   public NamedImpl(String value) {
      this.value = (String)Preconditions.checkNotNull(value, "name");
   }

   public String value() {
      return this.value;
   }

   public int hashCode() {
      return 127 * "value".hashCode() ^ this.value.hashCode();
   }

   public boolean equals(Object o) {
      if (!(o instanceof Named)) {
         return false;
      } else {
         Named other = (Named)o;
         return this.value.equals(other.value());
      }
   }

   public String toString() {
      String var1 = String.valueOf(String.valueOf(Named.class.getName()));
      String var2 = String.valueOf(String.valueOf(this.value));
      return (new StringBuilder(9 + var1.length() + var2.length())).append("@").append(var1).append("(value=").append(var2).append(")").toString();
   }

   public Class<? extends Annotation> annotationType() {
      return Named.class;
   }
}
