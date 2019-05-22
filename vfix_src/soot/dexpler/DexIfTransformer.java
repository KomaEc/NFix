package soot.dexpler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.EqExpr;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LengthExpr;
import soot.jimple.NeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.AbstractInstanceInvokeExpr;
import soot.jimple.internal.AbstractInvokeExpr;

public class DexIfTransformer extends AbstractNullTransformer {
   private boolean usedAsObject;
   private boolean doBreak = false;
   Local l = null;

   public static DexIfTransformer v() {
      return new DexIfTransformer();
   }

   protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
      DexDefUseAnalysis localDefs = new DexDefUseAnalysis(body);
      Set<IfStmt> ifSet = this.getNullIfCandidates(body);
      Iterator var6 = ifSet.iterator();

      while(true) {
         Local[] twoIfLocals;
         do {
            if (!var6.hasNext()) {
               return;
            }

            IfStmt ifs = (IfStmt)var6.next();
            ConditionExpr ifCondition = (ConditionExpr)ifs.getCondition();
            twoIfLocals = new Local[]{(Local)ifCondition.getOp1(), (Local)ifCondition.getOp2()};
            this.usedAsObject = false;
            Local[] var10 = twoIfLocals;
            int var11 = twoIfLocals.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               Local loc = var10[var12];
               Set<Unit> defs = localDefs.collectDefinitionsWithAliases(loc);
               this.doBreak = false;
               Iterator var15 = defs.iterator();

               while(var15.hasNext()) {
                  Unit u = (Unit)var15.next();
                  if (!(u instanceof DefinitionStmt)) {
                     throw new RuntimeException("ERROR: def can not be something else than Assign or Identity statement! (def: " + u + " class: " + u.getClass() + "");
                  }

                  this.l = (Local)((DefinitionStmt)u).getLeftOp();
                  u.apply(new AbstractStmtSwitch() {
                     public void caseAssignStmt(AssignStmt stmt) {
                        Value r = stmt.getRightOp();
                        if (r instanceof FieldRef) {
                           DexIfTransformer.this.usedAsObject = AbstractNullTransformer.isObject(((FieldRef)r).getFieldRef().type());
                           if (DexIfTransformer.this.usedAsObject) {
                              DexIfTransformer.this.doBreak = true;
                           }

                        } else if (r instanceof ArrayRef) {
                           ArrayRef ar = (ArrayRef)r;
                           if (ar.getType() instanceof UnknownType) {
                              DexIfTransformer.this.usedAsObject = stmt.hasTag("ObjectOpTag");
                           } else {
                              DexIfTransformer.this.usedAsObject = AbstractNullTransformer.isObject(ar.getType());
                           }

                           if (DexIfTransformer.this.usedAsObject) {
                              DexIfTransformer.this.doBreak = true;
                           }

                        } else if (!(r instanceof StringConstant) && !(r instanceof NewExpr) && !(r instanceof NewArrayExpr)) {
                           if (r instanceof CastExpr) {
                              DexIfTransformer.this.usedAsObject = AbstractNullTransformer.isObject(((CastExpr)r).getCastType());
                              if (DexIfTransformer.this.usedAsObject) {
                                 DexIfTransformer.this.doBreak = true;
                              }

                           } else if (r instanceof InvokeExpr) {
                              DexIfTransformer.this.usedAsObject = AbstractNullTransformer.isObject(((InvokeExpr)r).getType());
                              if (DexIfTransformer.this.usedAsObject) {
                                 DexIfTransformer.this.doBreak = true;
                              }

                           } else if (r instanceof LengthExpr) {
                              DexIfTransformer.this.usedAsObject = false;
                              if (DexIfTransformer.this.usedAsObject) {
                                 DexIfTransformer.this.doBreak = true;
                              }

                           }
                        } else {
                           DexIfTransformer.this.usedAsObject = true;
                           if (DexIfTransformer.this.usedAsObject) {
                              DexIfTransformer.this.doBreak = true;
                           }

                        }
                     }

                     public void caseIdentityStmt(IdentityStmt stmt) {
                        if (stmt.getLeftOp() == DexIfTransformer.this.l) {
                           DexIfTransformer.this.usedAsObject = AbstractNullTransformer.isObject(stmt.getRightOp().getType());
                           if (DexIfTransformer.this.usedAsObject) {
                              DexIfTransformer.this.doBreak = true;
                           }

                        }
                     }
                  });
                  if (this.doBreak) {
                     break;
                  }

                  Iterator var17 = localDefs.getUsesOf(this.l).iterator();

                  while(var17.hasNext()) {
                     Unit use = (Unit)var17.next();
                     use.apply(new AbstractStmtSwitch() {
                        private boolean examineInvokeExpr(InvokeExpr e) {
                           List<Value> args = e.getArgs();
                           List<Type> argTypes = e.getMethodRef().parameterTypes();

                           assert args.size() == argTypes.size();

                           for(int i = 0; i < args.size(); ++i) {
                              if (args.get(i) == DexIfTransformer.this.l && AbstractNullTransformer.isObject((Type)argTypes.get(i))) {
                                 return true;
                              }
                           }

                           SootMethodRef sm = e.getMethodRef();
                           if (!sm.isStatic() && e instanceof AbstractInvokeExpr) {
                              AbstractInstanceInvokeExpr aiiexpr = (AbstractInstanceInvokeExpr)e;
                              Value b = aiiexpr.getBase();
                              if (b == DexIfTransformer.this.l) {
                                 return true;
                              }
                           }

                           return false;
                        }

                        public void caseInvokeStmt(InvokeStmt stmt) {
                           InvokeExpr e = stmt.getInvokeExpr();
                           DexIfTransformer.this.usedAsObject = this.examineInvokeExpr(e);
                           if (DexIfTransformer.this.usedAsObject) {
                              DexIfTransformer.this.doBreak = true;
                           }

                        }

                        public void caseAssignStmt(AssignStmt stmt) {
                           Value left = stmt.getLeftOp();
                           Value r = stmt.getRightOp();
                           if (!(left instanceof ArrayRef) || ((ArrayRef)left).getIndex() != DexIfTransformer.this.l) {
                              if (stmt.getRightOp() == DexIfTransformer.this.l) {
                                 Value l = stmt.getLeftOp();
                                 if (l instanceof StaticFieldRef && AbstractNullTransformer.isObject(((StaticFieldRef)l).getFieldRef().type())) {
                                    DexIfTransformer.this.usedAsObject = true;
                                    if (DexIfTransformer.this.usedAsObject) {
                                       DexIfTransformer.this.doBreak = true;
                                    }

                                    return;
                                 }

                                 if (l instanceof InstanceFieldRef && AbstractNullTransformer.isObject(((InstanceFieldRef)l).getFieldRef().type())) {
                                    DexIfTransformer.this.usedAsObject = true;
                                    if (DexIfTransformer.this.usedAsObject) {
                                       DexIfTransformer.this.doBreak = true;
                                    }

                                    return;
                                 }

                                 if (l instanceof ArrayRef) {
                                    Type aType = ((ArrayRef)l).getType();
                                    if (aType instanceof UnknownType) {
                                       DexIfTransformer.this.usedAsObject = stmt.hasTag("ObjectOpTag");
                                    } else {
                                       DexIfTransformer.this.usedAsObject = AbstractNullTransformer.isObject(aType);
                                    }

                                    if (DexIfTransformer.this.usedAsObject) {
                                       DexIfTransformer.this.doBreak = true;
                                    }

                                    return;
                                 }
                              }

                              if (r instanceof FieldRef) {
                                 DexIfTransformer.this.usedAsObject = true;
                                 if (DexIfTransformer.this.usedAsObject) {
                                    DexIfTransformer.this.doBreak = true;
                                 }

                              } else if (r instanceof ArrayRef) {
                                 ArrayRef ar = (ArrayRef)r;
                                 if (ar.getBase() == DexIfTransformer.this.l) {
                                    DexIfTransformer.this.usedAsObject = true;
                                 } else {
                                    DexIfTransformer.this.usedAsObject = false;
                                 }

                                 if (DexIfTransformer.this.usedAsObject) {
                                    DexIfTransformer.this.doBreak = true;
                                 }

                              } else if (!(r instanceof StringConstant) && !(r instanceof NewExpr)) {
                                 if (r instanceof NewArrayExpr) {
                                    DexIfTransformer.this.usedAsObject = false;
                                    if (DexIfTransformer.this.usedAsObject) {
                                       DexIfTransformer.this.doBreak = true;
                                    }

                                 } else if (r instanceof CastExpr) {
                                    DexIfTransformer.this.usedAsObject = AbstractNullTransformer.isObject(((CastExpr)r).getCastType());
                                    if (DexIfTransformer.this.usedAsObject) {
                                       DexIfTransformer.this.doBreak = true;
                                    }

                                 } else if (r instanceof InvokeExpr) {
                                    DexIfTransformer.this.usedAsObject = this.examineInvokeExpr((InvokeExpr)stmt.getRightOp());
                                    if (DexIfTransformer.this.usedAsObject) {
                                       DexIfTransformer.this.doBreak = true;
                                    }

                                 } else if (r instanceof LengthExpr) {
                                    DexIfTransformer.this.usedAsObject = true;
                                    if (DexIfTransformer.this.usedAsObject) {
                                       DexIfTransformer.this.doBreak = true;
                                    }

                                 } else if (r instanceof BinopExpr) {
                                    DexIfTransformer.this.usedAsObject = false;
                                    if (DexIfTransformer.this.usedAsObject) {
                                       DexIfTransformer.this.doBreak = true;
                                    }

                                 }
                              } else {
                                 throw new RuntimeException("NOT POSSIBLE StringConstant or NewExpr at " + stmt);
                              }
                           }
                        }

                        public void caseIdentityStmt(IdentityStmt stmt) {
                           if (stmt.getLeftOp() == DexIfTransformer.this.l) {
                              throw new RuntimeException("IMPOSSIBLE 0");
                           }
                        }

                        public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
                           DexIfTransformer.this.usedAsObject = stmt.getOp() == DexIfTransformer.this.l;
                           if (DexIfTransformer.this.usedAsObject) {
                              DexIfTransformer.this.doBreak = true;
                           }

                        }

                        public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
                           DexIfTransformer.this.usedAsObject = stmt.getOp() == DexIfTransformer.this.l;
                           if (DexIfTransformer.this.usedAsObject) {
                              DexIfTransformer.this.doBreak = true;
                           }

                        }

                        public void caseReturnStmt(ReturnStmt stmt) {
                           DexIfTransformer.this.usedAsObject = stmt.getOp() == DexIfTransformer.this.l && AbstractNullTransformer.isObject(body.getMethod().getReturnType());
                           if (DexIfTransformer.this.usedAsObject) {
                              DexIfTransformer.this.doBreak = true;
                           }

                        }

                        public void caseThrowStmt(ThrowStmt stmt) {
                           DexIfTransformer.this.usedAsObject = stmt.getOp() == DexIfTransformer.this.l;
                           if (DexIfTransformer.this.usedAsObject) {
                              DexIfTransformer.this.doBreak = true;
                           }

                        }
                     });
                     if (this.doBreak) {
                        break;
                     }
                  }

                  if (this.doBreak) {
                     break;
                  }
               }

               if (this.doBreak) {
                  break;
               }
            }
         } while(!this.usedAsObject);

         Set<Unit> defsOp1 = localDefs.collectDefinitionsWithAliases(twoIfLocals[0]);
         Set<Unit> defsOp2 = localDefs.collectDefinitionsWithAliases(twoIfLocals[1]);
         defsOp1.addAll(defsOp2);
         Iterator var21 = defsOp1.iterator();

         label98:
         while(var21.hasNext()) {
            Unit u = (Unit)var21.next();
            Stmt s = (Stmt)u;
            if (!s.containsArrayRef() || !defsOp1.contains(s.getArrayRef().getBase()) && !defsOp2.contains(s.getArrayRef().getBase())) {
               this.replaceWithNull(u);
            }

            Local l = (Local)((DefinitionStmt)u).getLeftOp();
            Iterator var25 = localDefs.getUsesOf(l).iterator();

            while(true) {
               Stmt use;
               do {
                  if (!var25.hasNext()) {
                     continue label98;
                  }

                  Unit uuse = (Unit)var25.next();
                  use = (Stmt)uuse;
               } while(use.containsArrayRef() && (twoIfLocals[0] == use.getArrayRef().getBase() || twoIfLocals[1] == use.getArrayRef().getBase()));

               this.replaceWithNull(use);
            }
         }
      }
   }

   private Set<IfStmt> getNullIfCandidates(Body body) {
      Set<IfStmt> candidates = new HashSet();
      Iterator i = body.getUnits().iterator();

      while(true) {
         Unit u;
         do {
            if (!i.hasNext()) {
               return candidates;
            }

            u = (Unit)i.next();
         } while(!(u instanceof IfStmt));

         ConditionExpr expr = (ConditionExpr)((IfStmt)u).getCondition();
         boolean isTargetIf = false;
         if ((expr instanceof EqExpr || expr instanceof NeExpr) && expr.getOp1() instanceof Local && expr.getOp2() instanceof Local) {
            isTargetIf = true;
         }

         if (isTargetIf) {
            candidates.add((IfStmt)u);
         }
      }
   }
}
