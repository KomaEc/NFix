package com.gzoltar.shaded.org.pitest.reloc.xstream.core.util;

import java.util.regex.Pattern;

public class Types {
   private static final Pattern lambdaPattern = Pattern.compile(".*\\$\\$Lambda\\$[0-9]+/.*");

   public static final boolean isLambdaType(Class<?> type) {
      return type != null && type.isSynthetic() && lambdaPattern.matcher(type.getSimpleName()).matches();
   }
}
