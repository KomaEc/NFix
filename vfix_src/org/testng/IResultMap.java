package org.testng;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public interface IResultMap extends Serializable {
   void addResult(ITestResult var1, ITestNGMethod var2);

   Set<ITestResult> getResults(ITestNGMethod var1);

   Set<ITestResult> getAllResults();

   void removeResult(ITestNGMethod var1);

   void removeResult(ITestResult var1);

   Collection<ITestNGMethod> getAllMethods();

   int size();
}
