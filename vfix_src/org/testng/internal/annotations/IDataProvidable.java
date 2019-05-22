package org.testng.internal.annotations;

public interface IDataProvidable {
   String getDataProvider();

   void setDataProvider(String var1);

   Class<?> getDataProviderClass();

   void setDataProviderClass(Class<?> var1);
}
