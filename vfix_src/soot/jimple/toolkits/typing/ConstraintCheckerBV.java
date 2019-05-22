package soot.jimple.toolkits.typing;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.RefType;
import soot.SootMethodRef;
import soot.TrapManager;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.BreakpointStmt;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.EqExpr;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GotoStmt;
import soot.jimple.GtExpr;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;

/** @deprecated */
@Deprecated
class ConstraintCheckerBV extends AbstractStmtSwitch {
   private static final Logger logger = LoggerFactory.getLogger(ConstraintCheckerBV.class);
   private final ClassHierarchy hierarchy;
   private final boolean fix;
   private JimpleBody stmtBody;

   public ConstraintCheckerBV(TypeResolverBV resolver, boolean fix) {
      this.fix = fix;
      this.hierarchy = resolver.hierarchy();
   }

   public void check(Stmt stmt, JimpleBody stmtBody) throws TypeException {
      try {
         this.stmtBody = stmtBody;
         stmt.apply(this);
      } catch (ConstraintCheckerBV.RuntimeTypeException var6) {
         StringWriter st = new StringWriter();
         PrintWriter pw = new PrintWriter(st);
         logger.error((String)var6.getMessage(), (Throwable)var6);
         pw.close();
         throw new TypeException(st.toString());
      }
   }

   static void error(String message) {
      throw new ConstraintCheckerBV.RuntimeTypeException(message);
   }

   private void handleInvokeExpr(InvokeExpr ie, Stmt invokestmt) {
      SootMethodRef method;
      Value base;
      Local local;
      int i;
      Local local;
      int i;
      if (ie instanceof InterfaceInvokeExpr) {
         InterfaceInvokeExpr invoke = (InterfaceInvokeExpr)ie;
         method = invoke.getMethodRef();
         base = invoke.getBase();
         if (base instanceof Local) {
            local = (Local)base;
            if (!this.hierarchy.typeNode(local.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(method.declaringClass().getType()))) {
               if (this.fix) {
                  invoke.setBase(this.insertCast(local, method.declaringClass().getType(), invokestmt));
               } else {
                  error("Type Error(7): local " + local + " is of incompatible type " + local.getType());
               }
            }
         }

         i = invoke.getArgCount();

         for(i = 0; i < i; ++i) {
            if (invoke.getArg(i) instanceof Local) {
               local = (Local)invoke.getArg(i);
               if (!this.hierarchy.typeNode(local.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(method.parameterType(i)))) {
                  if (this.fix) {
                     invoke.setArg(i, this.insertCast(local, method.parameterType(i), invokestmt));
                  } else {
                     error("Type Error(8)");
                  }
               }
            }
         }
      } else if (ie instanceof SpecialInvokeExpr) {
         SpecialInvokeExpr invoke = (SpecialInvokeExpr)ie;
         method = invoke.getMethodRef();
         base = invoke.getBase();
         if (base instanceof Local) {
            local = (Local)base;
            if (!this.hierarchy.typeNode(local.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(method.declaringClass().getType()))) {
               if (this.fix) {
                  invoke.setBase(this.insertCast(local, method.declaringClass().getType(), invokestmt));
               } else {
                  error("Type Error(9)");
               }
            }
         }

         i = invoke.getArgCount();

         for(i = 0; i < i; ++i) {
            if (invoke.getArg(i) instanceof Local) {
               local = (Local)invoke.getArg(i);
               if (!this.hierarchy.typeNode(local.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(method.parameterType(i)))) {
                  if (this.fix) {
                     invoke.setArg(i, this.insertCast(local, method.parameterType(i), invokestmt));
                  } else {
                     error("Type Error(10)");
                  }
               }
            }
         }
      } else if (ie instanceof VirtualInvokeExpr) {
         VirtualInvokeExpr invoke = (VirtualInvokeExpr)ie;
         method = invoke.getMethodRef();
         base = invoke.getBase();
         if (base instanceof Local) {
            local = (Local)base;
            if (!this.hierarchy.typeNode(local.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(method.declaringClass().getType()))) {
               if (this.fix) {
                  invoke.setBase(this.insertCast(local, method.declaringClass().getType(), invokestmt));
               } else {
                  error("Type Error(13)");
               }
            }
         }

         i = invoke.getArgCount();

         for(i = 0; i < i; ++i) {
            if (invoke.getArg(i) instanceof Local) {
               local = (Local)invoke.getArg(i);
               if (!this.hierarchy.typeNode(local.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(method.parameterType(i)))) {
                  if (this.fix) {
                     invoke.setArg(i, this.insertCast(local, method.parameterType(i), invokestmt));
                  } else {
                     error("Type Error(14)");
                  }
               }
            }
         }
      } else {
         if (!(ie instanceof StaticInvokeExpr)) {
            throw new RuntimeException("Unhandled invoke expression type: " + ie.getClass());
         }

         StaticInvokeExpr invoke = (StaticInvokeExpr)ie;
         method = invoke.getMethodRef();
         int count = invoke.getArgCount();

         for(i = 0; i < count; ++i) {
            if (invoke.getArg(i) instanceof Local) {
               Local local = (Local)invoke.getArg(i);
               if (!this.hierarchy.typeNode(local.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(method.parameterType(i)))) {
                  if (this.fix) {
                     invoke.setArg(i, this.insertCast(local, method.parameterType(i), invokestmt));
                  } else {
                     error("Type Error(15)");
                  }
               }
            }
         }
      }

   }

   public void caseBreakpointStmt(BreakpointStmt stmt) {
   }

   public void caseInvokeStmt(InvokeStmt stmt) {
      this.handleInvokeExpr(stmt.getInvokeExpr(), stmt);
   }

   public void caseAssignStmt(AssignStmt stmt) {
      Value l = stmt.getLeftOp();
      Value r = stmt.getRightOp();
      TypeNode left = null;
      ArrayRef ref;
      TypeNode baseType;
      Value rv;
      InstanceFieldRef ref;
      StaticFieldRef ref;
      if (l instanceof ArrayRef) {
         ref = (ArrayRef)l;
         baseType = this.hierarchy.typeNode(((Local)ref.getBase()).getType());
         if (!baseType.isArray()) {
            error("Type Error(16)");
         }

         left = baseType.element();
         rv = ref.getIndex();
         if (rv instanceof Local && !this.hierarchy.typeNode(((Local)rv).getType()).hasAncestorOrSelf(this.hierarchy.typeNode(IntType.v()))) {
            error("Type Error(17)");
         }
      } else if (l instanceof Local) {
         try {
            left = this.hierarchy.typeNode(((Local)l).getType());
         } catch (InternalTypingException var13) {
            logger.debug("untyped local: " + l);
            throw var13;
         }
      } else if (l instanceof InstanceFieldRef) {
         ref = (InstanceFieldRef)l;
         baseType = this.hierarchy.typeNode(((Local)ref.getBase()).getType());
         if (!baseType.hasAncestorOrSelf(this.hierarchy.typeNode(ref.getField().getDeclaringClass().getType()))) {
            if (this.fix) {
               ref.setBase(this.insertCast((Local)ref.getBase(), ref.getField().getDeclaringClass().getType(), stmt));
            } else {
               error("Type Error(18)");
            }
         }

         left = this.hierarchy.typeNode(ref.getField().getType());
      } else {
         if (!(l instanceof StaticFieldRef)) {
            throw new RuntimeException("Unhandled assignment left hand side type: " + l.getClass());
         }

         ref = (StaticFieldRef)l;
         left = this.hierarchy.typeNode(ref.getField().getType());
      }

      if (r instanceof ArrayRef) {
         ref = (ArrayRef)r;
         baseType = this.hierarchy.typeNode(((Local)ref.getBase()).getType());
         if (!baseType.isArray()) {
            error("Type Error(19): " + baseType + " is not an array type");
         }

         if (baseType == this.hierarchy.NULL) {
            return;
         }

         if (!left.hasDescendantOrSelf(baseType.element())) {
            if (this.fix) {
               Type lefttype = left.type();
               if (lefttype instanceof ArrayType) {
                  ArrayType atype = (ArrayType)lefttype;
                  ref.setBase(this.insertCast((Local)ref.getBase(), ArrayType.v(atype.baseType, atype.numDimensions + 1), stmt));
               } else {
                  ref.setBase(this.insertCast((Local)ref.getBase(), ArrayType.v(lefttype, 1), stmt));
               }
            } else {
               error("Type Error(20)");
            }
         }

         rv = ref.getIndex();
         if (rv instanceof Local && !this.hierarchy.typeNode(((Local)rv).getType()).hasAncestorOrSelf(this.hierarchy.typeNode(IntType.v()))) {
            error("Type Error(21)");
         }
      } else if (r instanceof DoubleConstant) {
         if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(DoubleType.v()))) {
            error("Type Error(22)");
         }
      } else if (r instanceof FloatConstant) {
         if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(FloatType.v()))) {
            error("Type Error(45)");
         }
      } else if (r instanceof IntConstant) {
         if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(IntType.v()))) {
            error("Type Error(23)");
         }
      } else if (r instanceof LongConstant) {
         if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(LongType.v()))) {
            error("Type Error(24)");
         }
      } else if (r instanceof NullConstant) {
         if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(NullType.v()))) {
            error("Type Error(25)");
         }
      } else if (r instanceof StringConstant) {
         if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(RefType.v("java.lang.String")))) {
            error("Type Error(26)");
         }
      } else if (r instanceof ClassConstant) {
         if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(RefType.v("java.lang.Class")))) {
            error("Type Error(27)");
         }
      } else {
         TypeNode var;
         TypeNode var;
         if (r instanceof BinopExpr) {
            BinopExpr be = (BinopExpr)r;
            Value lv = be.getOp1();
            rv = be.getOp2();
            if (lv instanceof Local) {
               var = this.hierarchy.typeNode(((Local)lv).getType());
            } else if (lv instanceof DoubleConstant) {
               var = this.hierarchy.typeNode(DoubleType.v());
            } else if (lv instanceof FloatConstant) {
               var = this.hierarchy.typeNode(FloatType.v());
            } else if (lv instanceof IntConstant) {
               var = this.hierarchy.typeNode(IntType.v());
            } else if (lv instanceof LongConstant) {
               var = this.hierarchy.typeNode(LongType.v());
            } else if (lv instanceof NullConstant) {
               var = this.hierarchy.typeNode(NullType.v());
            } else if (lv instanceof StringConstant) {
               var = this.hierarchy.typeNode(RefType.v("java.lang.String"));
            } else {
               if (!(lv instanceof ClassConstant)) {
                  throw new RuntimeException("Unhandled binary expression left operand type: " + lv.getClass());
               }

               var = this.hierarchy.typeNode(RefType.v("java.lang.Class"));
            }

            if (rv instanceof Local) {
               var = this.hierarchy.typeNode(((Local)rv).getType());
            } else if (rv instanceof DoubleConstant) {
               var = this.hierarchy.typeNode(DoubleType.v());
            } else if (rv instanceof FloatConstant) {
               var = this.hierarchy.typeNode(FloatType.v());
            } else if (rv instanceof IntConstant) {
               var = this.hierarchy.typeNode(IntType.v());
            } else if (rv instanceof LongConstant) {
               var = this.hierarchy.typeNode(LongType.v());
            } else if (rv instanceof NullConstant) {
               var = this.hierarchy.typeNode(NullType.v());
            } else if (rv instanceof StringConstant) {
               var = this.hierarchy.typeNode(RefType.v("java.lang.String"));
            } else {
               if (!(rv instanceof ClassConstant)) {
                  throw new RuntimeException("Unhandled binary expression right operand type: " + rv.getClass());
               }

               var = this.hierarchy.typeNode(RefType.v("java.lang.Class"));
            }

            if (!(be instanceof AddExpr) && !(be instanceof SubExpr) && !(be instanceof MulExpr) && !(be instanceof DivExpr) && !(be instanceof RemExpr) && !(be instanceof AndExpr) && !(be instanceof OrExpr) && !(be instanceof XorExpr)) {
               if (!(be instanceof ShlExpr) && !(be instanceof ShrExpr) && !(be instanceof UshrExpr)) {
                  if (!(be instanceof CmpExpr) && !(be instanceof CmpgExpr) && !(be instanceof CmplExpr) && !(be instanceof EqExpr) && !(be instanceof GeExpr) && !(be instanceof GtExpr) && !(be instanceof LeExpr) && !(be instanceof LtExpr) && !(be instanceof NeExpr)) {
                     throw new RuntimeException("Unhandled binary expression type: " + be.getClass());
                  }

                  try {
                     var.lca(var);
                  } catch (TypeException var12) {
                     error(var12.getMessage());
                  }

                  if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(IntType.v()))) {
                     error("Type Error(29)");
                  }
               } else if (!left.hasDescendantOrSelf(var) || !this.hierarchy.typeNode(IntType.v()).hasAncestorOrSelf(var)) {
                  error("Type Error(28)");
               }
            } else if (!left.hasDescendantOrSelf(var) || !left.hasDescendantOrSelf(var)) {
               error("Type Error(27)");
            }
         } else {
            TypeNode right;
            if (r instanceof CastExpr) {
               CastExpr ce = (CastExpr)r;
               baseType = this.hierarchy.typeNode(ce.getCastType());
               if (ce.getOp() instanceof Local) {
                  right = this.hierarchy.typeNode(((Local)ce.getOp()).getType());

                  try {
                     if (baseType.isClassOrInterface() || right.isClassOrInterface()) {
                        baseType.lca(right);
                     }
                  } catch (TypeException var14) {
                     logger.debug("" + r + "[" + right + "<->" + baseType + "]");
                     error(var14.getMessage());
                  }
               }

               if (!left.hasDescendantOrSelf(baseType)) {
                  error("Type Error(30)");
               }
            } else if (r instanceof InstanceOfExpr) {
               InstanceOfExpr ioe = (InstanceOfExpr)r;
               baseType = this.hierarchy.typeNode(ioe.getCheckType());
               right = this.hierarchy.typeNode(ioe.getOp().getType());

               try {
                  right.lca(baseType);
               } catch (TypeException var11) {
                  logger.debug("" + r + "[" + right + "<->" + baseType + "]");
                  error(var11.getMessage());
               }

               if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(IntType.v()))) {
                  error("Type Error(31)");
               }
            } else if (r instanceof InvokeExpr) {
               InvokeExpr ie = (InvokeExpr)r;
               this.handleInvokeExpr(ie, stmt);
               if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(ie.getMethodRef().returnType()))) {
                  error("Type Error(32)");
               }
            } else if (r instanceof NewArrayExpr) {
               NewArrayExpr nae = (NewArrayExpr)r;
               Type baseType = nae.getBaseType();
               if (baseType instanceof ArrayType) {
                  right = this.hierarchy.typeNode(ArrayType.v(((ArrayType)baseType).baseType, ((ArrayType)baseType).numDimensions + 1));
               } else {
                  right = this.hierarchy.typeNode(ArrayType.v(baseType, 1));
               }

               if (!left.hasDescendantOrSelf(right)) {
                  error("Type Error(33)");
               }

               Value size = nae.getSize();
               if (size instanceof Local) {
                  var = this.hierarchy.typeNode(((Local)size).getType());
                  if (!var.hasAncestorOrSelf(this.hierarchy.typeNode(IntType.v()))) {
                     error("Type Error(34)");
                  }
               }
            } else if (r instanceof NewExpr) {
               NewExpr ne = (NewExpr)r;
               if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(ne.getBaseType()))) {
                  error("Type Error(35)");
               }
            } else if (r instanceof NewMultiArrayExpr) {
               NewMultiArrayExpr nmae = (NewMultiArrayExpr)r;
               if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(nmae.getBaseType()))) {
                  error("Type Error(36)");
               }

               for(int i = 0; i < nmae.getSizeCount(); ++i) {
                  rv = nmae.getSize(i);
                  if (rv instanceof Local) {
                     var = this.hierarchy.typeNode(((Local)rv).getType());
                     if (!var.hasAncestorOrSelf(this.hierarchy.typeNode(IntType.v()))) {
                        error("Type Error(37)");
                     }
                  }
               }
            } else if (r instanceof LengthExpr) {
               LengthExpr le = (LengthExpr)r;
               if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(IntType.v()))) {
                  error("Type Error(38)");
               }

               if (le.getOp() instanceof Local && !this.hierarchy.typeNode(((Local)le.getOp()).getType()).isArray()) {
                  error("Type Error(39)");
               }
            } else if (r instanceof NegExpr) {
               NegExpr ne = (NegExpr)r;
               if (ne.getOp() instanceof Local) {
                  baseType = this.hierarchy.typeNode(((Local)ne.getOp()).getType());
               } else if (ne.getOp() instanceof DoubleConstant) {
                  baseType = this.hierarchy.typeNode(DoubleType.v());
               } else if (ne.getOp() instanceof FloatConstant) {
                  baseType = this.hierarchy.typeNode(FloatType.v());
               } else if (ne.getOp() instanceof IntConstant) {
                  baseType = this.hierarchy.typeNode(IntType.v());
               } else {
                  if (!(ne.getOp() instanceof LongConstant)) {
                     throw new RuntimeException("Unhandled neg expression operand type: " + ne.getOp().getClass());
                  }

                  baseType = this.hierarchy.typeNode(LongType.v());
               }

               if (!left.hasDescendantOrSelf(baseType)) {
                  error("Type Error(40)");
               }
            } else if (r instanceof Local) {
               if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(((Local)r).getType()))) {
                  if (this.fix) {
                     stmt.setRightOp(this.insertCast((Local)r, left.type(), stmt));
                  } else {
                     error("Type Error(41)");
                  }
               }
            } else if (r instanceof InstanceFieldRef) {
               ref = (InstanceFieldRef)r;
               baseType = this.hierarchy.typeNode(((Local)ref.getBase()).getType());
               if (!baseType.hasAncestorOrSelf(this.hierarchy.typeNode(ref.getField().getDeclaringClass().getType()))) {
                  if (this.fix) {
                     ref.setBase(this.insertCast((Local)ref.getBase(), ref.getField().getDeclaringClass().getType(), stmt));
                  } else {
                     error("Type Error(42)");
                  }
               }

               if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(ref.getField().getType()))) {
                  error("Type Error(43)");
               }
            } else {
               if (!(r instanceof StaticFieldRef)) {
                  throw new RuntimeException("Unhandled assignment right hand side type: " + r.getClass());
               }

               ref = (StaticFieldRef)r;
               if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(ref.getField().getType()))) {
                  error("Type Error(44)");
               }
            }
         }
      }

   }

   public void caseIdentityStmt(IdentityStmt stmt) {
      TypeNode left = this.hierarchy.typeNode(((Local)stmt.getLeftOp()).getType());
      Value r = stmt.getRightOp();
      if (!(r instanceof CaughtExceptionRef)) {
         TypeNode right = this.hierarchy.typeNode(r.getType());
         if (!left.hasDescendantOrSelf(right)) {
            error("Type Error(46) [" + left + " <- " + right + "]");
         }
      } else {
         List<RefType> exceptionTypes = TrapManager.getExceptionTypesOf(stmt, this.stmtBody);
         Iterator typeIt = exceptionTypes.iterator();

         while(typeIt.hasNext()) {
            Type t = (Type)typeIt.next();
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(t))) {
               error("Type Error(47)");
            }
         }

         if (!left.hasAncestorOrSelf(this.hierarchy.typeNode(RefType.v("java.lang.Throwable")))) {
            error("Type Error(48)");
         }
      }

   }

   public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
      if (stmt.getOp() instanceof Local) {
         TypeNode op = this.hierarchy.typeNode(((Local)stmt.getOp()).getType());
         if (!op.hasAncestorOrSelf(this.hierarchy.typeNode(RefType.v("java.lang.Object")))) {
            error("Type Error(49)");
         }
      }

   }

   public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
      if (stmt.getOp() instanceof Local) {
         TypeNode op = this.hierarchy.typeNode(((Local)stmt.getOp()).getType());
         if (!op.hasAncestorOrSelf(this.hierarchy.typeNode(RefType.v("java.lang.Object")))) {
            error("Type Error(49)");
         }
      }

   }

   public void caseGotoStmt(GotoStmt stmt) {
   }

   public void caseIfStmt(IfStmt stmt) {
      ConditionExpr cond = (ConditionExpr)stmt.getCondition();
      Value lv = cond.getOp1();
      Value rv = cond.getOp2();
      TypeNode lop;
      if (lv instanceof Local) {
         lop = this.hierarchy.typeNode(((Local)lv).getType());
      } else if (lv instanceof DoubleConstant) {
         lop = this.hierarchy.typeNode(DoubleType.v());
      } else if (lv instanceof FloatConstant) {
         lop = this.hierarchy.typeNode(FloatType.v());
      } else if (lv instanceof IntConstant) {
         lop = this.hierarchy.typeNode(IntType.v());
      } else if (lv instanceof LongConstant) {
         lop = this.hierarchy.typeNode(LongType.v());
      } else if (lv instanceof NullConstant) {
         lop = this.hierarchy.typeNode(NullType.v());
      } else if (lv instanceof StringConstant) {
         lop = this.hierarchy.typeNode(RefType.v("java.lang.String"));
      } else {
         if (!(lv instanceof ClassConstant)) {
            throw new RuntimeException("Unhandled binary expression left operand type: " + lv.getClass());
         }

         lop = this.hierarchy.typeNode(RefType.v("java.lang.Class"));
      }

      TypeNode rop;
      if (rv instanceof Local) {
         rop = this.hierarchy.typeNode(((Local)rv).getType());
      } else if (rv instanceof DoubleConstant) {
         rop = this.hierarchy.typeNode(DoubleType.v());
      } else if (rv instanceof FloatConstant) {
         rop = this.hierarchy.typeNode(FloatType.v());
      } else if (rv instanceof IntConstant) {
         rop = this.hierarchy.typeNode(IntType.v());
      } else if (rv instanceof LongConstant) {
         rop = this.hierarchy.typeNode(LongType.v());
      } else if (rv instanceof NullConstant) {
         rop = this.hierarchy.typeNode(NullType.v());
      } else if (rv instanceof StringConstant) {
         rop = this.hierarchy.typeNode(RefType.v("java.lang.String"));
      } else {
         if (!(rv instanceof ClassConstant)) {
            throw new RuntimeException("Unhandled binary expression right operand type: " + rv.getClass());
         }

         rop = this.hierarchy.typeNode(RefType.v("java.lang.Class"));
      }

      try {
         lop.lca(rop);
      } catch (TypeException var9) {
         error(var9.getMessage());
      }

   }

   public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
      Value key = stmt.getKey();
      if (key instanceof Local && !this.hierarchy.typeNode(((Local)key).getType()).hasAncestorOrSelf(this.hierarchy.typeNode(IntType.v()))) {
         error("Type Error(50)");
      }

   }

   public void caseNopStmt(NopStmt stmt) {
   }

   public void caseReturnStmt(ReturnStmt stmt) {
      if (stmt.getOp() instanceof Local && !this.hierarchy.typeNode(((Local)stmt.getOp()).getType()).hasAncestorOrSelf(this.hierarchy.typeNode(this.stmtBody.getMethod().getReturnType()))) {
         if (this.fix) {
            stmt.setOp(this.insertCast((Local)stmt.getOp(), this.stmtBody.getMethod().getReturnType(), stmt));
         } else {
            error("Type Error(51)");
         }
      }

   }

   public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
   }

   public void caseTableSwitchStmt(TableSwitchStmt stmt) {
      Value key = stmt.getKey();
      if (key instanceof Local && !this.hierarchy.typeNode(((Local)key).getType()).hasAncestorOrSelf(this.hierarchy.typeNode(IntType.v()))) {
         error("Type Error(52)");
      }

   }

   public void caseThrowStmt(ThrowStmt stmt) {
      if (stmt.getOp() instanceof Local) {
         TypeNode op = this.hierarchy.typeNode(((Local)stmt.getOp()).getType());
         if (!op.hasAncestorOrSelf(this.hierarchy.typeNode(RefType.v("java.lang.Throwable")))) {
            if (this.fix) {
               stmt.setOp(this.insertCast((Local)stmt.getOp(), RefType.v("java.lang.Throwable"), stmt));
            } else {
               error("Type Error(53)");
            }
         }
      }

   }

   public void defaultCase(Stmt stmt) {
      throw new RuntimeException("Unhandled statement type: " + stmt.getClass());
   }

   private Local insertCast(Local oldlocal, Type type, Stmt stmt) {
      Local newlocal = Jimple.v().newLocal("tmp", type);
      this.stmtBody.getLocals().add(newlocal);
      Unit u = Util.findFirstNonIdentityUnit(this.stmtBody, stmt);
      this.stmtBody.getUnits().insertBefore((Unit)Jimple.v().newAssignStmt(newlocal, Jimple.v().newCastExpr(oldlocal, type)), (Unit)u);
      return newlocal;
   }

   private static class RuntimeTypeException extends RuntimeException {
      RuntimeTypeException(String message) {
         super(message);
      }
   }
}
