package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common;

import java.io.PrintStream;

public interface SLF4JPrintStream {
   void registerLoggerAppender(Object var1);

   void deregisterLoggerAppender();

   PrintStream getOriginalPrintStream();
}
