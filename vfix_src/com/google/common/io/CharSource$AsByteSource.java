package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

final class CharSource$AsByteSource extends ByteSource {
   final Charset charset;
   // $FF: synthetic field
   final CharSource this$0;

   CharSource$AsByteSource(CharSource var1, Charset charset) {
      this.this$0 = var1;
      this.charset = (Charset)Preconditions.checkNotNull(charset);
   }

   public CharSource asCharSource(Charset charset) {
      return charset.equals(this.charset) ? this.this$0 : super.asCharSource(charset);
   }

   public InputStream openStream() throws IOException {
      return new ReaderInputStream(this.this$0.openStream(), this.charset, 8192);
   }

   public String toString() {
      return this.this$0.toString() + ".asByteSource(" + this.charset + ")";
   }
}
