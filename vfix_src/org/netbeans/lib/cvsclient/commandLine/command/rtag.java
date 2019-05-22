package org.netbeans.lib.cvsclient.commandLine.command;

import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.command.tag.RtagCommand;
import org.netbeans.lib.cvsclient.commandLine.GetOpt;

public class rtag extends AbstractCommandProvider {
   public String getName() {
      return "rtag";
   }

   public String[] getSynonyms() {
      return new String[]{"rt"};
   }

   public Command createCommand(String[] var1, int var2, GlobalOptions var3, String var4) {
      RtagCommand var5 = new RtagCommand();
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
         if (var14 >= var1.length) {
            throw new IllegalArgumentException(this.getUsage());
         } else {
            var5.setTag(var1[var14]);
            int var11 = var14 + 1;
            if (var11 < var1.length) {
               String[] var12 = new String[var1.length - var11];

               for(int var13 = var11; var13 < var1.length; ++var13) {
                  var12[var13 - var11] = var1[var13];
               }

               var5.setModules(var12);
            }

            return var5;
         }
      }
   }
}
