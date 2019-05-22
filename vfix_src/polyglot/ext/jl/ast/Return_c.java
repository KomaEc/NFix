package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.List;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Return;
import polyglot.ast.Term;
import polyglot.types.CodeInstance;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.InitializerInstance;
import polyglot.types.MethodInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class Return_c extends Stmt_c implements Return {
   protected Expr expr;

   public Return_c(Position pos, Expr expr) {
      super(pos);
      this.expr = expr;
   }

   public Expr expr() {
      return this.expr;
   }

   public Return expr(Expr expr) {
      Return_c n = (Return_c)this.copy();
      n.expr = expr;
      return n;
   }

   protected Return_c reconstruct(Expr expr) {
      if (expr != this.expr) {
         Return_c n = (Return_c)this.copy();
         n.expr = expr;
         return n;
      } else {
         return this;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Expr expr = (Expr)this.visitChild(this.expr, v);
      return this.reconstruct(expr);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      Context c = tc.context();
      CodeInstance ci = c.currentCode();
      if (ci instanceof InitializerInstance) {
         throw new SemanticException("Cannot return from an initializer block.", this.position());
      } else if (ci instanceof ConstructorInstance) {
         if (this.expr != null) {
            throw new SemanticException("Cannot return a value from " + ci + ".", this.position());
         } else {
            return this;
         }
      } else if (ci instanceof MethodInstance) {
         MethodInstance mi = (MethodInstance)ci;
         if (mi.returnType().isVoid()) {
            if (this.expr != null) {
               throw new SemanticException("Cannot return a value from " + mi + ".", this.position());
            } else {
               return this;
            }
         } else if (this.expr == null) {
            throw new SemanticException("Must return a value from " + mi + ".", this.position());
         } else if (ts.isImplicitCastValid(this.expr.type(), mi.returnType())) {
            return this;
         } else if (ts.numericConversionValid(mi.returnType(), this.expr.constantValue())) {
            return this;
         } else {
            throw new SemanticException("Cannot return expression of type " + this.expr.type() + " from " + mi + ".", this.expr.position());
         }
      } else {
         throw new InternalCompilerError("Unrecognized code type.");
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      if (child == this.expr) {
         Context c = av.context();
         CodeInstance ci = c.currentCode();
         if (ci instanceof MethodInstance) {
            MethodInstance mi = (MethodInstance)ci;
            TypeSystem ts = av.typeSystem();
            if (ts.numericConversionValid(mi.returnType(), child.constantValue())) {
               return child.type();
            }

            return mi.returnType();
         }
      }

      return child.type();
   }

   public String toString() {
      return "return" + (this.expr != null ? " " + this.expr : "") + ";";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("return");
      if (this.expr != null) {
         w.write(" ");
         this.print(this.expr, w, tr);
      }

      w.write(";");
   }

   public Term entry() {
      return (Term)(this.expr != null ? this.expr.entry() : this);
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      if (this.expr != null) {
         v.visitCFG(this.expr, (Term)this);
      }

      v.visitReturn(this);
      return Collections.EMPTY_LIST;
   }
}
