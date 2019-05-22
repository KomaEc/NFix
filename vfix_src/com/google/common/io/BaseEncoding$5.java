package com.google.common.io;

import java.io.IOException;
import java.io.Writer;

final class BaseEncoding$5 extends Writer {
   // $FF: synthetic field
   final Appendable val$seperatingAppendable;
   // $FF: synthetic field
   final Writer val$delegate;

   BaseEncoding$5(Appendable var1, Writer var2) {
      this.val$seperatingAppendable = var1;
      this.val$delegate = var2;
   }

   public void write(int c) throws IOException {
      this.val$seperatingAppendable.append((char)c);
   }

   public void write(char[] chars, int off, int len) throws IOException {
      throw new UnsupportedOperationException();
   }

   public void flush() throws IOException {
      this.val$delegate.flush();
   }

   public void close() throws IOException {
      this.val$delegate.close();
   }
}
