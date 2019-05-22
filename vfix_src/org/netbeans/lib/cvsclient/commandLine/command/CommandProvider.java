package org.netbeans.lib.cvsclient.commandLine.command;

import java.io.PrintStream;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.GlobalOptions;

public interface CommandProvider {
   String getName();

   String[] getSynonyms();

   Command createCommand(String[] var1, int var2, GlobalOptions var3, String var4);

   String getUsage();

   void printShortDescription(PrintStream var1);

   void printLongDescription(PrintStream var1);
}
