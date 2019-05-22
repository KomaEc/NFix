package org.netbeans.lib.cvsclient.command;

public interface Builder {
   void parseLine(String var1, boolean var2);

   void parseEnhancedMessage(String var1, Object var2);

   void outputDone();
}
