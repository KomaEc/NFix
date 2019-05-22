package polyglot.visit;

import java.util.HashMap;
import java.util.Map;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.SubtypeSet;

public class ExceptionChecker extends ErrorHandlingVisitor {
   protected ExceptionChecker outer = null;
   private SubtypeSet scope = null;
   protected Map exceptionPositions = new HashMap();

   public ExceptionChecker(Job job, TypeSystem ts, NodeFactory nf) {
      super(job, ts, nf);
   }

   public ExceptionChecker pushNew() {
      ExceptionChecker ec = (ExceptionChecker)this.visitChildren();
      ec.scope = null;
      ec.outer = this;
      ec.exceptionPositions = new HashMap();
      return ec;
   }

   public ExceptionChecker push() {
      ExceptionChecker ec = (ExceptionChecker)this.visitChildren();
      ec.outer = this;
      ec.exceptionPositions = new HashMap();
      return ec;
   }

   public ExceptionChecker pop() {
      return this.outer;
   }

   protected NodeVisitor enterCall(Node n) throws SemanticException {
      return n.exceptionCheckEnter(this);
   }

   protected NodeVisitor enterError(Node n) {
      return this.push();
   }

   protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
      ExceptionChecker inner = (ExceptionChecker)v;
      if (inner.outer != this) {
         throw new InternalCompilerError("oops!");
      } else {
         n = n.del().exceptionCheck(inner);
         SubtypeSet t = inner.throwsSet();
         this.throwsSet().addAll(t);
         this.exceptionPositions.putAll(inner.exceptionPositions);
         return n;
      }
   }

   public void throwsException(Type t, Position pos) {
      this.throwsSet().add(t);
      this.exceptionPositions.put(t, pos);
   }

   public SubtypeSet throwsSet() {
      if (this.scope == null) {
         this.scope = new SubtypeSet(this.ts.Throwable());
      }

      return this.scope;
   }

   public Position exceptionPosition(Type t) {
      return (Position)this.exceptionPositions.get(t);
   }
}
