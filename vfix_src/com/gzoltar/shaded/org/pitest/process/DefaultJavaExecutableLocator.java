package com.gzoltar.shaded.org.pitest.process;

public class DefaultJavaExecutableLocator implements JavaExecutableLocator {
   public String javaExecutable() {
      String separator = System.getProperty("file.separator");
      return System.getProperty("java.home") + separator + "bin" + separator + "java";
   }
}
