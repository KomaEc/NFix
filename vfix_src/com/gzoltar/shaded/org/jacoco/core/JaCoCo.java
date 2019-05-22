package com.gzoltar.shaded.org.jacoco.core;

import java.util.ResourceBundle;

public final class JaCoCo {
   public static final String VERSION;
   public static final String HOMEURL;
   public static final String RUNTIMEPACKAGE;

   private JaCoCo() {
   }

   static {
      ResourceBundle bundle = ResourceBundle.getBundle("com.gzoltar.shaded.org.jacoco.core.jacoco");
      VERSION = bundle.getString("VERSION");
      HOMEURL = bundle.getString("HOMEURL");
      RUNTIMEPACKAGE = bundle.getString("RUNTIMEPACKAGE");
   }
}
