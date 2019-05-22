package soot.javaToJimple.jj.ast;

import polyglot.ast.Binary;
import polyglot.ast.Expr;
import polyglot.ext.jl.ast.Binary_c;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;

public class JjBinary_c extends Binary_c {
   public JjBinary_c(Position pos, Expr left, Binary.Operator op, Expr right) {
      super(pos, left, op, right);
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      Expr other;
      if (child == this.left) {
         other = this.right;
      } else {
         if (child != this.right) {
            return child.type();
         }

         other = this.left;
      }

      TypeSystem ts = av.typeSystem();
      if (this.op == EQ || this.op == NE) {
         if (other.type().isReference() || other.type().isNull()) {
            return ts.Object();
         }

         if (other.type().isBoolean()) {
            return ts.Boolean();
         }

         if (other.type().isNumeric()) {
            if (!other.type().isDouble() && !child.type().isDouble()) {
               if (!other.type().isFloat() && !child.type().isFloat()) {
                  if (!other.type().isLong() && !child.type().isLong()) {
                     return ts.Int();
                  }

                  return ts.Long();
               }

               return ts.Float();
            }

            return ts.Double();
         }
      }

      if (this.op == ADD && ts.equals(this.type, ts.String())) {
         return ts.String();
      } else {
         if (this.op == GT || this.op == LT || this.op == GE || this.op == LE) {
            if (other.type().isBoolean()) {
               return ts.Boolean();
            }

            if (other.type().isNumeric()) {
               if (!other.type().isDouble() && !child.type().isDouble()) {
                  if (!other.type().isFloat() && !child.type().isFloat()) {
                     if (!other.type().isLong() && !child.type().isLong()) {
                        return ts.Int();
                     }

                     return ts.Long();
                  }

                  return ts.Float();
               }

               return ts.Double();
            }
         }

         if (this.op != COND_OR && this.op != COND_AND) {
            if (this.op == BIT_AND || this.op == BIT_OR || this.op == BIT_XOR) {
               if (other.type().isBoolean()) {
                  return ts.Boolean();
               }

               if (other.type().isNumeric()) {
                  if (!other.type().isLong() && !child.type().isLong()) {
                     return ts.Int();
                  }

                  return ts.Long();
               }
            }

            if ((this.op == ADD || this.op == SUB || this.op == MUL || this.op == DIV || this.op == MOD) && other.type().isNumeric()) {
               if (!other.type().isDouble() && !child.type().isDouble()) {
                  if (!other.type().isFloat() && !child.type().isFloat()) {
                     return !other.type().isLong() && !child.type().isLong() ? ts.Int() : ts.Long();
                  } else {
                     return ts.Float();
                  }
               } else {
                  return ts.Double();
               }
            } else if (this.op != SHL && this.op != SHR && this.op != USHR) {
               return child.type();
            } else {
               return (Type)(child != this.right && child.type().isLong() ? child.type() : ts.Int());
            }
         } else {
            return ts.Boolean();
         }
      }
   }
}
