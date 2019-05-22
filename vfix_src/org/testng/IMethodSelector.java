package org.testng;

import java.io.Serializable;
import java.util.List;

public interface IMethodSelector extends Serializable {
   boolean includeMethod(IMethodSelectorContext var1, ITestNGMethod var2, boolean var3);

   void setTestMethods(List<ITestNGMethod> var1);
}
