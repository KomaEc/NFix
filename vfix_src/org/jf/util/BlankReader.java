package org.jf.util;

import java.io.IOException;
import java.io.Reader;
import javax.annotation.Nonnull;

public class BlankReader extends Reader {
   public static final BlankReader INSTANCE = new BlankReader();

   public int read(@Nonnull char[] chars, int i, int i2) throws IOException {
      return -1;
   }

   public void close() throws IOException {
   }
}
