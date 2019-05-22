package org.apache.maven.scm.provider.cvslib.command.login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.log.ScmLogger;

public class CvsPass {
   private String cvsRoot = null;
   private File passFile = null;
   private String password = null;
   private ScmLogger logger;
   private final char[] shifts = new char[]{'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007', '\b', '\t', '\n', '\u000b', '\f', '\r', '\u000e', '\u000f', '\u0010', '\u0011', '\u0012', '\u0013', '\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001a', '\u001b', '\u001c', '\u001d', '\u001e', '\u001f', 'r', 'x', '5', 'O', '`', 'm', 'H', 'l', 'F', '@', 'L', 'C', 't', 'J', 'D', 'W', 'o', '4', 'K', 'w', '1', '"', 'R', 'Q', '_', 'A', 'p', 'V', 'v', 'n', 'z', 'i', ')', '9', 'S', '+', '.', 'f', '(', 'Y', '&', 'g', '-', '2', '*', '{', '[', '#', '}', '7', '6', 'B', '|', '~', ';', '/', '\\', 'G', 's', 'N', 'X', 'k', 'j', '8', '$', 'y', 'u', 'h', 'e', 'd', 'E', 'I', 'c', '?', '^', ']', '\'', '%', '=', '0', ':', 'q', ' ', 'Z', ',', 'b', '<', '3', '!', 'a', '>', 'M', 'T', 'P', 'U', 'ß', 'á', 'Ø', '»', '¦', 'å', '½', 'Þ', '¼', '\u008d', 'ù', '\u0094', 'È', '¸', '\u0088', 'ø', '¾', 'Ç', 'ª', 'µ', 'Ì', '\u008a', 'è', 'Ú', '·', 'ÿ', 'ê', 'Ü', '÷', 'Õ', 'Ë', 'â', 'Á', '®', '¬', 'ä', 'ü', 'Ù', 'É', '\u0083', 'æ', 'Å', 'Ó', '\u0091', 'î', '¡', '³', ' ', 'Ô', 'Ï', 'Ý', 'þ', '\u00ad', 'Ê', '\u0092', 'à', '\u0097', '\u008c', 'Ä', 'Í', '\u0082', '\u0087', '\u0085', '\u008f', 'ö', 'À', '\u009f', 'ô', 'ï', '¹', '¨', '×', '\u0090', '\u008b', '¥', '´', '\u009d', '\u0093', 'º', 'Ö', '°', 'ã', 'ç', 'Û', '©', '¯', '\u009c', 'Î', 'Æ', '\u0081', '¤', '\u0096', 'Ò', '\u009a', '±', '\u0086', '\u007f', '¶', '\u0080', '\u009e', 'Ð', '¢', '\u0084', '§', 'Ñ', '\u0095', 'ñ', '\u0099', 'û', 'í', 'ì', '«', 'Ã', 'ó', 'é', 'ý', 'ð', 'Â', 'ú', '¿', '\u009b', '\u008e', '\u0089', 'õ', 'ë', '£', 'ò', '²', '\u0098'};

   public CvsPass(ScmLogger logger) {
      this.passFile = new File(System.getProperty("cygwin.user.home", System.getProperty("user.home")) + File.separatorChar + ".cvspass");
      this.logger = logger;
   }

   public final void execute() throws ScmException, IOException {
      if (this.cvsRoot == null) {
         throw new ScmException("cvsroot is required");
      } else {
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("cvsRoot: " + this.cvsRoot);
            this.logger.debug("passFile: " + this.passFile);
         }

         BufferedReader reader = null;
         PrintWriter writer = null;

         try {
            StringBuilder buf = new StringBuilder();
            String pwdfile;
            if (!this.passFile.exists()) {
               this.passFile.getParentFile().mkdirs();
            } else {
               reader = new BufferedReader(new FileReader(this.passFile));
               pwdfile = null;

               while((pwdfile = reader.readLine()) != null) {
                  if (pwdfile.startsWith(this.cvsRoot) || pwdfile.startsWith("/1 " + this.cvsRoot)) {
                     if (this.logger.isDebugEnabled()) {
                        this.logger.debug("cvsroot " + this.cvsRoot + " already exist in " + this.passFile.getAbsolutePath() + ". SKIPPED.");
                     }

                     return;
                  }

                  buf.append(pwdfile).append("\n");
               }
            }

            if (this.password == null) {
               throw new ScmException("password is required. You must run a 'cvs -d " + this.cvsRoot + " login' first or provide it in the connection url.");
            } else {
               pwdfile = buf.toString() + "/1 " + this.cvsRoot + " A" + this.mangle(this.password);
               if (this.logger.isDebugEnabled()) {
                  this.logger.debug("Writing -> " + pwdfile + " in " + this.passFile.getAbsolutePath());
               }

               writer = new PrintWriter(new FileWriter(this.passFile));
               writer.println(pwdfile);
            }
         } finally {
            if (reader != null) {
               try {
                  reader.close();
               } catch (IOException var12) {
               }
            }

            if (writer != null) {
               writer.close();
            }

         }
      }
   }

   private String mangle(String password) {
      StringBuilder buf = new StringBuilder();

      for(int i = 0; i < password.length(); ++i) {
         buf.append(this.shifts[password.charAt(i)]);
      }

      return buf.toString();
   }

   public void setCvsroot(String cvsRoot) {
      this.cvsRoot = cvsRoot;
   }

   public void setPassfile(File passFile) {
      this.passFile = passFile;
   }

   public void setPassword(String password) {
      this.password = password;
   }
}
