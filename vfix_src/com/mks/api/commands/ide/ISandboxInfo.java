package com.mks.api.commands.ide;

import java.io.File;

public interface ISandboxInfo {
   String VARIANT_SANDBOX = "variant";
   String BUILD_SANDBOX = "build";
   String NORMAL_SANDBOX = "normal";
   String SANDBOX_FIELD = "sandboxName";
   String PARENT_FIELD = "sandboxParent";
   String HOST_FIELD = "server";
   String PORT_FIELD = "serverPort";
   String PROJECT_FIELD = "projectName";
   String NORMAL_FIELD = "isNormal";
   String VARIANT_FIELD = "isVariant";
   String BUILD_FIELD = "isBuild";
   String DEV_PATH_FIELD = "developmentPath";
   String PENDING_FIELD = "isPending";
   String SUB_SANDBOX_FIELD = "isSubsandbox";
   String CONFIG_PATH_FIELD = "fullConfigSyntax";

   String getSandboxName();

   File getSandboxFile();

   File getSandboxLocation();

   String getHostname();

   String getPort();

   boolean isVariant();

   boolean isBuild();

   File getProject();

   String getTypeInfo();

   boolean isSubsandbox();

   String getParentName();

   boolean isPending();

   String getConfigPath();

   String getDevPath();
}
