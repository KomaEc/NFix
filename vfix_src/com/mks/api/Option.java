package com.mks.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Option {
   private static final String FLAG_PREFIX = "-";
   private static final String OPTION_PREFIX = "--";
   private String name;
   private String canonicalName;
   private List values;
   private String separator;

   public Option(String name) {
      this(name, (String)null);
   }

   public Option(String name, MultiValue mv) {
      this(name, mv.toString());
   }

   public Option(String name, String value) {
      if (name != null && name.startsWith("--")) {
         this.name = name.substring(2);
      } else if (name != null && name.startsWith("-")) {
         this.name = name.substring(1);
      } else {
         this.name = name;
      }

      this.canonicalName = name == null ? null : name.toLowerCase();
      this.separator = ",";
      this.values = new LinkedList();
      if (value != null) {
         this.values.add(value);
      }

   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      if (this.values.size() < 1) {
         return null;
      } else {
         StringBuffer value = new StringBuffer("");
         Iterator it = this.values.iterator();
         if (it.hasNext()) {
            if (value.length() > 0) {
               value.append(this.separator);
            }

            value.append(it.next());
         }

         while(it.hasNext()) {
            value.append(this.separator);
            value.append(it.next());
         }

         return value.toString();
      }
   }

   public void add(String value) {
      this.values.add(value);
   }

   public void add(Collection values) {
      this.values.addAll(values);
   }

   public void add(MultiValue mv) {
      this.add(mv.toString());
   }

   public String getSeparator() {
      return this.separator;
   }

   public void setSeparator(String separator) {
      if (!this.separator.equals(separator)) {
         this.separator = separator;
      }

   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      boolean isFlag = false;
      if (this.name.length() == 1) {
         sb.append("-");
         sb.append(this.name);
         isFlag = true;
      } else {
         sb.append("--");
         sb.append(this.name);
      }

      String value = this.getValue();
      if (value != null) {
         if (isFlag) {
            sb.append(" ");
         } else {
            sb.append("=");
         }

         sb.append(value);
      }

      return sb.toString();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Option other = (Option)o;
         return (this.canonicalName == other.canonicalName || this.canonicalName != null && this.canonicalName.equals(other.canonicalName)) && (this.separator == other.separator || this.separator != null && this.separator.equals(other.separator)) && (this.values == other.values || this.values != null && this.values.equals(other.values));
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.canonicalName.hashCode() ^ this.values.hashCode() ^ this.separator.hashCode();
   }
}
