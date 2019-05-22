package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PatternSet implements Serializable {
   private List<String> includes;
   private List<String> excludes;

   public void addExclude(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("PatternSet.addExcludes(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getExcludes().add(string);
      }
   }

   public void addInclude(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("PatternSet.addIncludes(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getIncludes().add(string);
      }
   }

   public List<String> getExcludes() {
      if (this.excludes == null) {
         this.excludes = new ArrayList();
      }

      return this.excludes;
   }

   public List<String> getIncludes() {
      if (this.includes == null) {
         this.includes = new ArrayList();
      }

      return this.includes;
   }

   public void removeExclude(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("PatternSet.removeExcludes(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getExcludes().remove(string);
      }
   }

   public void removeInclude(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("PatternSet.removeIncludes(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getIncludes().remove(string);
      }
   }

   public void setExcludes(List<String> excludes) {
      this.excludes = excludes;
   }

   public void setIncludes(List<String> includes) {
      this.includes = includes;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("PatternSet [includes: {");
      Iterator i = this.getIncludes().iterator();

      String str;
      while(i.hasNext()) {
         str = (String)i.next();
         sb.append(str).append(", ");
      }

      if (sb.substring(sb.length() - 2).equals(", ")) {
         sb.delete(sb.length() - 2, sb.length());
      }

      sb.append("}, excludes: {");
      i = this.getExcludes().iterator();

      while(i.hasNext()) {
         str = (String)i.next();
         sb.append(str).append(", ");
      }

      if (sb.substring(sb.length() - 2).equals(", ")) {
         sb.delete(sb.length() - 2, sb.length());
      }

      sb.append("}]");
      return sb.toString();
   }
}
