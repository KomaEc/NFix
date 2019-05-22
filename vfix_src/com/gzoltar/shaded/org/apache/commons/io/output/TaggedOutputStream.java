package com.gzoltar.shaded.org.apache.commons.io.output;

import com.gzoltar.shaded.org.apache.commons.io.TaggedIOException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.UUID;

public class TaggedOutputStream extends ProxyOutputStream {
   private final Serializable tag = UUID.randomUUID();

   public TaggedOutputStream(OutputStream proxy) {
      super(proxy);
   }

   public boolean isCauseOf(Exception exception) {
      return TaggedIOException.isTaggedWith(exception, this.tag);
   }

   public void throwIfCauseOf(Exception exception) throws IOException {
      TaggedIOException.throwCauseIfTaggedWith(exception, this.tag);
   }

   protected void handleIOException(IOException e) throws IOException {
      throw new TaggedIOException(e, this.tag);
   }
}
