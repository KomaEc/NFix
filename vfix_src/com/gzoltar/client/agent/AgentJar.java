package com.gzoltar.client.agent;

public class AgentJar extends JarAbstract {
   private static final String TEMP_FILE_PREFIX = "gzoltar.agent";
   private static final String PREMAIN_CLASS = "org.jacoco.agent.rt.internal_18f3f67.PreMain";
   private static final String[] AGENT_PACKAGES = new String[]{"junit", "org/junit", "org/hamcrest", "org/testng", "org/jacoco/agent/rt", "major/mutation", "com/gzoltar/nnative", "com/gzoltar/instrumentation", "com/gzoltar/client", "com/gzoltar/shaded"};

   public AgentJar() {
      super("gzoltar.agent", "org.jacoco.agent.rt.internal_18f3f67.PreMain", AGENT_PACKAGES);
   }
}
