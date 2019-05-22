package org.netbeans.lib.cvsclient.response;

import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

public interface Response {
   void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException;

   boolean isTerminalResponse();
}
