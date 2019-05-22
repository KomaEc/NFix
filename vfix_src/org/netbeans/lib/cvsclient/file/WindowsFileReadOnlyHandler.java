package org.netbeans.lib.cvsclient.file;

import java.io.File;
import java.io.IOException;
import org.netbeans.lib.cvsclient.util.BugLog;

public class WindowsFileReadOnlyHandler implements FileReadOnlyHandler {
   public void setFileReadOnly(File var1, boolean var2) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("file must not be null");
      } else {
         try {
            String[] var3 = new String[]{"attrib", var2 ? "+r" : "-r", var1.getName()};
            Process var4 = Runtime.getRuntime().exec(var3, (String[])null, var1.getParentFile());
            var4.waitFor();
         } catch (InterruptedException var5) {
            BugLog.getInstance().showException(var5);
         }

      }
   }
}
