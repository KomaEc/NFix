package soot.jimple.toolkits.typing.fast;

import java.util.Iterator;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.IntType;
import soot.IntegerType;
import soot.Local;
import soot.NullType;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.SootMethodRef;
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
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.Constant;
import soot.jimple.DivExpr;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.EqExpr;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FieldRef;
import soot.jimple.GeExpr;
import soot.jimple.GotoStmt;
import soot.jimple.GtExpr;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.JimpleBody;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UshrExpr;
import soot.jimple.XorExpr;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;

public class UseChecker extends AbstractStmtSwitch {
   private JimpleBody jb;
   private Typing tg;
   private IUseVisitor uv;
   private LocalDefs defs = null;
   private LocalUses uses = null;

   public UseChecker(JimpleBody jb) {
      this.jb = jb;
   }

   public void check(Typing tg, IUseVisitor uv) {
      this.tg = tg;
      this.uv = uv;
      if (this.tg == null) {
         throw new RuntimeException("null typing passed to useChecker");
      } else {
         Iterator i = this.jb.getUnits().snapshotIterator();

         while(i.hasNext()) {
            if (uv.finish()) {
               return;
            }

            ((Unit)i.next()).apply(this);
         }

      }
   }

   private void handleInvokeExpr(InvokeExpr ie, Stmt stmt) {
      SootMethodRef m = ie.getMethodRef();
      if (ie instanceof InstanceInvokeExpr) {
         InstanceInvokeExpr iie = (InstanceInvokeExpr)ie;
         iie.setBase(this.uv.visit(iie.getBase(), m.declaringClass().getType(), stmt));
      }

      for(int i = 0; i < ie.getArgCount(); ++i) {
         ie.setArg(i, this.uv.visit(ie.getArg(i), m.parameterType(i), stmt));
      }

   }

   private void handleBinopExpr(BinopExpr be, Stmt stmt, Type tlhs) {
      Value opl = be.getOp1();
      Value opr = be.getOp2();
      Type tl = AugEvalFunction.eval_(this.tg, opl, stmt, this.jb);
      Type tr = AugEvalFunction.eval_(this.tg, opr, stmt, this.jb);
      if (!(be instanceof AddExpr) && !(be instanceof SubExpr) && !(be instanceof MulExpr) && !(be instanceof DivExpr) && !(be instanceof RemExpr) && !(be instanceof GeExpr) && !(be instanceof GtExpr) && !(be instanceof LeExpr) && !(be instanceof LtExpr) && !(be instanceof ShlExpr) && !(be instanceof ShrExpr) && !(be instanceof UshrExpr)) {
         if (!(be instanceof CmpExpr) && !(be instanceof CmpgExpr) && !(be instanceof CmplExpr)) {
            if (!(be instanceof AndExpr) && !(be instanceof OrExpr) && !(be instanceof XorExpr)) {
               if ((be instanceof EqExpr || be instanceof NeExpr) && (!(tl instanceof BooleanType) || !(tr instanceof BooleanType)) && !(tl instanceof Integer1Type) && !(tr instanceof Integer1Type) && tl instanceof IntegerType) {
                  be.setOp1(this.uv.visit(opl, IntType.v(), stmt));
                  be.setOp2(this.uv.visit(opr, IntType.v(), stmt));
               }
            } else {
               be.setOp1(this.uv.visit(opl, tlhs, stmt));
               be.setOp2(this.uv.visit(opr, tlhs, stmt));
            }
         }
      } else if (tlhs instanceof IntegerType) {
         be.setOp1(this.uv.visit(opl, IntType.v(), stmt));
         be.setOp2(this.uv.visit(opr, IntType.v(), stmt));
      }

   }

   private void handleArrayRef(ArrayRef ar, Stmt stmt) {
      ar.setIndex(this.uv.visit(ar.getIndex(), IntType.v(), stmt));
   }

   private void handleInstanceFieldRef(InstanceFieldRef ifr, Stmt stmt) {
      ifr.setBase(this.uv.visit(ifr.getBase(), ifr.getFieldRef().declaringClass().getType(), stmt));
   }

   public void caseBreakpointStmt(BreakpointStmt stmt) {
   }

   public void caseInvokeStmt(InvokeStmt stmt) {
      this.handleInvokeExpr(stmt.getInvokeExpr(), stmt);
   }

   public void caseAssignStmt(AssignStmt stmt) {
      Value lhs = stmt.getLeftOp();
      Value rhs = stmt.getRightOp();
      Type tlhs = null;
      ArrayRef aref;
      Local base;
      ArrayType at;
      Type et;
      Type bt;
      if (lhs instanceof Local) {
         tlhs = this.tg.get((Local)lhs);
      } else if (lhs instanceof ArrayRef) {
         aref = (ArrayRef)lhs;
         base = (Local)aref.getBase();
         at = null;
         et = this.tg.get(base);
         if (et instanceof ArrayType) {
            at = (ArrayType)et;
         } else {
            if (et == Scene.v().getObjectType() && rhs instanceof Local) {
               bt = this.tg.get((Local)rhs);
               if (bt instanceof PrimType) {
                  if (this.defs == null) {
                     this.defs = LocalDefs.Factory.newLocalDefs((Body)this.jb);
                     this.uses = LocalUses.Factory.newLocalUses((Body)this.jb, this.defs);
                  }

                  Iterator var10 = this.defs.getDefsOfAt(base, stmt).iterator();

                  while(var10.hasNext()) {
                     Unit defU = (Unit)var10.next();
                     if (defU instanceof AssignStmt) {
                        AssignStmt defUas = (AssignStmt)defU;
                        if (defUas.getRightOp() instanceof NewArrayExpr) {
                           at = (ArrayType)defUas.getRightOp().getType();
                           break;
                        }
                     }
                  }
               }
            }

            if (at == null) {
               at = et.makeArrayType();
            }
         }

         tlhs = at.getElementType();
         this.handleArrayRef(aref, stmt);
         aref.setBase((Local)this.uv.visit(aref.getBase(), at, stmt));
         stmt.setRightOp(this.uv.visit(rhs, tlhs, stmt));
         stmt.setLeftOp(this.uv.visit(lhs, tlhs, stmt));
      } else if (lhs instanceof FieldRef) {
         tlhs = ((FieldRef)lhs).getFieldRef().type();
         if (lhs instanceof InstanceFieldRef) {
            this.handleInstanceFieldRef((InstanceFieldRef)lhs, stmt);
         }
      }

      lhs = stmt.getLeftOp();
      rhs = stmt.getRightOp();
      if (rhs instanceof Local) {
         stmt.setRightOp(this.uv.visit(rhs, tlhs, stmt));
      } else if (rhs instanceof ArrayRef) {
         aref = (ArrayRef)rhs;
         base = (Local)aref.getBase();
         at = null;
         et = null;
         if (this.tg.get(base) instanceof ArrayType) {
            at = (ArrayType)this.tg.get(base);
         } else {
            bt = this.tg.get(base);
            if (bt instanceof RefType || bt instanceof NullType) {
               RefType rt = bt instanceof NullType ? null : (RefType)bt;
               if (rt == null || rt.getSootClass().getName().equals("java.lang.Object") || rt.getSootClass().getName().equals("java.io.Serializable") || rt.getSootClass().getName().equals("java.lang.Cloneable")) {
                  if (this.defs == null) {
                     this.defs = LocalDefs.Factory.newLocalDefs((Body)this.jb);
                     this.uses = LocalUses.Factory.newLocalUses((Body)this.jb, this.defs);
                  }

                  Iterator var23 = this.uses.getUsesOf(stmt).iterator();

                  label152:
                  while(true) {
                     while(true) {
                        if (!var23.hasNext()) {
                           break label152;
                        }

                        UnitValueBoxPair usePair = (UnitValueBoxPair)var23.next();
                        Stmt useStmt = (Stmt)usePair.getUnit();
                        if (useStmt.containsInvokeExpr()) {
                           for(int i = 0; i < useStmt.getInvokeExpr().getArgCount(); ++i) {
                              if (useStmt.getInvokeExpr().getArg(i) == usePair.getValueBox().getValue()) {
                                 et = useStmt.getInvokeExpr().getMethod().getParameterType(i);
                                 at = et.makeArrayType();
                                 break label152;
                              }
                           }
                        } else {
                           Value other;
                           Type newEt;
                           if (useStmt instanceof IfStmt) {
                              IfStmt ifStmt = (IfStmt)useStmt;
                              if (ifStmt.getCondition() instanceof EqExpr) {
                                 EqExpr expr = (EqExpr)ifStmt.getCondition();
                                 if (expr.getOp1() == usePair.getValueBox().getValue()) {
                                    other = expr.getOp2();
                                 } else {
                                    other = expr.getOp1();
                                 }

                                 newEt = this.getTargetType(other);
                                 if (newEt != null) {
                                    et = newEt;
                                 }
                              }
                           } else if (useStmt instanceof AssignStmt) {
                              AssignStmt useAssignStmt = (AssignStmt)useStmt;
                              if (useAssignStmt.getRightOp() instanceof BinopExpr) {
                                 BinopExpr binOp = (BinopExpr)useAssignStmt.getRightOp();
                                 if (binOp.getOp1() == usePair.getValueBox().getValue()) {
                                    other = binOp.getOp2();
                                 } else {
                                    other = binOp.getOp1();
                                 }

                                 newEt = this.getTargetType(other);
                                 if (newEt != null) {
                                    et = newEt;
                                 }
                              }
                           } else if (useStmt instanceof ReturnStmt) {
                              et = this.jb.getMethod().getReturnType();
                           }
                        }
                     }
                  }
               }
            }

            if (at == null) {
               at = et.makeArrayType();
            }
         }

         bt = at.getElementType();
         this.handleArrayRef(aref, stmt);
         aref.setBase((Local)this.uv.visit(aref.getBase(), at, stmt));
         stmt.setRightOp(this.uv.visit(rhs, bt, stmt));
      } else if (rhs instanceof InstanceFieldRef) {
         this.handleInstanceFieldRef((InstanceFieldRef)rhs, stmt);
         stmt.setRightOp(this.uv.visit(rhs, tlhs, stmt));
      } else if (rhs instanceof BinopExpr) {
         this.handleBinopExpr((BinopExpr)rhs, stmt, tlhs);
      } else if (rhs instanceof InvokeExpr) {
         this.handleInvokeExpr((InvokeExpr)rhs, stmt);
         stmt.setRightOp(this.uv.visit(rhs, tlhs, stmt));
      } else if (rhs instanceof CastExpr) {
         stmt.setRightOp(this.uv.visit(rhs, tlhs, stmt));
      } else if (rhs instanceof InstanceOfExpr) {
         InstanceOfExpr ioe = (InstanceOfExpr)rhs;
         ioe.setOp(this.uv.visit(ioe.getOp(), RefType.v("java.lang.Object"), stmt));
         stmt.setRightOp(this.uv.visit(rhs, tlhs, stmt));
      } else if (rhs instanceof NewArrayExpr) {
         NewArrayExpr nae = (NewArrayExpr)rhs;
         nae.setSize(this.uv.visit(nae.getSize(), IntType.v(), stmt));
         stmt.setRightOp(this.uv.visit(rhs, tlhs, stmt));
      } else if (rhs instanceof NewMultiArrayExpr) {
         NewMultiArrayExpr nmae = (NewMultiArrayExpr)rhs;

         for(int i = 0; i < nmae.getSizeCount(); ++i) {
            nmae.setSize(i, this.uv.visit(nmae.getSize(i), IntType.v(), stmt));
         }

         stmt.setRightOp(this.uv.visit(rhs, tlhs, stmt));
      } else if (rhs instanceof LengthExpr) {
         stmt.setRightOp(this.uv.visit(rhs, tlhs, stmt));
      } else if (rhs instanceof NegExpr) {
         ((NegExpr)rhs).setOp(this.uv.visit(((NegExpr)rhs).getOp(), tlhs, stmt));
      } else if (rhs instanceof Constant && !(rhs instanceof NullConstant)) {
         stmt.setRightOp(this.uv.visit(rhs, tlhs, stmt));
      }

   }

   private Type getTargetType(Value other) {
      if (other instanceof Constant) {
         if (other.getType() != NullType.v()) {
            return other.getType();
         }
      } else if (other instanceof Local) {
         Type tgTp = this.tg.get((Local)other);
         if (tgTp instanceof PrimType) {
            return tgTp;
         }
      }

      return null;
   }

   public void caseIdentityStmt(IdentityStmt stmt) {
   }

   public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
      stmt.setOp(this.uv.visit(stmt.getOp(), RefType.v("java.lang.Object"), stmt));
   }

   public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
      stmt.setOp(this.uv.visit(stmt.getOp(), RefType.v("java.lang.Object"), stmt));
   }

   public void caseGotoStmt(GotoStmt stmt) {
   }

   public void caseIfStmt(IfStmt stmt) {
      this.handleBinopExpr((BinopExpr)stmt.getCondition(), stmt, BooleanType.v());
   }

   public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
      stmt.setKey(this.uv.visit(stmt.getKey(), IntType.v(), stmt));
   }

   public void caseNopStmt(NopStmt stmt) {
   }

   public void caseReturnStmt(ReturnStmt stmt) {
      stmt.setOp(this.uv.visit(stmt.getOp(), this.jb.getMethod().getReturnType(), stmt));
   }

   public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
   }

   public void caseTableSwitchStmt(TableSwitchStmt stmt) {
      stmt.setKey(this.uv.visit(stmt.getKey(), IntType.v(), stmt));
   }

   public void caseThrowStmt(ThrowStmt stmt) {
      stmt.setOp(this.uv.visit(stmt.getOp(), RefType.v("java.lang.Throwable"), stmt));
   }

   public void defaultCase(Stmt stmt) {
      throw new RuntimeException("Unhandled stgtement type: " + stmt.getClass());
   }
}
