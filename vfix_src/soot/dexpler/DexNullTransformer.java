package soot.dexpler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.Local;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NullConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.AbstractInstanceInvokeExpr;
import soot.jimple.internal.AbstractInvokeExpr;

public class DexNullTransformer extends AbstractNullTransformer {
   private boolean usedAsObject;
   private boolean doBreak = false;
   private Local l = null;

   public static DexNullTransformer v() {
      return new DexNullTransformer();
   }

   protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
      DexDefUseAnalysis localDefs = new DexDefUseAnalysis(body);
      AbstractStmtSwitch checkDef = new AbstractStmtSwitch() {
         public void caseAssignStmt(AssignStmt stmt) {
            Value r = stmt.getRightOp();
            if (r instanceof FieldRef) {
               DexNullTransformer.this.usedAsObject = AbstractNullTransformer.isObject(((FieldRef)r).getFieldRef().type());
               DexNullTransformer.this.doBreak = true;
            } else if (r instanceof ArrayRef) {
               ArrayRef ar = (ArrayRef)r;
               if (ar.getType() instanceof UnknownType) {
                  DexNullTransformer.this.usedAsObject = stmt.hasTag("ObjectOpTag");
               } else {
                  DexNullTransformer.this.usedAsObject = AbstractNullTransformer.isObject(ar.getType());
               }

               DexNullTransformer.this.doBreak = true;
            } else if (!(r instanceof StringConstant) && !(r instanceof NewExpr) && !(r instanceof NewArrayExpr)) {
               if (r instanceof CastExpr) {
                  DexNullTransformer.this.usedAsObject = AbstractNullTransformer.isObject(((CastExpr)r).getCastType());
                  DexNullTransformer.this.doBreak = true;
               } else if (r instanceof InvokeExpr) {
                  DexNullTransformer.this.usedAsObject = AbstractNullTransformer.isObject(((InvokeExpr)r).getType());
                  DexNullTransformer.this.doBreak = true;
               } else if (r instanceof LengthExpr) {
                  DexNullTransformer.this.usedAsObject = false;
                  DexNullTransformer.this.doBreak = true;
               }
            } else {
               DexNullTransformer.this.usedAsObject = true;
               DexNullTransformer.this.doBreak = true;
            }
         }

         public void caseIdentityStmt(IdentityStmt stmt) {
            if (stmt.getLeftOp() == DexNullTransformer.this.l) {
               DexNullTransformer.this.usedAsObject = AbstractNullTransformer.isObject(stmt.getRightOp().getType());
               DexNullTransformer.this.doBreak = true;
            }
         }
      };
      AbstractStmtSwitch checkUse = new AbstractStmtSwitch() {
         private boolean examineInvokeExpr(InvokeExpr e) {
            List<Value> args = e.getArgs();
            List<Type> argTypes = e.getMethodRef().parameterTypes();

            assert args.size() == argTypes.size();

            for(int i = 0; i < args.size(); ++i) {
               if (args.get(i) == DexNullTransformer.this.l && AbstractNullTransformer.isObject((Type)argTypes.get(i))) {
                  return true;
               }
            }

            SootMethodRef sm = e.getMethodRef();
            if (!sm.isStatic() && e instanceof AbstractInvokeExpr) {
               AbstractInstanceInvokeExpr aiiexpr = (AbstractInstanceInvokeExpr)e;
               Value b = aiiexpr.getBase();
               if (b == DexNullTransformer.this.l) {
                  return true;
               }
            }

            return false;
         }

         public void caseInvokeStmt(InvokeStmt stmt) {
            InvokeExpr e = stmt.getInvokeExpr();
            DexNullTransformer.this.usedAsObject = this.examineInvokeExpr(e);
            DexNullTransformer.this.doBreak = true;
         }

         public void caseAssignStmt(AssignStmt stmt) {
            Value left = stmt.getLeftOp();
            Value r = stmt.getRightOp();
            ArrayRef ar;
            if (left instanceof ArrayRef) {
               ar = (ArrayRef)left;
               if (ar.getIndex() == DexNullTransformer.this.l) {
                  DexNullTransformer.this.doBreak = true;
                  return;
               }

               if (ar.getBase() == DexNullTransformer.this.l) {
                  DexNullTransformer.this.usedAsObject = true;
                  DexNullTransformer.this.doBreak = true;
                  return;
               }
            }

            if (left instanceof InstanceFieldRef) {
               InstanceFieldRef ifr = (InstanceFieldRef)left;
               if (ifr.getBase() == DexNullTransformer.this.l) {
                  DexNullTransformer.this.usedAsObject = true;
                  DexNullTransformer.this.doBreak = true;
                  return;
               }
            }

            if (stmt.getRightOp() == DexNullTransformer.this.l) {
               Value l = stmt.getLeftOp();
               if (l instanceof StaticFieldRef && AbstractNullTransformer.isObject(((StaticFieldRef)l).getFieldRef().type())) {
                  DexNullTransformer.this.usedAsObject = true;
                  DexNullTransformer.this.doBreak = true;
                  return;
               }

               if (l instanceof InstanceFieldRef && AbstractNullTransformer.isObject(((InstanceFieldRef)l).getFieldRef().type())) {
                  DexNullTransformer.this.usedAsObject = true;
                  DexNullTransformer.this.doBreak = true;
                  return;
               }

               if (l instanceof ArrayRef) {
                  Type aType = ((ArrayRef)l).getType();
                  if (aType instanceof UnknownType) {
                     DexNullTransformer.this.usedAsObject = stmt.hasTag("ObjectOpTag");
                  } else {
                     DexNullTransformer.this.usedAsObject = AbstractNullTransformer.isObject(aType);
                  }

                  DexNullTransformer.this.doBreak = true;
                  return;
               }
            }

            if (r instanceof FieldRef) {
               DexNullTransformer.this.usedAsObject = true;
               DexNullTransformer.this.doBreak = true;
            } else if (r instanceof ArrayRef) {
               ar = (ArrayRef)r;
               if (ar.getBase() == DexNullTransformer.this.l) {
                  DexNullTransformer.this.usedAsObject = true;
               } else {
                  DexNullTransformer.this.usedAsObject = false;
               }

               DexNullTransformer.this.doBreak = true;
            } else if (!(r instanceof StringConstant) && !(r instanceof NewExpr)) {
               if (r instanceof NewArrayExpr) {
                  DexNullTransformer.this.usedAsObject = false;
                  DexNullTransformer.this.doBreak = true;
               } else if (r instanceof CastExpr) {
                  DexNullTransformer.this.usedAsObject = AbstractNullTransformer.isObject(((CastExpr)r).getCastType());
                  DexNullTransformer.this.doBreak = true;
               } else if (r instanceof InvokeExpr) {
                  DexNullTransformer.this.usedAsObject = this.examineInvokeExpr((InvokeExpr)stmt.getRightOp());
                  DexNullTransformer.this.doBreak = true;
               } else if (r instanceof LengthExpr) {
                  DexNullTransformer.this.usedAsObject = true;
                  DexNullTransformer.this.doBreak = true;
               } else if (r instanceof BinopExpr) {
                  DexNullTransformer.this.usedAsObject = false;
                  DexNullTransformer.this.doBreak = true;
               }
            } else {
               throw new RuntimeException("NOT POSSIBLE StringConstant or NewExpr at " + stmt);
            }
         }

         public void caseIdentityStmt(IdentityStmt stmt) {
            if (stmt.getLeftOp() == DexNullTransformer.this.l) {
               throw new RuntimeException("IMPOSSIBLE 0");
            }
         }

         public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
            DexNullTransformer.this.usedAsObject = stmt.getOp() == DexNullTransformer.this.l;
            DexNullTransformer.this.doBreak = true;
         }

         public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
            DexNullTransformer.this.usedAsObject = stmt.getOp() == DexNullTransformer.this.l;
            DexNullTransformer.this.doBreak = true;
         }

         public void caseReturnStmt(ReturnStmt stmt) {
            DexNullTransformer.this.usedAsObject = stmt.getOp() == DexNullTransformer.this.l && AbstractNullTransformer.isObject(body.getMethod().getReturnType());
            DexNullTransformer.this.doBreak = true;
         }

         public void caseThrowStmt(ThrowStmt stmt) {
            DexNullTransformer.this.usedAsObject = stmt.getOp() == DexNullTransformer.this.l;
            DexNullTransformer.this.doBreak = true;
         }
      };
      Iterator var7 = this.getNullCandidates(body).iterator();

      while(true) {
         Set defs;
         Iterator var10;
         Unit u;
         do {
            if (!var7.hasNext()) {
               AbstractStmtSwitch inlinedZeroValues = new AbstractStmtSwitch() {
                  final NullConstant nullConstant = NullConstant.v();

                  public void caseAssignStmt(AssignStmt stmt) {
                     if (AbstractNullTransformer.isObject(stmt.getLeftOp().getType()) && this.isConstZero(stmt.getRightOp())) {
                        stmt.setRightOp(this.nullConstant);
                     } else {
                        if (stmt.getRightOp() instanceof CastExpr) {
                           CastExpr ce = (CastExpr)stmt.getRightOp();
                           if (AbstractNullTransformer.isObject(ce.getCastType()) && this.isConstZero(ce.getOp())) {
                              stmt.setRightOp(this.nullConstant);
                           }
                        }

                        if (stmt.getLeftOp() instanceof ArrayRef && this.isConstZero(stmt.getRightOp())) {
                           ArrayRef ar = (ArrayRef)stmt.getLeftOp();
                           if (DexNullTransformer.this.isObjectArray(ar.getBase(), body) || stmt.hasTag("ObjectOpTag")) {
                              stmt.setRightOp(this.nullConstant);
                           }
                        }

                     }
                  }

                  private boolean isConstZero(Value rightOp) {
                     if (rightOp instanceof IntConstant && ((IntConstant)rightOp).value == 0) {
                        return true;
                     } else {
                        return rightOp instanceof LongConstant && ((LongConstant)rightOp).value == 0L;
                     }
                  }

                  public void caseReturnStmt(ReturnStmt stmt) {
                     if (stmt.getOp() instanceof IntConstant && AbstractNullTransformer.isObject(body.getMethod().getReturnType())) {
                        IntConstant iconst = (IntConstant)stmt.getOp();

                        assert iconst.value == 0;

                        stmt.setOp(this.nullConstant);
                     }

                  }

                  public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
                     if (stmt.getOp() instanceof IntConstant && ((IntConstant)stmt.getOp()).value == 0) {
                        stmt.setOp(this.nullConstant);
                     }

                  }

                  public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
                     if (stmt.getOp() instanceof IntConstant && ((IntConstant)stmt.getOp()).value == 0) {
                        stmt.setOp(this.nullConstant);
                     }

                  }
               };
               NullConstant nullConstant = NullConstant.v();
               Iterator var19 = body.getUnits().iterator();

               while(true) {
                  Stmt stmt;
                  do {
                     Unit u;
                     do {
                        if (!var19.hasNext()) {
                           return;
                        }

                        u = (Unit)var19.next();
                        u.apply(inlinedZeroValues);
                     } while(!(u instanceof Stmt));

                     stmt = (Stmt)u;
                  } while(!stmt.containsInvokeExpr());

                  InvokeExpr invExpr = stmt.getInvokeExpr();

                  for(int i = 0; i < invExpr.getArgCount(); ++i) {
                     if (isObject(invExpr.getMethodRef().parameterType(i)) && invExpr.getArg(i) instanceof IntConstant) {
                        IntConstant iconst = (IntConstant)invExpr.getArg(i);

                        assert iconst.value == 0;

                        invExpr.setArg(i, nullConstant);
                     }
                  }
               }
            }

            Local loc = (Local)var7.next();
            this.usedAsObject = false;
            defs = localDefs.collectDefinitionsWithAliases(loc);
            this.doBreak = false;
            var10 = defs.iterator();

            while(var10.hasNext()) {
               u = (Unit)var10.next();
               if (u instanceof DefinitionStmt) {
                  this.l = (Local)((DefinitionStmt)u).getLeftOp();
               } else if (u instanceof IfStmt) {
                  throw new RuntimeException("ERROR: def can not be something else than Assign or Identity statement! (def: " + u + " class: " + u.getClass() + "");
               }

               u.apply(checkDef);
               if (this.doBreak) {
                  break;
               }

               Iterator var12 = localDefs.getUsesOf(this.l).iterator();

               while(var12.hasNext()) {
                  Unit use = (Unit)var12.next();
                  use.apply(checkUse);
                  if (this.doBreak) {
                     break;
                  }
               }

               if (this.doBreak) {
                  break;
               }
            }
         } while(!this.usedAsObject);

         var10 = defs.iterator();

         label119:
         while(var10.hasNext()) {
            u = (Unit)var10.next();
            this.replaceWithNull(u);
            Set<Value> defLocals = new HashSet();
            Iterator var24 = u.getDefBoxes().iterator();

            while(var24.hasNext()) {
               ValueBox vb = (ValueBox)var24.next();
               defLocals.add(vb.getValue());
            }

            Local l = (Local)((DefinitionStmt)u).getLeftOp();
            Iterator var27 = localDefs.getUsesOf(l).iterator();

            while(true) {
               Stmt use;
               do {
                  if (!var27.hasNext()) {
                     continue label119;
                  }

                  Unit uuse = (Unit)var27.next();
                  use = (Stmt)uuse;
               } while(use.containsArrayRef() && defLocals.contains(use.getArrayRef().getBase()));

               this.replaceWithNull(use);
            }
         }
      }
   }

   private boolean isObjectArray(Value v, Body body) {
      Iterator var3 = body.getUnits().iterator();

      while(var3.hasNext()) {
         Unit u = (Unit)var3.next();
         if (u instanceof AssignStmt) {
            AssignStmt assign = (AssignStmt)u;
            if (assign.getLeftOp() == v) {
               if (assign.getRightOp() instanceof NewArrayExpr) {
                  NewArrayExpr nea = (NewArrayExpr)assign.getRightOp();
                  if (isObject(nea.getBaseType())) {
                     return true;
                  }
               } else if (assign.getRightOp() instanceof FieldRef) {
                  FieldRef fr = (FieldRef)assign.getRightOp();
                  if (fr.getType() instanceof ArrayType && isObject(((ArrayType)fr.getType()).getArrayElementType())) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   private Set<Local> getNullCandidates(Body body) {
      Set<Local> candidates = null;
      Iterator var3 = body.getUnits().iterator();

      while(true) {
         Local l;
         Value r;
         do {
            AssignStmt a;
            label46:
            do {
               while(var3.hasNext()) {
                  Unit u = (Unit)var3.next();
                  if (u instanceof AssignStmt) {
                     a = (AssignStmt)u;
                     continue label46;
                  }

                  if (u instanceof IfStmt) {
                     ConditionExpr expr = (ConditionExpr)((IfStmt)u).getCondition();
                     if (this.isZeroComparison(expr) && expr.getOp1() instanceof Local) {
                        if (candidates == null) {
                           candidates = new HashSet();
                        }

                        candidates.add((Local)expr.getOp1());
                     }
                  }
               }

               return (Set)(candidates == null ? Collections.emptySet() : candidates);
            } while(!(a.getLeftOp() instanceof Local));

            l = (Local)a.getLeftOp();
            r = a.getRightOp();
         } while((!(r instanceof IntConstant) || ((IntConstant)r).value != 0) && (!(r instanceof LongConstant) || ((LongConstant)r).value != 0L));

         if (candidates == null) {
            candidates = new HashSet();
         }

         candidates.add(l);
      }
   }
}
