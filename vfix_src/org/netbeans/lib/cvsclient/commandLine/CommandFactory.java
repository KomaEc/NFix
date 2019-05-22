package org.netbeans.lib.cvsclient.commandLine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.commandLine.command.CommandProvider;

public class CommandFactory {
   private static final String[] COMMAND_CLASSES = new String[]{"Import", "add", "annotate", "checkout", "commit", "diff", "export", "locbundlecheck", "log", "rannotate", "remove", "rlog", "rtag", "status", "tag", "update"};
   private static CommandFactory instance;
   private Map commandProvidersByNames;

   private CommandFactory() {
      this.createCommandProviders();
   }

   private void createCommandProviders() {
      this.commandProvidersByNames = new HashMap();
      String var1 = CommandFactory.class.getPackage().getName() + ".command.";

      for(int var2 = 0; var2 < COMMAND_CLASSES.length; ++var2) {
         try {
            Class var3 = Class.forName(var1 + COMMAND_CLASSES[var2]);
            CommandProvider var4 = (CommandProvider)var3.newInstance();
            this.commandProvidersByNames.put(var4.getName(), var4);
            String[] var5 = var4.getSynonyms();

            for(int var6 = 0; var6 < var5.length; ++var6) {
               this.commandProvidersByNames.put(var5[var6], var4);
            }
         } catch (Exception var7) {
            System.err.println("Creation of command '" + COMMAND_CLASSES[var2] + "' failed:");
            var7.printStackTrace(System.err);
         }
      }

   }

   public static synchronized CommandFactory getDefault() {
      if (instance == null) {
         instance = new CommandFactory();
      }

      return instance;
   }

   public Command createCommand(String var1, String[] var2, int var3, GlobalOptions var4, String var5) throws IllegalArgumentException {
      CommandProvider var6 = (CommandProvider)this.commandProvidersByNames.get(var1);
      if (var6 == null) {
         throw new IllegalArgumentException("Unknown command: '" + var1 + "'");
      } else {
         return var6.createCommand(var2, var3, var4, var5);
      }
   }

   public CommandProvider getCommandProvider(String var1) {
      return (CommandProvider)this.commandProvidersByNames.get(var1);
   }

   public CommandProvider[] getCommandProviders() {
      HashSet var1 = new HashSet(this.commandProvidersByNames.values());
      return (CommandProvider[])var1.toArray(new CommandProvider[0]);
   }
}
