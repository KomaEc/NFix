package org.netbeans.lib.cvsclient.response;

import org.netbeans.lib.cvsclient.command.CommandException;

public class ResponseException extends CommandException {
   public ResponseException(Exception var1) {
      super(var1, var1.getLocalizedMessage());
   }

   public ResponseException(Exception var1, String var2) {
      super(var1, var2);
   }

   public ResponseException(String var1, String var2) {
      super(var1, var2);
   }
}
