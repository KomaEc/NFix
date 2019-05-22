package soot.baf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import soot.Local;
import soot.LongType;
import soot.Modifier;
import soot.NullType;
import soot.PackManager;
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
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IdentityRef;
import soot.jimple.IntConstant;
import soot.jimple.JimpleBody;
import soot.jimple.LongConstant;
import soot.jimple.MethodHandle;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.options.Options;
import soot.tagkit.JasminAttribute;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.util.ArraySet;
import soot.util.Chain;

public class JasminClass extends AbstractJasminClass {
   private static final Logger logger = LoggerFactory.getLogger(JasminClass.class);

   public JasminClass(SootClass sootClass) {
      super(sootClass);
   }

   protected void assignColorsToLocals(Body body) {
      super.assignColorsToLocals(body);
      if (Options.v().time()) {
         Timers.v().packTimer.end();
      }

   }

   protected void emitMethodBody(SootMethod method) {
      if (Options.v().time()) {
         Timers.v().buildJasminTimer.end();
      }

      Body activeBody = method.getActiveBody();
      if (!(activeBody instanceof BafBody)) {
         if (!(activeBody instanceof JimpleBody)) {
            throw new RuntimeException("method: " + method.getName() + " has an invalid active body!");
         }

         if (Options.v().verbose()) {
            logger.debug("Was expecting Baf body for " + method + " but found a Jimple body. Will convert body to Baf on the fly.");
         }

         activeBody = PackManager.v().convertJimpleBodyToBaf(method);
      }

      BafBody body = (BafBody)activeBody;
      if (body == null) {
         throw new RuntimeException("method: " + method.getName() + " has no active body!");
      } else {
         if (Options.v().time()) {
            Timers.v().buildJasminTimer.start();
         }

         Chain<Unit> instList = body.getUnits();
         int stackLimitIndex = -1;
         this.subroutineToReturnAddressSlot = new HashMap(10, 0.7F);
         this.unitToLabel = new HashMap(instList.size() * 2 + 1, 0.7F);
         this.labelCount = 0;
         Iterator var6 = body.getUnitBoxes(true).iterator();

         while(var6.hasNext()) {
            UnitBox uBox = (UnitBox)var6.next();
            InstBox box = (InstBox)uBox;
            if (!this.unitToLabel.containsKey(box.getUnit())) {
               this.unitToLabel.put(box.getUnit(), "label" + this.labelCount++);
            }
         }

         Set<Unit> handlerUnits = new ArraySet(body.getTraps().size());
         Iterator var18 = body.getTraps().iterator();

         while(var18.hasNext()) {
            Trap trap = (Trap)var18.next();
            handlerUnits.add(trap.getHandlerUnit());
            if (trap.getBeginUnit() != trap.getEndUnit()) {
               this.emit(".catch " + slashify(trap.getException().getName()) + " from " + (String)this.unitToLabel.get(trap.getBeginUnit()) + " to " + (String)this.unitToLabel.get(trap.getEndUnit()) + " using " + (String)this.unitToLabel.get(trap.getHandlerUnit()));
            }
         }

         int localCount = 0;
         int[] paramSlots = new int[method.getParameterCount()];
         int thisSlot = 0;
         Set<Local> assignedLocals = new HashSet();
         this.localToSlot = new HashMap(body.getLocalCount() * 2 + 1, 0.7F);
         if (!method.isStatic()) {
            thisSlot = 0;
            ++localCount;
         }

         for(int i = 0; i < method.getParameterCount(); ++i) {
            paramSlots[i] = localCount;
            localCount += sizeOfType(method.getParameterType(i));
         }

         Iterator var29 = instList.iterator();

         while(true) {
            Local l;
            int slot;
            while(true) {
               Inst s;
               do {
                  do {
                     if (!var29.hasNext()) {
                        var29 = body.getLocals().iterator();

                        while(var29.hasNext()) {
                           Local local = (Local)var29.next();
                           if (assignedLocals.add(local)) {
                              this.localToSlot.put(local, new Integer(localCount));
                              localCount += sizeOfType(local.getType());
                           }
                        }

                        if (!Modifier.isNative(method.getModifiers()) && !Modifier.isAbstract(method.getModifiers())) {
                           this.emit("    .limit stack ?");
                           stackLimitIndex = this.code.size() - 1;
                           this.emit("    .limit locals " + localCount);
                        }

                        this.isEmittingMethodCode = true;
                        this.maxStackHeight = 0;
                        this.isNextGotoAJsr = false;

                        Inst s;
                        for(var18 = instList.iterator(); var18.hasNext(); this.emitInst(s)) {
                           Unit u = (Unit)var18.next();
                           s = (Inst)u;
                           if (this.unitToLabel.containsKey(s)) {
                              this.emit((String)this.unitToLabel.get(s) + ":");
                           }
                        }

                        this.isEmittingMethodCode = false;
                        this.maxStackHeight = 0;
                        if (((Body)activeBody).getUnits().size() != 0) {
                           BlockGraph blockGraph = new BriefBlockGraph((Body)activeBody);
                           List<Block> blocks = blockGraph.getBlocks();
                           if (blocks.size() != 0) {
                              List<Block> entryPoints = blockGraph.getHeads();

                              Iterator var28;
                              Block entryBlock;
                              Integer initialHeight;
                              for(var28 = entryPoints.iterator(); var28.hasNext(); this.blockToLogicalStackHeight.put(entryBlock, initialHeight)) {
                                 entryBlock = (Block)var28.next();
                                 if (handlerUnits.contains(entryBlock.getHead())) {
                                    initialHeight = new Integer(1);
                                 } else {
                                    initialHeight = new Integer(0);
                                 }

                                 if (this.blockToStackHeight == null) {
                                    this.blockToStackHeight = new HashMap();
                                 }

                                 this.blockToStackHeight.put(entryBlock, initialHeight);
                                 if (this.blockToLogicalStackHeight == null) {
                                    this.blockToLogicalStackHeight = new HashMap();
                                 }
                              }

                              var28 = entryPoints.iterator();

                              while(var28.hasNext()) {
                                 entryBlock = (Block)var28.next();
                                 this.calculateStackHeight(entryBlock);
                                 this.calculateLogicalStackHeightCheck(entryBlock);
                              }
                           }
                        }

                        if (!Modifier.isNative(method.getModifiers()) && !Modifier.isAbstract(method.getModifiers())) {
                           this.code.set(stackLimitIndex, "    .limit stack " + this.maxStackHeight);
                        }

                        var18 = body.getTags().iterator();

                        while(var18.hasNext()) {
                           Tag t = (Tag)var18.next();
                           if (t instanceof JasminAttribute) {
                              this.emit(".code_attribute " + t.getName() + " \"" + ((JasminAttribute)t).getJasminValue(this.unitToLabel) + "\"");
                           }
                        }

                        return;
                     }

                     Unit u = (Unit)var29.next();
                     s = (Inst)u;
                  } while(!(s instanceof IdentityInst));
               } while(!(((IdentityInst)s).getLeftOp() instanceof Local));

               l = (Local)((IdentityInst)s).getLeftOp();
               IdentityRef identity = (IdentityRef)((IdentityInst)s).getRightOp();
               int slot = false;
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

            this.localToSlot.put(l, new Integer(slot));
            assignedLocals.add(l);
         }
      }
   }

   void emitInst(Inst inst) {
      LineNumberTag lnTag = (LineNumberTag)inst.getTag("LineNumberTag");
      if (lnTag != null) {
         this.emit(".line " + lnTag.getLineNumber());
      }

      inst.apply(new InstSwitch() {
         public void caseReturnVoidInst(ReturnVoidInst i) {
            JasminClass.this.emit("return");
         }

         public void caseReturnInst(ReturnInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid return type " + t.toString());
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dreturn");
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("freturn");
               }

               public void caseIntType(IntType t) {
                  JasminClass.this.emit("ireturn");
               }

               public void caseByteType(ByteType t) {
                  JasminClass.this.emit("ireturn");
               }

               public void caseShortType(ShortType t) {
                  JasminClass.this.emit("ireturn");
               }

               public void caseCharType(CharType t) {
                  JasminClass.this.emit("ireturn");
               }

               public void caseBooleanType(BooleanType t) {
                  JasminClass.this.emit("ireturn");
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lreturn");
               }

               public void caseArrayType(ArrayType t) {
                  JasminClass.this.emit("areturn");
               }

               public void caseRefType(RefType t) {
                  JasminClass.this.emit("areturn");
               }

               public void caseNullType(NullType t) {
                  JasminClass.this.emit("areturn");
               }
            });
         }

         public void caseNopInst(NopInst i) {
            JasminClass.this.emit("nop");
         }

         public void caseEnterMonitorInst(EnterMonitorInst i) {
            JasminClass.this.emit("monitorenter");
         }

         public void casePopInst(PopInst i) {
            if (i.getWordCount() == 2) {
               JasminClass.this.emit("pop2");
            } else {
               JasminClass.this.emit("pop");
            }

         }

         public void caseExitMonitorInst(ExitMonitorInst i) {
            JasminClass.this.emit("monitorexit");
         }

         public void caseGotoInst(GotoInst i) {
            JasminClass.this.emit("goto " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
         }

         public void caseJSRInst(JSRInst i) {
            JasminClass.this.emit("jsr " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
         }

         public void casePushInst(PushInst i) {
            if (i.getConstant() instanceof IntConstant) {
               IntConstant vx = (IntConstant)((IntConstant)i.getConstant());
               if (vx.value == -1) {
                  JasminClass.this.emit("iconst_m1");
               } else if (vx.value >= 0 && vx.value <= 5) {
                  JasminClass.this.emit("iconst_" + vx.value);
               } else if (vx.value >= -128 && vx.value <= 127) {
                  JasminClass.this.emit("bipush " + vx.value);
               } else if (vx.value >= -32768 && vx.value <= 32767) {
                  JasminClass.this.emit("sipush " + vx.value);
               } else {
                  JasminClass.this.emit("ldc " + vx.toString());
               }
            } else if (i.getConstant() instanceof StringConstant) {
               JasminClass.this.emit("ldc " + i.getConstant().toString());
            } else if (i.getConstant() instanceof ClassConstant) {
               JasminClass.this.emit("ldc_w " + ((ClassConstant)i.getConstant()).getValue());
            } else {
               String s;
               if (i.getConstant() instanceof DoubleConstant) {
                  DoubleConstant vxx = (DoubleConstant)((DoubleConstant)i.getConstant());
                  if (vxx.value == 0.0D && 1.0D / vxx.value > 0.0D) {
                     JasminClass.this.emit("dconst_0");
                  } else if (vxx.value == 1.0D) {
                     JasminClass.this.emit("dconst_1");
                  } else {
                     s = JasminClass.this.doubleToString(vxx);
                     JasminClass.this.emit("ldc2_w " + s);
                  }
               } else if (i.getConstant() instanceof FloatConstant) {
                  FloatConstant vxxx = (FloatConstant)((FloatConstant)i.getConstant());
                  if (vxxx.value == 0.0F && 1.0F / vxxx.value > 1.0F) {
                     JasminClass.this.emit("fconst_0");
                  } else if (vxxx.value == 1.0F) {
                     JasminClass.this.emit("fconst_1");
                  } else if (vxxx.value == 2.0F) {
                     JasminClass.this.emit("fconst_2");
                  } else {
                     s = JasminClass.this.floatToString(vxxx);
                     JasminClass.this.emit("ldc " + s);
                  }
               } else if (i.getConstant() instanceof LongConstant) {
                  LongConstant v = (LongConstant)((LongConstant)i.getConstant());
                  if (v.value == 0L) {
                     JasminClass.this.emit("lconst_0");
                  } else if (v.value == 1L) {
                     JasminClass.this.emit("lconst_1");
                  } else {
                     JasminClass.this.emit("ldc2_w " + v.toString());
                  }
               } else {
                  if (!(i.getConstant() instanceof NullConstant)) {
                     if (i.getConstant() instanceof MethodHandle) {
                        throw new RuntimeException("MethodHandle constants not supported by Jasmin. Please use -asm-backend.");
                     }

                     throw new RuntimeException("unsupported opcode");
                  }

                  JasminClass.this.emit("aconst_null");
               }
            }

         }

         public void caseIdentityInst(IdentityInst i) {
            if (i.getRightOp() instanceof CaughtExceptionRef && i.getLeftOp() instanceof Local) {
               int slot = (Integer)JasminClass.this.localToSlot.get(i.getLeftOp());
               if (slot >= 0 && slot <= 3) {
                  JasminClass.this.emit("astore_" + slot);
               } else {
                  JasminClass.this.emit("astore " + slot);
               }
            }

         }

         public void caseStoreInst(StoreInst i) {
            final int slot = (Integer)JasminClass.this.localToSlot.get(i.getLocal());
            i.getOpType().apply(new TypeSwitch() {
               public void caseArrayType(ArrayType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("astore_" + slot);
                  } else {
                     JasminClass.this.emit("astore " + slot);
                  }

               }

               public void caseDoubleType(DoubleType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("dstore_" + slot);
                  } else {
                     JasminClass.this.emit("dstore " + slot);
                  }

               }

               public void caseFloatType(FloatType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("fstore_" + slot);
                  } else {
                     JasminClass.this.emit("fstore " + slot);
                  }

               }

               public void caseIntType(IntType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("istore_" + slot);
                  } else {
                     JasminClass.this.emit("istore " + slot);
                  }

               }

               public void caseByteType(ByteType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("istore_" + slot);
                  } else {
                     JasminClass.this.emit("istore " + slot);
                  }

               }

               public void caseShortType(ShortType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("istore_" + slot);
                  } else {
                     JasminClass.this.emit("istore " + slot);
                  }

               }

               public void caseCharType(CharType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("istore_" + slot);
                  } else {
                     JasminClass.this.emit("istore " + slot);
                  }

               }

               public void caseBooleanType(BooleanType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("istore_" + slot);
                  } else {
                     JasminClass.this.emit("istore " + slot);
                  }

               }

               public void caseLongType(LongType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("lstore_" + slot);
                  } else {
                     JasminClass.this.emit("lstore " + slot);
                  }

               }

               public void caseRefType(RefType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("astore_" + slot);
                  } else {
                     JasminClass.this.emit("astore " + slot);
                  }

               }

               public void caseStmtAddressType(StmtAddressType t) {
                  JasminClass.this.isNextGotoAJsr = true;
                  JasminClass.this.returnAddressSlot = slot;
               }

               public void caseNullType(NullType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("astore_" + slot);
                  } else {
                     JasminClass.this.emit("astore " + slot);
                  }

               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid local type:" + t);
               }
            });
         }

         public void caseLoadInst(LoadInst i) {
            final int slot = (Integer)JasminClass.this.localToSlot.get(i.getLocal());
            i.getOpType().apply(new TypeSwitch() {
               public void caseArrayType(ArrayType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("aload_" + slot);
                  } else {
                     JasminClass.this.emit("aload " + slot);
                  }

               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid local type to load" + t);
               }

               public void caseDoubleType(DoubleType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("dload_" + slot);
                  } else {
                     JasminClass.this.emit("dload " + slot);
                  }

               }

               public void caseFloatType(FloatType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("fload_" + slot);
                  } else {
                     JasminClass.this.emit("fload " + slot);
                  }

               }

               public void caseIntType(IntType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("iload_" + slot);
                  } else {
                     JasminClass.this.emit("iload " + slot);
                  }

               }

               public void caseByteType(ByteType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("iload_" + slot);
                  } else {
                     JasminClass.this.emit("iload " + slot);
                  }

               }

               public void caseShortType(ShortType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("iload_" + slot);
                  } else {
                     JasminClass.this.emit("iload " + slot);
                  }

               }

               public void caseCharType(CharType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("iload_" + slot);
                  } else {
                     JasminClass.this.emit("iload " + slot);
                  }

               }

               public void caseBooleanType(BooleanType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("iload_" + slot);
                  } else {
                     JasminClass.this.emit("iload " + slot);
                  }

               }

               public void caseLongType(LongType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("lload_" + slot);
                  } else {
                     JasminClass.this.emit("lload " + slot);
                  }

               }

               public void caseRefType(RefType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("aload_" + slot);
                  } else {
                     JasminClass.this.emit("aload " + slot);
                  }

               }

               public void caseNullType(NullType t) {
                  if (slot >= 0 && slot <= 3) {
                     JasminClass.this.emit("aload_" + slot);
                  } else {
                     JasminClass.this.emit("aload " + slot);
                  }

               }
            });
         }

         public void caseArrayWriteInst(ArrayWriteInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseArrayType(ArrayType t) {
                  JasminClass.this.emit("aastore");
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dastore");
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fastore");
               }

               public void caseIntType(IntType t) {
                  JasminClass.this.emit("iastore");
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lastore");
               }

               public void caseRefType(RefType t) {
                  JasminClass.this.emit("aastore");
               }

               public void caseByteType(ByteType t) {
                  JasminClass.this.emit("bastore");
               }

               public void caseBooleanType(BooleanType t) {
                  JasminClass.this.emit("bastore");
               }

               public void caseCharType(CharType t) {
                  JasminClass.this.emit("castore");
               }

               public void caseShortType(ShortType t) {
                  JasminClass.this.emit("sastore");
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid type: " + t);
               }
            });
         }

         public void caseArrayReadInst(ArrayReadInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseArrayType(ArrayType ty) {
                  JasminClass.this.emit("aaload");
               }

               public void caseBooleanType(BooleanType ty) {
                  JasminClass.this.emit("baload");
               }

               public void caseByteType(ByteType ty) {
                  JasminClass.this.emit("baload");
               }

               public void caseCharType(CharType ty) {
                  JasminClass.this.emit("caload");
               }

               public void defaultCase(Type ty) {
                  throw new RuntimeException("invalid base type");
               }

               public void caseDoubleType(DoubleType ty) {
                  JasminClass.this.emit("daload");
               }

               public void caseFloatType(FloatType ty) {
                  JasminClass.this.emit("faload");
               }

               public void caseIntType(IntType ty) {
                  JasminClass.this.emit("iaload");
               }

               public void caseLongType(LongType ty) {
                  JasminClass.this.emit("laload");
               }

               public void caseNullType(NullType ty) {
                  JasminClass.this.emit("aaload");
               }

               public void caseRefType(RefType ty) {
                  JasminClass.this.emit("aaload");
               }

               public void caseShortType(ShortType ty) {
                  JasminClass.this.emit("saload");
               }
            });
         }

         public void caseIfNullInst(IfNullInst i) {
            JasminClass.this.emit("ifnull " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
         }

         public void caseIfNonNullInst(IfNonNullInst i) {
            JasminClass.this.emit("ifnonnull " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
         }

         public void caseIfEqInst(IfEqInst i) {
            JasminClass.this.emit("ifeq " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
         }

         public void caseIfNeInst(IfNeInst i) {
            JasminClass.this.emit("ifne " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
         }

         public void caseIfGtInst(IfGtInst i) {
            JasminClass.this.emit("ifgt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
         }

         public void caseIfGeInst(IfGeInst i) {
            JasminClass.this.emit("ifge " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
         }

         public void caseIfLtInst(IfLtInst i) {
            JasminClass.this.emit("iflt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
         }

         public void caseIfLeInst(IfLeInst i) {
            JasminClass.this.emit("ifle " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
         }

         public void caseIfCmpEqInst(final IfCmpEqInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseIntType(IntType t) {
                  JasminClass.this.emit("if_icmpeq " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseBooleanType(BooleanType t) {
                  JasminClass.this.emit("if_icmpeq " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseShortType(ShortType t) {
                  JasminClass.this.emit("if_icmpeq " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseCharType(CharType t) {
                  JasminClass.this.emit("if_icmpeq " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseByteType(ByteType t) {
                  JasminClass.this.emit("if_icmpeq " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dcmpg");
                  JasminClass.this.emit("ifeq " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lcmp");
                  JasminClass.this.emit("ifeq " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fcmpg");
                  JasminClass.this.emit("ifeq " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseArrayType(ArrayType t) {
                  JasminClass.this.emit("if_acmpeq " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseRefType(RefType t) {
                  JasminClass.this.emit("if_acmpeq " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseNullType(NullType t) {
                  JasminClass.this.emit("if_acmpeq " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseIfCmpNeInst(final IfCmpNeInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseIntType(IntType t) {
                  JasminClass.this.emit("if_icmpne " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseBooleanType(BooleanType t) {
                  JasminClass.this.emit("if_icmpne " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseShortType(ShortType t) {
                  JasminClass.this.emit("if_icmpne " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseCharType(CharType t) {
                  JasminClass.this.emit("if_icmpne " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseByteType(ByteType t) {
                  JasminClass.this.emit("if_icmpne " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dcmpg");
                  JasminClass.this.emit("ifne " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lcmp");
                  JasminClass.this.emit("ifne " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fcmpg");
                  JasminClass.this.emit("ifne " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseArrayType(ArrayType t) {
                  JasminClass.this.emit("if_acmpne " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseRefType(RefType t) {
                  JasminClass.this.emit("if_acmpne " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseNullType(NullType t) {
                  JasminClass.this.emit("if_acmpne " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseIfCmpGtInst(final IfCmpGtInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseIntType(IntType t) {
                  JasminClass.this.emit("if_icmpgt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseBooleanType(BooleanType t) {
                  JasminClass.this.emit("if_icmpgt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseShortType(ShortType t) {
                  JasminClass.this.emit("if_icmpgt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseCharType(CharType t) {
                  JasminClass.this.emit("if_icmpgt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseByteType(ByteType t) {
                  JasminClass.this.emit("if_icmpgt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dcmpg");
                  JasminClass.this.emit("ifgt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lcmp");
                  JasminClass.this.emit("ifgt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fcmpg");
                  JasminClass.this.emit("ifgt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseArrayType(ArrayType t) {
                  JasminClass.this.emit("if_acmpgt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseRefType(RefType t) {
                  JasminClass.this.emit("if_acmpgt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseNullType(NullType t) {
                  JasminClass.this.emit("if_acmpgt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseIfCmpGeInst(final IfCmpGeInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseIntType(IntType t) {
                  JasminClass.this.emit("if_icmpge " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseBooleanType(BooleanType t) {
                  JasminClass.this.emit("if_icmpge " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseShortType(ShortType t) {
                  JasminClass.this.emit("if_icmpge " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseCharType(CharType t) {
                  JasminClass.this.emit("if_icmpge " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseByteType(ByteType t) {
                  JasminClass.this.emit("if_icmpge " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dcmpg");
                  JasminClass.this.emit("ifge " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lcmp");
                  JasminClass.this.emit("ifge " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fcmpg");
                  JasminClass.this.emit("ifge " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseArrayType(ArrayType t) {
                  JasminClass.this.emit("if_acmpge " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseRefType(RefType t) {
                  JasminClass.this.emit("if_acmpge " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseNullType(NullType t) {
                  JasminClass.this.emit("if_acmpge " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseIfCmpLtInst(final IfCmpLtInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseIntType(IntType t) {
                  JasminClass.this.emit("if_icmplt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseBooleanType(BooleanType t) {
                  JasminClass.this.emit("if_icmplt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseShortType(ShortType t) {
                  JasminClass.this.emit("if_icmplt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseCharType(CharType t) {
                  JasminClass.this.emit("if_icmplt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseByteType(ByteType t) {
                  JasminClass.this.emit("if_icmplt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dcmpg");
                  JasminClass.this.emit("iflt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lcmp");
                  JasminClass.this.emit("iflt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fcmpg");
                  JasminClass.this.emit("iflt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseArrayType(ArrayType t) {
                  JasminClass.this.emit("if_acmplt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseRefType(RefType t) {
                  JasminClass.this.emit("if_acmplt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseNullType(NullType t) {
                  JasminClass.this.emit("if_acmplt " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseIfCmpLeInst(final IfCmpLeInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseIntType(IntType t) {
                  JasminClass.this.emit("if_icmple " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseBooleanType(BooleanType t) {
                  JasminClass.this.emit("if_icmple " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseShortType(ShortType t) {
                  JasminClass.this.emit("if_icmple " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseCharType(CharType t) {
                  JasminClass.this.emit("if_icmple " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseByteType(ByteType t) {
                  JasminClass.this.emit("if_icmple " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("dcmpg");
                  JasminClass.this.emit("ifle " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseLongType(LongType t) {
                  JasminClass.this.emit("lcmp");
                  JasminClass.this.emit("ifle " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("fcmpg");
                  JasminClass.this.emit("ifle " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseArrayType(ArrayType t) {
                  JasminClass.this.emit("if_acmple " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseRefType(RefType t) {
                  JasminClass.this.emit("if_acmple " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void caseNullType(NullType t) {
                  JasminClass.this.emit("if_acmple " + (String)JasminClass.this.unitToLabel.get(i.getTarget()));
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseStaticGetInst(StaticGetInst i) {
            SootFieldRef field = i.getFieldRef();
            JasminClass.this.emit("getstatic " + AbstractJasminClass.slashify(field.declaringClass().getName()) + "/" + field.name() + " " + AbstractJasminClass.jasminDescriptorOf(field.type()));
         }

         public void caseStaticPutInst(StaticPutInst i) {
            JasminClass.this.emit("putstatic " + AbstractJasminClass.slashify(i.getFieldRef().declaringClass().getName()) + "/" + i.getFieldRef().name() + " " + AbstractJasminClass.jasminDescriptorOf(i.getFieldRef().type()));
         }

         public void caseFieldGetInst(FieldGetInst i) {
            JasminClass.this.emit("getfield " + AbstractJasminClass.slashify(i.getFieldRef().declaringClass().getName()) + "/" + i.getFieldRef().name() + " " + AbstractJasminClass.jasminDescriptorOf(i.getFieldRef().type()));
         }

         public void caseFieldPutInst(FieldPutInst i) {
            JasminClass.this.emit("putfield " + AbstractJasminClass.slashify(i.getFieldRef().declaringClass().getName()) + "/" + i.getFieldRef().name() + " " + AbstractJasminClass.jasminDescriptorOf(i.getFieldRef().type()));
         }

         public void caseInstanceCastInst(InstanceCastInst i) {
            Type castType = i.getCastType();
            if (castType instanceof RefType) {
               JasminClass.this.emit("checkcast " + AbstractJasminClass.slashify(((RefType)castType).getClassName()));
            } else if (castType instanceof ArrayType) {
               JasminClass.this.emit("checkcast " + AbstractJasminClass.jasminDescriptorOf(castType));
            }

         }

         public void caseInstanceOfInst(InstanceOfInst i) {
            Type checkType = i.getCheckType();
            if (checkType instanceof RefType) {
               JasminClass.this.emit("instanceof " + AbstractJasminClass.slashify(checkType.toString()));
            } else if (checkType instanceof ArrayType) {
               JasminClass.this.emit("instanceof " + AbstractJasminClass.jasminDescriptorOf(checkType));
            }

         }

         public void caseNewInst(NewInst i) {
            JasminClass.this.emit("new " + AbstractJasminClass.slashify(i.getBaseType().getClassName()));
         }

         public void casePrimitiveCastInst(PrimitiveCastInst i) {
            JasminClass.this.emit(i.toString());
         }

         public void caseDynamicInvokeInst(DynamicInvokeInst i) {
            SootMethodRef m = i.getMethodRef();
            SootMethodRef bsm = i.getBootstrapMethodRef();
            String bsmArgString = "";
            Iterator iterator = i.getBootstrapArgs().iterator();

            while(iterator.hasNext()) {
               Value val = (Value)iterator.next();
               bsmArgString = bsmArgString + "(" + AbstractJasminClass.jasminDescriptorOf(val.getType()) + ")";
               bsmArgString = bsmArgString + this.escape(val.toString());
               if (iterator.hasNext()) {
                  bsmArgString = bsmArgString + ",";
               }
            }

            JasminClass.this.emit("invokedynamic \"" + m.name() + "\" " + AbstractJasminClass.jasminDescriptorOf(m) + " " + AbstractJasminClass.slashify(bsm.declaringClass().getName()) + "/" + bsm.name() + AbstractJasminClass.jasminDescriptorOf(bsm) + "(" + bsmArgString + ")");
         }

         private String escape(String bsmArgString) {
            return bsmArgString.replace(",", "\\comma").replace(" ", "\\blank").replace("\t", "\\tab").replace("\n", "\\newline");
         }

         public void caseStaticInvokeInst(StaticInvokeInst i) {
            SootMethodRef m = i.getMethodRef();
            JasminClass.this.emit("invokestatic " + AbstractJasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + AbstractJasminClass.jasminDescriptorOf(m));
         }

         public void caseVirtualInvokeInst(VirtualInvokeInst i) {
            SootMethodRef m = i.getMethodRef();
            JasminClass.this.emit("invokevirtual " + AbstractJasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + AbstractJasminClass.jasminDescriptorOf(m));
         }

         public void caseInterfaceInvokeInst(InterfaceInvokeInst i) {
            SootMethodRef m = i.getMethodRef();
            JasminClass.this.emit("invokeinterface " + AbstractJasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + AbstractJasminClass.jasminDescriptorOf(m) + " " + (AbstractJasminClass.argCountOf(m) + 1));
         }

         public void caseSpecialInvokeInst(SpecialInvokeInst i) {
            SootMethodRef m = i.getMethodRef();
            JasminClass.this.emit("invokespecial " + AbstractJasminClass.slashify(m.declaringClass().getName()) + "/" + m.name() + AbstractJasminClass.jasminDescriptorOf(m));
         }

         public void caseThrowInst(ThrowInst i) {
            JasminClass.this.emit("athrow");
         }

         public void caseCmpInst(CmpInst i) {
            JasminClass.this.emit("lcmp");
         }

         public void caseCmplInst(CmplInst i) {
            if (i.getOpType().equals(FloatType.v())) {
               JasminClass.this.emit("fcmpl");
            } else {
               JasminClass.this.emit("dcmpl");
            }

         }

         public void caseCmpgInst(CmpgInst i) {
            if (i.getOpType().equals(FloatType.v())) {
               JasminClass.this.emit("fcmpg");
            } else {
               JasminClass.this.emit("dcmpg");
            }

         }

         private void emitOpTypeInst(final String s, OpTypeArgInst i) {
            i.getOpType().apply(new TypeSwitch() {
               private void handleIntCase() {
                  JasminClass.this.emit("i" + s);
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
                  JasminClass.this.emit("l" + s);
               }

               public void caseDoubleType(DoubleType t) {
                  JasminClass.this.emit("d" + s);
               }

               public void caseFloatType(FloatType t) {
                  JasminClass.this.emit("f" + s);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid argument type for div");
               }
            });
         }

         public void caseAddInst(AddInst i) {
            this.emitOpTypeInst("add", i);
         }

         public void caseDivInst(DivInst i) {
            this.emitOpTypeInst("div", i);
         }

         public void caseSubInst(SubInst i) {
            this.emitOpTypeInst("sub", i);
         }

         public void caseMulInst(MulInst i) {
            this.emitOpTypeInst("mul", i);
         }

         public void caseRemInst(RemInst i) {
            this.emitOpTypeInst("rem", i);
         }

         public void caseShlInst(ShlInst i) {
            this.emitOpTypeInst("shl", i);
         }

         public void caseAndInst(AndInst i) {
            this.emitOpTypeInst("and", i);
         }

         public void caseOrInst(OrInst i) {
            this.emitOpTypeInst("or", i);
         }

         public void caseXorInst(XorInst i) {
            this.emitOpTypeInst("xor", i);
         }

         public void caseShrInst(ShrInst i) {
            this.emitOpTypeInst("shr", i);
         }

         public void caseUshrInst(UshrInst i) {
            this.emitOpTypeInst("ushr", i);
         }

         public void caseIncInst(IncInst i) {
            if (((ValueBox)i.getUseBoxes().get(0)).getValue() != ((ValueBox)i.getDefBoxes().get(0)).getValue()) {
               throw new RuntimeException("iinc def and use boxes don't match");
            } else {
               JasminClass.this.emit("iinc " + JasminClass.this.localToSlot.get(i.getLocal()) + " " + i.getConstant());
            }
         }

         public void caseArrayLengthInst(ArrayLengthInst i) {
            JasminClass.this.emit("arraylength");
         }

         public void caseNegInst(NegInst i) {
            this.emitOpTypeInst("neg", i);
         }

         public void caseNewArrayInst(NewArrayInst i) {
            if (i.getBaseType() instanceof RefType) {
               JasminClass.this.emit("anewarray " + AbstractJasminClass.slashify(((RefType)i.getBaseType()).getClassName()));
            } else if (i.getBaseType() instanceof ArrayType) {
               JasminClass.this.emit("anewarray " + AbstractJasminClass.jasminDescriptorOf(i.getBaseType()));
            } else {
               JasminClass.this.emit("newarray " + i.getBaseType().toString());
            }

         }

         public void caseNewMultiArrayInst(NewMultiArrayInst i) {
            JasminClass.this.emit("multianewarray " + AbstractJasminClass.jasminDescriptorOf((Type)i.getBaseType()) + " " + i.getDimensionCount());
         }

         public void caseLookupSwitchInst(LookupSwitchInst i) {
            JasminClass.this.emit("lookupswitch");
            List<IntConstant> lookupValues = i.getLookupValues();
            List<Unit> targets = i.getTargets();

            for(int j = 0; j < lookupValues.size(); ++j) {
               JasminClass.this.emit("  " + lookupValues.get(j) + " : " + (String)JasminClass.this.unitToLabel.get(targets.get(j)));
            }

            JasminClass.this.emit("  default : " + (String)JasminClass.this.unitToLabel.get(i.getDefaultTarget()));
         }

         public void caseTableSwitchInst(TableSwitchInst i) {
            JasminClass.this.emit("tableswitch " + i.getLowIndex() + " ; high = " + i.getHighIndex());
            List<Unit> targets = i.getTargets();

            for(int j = 0; j < targets.size(); ++j) {
               JasminClass.this.emit("  " + (String)JasminClass.this.unitToLabel.get(targets.get(j)));
            }

            JasminClass.this.emit("default : " + (String)JasminClass.this.unitToLabel.get(i.getDefaultTarget()));
         }

         private boolean isDwordType(Type t) {
            return t instanceof LongType || t instanceof DoubleType || t instanceof DoubleWordType;
         }

         public void caseDup1Inst(Dup1Inst i) {
            Type firstOpType = i.getOp1Type();
            if (this.isDwordType(firstOpType)) {
               JasminClass.this.emit("dup2");
            } else {
               JasminClass.this.emit("dup");
            }

         }

         public void caseDup2Inst(Dup2Inst i) {
            Type firstOpType = i.getOp1Type();
            Type secondOpType = i.getOp2Type();
            if (this.isDwordType(firstOpType)) {
               JasminClass.this.emit("dup2");
               if (this.isDwordType(secondOpType)) {
                  JasminClass.this.emit("dup2");
               } else {
                  JasminClass.this.emit("dup");
               }
            } else if (this.isDwordType(secondOpType)) {
               if (this.isDwordType(firstOpType)) {
                  JasminClass.this.emit("dup2");
               } else {
                  JasminClass.this.emit("dup");
               }

               JasminClass.this.emit("dup2");
            } else {
               JasminClass.this.emit("dup2");
            }

         }

         public void caseDup1_x1Inst(Dup1_x1Inst i) {
            Type opType = i.getOp1Type();
            Type underType = i.getUnder1Type();
            if (this.isDwordType(opType)) {
               if (this.isDwordType(underType)) {
                  JasminClass.this.emit("dup2_x2");
               } else {
                  JasminClass.this.emit("dup2_x1");
               }
            } else if (this.isDwordType(underType)) {
               JasminClass.this.emit("dup_x2");
            } else {
               JasminClass.this.emit("dup_x1");
            }

         }

         public void caseDup1_x2Inst(Dup1_x2Inst i) {
            Type opType = i.getOp1Type();
            Type under1Type = i.getUnder1Type();
            Type under2Type = i.getUnder2Type();
            if (this.isDwordType(opType)) {
               if (this.isDwordType(under1Type) || this.isDwordType(under2Type)) {
                  throw new RuntimeException("magic not implemented yet");
               }

               JasminClass.this.emit("dup2_x2");
            } else if (this.isDwordType(under1Type) || this.isDwordType(under2Type)) {
               throw new RuntimeException("magic not implemented yet");
            }

            JasminClass.this.emit("dup_x2");
         }

         public void caseDup2_x1Inst(Dup2_x1Inst i) {
            Type op1Type = i.getOp1Type();
            Type op2Type = i.getOp2Type();
            Type under1Type = i.getUnder1Type();
            if (this.isDwordType(under1Type)) {
               if (!this.isDwordType(op1Type) && !this.isDwordType(op2Type)) {
                  throw new RuntimeException("magic not implemented yet");
               }

               JasminClass.this.emit("dup2_x2");
            } else if (this.isDwordType(op1Type) && op2Type != null || this.isDwordType(op2Type)) {
               throw new RuntimeException("magic not implemented yet");
            }

            JasminClass.this.emit("dup2_x1");
         }

         public void caseDup2_x2Inst(Dup2_x2Inst i) {
            Type op1Type = i.getOp1Type();
            Type op2Type = i.getOp2Type();
            Type under1Type = i.getUnder1Type();
            Type under2Type = i.getUnder2Type();
            boolean malformed = true;
            if (this.isDwordType(op1Type)) {
               if (op2Type == null && under1Type != null && (under2Type == null && this.isDwordType(under1Type) || !this.isDwordType(under1Type) && under2Type != null && !this.isDwordType(under2Type))) {
                  malformed = false;
               }
            } else if (op1Type != null && op2Type != null && !this.isDwordType(op2Type) && (under2Type == null && this.isDwordType(under1Type) || under1Type != null && !this.isDwordType(under1Type) && under2Type != null && !this.isDwordType(under2Type))) {
               malformed = false;
            }

            if (malformed) {
               throw new RuntimeException("magic not implemented yet");
            } else {
               JasminClass.this.emit("dup2_x2");
            }
         }

         public void caseSwapInst(SwapInst i) {
            JasminClass.this.emit("swap");
         }
      });
   }

   private void calculateStackHeight(Block aBlock) {
      int blockHeight = (Integer)this.blockToStackHeight.get(aBlock);
      if (blockHeight > this.maxStackHeight) {
         this.maxStackHeight = blockHeight;
      }

      Iterator var3 = aBlock.iterator();

      while(var3.hasNext()) {
         Unit u = (Unit)var3.next();
         Inst nInst = (Inst)u;
         blockHeight -= nInst.getInMachineCount();
         if (blockHeight < 0) {
            throw new RuntimeException("Negative Stack height has been attained in :" + aBlock.getBody().getMethod().getSignature() + " \nStackHeight: " + blockHeight + "\nAt instruction:" + nInst + "\nBlock:\n" + aBlock + "\n\nMethod: " + aBlock.getBody().getMethod().getName() + "\n" + aBlock.getBody().getMethod());
         }

         blockHeight += nInst.getOutMachineCount();
         if (blockHeight > this.maxStackHeight) {
            this.maxStackHeight = blockHeight;
         }
      }

      var3 = aBlock.getSuccs().iterator();

      while(var3.hasNext()) {
         Block b = (Block)var3.next();
         Integer i = (Integer)this.blockToStackHeight.get(b);
         if (i != null) {
            if (i != blockHeight) {
               throw new RuntimeException(aBlock.getBody().getMethod().getSignature() + ": incoherent stack height at block merge point " + b + aBlock + "\ncomputed blockHeight == " + blockHeight + " recorded blockHeight = " + i);
            }
         } else {
            this.blockToStackHeight.put(b, new Integer(blockHeight));
            this.calculateStackHeight(b);
         }
      }

   }

   private void calculateLogicalStackHeightCheck(Block aBlock) {
      int blockHeight = (Integer)this.blockToLogicalStackHeight.get(aBlock);

      Iterator var3;
      Inst nInst;
      for(var3 = aBlock.iterator(); var3.hasNext(); blockHeight += nInst.getOutCount()) {
         Unit u = (Unit)var3.next();
         nInst = (Inst)u;
         blockHeight -= nInst.getInCount();
         if (blockHeight < 0) {
            throw new RuntimeException("Negative Stack Logical height has been attained: \nStackHeight: " + blockHeight + "\nAt instruction:" + nInst + "\nBlock:\n" + aBlock + "\n\nMethod: " + aBlock.getBody().getMethod().getName() + "\n" + aBlock.getBody().getMethod());
         }
      }

      var3 = aBlock.getSuccs().iterator();

      while(var3.hasNext()) {
         Block b = (Block)var3.next();
         Integer i = (Integer)this.blockToLogicalStackHeight.get(b);
         if (i != null) {
            if (i != blockHeight) {
               throw new RuntimeException("incoherent logical stack height at block merge point " + b + aBlock);
            }
         } else {
            this.blockToLogicalStackHeight.put(b, new Integer(blockHeight));
            this.calculateLogicalStackHeightCheck(b);
         }
      }

   }
}
