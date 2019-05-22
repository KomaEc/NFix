package polyglot.ext.jl.qq;

import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public class QQError extends InternalCompilerError {
   public QQError(String msg, Position pos) {
      super(msg, pos);
   }
}
