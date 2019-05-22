package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

class CharSource$StringCharSource extends CharSource.CharSequenceCharSource {
   protected CharSource$StringCharSource(String seq) {
      super(seq);
   }

   public Reader openStream() {
      return new StringReader((String)this.seq);
   }

   public long copyTo(Appendable appendable) throws IOException {
      appendable.append(this.seq);
      return (long)this.seq.length();
   }

   public long copyTo(CharSink sink) throws IOException {
      Preconditions.checkNotNull(sink);
      Closer closer = Closer.create();

      long var4;
      try {
         Writer writer = (Writer)closer.register(sink.openStream());
         writer.write((String)this.seq);
         var4 = (long)this.seq.length();
      } catch (Throwable var9) {
         throw closer.rethrow(var9);
      } finally {
         closer.close();
      }

      return var4;
   }
}
