package com.gzoltar.shaded.org.pitest.coverage;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import java.math.BigInteger;
import java.util.Collection;

public interface CoverageDatabase {
   Collection<ClassInfo> getClassInfo(Collection<ClassName> var1);

   int getNumberOfCoveredLines(Collection<ClassName> var1);

   Collection<TestInfo> getTestsForClass(ClassName var1);

   Collection<TestInfo> getTestsForClassLine(ClassLine var1);

   BigInteger getCoverageIdForClass(ClassName var1);

   Collection<ClassInfo> getClassesForFile(String var1, String var2);

   CoverageSummary createSummary();
}
