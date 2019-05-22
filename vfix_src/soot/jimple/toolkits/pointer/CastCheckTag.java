package soot.jimple.toolkits.pointer;

import soot.tagkit.Tag;

public class CastCheckTag implements Tag {
   boolean eliminateCheck;

   CastCheckTag(boolean eliminateCheck) {
      this.eliminateCheck = eliminateCheck;
   }

   public String getName() {
      return "CastCheckTag";
   }

   public byte[] getValue() {
      byte[] ret = new byte[]{(byte)(this.eliminateCheck ? 1 : 0)};
      return ret;
   }

   public String toString() {
      return this.eliminateCheck ? "This cast check can be eliminated." : "This cast check should NOT be eliminated.";
   }

   public boolean canEliminateCheck() {
      return this.eliminateCheck;
   }
}
