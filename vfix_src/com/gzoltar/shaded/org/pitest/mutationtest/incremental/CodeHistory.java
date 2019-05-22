package com.gzoltar.shaded.org.pitest.mutationtest.incremental;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationStatusTestPair;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import java.math.BigInteger;

public interface CodeHistory {
   Option<MutationStatusTestPair> getPreviousResult(MutationIdentifier var1);

   boolean hasClassChanged(ClassName var1);

   boolean hasCoverageChanged(ClassName var1, BigInteger var2);
}
