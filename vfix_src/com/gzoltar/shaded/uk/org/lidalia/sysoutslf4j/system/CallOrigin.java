package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.system;

final class CallOrigin {
   private final boolean printingStackTrace;
   private final String className;

   private CallOrigin(boolean isStacktrace, String className) {
      this.printingStackTrace = isStacktrace;
      this.className = className;
   }

   boolean isPrintingStackTrace() {
      return this.printingStackTrace;
   }

   String getClassName() {
      return this.className;
   }

   static CallOrigin getCallOrigin(StackTraceElement[] stackTraceElements, String libraryPackageName) {
      boolean isStackTrace = false;
      StackTraceElement[] var3 = stackTraceElements;
      int var4 = stackTraceElements.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         StackTraceElement stackTraceElement = var3[var5];
         String className = stackTraceElement.getClassName();
         if (className.equals(Throwable.class.getName())) {
            isStackTrace = true;
         } else if (outsideThisLibrary(className, libraryPackageName)) {
            className = getOuterClassName(className);
            return new CallOrigin(isStackTrace, className);
         }
      }

      throw new IllegalStateException("Nothing in the stack originated from outside package name " + libraryPackageName);
   }

   private static boolean outsideThisLibrary(String className, String libraryPackageName) {
      return !className.equals(Thread.class.getName()) && !className.startsWith(libraryPackageName);
   }

   private static String getOuterClassName(String className) {
      int startOfInnerClassName = className.indexOf(36);
      String outerClassName;
      if (startOfInnerClassName == -1) {
         outerClassName = className;
      } else {
         outerClassName = className.substring(0, startOfInnerClassName);
      }

      return outerClassName;
   }
}
