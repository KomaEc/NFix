package com.gzoltar.shaded.org.pitest.util;

public enum ExitCode {
   OK(0),
   OUT_OF_MEMORY(11),
   UNKNOWN_ERROR(13),
   TIMEOUT(14),
   JUNIT_ISSUE(15);

   private final int code;

   private ExitCode(int code) {
      this.code = code;
   }

   public int getCode() {
      return this.code;
   }

   public static ExitCode fromCode(int code) {
      ExitCode[] arr$ = values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ExitCode each = arr$[i$];
         if (each.getCode() == code) {
            return each;
         }
      }

      return UNKNOWN_ERROR;
   }

   public boolean isOk() {
      return this.equals(OK);
   }
}
