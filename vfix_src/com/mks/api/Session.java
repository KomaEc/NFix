package com.mks.api;

public interface Session extends CmdRunnerCreator {
   IntegrationPoint getIntegrationPoint();

   void setAutoReconnect(boolean var1);

   boolean getAutoReconnect();

   int getTimeout();

   void setTimeout(int var1);

   boolean isCommon();
}
