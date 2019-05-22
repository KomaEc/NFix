package org.netbeans.lib.cvsclient.commandLine.command;

import java.io.File;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.command.add.AddCommand;
import org.netbeans.lib.cvsclient.commandLine.GetOpt;

public class add extends AbstractCommandProvider {
   public String[] getSynonyms() {
      return new String[]{"ad", "new"};
   }

   public Command createCommand(String[] var1, int var2, GlobalOptions var3, String var4) {
      AddCommand var5 = new AddCommand();
      var5.setBuilder((Builder)null);
      String var6 = var5.getOptString();
      GetOpt var7 = new GetOpt(var1, var6);
      boolean var8 = true;
      var7.optIndexSet(var2);
      boolean var9 = false;

      int var15;
      while((var15 = var7.getopt()) != -1) {
         boolean var10 = var5.setCVSCommand((char)var15, var7.optArgGet());
         if (!var10) {
            var9 = true;
         }
      }

      if (var9) {
         throw new IllegalArgumentException(this.getUsage());
      } else {
         int var14 = var7.optIndexGet();
         if (var14 < var1.length) {
            File[] var11 = new File[var1.length - var14];
            if (var4 == null) {
               var4 = System.getProperty("user.dir");
            }

            File var12 = new File(var4);

            for(int var13 = var14; var13 < var1.length; ++var13) {
               var11[var13 - var14] = new File(var12, var1[var13]);
            }

            var5.setFiles(var11);
         }

         return var5;
      }
   }
}
