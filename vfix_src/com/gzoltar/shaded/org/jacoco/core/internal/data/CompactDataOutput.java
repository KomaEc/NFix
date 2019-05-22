package com.gzoltar.shaded.org.jacoco.core.internal.data;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CompactDataOutput extends DataOutputStream {
   public CompactDataOutput(OutputStream out) {
      super(out);
   }

   public void writeVarInt(int value) throws IOException {
      if ((value & -128) == 0) {
         this.writeByte(value);
      } else {
         this.writeByte(128 | value & 127);
         this.writeVarInt(value >>> 7);
      }

   }

   public void writeBooleanArray(boolean[] value) throws IOException {
      this.writeVarInt(value.length);
      int buffer = 0;
      int bufferSize = 0;
      boolean[] arr$ = value;
      int len$ = value.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         boolean b = arr$[i$];
         if (b) {
            buffer |= 1 << bufferSize;
         }

         ++bufferSize;
         if (bufferSize == 8) {
            this.writeByte(buffer);
            buffer = 0;
            bufferSize = 0;
         }
      }

      if (bufferSize > 0) {
         this.writeByte(buffer);
      }

   }
}
