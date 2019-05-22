package org.netbeans.lib.cvsclient.file;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.request.Request;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;
import org.netbeans.lib.cvsclient.util.LoggedDataOutputStream;

public interface FileHandler {
   void transmitTextFile(File var1, LoggedDataOutputStream var2) throws IOException;

   void transmitBinaryFile(File var1, LoggedDataOutputStream var2) throws IOException;

   void writeTextFile(String var1, String var2, LoggedDataInputStream var3, int var4) throws IOException;

   void writeRcsDiffFile(String var1, String var2, LoggedDataInputStream var3, int var4) throws IOException;

   void writeBinaryFile(String var1, String var2, LoggedDataInputStream var3, int var4) throws IOException;

   void removeLocalFile(String var1) throws IOException;

   void renameLocalFile(String var1, String var2) throws IOException;

   void setNextFileDate(Date var1);

   Request[] getInitialisationRequests();

   void setGlobalOptions(GlobalOptions var1);
}
