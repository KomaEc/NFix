package soot.dexpler.typing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PrimType;
import soot.RefType;
import soot.ShortType;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.dexpler.IDalvikTyper;
import soot.dexpler.tags.DoubleOpTag;
import soot.dexpler.tags.FloatOpTag;
import soot.dexpler.tags.IntOpTag;
import soot.dexpler.tags.LongOpTag;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.BreakpointStmt;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.DivExpr;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.RemExpr;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UnopExpr;
import soot.jimple.UshrExpr;
import soot.tagkit.Tag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.SimpleLocalDefs;
import soot.toolkits.scalar.SimpleLocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;

public class DalvikTyper implements IDalvikTyper {
   private static DalvikTyper dt = null;
   private Set<DalvikTyper.Constraint> constraints = new HashSet();
   private Map<ValueBox, Type> typed = new HashMap();
   private Map<Local, Type> localTyped = new HashMap();
   private Set<Local> localTemp = new HashSet();
   private List<DalvikTyper.LocalObj> localObjList = new ArrayList();
   private Map<Local, List<DalvikTyper.LocalObj>> local2Obj = new HashMap();

   private DalvikTyper() {
   }

   public static DalvikTyper v() {
      if (dt == null) {
         dt = new DalvikTyper();
      }

      return dt;
   }

   public void clear() {
      this.constraints.clear();
      this.typed.clear();
      this.localTyped.clear();
      this.localTemp.clear();
      this.localObjList.clear();
      this.local2Obj.clear();
   }

   public void setType(ValueBox vb, Type t, boolean isUse) {
      if (vb.getValue() instanceof Local) {
         DalvikTyper.LocalObj lb = new DalvikTyper.LocalObj(vb, t, isUse);
         this.localObjList.add(lb);
         Local k = (Local)vb.getValue();
         if (!this.local2Obj.containsKey(k)) {
            this.local2Obj.put(k, new ArrayList());
         }

         ((List)this.local2Obj.get(k)).add(lb);
      }

   }

   public void addConstraint(ValueBox l, ValueBox r) {
   }

   public void assignType(final Body b) {
      this.constraints.clear();
      this.localObjList.clear();
      final Set<Unit> todoUnits = new HashSet();
      Iterator var3 = b.getUnits().iterator();

      while(var3.hasNext()) {
         Unit u = (Unit)var3.next();
         StmtSwitch ss = new StmtSwitch() {
            public void caseBreakpointStmt(BreakpointStmt stmt) {
            }

            public void caseInvokeStmt(InvokeStmt stmt) {
               DalvikTyper.v().setInvokeType(stmt.getInvokeExpr());
            }

            public void caseAssignStmt(AssignStmt stmt) {
               Value l = stmt.getLeftOp();
               Value r = stmt.getRightOp();
               ValueBox sb;
               if (r instanceof NewArrayExpr) {
                  NewArrayExpr nae = (NewArrayExpr)r;
                  sb = nae.getSizeBox();
                  if (sb.getValue() instanceof Local) {
                     DalvikTyper.v().setType(sb, IntType.v(), true);
                  }
               }

               if (stmt.containsArrayRef()) {
                  ArrayRef ar = stmt.getArrayRef();
                  sb = ar.getIndexBox();
                  if (sb.getValue() instanceof Local) {
                     DalvikTyper.v().setType(sb, IntType.v(), true);
                  }
               }

               if (l instanceof Local && r instanceof Local) {
                  DalvikTyper.v().addConstraint(stmt.getLeftOpBox(), stmt.getRightOpBox());
               } else {
                  if (stmt.containsInvokeExpr()) {
                     DalvikTyper.v().setInvokeType(stmt.getInvokeExpr());
                  }

                  Type rightType;
                  if (r instanceof Local) {
                     rightType = stmt.getLeftOp().getType();
                     if (l instanceof ArrayRef && rightType instanceof UnknownType) {
                        todoUnits.add(stmt);
                     } else {
                        DalvikTyper.v().setType(stmt.getRightOpBox(), rightType, true);
                     }
                  } else if (l instanceof Local) {
                     if (!(r instanceof UntypedConstant)) {
                        Iterator var10 = stmt.getTags().iterator();

                        while(var10.hasNext()) {
                           Tag t = (Tag)var10.next();
                           if (r instanceof CastExpr) {
                              break;
                           }

                           if (t instanceof IntOpTag) {
                              DalvikTyper.this.checkExpr(r, IntType.v());
                              DalvikTyper.v().setType(stmt.getLeftOpBox(), IntType.v(), false);
                              return;
                           }

                           if (t instanceof FloatOpTag) {
                              DalvikTyper.this.checkExpr(r, FloatType.v());
                              DalvikTyper.v().setType(stmt.getLeftOpBox(), FloatType.v(), false);
                              return;
                           }

                           if (t instanceof DoubleOpTag) {
                              DalvikTyper.this.checkExpr(r, DoubleType.v());
                              DalvikTyper.v().setType(stmt.getLeftOpBox(), DoubleType.v(), false);
                              return;
                           }

                           if (t instanceof LongOpTag) {
                              DalvikTyper.this.checkExpr(r, LongType.v());
                              DalvikTyper.v().setType(stmt.getLeftOpBox(), LongType.v(), false);
                              return;
                           }
                        }

                        rightType = stmt.getRightOp().getType();
                        if (r instanceof ArrayRef && rightType instanceof UnknownType) {
                           todoUnits.add(stmt);
                        } else {
                           if (r instanceof CastExpr) {
                              CastExpr ce = (CastExpr)r;
                              Type castType = ce.getCastType();
                              if (castType instanceof PrimType) {
                                 Iterator var7 = stmt.getTags().iterator();

                                 while(var7.hasNext()) {
                                    Tag tx = (Tag)var7.next();
                                    if (tx instanceof IntOpTag) {
                                       DalvikTyper.v().setType(ce.getOpBox(), IntType.v(), false);
                                       return;
                                    }

                                    if (tx instanceof FloatOpTag) {
                                       DalvikTyper.v().setType(ce.getOpBox(), FloatType.v(), false);
                                       return;
                                    }

                                    if (tx instanceof DoubleOpTag) {
                                       DalvikTyper.v().setType(ce.getOpBox(), DoubleType.v(), false);
                                       return;
                                    }

                                    if (tx instanceof LongOpTag) {
                                       DalvikTyper.v().setType(ce.getOpBox(), LongType.v(), false);
                                       return;
                                    }
                                 }
                              } else {
                                 DalvikTyper.v().setType(ce.getOpBox(), RefType.v("java.lang.Object"), false);
                              }
                           }

                           DalvikTyper.v().setType(stmt.getLeftOpBox(), rightType, false);
                        }
                     }
                  }
               }
            }

            public void caseIdentityStmt(IdentityStmt stmt) {
               DalvikTyper.v().setType(stmt.getLeftOpBox(), stmt.getRightOp().getType(), false);
            }

            public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
               DalvikTyper.v().setType(stmt.getOpBox(), RefType.v("java.lang.Object"), true);
            }

            public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
               DalvikTyper.v().setType(stmt.getOpBox(), RefType.v("java.lang.Object"), true);
            }

            public void caseGotoStmt(GotoStmt stmt) {
            }

            public void caseIfStmt(IfStmt stmt) {
               Value c = stmt.getCondition();
               if (c instanceof BinopExpr) {
                  BinopExpr bo = (BinopExpr)c;
                  Value op1 = bo.getOp1();
                  Value op2 = bo.getOp2();
                  if (op1 instanceof Local && op2 instanceof Local) {
                     DalvikTyper.v().addConstraint(bo.getOp1Box(), bo.getOp2Box());
                  }
               }

            }

            public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
               DalvikTyper.v().setType(stmt.getKeyBox(), IntType.v(), true);
            }

            public void caseNopStmt(NopStmt stmt) {
            }

            public void caseRetStmt(RetStmt stmt) {
            }

            public void caseReturnStmt(ReturnStmt stmt) {
               DalvikTyper.v().setType(stmt.getOpBox(), b.getMethod().getReturnType(), true);
            }

            public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
            }

            public void caseTableSwitchStmt(TableSwitchStmt stmt) {
               DalvikTyper.v().setType(stmt.getKeyBox(), IntType.v(), true);
            }

            public void caseThrowStmt(ThrowStmt stmt) {
               DalvikTyper.v().setType(stmt.getOpBox(), RefType.v("java.lang.Object"), true);
            }

            public void defaultCase(Object obj) {
               throw new RuntimeException("error: unknown statement: " + obj);
            }
         };
         u.apply(ss);
      }

      Iterator var6;
      Value r;
      HashSet toDo;
      Iterator elementType;
      Value right;
      Unit u;
      AssignStmt ass;
      Value r;
      Local rightLocal;
      Local base;
      if (!todoUnits.isEmpty()) {
         UnitGraph ug = new ExceptionalUnitGraph(b);
         SimpleLocalDefs sld = new SimpleLocalDefs(ug);
         SimpleLocalUses slu = new SimpleLocalUses(b, sld);
         var6 = b.getUnits().iterator();

         label473:
         while(true) {
            DefinitionStmt ass;
            Type ar;
            HashSet done;
            do {
               do {
                  do {
                     Unit u;
                     do {
                        if (!var6.hasNext()) {
                           for(var6 = todoUnits.iterator(); var6.hasNext(); u = (Unit)var6.next()) {
                           }

                           while(true) {
                              if (todoUnits.isEmpty()) {
                                 break label473;
                              }

                              u = (Unit)todoUnits.iterator().next();
                              if (!(u instanceof AssignStmt)) {
                                 throw new RuntimeException("error: expecting assign stmt. Got " + u);
                              }

                              ass = (AssignStmt)u;
                              r = ass.getLeftOp();
                              r = ass.getRightOp();
                              ar = null;
                              done = null;
                              ArrayRef ar;
                              if (r instanceof ArrayRef) {
                                 ar = (ArrayRef)r;
                                 rightLocal = (Local)r;
                              } else {
                                 if (!(r instanceof ArrayRef)) {
                                    throw new RuntimeException("error: expecting an array ref. Got " + u);
                                 }

                                 ar = (ArrayRef)r;
                                 rightLocal = (Local)r;
                              }

                              base = (Local)ar.getBase();
                              if (!this.local2Obj.containsKey(base)) {
                                 throw new RuntimeException("oups");
                              }

                              Type baseT = ((DalvikTyper.LocalObj)((List)this.local2Obj.get(base)).get(0)).t;
                              ArrayType aTypeOtherThanObject;
                              if (((Type)baseT).toString().equals("java.lang.Object")) {
                                 aTypeOtherThanObject = null;
                                 Iterator var67 = ((List)this.local2Obj.get(base)).iterator();

                                 while(var67.hasNext()) {
                                    DalvikTyper.LocalObj lo = (DalvikTyper.LocalObj)var67.next();
                                    if (lo.t instanceof ArrayType) {
                                       aTypeOtherThanObject = (ArrayType)lo.t;
                                    }
                                 }

                                 if (aTypeOtherThanObject == null) {
                                    throw new RuntimeException("did not found array type for base " + base + " " + this.local2Obj.get(base) + " \n " + b);
                                 }

                                 baseT = aTypeOtherThanObject;
                              }

                              aTypeOtherThanObject = (ArrayType)baseT;
                              Type t = aTypeOtherThanObject.getElementType();
                              if (t instanceof UnknownType) {
                                 todoUnits.add(u);
                              } else {
                                 v().setType(ar == r ? ass.getRightOpBox() : ass.getLeftOpBox(), t, true);
                                 todoUnits.remove(u);
                              }
                           }
                        }

                        u = (Unit)var6.next();
                     } while(!(u instanceof DefinitionStmt));

                     ass = (DefinitionStmt)u;
                     r = ass.getRightOp();
                  } while(r instanceof UntypedConstant);

                  ar = r.getType();
               } while(!(ar instanceof ArrayType));
            } while(!(ass.getLeftOp() instanceof Local));

            done = new HashSet();
            toDo = new HashSet();
            toDo.add(ass);

            while(true) {
               label469:
               while(true) {
                  if (toDo.isEmpty()) {
                     continue label473;
                  }

                  DefinitionStmt currentUnit = (DefinitionStmt)toDo.iterator().next();
                  if (done.contains(currentUnit)) {
                     toDo.remove(currentUnit);
                  } else {
                     done.add(currentUnit);
                     elementType = slu.getUsesOf(currentUnit).iterator();

                     while(true) {
                        Unit use;
                        AssignStmt ass2;
                        Type newType;
                        while(true) {
                           Value r2;
                           do {
                              do {
                                 do {
                                    if (!elementType.hasNext()) {
                                       continue label469;
                                    }

                                    UnitValueBoxPair uvbp = (UnitValueBoxPair)elementType.next();
                                    use = uvbp.unit;
                                    right = null;
                                    r2 = null;
                                 } while(!(use instanceof AssignStmt));

                                 ass2 = (AssignStmt)use;
                                 right = ass2.getLeftOp();
                                 r2 = ass2.getRightOp();
                              } while(!(right instanceof Local));
                           } while(!(r2 instanceof Local) && !(r2 instanceof ArrayRef));

                           newType = null;
                           if (r2 instanceof Local) {
                              List<DalvikTyper.LocalObj> lobjs = (List)this.local2Obj.get(r2);
                              newType = ((DalvikTyper.LocalObj)lobjs.get(0)).t;
                              break;
                           }

                           if (!(r2 instanceof ArrayRef)) {
                              throw new RuntimeException("error: expected Local or ArrayRef. Got " + r2);
                           }

                           ArrayRef ar = (ArrayRef)r2;
                           if (ar.getIndex() != currentUnit.getLeftOp()) {
                              Local arBase = (Local)ar.getBase();
                              List<DalvikTyper.LocalObj> lobjs = (List)this.local2Obj.get(arBase);
                              Type baseT = ((DalvikTyper.LocalObj)lobjs.get(0)).t;
                              ArrayType aTypeOtherThanObject;
                              if (((Type)baseT).toString().equals("java.lang.Object")) {
                                 aTypeOtherThanObject = null;
                                 Iterator var26 = ((List)this.local2Obj.get(arBase)).iterator();

                                 while(var26.hasNext()) {
                                    DalvikTyper.LocalObj lo = (DalvikTyper.LocalObj)var26.next();
                                    if (lo.t instanceof ArrayType) {
                                       aTypeOtherThanObject = (ArrayType)lo.t;
                                    }
                                 }

                                 if (aTypeOtherThanObject == null) {
                                    throw new RuntimeException("error: did not found array type for base " + arBase + " " + this.local2Obj.get(arBase) + " \n " + b);
                                 }

                                 baseT = aTypeOtherThanObject;
                              }

                              aTypeOtherThanObject = (ArrayType)baseT;
                              newType = aTypeOtherThanObject.getElementType();
                              break;
                           }
                        }

                        toDo.add((DefinitionStmt)use);
                        v().setType(ass2.getLeftOpBox(), newType, true);
                     }
                  }
               }
            }
         }
      }

      List<ValueBox> vbList = b.getUseAndDefBoxes();
      List<DalvikTyper.Constraint> toRemove = new ArrayList();
      Iterator var33 = this.constraints.iterator();

      DalvikTyper.Constraint c;
      while(var33.hasNext()) {
         c = (DalvikTyper.Constraint)var33.next();
         if (!vbList.contains(c.l)) {
            toRemove.add(c);
         } else if (!vbList.contains(c.r)) {
            toRemove.add(c);
         }
      }

      var33 = toRemove.iterator();

      while(var33.hasNext()) {
         c = (DalvikTyper.Constraint)var33.next();
         this.constraints.remove(c);
      }

      var33 = this.localObjList.iterator();

      while(true) {
         DalvikTyper.LocalObj lo;
         Local l;
         Type t;
         do {
            do {
               if (!var33.hasNext()) {
                  var33 = this.typed.keySet().iterator();

                  while(var33.hasNext()) {
                     ValueBox vb = (ValueBox)var33.next();
                     if (vb.getValue() instanceof Local) {
                        l = (Local)vb.getValue();
                        this.localTyped.put(l, this.typed.get(vb));
                     }
                  }

                  var33 = this.constraints.iterator();

                  while(var33.hasNext()) {
                     c = (DalvikTyper.Constraint)var33.next();

                     ValueBox var45;
                     for(Iterator var43 = this.typed.keySet().iterator(); var43.hasNext(); var45 = (ValueBox)var43.next()) {
                     }
                  }

                  Local var44;
                  for(var33 = this.localTyped.keySet().iterator(); var33.hasNext(); var44 = (Local)var33.next()) {
                  }

                  while(!this.constraints.isEmpty()) {
                     boolean update = false;
                     var6 = this.constraints.iterator();

                     while(var6.hasNext()) {
                        DalvikTyper.Constraint c = (DalvikTyper.Constraint)var6.next();
                        r = c.l.getValue();
                        r = c.r.getValue();
                        if (r instanceof Local && r instanceof Constant) {
                           Constant cst = (Constant)r;
                           if (this.localTyped.containsKey(r)) {
                              Type lt = (Type)this.localTyped.get(r);
                              toDo = null;
                              Object newValue;
                              UntypedIntOrFloatConstant uf;
                              if (!(lt instanceof IntType) && !(lt instanceof BooleanType) && !(lt instanceof ShortType) && !(lt instanceof CharType) && !(lt instanceof ByteType)) {
                                 if (lt instanceof FloatType) {
                                    uf = (UntypedIntOrFloatConstant)cst;
                                    newValue = uf.toFloatConstant();
                                 } else {
                                    UntypedLongOrDoubleConstant ud;
                                    if (lt instanceof DoubleType) {
                                       ud = (UntypedLongOrDoubleConstant)cst;
                                       newValue = ud.toDoubleConstant();
                                    } else if (lt instanceof LongType) {
                                       ud = (UntypedLongOrDoubleConstant)cst;
                                       newValue = ud.toLongConstant();
                                    } else {
                                       if (!(cst instanceof UntypedIntOrFloatConstant) || ((UntypedIntOrFloatConstant)cst).value != 0) {
                                          throw new RuntimeException("unknow type for constance: " + lt);
                                       }

                                       newValue = NullConstant.v();
                                    }
                                 }
                              } else {
                                 uf = (UntypedIntOrFloatConstant)cst;
                                 newValue = uf.toIntConstant();
                              }

                              c.r.setValue((Value)newValue);
                              this.constraints.remove(c);
                              update = true;
                              break;
                           }
                        } else {
                           Local leftLocal;
                           if (r instanceof Local && r instanceof Local) {
                              leftLocal = (Local)r;
                              rightLocal = (Local)r;
                              Type rightLocalType;
                              if (this.localTyped.containsKey(leftLocal)) {
                                 rightLocalType = (Type)this.localTyped.get(leftLocal);
                                 if (!this.localTyped.containsKey(rightLocal)) {
                                    rightLocal.setType(rightLocalType);
                                    this.setLocalTyped(rightLocal, rightLocalType);
                                 }

                                 this.constraints.remove(c);
                                 update = true;
                                 break;
                              } else if (this.localTyped.containsKey(rightLocal)) {
                                 rightLocalType = (Type)this.localTyped.get(rightLocal);
                                 if (!this.localTyped.containsKey(leftLocal)) {
                                    leftLocal.setType(rightLocalType);
                                    this.setLocalTyped(leftLocal, rightLocalType);
                                 }

                                 this.constraints.remove(c);
                                 update = true;
                                 break;
                              }
                           } else {
                              ArrayRef ar;
                              Type t;
                              Type elementType;
                              ArrayType at;
                              if (r instanceof ArrayRef && r instanceof Local) {
                                 leftLocal = (Local)r;
                                 ar = (ArrayRef)r;
                                 base = (Local)ar.getBase();
                                 if (this.localTyped.containsKey(base)) {
                                    t = (Type)this.localTyped.get(base);
                                    elementType = null;
                                    if (t instanceof ArrayType) {
                                       at = (ArrayType)t;
                                       elementType = at.getArrayElementType();
                                       if (!this.localTyped.containsKey(leftLocal)) {
                                          leftLocal.setType(elementType);
                                          this.setLocalTyped(leftLocal, elementType);
                                       }

                                       this.constraints.remove(c);
                                       update = true;
                                       break;
                                    }
                                 }
                              } else {
                                 if (r instanceof Local && r instanceof ArrayRef) {
                                    leftLocal = (Local)r;
                                    ar = (ArrayRef)r;
                                    base = (Local)ar.getBase();
                                    if (!this.localTyped.containsKey(base)) {
                                       continue;
                                    }

                                    t = (Type)this.localTyped.get(base);
                                    elementType = null;
                                    if (!(t instanceof ArrayType)) {
                                       continue;
                                    }

                                    at = (ArrayType)t;
                                    elementType = at.getArrayElementType();
                                    if (!this.localTyped.containsKey(leftLocal)) {
                                       leftLocal.setType(elementType);
                                       this.setLocalTyped(leftLocal, elementType);
                                    }

                                    this.constraints.remove(c);
                                    update = true;
                                    break;
                                 }

                                 throw new RuntimeException("error: do not handling this kind of constraint: " + c);
                              }
                           }
                        }
                     }

                     if (!update) {
                        break;
                     }
                  }

                  var33 = b.getUnits().iterator();

                  while(var33.hasNext()) {
                     u = (Unit)var33.next();
                     if (u instanceof AssignStmt) {
                        ass = (AssignStmt)u;
                        if (ass.getLeftOp() instanceof Local && ass.getRightOp() instanceof UntypedConstant) {
                           UntypedConstant uc = (UntypedConstant)ass.getRightOp();
                           ass.setRightOp(uc.defineType((Type)this.localTyped.get(ass.getLeftOp())));
                        }
                     }
                  }

                  var33 = this.constraints.iterator();

                  while(true) {
                     while(true) {
                        Value l;
                        do {
                           do {
                              if (!var33.hasNext()) {
                                 var33 = b.getUnits().iterator();

                                 while(var33.hasNext()) {
                                    u = (Unit)var33.next();
                                    StmtSwitch sw = new StmtSwitch() {
                                       public void caseBreakpointStmt(BreakpointStmt stmt) {
                                       }

                                       public void caseInvokeStmt(InvokeStmt stmt) {
                                          DalvikTyper.this.changeUntypedConstantsInInvoke(stmt.getInvokeExpr());
                                       }

                                       public void caseAssignStmt(AssignStmt stmt) {
                                          UntypedIntOrFloatConstant ucx;
                                          if (stmt.getRightOp() instanceof NewArrayExpr) {
                                             NewArrayExpr nae = (NewArrayExpr)stmt.getRightOp();
                                             if (nae.getSize() instanceof UntypedConstant) {
                                                ucx = (UntypedIntOrFloatConstant)nae.getSize();
                                                nae.setSize(ucx.defineType(IntType.v()));
                                             }
                                          } else if (stmt.getRightOp() instanceof UntypedConstant) {
                                             UntypedConstant ucxx = (UntypedConstant)stmt.getRightOp();
                                             Value l = stmt.getLeftOp();
                                             Type lTypex = null;
                                             if (l instanceof ArrayRef) {
                                                ArrayRef ar = (ArrayRef)l;
                                                Local baseLocalx = (Local)ar.getBase();
                                                ArrayType arrayType = (ArrayType)DalvikTyper.this.localTyped.get(baseLocalx);
                                                lTypex = arrayType.getElementType();
                                             } else {
                                                lTypex = l.getType();
                                             }

                                             stmt.setRightOp(ucxx.defineType(lTypex));
                                          } else if (stmt.getRightOp() instanceof InvokeExpr) {
                                             DalvikTyper.this.changeUntypedConstantsInInvoke((InvokeExpr)stmt.getRightOp());
                                          }

                                          if (stmt.containsArrayRef()) {
                                             ArrayRef arx = stmt.getArrayRef();
                                             if (arx.getIndex() instanceof UntypedConstant) {
                                                ucx = (UntypedIntOrFloatConstant)arx.getIndex();
                                                arx.setIndex(ucx.toIntConstant());
                                             }

                                             if (stmt.getLeftOp() instanceof ArrayRef && stmt.getRightOp() instanceof UntypedConstant) {
                                                UntypedConstant uc = (UntypedConstant)stmt.getRightOp();
                                                Local baseLocal = (Local)stmt.getArrayRef().getBase();
                                                ArrayType lType = (ArrayType)DalvikTyper.this.localTyped.get(baseLocal);
                                                Type elemType = lType.getElementType();
                                                stmt.setRightOp(uc.defineType(elemType));
                                             }

                                          }
                                       }

                                       public void caseIdentityStmt(IdentityStmt stmt) {
                                       }

                                       public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
                                       }

                                       public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
                                       }

                                       public void caseGotoStmt(GotoStmt stmt) {
                                       }

                                       public void caseIfStmt(IfStmt stmt) {
                                          Value c = stmt.getCondition();
                                          if (c instanceof BinopExpr) {
                                             BinopExpr be = (BinopExpr)c;
                                             Value op1 = be.getOp1();
                                             Value op2 = be.getOp2();
                                             if (op1 instanceof UntypedConstant || op2 instanceof UntypedConstant) {
                                                Type t;
                                                UntypedConstant uc;
                                                if (op1 instanceof Local) {
                                                   t = (Type)DalvikTyper.this.localTyped.get(op1);
                                                   uc = (UntypedConstant)op2;
                                                   be.setOp2(uc.defineType(t));
                                                } else if (op2 instanceof Local) {
                                                   t = (Type)DalvikTyper.this.localTyped.get(op2);
                                                   uc = (UntypedConstant)op1;
                                                   be.setOp1(uc.defineType(t));
                                                } else if (op1 instanceof UntypedConstant && op2 instanceof UntypedConstant) {
                                                   if (op1 instanceof UntypedIntOrFloatConstant && op2 instanceof UntypedIntOrFloatConstant) {
                                                      UntypedIntOrFloatConstant uc1x = (UntypedIntOrFloatConstant)op1;
                                                      UntypedIntOrFloatConstant uc2 = (UntypedIntOrFloatConstant)op2;
                                                      be.setOp1(uc1x.toIntConstant());
                                                      be.setOp2(uc2.toIntConstant());
                                                   } else {
                                                      if (!(op1 instanceof UntypedLongOrDoubleConstant) || !(op2 instanceof UntypedLongOrDoubleConstant)) {
                                                         throw new RuntimeException("error: expected same type of untyped constants. Got " + stmt);
                                                      }

                                                      UntypedLongOrDoubleConstant uc1 = (UntypedLongOrDoubleConstant)op1;
                                                      UntypedLongOrDoubleConstant uc2x = (UntypedLongOrDoubleConstant)op2;
                                                      be.setOp1(uc1.toLongConstant());
                                                      be.setOp2(uc2x.toLongConstant());
                                                   }
                                                } else {
                                                   if (!(op1 instanceof UntypedConstant) && !(op2 instanceof UntypedConstant)) {
                                                      throw new RuntimeException("error: expected local/untyped untyped/local or untyped/untyped. Got " + stmt);
                                                   }

                                                   UntypedConstant ucx;
                                                   if (op1 instanceof UntypedConstant) {
                                                      ucx = (UntypedConstant)op1;
                                                      be.setOp1(ucx.defineType(op2.getType()));
                                                   } else if (op2 instanceof UntypedConstant) {
                                                      ucx = (UntypedConstant)op2;
                                                      be.setOp2(ucx.defineType(op1.getType()));
                                                   }
                                                }
                                             }
                                          } else if (!(c instanceof UnopExpr)) {
                                             throw new RuntimeException("error: expected binop or unop. Got " + stmt);
                                          }

                                       }

                                       public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
                                       }

                                       public void caseNopStmt(NopStmt stmt) {
                                       }

                                       public void caseRetStmt(RetStmt stmt) {
                                       }

                                       public void caseReturnStmt(ReturnStmt stmt) {
                                          if (stmt.getOp() instanceof UntypedConstant) {
                                             UntypedConstant uc = (UntypedConstant)stmt.getOp();
                                             Type type = b.getMethod().getReturnType();
                                             stmt.setOp(uc.defineType(type));
                                          }

                                       }

                                       public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
                                       }

                                       public void caseTableSwitchStmt(TableSwitchStmt stmt) {
                                       }

                                       public void caseThrowStmt(ThrowStmt stmt) {
                                       }

                                       public void defaultCase(Object obj) {
                                       }
                                    };
                                    u.apply(sw);
                                 }

                                 return;
                              }

                              c = (DalvikTyper.Constraint)var33.next();
                              l = c.l.getValue();
                              r = c.r.getValue();
                           } while(!(l instanceof Local));
                        } while(!(r instanceof Constant));

                        if (r instanceof UntypedIntOrFloatConstant) {
                           UntypedIntOrFloatConstant cst = (UntypedIntOrFloatConstant)r;
                           Value newValue = null;
                           if (cst.value != 0) {
                              newValue = cst.toIntConstant();
                           } else {
                              Iterator var62 = b.getUnits().iterator();

                              while(var62.hasNext()) {
                                 Unit u = (Unit)var62.next();
                                 Iterator var74 = u.getUseBoxes().iterator();

                                 while(var74.hasNext()) {
                                    ValueBox vb1 = (ValueBox)var74.next();
                                    Value v1 = vb1.getValue();
                                    if (v1 == l) {
                                       if (u instanceof AssignStmt) {
                                          AssignStmt a = (AssignStmt)u;
                                          right = a.getRightOp();
                                          if (right instanceof CastExpr) {
                                             newValue = NullConstant.v();
                                          } else {
                                             newValue = cst.toIntConstant();
                                          }
                                       } else if (u instanceof IfStmt) {
                                          newValue = cst.toIntConstant();
                                       }
                                    }
                                 }
                              }
                           }

                           if (newValue == null) {
                              throw new RuntimeException("error: no type found for local: " + l);
                           }

                           c.r.setValue((Value)newValue);
                        } else if (r instanceof UntypedLongOrDoubleConstant) {
                           Value newValue = ((UntypedLongOrDoubleConstant)r).toLongConstant();
                           c.r.setValue(newValue);
                        }
                     }
                  }
               }

               lo = (DalvikTyper.LocalObj)var33.next();
            } while(!vbList.contains(lo.vb));

            l = lo.getLocal();
            t = lo.t;
         } while(this.localTemp.contains(l) && lo.isUse);

         this.localTemp.add(l);
         this.typed.put(lo.vb, t);
      }
   }

   private void changeUntypedConstantsInInvoke(InvokeExpr invokeExpr) {
      for(int i = 0; i < invokeExpr.getArgCount(); ++i) {
         Value v = invokeExpr.getArg(i);
         if (v instanceof UntypedConstant) {
            Type t = invokeExpr.getMethodRef().parameterType(i);
            UntypedConstant uc = (UntypedConstant)v;
            invokeExpr.setArg(i, uc.defineType(t));
         }
      }

   }

   protected void checkExpr(Value v, Type t) {
      Iterator var3 = v.getUseBoxes().iterator();

      while(true) {
         while(true) {
            while(var3.hasNext()) {
               ValueBox vb = (ValueBox)var3.next();
               Value value = vb.getValue();
               if (value instanceof Local) {
                  if ((v instanceof ShrExpr || v instanceof ShlExpr || v instanceof UshrExpr) && ((BinopExpr)v).getOp2() == value) {
                     v().setType(vb, IntType.v(), true);
                  } else {
                     v().setType(vb, t, true);
                  }
               } else if (value instanceof UntypedConstant) {
                  UntypedConstant uc = (UntypedConstant)value;
                  if ((v instanceof ShrExpr || v instanceof ShlExpr || v instanceof UshrExpr) && ((BinopExpr)v).getOp2() == value) {
                     UntypedIntOrFloatConstant ui = (UntypedIntOrFloatConstant)uc;
                     vb.setValue(ui.toIntConstant());
                  } else {
                     vb.setValue(uc.defineType(t));
                  }
               }
            }

            return;
         }
      }
   }

   protected void setInvokeType(InvokeExpr invokeExpr) {
      for(int i = 0; i < invokeExpr.getArgCount(); ++i) {
         Value v = invokeExpr.getArg(i);
         if (v instanceof Local) {
            Type t = invokeExpr.getMethodRef().parameterType(i);
            v().setType(invokeExpr.getArgBox(i), t, true);
         }
      }

      if (!(invokeExpr instanceof StaticInvokeExpr)) {
         if (invokeExpr instanceof InstanceInvokeExpr) {
            InstanceInvokeExpr iie = (InstanceInvokeExpr)invokeExpr;
            v().setType(iie.getBaseBox(), RefType.v("java.lang.Object"), true);
         } else {
            if (!(invokeExpr instanceof DynamicInvokeExpr)) {
               throw new RuntimeException("error: unhandled invoke expression: " + invokeExpr + " " + invokeExpr.getClass());
            }

            DynamicInvokeExpr var6 = (DynamicInvokeExpr)invokeExpr;
         }
      }

   }

   private void setLocalTyped(Local l, Type t) {
      this.localTyped.put(l, t);
   }

   public void typeUntypedConstrantInDiv(final Body b) {
      Iterator var2 = b.getUnits().iterator();

      while(var2.hasNext()) {
         Unit u = (Unit)var2.next();
         StmtSwitch sw = new StmtSwitch() {
            public void caseBreakpointStmt(BreakpointStmt stmt) {
            }

            public void caseInvokeStmt(InvokeStmt stmt) {
               DalvikTyper.this.changeUntypedConstantsInInvoke(stmt.getInvokeExpr());
            }

            public void caseAssignStmt(AssignStmt stmt) {
               UntypedIntOrFloatConstant uc;
               if (stmt.getRightOp() instanceof NewArrayExpr) {
                  NewArrayExpr nae = (NewArrayExpr)stmt.getRightOp();
                  if (nae.getSize() instanceof UntypedConstant) {
                     uc = (UntypedIntOrFloatConstant)nae.getSize();
                     nae.setSize(uc.defineType(IntType.v()));
                  }
               } else if (stmt.getRightOp() instanceof InvokeExpr) {
                  DalvikTyper.this.changeUntypedConstantsInInvoke((InvokeExpr)stmt.getRightOp());
               } else if (stmt.getRightOp() instanceof CastExpr) {
                  CastExpr ce = (CastExpr)stmt.getRightOp();
                  if (ce.getOp() instanceof UntypedConstant) {
                     UntypedConstant ucx = (UntypedConstant)ce.getOp();
                     Iterator var4 = stmt.getTags().iterator();

                     while(var4.hasNext()) {
                        Tag tx = (Tag)var4.next();
                        if (tx instanceof IntOpTag) {
                           ce.setOp(ucx.defineType(IntType.v()));
                           return;
                        }

                        if (tx instanceof FloatOpTag) {
                           ce.setOp(ucx.defineType(FloatType.v()));
                           return;
                        }

                        if (tx instanceof DoubleOpTag) {
                           ce.setOp(ucx.defineType(DoubleType.v()));
                           return;
                        }

                        if (tx instanceof LongOpTag) {
                           ce.setOp(ucx.defineType(LongType.v()));
                           return;
                        }
                     }

                     ce.setOp(ucx.defineType(RefType.v("java.lang.Object")));
                  }
               }

               if (stmt.containsArrayRef()) {
                  ArrayRef ar = stmt.getArrayRef();
                  if (ar.getIndex() instanceof UntypedConstant) {
                     uc = (UntypedIntOrFloatConstant)ar.getIndex();
                     ar.setIndex(uc.toIntConstant());
                  }
               }

               Value r = stmt.getRightOp();
               if (r instanceof DivExpr || r instanceof RemExpr) {
                  Iterator var10 = stmt.getTags().iterator();

                  while(var10.hasNext()) {
                     Tag t = (Tag)var10.next();
                     if (t instanceof IntOpTag) {
                        DalvikTyper.this.checkExpr(r, IntType.v());
                        return;
                     }

                     if (t instanceof FloatOpTag) {
                        DalvikTyper.this.checkExpr(r, FloatType.v());
                        return;
                     }

                     if (t instanceof DoubleOpTag) {
                        DalvikTyper.this.checkExpr(r, DoubleType.v());
                        return;
                     }

                     if (t instanceof LongOpTag) {
                        DalvikTyper.this.checkExpr(r, LongType.v());
                        return;
                     }
                  }
               }

            }

            public void caseIdentityStmt(IdentityStmt stmt) {
            }

            public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
            }

            public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
            }

            public void caseGotoStmt(GotoStmt stmt) {
            }

            public void caseIfStmt(IfStmt stmt) {
            }

            public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
            }

            public void caseNopStmt(NopStmt stmt) {
            }

            public void caseRetStmt(RetStmt stmt) {
            }

            public void caseReturnStmt(ReturnStmt stmt) {
               if (stmt.getOp() instanceof UntypedConstant) {
                  UntypedConstant uc = (UntypedConstant)stmt.getOp();
                  Type type = b.getMethod().getReturnType();
                  stmt.setOp(uc.defineType(type));
               }

            }

            public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
            }

            public void caseTableSwitchStmt(TableSwitchStmt stmt) {
            }

            public void caseThrowStmt(ThrowStmt stmt) {
               if (stmt.getOp() instanceof UntypedConstant) {
                  UntypedConstant uc = (UntypedConstant)stmt.getOp();
                  stmt.setOp(uc.defineType(RefType.v("java.lang.Object")));
               }

            }

            public void defaultCase(Object obj) {
            }
         };
         u.apply(sw);
      }

   }

   class Constraint {
      ValueBox l;
      ValueBox r;

      public Constraint(ValueBox l, ValueBox r) {
         this.l = l;
         this.r = r;
      }

      public String toString() {
         return this.l + " < " + this.r;
      }
   }

   class LocalObj {
      ValueBox vb;
      Type t;
      boolean isUse;

      public LocalObj(ValueBox vb, Type t, boolean isUse) {
         this.vb = vb;
         this.t = t;
         this.isUse = isUse;
      }

      public Local getLocal() {
         return (Local)this.vb.getValue();
      }
   }
}
