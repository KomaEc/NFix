package org.jf.util;

import java.util.BitSet;

public class BitSetUtils {
   public static BitSet bitSetOfIndexes(int... indexes) {
      BitSet bitSet = new BitSet();
      int[] var2 = indexes;
      int var3 = indexes.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int index = var2[var4];
         bitSet.set(index);
      }

      return bitSet;
   }
}
