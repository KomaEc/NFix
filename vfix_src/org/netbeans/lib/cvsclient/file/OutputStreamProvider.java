package org.netbeans.lib.cvsclient.file;

import java.io.IOException;
import java.io.OutputStream;

public interface OutputStreamProvider {
   OutputStream createOutputStream() throws IOException;
}
