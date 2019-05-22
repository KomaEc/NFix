package org.jf.dexlib2.writer.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nonnull;

public interface DexDataStore {
   @Nonnull
   OutputStream outputAt(int var1);

   @Nonnull
   InputStream readAt(int var1);

   void close() throws IOException;
}
