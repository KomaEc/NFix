package org.apache.maven.toolchain;

import org.apache.maven.toolchain.model.ToolchainModel;

public interface ToolchainFactory {
   String ROLE = ToolchainFactory.class.getName();

   ToolchainPrivate createToolchain(ToolchainModel var1) throws MisconfiguredToolchainException;

   ToolchainPrivate createDefaultToolchain();
}
