package soot.shimple;

import soot.Body;
import soot.MethodSource;
import soot.SootMethod;

public class ShimpleMethodSource implements MethodSource {
   MethodSource ms;

   public ShimpleMethodSource(MethodSource ms) {
      this.ms = ms;
   }

   public Body getBody(SootMethod m, String phaseName) {
      Body b = this.ms.getBody(m, phaseName);
      return b == null ? null : Shimple.v().newBody(b);
   }
}
