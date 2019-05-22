package soot.baf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import soot.AbstractASMBackend;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.RefType;
import soot.ShortType;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.StmtAddressType;
import soot.Trap;
import soot.Type;
import soot.TypeSwitch;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.baf.internal.BafLocal;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.Constant;
import soot.jimple.ConstantSwitch;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IdentityRef;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.MethodHandle;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.options.Options;
import soot.tagkit.LineNumberTag;
import soot.util.Chain;
import soot.util.backend.ASMBackendUtils;

public class BafASMBackend extends AbstractASMBackend {
   protected final Map<Unit, Label> branchTargetLabels = new HashMap();
   protected final Map<Local, Integer> localToSlot = new HashMap();

   protected Label getBranchTargetLabel(Unit target) {
      return (Label)this.branchTargetLabels.get(target);
   }

   public BafASMBackend(SootClass sc, int javaVersion) {
      super(sc, javaVersion);
   }

   protected int getMinJavaVersion(SootMethod method) {
      BafBody body = this.getBafBody(method);
      int minVersion = 2;
      if (method.getDeclaringClass().isInterface() && method.isStatic() && !method.isStaticInitializer()) {
         return 9;
      } else {
         Iterator var4 = body.getUnits().iterator();

         while(var4.hasNext()) {
            Unit u = (Unit)var4.next();
            if (u instanceof DynamicInvokeInst) {
               return 8;
            }

            if (u instanceof PushInst && ((PushInst)u).getConstant() instanceof ClassConstant) {
               minVersion = 6;
            }
         }

         return minVersion;
      }
   }

   protected void generateMethodBody(MethodVisitor mv, SootMethod method) {
      BafBody body = this.getBafBody(method);
      Chain<Unit> instructions = body.getUnits();
      Iterator var5 = body.getUnitBoxes(true).iterator();

      while(var5.hasNext()) {
         UnitBox box = (UnitBox)var5.next();
         Unit u = box.getUnit();
         if (!this.branchTargetLabels.containsKey(u)) {
            this.branchTargetLabels.put(u, new Label());
         }
      }

      Label startLabel = null;
      if (Options.v().write_local_annotations()) {
         startLabel = new Label();
         mv.visitLabel(startLabel);
      }

      Iterator var16 = body.getTraps().iterator();

      Label endLabel;
      while(var16.hasNext()) {
         Trap trap = (Trap)var16.next();
         if (trap.getBeginUnit() != trap.getEndUnit()) {
            Label start = (Label)this.branchTargetLabels.get(trap.getBeginUnit());
            endLabel = (Label)this.branchTargetLabels.get(trap.getEndUnit());
            Label handler = (Label)this.branchTargetLabels.get(trap.getHandlerUnit());
            String type = ASMBackendUtils.slashify(trap.getException().getName());
            mv.visitTryCatchBlock(start, endLabel, handler, type);
         }
      }

      int localCount = 0;
      int[] paramSlots = new int[method.getParameterCount()];
      Set<Local> assignedLocals = new HashSet();
      if (!method.isStatic()) {
         ++localCount;
      }

      for(int i = 0; i < method.getParameterCount(); ++i) {
         paramSlots[i] = localCount;
         localCount += ASMBackendUtils.sizeOfType(method.getParameterType(i));
      }

      Iterator var22 = instructions.iterator();

      while(true) {
         int slot;
         Local local;
         while(true) {
            Unit u;
            do {
               do {
                  if (!var22.hasNext()) {
                     var22 = body.getLocals().iterator();

                     while(var22.hasNext()) {
                        Local local = (Local)var22.next();
                        if (assignedLocals.add(local)) {
                           this.localToSlot.put(local, localCount);
                           localCount += ASMBackendUtils.sizeOfType(local.getType());
                        }
                     }

                     for(var22 = instructions.iterator(); var22.hasNext(); this.generateInstruction(mv, (Inst)u)) {
                        u = (Unit)var22.next();
                        if (this.branchTargetLabels.containsKey(u)) {
                           mv.visitLabel((Label)this.branchTargetLabels.get(u));
                        }

                        if (u.hasTag("LineNumberTag")) {
                           LineNumberTag lnt = (LineNumberTag)u.getTag("LineNumberTag");
                           Label l;
                           if (this.branchTargetLabels.containsKey(u)) {
                              l = (Label)this.branchTargetLabels.get(u);
                           } else {
                              l = new Label();
                              mv.visitLabel(l);
                           }

                           mv.visitLineNumber(lnt.getLineNumber(), l);
                        }
                     }

                     if (Options.v().write_local_annotations()) {
                        endLabel = new Label();
                        mv.visitLabel(endLabel);
                        Iterator var26 = body.getLocals().iterator();

                        while(var26.hasNext()) {
                           local = (Local)var26.next();
                           Integer slot = (Integer)this.localToSlot.get(local);
                           if (slot != null) {
                              BafLocal l = (BafLocal)local;
                              if (l.getOriginalLocal() != null) {
                                 Local jimpleLocal = l.getOriginalLocal();
                                 if (jimpleLocal != null) {
                                    mv.visitLocalVariable(jimpleLocal.getName(), ASMBackendUtils.toTypeDesc(jimpleLocal.getType()), (String)null, startLabel, endLabel, slot);
                                 }
                              }
                           }
                        }
                     }

                     return;
                  }

                  u = (Unit)var22.next();
               } while(!(u instanceof IdentityInst));
            } while(!(((IdentityInst)u).getLeftOp() instanceof Local));

            local = (Local)((IdentityInst)u).getLeftOp();
            IdentityRef identity = (IdentityRef)((IdentityInst)u).getRightOp();
            slot = 0;
            if (identity instanceof ThisRef) {
               if (method.isStatic()) {
                  throw new RuntimeException("Attempting to use 'this' in static method");
               }
               break;
            }

            if (identity instanceof ParameterRef) {
               slot = paramSlots[((ParameterRef)identity).getIndex()];
               break;
            }
         }

         this.localToSlot.put(local, slot);
         assignedLocals.add(local);
      }
   }

   protected void generateInstruction(final MethodVisitor mv, Inst inst) {
      inst.apply(new InstSwitch() {
         public void caseReturnVoidInst(ReturnVoidInst i) {
            mv.visitInsn(177);
         }

         public void caseReturnInst(ReturnInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseArrayType(ArrayType t) {
                  mv.visitInsn(176);
               }

               public void caseBooleanType(BooleanType t) {
                  mv.visitInsn(172);
               }

               public void caseByteType(ByteType t) {
                  mv.visitInsn(172);
               }

               public void caseCharType(CharType t) {
                  mv.visitInsn(172);
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(175);
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(174);
               }

               public void caseIntType(IntType t) {
                  mv.visitInsn(172);
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(173);
               }

               public void caseRefType(RefType t) {
                  mv.visitInsn(176);
               }

               public void caseShortType(ShortType t) {
                  mv.visitInsn(172);
               }

               public void caseNullType(NullType t) {
                  mv.visitInsn(176);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid return type " + t.toString());
               }
            });
         }

         public void caseNopInst(NopInst i) {
            mv.visitInsn(0);
         }

         public void caseJSRInst(JSRInst i) {
            mv.visitJumpInsn(168, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
         }

         public void casePushInst(PushInst i) {
            Constant c = i.getConstant();
            if (c instanceof IntConstant) {
               int vxxx = ((IntConstant)c).value;
               switch(vxxx) {
               case -1:
                  mv.visitInsn(2);
                  break;
               case 0:
                  mv.visitInsn(3);
                  break;
               case 1:
                  mv.visitInsn(4);
                  break;
               case 2:
                  mv.visitInsn(5);
                  break;
               case 3:
                  mv.visitInsn(6);
                  break;
               case 4:
                  mv.visitInsn(7);
                  break;
               case 5:
                  mv.visitInsn(8);
                  break;
               default:
                  if (vxxx >= -128 && vxxx <= 127) {
                     mv.visitIntInsn(16, vxxx);
                  } else if (vxxx >= -32768 && vxxx <= 32767) {
                     mv.visitIntInsn(17, vxxx);
                  } else {
                     mv.visitLdcInsn(vxxx);
                  }
               }
            } else if (c instanceof StringConstant) {
               mv.visitLdcInsn(((StringConstant)c).value);
            } else if (c instanceof ClassConstant) {
               mv.visitLdcInsn(org.objectweb.asm.Type.getType(((ClassConstant)c).getValue()));
            } else if (c instanceof DoubleConstant) {
               double v = ((DoubleConstant)c).value;
               if ((new Double(v)).equals(0.0D)) {
                  mv.visitInsn(14);
               } else if (v == 1.0D) {
                  mv.visitInsn(15);
               } else {
                  mv.visitLdcInsn(v);
               }
            } else if (c instanceof FloatConstant) {
               float vx = ((FloatConstant)c).value;
               if ((new Float(vx)).equals(0.0F)) {
                  mv.visitInsn(11);
               } else if (vx == 1.0F) {
                  mv.visitInsn(12);
               } else if (vx == 2.0F) {
                  mv.visitInsn(13);
               } else {
                  mv.visitLdcInsn(vx);
               }
            } else if (c instanceof LongConstant) {
               long vxx = ((LongConstant)c).value;
               if (vxx == 0L) {
                  mv.visitInsn(9);
               } else if (vxx == 1L) {
                  mv.visitInsn(10);
               } else {
                  mv.visitLdcInsn(vxx);
               }
            } else if (c instanceof NullConstant) {
               mv.visitInsn(1);
            } else {
               if (!(c instanceof MethodHandle)) {
                  throw new RuntimeException("unsupported opcode");
               }

               SootMethodRef ref = ((MethodHandle)c).getMethodRef();
               byte tag;
               if (ref.isStatic()) {
                  tag = 6;
               } else if (ref.declaringClass().isInterface()) {
                  tag = 9;
               } else {
                  tag = 5;
               }

               Handle handle = new Handle(tag, ref.declaringClass().getName(), ref.name(), ref.getSignature(), ref.declaringClass().isInnerClass());
               mv.visitLdcInsn(handle);
            }

         }

         public void casePopInst(PopInst i) {
            if (i.getWordCount() == 2) {
               mv.visitInsn(88);
            } else {
               mv.visitInsn(87);
            }

         }

         public void caseIdentityInst(IdentityInst i) {
            Value l = i.getLeftOp();
            Value r = i.getRightOp();
            if (r instanceof CaughtExceptionRef && l instanceof Local) {
               mv.visitVarInsn(58, (Integer)BafASMBackend.this.localToSlot.get(l));
            }

         }

         public void caseStoreInst(StoreInst i) {
            final int slot = (Integer)BafASMBackend.this.localToSlot.get(i.getLocal());
            i.getOpType().apply(new TypeSwitch() {
               public void caseArrayType(ArrayType t) {
                  mv.visitVarInsn(58, slot);
               }

               public void caseBooleanType(BooleanType t) {
                  mv.visitVarInsn(54, slot);
               }

               public void caseByteType(ByteType t) {
                  mv.visitVarInsn(54, slot);
               }

               public void caseCharType(CharType t) {
                  mv.visitVarInsn(54, slot);
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitVarInsn(57, slot);
               }

               public void caseFloatType(FloatType t) {
                  mv.visitVarInsn(56, slot);
               }

               public void caseIntType(IntType t) {
                  mv.visitVarInsn(54, slot);
               }

               public void caseLongType(LongType t) {
                  mv.visitVarInsn(55, slot);
               }

               public void caseRefType(RefType t) {
                  mv.visitVarInsn(58, slot);
               }

               public void caseShortType(ShortType t) {
                  mv.visitVarInsn(54, slot);
               }

               public void caseStmtAddressType(StmtAddressType t) {
                  throw new RuntimeException("JSR not supported, use recent Java compiler!");
               }

               public void caseNullType(NullType t) {
                  mv.visitVarInsn(58, slot);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid local type: " + t);
               }
            });
         }

         public void caseGotoInst(GotoInst i) {
            mv.visitJumpInsn(167, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
         }

         public void caseLoadInst(LoadInst i) {
            final int slot = (Integer)BafASMBackend.this.localToSlot.get(i.getLocal());
            i.getOpType().apply(new TypeSwitch() {
               public void caseArrayType(ArrayType t) {
                  mv.visitVarInsn(25, slot);
               }

               public void caseBooleanType(BooleanType t) {
                  mv.visitVarInsn(21, slot);
               }

               public void caseByteType(ByteType t) {
                  mv.visitVarInsn(21, slot);
               }

               public void caseCharType(CharType t) {
                  mv.visitVarInsn(21, slot);
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitVarInsn(24, slot);
               }

               public void caseFloatType(FloatType t) {
                  mv.visitVarInsn(23, slot);
               }

               public void caseIntType(IntType t) {
                  mv.visitVarInsn(21, slot);
               }

               public void caseLongType(LongType t) {
                  mv.visitVarInsn(22, slot);
               }

               public void caseRefType(RefType t) {
                  mv.visitVarInsn(25, slot);
               }

               public void caseShortType(ShortType t) {
                  mv.visitVarInsn(21, slot);
               }

               public void caseNullType(NullType t) {
                  mv.visitVarInsn(25, slot);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid local type: " + t);
               }
            });
         }

         public void caseArrayWriteInst(ArrayWriteInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseArrayType(ArrayType t) {
                  mv.visitInsn(83);
               }

               public void caseBooleanType(BooleanType t) {
                  mv.visitInsn(84);
               }

               public void caseByteType(ByteType t) {
                  mv.visitInsn(84);
               }

               public void caseCharType(CharType t) {
                  mv.visitInsn(85);
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(82);
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(81);
               }

               public void caseIntType(IntType t) {
                  mv.visitInsn(79);
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(80);
               }

               public void caseRefType(RefType t) {
                  mv.visitInsn(83);
               }

               public void caseShortType(ShortType t) {
                  mv.visitInsn(86);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid type: " + t);
               }
            });
         }

         public void caseArrayReadInst(ArrayReadInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseArrayType(ArrayType t) {
                  mv.visitInsn(50);
               }

               public void caseBooleanType(BooleanType t) {
                  mv.visitInsn(51);
               }

               public void caseByteType(ByteType t) {
                  mv.visitInsn(51);
               }

               public void caseCharType(CharType t) {
                  mv.visitInsn(52);
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(49);
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(48);
               }

               public void caseIntType(IntType t) {
                  mv.visitInsn(46);
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(47);
               }

               public void caseRefType(RefType t) {
                  mv.visitInsn(50);
               }

               public void caseShortType(ShortType t) {
                  mv.visitInsn(53);
               }

               public void caseNullType(NullType t) {
                  mv.visitInsn(50);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("Invalid type: " + t);
               }
            });
         }

         public void caseIfNullInst(IfNullInst i) {
            mv.visitJumpInsn(198, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
         }

         public void caseIfNonNullInst(IfNonNullInst i) {
            mv.visitJumpInsn(199, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
         }

         public void caseIfEqInst(IfEqInst i) {
            mv.visitJumpInsn(153, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
         }

         public void caseIfNeInst(IfNeInst i) {
            mv.visitJumpInsn(154, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
         }

         public void caseIfGtInst(IfGtInst i) {
            mv.visitJumpInsn(157, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
         }

         public void caseIfGeInst(IfGeInst i) {
            mv.visitJumpInsn(156, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
         }

         public void caseIfLtInst(IfLtInst i) {
            mv.visitJumpInsn(155, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
         }

         public void caseIfLeInst(IfLeInst i) {
            mv.visitJumpInsn(158, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
         }

         public void caseIfCmpEqInst(final IfCmpEqInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseArrayType(ArrayType t) {
                  mv.visitJumpInsn(165, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseBooleanType(BooleanType t) {
                  mv.visitJumpInsn(159, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseByteType(ByteType t) {
                  mv.visitJumpInsn(159, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseCharType(CharType t) {
                  mv.visitJumpInsn(159, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(152);
                  mv.visitJumpInsn(153, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(150);
                  mv.visitJumpInsn(153, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseIntType(IntType t) {
                  mv.visitJumpInsn(159, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(148);
                  mv.visitJumpInsn(153, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseRefType(RefType t) {
                  mv.visitJumpInsn(165, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseShortType(ShortType t) {
                  mv.visitJumpInsn(159, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseNullType(NullType t) {
                  mv.visitJumpInsn(165, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseIfCmpNeInst(final IfCmpNeInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseArrayType(ArrayType t) {
                  mv.visitJumpInsn(166, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseBooleanType(BooleanType t) {
                  mv.visitJumpInsn(160, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseByteType(ByteType t) {
                  mv.visitJumpInsn(160, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseCharType(CharType t) {
                  mv.visitJumpInsn(160, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(152);
                  mv.visitJumpInsn(154, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(150);
                  mv.visitJumpInsn(154, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseIntType(IntType t) {
                  mv.visitJumpInsn(160, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(148);
                  mv.visitJumpInsn(154, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseRefType(RefType t) {
                  mv.visitJumpInsn(166, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseShortType(ShortType t) {
                  mv.visitJumpInsn(160, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseNullType(NullType t) {
                  mv.visitJumpInsn(166, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseIfCmpGtInst(final IfCmpGtInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseBooleanType(BooleanType t) {
                  mv.visitJumpInsn(163, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseByteType(ByteType t) {
                  mv.visitJumpInsn(163, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseCharType(CharType t) {
                  mv.visitJumpInsn(163, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(152);
                  mv.visitJumpInsn(157, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(150);
                  mv.visitJumpInsn(157, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseIntType(IntType t) {
                  mv.visitJumpInsn(163, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(148);
                  mv.visitJumpInsn(157, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseShortType(ShortType t) {
                  mv.visitJumpInsn(163, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseIfCmpGeInst(final IfCmpGeInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseBooleanType(BooleanType t) {
                  mv.visitJumpInsn(162, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseByteType(ByteType t) {
                  mv.visitJumpInsn(162, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseCharType(CharType t) {
                  mv.visitJumpInsn(162, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(152);
                  mv.visitJumpInsn(156, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(150);
                  mv.visitJumpInsn(156, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseIntType(IntType t) {
                  mv.visitJumpInsn(162, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(148);
                  mv.visitJumpInsn(156, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseShortType(ShortType t) {
                  mv.visitJumpInsn(162, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseIfCmpLtInst(final IfCmpLtInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseBooleanType(BooleanType t) {
                  mv.visitJumpInsn(161, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseByteType(ByteType t) {
                  mv.visitJumpInsn(161, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseCharType(CharType t) {
                  mv.visitJumpInsn(161, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(152);
                  mv.visitJumpInsn(155, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(150);
                  mv.visitJumpInsn(155, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseIntType(IntType t) {
                  mv.visitJumpInsn(161, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(148);
                  mv.visitJumpInsn(155, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseShortType(ShortType t) {
                  mv.visitJumpInsn(161, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseIfCmpLeInst(final IfCmpLeInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseBooleanType(BooleanType t) {
                  mv.visitJumpInsn(164, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseByteType(ByteType t) {
                  mv.visitJumpInsn(164, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseCharType(CharType t) {
                  mv.visitJumpInsn(164, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(152);
                  mv.visitJumpInsn(158, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(150);
                  mv.visitJumpInsn(158, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseIntType(IntType t) {
                  mv.visitJumpInsn(164, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(148);
                  mv.visitJumpInsn(158, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void caseShortType(ShortType t) {
                  mv.visitJumpInsn(164, BafASMBackend.this.getBranchTargetLabel(i.getTarget()));
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseStaticGetInst(StaticGetInst i) {
            SootFieldRef field = i.getFieldRef();
            mv.visitFieldInsn(178, ASMBackendUtils.slashify(field.declaringClass().getName()), field.name(), ASMBackendUtils.toTypeDesc(field.type()));
         }

         public void caseStaticPutInst(StaticPutInst i) {
            SootFieldRef field = i.getFieldRef();
            mv.visitFieldInsn(179, ASMBackendUtils.slashify(field.declaringClass().getName()), field.name(), ASMBackendUtils.toTypeDesc(field.type()));
         }

         public void caseFieldGetInst(FieldGetInst i) {
            SootFieldRef field = i.getFieldRef();
            mv.visitFieldInsn(180, ASMBackendUtils.slashify(field.declaringClass().getName()), field.name(), ASMBackendUtils.toTypeDesc(field.type()));
         }

         public void caseFieldPutInst(FieldPutInst i) {
            SootFieldRef field = i.getFieldRef();
            mv.visitFieldInsn(181, ASMBackendUtils.slashify(field.declaringClass().getName()), field.name(), ASMBackendUtils.toTypeDesc(field.type()));
         }

         public void caseInstanceCastInst(InstanceCastInst i) {
            Type castType = i.getCastType();
            if (castType instanceof RefType) {
               mv.visitTypeInsn(192, ASMBackendUtils.slashify(((RefType)castType).getClassName()));
            } else if (castType instanceof ArrayType) {
               mv.visitTypeInsn(192, ASMBackendUtils.toTypeDesc(castType));
            }

         }

         public void caseInstanceOfInst(InstanceOfInst i) {
            Type checkType = i.getCheckType();
            if (checkType instanceof RefType) {
               mv.visitTypeInsn(193, ASMBackendUtils.slashify(((RefType)checkType).getClassName()));
            } else if (checkType instanceof ArrayType) {
               mv.visitTypeInsn(193, ASMBackendUtils.toTypeDesc(checkType));
            }

         }

         public void casePrimitiveCastInst(PrimitiveCastInst i) {
            Type from = i.getFromType();
            final Type to = i.getToType();
            from.apply(new TypeSwitch() {
               public void caseBooleanType(BooleanType t) {
                  this.emitIntToTypeCast();
               }

               public void caseByteType(ByteType t) {
                  this.emitIntToTypeCast();
               }

               public void caseCharType(CharType t) {
                  this.emitIntToTypeCast();
               }

               public void caseDoubleType(DoubleType t) {
                  if (to.equals(IntType.v())) {
                     mv.visitInsn(142);
                  } else if (to.equals(LongType.v())) {
                     mv.visitInsn(143);
                  } else {
                     if (!to.equals(FloatType.v())) {
                        throw new RuntimeException("invalid to-type from double");
                     }

                     mv.visitInsn(144);
                  }

               }

               public void caseFloatType(FloatType t) {
                  if (to.equals(IntType.v())) {
                     mv.visitInsn(139);
                  } else if (to.equals(LongType.v())) {
                     mv.visitInsn(140);
                  } else {
                     if (!to.equals(DoubleType.v())) {
                        throw new RuntimeException("invalid to-type from float");
                     }

                     mv.visitInsn(141);
                  }

               }

               public void caseIntType(IntType t) {
                  this.emitIntToTypeCast();
               }

               public void caseLongType(LongType t) {
                  if (to.equals(IntType.v())) {
                     mv.visitInsn(136);
                  } else if (to.equals(FloatType.v())) {
                     mv.visitInsn(137);
                  } else {
                     if (!to.equals(DoubleType.v())) {
                        throw new RuntimeException("invalid to-type from long");
                     }

                     mv.visitInsn(138);
                  }

               }

               public void caseShortType(ShortType t) {
                  this.emitIntToTypeCast();
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid from-type: " + t);
               }

               private void emitIntToTypeCast() {
                  if (to.equals(ByteType.v())) {
                     mv.visitInsn(145);
                  } else if (to.equals(CharType.v())) {
                     mv.visitInsn(146);
                  } else if (to.equals(ShortType.v())) {
                     mv.visitInsn(147);
                  } else if (to.equals(FloatType.v())) {
                     mv.visitInsn(134);
                  } else if (to.equals(LongType.v())) {
                     mv.visitInsn(133);
                  } else if (to.equals(DoubleType.v())) {
                     mv.visitInsn(135);
                  } else if (!to.equals(IntType.v()) && !to.equals(BooleanType.v())) {
                     throw new RuntimeException("invalid to-type from int");
                  }

               }
            });
         }

         public void caseDynamicInvokeInst(DynamicInvokeInst i) {
            SootMethodRef m = i.getMethodRef();
            SootMethodRef bsm = i.getBootstrapMethodRef();
            List<Value> args = i.getBootstrapArgs();
            final Object[] argsArray = new Object[args.size()];
            final int index = 0;

            for(Iterator var7 = args.iterator(); var7.hasNext(); ++index) {
               Value v = (Value)var7.next();
               v.apply(new ConstantSwitch() {
                  public void defaultCase(Object object) {
                     throw new RuntimeException("Unexpected constant type!");
                  }

                  public void caseStringConstant(StringConstant v) {
                     argsArray[index] = v.value;
                  }

                  public void caseNullConstant(NullConstant v) {
                     throw new RuntimeException("Unexpected NullType as argument-type in invokedynamic!");
                  }

                  public void caseMethodHandle(MethodHandle handle) {
                     SootMethodRef methodRef = handle.getMethodRef();
                     argsArray[index] = new Handle(handle.tag, ASMBackendUtils.slashify(methodRef.declaringClass().getName()), methodRef.name(), ASMBackendUtils.toTypeDesc(methodRef));
                  }

                  public void caseLongConstant(LongConstant v) {
                     argsArray[index] = v.value;
                  }

                  public void caseIntConstant(IntConstant v) {
                     argsArray[index] = v.value;
                  }

                  public void caseFloatConstant(FloatConstant v) {
                     argsArray[index] = v.value;
                  }

                  public void caseDoubleConstant(DoubleConstant v) {
                     argsArray[index] = v.value;
                  }

                  public void caseClassConstant(ClassConstant v) {
                     argsArray[index] = org.objectweb.asm.Type.getType(v.getValue());
                  }
               });
            }

            mv.visitInvokeDynamicInsn(m.name(), ASMBackendUtils.toTypeDesc(m), new Handle(i.getHandleTag(), ASMBackendUtils.slashify(bsm.declaringClass().getName()), bsm.name(), ASMBackendUtils.toTypeDesc(bsm)), argsArray);
         }

         public void caseStaticInvokeInst(StaticInvokeInst i) {
            SootMethodRef m = i.getMethodRef();
            mv.visitMethodInsn(184, ASMBackendUtils.slashify(m.declaringClass().getName()), m.name(), ASMBackendUtils.toTypeDesc(m), m.declaringClass().isInterface() && !m.isStatic());
         }

         public void caseVirtualInvokeInst(VirtualInvokeInst i) {
            SootMethodRef m = i.getMethodRef();
            mv.visitMethodInsn(182, ASMBackendUtils.slashify(m.declaringClass().getName()), m.name(), ASMBackendUtils.toTypeDesc(m), m.declaringClass().isInterface());
         }

         public void caseInterfaceInvokeInst(InterfaceInvokeInst i) {
            SootMethodRef m = i.getMethodRef();
            SootClass declaration = m.declaringClass();
            boolean isInterface = true;
            if (!declaration.isPhantom() && !declaration.isInterface()) {
               isInterface = false;
            }

            mv.visitMethodInsn(185, ASMBackendUtils.slashify(declaration.getName()), m.name(), ASMBackendUtils.toTypeDesc(m), isInterface);
         }

         public void caseSpecialInvokeInst(SpecialInvokeInst i) {
            SootMethodRef m = i.getMethodRef();
            mv.visitMethodInsn(183, ASMBackendUtils.slashify(m.declaringClass().getName()), m.name(), ASMBackendUtils.toTypeDesc(m), m.declaringClass().isInterface());
         }

         public void caseThrowInst(ThrowInst i) {
            mv.visitInsn(191);
         }

         public void caseAddInst(AddInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseBooleanType(BooleanType t) {
                  mv.visitInsn(96);
               }

               public void caseByteType(ByteType t) {
                  mv.visitInsn(96);
               }

               public void caseCharType(CharType t) {
                  mv.visitInsn(96);
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(99);
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(98);
               }

               public void caseIntType(IntType t) {
                  mv.visitInsn(96);
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(97);
               }

               public void caseShortType(ShortType t) {
                  mv.visitInsn(96);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseAndInst(AndInst i) {
            if (i.getOpType().equals(LongType.v())) {
               mv.visitInsn(127);
            } else {
               mv.visitInsn(126);
            }

         }

         public void caseOrInst(OrInst i) {
            if (i.getOpType().equals(LongType.v())) {
               mv.visitInsn(129);
            } else {
               mv.visitInsn(128);
            }

         }

         public void caseXorInst(XorInst i) {
            if (i.getOpType().equals(LongType.v())) {
               mv.visitInsn(131);
            } else {
               mv.visitInsn(130);
            }

         }

         public void caseArrayLengthInst(ArrayLengthInst i) {
            mv.visitInsn(190);
         }

         public void caseCmpInst(CmpInst i) {
            mv.visitInsn(148);
         }

         public void caseCmpgInst(CmpgInst i) {
            if (i.getOpType().equals(FloatType.v())) {
               mv.visitInsn(150);
            } else {
               mv.visitInsn(152);
            }

         }

         public void caseCmplInst(CmplInst i) {
            if (i.getOpType().equals(FloatType.v())) {
               mv.visitInsn(149);
            } else {
               mv.visitInsn(151);
            }

         }

         public void caseDivInst(DivInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseBooleanType(BooleanType t) {
                  mv.visitInsn(108);
               }

               public void caseByteType(ByteType t) {
                  mv.visitInsn(108);
               }

               public void caseCharType(CharType t) {
                  mv.visitInsn(108);
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(111);
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(110);
               }

               public void caseIntType(IntType t) {
                  mv.visitInsn(108);
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(109);
               }

               public void caseShortType(ShortType t) {
                  mv.visitInsn(108);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseIncInst(IncInst i) {
            if (((ValueBox)i.getUseBoxes().get(0)).getValue() != ((ValueBox)i.getDefBoxes().get(0)).getValue()) {
               throw new RuntimeException("iinc def and use boxes don't match");
            } else if (i.getConstant() instanceof IntConstant) {
               mv.visitIincInsn((Integer)BafASMBackend.this.localToSlot.get(i.getLocal()), ((IntConstant)i.getConstant()).value);
            } else {
               throw new RuntimeException("Wrong constant type for increment!");
            }
         }

         public void caseMulInst(MulInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseBooleanType(BooleanType t) {
                  mv.visitInsn(104);
               }

               public void caseByteType(ByteType t) {
                  mv.visitInsn(104);
               }

               public void caseCharType(CharType t) {
                  mv.visitInsn(104);
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(107);
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(106);
               }

               public void caseIntType(IntType t) {
                  mv.visitInsn(104);
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(105);
               }

               public void caseShortType(ShortType t) {
                  mv.visitInsn(104);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseRemInst(RemInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseBooleanType(BooleanType t) {
                  mv.visitInsn(112);
               }

               public void caseByteType(ByteType t) {
                  mv.visitInsn(112);
               }

               public void caseCharType(CharType t) {
                  mv.visitInsn(112);
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(115);
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(114);
               }

               public void caseIntType(IntType t) {
                  mv.visitInsn(112);
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(113);
               }

               public void caseShortType(ShortType t) {
                  mv.visitInsn(112);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseSubInst(SubInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseBooleanType(BooleanType t) {
                  mv.visitInsn(100);
               }

               public void caseByteType(ByteType t) {
                  mv.visitInsn(100);
               }

               public void caseCharType(CharType t) {
                  mv.visitInsn(100);
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(103);
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(102);
               }

               public void caseIntType(IntType t) {
                  mv.visitInsn(100);
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(101);
               }

               public void caseShortType(ShortType t) {
                  mv.visitInsn(100);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseShlInst(ShlInst i) {
            if (i.getOpType().equals(LongType.v())) {
               mv.visitInsn(121);
            } else {
               mv.visitInsn(120);
            }

         }

         public void caseShrInst(ShrInst i) {
            if (i.getOpType().equals(LongType.v())) {
               mv.visitInsn(123);
            } else {
               mv.visitInsn(122);
            }

         }

         public void caseUshrInst(UshrInst i) {
            if (i.getOpType().equals(LongType.v())) {
               mv.visitInsn(125);
            } else {
               mv.visitInsn(124);
            }

         }

         public void caseNewInst(NewInst i) {
            mv.visitTypeInsn(187, ASMBackendUtils.slashify(i.getBaseType().getClassName()));
         }

         public void caseNegInst(NegInst i) {
            i.getOpType().apply(new TypeSwitch() {
               public void caseBooleanType(BooleanType t) {
                  mv.visitInsn(116);
               }

               public void caseByteType(ByteType t) {
                  mv.visitInsn(116);
               }

               public void caseCharType(CharType t) {
                  mv.visitInsn(116);
               }

               public void caseDoubleType(DoubleType t) {
                  mv.visitInsn(119);
               }

               public void caseFloatType(FloatType t) {
                  mv.visitInsn(118);
               }

               public void caseIntType(IntType t) {
                  mv.visitInsn(116);
               }

               public void caseLongType(LongType t) {
                  mv.visitInsn(117);
               }

               public void caseShortType(ShortType t) {
                  mv.visitInsn(116);
               }

               public void defaultCase(Type t) {
                  throw new RuntimeException("invalid type");
               }
            });
         }

         public void caseSwapInst(SwapInst i) {
            mv.visitInsn(95);
         }

         public void caseDup1Inst(Dup1Inst i) {
            if (ASMBackendUtils.sizeOfType(i.getOp1Type()) == 2) {
               mv.visitInsn(92);
            } else {
               mv.visitInsn(89);
            }

         }

         public void caseDup2Inst(Dup2Inst i) {
            Type firstOpType = i.getOp1Type();
            Type secondOpType = i.getOp2Type();
            if (ASMBackendUtils.sizeOfType(firstOpType) == 2) {
               mv.visitInsn(92);
               if (ASMBackendUtils.sizeOfType(secondOpType) == 2) {
                  mv.visitInsn(92);
               } else {
                  mv.visitInsn(89);
               }
            } else if (ASMBackendUtils.sizeOfType(secondOpType) == 2) {
               mv.visitInsn(89);
               mv.visitInsn(92);
            } else {
               mv.visitInsn(92);
            }

         }

         public void caseDup1_x1Inst(Dup1_x1Inst i) {
            Type opType = i.getOp1Type();
            Type underType = i.getUnder1Type();
            if (ASMBackendUtils.sizeOfType(opType) == 2) {
               if (ASMBackendUtils.sizeOfType(underType) == 2) {
                  mv.visitInsn(94);
               } else {
                  mv.visitInsn(93);
               }
            } else if (ASMBackendUtils.sizeOfType(underType) == 2) {
               mv.visitInsn(91);
            } else {
               mv.visitInsn(90);
            }

         }

         public void caseDup1_x2Inst(Dup1_x2Inst i) {
            int toSkip = ASMBackendUtils.sizeOfType(i.getUnder1Type()) + ASMBackendUtils.sizeOfType(i.getUnder2Type());
            if (ASMBackendUtils.sizeOfType(i.getOp1Type()) == 2) {
               if (toSkip != 2) {
                  throw new RuntimeException("magic not implemented yet");
               }

               mv.visitInsn(94);
            } else {
               if (toSkip != 2) {
                  throw new RuntimeException("magic not implemented yet");
               }

               mv.visitInsn(91);
            }

         }

         public void caseDup2_x1Inst(Dup2_x1Inst i) {
            int toDup = ASMBackendUtils.sizeOfType(i.getOp1Type()) + ASMBackendUtils.sizeOfType(i.getOp2Type());
            if (toDup == 2) {
               if (ASMBackendUtils.sizeOfType(i.getUnder1Type()) == 2) {
                  mv.visitInsn(94);
               } else {
                  mv.visitInsn(93);
               }

            } else {
               throw new RuntimeException("magic not implemented yet");
            }
         }

         public void caseDup2_x2Inst(Dup2_x2Inst i) {
            int toDup = ASMBackendUtils.sizeOfType(i.getOp1Type()) + ASMBackendUtils.sizeOfType(i.getOp2Type());
            int toSkip = ASMBackendUtils.sizeOfType(i.getUnder1Type()) + ASMBackendUtils.sizeOfType(i.getUnder2Type());
            if (toDup <= 2 && toSkip <= 2) {
               if (toDup == 2 && toSkip == 2) {
                  mv.visitInsn(94);
               } else {
                  throw new RuntimeException("VoidType not allowed in Dup2_x2 Instruction");
               }
            } else {
               throw new RuntimeException("magic not implemented yet");
            }
         }

         public void caseNewArrayInst(NewArrayInst i) {
            Type t = i.getBaseType();
            if (t instanceof RefType) {
               mv.visitTypeInsn(189, ASMBackendUtils.slashify(((RefType)t).getClassName()));
            } else if (t instanceof ArrayType) {
               mv.visitTypeInsn(189, ASMBackendUtils.toTypeDesc(t));
            } else {
               byte type;
               if (t.equals(BooleanType.v())) {
                  type = 4;
               } else if (t.equals(CharType.v())) {
                  type = 5;
               } else if (t.equals(FloatType.v())) {
                  type = 6;
               } else if (t.equals(DoubleType.v())) {
                  type = 7;
               } else if (t.equals(ByteType.v())) {
                  type = 8;
               } else if (t.equals(ShortType.v())) {
                  type = 9;
               } else if (t.equals(IntType.v())) {
                  type = 10;
               } else {
                  if (!t.equals(LongType.v())) {
                     throw new RuntimeException("invalid type");
                  }

                  type = 11;
               }

               mv.visitIntInsn(188, type);
            }

         }

         public void caseNewMultiArrayInst(NewMultiArrayInst i) {
            mv.visitMultiANewArrayInsn(ASMBackendUtils.toTypeDesc((Type)i.getBaseType()), i.getDimensionCount());
         }

         public void caseLookupSwitchInst(LookupSwitchInst i) {
            List<IntConstant> values = i.getLookupValues();
            List<Unit> targets = i.getTargets();
            int[] keys = new int[values.size()];
            Label[] labels = new Label[values.size()];

            for(int j = 0; j < values.size(); ++j) {
               keys[j] = ((IntConstant)values.get(j)).value;
               labels[j] = (Label)BafASMBackend.this.branchTargetLabels.get(targets.get(j));
            }

            mv.visitLookupSwitchInsn((Label)BafASMBackend.this.branchTargetLabels.get(i.getDefaultTarget()), keys, labels);
         }

         public void caseTableSwitchInst(TableSwitchInst i) {
            List<Unit> targets = i.getTargets();
            Label[] labels = new Label[targets.size()];

            for(int j = 0; j < targets.size(); ++j) {
               labels[j] = (Label)BafASMBackend.this.branchTargetLabels.get(targets.get(j));
            }

            mv.visitTableSwitchInsn(i.getLowIndex(), i.getHighIndex(), (Label)BafASMBackend.this.branchTargetLabels.get(i.getDefaultTarget()), labels);
         }

         public void caseEnterMonitorInst(EnterMonitorInst i) {
            mv.visitInsn(194);
         }

         public void caseExitMonitorInst(ExitMonitorInst i) {
            mv.visitInsn(195);
         }
      });
   }
}
