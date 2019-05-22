package org.netbeans.lib.cvsclient.command.watchers;

import java.io.File;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;
import org.netbeans.lib.cvsclient.util.BugLog;

public class WatchersBuilder implements Builder {
   private static final String UNKNOWN_FILE = "? ";
   private WatchersInformation watchersInfo;
   private final EventManager eventManager;
   private final String localPath;

   public WatchersBuilder(EventManager var1, String var2) {
      this.eventManager = var1;
      this.localPath = var2;
   }

   public void outputDone() {
      if (this.watchersInfo != null) {
         this.eventManager.fireCVSEvent(new FileInfoEvent(this, this.watchersInfo));
         this.watchersInfo = null;
      }

   }

   public void parseLine(String var1, boolean var2) {
      if (var1.startsWith("? ")) {
         File var7 = new File(this.localPath, var1.substring("? ".length()));
         this.watchersInfo = new WatchersInformation(var7);
         this.outputDone();
      } else if (!var2) {
         if (!var1.startsWith(" ") && !var1.startsWith("\t")) {
            this.outputDone();
            String var3 = var1.trim().replace('\t', ' ');
            int var4 = var3.indexOf(32);
            BugLog.getInstance().assertTrue(var4 > 0, "Wrong line = " + var1);
            File var5 = new File(this.localPath, var3.substring(0, var4));
            String var6 = var3.substring(var4 + 1);
            this.watchersInfo = new WatchersInformation(var5);
            this.watchersInfo.addWatcher(var6);
         } else {
            BugLog.getInstance().assertNotNull(this.watchersInfo);
            this.watchersInfo.addWatcher(var1);
         }
      }
   }

   public void parseEnhancedMessage(String var1, Object var2) {
   }
}
