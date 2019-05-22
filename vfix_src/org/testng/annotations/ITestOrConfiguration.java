package org.testng.annotations;

public interface ITestOrConfiguration extends IParameterizable {
   long getTimeOut();

   void setTimeOut(long var1);

   String[] getGroups();

   void setGroups(String[] var1);

   String[] getDependsOnGroups();

   void setDependsOnGroups(String[] var1);

   String[] getDependsOnMethods();

   void setDependsOnMethods(String[] var1);

   String getDescription();

   void setDescription(String var1);
}
