package com.gzoltar.shaded.jline;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Terminal {
   void init() throws Exception;

   void restore() throws Exception;

   void reset() throws Exception;

   boolean isSupported();

   int getWidth();

   int getHeight();

   boolean isAnsiSupported();

   OutputStream wrapOutIfNeeded(OutputStream var1);

   InputStream wrapInIfNeeded(InputStream var1) throws IOException;

   boolean hasWeirdWrap();

   boolean isEchoEnabled();

   void setEchoEnabled(boolean var1);

   String getOutputEncoding();
}
