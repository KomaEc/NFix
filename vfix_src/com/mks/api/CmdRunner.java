package com.mks.api;

import com.mks.api.response.APIException;
import com.mks.api.response.Response;

public interface CmdRunner {
   Session getSession();

   void interrupt() throws APIException;

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

   Response execute(String[] var1) throws APIException;

   Response execute(Command var1) throws APIException;

   Response executeWithInterim(String[] var1, boolean var2) throws APIException;

   Response executeWithInterim(Command var1, boolean var2) throws APIException;

   void release() throws APIException;

   boolean isFinished();
}
