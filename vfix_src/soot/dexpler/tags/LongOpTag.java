package soot.dexpler.tags;

import soot.tagkit.Tag;

public class LongOpTag implements Tag {
   public String getName() {
      return "LongOpTag";
   }

   public byte[] getValue() {
      byte[] b = new byte[]{0};
      return b;
   }
}
