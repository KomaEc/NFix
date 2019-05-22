package com.gzoltar.shaded.org.jacoco.report.check;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;

public interface IViolationsOutput {
   void onViolation(ICoverageNode var1, Rule var2, Limit var3, String var4);
}
