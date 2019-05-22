package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.List;
import polyglot.ast.Binary;
import polyglot.ast.BooleanLit;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.types.PrimitiveType;
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

public class Binary_c extends Expr_c implements Binary {
   protected Expr left;
   protected Binary.Operator op;
   protected Expr right;
   protected Precedence precedence;

   public Binary_c(Position pos, Expr left, Binary.Operator op, Expr right) {
      super(pos);
      this.left = left;
      this.op = op;
      this.right = right;
      this.precedence = op.precedence();
   }

   public Expr left() {
      return this.left;
   }

   public Binary left(Expr left) {
      Binary_c n = (Binary_c)this.copy();
      n.left = left;
      return n;
   }

   public Binary.Operator operator() {
      return this.op;
   }

   public Binary operator(Binary.Operator op) {
      Binary_c n = (Binary_c)this.copy();
      n.op = op;
      return n;
   }

   public Expr right() {
      return this.right;
   }

   public Binary right(Expr right) {
      Binary_c n = (Binary_c)this.copy();
      n.right = right;
      return n;
   }

   public Precedence precedence() {
      return this.precedence;
   }

   public Binary precedence(Precedence precedence) {
      Binary_c n = (Binary_c)this.copy();
      n.precedence = precedence;
      return n;
   }

   protected Binary_c reconstruct(Expr left, Expr right) {
      if (left == this.left && right == this.right) {
         return this;
      } else {
         Binary_c n = (Binary_c)this.copy();
         n.left = left;
         n.right = right;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Expr left = (Expr)this.visitChild(this.left, v);
      Expr right = (Expr)this.visitChild(this.right, v);
      return this.reconstruct(left, right);
   }

   public boolean isConstant() {
      return this.left.isConstant() && this.right.isConstant();
   }

   public Object constantValue() {
      Object lv = this.left.constantValue();
      Object rv = this.right.constantValue();
      if (!this.isConstant()) {
         return null;
      } else if (this.op == Binary.ADD && (lv instanceof String || rv instanceof String)) {
         if (lv == null) {
            lv = "null";
         }

         if (rv == null) {
            rv = "null";
         }

         return lv.toString() + rv.toString();
      } else if (this.op == Binary.EQ && lv instanceof String && rv instanceof String) {
         return ((String)lv).intern() == ((String)rv).intern();
      } else if (this.op == Binary.NE && lv instanceof String && rv instanceof String) {
         return ((String)lv).intern() != ((String)rv).intern();
      } else {
         if (lv instanceof Character) {
            lv = new Integer((Character)lv);
         }

         if (rv instanceof Character) {
            rv = new Integer((Character)rv);
         }

         try {
            if (lv instanceof Number && rv instanceof Number) {
               if (!(lv instanceof Double) && !(rv instanceof Double)) {
                  if (!(lv instanceof Float) && !(rv instanceof Float)) {
                     long l;
                     long r;
                     if (lv instanceof Long && rv instanceof Number) {
                        l = (Long)lv;
                        r = ((Number)rv).longValue();
                        if (this.op == Binary.SHL) {
                           return new Long(l << (int)r);
                        }

                        if (this.op == Binary.SHR) {
                           return new Long(l >> (int)r);
                        }

                        if (this.op == Binary.USHR) {
                           return new Long(l >>> (int)r);
                        }
                     }

                     if (!(lv instanceof Long) && !(rv instanceof Long)) {
                        int l = ((Number)lv).intValue();
                        int r = ((Number)rv).intValue();
                        if (this.op == Binary.ADD) {
                           return new Integer(l + r);
                        }

                        if (this.op == Binary.SUB) {
                           return new Integer(l - r);
                        }

                        if (this.op == Binary.MUL) {
                           return new Integer(l * r);
                        }

                        if (this.op == Binary.DIV) {
                           return new Integer(l / r);
                        }

                        if (this.op == Binary.MOD) {
                           return new Integer(l % r);
                        }

                        if (this.op == Binary.EQ) {
                           return l == r;
                        }

                        if (this.op == Binary.NE) {
                           return l != r;
                        }

                        if (this.op == Binary.LT) {
                           return l < r;
                        }

                        if (this.op == Binary.LE) {
                           return l <= r;
                        }

                        if (this.op == Binary.GE) {
                           return l >= r;
                        }

                        if (this.op == Binary.GT) {
                           return l > r;
                        }

                        if (this.op == Binary.BIT_AND) {
                           return new Integer(l & r);
                        }

                        if (this.op == Binary.BIT_OR) {
                           return new Integer(l | r);
                        }

                        if (this.op == Binary.BIT_XOR) {
                           return new Integer(l ^ r);
                        }

                        if (this.op == Binary.SHL) {
                           return new Integer(l << r);
                        }

                        if (this.op == Binary.SHR) {
                           return new Integer(l >> r);
                        }

                        if (this.op == Binary.USHR) {
                           return new Integer(l >>> r);
                        }

                        return null;
                     }

                     l = ((Number)lv).longValue();
                     r = ((Number)rv).longValue();
                     if (this.op == Binary.ADD) {
                        return new Long(l + r);
                     }

                     if (this.op == Binary.SUB) {
                        return new Long(l - r);
                     }

                     if (this.op == Binary.MUL) {
                        return new Long(l * r);
                     }

                     if (this.op == Binary.DIV) {
                        return new Long(l / r);
                     }

                     if (this.op == Binary.MOD) {
                        return new Long(l % r);
                     }

                     if (this.op == Binary.EQ) {
                        return l == r;
                     }

                     if (this.op == Binary.NE) {
                        return l != r;
                     }

                     if (this.op == Binary.LT) {
                        return l < r;
                     }

                     if (this.op == Binary.LE) {
                        return l <= r;
                     }

                     if (this.op == Binary.GE) {
                        return l >= r;
                     }

                     if (this.op == Binary.GT) {
                        return l > r;
                     }

                     if (this.op == Binary.BIT_AND) {
                        return new Long(l & r);
                     }

                     if (this.op == Binary.BIT_OR) {
                        return new Long(l | r);
                     }

                     if (this.op == Binary.BIT_XOR) {
                        return new Long(l ^ r);
                     }

                     return null;
                  }

                  float l = ((Number)lv).floatValue();
                  float r = ((Number)rv).floatValue();
                  if (this.op == Binary.ADD) {
                     return new Float(l + r);
                  }

                  if (this.op == Binary.SUB) {
                     return new Float(l - r);
                  }

                  if (this.op == Binary.MUL) {
                     return new Float(l * r);
                  }

                  if (this.op == Binary.DIV) {
                     return new Float(l / r);
                  }

                  if (this.op == Binary.MOD) {
                     return new Float(l % r);
                  }

                  if (this.op == Binary.EQ) {
                     return l == r;
                  }

                  if (this.op == Binary.NE) {
                     return l != r;
                  }

                  if (this.op == Binary.LT) {
                     return l < r;
                  }

                  if (this.op == Binary.LE) {
                     return l <= r;
                  }

                  if (this.op == Binary.GE) {
                     return l >= r;
                  }

                  if (this.op == Binary.GT) {
                     return l > r;
                  }

                  return null;
               }

               double l = ((Number)lv).doubleValue();
               double r = ((Number)rv).doubleValue();
               if (this.op == Binary.ADD) {
                  return new Double(l + r);
               }

               if (this.op == Binary.SUB) {
                  return new Double(l - r);
               }

               if (this.op == Binary.MUL) {
                  return new Double(l * r);
               }

               if (this.op == Binary.DIV) {
                  return new Double(l / r);
               }

               if (this.op == Binary.MOD) {
                  return new Double(l % r);
               }

               if (this.op == Binary.EQ) {
                  return l == r;
               }

               if (this.op == Binary.NE) {
                  return l != r;
               }

               if (this.op == Binary.LT) {
                  return l < r;
               }

               if (this.op == Binary.LE) {
                  return l <= r;
               }

               if (this.op == Binary.GE) {
                  return l >= r;
               }

               if (this.op == Binary.GT) {
                  return l > r;
               }

               return null;
            }
         } catch (ArithmeticException var7) {
            return null;
         }

         if (lv instanceof Boolean && rv instanceof Boolean) {
            boolean l = (Boolean)lv;
            boolean r = (Boolean)rv;
            if (this.op == Binary.EQ) {
               return l == r;
            }

            if (this.op == Binary.NE) {
               return l != r;
            }

            if (this.op == Binary.BIT_AND) {
               return l & r;
            }

            if (this.op == Binary.BIT_OR) {
               return l | r;
            }

            if (this.op == Binary.BIT_XOR) {
               return l ^ r;
            }

            if (this.op == Binary.COND_AND) {
               return l && r;
            }

            if (this.op == Binary.COND_OR) {
               return l || r;
            }
         }

         return null;
      }
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      Type l = this.left.type();
      Type r = this.right.type();
      TypeSystem ts = tc.typeSystem();
      if (this.op != Binary.GT && this.op != Binary.LT && this.op != Binary.GE && this.op != Binary.LE) {
         if (this.op != Binary.EQ && this.op != Binary.NE) {
            if (this.op != Binary.COND_OR && this.op != Binary.COND_AND) {
               if (this.op == Binary.ADD && (ts.equals(l, ts.String()) || ts.equals(r, ts.String()))) {
                  if (!ts.canCoerceToString(r, tc.context())) {
                     throw new SemanticException("Cannot coerce an expression of type " + r + " to a String.", this.right.position());
                  } else if (!ts.canCoerceToString(l, tc.context())) {
                     throw new SemanticException("Cannot coerce an expression of type " + l + " to a String.", this.left.position());
                  } else {
                     return this.precedence(Precedence.STRING_ADD).type(ts.String());
                  }
               } else if ((this.op == Binary.BIT_AND || this.op == Binary.BIT_OR || this.op == Binary.BIT_XOR) && l.isBoolean() && r.isBoolean()) {
                  return this.type(ts.Boolean());
               } else {
                  if (this.op == Binary.ADD) {
                     if (!l.isNumeric()) {
                        throw new SemanticException("The " + this.op + " operator must have numeric or String operands.", this.left.position());
                     }

                     if (!r.isNumeric()) {
                        throw new SemanticException("The " + this.op + " operator must have numeric or String operands.", this.right.position());
                     }
                  }

                  if (this.op == Binary.BIT_AND || this.op == Binary.BIT_OR || this.op == Binary.BIT_XOR) {
                     if (!ts.isImplicitCastValid(l, ts.Long())) {
                        throw new SemanticException("The " + this.op + " operator must have numeric or boolean operands.", this.left.position());
                     }

                     if (!ts.isImplicitCastValid(r, ts.Long())) {
                        throw new SemanticException("The " + this.op + " operator must have numeric or boolean operands.", this.right.position());
                     }
                  }

                  if (this.op == Binary.SUB || this.op == Binary.MUL || this.op == Binary.DIV || this.op == Binary.MOD) {
                     if (!l.isNumeric()) {
                        throw new SemanticException("The " + this.op + " operator must have numeric operands.", this.left.position());
                     }

                     if (!r.isNumeric()) {
                        throw new SemanticException("The " + this.op + " operator must have numeric operands.", this.right.position());
                     }
                  }

                  if (this.op == Binary.SHL || this.op == Binary.SHR || this.op == Binary.USHR) {
                     if (!ts.isImplicitCastValid(l, ts.Long())) {
                        throw new SemanticException("The " + this.op + " operator must have numeric operands.", this.left.position());
                     }

                     if (!ts.isImplicitCastValid(r, ts.Long())) {
                        throw new SemanticException("The " + this.op + " operator must have numeric operands.", this.right.position());
                     }
                  }

                  return this.op != Binary.SHL && this.op != Binary.SHR && this.op != Binary.USHR ? this.type(ts.promote(l, r)) : this.type(ts.promote(l));
               }
            } else if (!l.isBoolean()) {
               throw new SemanticException("The " + this.op + " operator must have boolean operands.", this.left.position());
            } else if (!r.isBoolean()) {
               throw new SemanticException("The " + this.op + " operator must have boolean operands.", this.right.position());
            } else {
               return this.type(ts.Boolean());
            }
         } else if (!ts.isCastValid(l, r) && !ts.isCastValid(r, l)) {
            throw new SemanticException("The " + this.op + " operator must have operands of similar type.", this.position());
         } else {
            return this.type(ts.Boolean());
         }
      } else if (!l.isNumeric()) {
         throw new SemanticException("The " + this.op + " operator must have numeric operands.", this.left.position());
      } else if (!r.isNumeric()) {
         throw new SemanticException("The " + this.op + " operator must have numeric operands.", this.right.position());
      } else {
         return this.type(ts.Boolean());
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      Expr var3;
      if (child == this.left) {
         var3 = this.right;
      } else {
         if (child != this.right) {
            return child.type();
         }

         var3 = this.left;
      }

      TypeSystem ts = av.typeSystem();

      try {
         if (this.op != Binary.EQ && this.op != Binary.NE) {
            if (this.op == Binary.ADD && ts.equals(this.type, ts.String())) {
               return ts.String();
            } else if (this.op != Binary.GT && this.op != Binary.LT && this.op != Binary.GE && this.op != Binary.LE) {
               if (this.op != Binary.COND_OR && this.op != Binary.COND_AND) {
                  if (this.op != Binary.BIT_AND && this.op != Binary.BIT_OR && this.op != Binary.BIT_XOR) {
                     PrimitiveType t;
                     if (this.op != Binary.ADD && this.op != Binary.SUB && this.op != Binary.MUL && this.op != Binary.DIV && this.op != Binary.MOD) {
                        if (this.op != Binary.SHL && this.op != Binary.SHR && this.op != Binary.USHR) {
                           return child.type();
                        } else if (child.type().isNumeric() && var3.type().isNumeric()) {
                           if (child == this.left) {
                              t = ts.promote(child.type());
                              return (Type)(ts.isImplicitCastValid(t, av.toType()) ? t : av.toType());
                           } else {
                              return ts.promote(child.type());
                           }
                        } else {
                           return child.type();
                        }
                     } else if (child.type().isNumeric() && var3.type().isNumeric()) {
                        t = ts.promote(child.type(), var3.type());
                        return (Type)(ts.isImplicitCastValid(t, av.toType()) ? t : av.toType());
                     } else {
                        return child.type();
                     }
                  } else if (var3.type().isBoolean()) {
                     return ts.Boolean();
                  } else {
                     return (Type)(child.type().isNumeric() && var3.type().isNumeric() ? ts.promote(child.type(), var3.type()) : child.type());
                  }
               } else {
                  return ts.Boolean();
               }
            } else {
               return (Type)(child.type().isNumeric() && var3.type().isNumeric() ? ts.promote(child.type(), var3.type()) : child.type());
            }
         } else if ((child.type().isReference() || child.type().isNull()) && (var3.type().isReference() || var3.type().isNull())) {
            return ts.leastCommonAncestor(child.type(), var3.type());
         } else if (child.type().isBoolean() && var3.type().isBoolean()) {
            return ts.Boolean();
         } else if (child.type().isNumeric() && var3.type().isNumeric()) {
            return ts.promote(child.type(), var3.type());
         } else {
            return child.type().isImplicitCastValid(var3.type()) ? var3.type() : child.type();
         }
      } catch (SemanticException var6) {
         return child.type();
      }
   }

   public boolean throwsArithmeticException() {
      return this.op == Binary.DIV || this.op == Binary.MOD;
   }

   public String toString() {
      return this.left + " " + this.op + " " + this.right;
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      this.printSubExpr(this.left, true, w, tr);
      w.write(" ");
      w.write(this.op.toString());
      w.allowBreak(this.type() != null && !this.type().isPrimitive() ? 0 : 2, " ");
      this.printSubExpr(this.right, false, w, tr);
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      if (this.type != null) {
         w.allowBreak(4, " ");
         w.begin(0);
         w.write("(type " + this.type + ")");
         w.end();
      }

      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(operator " + this.op + ")");
      w.end();
   }

   public Term entry() {
      return this.left.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      if (this.op != Binary.COND_AND && this.op != Binary.COND_OR) {
         if (this.left.type().isBoolean() && this.right.type().isBoolean()) {
            v.visitCFG(this.left, FlowGraph.EDGE_KEY_TRUE, this.right.entry(), FlowGraph.EDGE_KEY_FALSE, this.right.entry());
            v.visitCFG(this.right, FlowGraph.EDGE_KEY_TRUE, this, FlowGraph.EDGE_KEY_FALSE, this);
         } else {
            v.visitCFG(this.left, (Term)this.right.entry());
            v.visitCFG(this.right, (Term)this);
         }
      } else if (this.left instanceof BooleanLit) {
         BooleanLit b = (BooleanLit)this.left;
         if ((!b.value() || this.op != Binary.COND_OR) && (b.value() || this.op != Binary.COND_AND)) {
            v.visitCFG(this.left, (Term)this.right.entry());
            v.visitCFG(this.right, (Term)this);
         } else {
            v.visitCFG(this.left, (Term)this);
         }
      } else {
         if (this.op == Binary.COND_AND) {
            v.visitCFG(this.left, FlowGraph.EDGE_KEY_TRUE, this.right.entry(), FlowGraph.EDGE_KEY_FALSE, this);
         } else {
            v.visitCFG(this.left, FlowGraph.EDGE_KEY_FALSE, this.right.entry(), FlowGraph.EDGE_KEY_TRUE, this);
         }

         v.visitCFG(this.right, FlowGraph.EDGE_KEY_TRUE, this, FlowGraph.EDGE_KEY_FALSE, this);
      }

      return succs;
   }

   public List throwTypes(TypeSystem ts) {
      return this.throwsArithmeticException() ? Collections.singletonList(ts.ArithmeticException()) : Collections.EMPTY_LIST;
   }
}
