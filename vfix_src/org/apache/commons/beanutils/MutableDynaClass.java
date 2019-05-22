package org.apache.commons.beanutils;

public interface MutableDynaClass extends DynaClass {
   void add(String var1);

   void add(String var1, Class var2);

   void add(String var1, Class var2, boolean var3, boolean var4);

   boolean isRestricted();

   void remove(String var1);

   void setRestricted(boolean var1);
}
