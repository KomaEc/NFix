package com.gzoltar.shaded.org.pitest.reloc.xstream.mapper;

import com.gzoltar.shaded.org.pitest.reloc.xstream.XStreamException;

public class CannotResolveClassException extends XStreamException {
   public CannotResolveClassException(String className) {
      super(className);
   }

   public CannotResolveClassException(String className, Throwable cause) {
      super(className, cause);
   }
}
