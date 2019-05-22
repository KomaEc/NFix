package soot.javaToJimple;

import java.util.ArrayList;
import polyglot.util.IdentityKey;

public class AnonLocalClassInfo {
   private boolean inStaticMethod;
   private ArrayList<IdentityKey> finalLocalsAvail;
   private ArrayList<IdentityKey> finalLocalsUsed;

   public boolean inStaticMethod() {
      return this.inStaticMethod;
   }

   public void inStaticMethod(boolean b) {
      this.inStaticMethod = b;
   }

   public ArrayList<IdentityKey> finalLocalsAvail() {
      return this.finalLocalsAvail;
   }

   public void finalLocalsAvail(ArrayList<IdentityKey> list) {
      this.finalLocalsAvail = list;
   }

   public ArrayList<IdentityKey> finalLocalsUsed() {
      return this.finalLocalsUsed;
   }

   public void finalLocalsUsed(ArrayList<IdentityKey> list) {
      this.finalLocalsUsed = list;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("static: ");
      sb.append(this.inStaticMethod);
      sb.append(" finalLocalsAvail: ");
      sb.append(this.finalLocalsAvail);
      sb.append(" finalLocalsUsed: ");
      sb.append(this.finalLocalsUsed);
      return sb.toString();
   }
}
