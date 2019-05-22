package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

import com.gzoltar.shaded.org.pitest.reloc.xstream.XStreamException;

public class ObjectAccessException extends XStreamException {
   public ObjectAccessException(String message) {
      super(message);
   }

   public ObjectAccessException(String message, Throwable cause) {
      super(message, cause);
   }
}
