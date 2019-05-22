package com.gzoltar.shaded.org.pitest.mutationtest;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.classinfo.HierarchicalClassId;
import com.gzoltar.shaded.org.pitest.coverage.CoverageDatabase;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import java.util.Collection;
import java.util.Map;

public interface HistoryStore {
   void initialize();

   void recordClassPath(Collection<HierarchicalClassId> var1, CoverageDatabase var2);

   void recordResult(MutationResult var1);

   Map<MutationIdentifier, MutationStatusTestPair> getHistoricResults();

   Map<ClassName, ClassHistory> getHistoricClassPath();
}
