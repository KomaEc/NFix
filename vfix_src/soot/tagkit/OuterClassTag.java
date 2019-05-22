package soot.tagkit;

import java.io.UnsupportedEncodingException;
import soot.SootClass;

public class OuterClassTag implements Tag {
   SootClass outerClass;
   String simpleName;
   boolean anon;

   public OuterClassTag(SootClass outer, String simpleName, boolean anon) {
      this.outerClass = outer;
      this.simpleName = simpleName;
      this.anon = anon;
   }

   public String getName() {
      return "OuterClassTag";
   }

   public byte[] getValue() {
      try {
         return this.outerClass.getName().getBytes("UTF8");
      } catch (UnsupportedEncodingException var2) {
         return new byte[0];
      }
   }

   public SootClass getOuterClass() {
      return this.outerClass;
   }

   public String getSimpleName() {
      return this.simpleName;
   }

   public boolean isAnon() {
      return this.anon;
   }

   public String toString() {
      return "[outer class=" + this.outerClass.getName() + "]";
   }
}
