package com.gzoltar.instrumentation.utils;

public class SystemProperties {
   public static final String ARRAY_SEPARATOR = ":";
   public static final String PATH_SEPARATOR = System.getProperty("path.separator");
   public static final String FILE_SEPARATOR = System.getProperty("file.separator");
   public static final String JAVA_HOME;
   public static final String OS_NAME;

   static {
      JAVA_HOME = System.getProperty("java.home") + FILE_SEPARATOR + "bin" + FILE_SEPARATOR + "java";
      OS_NAME = System.getProperty("os.name").toLowerCase();
   }
}
