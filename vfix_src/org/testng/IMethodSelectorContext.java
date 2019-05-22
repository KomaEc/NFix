package org.testng;

import java.util.Map;

public interface IMethodSelectorContext {
   boolean isStopped();

   void setStopped(boolean var1);

   Map<Object, Object> getUserData();
}
