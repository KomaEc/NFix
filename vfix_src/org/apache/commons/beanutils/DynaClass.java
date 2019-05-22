package org.apache.commons.beanutils;

public interface DynaClass {
   String getName();

   DynaProperty getDynaProperty(String var1);

   DynaProperty[] getDynaProperties();

   DynaBean newInstance() throws IllegalAccessException, InstantiationException;
}
