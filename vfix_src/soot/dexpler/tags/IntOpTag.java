package soot.dexpler.tags;

import soot.tagkit.Tag;

public class IntOpTag implements Tag {
   public String getName() {
      return "IntOpTag";
   }

   public byte[] getValue() {
      byte[] b = new byte[]{0};
      return b;
   }
}
