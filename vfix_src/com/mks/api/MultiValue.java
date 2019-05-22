package com.mks.api;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MultiValue {
   protected String separator;
   protected List values;

   public MultiValue() {
      this(":");
   }

   public MultiValue(String separator) {
      this.separator = separator;
      this.values = new LinkedList();
   }

   public String getSeparator() {
      return this.separator;
   }

   public void setSeparator(String separator) {
      this.separator = separator;
   }

   public List getValues() {
      return this.values;
   }

   public void setValues(List values) {
      this.values = new LinkedList(values);
   }

   public void setValues(String[] values) {
      this.values = Arrays.asList(values);
   }

   public void add(String value) {
      this.values.add(value);
   }

   public void add(MultiValue value) {
      this.values.add(value.toString());
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      Iterator it = this.values.iterator();
      if (it.hasNext()) {
         sb.append(it.next());
      }

      while(it.hasNext()) {
         sb.append(this.separator);
         sb.append(it.next());
      }

      return sb.toString();
   }
}
