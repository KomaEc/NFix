package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.SemanticException;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.SubtypeSet;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;

public abstract class Term_c extends Node_c implements Term {
   protected boolean reachable;
   protected SubtypeSet exceptions;

   public Term_c(Position pos) {
      super(pos);
   }

   public abstract Term entry();

   public abstract List acceptCFG(CFGBuilder var1, List var2);

   public boolean reachable() {
      return this.reachable;
   }

   public Term reachable(boolean reachability) {
      if (this.reachable == reachability) {
         return this;
      } else {
         Term_c t = (Term_c)this.copy();
         t.reachable = reachability;
         return t;
      }
   }

   public static Term listEntry(List l, Term alt) {
      Term c = (Term)CollectionUtil.firstOrElse(l, alt);
      return c != alt ? c.entry() : alt;
   }

   public SubtypeSet exceptions() {
      return this.exceptions;
   }

   public Term exceptions(SubtypeSet exceptions) {
      Term_c n = (Term_c)this.copy();
      n.exceptions = new SubtypeSet(exceptions);
      return n;
   }

   public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
      Term t = (Term)super.exceptionCheck(ec);
      return t.exceptions(ec.throwsSet());
   }
}
