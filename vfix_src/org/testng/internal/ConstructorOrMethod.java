package org.testng.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ConstructorOrMethod {
   private Method m_method;
   private Constructor m_constructor;
   private boolean m_enabled = true;

   public ConstructorOrMethod(Method m) {
      this.m_method = m;
   }

   public ConstructorOrMethod(Constructor c) {
      this.m_constructor = c;
   }

   public Class<?> getDeclaringClass() {
      return this.getMethod() != null ? this.getMethod().getDeclaringClass() : this.getConstructor().getDeclaringClass();
   }

   public String getName() {
      return this.getMethod() != null ? this.getMethod().getName() : this.getConstructor().getName();
   }

   public Class[] getParameterTypes() {
      return this.getMethod() != null ? this.getMethod().getParameterTypes() : this.getConstructor().getParameterTypes();
   }

   public Method getMethod() {
      return this.m_method;
   }

   public Constructor getConstructor() {
      return this.m_constructor;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.getConstructor() == null ? 0 : this.getConstructor().hashCode());
      result = 31 * result + (this.getMethod() == null ? 0 : this.getMethod().hashCode());
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
         ConstructorOrMethod other = (ConstructorOrMethod)obj;
         if (this.getConstructor() == null) {
            if (other.getConstructor() != null) {
               return false;
            }
         } else if (!this.getConstructor().equals(other.getConstructor())) {
            return false;
         }

         if (this.getMethod() == null) {
            if (other.getMethod() != null) {
               return false;
            }
         } else if (!this.getMethod().equals(other.getMethod())) {
            return false;
         }

         return true;
      }
   }

   public void setEnabled(boolean enabled) {
      this.m_enabled = enabled;
   }

   public boolean getEnabled() {
      return this.m_enabled;
   }

   public String toString() {
      return this.m_method != null ? this.m_method.toString() : this.m_constructor.toString();
   }
}
