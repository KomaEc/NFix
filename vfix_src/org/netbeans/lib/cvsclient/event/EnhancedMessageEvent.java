package org.netbeans.lib.cvsclient.event;

public class EnhancedMessageEvent extends MessageEvent {
   public static final String MERGED_PATH = "Merged_Response_File_Path";
   public static final String FILE_SENDING = "File_Sent_To_Server";
   public static final String REQUESTS_SENT = "All_Requests_Were_Sent";
   public static final String REQUESTS_COUNT = "Requests_Count";
   private String key;
   private Object value;

   public EnhancedMessageEvent(Object var1, String var2, Object var3) {
      super(var1, "", false);
      this.key = var2;
      this.value = var3;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String var1) {
      this.key = var1;
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object var1) {
      this.value = var1;
   }
}
