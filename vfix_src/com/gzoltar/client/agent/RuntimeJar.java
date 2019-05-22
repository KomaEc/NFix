package com.gzoltar.client.agent;

public class RuntimeJar extends JarAbstract {
   private static final String TEMP_FILE_PREFIX = "runtime";
   private static final String[] AGENT_PACKAGES = new String[]{"com/gzoltar/instrumentation/runtime"};

   public RuntimeJar() {
      super("runtime", (String)null, AGENT_PACKAGES);
   }
}
