package org.apache.commons.beanutils;

public interface DynaBean {
   boolean contains(String var1, String var2);

   Object get(String var1);

   Object get(String var1, int var2);

   Object get(String var1, String var2);

   DynaClass getDynaClass();

   void remove(String var1, String var2);

   void set(String var1, Object var2);

   void set(String var1, int var2, Object var3);

   void set(String var1, String var2, Object var3);
}
