package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context.exceptionhandlers;

import org.slf4j.Logger;

public interface ExceptionHandlingStrategy {
   void handleExceptionLine(String var1, Logger var2);

   void notifyNotStackTrace();
}
