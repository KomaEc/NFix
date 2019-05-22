package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SysOutOverSLF4JServletContextListener implements ServletContextListener {
   public final void contextInitialized(ServletContextEvent servletContextEvent) {
      SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
   }

   public void contextDestroyed(ServletContextEvent servletContextEvent) {
      SysOutOverSLF4J.stopSendingSystemOutAndErrToSLF4J();
   }
}
