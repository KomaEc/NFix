package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Expr;
import polyglot.ast.IntLit;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.ast.Unary;
import polyglot.ast.Variable;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.FlowGraph;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class Unary_c extends Expr_c implements Unary {
   protected Unary.Operator op;
   protected Expr expr;

   public Unary_c(Position pos, Unary.Operator op, Expr expr) {
      super(pos);
      this.op = op;
      this.expr = expr;
   }

   public Precedence precedence() {
      return Precedence.UNARY;
   }

   public Expr expr() {
      return this.expr;
   }

   public Unary expr(Expr expr) {
      Unary_c n = (Unary_c)this.copy();
      n.expr = expr;
      return n;
   }

   public Unary.Operator operator() {
      return this.op;
   }

   public Unary operator(Unary.Operator op) {
      Unary_c n = (Unary_c)this.copy();
      n.op = op;
      return n;
   }

   protected Unary_c reconstruct(Expr expr) {
      if (expr != this.expr) {
         Unary_c n = (Unary_c)this.copy();
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
      if (this.op != Unary.POST_INC && this.op != Unary.POST_DEC && this.op != Unary.PRE_INC && this.op != Unary.PRE_DEC) {
         if (this.op == Unary.BIT_NOT) {
            if (!ts.isImplicitCastValid(this.expr.type(), ts.Long())) {
               throw new SemanticException("Operand of " + this.op + " operator must be numeric.", this.expr.position());
            } else {
               return this.type(ts.promote(this.expr.type()));
            }
         } else if (this.op != Unary.NEG && this.op != Unary.POS) {
            if (this.op == Unary.NOT) {
               if (!this.expr.type().isBoolean()) {
                  throw new SemanticException("Operand of " + this.op + " operator must be boolean.", this.expr.position());
               } else {
                  return this.type(this.expr.type());
               }
            } else {
               return this;
            }
         } else if (!this.expr.type().isNumeric()) {
            throw new SemanticException("Operand of " + this.op + " operator must be numeric.", this.expr.position());
         } else {
            return this.type(ts.promote(this.expr.type()));
         }
      } else if (!this.expr.type().isNumeric()) {
         throw new SemanticException("Operand of " + this.op + " operator must be numeric.", this.expr.position());
      } else if (!(this.expr instanceof Variable)) {
         throw new SemanticException("Operand of " + this.op + " operator must be a variable.", this.expr.position());
      } else if (((Variable)this.expr).flags().isFinal()) {
         throw new SemanticException("Operand of " + this.op + " operator must be a non-final variable.", this.expr.position());
      } else {
         return this.type(this.expr.type());
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();

      try {
         if (child == this.expr) {
            if (this.op != Unary.POST_INC && this.op != Unary.POST_DEC && this.op != Unary.PRE_INC && this.op != Unary.PRE_DEC) {
               if (this.op != Unary.NEG && this.op != Unary.POS) {
                  if (this.op == Unary.BIT_NOT) {
                     if (ts.isImplicitCastValid(child.type(), av.toType())) {
                        return ts.promote(child.type());
                     }

                     return av.toType();
                  }

                  if (this.op == Unary.NOT) {
                     return ts.Boolean();
                  }

                  return child.type();
               }

               if (ts.isImplicitCastValid(child.type(), av.toType())) {
                  return ts.promote(child.type());
               }

               return av.toType();
            }

            if (ts.isImplicitCastValid(child.type(), av.toType())) {
               return ts.promote(child.type());
            }

            return av.toType();
         }
      } catch (SemanticException var5) {
      }

      return child.type();
   }

   public String toString() {
      if (this.op == Unary.NEG && this.expr instanceof IntLit && ((IntLit)this.expr).boundary()) {
         return this.op.toString() + ((IntLit)this.expr).positiveToString();
      } else {
         return this.op.isPrefix() ? this.op.toString() + this.expr.toString() : this.expr.toString() + this.op.toString();
      }
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      if (this.op == Unary.NEG && this.expr instanceof IntLit && ((IntLit)this.expr).boundary()) {
         w.write(this.op.toString());
         w.write(((IntLit)this.expr).positiveToString());
      } else if (this.op.isPrefix()) {
         w.write(this.op.toString());
         this.printSubExpr(this.expr, false, w, tr);
      } else {
         this.printSubExpr(this.expr, false, w, tr);
         w.write(this.op.toString());
      }

   }

   public Term entry() {
      return this.expr.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      if (this.expr.type().isBoolean()) {
         v.visitCFG(this.expr, FlowGraph.EDGE_KEY_TRUE, this, FlowGraph.EDGE_KEY_FALSE, this);
      } else {
         v.visitCFG(this.expr, (Term)this);
      }

      return succs;
   }

   public boolean isConstant() {
      return this.expr.isConstant();
   }

   public Object constantValue() {
      if (!this.isConstant()) {
         return null;
      } else {
         Object v = this.expr.constantValue();
         if (v instanceof Boolean) {
            boolean vv = (Boolean)v;
            if (this.op == Unary.NOT) {
               return !vv;
            }
         }

         if (v instanceof Double) {
            double vv = (Double)v;
            if (this.op == Unary.POS) {
               return new Double(vv);
            }

            if (this.op == Unary.NEG) {
               return new Double(-vv);
            }
         }

         if (v instanceof Float) {
            float vv = (Float)v;
            if (this.op == Unary.POS) {
               return new Float(vv);
            }

            if (this.op == Unary.NEG) {
               return new Float(-vv);
            }
         }

         if (v instanceof Long) {
            long vv = (Long)v;
            if (this.op == Unary.BIT_NOT) {
               return new Long(~vv);
            }

            if (this.op == Unary.POS) {
               return new Long(vv);
            }

            if (this.op == Unary.NEG) {
               return new Long(-vv);
            }
         }

         if (v instanceof Integer) {
            int vv = (Integer)v;
            if (this.op == Unary.BIT_NOT) {
               return new Integer(~vv);
            }

            if (this.op == Unary.POS) {
               return new Integer(vv);
            }

            if (this.op == Unary.NEG) {
               return new Integer(-vv);
            }
         }

         if (v instanceof Character) {
            char vv = (Character)v;
            if (this.op == Unary.BIT_NOT) {
               return new Integer(~vv);
            }

            if (this.op == Unary.POS) {
               return new Integer(vv);
            }

            if (this.op == Unary.NEG) {
               return new Integer(-vv);
            }
         }

         if (v instanceof Short) {
            short vv = (Short)v;
            if (this.op == Unary.BIT_NOT) {
               return new Integer(~vv);
            }

            if (this.op == Unary.POS) {
               return new Integer(vv);
            }

            if (this.op == Unary.NEG) {
               return new Integer(-vv);
            }
         }

         if (v instanceof Byte) {
            byte vv = (Byte)v;
            if (this.op == Unary.BIT_NOT) {
               return new Integer(~vv);
            }

            if (this.op == Unary.POS) {
               return new Integer(vv);
            }

            if (this.op == Unary.NEG) {
               return new Integer(-vv);
            }
         }

         return null;
      }
   }
}
