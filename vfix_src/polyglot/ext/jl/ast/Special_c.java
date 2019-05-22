package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Special;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.ClassType;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class Special_c extends Expr_c implements Special {
   protected Special.Kind kind;
   protected TypeNode qualifier;

   public Special_c(Position pos, Special.Kind kind, TypeNode qualifier) {
      super(pos);
      this.kind = kind;
      this.qualifier = qualifier;
   }

   public Precedence precedence() {
      return Precedence.LITERAL;
   }

   public Special.Kind kind() {
      return this.kind;
   }

   public Special kind(Special.Kind kind) {
      Special_c n = (Special_c)this.copy();
      n.kind = kind;
      return n;
   }

   public TypeNode qualifier() {
      return this.qualifier;
   }

   public Special qualifier(TypeNode qualifier) {
      Special_c n = (Special_c)this.copy();
      n.qualifier = qualifier;
      return n;
   }

   protected Special_c reconstruct(TypeNode qualifier) {
      if (qualifier != this.qualifier) {
         Special_c n = (Special_c)this.copy();
         n.qualifier = qualifier;
         return n;
      } else {
         return this;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      TypeNode qualifier = (TypeNode)this.visitChild(this.qualifier, v);
      return this.reconstruct(qualifier);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      Context c = tc.context();
      ClassType t;
      if (this.qualifier == null) {
         t = c.currentClass();
      } else {
         if (!this.qualifier.type().isClass()) {
            throw new SemanticException("Qualified " + this.kind + " expression must be of a class type", this.qualifier.position());
         }

         t = this.qualifier.type().toClass();
         if (!c.currentClass().hasEnclosingInstance(t)) {
            throw new SemanticException("The nested class \"" + c.currentClass() + "\" does not have " + "an enclosing instance of type \"" + t + "\".", this.qualifier.position());
         }
      }

      if (c.inStaticContext() && ts.equals(t, c.currentClass())) {
         throw new SemanticException("Cannot access a non-static field or method, or refer to \"this\" or \"super\" from a static context.", this.position());
      } else if (this.kind == Special.THIS) {
         return this.type(t);
      } else {
         return (Node)(this.kind == Special.SUPER ? this.type(t.superType()) : this);
      }
   }

   public Term entry() {
      return this;
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      return succs;
   }

   public String toString() {
      return (this.qualifier != null ? this.qualifier + "." : "") + this.kind;
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      if (this.qualifier != null) {
         this.print(this.qualifier, w, tr);
         w.write(".");
      }

      w.write(this.kind.toString());
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      if (this.kind != null) {
         w.allowBreak(4, " ");
         w.begin(0);
         w.write("(kind " + this.kind + ")");
         w.end();
      }

   }
}
