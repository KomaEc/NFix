package org.codehaus.groovy.control.io;

import java.io.IOException;
import java.io.Reader;
import org.codehaus.groovy.control.HasCleanup;
import org.codehaus.groovy.control.Janitor;

public interface ReaderSource extends HasCleanup {
   Reader getReader() throws IOException;

   boolean canReopenSource();

   String getLine(int var1, Janitor var2);

   void cleanup();
}
