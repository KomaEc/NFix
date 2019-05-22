package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import java.util.HashSet;
import java.util.Set;

public class PremutationClassInfo {
   private final Set<Integer> linesToAvoid = new HashSet();

   public void registerLineToAvoid(int lineNumber) {
      this.linesToAvoid.add(lineNumber);
   }

   public boolean isLineToAvoid(int line) {
      return this.linesToAvoid.contains(line);
   }
}
