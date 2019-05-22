package com.gzoltar.shaded.org.jacoco.core.internal.data;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CompactDataInput extends DataInputStream {
   public CompactDataInput(InputStream in) {
      super(in);
   }

   public int readVarInt() throws IOException {
      int value = 255 & this.readByte();
      return (value & 128) == 0 ? value : value & 127 | this.readVarInt() << 7;
   }

   public boolean[] readBooleanArray() throws IOException {
      boolean[] value = new boolean[this.readVarInt()];
      int buffer = 0;

      for(int i = 0; i < value.length; ++i) {
         if (i % 8 == 0) {
            buffer = this.readByte();
         }

         value[i] = (buffer & 1) != 0;
         buffer >>>= 1;
      }

      return value;
   }
}
