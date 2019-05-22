package com.mks.api;

import com.mks.api.response.APIException;
import java.util.Iterator;

public interface IntegrationPoint extends IntegrationVersionRequest {
   String getHostname();

   int getPort();

   boolean isClientIntegrationPoint();

   boolean isSecure();

   Session createSession() throws APIException;

   Session createSession(String var1, String var2) throws APIException;

   Session createSession(String var1, String var2, int var3, int var4) throws APIException;

   Session getCommonSession() throws APIException;

   Session getCommonSession(String var1, String var2) throws APIException;

   Iterator getSessions();

   boolean getAutoStartIntegrityClient();

   void setAutoStartIntegrityClient(boolean var1);

   void release();
}
