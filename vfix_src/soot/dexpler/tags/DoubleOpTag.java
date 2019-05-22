package soot.dexpler.tags;

import soot.tagkit.Tag;

public class DoubleOpTag implements Tag {
   public String getName() {
      return "DoubleOpTag";
   }

   public byte[] getValue() {
      byte[] b = new byte[]{0};
      return b;
   }
}
