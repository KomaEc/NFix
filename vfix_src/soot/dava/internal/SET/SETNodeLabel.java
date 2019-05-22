package soot.dava.internal.SET;

import soot.G;

public class SETNodeLabel {
   private String name = null;

   public void set_Name() {
      if (this.name == null) {
         this.name = "label_" + Integer.toString(G.v().SETNodeLabel_uniqueId++);
      }

   }

   public void set_Name(String name) {
      this.name = name;
   }

   public String toString() {
      return this.name;
   }

   public void clear_Name() {
      this.name = null;
   }
}
