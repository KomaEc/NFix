package org.netbeans.lib.cvsclient.response;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ResponseFactory {
   private final Map responseInstancesMap = new HashMap();
   private String previousResponse = null;

   public ResponseFactory() {
      this.responseInstancesMap.put("E", new ErrorMessageResponse());
      this.responseInstancesMap.put("M", new MessageResponse());
      this.responseInstancesMap.put("Mbinary", new MessageBinaryResponse());
      this.responseInstancesMap.put("MT", new MessageTaggedResponse());
      this.responseInstancesMap.put("Updated", new UpdatedResponse());
      this.responseInstancesMap.put("Update-existing", new UpdatedResponse());
      this.responseInstancesMap.put("Created", new CreatedResponse());
      this.responseInstancesMap.put("Rcs-diff", new RcsDiffResponse());
      this.responseInstancesMap.put("Checked-in", new CheckedInResponse());
      this.responseInstancesMap.put("New-entry", new NewEntryResponse());
      this.responseInstancesMap.put("ok", new OKResponse());
      this.responseInstancesMap.put("error", new ErrorResponse());
      this.responseInstancesMap.put("Set-static-directory", new SetStaticDirectoryResponse());
      this.responseInstancesMap.put("Clear-static-directory", new ClearStaticDirectoryResponse());
      this.responseInstancesMap.put("Set-sticky", new SetStickyResponse());
      this.responseInstancesMap.put("Clear-sticky", new ClearStickyResponse());
      this.responseInstancesMap.put("Valid-requests", new ValidRequestsResponse());
      this.responseInstancesMap.put("Merged", new MergedResponse());
      this.responseInstancesMap.put("Notified", new NotifiedResponse());
      this.responseInstancesMap.put("Removed", new RemovedResponse());
      this.responseInstancesMap.put("Remove-entry", new RemoveEntryResponse());
      this.responseInstancesMap.put("Copy-file", new CopyFileResponse());
      this.responseInstancesMap.put("Mod-time", new ModTimeResponse());
      this.responseInstancesMap.put("Template", new TemplateResponse());
      this.responseInstancesMap.put("Module-expansion", new ModuleExpansionResponse());
      this.responseInstancesMap.put("Wrapper-rcsOption", new WrapperSendResponse());
   }

   public Response createResponse(String var1) {
      Response var2 = (Response)this.responseInstancesMap.get(var1);
      if (var2 != null) {
         this.previousResponse = var1;
         return var2;
      } else if (this.previousResponse != null && this.previousResponse.equals("M")) {
         return new MessageResponse(var1);
      } else {
         this.previousResponse = null;
         ResponseFactory.IllegalArgumentException2 var3 = new ResponseFactory.IllegalArgumentException2("Unhandled response: " + var1 + ".");
         String var4 = System.getProperty("Env-CVS_SERVER");
         if (var4 == null) {
            var4 = "";
         } else {
            var4 = "=" + var4;
         }

         String var5 = System.getProperty("Env-CVS_EXE");
         if (var5 == null) {
            var5 = "";
         } else {
            var5 = "=" + var5;
         }

         ResourceBundle var6 = ResourceBundle.getBundle("org.netbeans.lib.cvsclient.response.Bundle");
         String var7 = var6.getString("BK0001");
         var7 = MessageFormat.format(var7, var1, var4, var5);
         var3.setLocalizedMessage(var7);
         throw var3;
      }
   }

   private static class IllegalArgumentException2 extends IllegalArgumentException {
      private String localizedMessage;

      public IllegalArgumentException2(String var1) {
         super(var1);
      }

      public String getLocalizedMessage() {
         return this.localizedMessage;
      }

      private void setLocalizedMessage(String var1) {
         this.localizedMessage = var1;
      }
   }
}
