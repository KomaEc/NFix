package com.gzoltar.shaded.org.pitest.mutationtest.execute;

import com.gzoltar.shaded.org.pitest.functional.SideEffect;
import com.gzoltar.shaded.org.pitest.util.ExitCode;

public class TimeOutSystemExitSideEffect implements SideEffect {
   private final Reporter r;

   public TimeOutSystemExitSideEffect(Reporter r) {
      this.r = r;
   }

   public void apply() {
      this.r.done(ExitCode.TIMEOUT);
   }
}
