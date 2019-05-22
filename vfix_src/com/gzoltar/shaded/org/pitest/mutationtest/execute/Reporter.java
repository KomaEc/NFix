package com.gzoltar.shaded.org.pitest.mutationtest.execute;

import com.gzoltar.shaded.org.pitest.mutationtest.MutationStatusTestPair;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.util.ExitCode;
import java.io.IOException;

public interface Reporter {
   void describe(MutationIdentifier var1) throws IOException;

   void report(MutationIdentifier var1, MutationStatusTestPair var2) throws IOException;

   void done(ExitCode var1);
}
