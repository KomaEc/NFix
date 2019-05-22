package org.apache.tools.ant.types;

import java.util.Enumeration;
import java.util.Vector;

public class FilterSetCollection {
   private Vector filterSets = new Vector();

   public FilterSetCollection() {
   }

   public FilterSetCollection(FilterSet filterSet) {
      this.addFilterSet(filterSet);
   }

   public void addFilterSet(FilterSet filterSet) {
      this.filterSets.addElement(filterSet);
   }

   public String replaceTokens(String line) {
      String replacedLine = line;

      FilterSet filterSet;
      for(Enumeration e = this.filterSets.elements(); e.hasMoreElements(); replacedLine = filterSet.replaceTokens(replacedLine)) {
         filterSet = (FilterSet)e.nextElement();
      }

      return replacedLine;
   }

   public boolean hasFilters() {
      Enumeration e = this.filterSets.elements();

      FilterSet filterSet;
      do {
         if (!e.hasMoreElements()) {
            return false;
         }

         filterSet = (FilterSet)e.nextElement();
      } while(!filterSet.hasFilters());

      return true;
   }
}
