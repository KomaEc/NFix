package org.apache.maven.scm.provider.integrity;

import com.mks.api.response.APIException;
import com.mks.api.response.InterruptedException;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItemIterator;

public class ExceptionHandler {
   private String message;
   private String command;
   private int exitCode;

   public ExceptionHandler(APIException ex) {
      Response response = ex.getResponse();
      ex.printStackTrace();
      if (null == response) {
         this.message = ex.getMessage();
         this.command = new String();
         this.exitCode = -1;
      } else {
         this.command = response.getCommandString();

         try {
            this.exitCode = response.getExitCode();
         } catch (InterruptedException var6) {
            var6.printStackTrace();
            this.exitCode = -1;
         }

         WorkItemIterator wit = response.getWorkItems();

         try {
            while(wit.hasNext()) {
               wit.next();
            }

            if (ex.getMessage() != null) {
               this.message = ex.getMessage();
            }
         } catch (APIException var7) {
            String curMessage = var7.getMessage();
            if (curMessage != null) {
               this.message = curMessage;
            }

            var7.printStackTrace();
         }
      }

   }

   public String getMessage() {
      return this.message;
   }

   public String getCommand() {
      return this.command;
   }

   public int getExitCode() {
      return this.exitCode;
   }
}
