package org.apache.velocity.runtime.resource.util;

public interface StringResourceRepository {
   StringResource getStringResource(String var1);

   void putStringResource(String var1, String var2);

   void removeStringResource(String var1);

   void setEncoding(String var1);

   String getEncoding();
}
