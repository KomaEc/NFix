package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.AmbExpr;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Prefix;
import polyglot.ast.Term;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeChecker;

public class AmbExpr_c extends Expr_c implements AmbExpr {
   protected String name;

   public AmbExpr_c(Position pos, String name) {
      super(pos);
      this.name = name;
   }

   public Precedence precedence() {
      return Precedence.LITERAL;
   }

   public String name() {
      return this.name;
   }

   public AmbExpr name(String name) {
      AmbExpr_c n = (AmbExpr_c)this.copy();
      n.name = name;
      return n;
   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      Node n = ar.nodeFactory().disamb().disambiguate(this, ar, this.position(), (Prefix)null, this.name);
      if (n instanceof Expr) {
         return n;
      } else {
         throw new SemanticException("Could not find field or local variable \"" + this.name + "\".", this.position());
      }
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      throw new InternalCompilerError(this.position(), "Cannot type check ambiguous node " + this + ".");
   }

   public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
      throw new InternalCompilerError(this.position(), "Cannot exception check ambiguous node " + this + ".");
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write(this.name);
   }

   public void translate(CodeWriter w, Translator tr) {
      throw new InternalCompilerError(this.position(), "Cannot translate ambiguous node " + this + ".");
   }

   public String toString() {
      return this.name + "{amb}";
   }

   public Term entry() {
      return this;
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      return succs;
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(name \"" + this.name + "\")");
      w.end();
   }
}
