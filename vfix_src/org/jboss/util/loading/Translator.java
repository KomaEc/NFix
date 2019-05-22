package org.jboss.util.loading;

import java.security.ProtectionDomain;

public interface Translator {
   byte[] transform(ClassLoader var1, String var2, Class<?> var3, ProtectionDomain var4, byte[] var5) throws Exception;

   void unregisterClassLoader(ClassLoader var1);
}
