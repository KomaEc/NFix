package org.apache.maven.toolchain;

import org.apache.maven.execution.MavenSession;

public interface ToolchainManagerPrivate {
   String ROLE = ToolchainManagerPrivate.class.getName();

   ToolchainPrivate[] getToolchainsForType(String var1) throws MisconfiguredToolchainException;

   void storeToolchainToBuildContext(ToolchainPrivate var1, MavenSession var2);
}
