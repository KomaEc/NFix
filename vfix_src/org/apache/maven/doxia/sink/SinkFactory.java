package org.apache.maven.doxia.sink;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public interface SinkFactory {
   String ROLE = SinkFactory.class.getName();

   Sink createSink(File var1, String var2) throws IOException;

   Sink createSink(File var1, String var2, String var3) throws IOException;

   Sink createSink(OutputStream var1) throws IOException;

   Sink createSink(OutputStream var1, String var2) throws IOException;
}
