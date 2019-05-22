package com.mks.api.commands;

public interface IHasChangePackage extends ICommandBase {
   void setCpid(String var1);

   void setCloseCP(boolean var1);

   void resetCloseCP();

   boolean isCloseCPOverridden();
}
