package org.netbeans.lib.cvsclient.response;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class TemplateResponse implements Response {
   protected String localPath;
   protected String repositoryPath;

   public TemplateResponse() {
   }

   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      String var4;
      try {
         this.localPath = var1.readLine();
         this.repositoryPath = var1.readLine();
         int var3 = Integer.parseInt(var1.readLine());
         var4 = var2.convertPathname(this.localPath, this.repositoryPath) + "CVS/Template";
         BufferedOutputStream var5 = null;
         File var6 = new File(var4);
         var6.getParentFile().mkdirs();

         try {
            FileOutputStream var26 = new FileOutputStream(var6);
            var5 = new BufferedOutputStream(var26);
            byte[] var7 = System.getProperty("line.separator").getBytes();
            byte[] var8 = var1.readBytes(var3);

            for(int var9 = 0; var9 < var8.length; ++var9) {
               byte var10 = var8[var9];
               if (var10 == 10) {
                  var5.write(var7);
               } else {
                  var5.write(var10);
               }
            }
         } catch (EOFException var22) {
         } finally {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (IOException var21) {
               }
            }

         }

      } catch (EOFException var24) {
         var4 = ResponseException.getLocalMessage("CommandException.EndOfFile");
         throw new ResponseException(var24, var4);
      } catch (IOException var25) {
         throw new ResponseException(var25);
      }
   }

   public boolean isTerminalResponse() {
      return false;
   }
}
