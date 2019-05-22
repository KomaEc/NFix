package com.gzoltar.shaded.org.pitest.testapi.execute.containers;

import com.gzoltar.shaded.org.pitest.testapi.TestResult;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import com.gzoltar.shaded.org.pitest.testapi.execute.Container;
import com.gzoltar.shaded.org.pitest.util.IsolationUtils;
import java.util.ArrayList;
import java.util.List;

public class UnContainer implements Container {
   public List<TestResult> execute(TestUnit group) {
      List<TestResult> results = new ArrayList(12);
      ConcreteResultCollector rc = new ConcreteResultCollector(results);
      group.execute(IsolationUtils.getContextClassLoader(), rc);
      return results;
   }
}
