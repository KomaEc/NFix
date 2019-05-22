package org.apache.velocity.context;

public interface InternalWrapperContext {
   Context getInternalUserContext();

   InternalContextAdapter getBaseContext();

   Object localPut(String var1, Object var2);
}
