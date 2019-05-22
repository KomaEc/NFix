package org.netbeans.lib.cvsclient.commandLine.command;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.command.commit.CommitCommand;
import org.netbeans.lib.cvsclient.commandLine.GetOpt;

public class commit extends AbstractCommandProvider {
   public String[] getSynonyms() {
      return new String[]{"ci", "com", "put"};
   }

   private static String getEditorProcess(String var0) {
      if (var0 == null) {
         if (System.getProperty("os.name").startsWith("Windows")) {
            var0 = "notepad.exe";
         } else {
            var0 = null;
         }

         var0 = System.getProperty("cvs.editor", var0);
      }

      return var0;
   }

   private static File createTempFile(File[] var0, File var1) throws IOException {
      File var2 = null;
      BufferedReader var3 = null;
      BufferedWriter var4 = null;

      File var12;
      try {
         File var5 = File.createTempFile("cvsTemplate", "txt", var1);
         var4 = new BufferedWriter(new FileWriter(var5));
         if (var0 != null && var0.length > 0) {
            var2 = new File(var0[0].getParentFile(), "CVS" + File.separator + "Template");
            if (var2.exists()) {
               var3 = new BufferedReader(new FileReader(var2));
               String var6 = null;

               while((var6 = var3.readLine()) != null) {
                  var4.write(var6);
                  var4.newLine();
               }
            }
         }

         var4.write("CVS: ----------------------------------------------------------------------");
         var4.newLine();
         var4.write("CVS: Enter Log.  Lines beginning with `CVS:' are removed automatically");
         var4.newLine();
         var4.write("CVS: ");
         var4.newLine();
         var4.write("CVS: Committing in .");
         var4.newLine();
         var4.write("CVS: ");
         var4.newLine();
         var4.write("CVS: Modified Files:");
         var4.newLine();
         if (var0 != null) {
            for(int var11 = 0; var11 < var0.length; ++var11) {
               var4.write("CVS:  " + var0[var11].getPath());
            }
         }

         var4.write("CVS: ----------------------------------------------------------------------");
         var4.newLine();
         var12 = var5;
      } finally {
         if (var3 != null) {
            var3.close();
         }

         if (var4 != null) {
            var4.close();
         }

      }

      return var12;
   }

   private static String createMessage(File[] var0, GlobalOptions var1) {
      File var2 = null;
      BufferedReader var3 = null;

      Process var5;
      try {
         var2 = createTempFile(var0, var1.getTempDir());
         String var4 = getEditorProcess(var1.getEditor());
         if (var4 != null) {
            var5 = Runtime.getRuntime().exec(new String[]{var4, var2.getPath()});
            int var6 = -1;

            try {
               var6 = var5.waitFor();
            } catch (InterruptedException var22) {
            }

            String var7;
            if (var6 != 0) {
               var7 = null;
               return var7;
            }

            var3 = new BufferedReader(new FileReader(var2));
            StringBuffer var8 = new StringBuffer((int)var2.length());

            while((var7 = var3.readLine()) != null) {
               if (!var7.startsWith("CVS:")) {
                  var8.append(var7);
                  var8.append('\n');
               }
            }

            String var9 = var8.toString();
            return var9;
         }

         var5 = null;
      } catch (IOException var23) {
         System.err.println("Error: " + var23);
         var23.printStackTrace();
         var5 = null;
         return var5;
      } finally {
         try {
            if (var3 != null) {
               var3.close();
            }

            if (var2 != null) {
               var2.delete();
            }
         } catch (Exception var21) {
            System.err.println("Fatal error: " + var21);
            var21.printStackTrace();
         }

      }

      return var5;
   }

   public Command createCommand(String[] var1, int var2, GlobalOptions var3, String var4) {
      CommitCommand var5 = new CommitCommand();
      var5.setBuilder((Builder)null);
      String var6 = var5.getOptString();
      GetOpt var7 = new GetOpt(var1, var6);
      boolean var8 = true;
      var7.optIndexSet(var2);
      boolean var9 = false;

      int var16;
      while((var16 = var7.getopt()) != -1) {
         boolean var10 = var5.setCVSCommand((char)var16, var7.optArgGet());
         if (!var10) {
            var9 = true;
         }
      }

      if (var9) {
         throw new IllegalArgumentException(this.getUsage());
      } else {
         int var14 = var7.optIndexGet();
         File[] var11 = null;
         if (var14 < var1.length) {
            var11 = new File[var1.length - var14];
            if (var4 == null) {
               var4 = System.getProperty("user.dir");
            }

            File var12 = new File(var4);

            for(int var13 = var14; var13 < var1.length; ++var13) {
               var11[var13 - var14] = new File(var12, var1[var13]);
            }

            var5.setFiles(var11);
         }

         if (var5.getMessage() == null && var5.getLogMessageFromFile() == null) {
            String var15 = createMessage(var11, var3);
            if (var15 == null) {
               throw new IllegalArgumentException(ResourceBundle.getBundle(commit.class.getPackage().getName() + ".Bundle").getString("commit.messageNotSpecified"));
            }

            var5.setMessage(var15);
         }

         return var5;
      }
   }
}
