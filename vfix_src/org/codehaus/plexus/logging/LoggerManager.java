package org.codehaus.plexus.logging;

public interface LoggerManager {
   String ROLE = (null.class$org$codehaus$plexus$logging$LoggerManager == null ? (null.class$org$codehaus$plexus$logging$LoggerManager = null.class$("org.codehaus.plexus.logging.LoggerManager")) : null.class$org$codehaus$plexus$logging$LoggerManager).getName();

   void setThreshold(int var1);

   int getThreshold();

   void setThreshold(String var1, int var2);

   void setThreshold(String var1, String var2, int var3);

   int getThreshold(String var1);

   int getThreshold(String var1, String var2);

   Logger getLoggerForComponent(String var1);

   Logger getLoggerForComponent(String var1, String var2);

   void returnComponentLogger(String var1);

   void returnComponentLogger(String var1, String var2);

   int getActiveLoggerCount();
}
