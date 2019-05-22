package org.apache.maven.scm.provider.hg;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.DefaultLog;
import org.apache.maven.scm.provider.hg.command.HgConsumer;
import org.codehaus.plexus.util.cli.Commandline;

public class HgConfig {
   private static final String HG_REQ = "0.9.2";
   private static final String HG_VERSION_TAG = "ercurial Distributed SCM (version ";
   private static final String HG_INSTALL_URL = "'http://www.selenic.com/mercurial/wiki/index.cgi/Download'";
   private HgConfig.HgVersionConsumer hgVersion = new HgConfig.HgVersionConsumer((String)null);

   HgConfig(File workingDir) {
      try {
         this.hgVersion = getHgVersion(workingDir);
      } catch (ScmException var3) {
      }

   }

   private boolean isInstalled() {
      return this.hgVersion.isVersionOk("0.9.2");
   }

   private boolean isComplete() {
      return this.isInstalled();
   }

   public static HgConfig.HgVersionConsumer getHgVersion(File workingDir) throws ScmException {
      String[] versionCmd = new String[]{"version"};
      HgConfig.HgVersionConsumer consumer = new HgConfig.HgVersionConsumer("ercurial Distributed SCM (version ");
      Commandline cmd = HgUtils.buildCmd(workingDir, versionCmd);
      HgUtils.executeCmd(consumer, cmd);
      return consumer;
   }

   private static boolean compareVersion(String version1, String version2) {
      String v1 = version1;
      String v2 = version2;
      int l1 = version1.length();
      int l2 = version2.length();
      int x;
      if (l1 > l2) {
         for(x = l2; x >= l1; --x) {
            v2 = v2 + ' ';
         }
      }

      if (l2 > l1) {
         for(x = l1; x <= l2; ++x) {
            v1 = v1 + ' ';
         }
      }

      return v2.compareTo(v1) >= 0;
   }

   private String getInstalledStr() {
      return this.isComplete() ? "valid and complete." : (this.isInstalled() ? "incomplete. " : "invalid. ") + "Consult " + "'http://www.selenic.com/mercurial/wiki/index.cgi/Download'";
   }

   public String toString(File workingDir) {
      boolean hgOk = this.hgVersion.isVersionOk("0.9.2");
      return "\n  Your Hg installation seems to be " + this.getInstalledStr() + "\n    Hg version: " + this.hgVersion.getVersion() + (hgOk ? " (OK)" : " (May be INVALID)") + "\n";
   }

   private static class HgVersionConsumer extends HgConsumer {
      private String versionStr = "NA";
      private String versionTag;

      HgVersionConsumer(String versionTag) {
         super(new DefaultLog());
         this.versionTag = versionTag;
      }

      public void doConsume(ScmFileStatus status, String line) {
         if (line.startsWith(this.versionTag)) {
            String[] elements = line.split(" ");
            this.versionStr = elements[elements.length - 1].split("\\)")[0];
         }

      }

      String getVersion() {
         return this.versionStr;
      }

      boolean isVersionOk(String version) {
         return HgConfig.compareVersion(version, this.versionStr);
      }
   }
}
