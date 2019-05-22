package org.apache.commons.beanutils.converters;

import org.apache.commons.beanutils.Converter;

public final class StringConverter implements Converter {
   public Object convert(Class type, Object value) {
      return value == null ? (String)null : value.toString();
   }
}
