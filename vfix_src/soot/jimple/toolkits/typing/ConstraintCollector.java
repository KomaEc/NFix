package soot.jimple.toolkits.typing;

import java.util.Iterator;
import java.util.List;
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
import soot.jimple.DynamicInvokeExpr;
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

class ConstraintCollector extends AbstractStmtSwitch {
   private TypeResolver resolver;
   private boolean uses;
   private JimpleBody stmtBody;

   public ConstraintCollector(TypeResolver resolver, boolean uses) {
      this.resolver = resolver;
      this.uses = uses;
   }

   public void collect(Stmt stmt, JimpleBody stmtBody) {
      this.stmtBody = stmtBody;
      stmt.apply(this);
   }

   private void handleInvokeExpr(InvokeExpr ie) {
      if (this.uses) {
         SootMethodRef method = ie.getMethodRef();

         for(int i = 0; i < ie.getArgCount(); ++i) {
            if (ie.getArg(i) instanceof Local) {
               Local local = (Local)ie.getArg(i);
               TypeVariable localType = this.resolver.typeVariable(local);
               localType.addParent(this.resolver.typeVariable(method.parameterType(i)));
            }
         }

         TypeVariable localType;
         Value base;
         Local local;
         if (ie instanceof InterfaceInvokeExpr) {
            InterfaceInvokeExpr invoke = (InterfaceInvokeExpr)ie;
            base = invoke.getBase();
            if (base instanceof Local) {
               local = (Local)base;
               localType = this.resolver.typeVariable(local);
               localType.addParent(this.resolver.typeVariable(method.declaringClass()));
            }
         } else if (ie instanceof SpecialInvokeExpr) {
            SpecialInvokeExpr invoke = (SpecialInvokeExpr)ie;
            base = invoke.getBase();
            if (base instanceof Local) {
               local = (Local)base;
               localType = this.resolver.typeVariable(local);
               localType.addParent(this.resolver.typeVariable(method.declaringClass()));
            }
         } else if (ie instanceof VirtualInvokeExpr) {
            VirtualInvokeExpr invoke = (VirtualInvokeExpr)ie;
            base = invoke.getBase();
            if (base instanceof Local) {
               local = (Local)base;
               localType = this.resolver.typeVariable(local);
               localType.addParent(this.resolver.typeVariable(method.declaringClass()));
            }
         } else if (!(ie instanceof StaticInvokeExpr)) {
            if (!(ie instanceof DynamicInvokeExpr)) {
               throw new RuntimeException("Unhandled invoke expression type: " + ie.getClass());
            }

            DynamicInvokeExpr invoke = (DynamicInvokeExpr)ie;
            SootMethodRef bootstrapMethod = invoke.getBootstrapMethodRef();

            for(int i = 0; i < invoke.getBootstrapArgCount(); ++i) {
               if (invoke.getArg(i) instanceof Local) {
                  Local local = (Local)invoke.getBootstrapArg(i);
                  TypeVariable localType = this.resolver.typeVariable(local);
                  localType.addParent(this.resolver.typeVariable(bootstrapMethod.parameterType(i)));
               }
            }
         }

      }
   }

   public void caseBreakpointStmt(BreakpointStmt stmt) {
   }

   public void caseInvokeStmt(InvokeStmt stmt) {
      this.handleInvokeExpr(stmt.getInvokeExpr());
   }

   public void caseAssignStmt(AssignStmt stmt) {
      Value l = stmt.getLeftOp();
      Value r = stmt.getRightOp();
      TypeVariable left = null;
      TypeVariable right = null;
      ArrayRef ref;
      Value lv;
      Value rv;
      TypeVariable var;
      InstanceFieldRef ref;
      StaticFieldRef ref;
      TypeVariable baseType;
      if (l instanceof ArrayRef) {
         ref = (ArrayRef)l;
         lv = ref.getBase();
         rv = ref.getIndex();
         var = this.resolver.typeVariable((Local)lv);
         var.makeElement();
         left = var.element();
         if (rv instanceof Local && this.uses) {
            this.resolver.typeVariable((Local)rv).addParent(this.resolver.typeVariable((Type)IntType.v()));
         }
      } else if (l instanceof Local) {
         left = this.resolver.typeVariable((Local)l);
      } else if (l instanceof InstanceFieldRef) {
         ref = (InstanceFieldRef)l;
         if (this.uses) {
            baseType = this.resolver.typeVariable((Local)ref.getBase());
            baseType.addParent(this.resolver.typeVariable(ref.getField().getDeclaringClass()));
            left = this.resolver.typeVariable(ref.getField().getType());
         }
      } else {
         if (!(l instanceof StaticFieldRef)) {
            throw new RuntimeException("Unhandled assignment left hand side type: " + l.getClass());
         }

         if (this.uses) {
            ref = (StaticFieldRef)l;
            left = this.resolver.typeVariable(ref.getField().getType());
         }
      }

      if (r instanceof ArrayRef) {
         ref = (ArrayRef)r;
         lv = ref.getBase();
         rv = ref.getIndex();
         var = this.resolver.typeVariable((Local)lv);
         var.makeElement();
         right = var.element();
         if (rv instanceof Local && this.uses) {
            this.resolver.typeVariable((Local)rv).addParent(this.resolver.typeVariable((Type)IntType.v()));
         }
      } else if (r instanceof DoubleConstant) {
         right = this.resolver.typeVariable((Type)DoubleType.v());
      } else if (r instanceof FloatConstant) {
         right = this.resolver.typeVariable((Type)FloatType.v());
      } else if (r instanceof IntConstant) {
         right = this.resolver.typeVariable((Type)IntType.v());
      } else if (r instanceof LongConstant) {
         right = this.resolver.typeVariable((Type)LongType.v());
      } else if (r instanceof NullConstant) {
         right = this.resolver.typeVariable((Type)NullType.v());
      } else if (r instanceof StringConstant) {
         right = this.resolver.typeVariable((Type)RefType.v("java.lang.String"));
      } else if (r instanceof ClassConstant) {
         right = this.resolver.typeVariable((Type)RefType.v("java.lang.Class"));
      } else if (r instanceof BinopExpr) {
         BinopExpr be = (BinopExpr)r;
         lv = be.getOp1();
         rv = be.getOp2();
         if (lv instanceof Local) {
            var = this.resolver.typeVariable((Local)lv);
         } else if (lv instanceof DoubleConstant) {
            var = this.resolver.typeVariable((Type)DoubleType.v());
         } else if (lv instanceof FloatConstant) {
            var = this.resolver.typeVariable((Type)FloatType.v());
         } else if (lv instanceof IntConstant) {
            var = this.resolver.typeVariable((Type)IntType.v());
         } else if (lv instanceof LongConstant) {
            var = this.resolver.typeVariable((Type)LongType.v());
         } else if (lv instanceof NullConstant) {
            var = this.resolver.typeVariable((Type)NullType.v());
         } else if (lv instanceof StringConstant) {
            var = this.resolver.typeVariable((Type)RefType.v("java.lang.String"));
         } else {
            if (!(lv instanceof ClassConstant)) {
               throw new RuntimeException("Unhandled binary expression left operand type: " + lv.getClass());
            }

            var = this.resolver.typeVariable((Type)RefType.v("java.lang.Class"));
         }

         TypeVariable rop;
         if (rv instanceof Local) {
            rop = this.resolver.typeVariable((Local)rv);
         } else if (rv instanceof DoubleConstant) {
            rop = this.resolver.typeVariable((Type)DoubleType.v());
         } else if (rv instanceof FloatConstant) {
            rop = this.resolver.typeVariable((Type)FloatType.v());
         } else if (rv instanceof IntConstant) {
            rop = this.resolver.typeVariable((Type)IntType.v());
         } else if (rv instanceof LongConstant) {
            rop = this.resolver.typeVariable((Type)LongType.v());
         } else if (rv instanceof NullConstant) {
            rop = this.resolver.typeVariable((Type)NullType.v());
         } else if (rv instanceof StringConstant) {
            rop = this.resolver.typeVariable((Type)RefType.v("java.lang.String"));
         } else {
            if (!(rv instanceof ClassConstant)) {
               throw new RuntimeException("Unhandled binary expression right operand type: " + rv.getClass());
            }

            rop = this.resolver.typeVariable((Type)RefType.v("java.lang.Class"));
         }

         TypeVariable common;
         if (!(be instanceof AddExpr) && !(be instanceof SubExpr) && !(be instanceof MulExpr) && !(be instanceof DivExpr) && !(be instanceof RemExpr) && !(be instanceof AndExpr) && !(be instanceof OrExpr) && !(be instanceof XorExpr)) {
            if (!(be instanceof ShlExpr) && !(be instanceof ShrExpr) && !(be instanceof UshrExpr)) {
               if (!(be instanceof CmpExpr) && !(be instanceof CmpgExpr) && !(be instanceof CmplExpr) && !(be instanceof EqExpr) && !(be instanceof GeExpr) && !(be instanceof GtExpr) && !(be instanceof LeExpr) && !(be instanceof LtExpr) && !(be instanceof NeExpr)) {
                  throw new RuntimeException("Unhandled binary expression type: " + be.getClass());
               }

               if (this.uses) {
                  common = this.resolver.typeVariable();
                  rop.addParent(common);
                  var.addParent(common);
               }

               right = this.resolver.typeVariable((Type)IntType.v());
            } else {
               if (this.uses) {
                  rop.addParent(this.resolver.typeVariable((Type)IntType.v()));
               }

               right = var;
            }
         } else {
            if (this.uses) {
               common = this.resolver.typeVariable();
               rop.addParent(common);
               var.addParent(common);
            }

            if (left != null) {
               rop.addParent(left);
               var.addParent(left);
            }
         }
      } else if (r instanceof CastExpr) {
         CastExpr ce = (CastExpr)r;
         right = this.resolver.typeVariable(ce.getCastType());
      } else if (r instanceof InstanceOfExpr) {
         right = this.resolver.typeVariable((Type)IntType.v());
      } else if (r instanceof InvokeExpr) {
         InvokeExpr ie = (InvokeExpr)r;
         this.handleInvokeExpr(ie);
         right = this.resolver.typeVariable(ie.getMethodRef().returnType());
      } else if (r instanceof NewArrayExpr) {
         NewArrayExpr nae = (NewArrayExpr)r;
         Type baseType = nae.getBaseType();
         if (baseType instanceof ArrayType) {
            right = this.resolver.typeVariable((Type)ArrayType.v(((ArrayType)baseType).baseType, ((ArrayType)baseType).numDimensions + 1));
         } else {
            right = this.resolver.typeVariable((Type)ArrayType.v(baseType, 1));
         }

         if (this.uses) {
            rv = nae.getSize();
            if (rv instanceof Local) {
               var = this.resolver.typeVariable((Local)rv);
               var.addParent(this.resolver.typeVariable((Type)IntType.v()));
            }
         }
      } else if (r instanceof NewExpr) {
         NewExpr na = (NewExpr)r;
         right = this.resolver.typeVariable((Type)na.getBaseType());
      } else if (r instanceof NewMultiArrayExpr) {
         NewMultiArrayExpr nmae = (NewMultiArrayExpr)r;
         right = this.resolver.typeVariable((Type)nmae.getBaseType());
         if (this.uses) {
            for(int i = 0; i < nmae.getSizeCount(); ++i) {
               rv = nmae.getSize(i);
               if (rv instanceof Local) {
                  var = this.resolver.typeVariable((Local)rv);
                  var.addParent(this.resolver.typeVariable((Type)IntType.v()));
               }
            }
         }
      } else if (r instanceof LengthExpr) {
         LengthExpr le = (LengthExpr)r;
         if (this.uses && le.getOp() instanceof Local) {
            this.resolver.typeVariable((Local)le.getOp()).makeElement();
         }

         right = this.resolver.typeVariable((Type)IntType.v());
      } else if (r instanceof NegExpr) {
         NegExpr ne = (NegExpr)r;
         if (ne.getOp() instanceof Local) {
            right = this.resolver.typeVariable((Local)ne.getOp());
         } else if (ne.getOp() instanceof DoubleConstant) {
            right = this.resolver.typeVariable((Type)DoubleType.v());
         } else if (ne.getOp() instanceof FloatConstant) {
            right = this.resolver.typeVariable((Type)FloatType.v());
         } else if (ne.getOp() instanceof IntConstant) {
            right = this.resolver.typeVariable((Type)IntType.v());
         } else {
            if (!(ne.getOp() instanceof LongConstant)) {
               throw new RuntimeException("Unhandled neg expression operand type: " + ne.getOp().getClass());
            }

            right = this.resolver.typeVariable((Type)LongType.v());
         }
      } else if (r instanceof Local) {
         right = this.resolver.typeVariable((Local)r);
      } else if (r instanceof InstanceFieldRef) {
         ref = (InstanceFieldRef)r;
         if (this.uses) {
            baseType = this.resolver.typeVariable((Local)ref.getBase());
            baseType.addParent(this.resolver.typeVariable(ref.getField().getDeclaringClass()));
         }

         right = this.resolver.typeVariable(ref.getField().getType());
      } else {
         if (!(r instanceof StaticFieldRef)) {
            throw new RuntimeException("Unhandled assignment right hand side type: " + r.getClass());
         }

         ref = (StaticFieldRef)r;
         right = this.resolver.typeVariable(ref.getField().getType());
      }

      if (left != null && right != null) {
         right.addParent(left);
      }

   }

   public void caseIdentityStmt(IdentityStmt stmt) {
      Value l = stmt.getLeftOp();
      Value r = stmt.getRightOp();
      if (l instanceof Local) {
         TypeVariable left = this.resolver.typeVariable((Local)l);
         if (!(r instanceof CaughtExceptionRef)) {
            TypeVariable right = this.resolver.typeVariable(r.getType());
            right.addParent(left);
         } else {
            List<RefType> exceptionTypes = TrapManager.getExceptionTypesOf(stmt, this.stmtBody);
            Iterator typeIt = exceptionTypes.iterator();

            while(typeIt.hasNext()) {
               Type t = (Type)typeIt.next();
               this.resolver.typeVariable(t).addParent(left);
            }

            if (this.uses) {
               left.addParent(this.resolver.typeVariable((Type)RefType.v("java.lang.Throwable")));
            }
         }
      }

   }

   public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
      if (this.uses && stmt.getOp() instanceof Local) {
         TypeVariable op = this.resolver.typeVariable((Local)stmt.getOp());
         op.addParent(this.resolver.typeVariable((Type)RefType.v("java.lang.Object")));
      }

   }

   public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
      if (this.uses && stmt.getOp() instanceof Local) {
         TypeVariable op = this.resolver.typeVariable((Local)stmt.getOp());
         op.addParent(this.resolver.typeVariable((Type)RefType.v("java.lang.Object")));
      }

   }

   public void caseGotoStmt(GotoStmt stmt) {
   }

   public void caseIfStmt(IfStmt stmt) {
      if (this.uses) {
         ConditionExpr cond = (ConditionExpr)stmt.getCondition();
         Value lv = cond.getOp1();
         Value rv = cond.getOp2();
         TypeVariable lop;
         if (lv instanceof Local) {
            lop = this.resolver.typeVariable((Local)lv);
         } else if (lv instanceof DoubleConstant) {
            lop = this.resolver.typeVariable((Type)DoubleType.v());
         } else if (lv instanceof FloatConstant) {
            lop = this.resolver.typeVariable((Type)FloatType.v());
         } else if (lv instanceof IntConstant) {
            lop = this.resolver.typeVariable((Type)IntType.v());
         } else if (lv instanceof LongConstant) {
            lop = this.resolver.typeVariable((Type)LongType.v());
         } else if (lv instanceof NullConstant) {
            lop = this.resolver.typeVariable((Type)NullType.v());
         } else if (lv instanceof StringConstant) {
            lop = this.resolver.typeVariable((Type)RefType.v("java.lang.String"));
         } else {
            if (!(lv instanceof ClassConstant)) {
               throw new RuntimeException("Unhandled binary expression left operand type: " + lv.getClass());
            }

            lop = this.resolver.typeVariable((Type)RefType.v("java.lang.Class"));
         }

         TypeVariable rop;
         if (rv instanceof Local) {
            rop = this.resolver.typeVariable((Local)rv);
         } else if (rv instanceof DoubleConstant) {
            rop = this.resolver.typeVariable((Type)DoubleType.v());
         } else if (rv instanceof FloatConstant) {
            rop = this.resolver.typeVariable((Type)FloatType.v());
         } else if (rv instanceof IntConstant) {
            rop = this.resolver.typeVariable((Type)IntType.v());
         } else if (rv instanceof LongConstant) {
            rop = this.resolver.typeVariable((Type)LongType.v());
         } else if (rv instanceof NullConstant) {
            rop = this.resolver.typeVariable((Type)NullType.v());
         } else if (rv instanceof StringConstant) {
            rop = this.resolver.typeVariable((Type)RefType.v("java.lang.String"));
         } else {
            if (!(rv instanceof ClassConstant)) {
               throw new RuntimeException("Unhandled binary expression right operand type: " + rv.getClass());
            }

            rop = this.resolver.typeVariable((Type)RefType.v("java.lang.Class"));
         }

         TypeVariable common = this.resolver.typeVariable();
         rop.addParent(common);
         lop.addParent(common);
      }

   }

   public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
      if (this.uses) {
         Value key = stmt.getKey();
         if (key instanceof Local) {
            this.resolver.typeVariable((Local)key).addParent(this.resolver.typeVariable((Type)IntType.v()));
         }
      }

   }

   public void caseNopStmt(NopStmt stmt) {
   }

   public void caseReturnStmt(ReturnStmt stmt) {
      if (this.uses && stmt.getOp() instanceof Local) {
         this.resolver.typeVariable((Local)stmt.getOp()).addParent(this.resolver.typeVariable(this.stmtBody.getMethod().getReturnType()));
      }

   }

   public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
   }

   public void caseTableSwitchStmt(TableSwitchStmt stmt) {
      if (this.uses) {
         Value key = stmt.getKey();
         if (key instanceof Local) {
            this.resolver.typeVariable((Local)key).addParent(this.resolver.typeVariable((Type)IntType.v()));
         }
      }

   }

   public void caseThrowStmt(ThrowStmt stmt) {
      if (this.uses && stmt.getOp() instanceof Local) {
         TypeVariable op = this.resolver.typeVariable((Local)stmt.getOp());
         op.addParent(this.resolver.typeVariable((Type)RefType.v("java.lang.Throwable")));
      }

   }

   public void defaultCase(Stmt stmt) {
      throw new RuntimeException("Unhandled statement type: " + stmt.getClass());
   }
}
