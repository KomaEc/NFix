package org.netbeans.lib.cvsclient.command.watch;

import org.netbeans.lib.cvsclient.request.CommandRequest;

public class WatchMode {
   public static final WatchMode ON;
   public static final WatchMode OFF;
   public static final WatchMode ADD;
   public static final WatchMode REMOVE;
   private final String name;
   private final CommandRequest command;
   private final boolean watchOptionAllowed;

   private WatchMode(String var1, CommandRequest var2, boolean var3) {
      this.name = var1;
      this.command = var2;
      this.watchOptionAllowed = var3;
   }

   public CommandRequest getCommand() {
      return this.command;
   }

   public boolean isWatchOptionAllowed() {
      return this.watchOptionAllowed;
   }

   public String toString() {
      return this.name;
   }

   static {
      ON = new WatchMode("on", CommandRequest.WATCH_ON, false);
      OFF = new WatchMode("off", CommandRequest.WATCH_OFF, false);
      ADD = new WatchMode("add", CommandRequest.WATCH_ADD, true);
      REMOVE = new WatchMode("remove", CommandRequest.WATCH_REMOVE, true);
   }
}
