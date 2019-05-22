package com.mks.api;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OptionList {
   private List list = new LinkedList();

   public void add(Option option) {
      this.list.add(option);
   }

   public void add(OptionList list) {
      this.list.addAll(list.list);
   }

   public void add(String name, String value) {
      this.add(new Option(name, value));
   }

   public void clear() {
      this.list.clear();
   }

   public Iterator getOptions() {
      return this.list.iterator();
   }

   public int size() {
      return this.list.size();
   }

   public Option getOption(String name) {
      Option option = null;
      Iterator it = this.getOptions();

      while(it.hasNext()) {
         Option test = (Option)it.next();
         if (test.getName().equalsIgnoreCase(name)) {
            option = test;
         }
      }

      return option;
   }

   public boolean hasOption(String name) {
      return this.getOption(name) != null;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() != o.getClass()) {
         OptionList other = (OptionList)o;
         return this.list.containsAll(other.list) && other.list.containsAll(this.list);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.list.hashCode();
   }
}
