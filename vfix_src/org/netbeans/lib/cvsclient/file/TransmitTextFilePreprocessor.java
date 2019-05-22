package org.netbeans.lib.cvsclient.file;

import java.io.File;
import java.io.IOException;

public interface TransmitTextFilePreprocessor {
   File getPreprocessedTextFile(File var1) throws IOException;

   void cleanup(File var1);

   void setTempDir(File var1);
}
