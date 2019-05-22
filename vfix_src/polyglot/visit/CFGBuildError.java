package polyglot.visit;

import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public class CFGBuildError extends InternalCompilerError {
   public CFGBuildError(String msg) {
      super(msg);
   }

   public CFGBuildError(Position position, String msg) {
      super(position, msg);
   }

   public CFGBuildError(String msg, Position position) {
      super(msg, position);
   }
}
