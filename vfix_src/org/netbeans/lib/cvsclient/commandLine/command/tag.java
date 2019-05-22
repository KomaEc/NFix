package org.netbeans.lib.cvsclient.commandLine.command;

import java.io.File;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.command.tag.TagCommand;
import org.netbeans.lib.cvsclient.commandLine.GetOpt;

public class tag extends AbstractCommandProvider {
   public String getName() {
      return "tag";
   }

   public String[] getSynonyms() {
      return new String[]{"ta"};
   }

   public Command createCommand(String[] var1, int var2, GlobalOptions var3, String var4) {
      TagCommand var5 = new TagCommand();
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
         int var16 = var7.optIndexGet();
         if (var16 >= var1.length) {
            throw new IllegalArgumentException(this.getUsage());
         } else {
            var5.setTag(var1[var16]);
            int var11 = var16 + 1;
            if (var11 < var1.length) {
               File[] var12 = new File[var1.length - var11];
               if (var4 == null) {
                  var4 = System.getProperty("user.dir");
               }

               File var13 = new File(var4);

               for(int var14 = var11; var14 < var1.length; ++var14) {
                  var12[var14 - var11] = new File(var13, var1[var14]);
               }

               var5.setFiles(var12);
            }

            return var5;
         }
      }
   }
}
