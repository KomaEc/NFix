package com.mks.api.commands;

public interface IWorkingFileCompatibleCommand extends ICommandBase {
   void setCwd(String var1);

   void setSandbox(String var1);
}
