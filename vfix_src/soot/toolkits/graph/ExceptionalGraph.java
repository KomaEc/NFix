package soot.toolkits.graph;

import java.util.Collection;
import java.util.List;
import soot.Body;
import soot.Trap;
import soot.toolkits.exceptions.ThrowableSet;

public interface ExceptionalGraph<N> extends DirectedGraph<N> {
   Body getBody();

   List<N> getUnexceptionalPredsOf(N var1);

   List<N> getUnexceptionalSuccsOf(N var1);

   List<N> getExceptionalPredsOf(N var1);

   List<N> getExceptionalSuccsOf(N var1);

   Collection<? extends ExceptionalGraph.ExceptionDest<N>> getExceptionDests(N var1);

   public interface ExceptionDest<N> {
      Trap getTrap();

      ThrowableSet getThrowables();

      N getHandlerNode();
   }
}
