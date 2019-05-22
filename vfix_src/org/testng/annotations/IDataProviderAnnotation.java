package org.testng.annotations;

public interface IDataProviderAnnotation extends IAnnotation {
   String getName();

   void setName(String var1);

   boolean isParallel();

   void setParallel(boolean var1);
}
