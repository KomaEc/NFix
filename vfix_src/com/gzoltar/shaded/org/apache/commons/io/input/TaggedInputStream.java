package com.gzoltar.shaded.org.apache.commons.io.input;

import com.gzoltar.shaded.org.apache.commons.io.TaggedIOException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.UUID;

public class TaggedInputStream extends ProxyInputStream {
   private final Serializable tag = UUID.randomUUID();

   public TaggedInputStream(InputStream proxy) {
      super(proxy);
   }

   public boolean isCauseOf(Throwable exception) {
      return TaggedIOException.isTaggedWith(exception, this.tag);
   }

   public void throwIfCauseOf(Throwable throwable) throws IOException {
      TaggedIOException.throwCauseIfTaggedWith(throwable, this.tag);
   }

   protected void handleIOException(IOException e) throws IOException {
      throw new TaggedIOException(e, this.tag);
   }
}
