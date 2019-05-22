package org.testng.collections;

import java.util.List;
import org.testng.util.Strings;

public final class Objects {
   private Objects() {
   }

   public static Objects.ToStringHelper toStringHelper(Class<?> class1) {
      return new Objects.ToStringHelper(class1.getSimpleName());
   }

   public static class ToStringHelper {
      private String m_className;
      private List<Objects.ValueHolder> values = Lists.newArrayList();
      private boolean m_omitNulls = false;
      private boolean m_omitEmptyStrings = false;

      public ToStringHelper(String className) {
         this.m_className = className;
      }

      public Objects.ToStringHelper omitNulls() {
         this.m_omitNulls = true;
         return this;
      }

      public Objects.ToStringHelper omitEmptyStrings() {
         this.m_omitEmptyStrings = true;
         return this;
      }

      public Objects.ToStringHelper add(String name, String value) {
         this.values.add(new Objects.ValueHolder(name, this.s(value)));
         return this;
      }

      public Objects.ToStringHelper add(String name, Object value) {
         this.values.add(new Objects.ValueHolder(name, this.s(value)));
         return this;
      }

      private String s(Object o) {
         return o != null ? (o.toString().isEmpty() ? "\"\"" : o.toString()) : "{null}";
      }

      public String toString() {
         StringBuilder result = new StringBuilder("[" + this.m_className + " ");

         for(int i = 0; i < this.values.size(); ++i) {
            Objects.ValueHolder vh = (Objects.ValueHolder)this.values.get(i);
            if ((!this.m_omitNulls || !vh.isNull()) && (!this.m_omitEmptyStrings || !vh.isEmptyString())) {
               if (i > 0) {
                  result.append(" ");
               }

               result.append(vh.toString());
            }
         }

         result.append("]");
         return result.toString();
      }
   }

   private static class ValueHolder {
      private String m_name;
      private String m_value;

      public ValueHolder(String name, String value) {
         this.m_name = name;
         this.m_value = value;
      }

      boolean isNull() {
         return this.m_value == null;
      }

      public String toString() {
         return this.m_name + "=" + this.m_value;
      }

      public boolean isEmptyString() {
         return Strings.isNullOrEmpty(this.m_value);
      }
   }
}
