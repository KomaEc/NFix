package org.netbeans.lib.cvsclient.response;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.command.KeywordSubstitutionOptions;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.file.FileHandler;
import org.netbeans.lib.cvsclient.util.StringPattern;

public interface ResponseServices {
   void setNextFileDate(Date var1);

   Date getNextFileDate();

   String convertPathname(String var1, String var2);

   void updateAdminData(String var1, String var2, Entry var3) throws IOException;

   void setEntry(File var1, Entry var2) throws IOException;

   void removeEntry(File var1) throws IOException;

   void removeLocalFile(String var1) throws IOException;

   void removeLocalFile(String var1, String var2) throws IOException;

   void renameLocalFile(String var1, String var2) throws IOException;

   EventManager getEventManager();

   FileHandler getUncompressedFileHandler();

   FileHandler getGzipFileHandler();

   void dontUseGzipFileHandler();

   void setValidRequests(String var1);

   void addWrapper(StringPattern var1, KeywordSubstitutionOptions var2);

   GlobalOptions getGlobalOptions();
}
