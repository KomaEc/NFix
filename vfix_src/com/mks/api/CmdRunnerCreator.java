package com.mks.api;

import com.mks.api.response.APIException;
import java.io.IOException;
import java.util.Iterator;

public interface CmdRunnerCreator extends IntegrationVersionRequest {
   CmdRunner createCmdRunner() throws APIException;

   Iterator getCmdRunners();

   void release() throws IOException, APIException;

   void release(boolean var1) throws IOException, APIException;

   void setDefaultImpersonationUser(String var1);

   String getDefaultImpersonationUser();

   String getDefaultHostname();

   int getDefaultPort();

   void setDefaultHostname(String var1);

   void setDefaultPort(int var1);

   String getDefaultUsername();

   String getDefaultPassword();

   void setDefaultUsername(String var1);

   void setDefaultPassword(String var1);
}
