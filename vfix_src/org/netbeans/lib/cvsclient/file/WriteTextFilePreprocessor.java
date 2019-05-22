package org.netbeans.lib.cvsclient.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface WriteTextFilePreprocessor {
   void copyTextFileToLocation(InputStream var1, File var2, OutputStreamProvider var3) throws IOException;
}
