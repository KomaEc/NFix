package org.netbeans.lib.cvsclient.commandLine.command;

import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.command.importcmd.ImportCommand;
import org.netbeans.lib.cvsclient.commandLine.GetOpt;

public class Import extends AbstractCommandProvider {
   public String getName() {
      return "import";
   }

   public String[] getSynonyms() {
      return new String[]{"im", "imp"};
   }

   public Command createCommand(String[] var1, int var2, GlobalOptions var3, String var4) {
      ImportCommand var5 = new ImportCommand();
      var5.setBuilder((Builder)null);
      String var6 = var5.getOptString();
      GetOpt var7 = new GetOpt(var1, var6);
      boolean var8 = true;
      var7.optIndexSet(var2);
      boolean var9 = false;

      int var12;
      while((var12 = var7.getopt()) != -1) {
         boolean var10 = var5.setCVSCommand((char)var12, var7.optArgGet());
         if (!var10) {
            var9 = true;
         }
      }

      if (var9) {
         this.throwUsage();
      }

      int var11 = var7.optIndexGet();
      if (var11 < var1.length - 2) {
         var5.setModule(var1[var11]);
         ++var11;
         var5.setVendorTag(var1[var11]);
         ++var11;
         var5.setReleaseTag(var1[var11]);
      } else {
         this.throwUsage();
      }

      return var5;
   }

   private void throwUsage() {
      throw new IllegalArgumentException(this.getUsage());
   }
}
