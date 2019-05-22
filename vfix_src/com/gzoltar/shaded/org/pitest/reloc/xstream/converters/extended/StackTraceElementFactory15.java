package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

class StackTraceElementFactory15 extends StackTraceElementFactory {
   protected StackTraceElement create(String declaringClass, String methodName, String fileName, int lineNumber) {
      return new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
   }
}
