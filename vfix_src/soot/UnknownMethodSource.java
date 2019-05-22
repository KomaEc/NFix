package soot;

public class UnknownMethodSource implements MethodSource {
   UnknownMethodSource() {
   }

   public Body getBody(SootMethod m, String phaseName) {
      throw new RuntimeException("Can't get body for unknown source!");
   }
}
