package org.jboss.util;

import java.util.EventListener;

public interface ThrowableListener extends EventListener {
   void onThrowable(int var1, Throwable var2);
}
