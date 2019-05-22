package org.jf.dexlib2.dexbacked;

import javax.annotation.Nonnull;

public class DexReader extends BaseDexReader<DexBackedDexFile> {
   public DexReader(@Nonnull DexBackedDexFile dexFile, int offset) {
      super(dexFile, offset);
   }
}
