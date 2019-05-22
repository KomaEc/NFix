package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.coverage.TestInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import java.util.List;

public interface TestPrioritiser {
   List<TestInfo> assignTests(MutationDetails var1);
}
