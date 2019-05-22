package com.gzoltar.shaded.org.pitest.mutationtest.tooling;

import com.gzoltar.shaded.org.pitest.mutationtest.ClassMutationResults;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListener;
import java.io.PrintStream;

public class SpinnerListener implements MutationResultListener {
   private static final String[] SPINNER_CHARS = new String[]{"\b/", "\b-", "\b\\", "\b|"};
   private final PrintStream out;
   private int position = 0;

   public SpinnerListener(PrintStream out) {
      this.out = out;
   }

   public void runStart() {
   }

   public void handleMutationResult(ClassMutationResults metaData) {
      this.out.printf("%s", SPINNER_CHARS[this.position % SPINNER_CHARS.length]);
      ++this.position;
   }

   public void runEnd() {
   }
}
