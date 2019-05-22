package org.apache.maven.scm.provider.bazaar;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.DefaultLog;
import org.apache.maven.scm.provider.bazaar.command.BazaarConsumer;
import org.codehaus.plexus.util.cli.Commandline;

public class BazaarConfig {
   private static final float BAZAAR_REQ = 0.7F;
   private static final float PYTHON_REQ = 2.4F;
   private static final String BAZAAR_VERSION_TAG = "bzr (bazaar-ng) ";
   private static final String BAZAAR_INSTALL_URL = "'http://bazaar-vcs.org/Installation'";
   private static final String PYTHON_EXEC = "python";
   private static final String PYTHON_VERSION = "-V";
   private static final String PYTHON_VERSION_TAG = "Python ";
   private static final String PARAMIKO = "\"import paramiko\"";
   private static final String CCRYPT = "\"import Crypto\"";
   private static final String CELEMENTREE = "\"import cElementTree\"";
   private BazaarConfig.VersionConsumer bazaarVersion = new BazaarConfig.VersionConsumer((String)null);
   private BazaarConfig.VersionConsumer pythonVersion = new BazaarConfig.VersionConsumer((String)null);
   private boolean cElementTree = false;
   private boolean paramiko = false;
   private boolean cCrypt = false;

   BazaarConfig(File workingDir) {
      try {
         this.pythonVersion = getPythonVersion(workingDir);
         this.paramiko = this.checkPyModules(workingDir, "\"import paramiko\"");
         this.cCrypt = this.checkPyModules(workingDir, "\"import Crypto\"");
         this.cElementTree = this.checkPyModules(workingDir, "\"import cElementTree\"");
         this.bazaarVersion = getBazaarVersion(workingDir);
      } catch (ScmException var3) {
      }

   }

   private boolean checkPyModules(File workingDir, String cmd) {
      BazaarConfig.PythonConsumer consumer = new BazaarConfig.PythonConsumer();

      int exitCode;
      try {
         Commandline cmdLine = buildPythonCmd(workingDir, new String[]{"-c", cmd});
         exitCode = BazaarUtils.executeCmd(consumer, cmdLine);
      } catch (ScmException var6) {
         exitCode = -1;
      }

      return exitCode == 0 && consumer.getConsumedAndClear().equals("");
   }

   private boolean isInstalled() {
      return this.pythonVersion.isVersionOk(2.4F) && this.bazaarVersion.isVersionOk(0.7F);
   }

   private boolean isComplete() {
      return this.isInstalled() && this.cElementTree && this.paramiko && this.cCrypt;
   }

   public static BazaarConfig.VersionConsumer getBazaarVersion(File workingDir) throws ScmException {
      String[] versionCmd = new String[]{"version"};
      BazaarConfig.VersionConsumer consumer = new BazaarConfig.VersionConsumer("bzr (bazaar-ng) ");
      Commandline cmd = BazaarUtils.buildCmd(workingDir, versionCmd);
      BazaarUtils.executeCmd(consumer, cmd);
      return consumer;
   }

   public static BazaarConfig.VersionConsumer getPythonVersion(File workingDir) throws ScmException {
      String[] versionCmd = new String[]{"-V"};
      BazaarConfig.VersionConsumer consumer = new BazaarConfig.VersionConsumer("Python ");
      Commandline cmd = buildPythonCmd(workingDir, versionCmd);
      BazaarUtils.executeCmd(consumer, cmd);
      return consumer;
   }

   private static Commandline buildPythonCmd(File workingDir, String[] cmdAndArgs) throws ScmException {
      Commandline cmd = new Commandline();
      cmd.setExecutable("python");
      cmd.setWorkingDirectory(workingDir.getAbsolutePath());
      cmd.addArguments(cmdAndArgs);
      if (!workingDir.exists()) {
         boolean success = workingDir.mkdirs();
         if (!success) {
            String msg = "Working directory did not exist and it couldn't be created: " + workingDir;
            throw new ScmException(msg);
         }
      }

      return cmd;
   }

   private String getInstalledStr() {
      return this.isComplete() ? "valid and complete." : (this.isInstalled() ? "incomplete. " : "invalid. ") + "Consult " + "'http://bazaar-vcs.org/Installation'";
   }

   public String toString(File workingDir) {
      boolean bzrOk = this.bazaarVersion.isVersionOk(0.7F);
      boolean pyOk = this.pythonVersion.isVersionOk(2.4F);
      return "\n  Your Bazaar installation seems to be " + this.getInstalledStr() + "\n    Python version: " + this.pythonVersion.getVersion() + (pyOk ? " (OK)" : " (May be INVALID)") + "\n    Bazaar version: " + this.bazaarVersion.getVersion() + (bzrOk ? " (OK)" : " (May be INVALID)") + "\n    Paramiko installed: " + this.paramiko + " (For remote access eg. sftp) " + "\n    cCrypt installed: " + this.cCrypt + " (For remote access eg. sftp) " + "\n    cElementTree installed: " + this.cElementTree + " (Not mandatory) " + "\n";
   }

   private static class PythonConsumer extends BazaarConsumer {
      private String consumed = "";

      PythonConsumer() {
         super(new DefaultLog());
      }

      public void doConsume(ScmFileStatus status, String line) {
         this.consumed = line;
      }

      String getConsumedAndClear() {
         String tmp = this.consumed;
         this.consumed = "";
         return tmp;
      }
   }

   private static class VersionConsumer extends BazaarConsumer {
      private static final Pattern VERSION_PATTERN = Pattern.compile("[\\d]+.?[\\d]*");
      private final String versionTag;
      private String versionStr = "NA";
      private float version = -1.0F;

      VersionConsumer(String aVersionTag) {
         super(new DefaultLog());
         this.versionTag = aVersionTag;
      }

      public void doConsume(ScmFileStatus status, String line) {
         if (line.startsWith(this.versionTag)) {
            this.versionStr = line.substring(this.versionTag.length());
         }

      }

      String getVersion() {
         return this.versionStr;
      }

      boolean isVersionOk(float min) {
         Matcher matcher = VERSION_PATTERN.matcher(this.versionStr);
         if (matcher.find()) {
            String subStr = this.versionStr.substring(matcher.start(), matcher.end());

            try {
               this.version = Float.valueOf(subStr);
            } catch (NumberFormatException var5) {
               if (this.getLogger().isErrorEnabled()) {
                  this.getLogger().error("Regexp for version did not result in a number: " + subStr, var5);
               }
            }
         }

         return min <= this.version;
      }
   }
}
