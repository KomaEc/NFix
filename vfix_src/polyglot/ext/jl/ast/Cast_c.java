package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.List;
import polyglot.ast.Cast;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class Cast_c extends Expr_c implements Cast {
   protected TypeNode castType;
   protected Expr expr;

   public Cast_c(Position pos, TypeNode castType, Expr expr) {
      super(pos);
      this.castType = castType;
      this.expr = expr;
   }

   public Precedence precedence() {
      return Precedence.CAST;
   }

   public TypeNode castType() {
      return this.castType;
   }

   public Cast castType(TypeNode castType) {
      Cast_c n = (Cast_c)this.copy();
      n.castType = castType;
      return n;
   }

   public Expr expr() {
      return this.expr;
   }

   public Cast expr(Expr expr) {
      Cast_c n = (Cast_c)this.copy();
      n.expr = expr;
      return n;
   }

   protected Cast_c reconstruct(TypeNode castType, Expr expr) {
      if (castType == this.castType && expr == this.expr) {
         return this;
      } else {
         Cast_c n = (Cast_c)this.copy();
         n.castType = castType;
         n.expr = expr;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      TypeNode castType = (TypeNode)this.visitChild(this.castType, v);
      Expr expr = (Expr)this.visitChild(this.expr, v);
      return this.reconstruct(castType, expr);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      if (!ts.isCastValid(this.expr.type(), this.castType.type())) {
         throw new SemanticException("Cannot cast the expression of type \"" + this.expr.type() + "\" to type \"" + this.castType.type() + "\".", this.position());
      } else {
         return this.type(this.castType.type());
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      if (child == this.expr) {
         if (this.castType.type().isReference()) {
            return ts.Object();
         }

         if (this.castType.type().isNumeric()) {
            return ts.Double();
         }

         if (this.castType.type().isBoolean()) {
            return ts.Boolean();
         }
      }

      return child.type();
   }

   public String toString() {
      return "(" + this.castType + ") " + this.expr;
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.begin(0);
      w.write("(");
      this.print(this.castType, w, tr);
      w.write(")");
      w.allowBreak(2, " ");
      this.printSubExpr(this.expr, w, tr);
      w.end();
   }

   public Term entry() {
      return this.expr.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFG(this.expr, (Term)this);
      return succs;
   }

   public List throwTypes(TypeSystem ts) {
      return this.expr.type().isReference() ? Collections.singletonList(ts.ClassCastException()) : Collections.EMPTY_LIST;
   }

   public boolean isConstant() {
      return this.expr.isConstant() && this.castType.type().isPrimitive();
   }

   public Object constantValue() {
      Object v = this.expr.constantValue();
      if (v == null) {
         return null;
      } else if (v instanceof Boolean && this.castType.type().isBoolean()) {
         return v;
      } else {
         if (v instanceof String) {
            TypeSystem ts = this.castType.type().typeSystem();
            if (this.castType.type().equals(ts.String())) {
               return v;
            }
         }

         if (v instanceof Double) {
            double vv = (Double)v;
            if (this.castType.type().isDouble()) {
               return new Double(vv);
            }

            if (this.castType.type().isFloat()) {
               return new Float((float)vv);
            }

            if (this.castType.type().isLong()) {
               return new Long((long)vv);
            }

            if (this.castType.type().isInt()) {
               return new Integer((int)vv);
            }

            if (this.castType.type().isChar()) {
               return new Character((char)((int)vv));
            }

            if (this.castType.type().isShort()) {
               return new Short((short)((int)vv));
            }

            if (this.castType.type().isByte()) {
               return new Byte((byte)((int)vv));
            }
         }

         if (v instanceof Float) {
            float vv = (Float)v;
            if (this.castType.type().isDouble()) {
               return new Double((double)vv);
            }

            if (this.castType.type().isFloat()) {
               return new Float(vv);
            }

            if (this.castType.type().isLong()) {
               return new Long((long)vv);
            }

            if (this.castType.type().isInt()) {
               return new Integer((int)vv);
            }

            if (this.castType.type().isChar()) {
               return new Character((char)((int)vv));
            }

            if (this.castType.type().isShort()) {
               return new Short((short)((int)vv));
            }

            if (this.castType.type().isByte()) {
               return new Byte((byte)((int)vv));
            }
         }

         if (v instanceof Number) {
            long vv = ((Number)v).longValue();
            if (this.castType.type().isDouble()) {
               return new Double((double)vv);
            }

            if (this.castType.type().isFloat()) {
               return new Float((float)vv);
            }

            if (this.castType.type().isLong()) {
               return new Long(vv);
            }

            if (this.castType.type().isInt()) {
               return new Integer((int)vv);
            }

            if (this.castType.type().isChar()) {
               return new Character((char)((int)vv));
            }

            if (this.castType.type().isShort()) {
               return new Short((short)((int)vv));
            }

            if (this.castType.type().isByte()) {
               return new Byte((byte)((int)vv));
            }
         }

         if (v instanceof Character) {
            char vv = (Character)v;
            if (this.castType.type().isDouble()) {
               return new Double((double)vv);
            }

            if (this.castType.type().isFloat()) {
               return new Float((float)vv);
            }

            if (this.castType.type().isLong()) {
               return new Long((long)vv);
            }

            if (this.castType.type().isInt()) {
               return new Integer(vv);
            }

            if (this.castType.type().isChar()) {
               return new Character(vv);
            }

            if (this.castType.type().isShort()) {
               return new Short((short)vv);
            }

            if (this.castType.type().isByte()) {
               return new Byte((byte)vv);
            }
         }

         return null;
      }
   }
}
