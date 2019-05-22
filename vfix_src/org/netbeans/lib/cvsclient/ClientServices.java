package org.netbeans.lib.cvsclient;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.command.CommandAbortedException;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.file.FileHandler;
import org.netbeans.lib.cvsclient.request.UnconfiguredRequestException;
import org.netbeans.lib.cvsclient.response.ResponseException;
import org.netbeans.lib.cvsclient.util.IgnoreFileFilter;

public interface ClientServices {
   void processRequests(List var1) throws IOException, UnconfiguredRequestException, ResponseException, CommandAbortedException;

   String getRepository();

   String getRepositoryForDirectory(String var1) throws IOException;

   String getRepositoryForDirectory(File var1) throws IOException;

   String getLocalPath();

   Entry getEntry(File var1) throws IOException;

   Iterator getEntries(File var1) throws IOException;

   void updateAdminData(String var1, String var2, Entry var3) throws IOException;

   Set getAllFiles(File var1) throws IOException;

   boolean isFirstCommand();

   void setIsFirstCommand(boolean var1);

   void removeEntry(File var1) throws IOException;

   void setIgnoreFileFilter(IgnoreFileFilter var1);

   IgnoreFileFilter getIgnoreFileFilter();

   boolean shouldBeIgnored(File var1, String var2);

   void setUncompressedFileHandler(FileHandler var1);

   void setGzipFileHandler(FileHandler var1);

   String getStickyTagForDirectory(File var1);

   void ensureConnection() throws AuthenticationException;

   Map getWrappersMap() throws CommandException;

   GlobalOptions getGlobalOptions();

   boolean exists(File var1);

   boolean isAborted();
}
