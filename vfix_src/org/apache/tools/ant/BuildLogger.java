package org.apache.tools.ant;

import java.io.PrintStream;

public interface BuildLogger extends BuildListener {
   void setMessageOutputLevel(int var1);

   void setOutputPrintStream(PrintStream var1);

   void setEmacsMode(boolean var1);

   void setErrorPrintStream(PrintStream var1);
}
