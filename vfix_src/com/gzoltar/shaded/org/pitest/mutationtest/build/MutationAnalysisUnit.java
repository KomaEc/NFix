package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.mutationtest.MutationMetaData;
import java.util.concurrent.Callable;

public interface MutationAnalysisUnit extends Callable<MutationMetaData> {
   int priority();
}
