package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ExecuteStreamHandler {
   void setProcessInputStream(OutputStream var1) throws IOException;

   void setProcessErrorStream(InputStream var1) throws IOException;

   void setProcessOutputStream(InputStream var1) throws IOException;

   void start() throws IOException;

   void stop();
}
