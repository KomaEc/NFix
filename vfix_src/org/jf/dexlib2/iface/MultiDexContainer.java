package org.jf.dexlib2.iface;

import java.io.IOException;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcodes;

public interface MultiDexContainer<T extends DexFile> {
   @Nonnull
   List<String> getDexEntryNames() throws IOException;

   @Nullable
   T getEntry(@Nonnull String var1) throws IOException;

   @Nonnull
   Opcodes getOpcodes();

   public interface MultiDexFile extends DexFile {
      @Nonnull
      String getEntryName();

      @Nonnull
      MultiDexContainer<? extends MultiDexContainer.MultiDexFile> getContainer();
   }
}
