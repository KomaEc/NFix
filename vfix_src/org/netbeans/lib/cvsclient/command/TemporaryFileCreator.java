package org.netbeans.lib.cvsclient.command;

import java.io.File;
import java.io.IOException;

public interface TemporaryFileCreator {
   File createTempFile(String var1) throws IOException;
}
