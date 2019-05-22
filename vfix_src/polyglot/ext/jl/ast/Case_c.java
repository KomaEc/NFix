package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Case;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Local;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.FieldInstance;
import polyglot.types.LocalInstance;
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

public class Case_c extends Stmt_c implements Case {
   protected Expr expr;
   protected long value;

   public Case_c(Position pos, Expr expr) {
      super(pos);
      this.expr = expr;
   }

   public boolean isDefault() {
      return this.expr == null;
   }

   public Expr expr() {
      return this.expr;
   }

   public Case expr(Expr expr) {
      Case_c n = (Case_c)this.copy();
      n.expr = expr;
      return n;
   }

   public long value() {
      return this.value;
   }

   public Case value(long value) {
      Case_c n = (Case_c)this.copy();
      n.value = value;
      return n;
   }

   protected Case_c reconstruct(Expr expr) {
      if (expr != this.expr) {
         Case_c n = (Case_c)this.copy();
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
      if (this.expr == null) {
         return this;
      } else {
         TypeSystem ts = tc.typeSystem();
         if (!ts.isImplicitCastValid(this.expr.type(), ts.Int())) {
            throw new SemanticException("Case label must be an byte, char, short, or int.", this.position());
         } else {
            Object o = null;
            if (this.expr instanceof Field) {
               FieldInstance fi = ((Field)this.expr).fieldInstance();
               if (fi == null) {
                  throw new InternalCompilerError("Undefined FieldInstance after type-checking.");
               }

               if (!fi.isConstant()) {
                  throw new SemanticException("Case label must be an integral constant.", this.position());
               }

               o = fi.constantValue();
            } else if (this.expr instanceof Local) {
               LocalInstance li = ((Local)this.expr).localInstance();
               if (li == null) {
                  throw new InternalCompilerError("Undefined LocalInstance after type-checking.");
               }

               if (!li.isConstant()) {
                  return this;
               }

               o = li.constantValue();
            } else {
               o = this.expr.constantValue();
            }

            if (o instanceof Number && !(o instanceof Long) && !(o instanceof Float) && !(o instanceof Double)) {
               return this.value(((Number)o).longValue());
            } else if (o instanceof Character) {
               return this.value((long)(Character)o);
            } else {
               throw new SemanticException("Case label must be an integral constant.", this.position());
            }
         }
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      return (Type)(child == this.expr ? ts.Int() : child.type());
   }

   public String toString() {
      return this.expr == null ? "default:" : "case " + this.expr + ":";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      if (this.expr == null) {
         w.write("default:");
      } else {
         w.write("case ");
         this.print(this.expr, w, tr);
         w.write(":");
      }

   }

   public Term entry() {
      return (Term)(this.expr != null ? this.expr : this);
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      if (this.expr != null) {
         v.visitCFG(this.expr, (Term)this);
      }

      return succs;
   }
}
