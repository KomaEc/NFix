package com.gzoltar.shaded.org.pitest.process;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaunchOptions {
   private final JavaAgent javaAgentFinder;
   private final List<String> childJVMArgs;
   private final JavaExecutableLocator javaExecutable;
   private final Map<String, String> environmentVariables;

   public LaunchOptions(JavaAgent javaAgentFinder) {
      this(javaAgentFinder, new DefaultJavaExecutableLocator(), Collections.emptyList(), new HashMap());
   }

   public LaunchOptions(JavaAgent javaAgentFinder, JavaExecutableLocator javaExecutable, List<String> childJVMArgs, Map<String, String> environmentVariables) {
      this.javaAgentFinder = javaAgentFinder;
      this.childJVMArgs = childJVMArgs;
      this.javaExecutable = javaExecutable;
      this.environmentVariables = environmentVariables;
   }

   public JavaAgent getJavaAgentFinder() {
      return this.javaAgentFinder;
   }

   public List<String> getChildJVMArgs() {
      return this.childJVMArgs;
   }

   public String getJavaExecutable() {
      return this.javaExecutable.javaExecutable();
   }

   public Map<String, String> getEnvironmentVariables() {
      return this.environmentVariables;
   }
}
