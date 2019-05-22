package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Assert;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.main.Options;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.ErrorQueue;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeChecker;

public class Assert_c extends Stmt_c implements Assert {
   protected Expr cond;
   protected Expr errorMessage;

   public Assert_c(Position pos, Expr cond, Expr errorMessage) {
      super(pos);
      this.cond = cond;
      this.errorMessage = errorMessage;
   }

   public Expr cond() {
      return this.cond;
   }

   public Assert cond(Expr cond) {
      Assert_c n = (Assert_c)this.copy();
      n.cond = cond;
      return n;
   }

   public Expr errorMessage() {
      return this.errorMessage;
   }

   public Assert errorMessage(Expr errorMessage) {
      Assert_c n = (Assert_c)this.copy();
      n.errorMessage = errorMessage;
      return n;
   }

   protected Assert_c reconstruct(Expr cond, Expr errorMessage) {
      if (cond == this.cond && errorMessage == this.errorMessage) {
         return this;
      } else {
         Assert_c n = (Assert_c)this.copy();
         n.cond = cond;
         n.errorMessage = errorMessage;
         return n;
      }
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      if (!Options.global.assertions) {
         ErrorQueue eq = tc.errorQueue();
         eq.enqueue(0, "assert statements are disabled. Recompile with -assert and ensure the post compiler supports assert (e.g., -post \"javac -source 1.4\"). Removing the statement and continuing.", this.cond.position());
      }

      if (!ts.equals(this.cond.type(), ts.Boolean())) {
         throw new SemanticException("Condition of assert statement must have boolean type.", this.cond.position());
      } else if (this.errorMessage != null && ts.equals(this.errorMessage.type(), ts.Void())) {
         throw new SemanticException("Error message in assert statement must have a value.", this.errorMessage.position());
      } else {
         return this;
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      return (Type)(child == this.cond ? ts.Boolean() : child.type());
   }

   public Node visitChildren(NodeVisitor v) {
      Expr cond = (Expr)this.visitChild(this.cond, v);
      Expr errorMessage = (Expr)this.visitChild(this.errorMessage, v);
      return this.reconstruct(cond, errorMessage);
   }

   public String toString() {
      return "assert " + this.cond.toString() + (this.errorMessage != null ? ": " + this.errorMessage.toString() : "") + ";";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("assert ");
      this.print(this.cond, w, tr);
      if (this.errorMessage != null) {
         w.write(": ");
         this.print(this.errorMessage, w, tr);
      }

      w.write(";");
   }

   public void translate(CodeWriter w, Translator tr) {
      if (!Options.global.assertions) {
         w.write(";");
      } else {
         this.prettyPrint(w, tr);
      }

   }

   public Term entry() {
      return this.cond.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      if (this.errorMessage != null) {
         v.visitCFG(this.cond, (Term)this.errorMessage.entry());
         v.visitCFG(this.errorMessage, (Term)this);
      } else {
         v.visitCFG(this.cond, (Term)this);
      }

      return succs;
   }
}
