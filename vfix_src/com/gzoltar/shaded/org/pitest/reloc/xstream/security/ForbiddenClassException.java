package com.gzoltar.shaded.org.pitest.reloc.xstream.security;

import com.gzoltar.shaded.org.pitest.reloc.xstream.XStreamException;

public class ForbiddenClassException extends XStreamException {
   public ForbiddenClassException(Class type) {
      super(type == null ? "null" : type.getName());
   }
}
