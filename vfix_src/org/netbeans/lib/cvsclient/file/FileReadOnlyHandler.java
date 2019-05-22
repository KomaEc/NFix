package org.netbeans.lib.cvsclient.file;

import java.io.File;
import java.io.IOException;

public interface FileReadOnlyHandler {
   void setFileReadOnly(File var1, boolean var2) throws IOException;
}
