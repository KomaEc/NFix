package com.gzoltar.shaded.org.pitest.classinfo;

import com.gzoltar.shaded.org.pitest.functional.F5;

public enum BridgeMethodFilter implements F5<Integer, String, String, String, String[], Boolean> {
   INSTANCE;

   public Boolean apply(Integer access, String name, String desc, String signature, String[] exceptions) {
      return this.isSynthetic(access);
   }

   private boolean isSynthetic(Integer access) {
      return (access & 64) == 0;
   }
}
