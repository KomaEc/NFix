package org.netbeans.lib.cvsclient.commandLine.command;

import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.command.checkout.CheckoutCommand;
import org.netbeans.lib.cvsclient.commandLine.GetOpt;

public class checkout extends AbstractCommandProvider {
   public String[] getSynonyms() {
      return new String[]{"co", "get"};
   }

   public Command createCommand(String[] var1, int var2, GlobalOptions var3, String var4) {
      CheckoutCommand var5 = new CheckoutCommand();
      var5.setBuilder((Builder)null);
      String var6 = var5.getOptString();
      GetOpt var7 = new GetOpt(var1, var6);
      boolean var8 = true;
      var7.optIndexSet(var2);
      boolean var9 = false;

      int var14;
      while((var14 = var7.getopt()) != -1) {
         boolean var10 = var5.setCVSCommand((char)var14, var7.optArgGet());
         if (!var10) {
            var9 = true;
         }
      }

      if (var9) {
         throw new IllegalArgumentException(this.getUsage());
      } else {
         int var13 = var7.optIndexGet();
         if (var13 < var1.length) {
            String[] var11 = new String[var1.length - var13];

            for(int var12 = var13; var12 < var1.length; ++var12) {
               var11[var12 - var13] = var1[var12];
            }

            var5.setModules(var11);
         }

         return var5;
      }
   }
}
