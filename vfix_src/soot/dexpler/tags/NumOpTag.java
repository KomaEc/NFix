package soot.dexpler.tags;

import soot.tagkit.Tag;

public class NumOpTag implements Tag {
   public String getName() {
      return "NumOpTag";
   }

   public byte[] getValue() {
      byte[] b = new byte[]{0};
      return b;
   }
}
