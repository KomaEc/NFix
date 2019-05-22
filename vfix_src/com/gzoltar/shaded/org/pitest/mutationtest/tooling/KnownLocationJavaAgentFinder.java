package com.gzoltar.shaded.org.pitest.mutationtest.tooling;

import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.process.JavaAgent;

public class KnownLocationJavaAgentFinder implements JavaAgent {
   private final String location;

   public KnownLocationJavaAgentFinder(String location) {
      this.location = location;
   }

   public Option<String> getJarLocation() {
      return Option.some(this.location);
   }

   public void close() {
   }
}
