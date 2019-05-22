package org.netbeans.lib.cvsclient.command;

public class Watch {
   public static final Watch EDIT = new Watch("edit", "E", new String[]{"edit"});
   public static final Watch UNEDIT = new Watch("unedit", "U", new String[]{"unedit"});
   public static final Watch COMMIT = new Watch("commit", "C", new String[]{"commit"});
   public static final Watch ALL = new Watch("all", "EUC", new String[]{"edit", "unedit", "commit"});
   public static final Watch NONE = new Watch("none", "", new String[0]);
   private final String name;
   private final String value;
   private final String[] arguments;

   public static String getWatchString(Watch var0) {
      return var0 == null ? NONE.getValue() : var0.getValue();
   }

   private Watch(String var1, String var2, String[] var3) {
      this.name = var1;
      this.value = var2;
      this.arguments = var3;
   }

   public String[] getArguments() {
      return this.arguments;
   }

   public String toString() {
      return this.name;
   }

   private String getValue() {
      return this.value;
   }
}
