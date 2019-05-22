package soot.dexpler.tags;

import soot.tagkit.Tag;

public class FloatOpTag implements Tag {
   public String getName() {
      return "FloatOpTag";
   }

   public byte[] getValue() {
      byte[] b = new byte[]{0};
      return b;
   }
}
