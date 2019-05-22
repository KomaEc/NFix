package soot.asm;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.Handle;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
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
import soot.MethodSource;
import soot.PackManager;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.coffi.Util;
import soot.jimple.AddExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.ConditionExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LongConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.MethodHandle;
import soot.jimple.MonitorStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.StringConstant;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UnopExpr;
import soot.options.Options;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.util.Chain;

final class AsmMethodSource implements MethodSource {
   private static final Operand DWORD_DUMMY = new Operand((AbstractInsnNode)null, (Value)null);
   private int nextLocal;
   private Map<Integer, Local> locals;
   private Multimap<LabelNode, UnitBox> labels;
   private Map<AbstractInsnNode, Unit> units;
   private ArrayList<Operand> stack;
   private Map<AbstractInsnNode, StackFrame> frames;
   private Multimap<LabelNode, UnitBox> trapHandlers;
   private JimpleBody body;
   private int lastLineNumber = -1;
   private final int maxLocals;
   private final InsnList instructions;
   private final List<LocalVariableNode> localVars;
   private final List<TryCatchBlockNode> tryCatchBlocks;
   private final Set<LabelNode> inlineExceptionLabels = new HashSet();
   private final Map<LabelNode, Unit> inlineExceptionHandlers = new HashMap();
   private final CastAndReturnInliner castAndReturnInliner = new CastAndReturnInliner();
   private Table<AbstractInsnNode, AbstractInsnNode, AsmMethodSource.Edge> edges;
   private ArrayDeque<AsmMethodSource.Edge> conversionWorklist;

   AsmMethodSource(int maxLocals, InsnList insns, List<LocalVariableNode> localVars, List<TryCatchBlockNode> tryCatchBlocks) {
      this.maxLocals = maxLocals;
      this.instructions = insns;
      this.localVars = localVars;
      this.tryCatchBlocks = tryCatchBlocks;
   }

   private StackFrame getFrame(AbstractInsnNode insn) {
      StackFrame frame = (StackFrame)this.frames.get(insn);
      if (frame == null) {
         frame = new StackFrame(this);
         this.frames.put(insn, frame);
      }

      return frame;
   }

   private Local getLocal(int idx) {
      if (idx >= this.maxLocals) {
         throw new IllegalArgumentException("Invalid local index: " + idx);
      } else {
         Integer i = idx;
         Local l = (Local)this.locals.get(i);
         if (l == null) {
            String name;
            if (this.localVars == null) {
               name = "l" + idx;
            } else {
               name = null;
               Iterator var5 = this.localVars.iterator();

               while(var5.hasNext()) {
                  LocalVariableNode lvn = (LocalVariableNode)var5.next();
                  if (lvn.index == idx) {
                     name = lvn.name;
                     break;
                  }
               }

               if (name == null) {
                  name = "l" + idx;
               }
            }

            l = Jimple.v().newLocal(name, UnknownType.v());
            this.locals.put(i, l);
         }

         return l;
      }
   }

   private void push(Operand opr) {
      this.stack.add(opr);
   }

   private void pushDual(Operand opr) {
      this.stack.add(DWORD_DUMMY);
      this.stack.add(opr);
   }

   private Operand peek() {
      return (Operand)this.stack.get(this.stack.size() - 1);
   }

   private void push(Type t, Operand opr) {
      if (AsmUtil.isDWord(t)) {
         this.pushDual(opr);
      } else {
         this.push(opr);
      }

   }

   private Operand pop() {
      if (this.stack.isEmpty()) {
         throw new RuntimeException("Stack underrun");
      } else {
         return (Operand)this.stack.remove(this.stack.size() - 1);
      }
   }

   private Operand popDual() {
      Operand o = this.pop();
      Operand o2 = this.pop();
      if (o2 != DWORD_DUMMY && o2 != o) {
         throw new AssertionError("Not dummy operand, " + o2.value + " -- " + o.value);
      } else {
         return o;
      }
   }

   private Operand pop(Type t) {
      return AsmUtil.isDWord(t) ? this.popDual() : this.pop();
   }

   private Operand popLocal(Operand o) {
      Value v = o.value;
      Local l = o.stack;
      if (l == null && !(v instanceof Local)) {
         l = o.stack = this.newStackLocal();
         this.setUnit(o.insn, Jimple.v().newAssignStmt(l, v));
         o.updateBoxes();
      }

      return o;
   }

   private Operand popImmediate(Operand o) {
      Value v = o.value;
      Local l = o.stack;
      if (l == null && !(v instanceof Local) && !(v instanceof Constant)) {
         l = o.stack = this.newStackLocal();
         this.setUnit(o.insn, Jimple.v().newAssignStmt(l, v));
         o.updateBoxes();
      }

      return o;
   }

   private Operand popStackConst(Operand o) {
      Value v = o.value;
      Local l = o.stack;
      if (l == null && !(v instanceof Constant)) {
         l = o.stack = this.newStackLocal();
         this.setUnit(o.insn, Jimple.v().newAssignStmt(l, v));
         o.updateBoxes();
      }

      return o;
   }

   private Operand popLocal() {
      return this.popLocal(this.pop());
   }

   private Operand popLocalDual() {
      return this.popLocal(this.popDual());
   }

   private Operand popLocal(Type t) {
      return AsmUtil.isDWord(t) ? this.popLocalDual() : this.popLocal();
   }

   private Operand popImmediate() {
      return this.popImmediate(this.pop());
   }

   private Operand popImmediateDual() {
      return this.popImmediate(this.popDual());
   }

   private Operand popImmediate(Type t) {
      return AsmUtil.isDWord(t) ? this.popImmediateDual() : this.popImmediate();
   }

   private Operand popStackConst() {
      return this.popStackConst(this.pop());
   }

   private Operand popStackConstDual() {
      return this.popStackConst(this.popDual());
   }

   private Operand popStackConst(Type t) {
      return AsmUtil.isDWord(t) ? this.popStackConstDual() : this.popStackConst();
   }

   void setUnit(AbstractInsnNode insn, Unit u) {
      if (Options.v().keep_line_number() && this.lastLineNumber >= 0) {
         Tag lineTag = u.getTag("LineNumberTag");
         if (lineTag == null) {
            Tag lineTag = new LineNumberTag(this.lastLineNumber);
            u.addTag(lineTag);
         } else if (((LineNumberTag)lineTag).getLineNumber() != this.lastLineNumber) {
            throw new RuntimeException("Line tag mismatch");
         }
      }

      Unit o = (Unit)this.units.put(insn, u);
      if (o != null) {
         throw new AssertionError(insn.getOpcode() + " already has a unit, " + o);
      }
   }

   void mergeUnits(AbstractInsnNode insn, Unit u) {
      Unit prev = (Unit)this.units.put(insn, u);
      if (prev != null) {
         Unit merged = new UnitContainer(new Unit[]{prev, u});
         this.units.put(insn, merged);
      }

   }

   Local newStackLocal() {
      Integer idx = this.nextLocal++;
      Local l = Jimple.v().newLocal("$stack" + idx, UnknownType.v());
      this.locals.put(idx, l);
      return l;
   }

   <A extends Unit> A getUnit(AbstractInsnNode insn) {
      return (Unit)this.units.get(insn);
   }

   private void assignReadOps(Local l) {
      if (!this.stack.isEmpty()) {
         Iterator var2 = this.stack.iterator();

         while(true) {
            Operand opr;
            int op;
            do {
               boolean noref;
               do {
                  do {
                     do {
                        do {
                           if (!var2.hasNext()) {
                              return;
                           }

                           opr = (Operand)var2.next();
                        } while(opr == DWORD_DUMMY);
                     } while(opr.stack != null);
                  } while(l == null && opr.value instanceof Local);

                  if (l == null || opr.value.equivTo(l)) {
                     break;
                  }

                  List<ValueBox> uses = opr.value.getUseBoxes();
                  noref = true;
                  Iterator var6 = uses.iterator();

                  while(var6.hasNext()) {
                     ValueBox use = (ValueBox)var6.next();
                     Value val = use.getValue();
                     if (val.equivTo(l)) {
                        noref = false;
                        break;
                     }
                  }
               } while(noref);

               op = opr.insn.getOpcode();
            } while(l == null && op != 180 && op != 178 && op < 46 && op > 53);

            Local stack = this.newStackLocal();
            opr.stack = stack;
            AssignStmt as = Jimple.v().newAssignStmt(stack, opr.value);
            opr.updateBoxes();
            this.setUnit(opr.insn, as);
         }
      }
   }

   private void convertGetFieldInsn(FieldInsnNode insn) {
      StackFrame frame = this.getFrame(insn);
      Operand[] out = frame.out();
      Operand opr;
      Type type;
      if (out == null) {
         SootClass declClass = Scene.v().getSootClass(AsmUtil.toQualifiedName(insn.owner));
         type = AsmUtil.toJimpleType(insn.desc);
         Object val;
         SootFieldRef ref;
         if (insn.getOpcode() == 178) {
            ref = Scene.v().makeFieldRef(declClass, insn.name, type, true);
            val = Jimple.v().newStaticFieldRef(ref);
         } else {
            Operand base = this.popLocal();
            ref = Scene.v().makeFieldRef(declClass, insn.name, type, false);
            InstanceFieldRef ifr = Jimple.v().newInstanceFieldRef(base.stackOrValue(), ref);
            val = ifr;
            base.addBox(ifr.getBaseBox());
            frame.in(base);
            frame.boxes(ifr.getBaseBox());
         }

         opr = new Operand(insn, (Value)val);
         frame.out(opr);
      } else {
         opr = out[0];
         type = ((FieldRef)opr.value()).getFieldRef().type();
         if (insn.getOpcode() == 180) {
            frame.mergeIn(this.pop());
         }
      }

      this.push(type, opr);
   }

   private void convertPutFieldInsn(FieldInsnNode insn) {
      boolean instance = insn.getOpcode() == 181;
      StackFrame frame = this.getFrame(insn);
      Operand[] out = frame.out();
      Operand opr;
      Operand rvalue;
      Type type;
      if (out == null) {
         SootClass declClass = Scene.v().getSootClass(AsmUtil.toQualifiedName(insn.owner));
         type = AsmUtil.toJimpleType(insn.desc);
         rvalue = this.popImmediate(type);
         Object val;
         SootFieldRef ref;
         if (!instance) {
            ref = Scene.v().makeFieldRef(declClass, insn.name, type, true);
            val = Jimple.v().newStaticFieldRef(ref);
            frame.in(rvalue);
         } else {
            Operand base = this.popLocal();
            ref = Scene.v().makeFieldRef(declClass, insn.name, type, false);
            InstanceFieldRef ifr = Jimple.v().newInstanceFieldRef(base.stackOrValue(), ref);
            val = ifr;
            base.addBox(ifr.getBaseBox());
            frame.in(rvalue, base);
         }

         opr = new Operand(insn, (Value)val);
         frame.out(opr);
         AssignStmt as = Jimple.v().newAssignStmt((Value)val, rvalue.stackOrValue());
         rvalue.addBox(as.getRightOpBox());
         if (!instance) {
            frame.boxes(as.getRightOpBox());
         } else {
            frame.boxes(as.getRightOpBox(), ((InstanceFieldRef)val).getBaseBox());
         }

         this.setUnit(insn, as);
      } else {
         opr = out[0];
         type = ((FieldRef)opr.value()).getFieldRef().type();
         rvalue = this.pop(type);
         if (!instance) {
            frame.mergeIn(rvalue);
         } else {
            frame.mergeIn(rvalue, this.pop());
         }
      }

      this.assignReadOps((Local)null);
   }

   private void convertFieldInsn(FieldInsnNode insn) {
      int op = insn.getOpcode();
      if (op != 178 && op != 180) {
         this.convertPutFieldInsn(insn);
      } else {
         this.convertGetFieldInsn(insn);
      }

   }

   private void convertIincInsn(IincInsnNode insn) {
      Local local = this.getLocal(insn.var);
      this.assignReadOps(local);
      if (!this.units.containsKey(insn)) {
         AddExpr add = Jimple.v().newAddExpr(local, IntConstant.v(insn.incr));
         this.setUnit(insn, Jimple.v().newAssignStmt(local, add));
      }

   }

   private void convertConstInsn(InsnNode insn) {
      int op = insn.getOpcode();
      StackFrame frame = this.getFrame(insn);
      Operand[] out = frame.out();
      Operand opr;
      if (out == null) {
         Object v;
         if (op == 1) {
            v = NullConstant.v();
         } else if (op >= 2 && op <= 8) {
            v = IntConstant.v(op - 3);
         } else if (op != 9 && op != 10) {
            if (op >= 11 && op <= 13) {
               v = FloatConstant.v((float)(op - 11));
            } else {
               if (op != 14 && op != 15) {
                  throw new AssertionError("Unknown constant opcode: " + op);
               }

               v = DoubleConstant.v((double)(op - 14));
            }
         } else {
            v = LongConstant.v((long)(op - 9));
         }

         opr = new Operand(insn, (Value)v);
         frame.out(opr);
      } else {
         opr = out[0];
      }

      if (op != 9 && op != 10 && op != 14 && op != 15) {
         this.push(opr);
      } else {
         this.pushDual(opr);
      }

   }

   private void convertArrayLoadInsn(InsnNode insn) {
      StackFrame frame = this.getFrame(insn);
      Operand[] out = frame.out();
      Operand opr;
      if (out == null) {
         Operand indx = this.popImmediate();
         Operand base = this.popImmediate();
         ArrayRef ar = Jimple.v().newArrayRef(base.stackOrValue(), indx.stackOrValue());
         indx.addBox(ar.getIndexBox());
         base.addBox(ar.getBaseBox());
         opr = new Operand(insn, ar);
         frame.in(indx, base);
         frame.boxes(ar.getIndexBox(), ar.getBaseBox());
         frame.out(opr);
      } else {
         opr = out[0];
         frame.mergeIn(this.pop(), this.pop());
      }

      int op = insn.getOpcode();
      if (op != 49 && op != 47) {
         this.push(opr);
      } else {
         this.pushDual(opr);
      }

   }

   private void convertArrayStoreInsn(InsnNode insn) {
      int op = insn.getOpcode();
      boolean dword = op == 80 || op == 82;
      StackFrame frame = this.getFrame(insn);
      if (!this.units.containsKey(insn)) {
         Operand valu = dword ? this.popImmediateDual() : this.popImmediate();
         Operand indx = this.popImmediate();
         Operand base = this.popLocal();
         ArrayRef ar = Jimple.v().newArrayRef(base.stackOrValue(), indx.stackOrValue());
         indx.addBox(ar.getIndexBox());
         base.addBox(ar.getBaseBox());
         AssignStmt as = Jimple.v().newAssignStmt(ar, valu.stackOrValue());
         valu.addBox(as.getRightOpBox());
         frame.in(valu, indx, base);
         frame.boxes(as.getRightOpBox(), ar.getIndexBox(), ar.getBaseBox());
         this.setUnit(insn, as);
      } else {
         frame.mergeIn(dword ? this.popDual() : this.pop(), this.pop(), this.pop());
      }

   }

   private void convertDupInsn(InsnNode insn) {
      int op = insn.getOpcode();
      Operand dupd = this.popImmediate();
      Operand dupd2 = null;
      boolean dword = op == 92 || op == 93 || op == 94;
      if (dword) {
         if (this.peek() == DWORD_DUMMY) {
            this.pop();
            dupd2 = dupd;
         } else {
            dupd2 = this.popImmediate();
         }
      }

      if (op == 89) {
         this.push(dupd);
         this.push(dupd);
      } else {
         Operand o2;
         if (op == 90) {
            o2 = this.popImmediate();
            this.push(dupd);
            this.push(o2);
            this.push(dupd);
         } else {
            Operand o2h;
            if (op == 91) {
               o2 = this.popImmediate();
               o2h = this.peek() == DWORD_DUMMY ? this.pop() : this.popImmediate();
               this.push(dupd);
               this.push(o2h);
               this.push(o2);
               this.push(dupd);
            } else if (op == 92) {
               this.push(dupd2);
               this.push(dupd);
               this.push(dupd2);
               this.push(dupd);
            } else if (op == 93) {
               o2 = this.popImmediate();
               this.push(dupd2);
               this.push(dupd);
               this.push(o2);
               this.push(dupd2);
               this.push(dupd);
            } else if (op == 94) {
               o2 = this.popImmediate();
               o2h = this.peek() == DWORD_DUMMY ? this.pop() : this.popImmediate();
               this.push(dupd2);
               this.push(dupd);
               this.push(o2h);
               this.push(o2);
               this.push(dupd2);
               this.push(dupd);
            }
         }
      }

   }

   private void convertBinopInsn(InsnNode insn) {
      int op = insn.getOpcode();
      boolean dword = op == 99 || op == 97 || op == 103 || op == 101 || op == 107 || op == 105 || op == 111 || op == 109 || op == 115 || op == 113 || op == 121 || op == 123 || op == 125 || op == 127 || op == 129 || op == 131 || op == 148 || op == 151 || op == 152;
      StackFrame frame = this.getFrame(insn);
      Operand[] out = frame.out();
      Operand opr;
      if (out == null) {
         Operand op2 = dword && op != 121 && op != 123 && op != 125 ? this.popImmediateDual() : this.popImmediate();
         Operand op1 = dword ? this.popImmediateDual() : this.popImmediate();
         Value v1 = op1.stackOrValue();
         Value v2 = op2.stackOrValue();
         Object binop;
         if (op >= 96 && op <= 99) {
            binop = Jimple.v().newAddExpr(v1, v2);
         } else if (op >= 100 && op <= 103) {
            binop = Jimple.v().newSubExpr(v1, v2);
         } else if (op >= 104 && op <= 107) {
            binop = Jimple.v().newMulExpr(v1, v2);
         } else if (op >= 108 && op <= 111) {
            binop = Jimple.v().newDivExpr(v1, v2);
         } else if (op >= 112 && op <= 115) {
            binop = Jimple.v().newRemExpr(v1, v2);
         } else if (op >= 120 && op <= 121) {
            binop = Jimple.v().newShlExpr(v1, v2);
         } else if (op >= 122 && op <= 123) {
            binop = Jimple.v().newShrExpr(v1, v2);
         } else if (op >= 124 && op <= 125) {
            binop = Jimple.v().newUshrExpr(v1, v2);
         } else if (op >= 126 && op <= 127) {
            binop = Jimple.v().newAndExpr(v1, v2);
         } else if (op >= 128 && op <= 129) {
            binop = Jimple.v().newOrExpr(v1, v2);
         } else if (op >= 130 && op <= 131) {
            binop = Jimple.v().newXorExpr(v1, v2);
         } else if (op == 148) {
            binop = Jimple.v().newCmpExpr(v1, v2);
         } else if (op != 149 && op != 151) {
            if (op != 150 && op != 152) {
               throw new AssertionError("Unknown binop: " + op);
            }

            binop = Jimple.v().newCmpgExpr(v1, v2);
         } else {
            binop = Jimple.v().newCmplExpr(v1, v2);
         }

         op1.addBox(((BinopExpr)binop).getOp1Box());
         op2.addBox(((BinopExpr)binop).getOp2Box());
         opr = new Operand(insn, (Value)binop);
         frame.in(op2, op1);
         frame.boxes(((BinopExpr)binop).getOp2Box(), ((BinopExpr)binop).getOp1Box());
         frame.out(opr);
      } else {
         opr = out[0];
         if (dword) {
            if (op != 121 && op != 123 && op != 125) {
               frame.mergeIn(this.popDual(), this.popDual());
            } else {
               frame.mergeIn(this.pop(), this.popDual());
            }
         } else {
            frame.mergeIn(this.pop(), this.pop());
         }
      }

      if (!dword || op >= 148 && op <= 152) {
         this.push(opr);
      } else {
         this.pushDual(opr);
      }

   }

   private void convertUnopInsn(InsnNode insn) {
      int op = insn.getOpcode();
      boolean dword = op == 117 || op == 119;
      StackFrame frame = this.getFrame(insn);
      Operand[] out = frame.out();
      Operand opr;
      if (out == null) {
         Operand op1 = dword ? this.popImmediateDual() : this.popImmediate();
         Value v1 = op1.stackOrValue();
         Object unop;
         if (op >= 116 && op <= 119) {
            unop = Jimple.v().newNegExpr(v1);
         } else {
            if (op != 190) {
               throw new AssertionError("Unknown unop: " + op);
            }

            unop = Jimple.v().newLengthExpr(v1);
         }

         op1.addBox(((UnopExpr)unop).getOpBox());
         opr = new Operand(insn, (Value)unop);
         frame.in(op1);
         frame.boxes(((UnopExpr)unop).getOpBox());
         frame.out(opr);
      } else {
         opr = out[0];
         frame.mergeIn(dword ? this.popDual() : this.pop());
      }

      if (dword) {
         this.pushDual(opr);
      } else {
         this.push(opr);
      }

   }

   private void convertPrimCastInsn(InsnNode insn) {
      int op = insn.getOpcode();
      boolean tod = op == 133 || op == 135 || op == 140 || op == 141 || op == 143 || op == 138;
      boolean fromd = op == 143 || op == 138 || op == 142 || op == 136 || op == 144 || op == 137;
      StackFrame frame = this.getFrame(insn);
      Operand[] out = frame.out();
      Operand opr;
      if (out == null) {
         Object totype;
         if (op != 133 && op != 140 && op != 143) {
            if (op != 136 && op != 139 && op != 142) {
               if (op != 134 && op != 137 && op != 144) {
                  if (op != 135 && op != 138 && op != 141) {
                     if (op == 145) {
                        totype = ByteType.v();
                     } else if (op == 147) {
                        totype = ShortType.v();
                     } else {
                        if (op != 146) {
                           throw new AssertionError("Unknonw prim cast op: " + op);
                        }

                        totype = CharType.v();
                     }
                  } else {
                     totype = DoubleType.v();
                  }
               } else {
                  totype = FloatType.v();
               }
            } else {
               totype = IntType.v();
            }
         } else {
            totype = LongType.v();
         }

         Operand val = fromd ? this.popImmediateDual() : this.popImmediate();
         CastExpr cast = Jimple.v().newCastExpr(val.stackOrValue(), (Type)totype);
         opr = new Operand(insn, cast);
         val.addBox(cast.getOpBox());
         frame.in(val);
         frame.boxes(cast.getOpBox());
         frame.out(opr);
      } else {
         opr = out[0];
         frame.mergeIn(fromd ? this.popDual() : this.pop());
      }

      if (tod) {
         this.pushDual(opr);
      } else {
         this.push(opr);
      }

   }

   private void convertReturnInsn(InsnNode insn) {
      int op = insn.getOpcode();
      boolean dword = op == 173 || op == 175;
      StackFrame frame = this.getFrame(insn);
      if (!this.units.containsKey(insn)) {
         Operand val = dword ? this.popImmediateDual() : this.popImmediate();
         ReturnStmt ret = Jimple.v().newReturnStmt(val.stackOrValue());
         val.addBox(ret.getOpBox());
         frame.in(val);
         frame.boxes(ret.getOpBox());
         this.setUnit(insn, ret);
      } else {
         frame.mergeIn(dword ? this.popDual() : this.pop());
      }

   }

   private void convertInsn(InsnNode insn) {
      int op = insn.getOpcode();
      if (op == 0) {
         if (!this.units.containsKey(insn)) {
            this.units.put(insn, Jimple.v().newNopStmt());
         }
      } else if (op >= 1 && op <= 15) {
         this.convertConstInsn(insn);
      } else if (op >= 46 && op <= 53) {
         this.convertArrayLoadInsn(insn);
      } else if (op >= 79 && op <= 86) {
         this.convertArrayStoreInsn(insn);
      } else if (op == 87) {
         this.popImmediate();
      } else if (op == 88) {
         this.popImmediate();
         if (this.peek() == DWORD_DUMMY) {
            this.pop();
         } else {
            this.popImmediate();
         }
      } else if (op >= 89 && op <= 94) {
         this.convertDupInsn(insn);
      } else {
         Operand opr;
         if (op == 95) {
            Operand o1 = this.popImmediate();
            opr = this.popImmediate();
            this.push(o1);
            this.push(opr);
         } else if (op >= 96 && op <= 115 || op >= 120 && op <= 131 || op >= 148 && op <= 152) {
            this.convertBinopInsn(insn);
         } else if ((op < 116 || op > 119) && op != 190) {
            if (op >= 133 && op <= 147) {
               this.convertPrimCastInsn(insn);
            } else if (op >= 172 && op <= 176) {
               this.convertReturnInsn(insn);
            } else if (op == 177) {
               if (!this.units.containsKey(insn)) {
                  this.setUnit(insn, Jimple.v().newReturnVoidStmt());
               }
            } else {
               StackFrame frame;
               if (op == 191) {
                  frame = this.getFrame(insn);
                  if (!this.units.containsKey(insn)) {
                     opr = this.popImmediate();
                     ThrowStmt ts = Jimple.v().newThrowStmt(opr.stackOrValue());
                     opr.addBox(ts.getOpBox());
                     frame.in(opr);
                     frame.out(opr);
                     frame.boxes(ts.getOpBox());
                     this.setUnit(insn, ts);
                  } else {
                     opr = this.pop();
                     frame.mergeIn(opr);
                  }

                  this.push(opr);
               } else {
                  if (op != 194 && op != 195) {
                     throw new AssertionError("Unknown insn op: " + op);
                  }

                  frame = this.getFrame(insn);
                  if (!this.units.containsKey(insn)) {
                     opr = this.popStackConst();
                     MonitorStmt ts = op == 194 ? Jimple.v().newEnterMonitorStmt(opr.stackOrValue()) : Jimple.v().newExitMonitorStmt(opr.stackOrValue());
                     opr.addBox(((MonitorStmt)ts).getOpBox());
                     frame.in(opr);
                     frame.boxes(((MonitorStmt)ts).getOpBox());
                     this.setUnit(insn, (Unit)ts);
                  } else {
                     frame.mergeIn(this.pop());
                  }
               }
            }
         } else {
            this.convertUnopInsn(insn);
         }
      }

   }

   private void convertIntInsn(IntInsnNode insn) {
      int op = insn.getOpcode();
      StackFrame frame = this.getFrame(insn);
      Operand[] out = frame.out();
      Operand opr;
      if (out == null) {
         Object v;
         if (op != 16 && op != 17) {
            Object type;
            switch(insn.operand) {
            case 4:
               type = BooleanType.v();
               break;
            case 5:
               type = CharType.v();
               break;
            case 6:
               type = FloatType.v();
               break;
            case 7:
               type = DoubleType.v();
               break;
            case 8:
               type = ByteType.v();
               break;
            case 9:
               type = ShortType.v();
               break;
            case 10:
               type = IntType.v();
               break;
            case 11:
               type = LongType.v();
               break;
            default:
               throw new AssertionError("Unknown NEWARRAY type!");
            }

            Operand size = this.popImmediate();
            NewArrayExpr anew = Jimple.v().newNewArrayExpr((Type)type, size.stackOrValue());
            size.addBox(anew.getSizeBox());
            frame.in(size);
            frame.boxes(anew.getSizeBox());
            v = anew;
         } else {
            v = IntConstant.v(insn.operand);
         }

         opr = new Operand(insn, (Value)v);
         frame.out(opr);
      } else {
         opr = out[0];
         if (op == 188) {
            frame.mergeIn(this.pop());
         }
      }

      this.push(opr);
   }

   private void convertJumpInsn(JumpInsnNode insn) {
      int op = insn.getOpcode();
      if (op == 167) {
         if (!this.units.containsKey(insn)) {
            UnitBox box = Jimple.v().newStmtBox((Unit)null);
            this.labels.put(insn.label, box);
            this.setUnit(insn, Jimple.v().newGotoStmt(box));
         }

      } else {
         StackFrame frame = this.getFrame(insn);
         if (!this.units.containsKey(insn)) {
            Operand val = this.popImmediate();
            Value v = val.stackOrValue();
            Object cond;
            if (op >= 159 && op <= 166) {
               Operand val1 = this.popImmediate();
               Value v1 = val1.stackOrValue();
               if (op == 159) {
                  cond = Jimple.v().newEqExpr(v1, v);
               } else if (op == 160) {
                  cond = Jimple.v().newNeExpr(v1, v);
               } else if (op == 161) {
                  cond = Jimple.v().newLtExpr(v1, v);
               } else if (op == 162) {
                  cond = Jimple.v().newGeExpr(v1, v);
               } else if (op == 163) {
                  cond = Jimple.v().newGtExpr(v1, v);
               } else if (op == 164) {
                  cond = Jimple.v().newLeExpr(v1, v);
               } else if (op == 165) {
                  cond = Jimple.v().newEqExpr(v1, v);
               } else {
                  if (op != 166) {
                     throw new AssertionError("Unknown if op: " + op);
                  }

                  cond = Jimple.v().newNeExpr(v1, v);
               }

               val1.addBox(((ConditionExpr)cond).getOp1Box());
               val.addBox(((ConditionExpr)cond).getOp2Box());
               frame.boxes(((ConditionExpr)cond).getOp2Box(), ((ConditionExpr)cond).getOp1Box());
               frame.in(val, val1);
            } else {
               if (op == 153) {
                  cond = Jimple.v().newEqExpr(v, IntConstant.v(0));
               } else if (op == 154) {
                  cond = Jimple.v().newNeExpr(v, IntConstant.v(0));
               } else if (op == 155) {
                  cond = Jimple.v().newLtExpr(v, IntConstant.v(0));
               } else if (op == 156) {
                  cond = Jimple.v().newGeExpr(v, IntConstant.v(0));
               } else if (op == 157) {
                  cond = Jimple.v().newGtExpr(v, IntConstant.v(0));
               } else if (op == 158) {
                  cond = Jimple.v().newLeExpr(v, IntConstant.v(0));
               } else if (op == 198) {
                  cond = Jimple.v().newEqExpr(v, NullConstant.v());
               } else {
                  if (op != 199) {
                     throw new AssertionError("Unknown if op: " + op);
                  }

                  cond = Jimple.v().newNeExpr(v, NullConstant.v());
               }

               val.addBox(((ConditionExpr)cond).getOp1Box());
               frame.boxes(((ConditionExpr)cond).getOp1Box());
               frame.in(val);
            }

            UnitBox box = Jimple.v().newStmtBox((Unit)null);
            this.labels.put(insn.label, box);
            this.setUnit(insn, Jimple.v().newIfStmt((Value)cond, (UnitBox)box));
         } else if (op >= 159 && op <= 166) {
            frame.mergeIn(this.pop(), this.pop());
         } else {
            frame.mergeIn(this.pop());
         }

      }
   }

   private void convertLdcInsn(LdcInsnNode insn) {
      Object val = insn.cst;
      boolean dword = val instanceof Long || val instanceof Double;
      StackFrame frame = this.getFrame(insn);
      Operand[] out = frame.out();
      Operand opr;
      if (out == null) {
         Value v = this.toSootValue(val);
         opr = new Operand(insn, v);
         frame.out(opr);
      } else {
         opr = out[0];
      }

      if (dword) {
         this.pushDual(opr);
      } else {
         this.push(opr);
      }

   }

   private Value toSootValue(Object val) throws AssertionError {
      Object v;
      if (val instanceof Integer) {
         v = IntConstant.v((Integer)val);
      } else if (val instanceof Float) {
         v = FloatConstant.v((Float)val);
      } else if (val instanceof Long) {
         v = LongConstant.v((Long)val);
      } else if (val instanceof Double) {
         v = DoubleConstant.v((Double)val);
      } else if (val instanceof String) {
         v = StringConstant.v(val.toString());
      } else if (val instanceof org.objectweb.asm.Type) {
         v = ClassConstant.v(((org.objectweb.asm.Type)val).getDescriptor());
      } else {
         if (!(val instanceof Handle)) {
            throw new AssertionError("Unknown constant type: " + val.getClass());
         }

         v = MethodHandle.v(this.toSootMethodRef((Handle)val), ((Handle)val).getTag());
      }

      return (Value)v;
   }

   private void convertLookupSwitchInsn(LookupSwitchInsnNode insn) {
      StackFrame frame = this.getFrame(insn);
      if (this.units.containsKey(insn)) {
         frame.mergeIn(this.pop());
      } else {
         Operand key = this.popImmediate();
         UnitBox dflt = Jimple.v().newStmtBox((Unit)null);
         List<UnitBox> targets = new ArrayList(insn.labels.size());
         this.labels.put(insn.dflt, dflt);
         Iterator var6 = insn.labels.iterator();

         while(var6.hasNext()) {
            LabelNode ln = (LabelNode)var6.next();
            UnitBox box = Jimple.v().newStmtBox((Unit)null);
            targets.add(box);
            this.labels.put(ln, box);
         }

         List<IntConstant> keys = new ArrayList(insn.keys.size());
         Iterator var10 = insn.keys.iterator();

         while(var10.hasNext()) {
            Integer i = (Integer)var10.next();
            keys.add(IntConstant.v(i));
         }

         LookupSwitchStmt lss = Jimple.v().newLookupSwitchStmt(key.stackOrValue(), keys, targets, (UnitBox)dflt);
         key.addBox(lss.getKeyBox());
         frame.in(key);
         frame.boxes(lss.getKeyBox());
         this.setUnit(insn, lss);
      }
   }

   private void convertMethodInsn(MethodInsnNode insn) {
      int op = insn.getOpcode();
      boolean instance = op != 184;
      StackFrame frame = this.getFrame(insn);
      Operand[] out = frame.out();
      Operand opr;
      Type returnType;
      if (out == null) {
         String clsName = AsmUtil.toQualifiedName(insn.owner);
         if (clsName.charAt(0) == '[') {
            clsName = "java.lang.Object";
         }

         SootClass cls = Scene.v().getSootClass(clsName);
         List<Type> sigTypes = AsmUtil.toJimpleDesc(insn.desc);
         returnType = (Type)sigTypes.remove(sigTypes.size() - 1);
         SootMethodRef ref = Scene.v().makeMethodRef(cls, insn.name, sigTypes, returnType, !instance);
         int nrArgs = sigTypes.size();
         List<Value> argList = Collections.emptyList();
         Operand[] args;
         if (!instance) {
            args = nrArgs == 0 ? null : new Operand[nrArgs];
            if (args != null) {
               argList = new ArrayList(nrArgs);
            }
         } else {
            args = new Operand[nrArgs + 1];
            if (nrArgs != 0) {
               argList = new ArrayList(nrArgs);
            }
         }

         while(nrArgs-- != 0) {
            args[nrArgs] = this.popImmediate((Type)sigTypes.get(nrArgs));
            ((List)argList).add(args[nrArgs].stackOrValue());
         }

         if (((List)argList).size() > 1) {
            Collections.reverse((List)argList);
         }

         if (instance) {
            args[args.length - 1] = this.popLocal();
         }

         ValueBox[] boxes = args == null ? null : new ValueBox[args.length];
         Object invoke;
         if (!instance) {
            invoke = Jimple.v().newStaticInvokeExpr(ref, (List)argList);
         } else {
            Local base = (Local)args[args.length - 1].stackOrValue();
            Object iinvoke;
            if (op == 183) {
               iinvoke = Jimple.v().newSpecialInvokeExpr(base, ref, (List)argList);
            } else if (op == 182) {
               iinvoke = Jimple.v().newVirtualInvokeExpr(base, ref, (List)argList);
            } else {
               if (op != 185) {
                  throw new AssertionError("Unknown invoke op:" + op);
               }

               iinvoke = Jimple.v().newInterfaceInvokeExpr(base, ref, (List)argList);
            }

            boxes[boxes.length - 1] = ((InstanceInvokeExpr)iinvoke).getBaseBox();
            args[args.length - 1].addBox(boxes[boxes.length - 1]);
            invoke = iinvoke;
         }

         if (boxes != null) {
            for(int i = 0; i != sigTypes.size(); ++i) {
               boxes[i] = ((InvokeExpr)invoke).getArgBox(i);
               args[i].addBox(boxes[i]);
            }

            frame.boxes(boxes);
            frame.in(args);
         }

         opr = new Operand(insn, (Value)invoke);
         frame.out(opr);
      } else {
         opr = out[0];
         InvokeExpr expr = (InvokeExpr)opr.value;
         List<Type> types = expr.getMethodRef().parameterTypes();
         int nrArgs = types.size();
         Operand[] oprs;
         if (expr.getMethodRef().isStatic()) {
            oprs = nrArgs == 0 ? null : new Operand[nrArgs];
         } else {
            oprs = new Operand[nrArgs + 1];
         }

         if (oprs != null) {
            while(nrArgs-- != 0) {
               oprs[nrArgs] = this.pop((Type)types.get(nrArgs));
            }

            if (!expr.getMethodRef().isStatic()) {
               oprs[oprs.length - 1] = this.pop();
            }

            frame.mergeIn(oprs);
            nrArgs = types.size();
         }

         returnType = expr.getMethodRef().returnType();
      }

      if (AsmUtil.isDWord(returnType)) {
         this.pushDual(opr);
      } else if (!(returnType instanceof VoidType)) {
         this.push(opr);
      } else if (!this.units.containsKey(insn)) {
         this.setUnit(insn, Jimple.v().newInvokeStmt(opr.value));
      }

      this.assignReadOps((Local)null);
   }

   private void convertInvokeDynamicInsn(InvokeDynamicInsnNode insn) {
      StackFrame frame = this.getFrame(insn);
      Operand[] out = frame.out();
      Operand opr;
      Type returnType;
      int nrArgs;
      if (out == null) {
         SootMethodRef bsmMethodRef = this.toSootMethodRef(insn.bsm);
         List<Value> bsmMethodArgs = new ArrayList(insn.bsmArgs.length);
         Object[] var8 = insn.bsmArgs;
         nrArgs = var8.length;

         int nrArgs;
         for(nrArgs = 0; nrArgs < nrArgs; ++nrArgs) {
            Object bsmArg = var8[nrArgs];
            bsmMethodArgs.add(this.toSootValue(bsmArg));
         }

         SootClass bclass = Scene.v().getSootClass("soot.dummy.InvokeDynamic");
         Type[] types = Util.v().jimpleTypesOfFieldOrMethodDescriptor(insn.desc);
         nrArgs = types.length - 1;
         List<Type> parameterTypes = new ArrayList(nrArgs);
         List<Value> methodArgs = new ArrayList(nrArgs);
         Operand[] args = new Operand[nrArgs];
         ValueBox[] boxes = new ValueBox[nrArgs];

         while(nrArgs-- != 0) {
            parameterTypes.add(types[nrArgs]);
            args[nrArgs] = this.popImmediate(types[nrArgs]);
            methodArgs.add(args[nrArgs].stackOrValue());
         }

         if (methodArgs.size() > 1) {
            Collections.reverse(methodArgs);
            Collections.reverse(parameterTypes);
         }

         returnType = types[types.length - 1];
         SootMethodRef methodRef = Scene.v().makeMethodRef(bclass, insn.name, parameterTypes, returnType, true);
         DynamicInvokeExpr indy = Jimple.v().newDynamicInvokeExpr(bsmMethodRef, bsmMethodArgs, methodRef, insn.bsm.getTag(), methodArgs);
         if (boxes != null) {
            for(int i = 0; i < types.length - 1; ++i) {
               boxes[i] = indy.getArgBox(i);
               args[i].addBox(boxes[i]);
            }

            frame.boxes(boxes);
            frame.in(args);
         }

         opr = new Operand(insn, indy);
         frame.out(opr);
      } else {
         opr = out[0];
         InvokeExpr expr = (InvokeExpr)opr.value;
         List<Type> types = expr.getMethodRef().parameterTypes();
         nrArgs = types.size();
         Operand[] oprs;
         if (expr.getMethodRef().isStatic()) {
            oprs = nrArgs == 0 ? null : new Operand[nrArgs];
         } else {
            oprs = new Operand[nrArgs + 1];
         }

         if (oprs != null) {
            while(nrArgs-- != 0) {
               oprs[nrArgs] = this.pop((Type)types.get(nrArgs));
            }

            if (!expr.getMethodRef().isStatic()) {
               oprs[oprs.length - 1] = this.pop();
            }

            frame.mergeIn(oprs);
            nrArgs = types.size();
         }

         returnType = expr.getMethodRef().returnType();
      }

      if (AsmUtil.isDWord(returnType)) {
         this.pushDual(opr);
      } else if (!(returnType instanceof VoidType)) {
         this.push(opr);
      } else if (!this.units.containsKey(insn)) {
         this.setUnit(insn, Jimple.v().newInvokeStmt(opr.value));
      }

      this.assignReadOps((Local)null);
   }

   private SootMethodRef toSootMethodRef(Handle methodHandle) {
      String bsmClsName = AsmUtil.toQualifiedName(methodHandle.getOwner());
      SootClass bsmCls = Scene.v().getSootClass(bsmClsName);
      List<Type> bsmSigTypes = AsmUtil.toJimpleDesc(methodHandle.getDesc());
      Type returnType = (Type)bsmSigTypes.remove(bsmSigTypes.size() - 1);
      return Scene.v().makeMethodRef(bsmCls, methodHandle.getName(), bsmSigTypes, returnType, true);
   }

   private void convertMultiANewArrayInsn(MultiANewArrayInsnNode insn) {
      StackFrame frame = this.getFrame(insn);
      Operand[] out = frame.out();
      Operand opr;
      if (out == null) {
         ArrayType t = (ArrayType)AsmUtil.toJimpleType(insn.desc);
         int dims = insn.dims;
         Operand[] sizes = new Operand[dims];
         Value[] sizeVals = new Value[dims];

         ValueBox[] boxes;
         for(boxes = new ValueBox[dims]; dims-- != 0; sizeVals[dims] = sizes[dims].stackOrValue()) {
            sizes[dims] = this.popImmediate();
         }

         NewMultiArrayExpr nm = Jimple.v().newNewMultiArrayExpr(t, Arrays.asList(sizeVals));

         for(int i = 0; i != boxes.length; ++i) {
            ValueBox vb = nm.getSizeBox(i);
            sizes[i].addBox(vb);
            boxes[i] = vb;
         }

         frame.boxes(boxes);
         frame.in(sizes);
         opr = new Operand(insn, nm);
         frame.out(opr);
      } else {
         opr = out[0];
         int dims = insn.dims;

         Operand[] sizes;
         for(sizes = new Operand[dims]; dims-- != 0; sizes[dims] = this.pop()) {
         }

         frame.mergeIn(sizes);
      }

      this.push(opr);
   }

   private void convertTableSwitchInsn(TableSwitchInsnNode insn) {
      StackFrame frame = this.getFrame(insn);
      if (this.units.containsKey(insn)) {
         frame.mergeIn(this.pop());
      } else {
         Operand key = this.popImmediate();
         UnitBox dflt = Jimple.v().newStmtBox((Unit)null);
         List<UnitBox> targets = new ArrayList(insn.labels.size());
         this.labels.put(insn.dflt, dflt);
         Iterator var6 = insn.labels.iterator();

         while(var6.hasNext()) {
            LabelNode ln = (LabelNode)var6.next();
            UnitBox box = Jimple.v().newStmtBox((Unit)null);
            targets.add(box);
            this.labels.put(ln, box);
         }

         TableSwitchStmt tss = Jimple.v().newTableSwitchStmt(key.stackOrValue(), insn.min, insn.max, targets, (UnitBox)dflt);
         key.addBox(tss.getKeyBox());
         frame.in(key);
         frame.boxes(tss.getKeyBox());
         this.setUnit(insn, tss);
      }
   }

   private void convertTypeInsn(TypeInsnNode insn) {
      int op = insn.getOpcode();
      StackFrame frame = this.getFrame(insn);
      Operand[] out = frame.out();
      Operand opr;
      if (out == null) {
         Type t = AsmUtil.toJimpleRefType(insn.desc);
         Object val;
         if (op == 187) {
            val = Jimple.v().newNewExpr((RefType)t);
         } else {
            Operand op1 = this.popImmediate();
            Value v1 = op1.stackOrValue();
            ValueBox vb;
            if (op == 189) {
               NewArrayExpr expr = Jimple.v().newNewArrayExpr(t, v1);
               vb = expr.getSizeBox();
               val = expr;
            } else if (op == 192) {
               CastExpr expr = Jimple.v().newCastExpr(v1, t);
               vb = expr.getOpBox();
               val = expr;
            } else {
               if (op != 193) {
                  throw new AssertionError("Unknown type op: " + op);
               }

               InstanceOfExpr expr = Jimple.v().newInstanceOfExpr(v1, t);
               vb = expr.getOpBox();
               val = expr;
            }

            op1.addBox(vb);
            frame.in(op1);
            frame.boxes(vb);
         }

         opr = new Operand(insn, (Value)val);
         frame.out(opr);
      } else {
         opr = out[0];
         if (op != 187) {
            frame.mergeIn(this.pop());
         }
      }

      this.push(opr);
   }

   private void convertVarLoadInsn(VarInsnNode insn) {
      int op = insn.getOpcode();
      boolean dword = op == 22 || op == 24;
      StackFrame frame = this.getFrame(insn);
      Operand[] out = frame.out();
      Operand opr;
      if (out == null) {
         opr = new Operand(insn, this.getLocal(insn.var));
         frame.out(opr);
      } else {
         opr = out[0];
      }

      if (dword) {
         this.pushDual(opr);
      } else {
         this.push(opr);
      }

   }

   private void convertVarStoreInsn(VarInsnNode insn) {
      int op = insn.getOpcode();
      boolean dword = op == 55 || op == 57;
      StackFrame frame = this.getFrame(insn);
      Operand opr = dword ? this.popDual() : this.pop();
      Local local = this.getLocal(insn.var);
      if (!this.units.containsKey(insn)) {
         DefinitionStmt as = Jimple.v().newAssignStmt(local, opr.stackOrValue());
         opr.addBox(as.getRightOpBox());
         frame.boxes(as.getRightOpBox());
         frame.in(opr);
         this.setUnit(insn, as);
      } else {
         frame.mergeIn(opr);
      }

      this.assignReadOps(local);
   }

   private void convertVarInsn(VarInsnNode insn) {
      int op = insn.getOpcode();
      if (op >= 21 && op <= 25) {
         this.convertVarLoadInsn(insn);
      } else if (op >= 54 && op <= 58) {
         this.convertVarStoreInsn(insn);
      } else {
         if (op != 169) {
            throw new AssertionError("Unknown var op: " + op);
         }

         if (!this.units.containsKey(insn)) {
            this.setUnit(insn, Jimple.v().newRetStmt(this.getLocal(insn.var)));
         }
      }

   }

   private void convertLabel(LabelNode ln) {
      if (this.trapHandlers.containsKey(ln)) {
         if (this.inlineExceptionLabels.contains(ln)) {
            if (!this.units.containsKey(ln)) {
               NopStmt nop = Jimple.v().newNopStmt();
               this.setUnit(ln, nop);
            }

         } else {
            StackFrame frame = this.getFrame(ln);
            Operand[] out = frame.out();
            Operand opr;
            if (out == null) {
               CaughtExceptionRef ref = Jimple.v().newCaughtExceptionRef();
               Local stack = this.newStackLocal();
               DefinitionStmt as = Jimple.v().newIdentityStmt(stack, ref);
               opr = new Operand(ln, ref);
               opr.stack = stack;
               frame.out(opr);
               this.setUnit(ln, as);
            } else {
               opr = out[0];
            }

            this.push(opr);
         }
      }
   }

   private void convertLine(LineNumberNode ln) {
      this.lastLineNumber = ln.line;
   }

   private void addEdges(AbstractInsnNode cur, AbstractInsnNode tgt1, List<LabelNode> tgts) {
      int lastIdx = tgts == null ? -1 : tgts.size() - 1;
      Operand[] stackss = (Operand[])(new ArrayList(this.stack)).toArray(new Operand[this.stack.size()]);
      AbstractInsnNode tgt = tgt1;
      int i = 0;

      do {
         AsmMethodSource.Edge edge = (AsmMethodSource.Edge)this.edges.get(cur, tgt);
         if (edge == null) {
            edge = new AsmMethodSource.Edge(tgt);
            edge.prevStacks.add(stackss);
            this.edges.put(cur, tgt, edge);
            this.conversionWorklist.add(edge);
         } else if (edge.stack != null) {
            ArrayList<Operand> stackTemp = edge.stack;
            if (stackTemp.size() != stackss.length) {
               throw new AssertionError("Multiple un-equal stacks!");
            }

            for(int j = 0; j != stackss.length; ++j) {
               if (!((Operand)stackTemp.get(j)).equivTo(stackss[j])) {
                  throw new AssertionError("Multiple un-equal stacks!");
               }
            }
         } else {
            Iterator var9 = edge.prevStacks.iterator();

            Operand[] ps;
            do {
               if (!var9.hasNext()) {
                  edge.stack = new ArrayList(this.stack);
                  edge.prevStacks.add(stackss);
                  this.conversionWorklist.add(edge);
                  break;
               }

               ps = (Operand[])var9.next();
            } while(!Arrays.equals(ps, stackss));
         }
      } while(i <= lastIdx && (tgt = (AbstractInsnNode)tgts.get(i++)) != null);

   }

   private void convert() {
      ArrayDeque<AsmMethodSource.Edge> worklist = new ArrayDeque();
      Iterator var2 = this.trapHandlers.keySet().iterator();

      while(var2.hasNext()) {
         LabelNode ln = (LabelNode)var2.next();
         if (this.checkInlineExceptionHandler(ln)) {
            this.handleInlineExceptionHandler(ln, worklist);
         } else {
            worklist.add(new AsmMethodSource.Edge(ln, new ArrayList()));
         }
      }

      worklist.add(new AsmMethodSource.Edge(this.instructions.getFirst(), new ArrayList()));
      this.conversionWorklist = worklist;
      this.edges = HashBasedTable.create(1, 1);

      do {
         AsmMethodSource.Edge edge = (AsmMethodSource.Edge)worklist.pollLast();
         AbstractInsnNode insn = edge.insn;
         this.stack = edge.stack;
         edge.stack = null;

         do {
            int type = insn.getType();
            if (type == 4) {
               this.convertFieldInsn((FieldInsnNode)insn);
            } else if (type == 10) {
               this.convertIincInsn((IincInsnNode)insn);
            } else if (type == 0) {
               this.convertInsn((InsnNode)insn);
               int op = insn.getOpcode();
               if (op >= 172 && op <= 177 || op == 191) {
                  break;
               }
            } else if (type == 1) {
               this.convertIntInsn((IntInsnNode)insn);
            } else if (type == 9) {
               this.convertLdcInsn((LdcInsnNode)insn);
            } else {
               if (type == 7) {
                  JumpInsnNode jmp = (JumpInsnNode)insn;
                  this.convertJumpInsn(jmp);
                  int op = jmp.getOpcode();
                  if (op == 168) {
                     throw new UnsupportedOperationException("JSR!");
                  }

                  if (op != 167) {
                     AbstractInsnNode next = insn.getNext();
                     this.addEdges(insn, next, Collections.singletonList(jmp.label));
                  } else {
                     this.addEdges(insn, jmp.label, (List)null);
                  }
                  break;
               }

               LabelNode dflt;
               if (type == 12) {
                  LookupSwitchInsnNode swtch = (LookupSwitchInsnNode)insn;
                  this.convertLookupSwitchInsn(swtch);
                  dflt = swtch.dflt;
                  this.addEdges(insn, dflt, swtch.labels);
                  break;
               }

               if (type == 5) {
                  this.convertMethodInsn((MethodInsnNode)insn);
               } else if (type == 6) {
                  this.convertInvokeDynamicInsn((InvokeDynamicInsnNode)insn);
               } else if (type == 13) {
                  this.convertMultiANewArrayInsn((MultiANewArrayInsnNode)insn);
               } else if (type == 11) {
                  TableSwitchInsnNode swtch = (TableSwitchInsnNode)insn;
                  this.convertTableSwitchInsn(swtch);
                  dflt = swtch.dflt;
                  this.addEdges(insn, dflt, swtch.labels);
               } else if (type == 3) {
                  this.convertTypeInsn((TypeInsnNode)insn);
               } else if (type == 2) {
                  if (insn.getOpcode() == 169) {
                     throw new UnsupportedOperationException("RET!");
                  }

                  this.convertVarInsn((VarInsnNode)insn);
               } else if (type == 8) {
                  this.convertLabel((LabelNode)insn);
               } else if (type == 15) {
                  this.convertLine((LineNumberNode)insn);
               } else if (type != 14) {
                  throw new RuntimeException("Unknown instruction type: " + type);
               }
            }
         } while((insn = insn.getNext()) != null);
      } while(!worklist.isEmpty());

      this.conversionWorklist = null;
      this.edges = null;
   }

   private void handleInlineExceptionHandler(LabelNode ln, ArrayDeque<AsmMethodSource.Edge> worklist) {
      CaughtExceptionRef ref = Jimple.v().newCaughtExceptionRef();
      Local local = this.newStackLocal();
      DefinitionStmt as = Jimple.v().newIdentityStmt(local, ref);
      Operand opr = new Operand(ln, ref);
      opr.stack = local;
      ArrayList<Operand> stack = new ArrayList();
      stack.add(opr);
      worklist.add(new AsmMethodSource.Edge(ln, stack));
      this.inlineExceptionHandlers.put(ln, as);
   }

   private boolean checkInlineExceptionHandler(LabelNode ln) {
      ListIterator it = this.instructions.iterator();

      while(it.hasNext()) {
         AbstractInsnNode node = (AbstractInsnNode)it.next();
         if (node instanceof JumpInsnNode) {
            if (((JumpInsnNode)node).label == ln) {
               this.inlineExceptionLabels.add(ln);
               return true;
            }
         } else if (node instanceof LookupSwitchInsnNode) {
            if (((LookupSwitchInsnNode)node).labels.contains(ln)) {
               this.inlineExceptionLabels.add(ln);
               return true;
            }
         } else if (node instanceof TableSwitchInsnNode && ((TableSwitchInsnNode)node).labels.contains(ln)) {
            this.inlineExceptionLabels.add(ln);
            return true;
         }
      }

      return false;
   }

   private void emitLocals() {
      JimpleBody jb = this.body;
      SootMethod m = jb.getMethod();
      Collection<Local> jbl = jb.getLocals();
      Collection<Unit> jbu = jb.getUnits();
      int iloc = 0;
      if (!m.isStatic()) {
         Local l = this.getLocal(iloc++);
         jbu.add(Jimple.v().newIdentityStmt(l, Jimple.v().newThisRef(m.getDeclaringClass().getType())));
      }

      int nrp = 0;
      Iterator var7 = m.getParameterTypes().iterator();

      while(var7.hasNext()) {
         Object ot = var7.next();
         Type t = (Type)ot;
         Local l = this.getLocal(iloc);
         jbu.add(Jimple.v().newIdentityStmt(l, Jimple.v().newParameterRef(t, nrp++)));
         if (AsmUtil.isDWord(t)) {
            iloc += 2;
         } else {
            ++iloc;
         }
      }

      var7 = this.locals.values().iterator();

      while(var7.hasNext()) {
         Local l = (Local)var7.next();
         jbl.add(l);
      }

   }

   private void emitTraps() {
      Chain<Trap> traps = this.body.getTraps();
      SootClass throwable = Scene.v().getSootClass("java.lang.Throwable");
      Map<LabelNode, Iterator<UnitBox>> handlers = new HashMap(this.tryCatchBlocks.size());
      Iterator var4 = this.tryCatchBlocks.iterator();

      while(var4.hasNext()) {
         TryCatchBlockNode tc = (TryCatchBlockNode)var4.next();
         UnitBox start = Jimple.v().newStmtBox((Unit)null);
         UnitBox end = Jimple.v().newStmtBox((Unit)null);
         Iterator<UnitBox> hitr = (Iterator)handlers.get(tc.handler);
         if (hitr == null) {
            hitr = this.trapHandlers.get(tc.handler).iterator();
            handlers.put(tc.handler, hitr);
         }

         UnitBox handler = (UnitBox)hitr.next();
         SootClass cls = tc.type == null ? throwable : Scene.v().getSootClass(AsmUtil.toQualifiedName(tc.type));
         Trap trap = Jimple.v().newTrap(cls, start, end, handler);
         traps.add(trap);
         this.labels.put(tc.start, start);
         this.labels.put(tc.end, end);
      }

   }

   private void emitUnits(Unit u) {
      if (u instanceof UnitContainer) {
         Unit[] var2 = ((UnitContainer)u).units;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Unit uu = var2[var4];
            this.emitUnits(uu);
         }
      } else {
         this.body.getUnits().add(u);
      }

   }

   private void emitUnits() {
      AbstractInsnNode insn = this.instructions.getFirst();
      ArrayDeque labls = new ArrayDeque();

      Collection boxes;
      Iterator var6;
      UnitBox box;
      LabelNode ln;
      label104:
      while(insn != null) {
         if (insn instanceof LabelNode) {
            labls.add((LabelNode)insn);
         }

         Unit u = (Unit)this.units.get(insn);
         if (u == null) {
            insn = insn.getNext();
         } else {
            this.emitUnits(u);
            IdentityStmt caughtEx = null;
            if (u instanceof IdentityStmt) {
               caughtEx = (IdentityStmt)u;
            } else if (u instanceof UnitContainer) {
               caughtEx = this.getIdentityRefFromContrainer((UnitContainer)u);
            }

            if (insn instanceof LabelNode && caughtEx != null && caughtEx.getRightOp() instanceof CaughtExceptionRef) {
               boxes = this.trapHandlers.get((LabelNode)insn);
               var6 = boxes.iterator();

               while(var6.hasNext()) {
                  box = (UnitBox)var6.next();
                  box.setUnit(caughtEx);
               }
            }

            while(true) {
               do {
                  if (labls.isEmpty()) {
                     insn = insn.getNext();
                     continue label104;
                  }

                  ln = (LabelNode)labls.poll();
                  boxes = this.labels.get(ln);
               } while(boxes == null);

               var6 = boxes.iterator();

               while(var6.hasNext()) {
                  box = (UnitBox)var6.next();
                  box.setUnit(u instanceof UnitContainer ? ((UnitContainer)u).getFirstUnit() : u);
               }
            }
         }
      }

      Iterator var9 = this.inlineExceptionHandlers.keySet().iterator();

      while(var9.hasNext()) {
         ln = (LabelNode)var9.next();
         Unit handler = (Unit)this.inlineExceptionHandlers.get(ln);
         this.emitUnits(handler);
         Collection<UnitBox> traps = this.trapHandlers.get(ln);
         Iterator var14 = traps.iterator();

         while(var14.hasNext()) {
            UnitBox ub = (UnitBox)var14.next();
            ub.setUnit(handler);
         }

         Unit targetUnit = (Unit)this.units.get(ln);
         GotoStmt gotoImpl = Jimple.v().newGotoStmt(targetUnit);
         this.body.getUnits().add((Unit)gotoImpl);
      }

      if (!labls.isEmpty()) {
         Unit end = Jimple.v().newNopStmt();
         this.body.getUnits().add((Unit)end);

         while(true) {
            do {
               if (labls.isEmpty()) {
                  return;
               }

               ln = (LabelNode)labls.poll();
               boxes = this.labels.get(ln);
            } while(boxes == null);

            var6 = boxes.iterator();

            while(var6.hasNext()) {
               box = (UnitBox)var6.next();
               box.setUnit(end);
            }
         }
      }
   }

   private IdentityStmt getIdentityRefFromContrainer(UnitContainer u) {
      Unit[] var2 = u.units;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Unit uu = var2[var4];
         if (uu instanceof IdentityStmt) {
            return (IdentityStmt)uu;
         }

         if (uu instanceof UnitContainer) {
            return this.getIdentityRefFromContrainer((UnitContainer)uu);
         }
      }

      return null;
   }

   public Body getBody(SootMethod m, String phaseName) {
      if (!m.isConcrete()) {
         return null;
      } else {
         JimpleBody jb = Jimple.v().newBody(m);
         int nrInsn = this.instructions.size();
         this.nextLocal = this.maxLocals;
         this.locals = new HashMap(this.maxLocals + this.maxLocals / 2);
         this.labels = ArrayListMultimap.create(4, 1);
         this.units = new HashMap(nrInsn);
         this.frames = new HashMap(nrInsn);
         this.trapHandlers = ArrayListMultimap.create(this.tryCatchBlocks.size(), 1);
         this.body = jb;
         Iterator var5 = this.tryCatchBlocks.iterator();

         while(var5.hasNext()) {
            TryCatchBlockNode tc = (TryCatchBlockNode)var5.next();
            this.trapHandlers.put(tc.handler, Jimple.v().newStmtBox((Unit)null));
         }

         try {
            this.convert();
         } catch (Throwable var8) {
            throw new RuntimeException("Failed to convert " + m, var8);
         }

         this.emitLocals();
         this.emitTraps();
         this.emitUnits();
         this.locals = null;
         this.labels = null;
         this.units = null;
         this.stack = null;
         this.frames = null;
         this.body = null;
         this.castAndReturnInliner.transform(jb);

         try {
            PackManager.v().getPack("jb").apply(jb);
            return jb;
         } catch (Throwable var7) {
            throw new RuntimeException("Failed to apply jb to " + m, var7);
         }
      }
   }

   private final class Edge {
      final AbstractInsnNode insn;
      final LinkedList<Operand[]> prevStacks;
      ArrayList<Operand> stack;

      Edge(AbstractInsnNode insn, ArrayList<Operand> stack) {
         this.insn = insn;
         this.prevStacks = new LinkedList();
         this.stack = stack;
      }

      Edge(AbstractInsnNode insn) {
         this(insn, new ArrayList(AsmMethodSource.this.stack));
      }
   }
}
