package soot.jimple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.AbstractJasminClass;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.IntegerType;
import soot.Local;
import soot.LongType;
import soot.Modifier;
import soot.NullType;
import soot.RefType;
import soot.ShortType;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.StmtAddressType;
import soot.Timers;
import soot.Trap;
import soot.Type;
import soot.TypeSwitch;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.grimp.AbstractGrimpValueSwitch;
import soot.grimp.NewInvokeExpr;
import soot.jimple.internal.StmtBox;
import soot.options.Options;
import soot.tagkit.LineNumberTag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FastColorer;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
import soot.util.Chain;

public class JasminClass extends AbstractJasminClass {
   private static final Logger logger = LoggerFactory.getLogger(JasminClass.class);
   Value plusPlusValue;
   Local plusPlusHolder;
   int plusPlusState;
   int plusPlusPlace;
   int plusPlusHeight;
   Stmt plusPlusIncrementer;

   void emit(String s, int stackChange) {
      this.modifyStackHeight(stackChange);
      this.okayEmit(s);
   }

   void modifyStackHeight(int stackChange) {
      if (this.currentStackHeight > this.maxStackHeight) {
         this.maxStackHeight = this.currentStackHeight;
      }

      this.currentStackHeight += stackChange;
      if (this.currentStackHeight < 0) {
         throw new RuntimeException("Stack height is negative!");
      } else {
         if (this.currentStackHeight > this.maxStackHeight) {
            this.maxStackHeight = this.currentStackHeight;
         }

      }
   }

   public JasminClass(SootClass sootClass) {
      super(sootClass);
   }

   protected void assignColorsToLocals(Body body) {
      super.assignColorsToLocals(body);
      FastColorer.assignColorsToLocals(body, this.localToGroup, this.localToColor, this.groupToColorCount);
      if (Options.v().time()) {
         Timers.v().packTimer.end();
      }

   }

   protected void emitMethodBody(SootMethod method) {
      if (Options.v().time()) {
         Timers.v().buildJasminTimer.end();
      }

      Body activeBody = method.getActiveBody();
      if (!(activeBody instanceof StmtBody)) {
         throw new RuntimeException("method: " + method.getName() + " has an invalid active body!");
      } else {
         StmtBody body = (StmtBody)activeBody;
         body.validate();
         if (Options.v().time()) {
            Timers.v().buildJasminTimer.start();
         }

         Chain<Unit> units = body.getUnits();
         ExceptionalUnitGraph stmtGraph = null;
         LocalDefs ld = null;
         LocalUses lu = null;
         if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "] Performing peephole optimizations...");
         }

         boolean disablePeephole = true;
         if (!disablePeephole) {
            stmtGraph = new ExceptionalUnitGraph(body);
            ld = LocalDefs.Factory.newLocalDefs((UnitGraph)stmtGraph);
            lu = LocalUses.Factory.newLocalUses((Body)body, ld);
         }

         int stackLimitIndex = -1;
         this.subroutineToReturnAddressSlot = new HashMap(10, 0.7F);
         this.unitToLabel = new HashMap(units.size() * 2 + 1, 0.7F);
         this.labelCount = 0;
         Iterator codeIt = body.getUnitBoxes(true).iterator();

         while(codeIt.hasNext()) {
            UnitBox ubox = (UnitBox)codeIt.next();
            StmtBox box = (StmtBox)ubox;
            if (!this.unitToLabel.containsKey(box.getUnit())) {
               this.unitToLabel.put(box.getUnit(), "label" + this.labelCount++);
            }
         }

         codeIt = body.getTraps().iterator();

         while(codeIt.hasNext()) {
            Trap trap = (Trap)codeIt.next();
            if (trap.getBeginUnit() != trap.getEndUnit()) {
               this.emit(".catch " + slashify(trap.getException().getName()) + " from " + (String)this.unitToLabel.get(trap.getBeginUnit()) + " to " + (String)this.unitToLabel.get(trap.getEndUnit()) + " using " + (String)this.unitToLabel.get(trap.getHandlerUnit()));
            }
         }

         int localCount = 0;
         int[] paramSlots = new int[method.getParameterCount()];
         int thisSlot = 0;
         Set<Local> assignedLocals = new HashSet();
         Map<GroupIntPair, Integer> groupColorPairToSlot = new HashMap(body.getLocalCount() * 2 + 1, 0.7F);
         this.localToSlot = new HashMap(body.getLocalCount() * 2 + 1, 0.7F);
         this.assignColorsToLocals(body);
         List<Type> paramTypes = method.getParameterTypes();
         if (!method.isStatic()) {
            thisSlot = 0;
            ++localCount;
         }

         for(int i = 0; i < paramTypes.size(); ++i) {
            paramSlots[i] = localCount;
            localCount += sizeOfType((Type)paramTypes.get(i));
         }

         Iterator var33 = units.iterator();

         while(true) {
            Local l;
            int slot;
            while(true) {
               Stmt nextNextStmt;
               boolean found;
               do {
                  do {
                     if (!var33.hasNext()) {
                        var33 = body.getLocals().iterator();

                        while(var33.hasNext()) {
                           Local local = (Local)var33.next();
                           if (!assignedLocals.contains(local)) {
                              GroupIntPair pair = new GroupIntPair(this.localToGroup.get(local), (Integer)this.localToColor.get(local));
                              int slot;
                              if (groupColorPairToSlot.containsKey(pair)) {
                                 slot = (Integer)groupColorPairToSlot.get(pair);
                              } else {
                                 slot = localCount;
                                 localCount += sizeOfType(local.getType());
                                 groupColorPairToSlot.put(pair, new Integer(slot));
                              }

                              this.localToSlot.put(local, new Integer(slot));
                              assignedLocals.add(local);
                           }
                        }

                        if (!Modifier.isNative(method.getModifiers()) && !Modifier.isAbstract(method.getModifiers())) {
                           this.emit("    .limit stack ?");
                           stackLimitIndex = this.code.size() - 1;
                           this.emit("    .limit locals " + localCount);
                        }

                        codeIt = units.iterator();
                        this.isEmittingMethodCode = true;
                        this.maxStackHeight = 0;
                        this.isNextGotoAJsr = false;

                        while(codeIt.hasNext()) {
                           Stmt s = (Stmt)codeIt.next();
                           if (this.unitToLabel.containsKey(s)) {
                              this.emit((String)this.unitToLabel.get(s) + ":");
                           }

                           if (this.subroutineToReturnAddressSlot.containsKey(s)) {
                              AssignStmt assignStmt = (AssignStmt)s;
                              this.modifyStackHeight(1);
                              int slot = (Integer)this.localToSlot.get(assignStmt.getLeftOp());
                              if (slot >= 0 && slot <= 3) {
                                 this.emit("astore_" + slot, -1);
                              } else {
                                 this.emit("astore " + slot, -1);
                              }
                           }

                           boolean contFlag = false;
                           if (!disablePeephole && s instanceof AssignStmt) {
                              AssignStmt stmt = (AssignStmt)s;
                              if (codeIt.hasNext()) {
                                 Stmt ns = (Stmt)((Stmt)stmtGraph.getSuccsOf(stmt).get(0));
                                 if (ns instanceof AssignStmt) {
                                    AssignStmt nextStmt = (AssignStmt)ns;
                                    List<Unit> l = stmtGraph.getSuccsOf(nextStmt);
                                    if (l.size() == 1) {
                                       nextNextStmt = (Stmt)((Stmt)l.get(0));
                                       Value lvalue = stmt.getLeftOp();
                                       Value rvalue = stmt.getRightOp();
                                       if (lvalue instanceof Local && lvalue instanceof Local && nextStmt.getLeftOp().equivTo(rvalue) && nextStmt.getRightOp() instanceof AddExpr) {
                                          found = false;
                                          Iterator var43 = nextNextStmt.getUseBoxes().iterator();

                                          ValueBox box;
                                          while(var43.hasNext()) {
                                             box = (ValueBox)var43.next();
                                             if (box.getValue() == lvalue) {
                                                if (found) {
                                                   found = false;
                                                   break;
                                                }

                                                found = true;
                                             }
                                          }

                                          if (found) {
                                             found = false;
                                             var43 = nextNextStmt.getDefBoxes().iterator();

                                             while(var43.hasNext()) {
                                                box = (ValueBox)var43.next();
                                                if (box.getValue().equivTo(rvalue)) {
                                                   found = true;
                                                }
                                             }

                                             if (!found) {
                                                AddExpr addexp = (AddExpr)nextStmt.getRightOp();
                                                if (addexp.getOp1().equivTo(lvalue) || addexp.getOp1().equivTo(rvalue)) {
                                                   Value added = addexp.getOp2();
                                                   if (added instanceof IntConstant && ((IntConstant)((IntConstant)added)).value == 1) {
                                                      label297: {
                                                         if (addexp.getOp1().equivTo(lvalue)) {
                                                            if (lu.getUsesOf(stmt).size() != 2 || ld.getDefsOfAt((Local)lvalue, nextStmt).size() != 1 || ld.getDefsOfAt((Local)lvalue, nextNextStmt).size() != 1) {
                                                               break label297;
                                                            }

                                                            this.plusPlusState = 0;
                                                         } else {
                                                            if (lu.getUsesOf(stmt).size() != 1 || ld.getDefsOfAt((Local)lvalue, nextNextStmt).size() != 1) {
                                                               break label297;
                                                            }

                                                            this.plusPlusState = 10;
                                                         }

                                                         if (lvalue.getType() == IntType.v()) {
                                                            this.currentStackHeight = 0;
                                                            this.plusPlusValue = rvalue;
                                                            this.plusPlusHolder = (Local)lvalue;
                                                            this.plusPlusIncrementer = nextStmt;
                                                            this.emitStmt(nextNextStmt);
                                                            if (this.plusPlusHolder != null) {
                                                               this.emitStmt(stmt);
                                                               this.emitStmt(nextStmt);
                                                            }

                                                            if (this.currentStackHeight != 0) {
                                                               throw new RuntimeException("Stack has height " + this.currentStackHeight + " after execution of stmt: " + s);
                                                            }

                                                            contFlag = true;
                                                            codeIt.next();
                                                            codeIt.next();
                                                         }
                                                      }
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }

                           if (!contFlag) {
                              this.currentStackHeight = 0;
                              this.emitStmt(s);
                              if (this.currentStackHeight != 0) {
                                 throw new RuntimeException("Stack has height " + this.currentStackHeight + " after execution of stmt: " + s);
                              }
                           }
                        }

                        this.isEmittingMethodCode = false;
                        if (!Modifier.isNative(method.getModifiers()) && !Modifier.isAbstract(method.getModifiers())) {
                           this.code.set(stackLimitIndex, "    .limit stack " + this.maxStackHeight);
                        }

                        return;
                     }

                     Unit u = (Unit)var33.next();
                     nextNextStmt = (Stmt)u;
                  } while(!(nextNextStmt instanceof IdentityStmt));
               } while(!(((IdentityStmt)nextNextStmt).getLeftOp() instanceof Local));

               l = (Local)((IdentityStmt)nextNextStmt).getLeftOp();
               IdentityRef identity = (IdentityRef)((IdentityStmt)nextNextStmt).getRightOp();
               found = false;
               if (identity instanceof ThisRef) {
                  if (method.isStatic()) {
                     throw new RuntimeException("Attempting to use 'this' in static method");
                  }

                  slot = thisSlot;
                  break;
               }

               if (identity instanceof ParameterRef) {
                  slot = paramSlots[((ParameterRef)identity).getIndex()];
                  break;
               }
            }

            GroupIntPair pair = new GroupIntPair(this.localToGroup.get(l), (Integer)this.localToColor.get(l));
            groupColorPairToSlot.put(pair, new Integer(slot));
            this.localToSlot.put(l, new Integer(slot));
            assignedLocals.add(l);
         }
      }
   }

   void emitAssignStmt(AssignStmt stmt) {
      Value lvalue = stmt.getLeftOp();
      final Value rvalue = stmt.getRightOp();
      if (lvalue instanceof Local && (rvalue instanceof AddExpr || rvalue instanceof SubExpr)) {
         Local l = (Local)lvalue;
         BinopExpr expr = (BinopExpr)rvalue;
         Value op1 = expr.getOp1();
         Value op2 = expr.getOp2();
         if (lvalue == this.plusPlusHolder) {
            this.emitValue(lvalue);
            this.plusPlusHolder = null;
            this.plusPlusState = 0;
         }

         if (l.getType().equals(IntType.v())) {
            boolean isValidCase = false;
            int x = 0;
            if (op1 == l && op2 instanceof IntConstant) {
               x = ((IntConstant)op2).value;
               isValidCase = true;
            } else if (expr instanceof AddExpr && op2 == l && op1 instanceof IntConstant) {
               x = ((IntConstant)op1).value;
               isValidCase = true;
            }

            if (isValidCase && x >= -32768 && x <= 32767) {
               this.emit("iinc " + (Integer)this.localToSlot.get(l) + " " + (expr instanceof AddExpr ? x : -x), 0);
               return;
            }
         }
      }

      lvalue.apply(new AbstractJimpleValueSwitch() {
         public void caseArrayRef(ArrayRef v) {
            JasminClass.this.emitValue(v.getBase());
            JasminClass.this.emitValue(v.getIndex());
            JasminClass.this.emitValue(rvalue);
            v.getType().apply(new TypeSwitch() {
               public void caseArrayType(ArrayType t) {
                  JasminClass.this.emit("aastore", -3);
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dastore", -4);
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fastore", -3);
               }

               public void caseIntType(IntType t) {
                  JasminClass.this.emit("iastore", -3);
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lastore", -4);
               }

               public void caseRefType(RefType t) {
                  JasminClass.this.emit("aastore", -3);
               }

               public void caseByteType(ByteType t) {
                  JasminClass.this.emit("bastore", -3);
               }

               public void caseBooleanType(BooleanType t) {
                  JasminClass.this.emit("bastore", -3);
               }

               public void caseCharType(CharType t) {
                  JasminClass.this.emit("castore", -3);
               }

               public void caseShortType(ShortType t) {
                  JasminClass.this.emit("sastore", -3);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid type: " + t);
               }
            });
         }

         public void caseInstanceFieldRef(InstanceFieldRef v) {
            JasminClass.this.emitValue(v.getBase());
            JasminClass.this.emitValue(rvalue);
            JasminClass.this.emit("putfield " + AbstractJasminClass.slashify(v.getFieldRef().declaringClass().getName()) + "/" + v.getFieldRef().name() + " " + AbstractJasminClass.jasminDescriptorOf(v.getFieldRef().type()), -1 + -AbstractJasminClass.sizeOfType(v.getFieldRef().type()));
         }

         public void caseLocal(Local v) {
            final int slot = (Integer)JasminClass.this.localToSlot.get(v);
            v.getType().apply(new TypeSwitch() {
               private void handleIntegerType(IntegerType t) {
                  JasminClass.this.emitValue(rvalue);
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("istore_" + slot, -1);
                  } else {
                     JasminClass.this.emit("istore " + slot, -1);
                  }

               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntegerType(t);
               }

               public void caseByteType(ByteType t) {
                  this.handleIntegerType(t);
               }

               public void caseShortType(ShortType t) {
                  this.handleIntegerType(t);
               }

               public void caseCharType(CharType t) {
                  this.handleIntegerType(t);
               }

               public void caseIntType(IntType t) {
                  this.handleIntegerType(t);
               }

               public void caseArrayType(ArrayType t) {
                  JasminClass.this.emitValue(rvalue);
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("astore_" + slot, -1);
                  } else {
                     JasminClass.this.emit("astore " + slot, -1);
                  }

               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emitValue(rvalue);
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("dstore_" + slot, -2);
                  } else {
                     JasminClass.this.emit("dstore " + slot, -2);
                  }

               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emitValue(rvalue);
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("fstore_" + slot, -1);
                  } else {
                     JasminClass.this.emit("fstore " + slot, -1);
                  }

               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emitValue(rvalue);
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("lstore_" + slot, -2);
                  } else {
                     JasminClass.this.emit("lstore " + slot, -2);
                  }

               }

               public void caseRefType(RefType t) {
                  JasminClass.this.emitValue(rvalue);
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("astore_" + slot, -1);
                  } else {
                     JasminClass.this.emit("astore " + slot, -1);
                  }

               }

               public void caseStmtAddressType(StmtAddressType t) {
                  JasminClass.this.isNextGotoAJsr = true;
                  JasminClass.this.returnAddressSlot = slot;
               }

               public void caseNullType(NullType t) {
                  JasminClass.this.emitValue(rvalue);
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("astore_" + slot, -1);
                  } else {
                     JasminClass.this.emit("astore " + slot, -1);
                  }

               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid local type: " + t);
               }
            });
         }

         public void caseStaticFieldRef(StaticFieldRef v) {
            SootFieldRef field = v.getFieldRef();
            JasminClass.this.emitValue(rvalue);
            JasminClass.this.emit("putstatic " + AbstractJasminClass.slashify(field.declaringClass().getName()) + "/" + field.name() + " " + AbstractJasminClass.jasminDescriptorOf(field.type()), -AbstractJasminClass.sizeOfType(v.getFieldRef().type()));
         }
      });
   }

   void emitIfStmt(IfStmt stmt) {
      Value cond = stmt.getCondition();
      final Value op1 = ((BinopExpr)cond).getOp1();
      Value op2 = ((BinopExpr)cond).getOp2();
      final String label = (String)this.unitToLabel.get(stmt.getTarget());
      if (!(op2 instanceof NullConstant) && !(op1 instanceof NullConstant)) {
         if (op2 instanceof IntConstant && ((IntConstant)op2).value == 0) {
            this.emitValue(op1);
            cond.apply(new AbstractJimpleValueSwitch() {
               public void caseEqExpr(EqExpr expr) {
                  JasminClass.this.emit("ifeq " + label, -1);
               }

               public void caseNeExpr(NeExpr expr) {
                  JasminClass.this.emit("ifne " + label, -1);
               }

               public void caseLtExpr(LtExpr expr) {
                  JasminClass.this.emit("iflt " + label, -1);
               }

               public void caseLeExpr(LeExpr expr) {
                  JasminClass.this.emit("ifle " + label, -1);
               }

               public void caseGtExpr(GtExpr expr) {
                  JasminClass.this.emit("ifgt " + label, -1);
               }

               public void caseGeExpr(GeExpr expr) {
                  JasminClass.this.emit("ifge " + label, -1);
               }
            });
         } else if (op1 instanceof IntConstant && ((IntConstant)op1).value == 0) {
            this.emitValue(op2);
            cond.apply(new AbstractJimpleValueSwitch() {
               public void caseEqExpr(EqExpr expr) {
                  JasminClass.this.emit("ifeq " + label, -1);
               }

               public void caseNeExpr(NeExpr expr) {
                  JasminClass.this.emit("ifne " + label, -1);
               }

               public void caseLtExpr(LtExpr expr) {
                  JasminClass.this.emit("ifgt " + label, -1);
               }

               public void caseLeExpr(LeExpr expr) {
                  JasminClass.this.emit("ifge " + label, -1);
               }

               public void caseGtExpr(GtExpr expr) {
                  JasminClass.this.emit("iflt " + label, -1);
               }

               public void caseGeExpr(GeExpr expr) {
                  JasminClass.this.emit("ifle " + label, -1);
               }
            });
         } else {
            this.emitValue(op1);
            this.emitValue(op2);
            cond.apply(new AbstractJimpleValueSwitch() {
               public void caseEqExpr(EqExpr expr) {
                  op1.getType().apply(new TypeSwitch() {
                     public void caseIntType(IntType t) {
                        JasminClass.this.emit("if_icmpeq " + label, -2);
                     }

                     public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("if_icmpeq " + label, -2);
                     }

                     public void caseShortType(ShortType t) {
                        JasminClass.this.emit("if_icmpeq " + label, -2);
                     }

                     public void caseCharType(CharType t) {
                        JasminClass.this.emit("if_icmpeq " + label, -2);
                     }

                     public void caseByteType(ByteType t) {
                        JasminClass.this.emit("if_icmpeq " + label, -2);
                     }

                     public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg", -3);
                        JasminClass.this.emit("ifeq " + label, -1);
                     }

                     public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp", -3);
                        JasminClass.this.emit("ifeq " + label, -1);
                     }

                     public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg", -1);
                        JasminClass.this.emit("ifeq " + label, -1);
                     }

                     public void caseArrayType(ArrayType t) {
                        JasminClass.this.emit("if_acmpeq " + label, -2);
                     }

                     public void caseRefType(RefType t) {
                        JasminClass.this.emit("if_acmpeq " + label, -2);
                     }

                     public void caseNullType(NullType t) {
                        JasminClass.this.emit("if_acmpeq " + label, -2);
                     }

                     public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                     }
                  });
               }

               public void caseNeExpr(NeExpr expr) {
                  op1.getType().apply(new TypeSwitch() {
                     public void caseIntType(IntType t) {
                        JasminClass.this.emit("if_icmpne " + label, -2);
                     }

                     public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("if_icmpne " + label, -2);
                     }

                     public void caseShortType(ShortType t) {
                        JasminClass.this.emit("if_icmpne " + label, -2);
                     }

                     public void caseCharType(CharType t) {
                        JasminClass.this.emit("if_icmpne " + label, -2);
                     }

                     public void caseByteType(ByteType t) {
                        JasminClass.this.emit("if_icmpne " + label, -2);
                     }

                     public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg", -3);
                        JasminClass.this.emit("ifne " + label, -1);
                     }

                     public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp", -3);
                        JasminClass.this.emit("ifne " + label, -1);
                     }

                     public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg", -1);
                        JasminClass.this.emit("ifne " + label, -1);
                     }

                     public void caseArrayType(ArrayType t) {
                        JasminClass.this.emit("if_acmpne " + label, -2);
                     }

                     public void caseRefType(RefType t) {
                        JasminClass.this.emit("if_acmpne " + label, -2);
                     }

                     public void caseNullType(NullType t) {
                        JasminClass.this.emit("if_acmpne " + label, -2);
                     }

                     public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type for NeExpr: " + t);
                     }
                  });
               }

               public void caseLtExpr(LtExpr expr) {
                  op1.getType().apply(new TypeSwitch() {
                     public void caseIntType(IntType t) {
                        JasminClass.this.emit("if_icmplt " + label, -2);
                     }

                     public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("if_icmplt " + label, -2);
                     }

                     public void caseShortType(ShortType t) {
                        JasminClass.this.emit("if_icmplt " + label, -2);
                     }

                     public void caseCharType(CharType t) {
                        JasminClass.this.emit("if_icmplt " + label, -2);
                     }

                     public void caseByteType(ByteType t) {
                        JasminClass.this.emit("if_icmplt " + label, -2);
                     }

                     public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg", -3);
                        JasminClass.this.emit("iflt " + label, -1);
                     }

                     public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp", -3);
                        JasminClass.this.emit("iflt " + label, -1);
                     }

                     public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg", -1);
                        JasminClass.this.emit("iflt " + label, -1);
                     }

                     public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                     }
                  });
               }

               public void caseLeExpr(LeExpr expr) {
                  op1.getType().apply(new TypeSwitch() {
                     public void caseIntType(IntType t) {
                        JasminClass.this.emit("if_icmple " + label, -2);
                     }

                     public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("if_icmple " + label, -2);
                     }

                     public void caseShortType(ShortType t) {
                        JasminClass.this.emit("if_icmple " + label, -2);
                     }

                     public void caseCharType(CharType t) {
                        JasminClass.this.emit("if_icmple " + label, -2);
                     }

                     public void caseByteType(ByteType t) {
                        JasminClass.this.emit("if_icmple " + label, -2);
                     }

                     public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg", -3);
                        JasminClass.this.emit("ifle " + label, -1);
                     }

                     public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp", -3);
                        JasminClass.this.emit("ifle " + label, -1);
                     }

                     public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg", -1);
                        JasminClass.this.emit("ifle " + label, -1);
                     }

                     public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                     }
                  });
               }

               public void caseGtExpr(GtExpr expr) {
                  op1.getType().apply(new TypeSwitch() {
                     public void caseIntType(IntType t) {
                        JasminClass.this.emit("if_icmpgt " + label, -2);
                     }

                     public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("if_icmpgt " + label, -2);
                     }

                     public void caseShortType(ShortType t) {
                        JasminClass.this.emit("if_icmpgt " + label, -2);
                     }

                     public void caseCharType(CharType t) {
                        JasminClass.this.emit("if_icmpgt " + label, -2);
                     }

                     public void caseByteType(ByteType t) {
                        JasminClass.this.emit("if_icmpgt " + label, -2);
                     }

                     public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg", -3);
                        JasminClass.this.emit("ifgt " + label, -1);
                     }

                     public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp", -3);
                        JasminClass.this.emit("ifgt " + label, -1);
                     }

                     public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg", -1);
                        JasminClass.this.emit("ifgt " + label, -1);
                     }

                     public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                     }
                  });
               }

               public void caseGeExpr(GeExpr expr) {
                  op1.getType().apply(new TypeSwitch() {
                     public void caseIntType(IntType t) {
                        JasminClass.this.emit("if_icmpge " + label, -2);
                     }

                     public void caseBooleanType(BooleanType t) {
                        JasminClass.this.emit("if_icmpge " + label, -2);
                     }

                     public void caseShortType(ShortType t) {
                        JasminClass.this.emit("if_icmpge " + label, -2);
                     }

                     public void caseCharType(CharType t) {
                        JasminClass.this.emit("if_icmpge " + label, -2);
                     }

                     public void caseByteType(ByteType t) {
                        JasminClass.this.emit("if_icmpge " + label, -2);
                     }

                     public void caseDoubleType(DoubleType t) {
                        JasminClass.this.emit("dcmpg", -3);
                        JasminClass.this.emit("ifge " + label, -1);
                     }

                     public void caseLongType(LongType t) {
                        JasminClass.this.emit("lcmp", -3);
                        JasminClass.this.emit("ifge " + label, -1);
                     }

                     public void caseFloatType(FloatType t) {
                        JasminClass.this.emit("fcmpg", -1);
                        JasminClass.this.emit("ifge " + label, -1);
                     }

                     public void defaultCase(Type t) {
                        throw new RuntimeException("invalid type");
                     }
                  });
               }
            });
         }
      } else {
         if (op2 instanceof NullConstant) {
            this.emitValue(op1);
         } else {
            this.emitValue(op2);
         }

         if (cond instanceof EqExpr) {
            this.emit("ifnull " + label, -1);
         } else {
            if (!(cond instanceof NeExpr)) {
               throw new RuntimeException("invalid condition");
            }

            this.emit("ifnonnull " + label, -1);
         }

      }
   }

   void emitStmt(Stmt stmt) {
      LineNumberTag lnTag = (LineNumberTag)stmt.getTag("LineNumberTag");
      if (lnTag != null) {
         this.emit(".line " + lnTag.getLineNumber());
      }

      stmt.apply(new AbstractStmtSwitch() {
         public void caseAssignStmt(AssignStmt s) {
            JasminClass.this.emitAssignStmt(s);
         }

         public void caseIdentityStmt(IdentityStmt s) {
            if (s.getRightOp() instanceof CaughtExceptionRef && s.getLeftOp() instanceof Local) {
               int slot = (Integer)JasminClass.this.localToSlot.get(s.getLeftOp());
               JasminClass.this.modifyStackHeight(1);
               if (slot >= 0 && slot <= 3) {
                  JasminClass.this.emit("astore_" + slot, -1);
               } else {
                  JasminClass.this.emit("astore " + slot, -1);
               }
            }

         }

         public void caseBreakpointStmt(BreakpointStmt s) {
            JasminClass.this.emit("breakpoint", 0);
         }

         public void caseInvokeStmt(InvokeStmt s) {
            JasminClass.this.emitValue(s.getInvokeExpr());
            Type returnType = s.getInvokeExpr().getMethodRef().returnType();
            if (!returnType.equals(VoidType.v())) {
               if (AbstractJasminClass.sizeOfType(returnType) == 1) {
                  JasminClass.this.emit("pop", -1);
               } else {
                  JasminClass.this.emit("pop2", -2);
               }
            }

         }

         public void caseEnterMonitorStmt(EnterMonitorStmt s) {
            JasminClass.this.emitValue(s.getOp());
            JasminClass.this.emit("monitorenter", -1);
         }

         public void caseExitMonitorStmt(ExitMonitorStmt s) {
            JasminClass.this.emitValue(s.getOp());
            JasminClass.this.emit("monitorexit", -1);
         }

         public void caseGotoStmt(GotoStmt s) {
            if (JasminClass.this.isNextGotoAJsr) {
               JasminClass.this.emit("jsr " + (String)JasminClass.this.unitToLabel.get(s.getTarget()));
               JasminClass.this.isNextGotoAJsr = false;
               JasminClass.this.subroutineToReturnAddressSlot.put(s.getTarget(), new Integer(JasminClass.this.returnAddressSlot));
            } else {
               JasminClass.this.emit("goto " + (String)JasminClass.this.unitToLabel.get(s.getTarget()));
            }

         }

         public void caseIfStmt(IfStmt s) {
            JasminClass.this.emitIfStmt(s);
         }

         public void caseLookupSwitchStmt(LookupSwitchStmt s) {
            JasminClass.this.emitValue(s.getKey());
            JasminClass.this.emit("lookupswitch", -1);
            List<IntConstant> lookupValues = s.getLookupValues();
            List<Unit> targets = s.getTargets();

            for(int i = 0; i < lookupValues.size(); ++i) {
               JasminClass.this.emit("  " + lookupValues.get(i) + " : " + (String)JasminClass.this.unitToLabel.get(targets.get(i)));
            }

            JasminClass.this.emit("  default : " + (String)JasminClass.this.unitToLabel.get(s.getDefaultTarget()));
         }

         public void caseNopStmt(NopStmt s) {
            JasminClass.this.emit("nop", 0);
         }

         public void caseRetStmt(RetStmt s) {
            JasminClass.this.emit("ret " + JasminClass.this.localToSlot.get(s.getStmtAddress()), 0);
         }

         public void caseReturnStmt(ReturnStmt s) {
            JasminClass.this.emitValue(s.getOp());
            Value returnValue = s.getOp();
            returnValue.getType().apply(new TypeSwitch() {
               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid return type " + t.toString());
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dreturn", -2);
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("freturn", -1);
               }

               public void caseIntType(IntType t) {
                  JasminClass.this.emit("ireturn", -1);
               }

               public void caseByteType(ByteType t) {
                  JasminClass.this.emit("ireturn", -1);
               }

               public void caseShortType(ShortType t) {
                  JasminClass.this.emit("ireturn", -1);
               }

               public void caseCharType(CharType t) {
                  JasminClass.this.emit("ireturn", -1);
               }

               public void caseBooleanType(BooleanType t) {
                  JasminClass.this.emit("ireturn", -1);
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lreturn", -2);
               }

               public void caseArrayType(ArrayType t) {
                  JasminClass.this.emit("areturn", -1);
               }

               public void caseRefType(RefType t) {
                  JasminClass.this.emit("areturn", -1);
               }

               public void caseNullType(NullType t) {
                  JasminClass.this.emit("areturn", -1);
               }
            });
         }

         public void caseReturnVoidStmt(ReturnVoidStmt s) {
            JasminClass.this.emit("return", 0);
         }

         public void caseTableSwitchStmt(TableSwitchStmt s) {
            JasminClass.this.emitValue(s.getKey());
            JasminClass.this.emit("tableswitch " + s.getLowIndex() + " ; high = " + s.getHighIndex(), -1);
            List<Unit> targets = s.getTargets();

            for(int i = 0; i < targets.size(); ++i) {
               JasminClass.this.emit("  " + (String)JasminClass.this.unitToLabel.get(targets.get(i)));
            }

            JasminClass.this.emit("default : " + (String)JasminClass.this.unitToLabel.get(s.getDefaultTarget()));
         }

         public void caseThrowStmt(ThrowStmt s) {
            JasminClass.this.emitValue(s.getOp());
            JasminClass.this.emit("athrow", -1);
         }
      });
   }

   void emitLocal(final Local v) {
      final int slot = (Integer)this.localToSlot.get(v);
      v.getType().apply(new TypeSwitch() {
         public void caseArrayType(ArrayType t) {
            if (slot >= 0 && slot <= 3) {
               JasminClass.this.emit("aload_" + slot, 1);
            } else {
               JasminClass.this.emit("aload " + slot, 1);
            }

         }

         public void defaultCase(Type t) {
            throw new RuntimeException("invalid local type to load" + t);
         }

         public void caseDoubleType(DoubleType t) {
            if (slot >= 0 && slot <= 3) {
               JasminClass.this.emit("dload_" + slot, 2);
            } else {
               JasminClass.this.emit("dload " + slot, 2);
            }

         }

         public void caseFloatType(FloatType t) {
            if (slot >= 0 && slot <= 3) {
               JasminClass.this.emit("fload_" + slot, 1);
            } else {
               JasminClass.this.emit("fload " + slot, 1);
            }

         }

         public void caseBooleanType(BooleanType t) {
            this.handleIntegerType(t);
         }

         public void caseByteType(ByteType t) {
            this.handleIntegerType(t);
         }

         public void caseShortType(ShortType t) {
            this.handleIntegerType(t);
         }

         public void caseCharType(CharType t) {
            this.handleIntegerType(t);
         }

         public void caseIntType(IntType t) {
            this.handleIntegerType(t);
         }

         public void handleIntegerType(IntegerType t) {
            if (v.equals(JasminClass.this.plusPlusHolder)) {
               int diff;
               switch(JasminClass.this.plusPlusState) {
               case 0:
                  JasminClass.this.plusPlusState = 1;
                  JasminClass.this.emitStmt(JasminClass.this.plusPlusIncrementer);
                  diff = JasminClass.this.plusPlusHeight - JasminClass.this.currentStackHeight + 1;
                  if (diff > 0) {
                     JasminClass.this.code.set(JasminClass.this.plusPlusPlace, "    dup_x" + diff);
                  }

                  JasminClass.this.plusPlusHolder = null;
                  return;
               case 1:
                  JasminClass.this.plusPlusHeight = JasminClass.this.currentStackHeight;
                  JasminClass.this.plusPlusHolder = null;
                  JasminClass.this.emitValue(JasminClass.this.plusPlusValue);
                  JasminClass.this.plusPlusPlace = JasminClass.this.code.size();
                  JasminClass.this.emit("dup", 1);
                  return;
               case 10:
                  JasminClass.this.plusPlusState = 11;
                  JasminClass.this.plusPlusHolder = (Local)JasminClass.this.plusPlusValue;
                  JasminClass.this.emitStmt(JasminClass.this.plusPlusIncrementer);
                  diff = JasminClass.this.plusPlusHeight - JasminClass.this.currentStackHeight + 1;
                  if (diff > 0 && JasminClass.this.plusPlusState == 11) {
                     JasminClass.this.code.set(JasminClass.this.plusPlusPlace, "    dup_x" + diff);
                  }

                  JasminClass.this.plusPlusHolder = null;
                  return;
               case 11:
                  JasminClass.this.plusPlusHeight = JasminClass.this.currentStackHeight;
                  JasminClass.this.plusPlusHolder = null;
                  JasminClass.this.emitValue(JasminClass.this.plusPlusValue);
                  if (JasminClass.this.plusPlusState != 11) {
                     JasminClass.this.emit("dup", 1);
                  }

                  JasminClass.this.plusPlusPlace = JasminClass.this.code.size();
                  return;
               }
            }

            if (slot >= 0 && slot <= 3) {
               JasminClass.this.emit("iload_" + slot, 1);
            } else {
               JasminClass.this.emit("iload " + slot, 1);
            }

         }

         public void caseLongType(LongType t) {
            if (slot >= 0 && slot <= 3) {
               JasminClass.this.emit("lload_" + slot, 2);
            } else {
               JasminClass.this.emit("lload " + slot, 2);
            }

         }

         public void caseRefType(RefType t) {
            if (slot >= 0 && slot <= 3) {
               JasminClass.this.emit("aload_" + slot, 1);
            } else {
               JasminClass.this.emit("aload " + slot, 1);
            }

         }

         public void caseNullType(NullType t) {
            if (slot >= 0 && slot <= 3) {
               JasminClass.this.emit("aload_" + slot, 1);
            } else {
               JasminClass.this.emit("aload " + slot, 1);
            }

         }
      });
   }

   void emitValue(Value value) {
      value.apply(new AbstractGrimpValueSwitch() {
         public void caseAddExpr(AddExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getType().apply(new TypeSwitch() {
               private void handleIntCase() {
                  JasminClass.this.emit("iadd", -1);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("ladd", -2);
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dadd", -2);
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fadd", -1);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid argument type for add");
               }
            });
         }

         public void caseAndExpr(AndExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getType().apply(new TypeSwitch() {
               private void handleIntCase() {
                  JasminClass.this.emit("iand", -1);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("land", -2);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid argument type for and");
               }
            });
         }

         public void caseArrayRef(ArrayRef v) {
            JasminClass.this.emitValue(v.getBase());
            JasminClass.this.emitValue(v.getIndex());
            v.getType().apply(new TypeSwitch() {
               public void caseArrayType(ArrayType ty) {
                  JasminClass.this.emit("aaload", -1);
               }

               public void caseBooleanType(BooleanType ty) {
                  JasminClass.this.emit("baload", -1);
               }

               public void caseByteType(ByteType ty) {
                  JasminClass.this.emit("baload", -1);
               }

               public void caseCharType(CharType ty) {
                  JasminClass.this.emit("caload", -1);
               }

               public void defaultCase(Type ty) {
                  throw new RuntimeException("invalid base type");
               }

               public void caseDoubleType(DoubleType ty) {
                  JasminClass.this.emit("daload", 0);
               }

               public void caseFloatType(FloatType ty) {
                  JasminClass.this.emit("faload", -1);
               }

               public void caseIntType(IntType ty) {
                  JasminClass.this.emit("iaload", -1);
               }

               public void caseLongType(LongType ty) {
                  JasminClass.this.emit("laload", 0);
               }

               public void caseNullType(NullType ty) {
                  JasminClass.this.emit("aaload", -1);
               }

               public void caseRefType(RefType ty) {
                  JasminClass.this.emit("aaload", -1);
               }

               public void caseShortType(ShortType ty) {
                  JasminClass.this.emit("saload", -1);
               }
            });
         }

         public void caseCastExpr(final CastExpr v) {
            final Type toType = v.getCastType();
            final Type fromType = v.getOp().getType();
            JasminClass.this.emitValue(v.getOp());
            if (toType instanceof RefType) {
               JasminClass.this.emit("checkcast " + AbstractJasminClass.slashify(toType.toString()), 0);
            } else if (toType instanceof ArrayType) {
               JasminClass.this.emit("checkcast " + AbstractJasminClass.jasminDescriptorOf(toType), 0);
            } else {
               fromType.apply(new TypeSwitch() {
                  public void defaultCase(Type ty) {
                     throw new RuntimeException("invalid fromType " + fromType);
                  }

                  public void caseDoubleType(DoubleType ty) {
                     if (toType.equals(IntType.v())) {
                        JasminClass.this.emit("d2i", -1);
                     } else if (toType.equals(LongType.v())) {
                        JasminClass.this.emit("d2l", 0);
                     } else {
                        if (!toType.equals(FloatType.v())) {
                           throw new RuntimeException("invalid toType from double: " + toType);
                        }

                        JasminClass.this.emit("d2f", -1);
                     }

                  }

                  public void caseFloatType(FloatType ty) {
                     if (toType.equals(IntType.v())) {
                        JasminClass.this.emit("f2i", 0);
                     } else if (toType.equals(LongType.v())) {
                        JasminClass.this.emit("f2l", 1);
                     } else {
                        if (!toType.equals(DoubleType.v())) {
                           throw new RuntimeException("invalid toType from float: " + toType);
                        }

                        JasminClass.this.emit("f2d", 1);
                     }

                  }

                  public void caseIntType(IntType ty) {
                     this.emitIntToTypeCast();
                  }

                  public void caseBooleanType(BooleanType ty) {
                     this.emitIntToTypeCast();
                  }

                  public void caseByteType(ByteType ty) {
                     this.emitIntToTypeCast();
                  }

                  public void caseCharType(CharType ty) {
                     this.emitIntToTypeCast();
                  }

                  public void caseShortType(ShortType ty) {
                     this.emitIntToTypeCast();
                  }

                  private void emitIntToTypeCast() {
                     if (toType.equals(ByteType.v())) {
                        JasminClass.this.emit("i2b", 0);
                     } else if (toType.equals(CharType.v())) {
                        JasminClass.this.emit("i2c", 0);
                     } else if (toType.equals(ShortType.v())) {
                        JasminClass.this.emit("i2s", 0);
                     } else if (toType.equals(FloatType.v())) {
                        JasminClass.this.emit("i2f", 0);
                     } else if (toType.equals(LongType.v())) {
                        JasminClass.this.emit("i2l", 1);
                     } else if (toType.equals(DoubleType.v())) {
                        JasminClass.this.emit("i2d", 1);
                     } else if (!toType.equals(IntType.v()) && !toType.equals(BooleanType.v())) {
                        throw new RuntimeException("invalid toType from int: " + toType + " " + v.toString());
                     }

                  }

                  public void caseLongType(LongType ty) {
                     if (toType.equals(IntType.v())) {
                        JasminClass.this.emit("l2i", -1);
                     } else if (toType.equals(FloatType.v())) {
                        JasminClass.this.emit("l2f", -1);
                     } else if (toType.equals(DoubleType.v())) {
                        JasminClass.this.emit("l2d", 0);
                     } else if (toType.equals(ByteType.v())) {
                        JasminClass.this.emit("l2i", -1);
                        this.emitIntToTypeCast();
                     } else if (toType.equals(ShortType.v())) {
                        JasminClass.this.emit("l2i", -1);
                        this.emitIntToTypeCast();
                     } else if (toType.equals(CharType.v())) {
                        JasminClass.this.emit("l2i", -1);
                        this.emitIntToTypeCast();
                     } else {
                        if (!toType.equals(BooleanType.v())) {
                           throw new RuntimeException("invalid toType from long: " + toType);
                        }

                        JasminClass.this.emit("l2i", -1);
                        this.emitIntToTypeCast();
                     }

                  }
               });
            }

         }

         public void caseCmpExpr(CmpExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            JasminClass.this.emit("lcmp", -3);
         }

         public void caseCmpgExpr(CmpgExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            if (v.getOp1().getType().equals(FloatType.v())) {
               JasminClass.this.emit("fcmpg", -1);
            } else {
               JasminClass.this.emit("dcmpg", -3);
            }

         }

         public void caseCmplExpr(CmplExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            if (v.getOp1().getType().equals(FloatType.v())) {
               JasminClass.this.emit("fcmpl", -1);
            } else {
               JasminClass.this.emit("dcmpl", -3);
            }

         }

         public void caseDivExpr(DivExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getType().apply(new TypeSwitch() {
               private void handleIntCase() {
                  JasminClass.this.emit("idiv", -1);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("ldiv", -2);
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("ddiv", -2);
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fdiv", -1);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid argument type for div");
               }
            });
         }

         public void caseDoubleConstant(DoubleConstant v) {
            if (v.value == 0.0D && 1.0D / v.value > 0.0D) {
               JasminClass.this.emit("dconst_0", 2);
            } else if (v.value == 1.0D) {
               JasminClass.this.emit("dconst_1", 2);
            } else {
               String s = JasminClass.this.doubleToString(v);
               JasminClass.this.emit("ldc2_w " + s, 2);
            }

         }

         public void caseFloatConstant(FloatConstant v) {
            if (v.value == 0.0F && 1.0F / v.value > 0.0F) {
               JasminClass.this.emit("fconst_0", 1);
            } else if (v.value == 1.0F) {
               JasminClass.this.emit("fconst_1", 1);
            } else if (v.value == 2.0F) {
               JasminClass.this.emit("fconst_2", 1);
            } else {
               String s = JasminClass.this.floatToString(v);
               JasminClass.this.emit("ldc " + s, 1);
            }

         }

         public void caseInstanceFieldRef(InstanceFieldRef v) {
            JasminClass.this.emitValue(v.getBase());
            JasminClass.this.emit("getfield " + AbstractJasminClass.slashify(v.getFieldRef().declaringClass().getName()) + "/" + v.getFieldRef().name() + " " + AbstractJasminClass.jasminDescriptorOf(v.getFieldRef().type()), -1 + AbstractJasminClass.sizeOfType(v.getFieldRef().type()));
         }

         public void caseInstanceOfExpr(InstanceOfExpr v) {
            JasminClass.this.emitValue(v.getOp());
            Type checkType = v.getCheckType();
            if (checkType instanceof RefType) {
               JasminClass.this.emit("instanceof " + AbstractJasminClass.slashify(checkType.toString()), 0);
            } else if (checkType instanceof ArrayType) {
               JasminClass.this.emit("instanceof " + AbstractJasminClass.jasminDescriptorOf(checkType), 0);
            }

         }

         public void caseIntConstant(IntConstant v) {
            if (v.value == -1) {
               JasminClass.this.emit("iconst_m1", 1);
            } else if (v.value >= 0 && v.value <= 5) {
               JasminClass.this.emit("iconst_" + v.value, 1);
            } else if (v.value >= -128 && v.value <= 127) {
               JasminClass.this.emit("bipush " + v.value, 1);
            } else if (v.value >= -32768 && v.value <= 32767) {
               JasminClass.this.emit("sipush " + v.value, 1);
            } else {
               JasminClass.this.emit("ldc " + v.toString(), 1);
            }

         }

         public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
            SootMethodRef m = v.getMethodRef();
            JasminClass.this.emitValue(v.getBase());

            for(int i = 0; i < m.parameterTypes().size(); ++i) {
               JasminClass.this.emitValue(v.getArg(i));
            }

            JasminClass.this.emit("invokeinterface " + AbstractJasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + AbstractJasminClass.jasminDescriptorOf(m) + " " + (AbstractJasminClass.argCountOf(m) + 1), -(AbstractJasminClass.argCountOf(m) + 1) + AbstractJasminClass.sizeOfType(m.returnType()));
         }

         public void caseLengthExpr(LengthExpr v) {
            JasminClass.this.emitValue(v.getOp());
            JasminClass.this.emit("arraylength", 0);
         }

         public void caseLocal(Local v) {
            JasminClass.this.emitLocal(v);
         }

         public void caseLongConstant(LongConstant v) {
            if (v.value == 0L) {
               JasminClass.this.emit("lconst_0", 2);
            } else if (v.value == 1L) {
               JasminClass.this.emit("lconst_1", 2);
            } else {
               JasminClass.this.emit("ldc2_w " + v.toString(), 2);
            }

         }

         public void caseMulExpr(MulExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getType().apply(new TypeSwitch() {
               private void handleIntCase() {
                  JasminClass.this.emit("imul", -1);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lmul", -2);
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dmul", -2);
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fmul", -1);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid argument type for mul");
               }
            });
         }

         public void caseLtExpr(LtExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getOp1().getType().apply(new TypeSwitch() {
               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dcmpg", -3);
                  JasminClass.this.emitBooleanBranch("iflt");
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fcmpg", -1);
                  JasminClass.this.emitBooleanBranch("iflt");
               }

               private void handleIntCase() {
                  JasminClass.this.emit("if_icmplt", -2);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lcmp", -3);
                  JasminClass.this.emitBooleanBranch("iflt");
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseLeExpr(LeExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getOp1().getType().apply(new TypeSwitch() {
               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dcmpg", -3);
                  JasminClass.this.emitBooleanBranch("ifle");
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fcmpg", -1);
                  JasminClass.this.emitBooleanBranch("ifle");
               }

               private void handleIntCase() {
                  JasminClass.this.emit("if_icmple", -2);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lcmp", -3);
                  JasminClass.this.emitBooleanBranch("ifle");
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseGtExpr(GtExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getOp1().getType().apply(new TypeSwitch() {
               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dcmpg", -3);
                  JasminClass.this.emitBooleanBranch("ifgt");
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fcmpg", -1);
                  JasminClass.this.emitBooleanBranch("ifgt");
               }

               private void handleIntCase() {
                  JasminClass.this.emit("if_icmpgt", -2);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lcmp", -3);
                  JasminClass.this.emitBooleanBranch("ifgt");
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseGeExpr(GeExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getOp1().getType().apply(new TypeSwitch() {
               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dcmpg", -3);
                  JasminClass.this.emitBooleanBranch("ifge");
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fcmpg", -1);
                  JasminClass.this.emitBooleanBranch("ifge");
               }

               private void handleIntCase() {
                  JasminClass.this.emit("if_icmpge", -2);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lcmp", -3);
                  JasminClass.this.emitBooleanBranch("ifge");
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseNeExpr(NeExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getOp1().getType().apply(new TypeSwitch() {
               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dcmpg", -3);
                  JasminClass.this.emit("iconst_0", 1);
                  JasminClass.this.emitBooleanBranch("if_icmpne");
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fcmpg", -1);
                  JasminClass.this.emit("iconst_0", 1);
                  JasminClass.this.emitBooleanBranch("if_icmpne");
               }

               private void handleIntCase() {
                  JasminClass.this.emit("if_icmpne", -2);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lcmp", -3);
                  JasminClass.this.emit("iconst_0", 1);
                  JasminClass.this.emitBooleanBranch("if_icmpne");
               }

               public void caseArrayType(ArrayType t) {
                  JasminClass.this.emitBooleanBranch("if_acmpne");
               }

               public void caseRefType(RefType t) {
                  JasminClass.this.emitBooleanBranch("if_acmpne");
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseEqExpr(EqExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getOp1().getType().apply(new TypeSwitch() {
               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dcmpg", -3);
                  JasminClass.this.emit("iconst_0", 1);
                  JasminClass.this.emitBooleanBranch("if_icmpeq");
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fcmpg", -3);
                  JasminClass.this.emit("iconst_0", 1);
                  JasminClass.this.emitBooleanBranch("if_icmpeq");
               }

               private void handleIntCase() {
                  JasminClass.this.emit("if_icmpeq", -2);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lcmp", -3);
                  JasminClass.this.emit("iconst_0", 1);
                  JasminClass.this.emitBooleanBranch("if_icmpeq");
               }

               public void caseArrayType(ArrayType t) {
                  JasminClass.this.emitBooleanBranch("if_acmpeq");
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseNegExpr(final NegExpr v) {
            JasminClass.this.emitValue(v.getOp());
            v.getType().apply(new TypeSwitch() {
               private void handleIntCase() {
                  JasminClass.this.emit("ineg", 0);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lneg", 0);
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dneg", 0);
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fneg", 0);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid argument type for neg: " + t + ": " + v);
               }
            });
         }

         public void caseNewArrayExpr(NewArrayExpr v) {
            Value size = v.getSize();
            JasminClass.this.emitValue(size);
            if (v.getBaseType() instanceof RefType) {
               JasminClass.this.emit("anewarray " + AbstractJasminClass.slashify(v.getBaseType().toString()), 0);
            } else if (v.getBaseType() instanceof ArrayType) {
               JasminClass.this.emit("anewarray " + AbstractJasminClass.jasminDescriptorOf(v.getBaseType()), 0);
            } else {
               JasminClass.this.emit("newarray " + v.getBaseType().toString(), 0);
            }

         }

         public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
            Iterator var2 = v.getSizes().iterator();

            while(var2.hasNext()) {
               Value val = (Value)var2.next();
               JasminClass.this.emitValue(val);
            }

            int size = v.getSizeCount();
            JasminClass.this.emit("multianewarray " + AbstractJasminClass.jasminDescriptorOf((Type)v.getBaseType()) + " " + size, -size + 1);
         }

         public void caseNewExpr(NewExpr v) {
            JasminClass.this.emit("new " + AbstractJasminClass.slashify(v.getBaseType().toString()), 1);
         }

         public void caseNewInvokeExpr(NewInvokeExpr v) {
            JasminClass.this.emit("new " + AbstractJasminClass.slashify(v.getBaseType().toString()), 1);
            JasminClass.this.emit("dup", 1);
            SootMethodRef m = v.getMethodRef();

            for(int i = 0; i < m.parameterTypes().size(); ++i) {
               JasminClass.this.emitValue(v.getArg(i));
            }

            JasminClass.this.emit("invokespecial " + AbstractJasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + AbstractJasminClass.jasminDescriptorOf(m), -(AbstractJasminClass.argCountOf(m) + 1) + AbstractJasminClass.sizeOfType(m.returnType()));
         }

         public void caseNullConstant(NullConstant v) {
            JasminClass.this.emit("aconst_null", 1);
         }

         public void caseOrExpr(OrExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getType().apply(new TypeSwitch() {
               private void handleIntCase() {
                  JasminClass.this.emit("ior", -1);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lor", -2);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid argument type for or");
               }
            });
         }

         public void caseRemExpr(RemExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getType().apply(new TypeSwitch() {
               private void handleIntCase() {
                  JasminClass.this.emit("irem", -1);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lrem", -2);
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("drem", -2);
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("frem", -1);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid argument type for rem");
               }
            });
         }

         public void caseShlExpr(ShlExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getType().apply(new TypeSwitch() {
               private void handleIntCase() {
                  JasminClass.this.emit("ishl", -1);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lshl", -1);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid argument type for shl");
               }
            });
         }

         public void caseShrExpr(ShrExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getType().apply(new TypeSwitch() {
               private void handleIntCase() {
                  JasminClass.this.emit("ishr", -1);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lshr", -1);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid argument type for shr");
               }
            });
         }

         public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
            SootMethodRef m = v.getMethodRef();
            JasminClass.this.emitValue(v.getBase());

            for(int i = 0; i < m.parameterTypes().size(); ++i) {
               JasminClass.this.emitValue(v.getArg(i));
            }

            JasminClass.this.emit("invokespecial " + AbstractJasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + AbstractJasminClass.jasminDescriptorOf(m), -(AbstractJasminClass.argCountOf(m) + 1) + AbstractJasminClass.sizeOfType(m.returnType()));
         }

         public void caseStaticInvokeExpr(StaticInvokeExpr v) {
            SootMethodRef m = v.getMethodRef();

            for(int i = 0; i < m.parameterTypes().size(); ++i) {
               JasminClass.this.emitValue(v.getArg(i));
            }

            JasminClass.this.emit("invokestatic " + AbstractJasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + AbstractJasminClass.jasminDescriptorOf(m), -AbstractJasminClass.argCountOf(m) + AbstractJasminClass.sizeOfType(m.returnType()));
         }

         public void caseStaticFieldRef(StaticFieldRef v) {
            JasminClass.this.emit("getstatic " + AbstractJasminClass.slashify(v.getFieldRef().declaringClass().getName()) + "/" + v.getFieldRef().name() + " " + AbstractJasminClass.jasminDescriptorOf(v.getFieldRef().type()), AbstractJasminClass.sizeOfType(v.getFieldRef().type()));
         }

         public void caseStringConstant(StringConstant v) {
            JasminClass.this.emit("ldc " + v.toString(), 1);
         }

         public void caseClassConstant(ClassConstant v) {
            JasminClass.this.emit("ldc_w " + v.getValue(), 1);
         }

         public void caseSubExpr(SubExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getType().apply(new TypeSwitch() {
               private void handleIntCase() {
                  JasminClass.this.emit("isub", -1);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lsub", -2);
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dsub", -2);
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fsub", -1);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid argument type for sub");
               }
            });
         }

         public void caseUshrExpr(UshrExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getType().apply(new TypeSwitch() {
               private void handleIntCase() {
                  JasminClass.this.emit("iushr", -1);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lushr", -1);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid argument type for ushr");
               }
            });
         }

         public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
            SootMethodRef m = v.getMethodRef();
            JasminClass.this.emitValue(v.getBase());

            for(int i = 0; i < m.parameterTypes().size(); ++i) {
               JasminClass.this.emitValue(v.getArg(i));
            }

            JasminClass.this.emit("invokevirtual " + AbstractJasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + AbstractJasminClass.jasminDescriptorOf(m), -(AbstractJasminClass.argCountOf(m) + 1) + AbstractJasminClass.sizeOfType(m.returnType()));
         }

         public void caseXorExpr(XorExpr v) {
            JasminClass.this.emitValue(v.getOp1());
            JasminClass.this.emitValue(v.getOp2());
            v.getType().apply(new TypeSwitch() {
               private void handleIntCase() {
                  JasminClass.this.emit("ixor", -1);
               }

               public void caseIntType(IntType t) {
                  this.handleIntCase();
               }

               public void caseBooleanType(BooleanType t) {
                  this.handleIntCase();
               }

               public void caseShortType(ShortType t) {
                  this.handleIntCase();
               }

               public void caseCharType(CharType t) {
                  this.handleIntCase();
               }

               public void caseByteType(ByteType t) {
                  this.handleIntCase();
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lxor", -2);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid argument type for xor");
               }
            });
         }
      });
   }

   public void emitBooleanBranch(String s) {
      byte count;
      if (s.indexOf("icmp") == -1 && s.indexOf("acmp") == -1) {
         count = -1;
      } else {
         count = -2;
      }

      this.emit(s + " label" + this.labelCount, count);
      this.emit("iconst_0", 1);
      this.emit("goto label" + this.labelCount + 1, 0);
      this.emit("label" + this.labelCount++ + ":");
      this.emit("iconst_1", 1);
      this.emit("label" + this.labelCount++ + ":");
   }
}
