package com.gzoltar.shaded.org.pitest.testapi.execute;

import com.gzoltar.shaded.org.pitest.testapi.TestResult;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import java.util.List;

public interface Container {
   List<TestResult> execute(TestUnit var1);
}
