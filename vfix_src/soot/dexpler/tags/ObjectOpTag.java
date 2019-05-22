package soot.dexpler.tags;

import soot.tagkit.Tag;

public class ObjectOpTag implements Tag {
   public String getName() {
      return "ObjectOpTag";
   }

   public byte[] getValue() {
      byte[] b = new byte[]{0};
      return b;
   }
}
