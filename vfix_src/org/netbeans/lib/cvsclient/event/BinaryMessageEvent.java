package org.netbeans.lib.cvsclient.event;

public class BinaryMessageEvent extends CVSEvent {
   private byte[] message;
   private int len;

   public BinaryMessageEvent(Object var1, byte[] var2, int var3) {
      super(var1);
      this.message = var2;
      this.len = var3;
   }

   public byte[] getMessage() {
      return this.message;
   }

   public int getMessageLength() {
      return this.len;
   }

   protected void fireEvent(CVSListener var1) {
      var1.messageSent(this);
   }
}
