package com.gzoltar.shaded.org.pitest.reloc.xstream.io;

import com.gzoltar.shaded.org.pitest.reloc.xstream.XStreamException;

public class StreamException extends XStreamException {
   public StreamException(Throwable e) {
      super(e);
   }

   public StreamException(String message) {
      super(message);
   }

   public StreamException(String message, Throwable cause) {
      super(message, cause);
   }
}
