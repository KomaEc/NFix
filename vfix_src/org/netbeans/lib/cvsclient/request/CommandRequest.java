package org.netbeans.lib.cvsclient.request;

public class CommandRequest extends Request {
   public static final CommandRequest ADD = new CommandRequest("add\n");
   public static final CommandRequest ANNOTATE = new CommandRequest("annotate\n");
   public static final CommandRequest CHECKOUT = new CommandRequest("co\n");
   public static final CommandRequest COMMIT = new CommandRequest("ci\n");
   public static final CommandRequest DIFF = new CommandRequest("diff\n");
   public static final CommandRequest EDITORS = new CommandRequest("editors\n");
   public static final CommandRequest EXPORT = new CommandRequest("export\n");
   public static final CommandRequest HISTORY = new CommandRequest("history\n");
   public static final CommandRequest IMPORT = new CommandRequest("import\n");
   public static final CommandRequest LOG = new CommandRequest("log\n");
   public static final CommandRequest NOOP = new CommandRequest("noop\n");
   public static final CommandRequest RANNOTATE = new CommandRequest("rannotate\n");
   public static final CommandRequest REMOVE = new CommandRequest("remove\n");
   public static final CommandRequest RLOG = new CommandRequest("rlog\n");
   public static final CommandRequest RTAG = new CommandRequest("rtag\n");
   public static final CommandRequest STATUS = new CommandRequest("status\n");
   public static final CommandRequest TAG = new CommandRequest("tag\n");
   public static final CommandRequest UPDATE = new CommandRequest("update\n");
   public static final CommandRequest WATCH_ADD = new CommandRequest("watch-add\n");
   public static final CommandRequest WATCH_ON = new CommandRequest("watch-on\n");
   public static final CommandRequest WATCH_OFF = new CommandRequest("watch-off\n");
   public static final CommandRequest WATCH_REMOVE = new CommandRequest("watch-remove\n");
   public static final CommandRequest WATCHERS = new CommandRequest("watchers\n");
   private final String request;

   private CommandRequest(String var1) {
      this.request = var1;
   }

   public String getRequestString() {
      return this.request;
   }

   public boolean isResponseExpected() {
      return true;
   }
}
