package org.netbeans.lib.cvsclient.event;

public class MessageEvent extends CVSEvent {
   private String message;
   private boolean error;
   private boolean tagged;
   private final byte[] raw;

   public MessageEvent(Object var1, String var2, byte[] var3, boolean var4) {
      super(var1);
      this.setMessage(var2);
      this.setError(var4);
      this.setTagged(false);
      this.raw = var3;
   }

   public MessageEvent(Object var1, String var2, boolean var3) {
      this(var1, var2, (byte[])null, var3);
   }

   public MessageEvent(Object var1) {
      this(var1, (String)null, false);
   }

   public String getMessage() {
      return this.message;
   }

   public byte[] getRawData() {
      return this.raw;
   }

   public void setMessage(String var1) {
      this.message = var1;
   }

   public boolean isError() {
      return this.error;
   }

   public void setError(boolean var1) {
      this.error = var1;
   }

   protected void fireEvent(CVSListener var1) {
      var1.messageSent(this);
   }

   public boolean isTagged() {
      return this.tagged;
   }

   public void setTagged(boolean var1) {
      this.tagged = var1;
   }

   public static String parseTaggedMessage(StringBuffer var0, String var1) {
      if (var1.charAt(0) != '+' && var1.charAt(0) != '-') {
         String var3 = null;
         if (var1.equals("newline")) {
            var3 = var0.toString();
            var0.setLength(0);
         }

         int var4 = var1.indexOf(32);
         if (var4 > 0) {
            var0.append(var1.substring(var4 + 1));
         }

         return var3;
      } else {
         return null;
      }
   }
}
