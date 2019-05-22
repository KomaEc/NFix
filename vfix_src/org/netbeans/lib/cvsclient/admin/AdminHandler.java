package org.netbeans.lib.cvsclient.admin;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import org.netbeans.lib.cvsclient.command.GlobalOptions;

public interface AdminHandler {
   void updateAdminData(String var1, String var2, Entry var3, GlobalOptions var4) throws IOException;

   Entry getEntry(File var1) throws IOException;

   Iterator getEntries(File var1) throws IOException;

   void setEntry(File var1, Entry var2) throws IOException;

   String getRepositoryForDirectory(String var1, String var2) throws IOException;

   void removeEntry(File var1) throws IOException;

   Set getAllFiles(File var1) throws IOException;

   String getStickyTagForDirectory(File var1);

   boolean exists(File var1);
}
