package soot.toDex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.reference.FieldReference;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.ShortType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.ConcreteRef;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FloatConstant;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LongConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.MonitorStmt;
import soot.jimple.NopStmt;
import soot.jimple.ParameterRef;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.toDex.instructions.AbstractPayload;
import soot.toDex.instructions.AddressInsn;
import soot.toDex.instructions.ArrayDataPayload;
import soot.toDex.instructions.Insn;
import soot.toDex.instructions.Insn10t;
import soot.toDex.instructions.Insn10x;
import soot.toDex.instructions.Insn11x;
import soot.toDex.instructions.Insn12x;
import soot.toDex.instructions.Insn21c;
import soot.toDex.instructions.Insn22c;
import soot.toDex.instructions.Insn22x;
import soot.toDex.instructions.Insn23x;
import soot.toDex.instructions.Insn31t;
import soot.toDex.instructions.Insn32x;
import soot.toDex.instructions.InsnWithOffset;
import soot.toDex.instructions.PackedSwitchPayload;
import soot.toDex.instructions.SparseSwitchPayload;
import soot.toDex.instructions.SwitchPayload;

class StmtVisitor implements StmtSwitch {
   private final SootMethod belongingMethod;
   private final DexArrayInitDetector arrayInitDetector;
   private ConstantVisitor constantV;
   private RegisterAllocator regAlloc;
   private ExprVisitor exprV;
   private String lastReturnTypeDescriptor;
   private List<Insn> insns;
   private List<AbstractPayload> payloads;
   private Map<Insn, Stmt> insnStmtMap = new HashMap();
   private Map<Instruction, LocalRegisterAssignmentInformation> instructionRegisterMap = new IdentityHashMap();
   private Map<Instruction, Insn> instructionInsnMap = new IdentityHashMap();
   private Map<Insn, LocalRegisterAssignmentInformation> insnRegisterMap = new IdentityHashMap();
   private Map<Instruction, AbstractPayload> instructionPayloadMap = new IdentityHashMap();
   private List<LocalRegisterAssignmentInformation> parameterInstructionsList = new ArrayList();
   private Map<Constant, Register> monitorRegs = new HashMap();
   private static final Opcode[] OPCODES = Opcode.values();
   private static final int AGET_OPCODE;
   private static final int APUT_OPCODE;
   private static final int IGET_OPCODE;
   private static final int IPUT_OPCODE;
   private static final int SGET_OPCODE;
   private static final int SPUT_OPCODE;
   private static final int WIDE_OFFSET = 1;
   private static final int OBJECT_OFFSET = 2;
   private static final int BOOLEAN_OFFSET = 3;
   private static final int BYTE_OFFSET = 4;
   private static final int CHAR_OFFSET = 5;
   private static final int SHORT_OFFSET = 6;

   public StmtVisitor(SootMethod belongingMethod, DexArrayInitDetector arrayInitDetector) {
      this.belongingMethod = belongingMethod;
      this.arrayInitDetector = arrayInitDetector;
      this.constantV = new ConstantVisitor(this);
      this.regAlloc = new RegisterAllocator();
      this.exprV = new ExprVisitor(this, this.constantV, this.regAlloc);
      this.insns = new ArrayList();
      this.payloads = new ArrayList();
   }

   protected void setLastReturnTypeDescriptor(String typeDescriptor) {
      this.lastReturnTypeDescriptor = typeDescriptor;
   }

   protected SootClass getBelongingClass() {
      return this.belongingMethod.getDeclaringClass();
   }

   public Stmt getStmtForInstruction(Instruction instruction) {
      Insn insn = (Insn)this.instructionInsnMap.get(instruction);
      return insn == null ? null : (Stmt)this.insnStmtMap.get(insn);
   }

   public Insn getInsnForInstruction(Instruction instruction) {
      return (Insn)this.instructionInsnMap.get(instruction);
   }

   public Map<Instruction, LocalRegisterAssignmentInformation> getInstructionRegisterMap() {
      return this.instructionRegisterMap;
   }

   public List<LocalRegisterAssignmentInformation> getParameterInstructionsList() {
      return this.parameterInstructionsList;
   }

   public Map<Instruction, AbstractPayload> getInstructionPayloadMap() {
      return this.instructionPayloadMap;
   }

   public int getInstructionCount() {
      return this.insns.size();
   }

   protected void addInsn(Insn insn, Stmt s) {
      this.insns.add(insn);
      if (s != null && this.insnStmtMap.put(insn, s) != null) {
         throw new RuntimeException("Duplicate instruction");
      }
   }

   protected void beginNewStmt(Stmt s) {
      this.regAlloc.resetImmediateConstantsPool();
      this.addInsn(new AddressInsn(s), (Stmt)null);
   }

   public void finalizeInstructions(Set<Unit> trapReferences) {
      this.addPayloads();
      this.finishRegs();
      this.reduceInstructions(trapReferences);
   }

   private void reduceInstructions(Set<Unit> trapReferences) {
      for(int i = 0; i < this.insns.size() - 1; ++i) {
         Insn curInsn = (Insn)this.insns.get(i);
         if (!(curInsn instanceof AddressInsn) && this.isReducableMoveInstruction(curInsn.getOpcode())) {
            Insn nextInsn = null;
            int nextIndex = -1;

            for(int j = i + 1; j < this.insns.size(); ++j) {
               Insn candidate = (Insn)this.insns.get(j);
               if (!(candidate instanceof AddressInsn)) {
                  nextInsn = candidate;
                  nextIndex = j;
                  break;
               }
            }

            if (nextInsn != null && this.isReducableMoveInstruction(nextInsn.getOpcode()) && nextIndex != this.insns.size() - 1) {
               Register firstTarget = (Register)curInsn.getRegs().get(0);
               Register firstSource = (Register)curInsn.getRegs().get(1);
               Register secondTarget = (Register)nextInsn.getRegs().get(0);
               Register secondSource = (Register)nextInsn.getRegs().get(1);
               if (firstTarget.equals(secondSource) && secondTarget.equals(firstSource)) {
                  Stmt nextStmt = (Stmt)this.insnStmtMap.get(nextInsn);
                  if (nextStmt == null || !this.isJumpTarget(nextStmt) && !trapReferences.contains(nextStmt)) {
                     this.insns.remove(nextIndex);
                     if (nextStmt != null) {
                        Insn nextInst = (Insn)this.insns.get(nextIndex + 1);
                        this.insnStmtMap.remove(nextInsn);
                        this.insnStmtMap.put(nextInst, nextStmt);
                     }
                  }
               }
            }
         }
      }

   }

   private boolean isReducableMoveInstruction(Opcode opcode) {
      switch(opcode) {
      case MOVE:
      case MOVE_16:
      case MOVE_FROM16:
      case MOVE_OBJECT:
      case MOVE_OBJECT_16:
      case MOVE_OBJECT_FROM16:
      case MOVE_WIDE:
      case MOVE_WIDE_16:
      case MOVE_WIDE_FROM16:
         return true;
      default:
         return false;
      }
   }

   private boolean isJumpTarget(Stmt target) {
      Iterator var2 = this.insns.iterator();

      Insn insn;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         insn = (Insn)var2.next();
      } while(!(insn instanceof InsnWithOffset) || ((InsnWithOffset)insn).getTarget() != target);

      return true;
   }

   private void addPayloads() {
      Iterator var1 = this.payloads.iterator();

      while(var1.hasNext()) {
         AbstractPayload payload = (AbstractPayload)var1.next();
         this.addInsn(new AddressInsn(payload), (Stmt)null);
         this.addInsn(payload, (Stmt)null);
      }

   }

   public List<BuilderInstruction> getRealInsns(LabelAssigner labelAssigner) {
      List<BuilderInstruction> finalInsns = new ArrayList();
      Iterator var3 = this.insns.iterator();

      while(var3.hasNext()) {
         Insn i = (Insn)var3.next();
         if (!(i instanceof AddressInsn)) {
            BuilderInstruction realInsn = i.getRealInsn(labelAssigner);
            finalInsns.add(realInsn);
            if (this.insnStmtMap.containsKey(i)) {
               this.instructionInsnMap.put(realInsn, i);
            }

            LocalRegisterAssignmentInformation assignmentInfo = (LocalRegisterAssignmentInformation)this.insnRegisterMap.get(i);
            if (assignmentInfo != null) {
               this.instructionRegisterMap.put(realInsn, assignmentInfo);
            }

            if (i instanceof AbstractPayload) {
               this.instructionPayloadMap.put(realInsn, (AbstractPayload)i);
            }
         }
      }

      return finalInsns;
   }

   public void fakeNewInsn(Stmt s, Insn insn, Instruction instruction) {
      this.insnStmtMap.put(insn, s);
      this.instructionInsnMap.put(instruction, insn);
   }

   private void finishRegs() {
      RegisterAssigner regAssigner = new RegisterAssigner(this.regAlloc);
      this.insns = regAssigner.finishRegs(this.insns, this.insnStmtMap, this.insnRegisterMap, this.parameterInstructionsList);
   }

   protected int getRegisterCount() {
      return this.regAlloc.getRegCount();
   }

   public void defaultCase(Object o) {
      throw new Error("unknown Object (" + o.getClass() + ") as Stmt: " + o);
   }

   public void caseBreakpointStmt(BreakpointStmt stmt) {
   }

   public void caseNopStmt(NopStmt stmt) {
      this.addInsn(new Insn10x(Opcode.NOP), stmt);
   }

   public void caseRetStmt(RetStmt stmt) {
      throw new Error("ret statements are deprecated!");
   }

   public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
      this.addInsn(this.buildMonitorInsn(stmt, Opcode.MONITOR_ENTER), stmt);
   }

   public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
      this.addInsn(this.buildMonitorInsn(stmt, Opcode.MONITOR_EXIT), stmt);
   }

   private Insn buildMonitorInsn(MonitorStmt stmt, Opcode opc) {
      Value lockValue = stmt.getOp();
      this.constantV.setOrigStmt(stmt);
      Register lockReg = null;
      if (lockValue instanceof Constant && (lockReg = (Register)this.monitorRegs.get(lockValue)) != null) {
         lockReg = lockReg.clone();
      }

      if (lockReg == null) {
         lockReg = this.regAlloc.asImmediate(lockValue, this.constantV);
         this.regAlloc.lockRegister(lockReg);
         if (lockValue instanceof Constant) {
            this.monitorRegs.put((Constant)lockValue, lockReg);
            this.regAlloc.lockRegister(lockReg);
         }
      }

      return new Insn11x(opc, lockReg);
   }

   public void caseThrowStmt(ThrowStmt stmt) {
      Value exception = stmt.getOp();
      this.constantV.setOrigStmt(stmt);
      Register exceptionReg = this.regAlloc.asImmediate(exception, this.constantV);
      this.addInsn(new Insn11x(Opcode.THROW, exceptionReg), stmt);
   }

   public void caseAssignStmt(AssignStmt stmt) {
      List<Value> arrayValues = this.arrayInitDetector.getValuesForArrayInit(stmt);
      if (arrayValues != null) {
         Insn insn = this.buildArrayFillInsn((ArrayRef)stmt.getLeftOp(), arrayValues);
         if (insn != null) {
            this.addInsn(insn, stmt);
            return;
         }
      }

      if (!this.arrayInitDetector.getIgnoreUnits().contains(stmt)) {
         this.constantV.setOrigStmt(stmt);
         this.exprV.setOrigStmt(stmt);
         Value lhs = stmt.getLeftOp();
         if (lhs instanceof ConcreteRef) {
            Value source = stmt.getRightOp();
            this.addInsn(this.buildPutInsn((ConcreteRef)lhs, source), stmt);
         } else if (!(lhs instanceof Local)) {
            throw new Error("left-hand side of AssignStmt is not a Local: " + lhs.getClass());
         } else {
            Local lhsLocal = (Local)lhs;
            Register lhsReg = this.regAlloc.asLocal(lhsLocal);
            Value rhs = stmt.getRightOp();
            Insn newInsn;
            if (rhs instanceof Local) {
               Local rhsLocal = (Local)rhs;
               if (lhsLocal == rhsLocal) {
                  return;
               }

               Register sourceReg = this.regAlloc.asLocal(rhsLocal);
               newInsn = buildMoveInsn(lhsReg, sourceReg);
               this.addInsn(newInsn, stmt);
            } else if (rhs instanceof Constant) {
               this.constantV.setDestination(lhsReg);
               rhs.apply(this.constantV);
               newInsn = (Insn)this.insns.get(this.insns.size() - 1);
            } else if (rhs instanceof ConcreteRef) {
               newInsn = this.buildGetInsn((ConcreteRef)rhs, lhsReg);
               this.addInsn(newInsn, stmt);
            } else {
               this.exprV.setDestinationReg(lhsReg);
               rhs.apply(this.exprV);
               if (rhs instanceof InvokeExpr) {
                  Insn moveResultInsn = this.buildMoveResultInsn(lhsReg);
                  int invokeInsnIndex = this.exprV.getLastInvokeInstructionPosition();
                  this.insns.add(invokeInsnIndex + 1, moveResultInsn);
               }

               newInsn = (Insn)this.insns.get(this.insns.size() - 1);
            }

            this.insnRegisterMap.put(newInsn, LocalRegisterAssignmentInformation.v(lhsReg, lhsLocal));
         }
      }
   }

   private Insn buildGetInsn(ConcreteRef sourceRef, Register destinationReg) {
      if (sourceRef instanceof StaticFieldRef) {
         return this.buildStaticFieldGetInsn(destinationReg, (StaticFieldRef)sourceRef);
      } else if (sourceRef instanceof InstanceFieldRef) {
         return this.buildInstanceFieldGetInsn(destinationReg, (InstanceFieldRef)sourceRef);
      } else if (sourceRef instanceof ArrayRef) {
         return this.buildArrayGetInsn(destinationReg, (ArrayRef)sourceRef);
      } else {
         throw new RuntimeException("unsupported type of ConcreteRef: " + sourceRef.getClass());
      }
   }

   private Insn buildPutInsn(ConcreteRef destRef, Value source) {
      if (destRef instanceof StaticFieldRef) {
         return this.buildStaticFieldPutInsn((StaticFieldRef)destRef, source);
      } else if (destRef instanceof InstanceFieldRef) {
         return this.buildInstanceFieldPutInsn((InstanceFieldRef)destRef, source);
      } else if (destRef instanceof ArrayRef) {
         return this.buildArrayPutInsn((ArrayRef)destRef, source);
      } else {
         throw new RuntimeException("unsupported type of ConcreteRef: " + destRef.getClass());
      }
   }

   protected static Insn buildMoveInsn(Register destinationReg, Register sourceReg) {
      Opcode opc;
      if (!destinationReg.fitsShort()) {
         if (sourceReg.isObject()) {
            opc = Opcode.MOVE_OBJECT_16;
         } else if (sourceReg.isWide()) {
            opc = Opcode.MOVE_WIDE_16;
         } else {
            opc = Opcode.MOVE_16;
         }

         return new Insn32x(opc, destinationReg, sourceReg);
      } else if (destinationReg.fitsByte() && sourceReg.fitsByte()) {
         if (sourceReg.isObject()) {
            opc = Opcode.MOVE_OBJECT;
         } else if (sourceReg.isWide()) {
            opc = Opcode.MOVE_WIDE;
         } else {
            opc = Opcode.MOVE;
         }

         return new Insn12x(opc, destinationReg, sourceReg);
      } else {
         if (sourceReg.isObject()) {
            opc = Opcode.MOVE_OBJECT_FROM16;
         } else if (sourceReg.isWide()) {
            opc = Opcode.MOVE_WIDE_FROM16;
         } else {
            opc = Opcode.MOVE_FROM16;
         }

         return new Insn22x(opc, destinationReg, sourceReg);
      }
   }

   private Insn buildStaticFieldPutInsn(StaticFieldRef destRef, Value source) {
      Register sourceReg = this.regAlloc.asImmediate(source, this.constantV);
      FieldReference destField = DexPrinter.toFieldReference(destRef.getFieldRef());
      Opcode opc = this.getPutGetOpcodeWithTypeSuffix(SPUT_OPCODE, destField.getType());
      return new Insn21c(opc, sourceReg, destField);
   }

   private Insn buildInstanceFieldPutInsn(InstanceFieldRef destRef, Value source) {
      FieldReference destField = DexPrinter.toFieldReference(destRef.getFieldRef());
      Local instance = (Local)destRef.getBase();
      Register instanceReg = this.regAlloc.asLocal(instance);
      Register sourceReg = this.regAlloc.asImmediate(source, this.constantV);
      Opcode opc = this.getPutGetOpcodeWithTypeSuffix(IPUT_OPCODE, destField.getType());
      return new Insn22c(opc, sourceReg, instanceReg, destField);
   }

   private Insn buildArrayPutInsn(ArrayRef destRef, Value source) {
      Local array = (Local)destRef.getBase();
      Register arrayReg = this.regAlloc.asLocal(array);
      Value index = destRef.getIndex();
      Register indexReg = this.regAlloc.asImmediate(index, this.constantV);
      Register sourceReg = this.regAlloc.asImmediate(source, this.constantV);
      String arrayTypeDescriptor = SootToDexUtils.getArrayTypeDescriptor((ArrayType)array.getType());
      Opcode opc = this.getPutGetOpcodeWithTypeSuffix(APUT_OPCODE, arrayTypeDescriptor);
      return new Insn23x(opc, sourceReg, arrayReg, indexReg);
   }

   private Insn buildArrayFillInsn(ArrayRef destRef, List<Value> values) {
      Local array = (Local)destRef.getBase();
      Register arrayReg = this.regAlloc.asLocal(array);
      int elementSize = 0;
      List<Number> numbers = new ArrayList(values.size());
      Iterator var7 = values.iterator();

      while(var7.hasNext()) {
         Value val = (Value)var7.next();
         if (val instanceof IntConstant) {
            elementSize = Math.max(elementSize, 4);
            numbers.add(((IntConstant)val).value);
         } else if (val instanceof LongConstant) {
            elementSize = Math.max(elementSize, 8);
            numbers.add(((LongConstant)val).value);
         } else if (val instanceof FloatConstant) {
            elementSize = Math.max(elementSize, 4);
            numbers.add(((FloatConstant)val).value);
         } else {
            if (!(val instanceof DoubleConstant)) {
               return null;
            }

            elementSize = Math.max(elementSize, 8);
            numbers.add(((DoubleConstant)val).value);
         }
      }

      if (destRef.getType() instanceof BooleanType) {
         elementSize = 1;
      } else if (destRef.getType() instanceof ByteType) {
         elementSize = 1;
      } else if (destRef.getType() instanceof CharType) {
         elementSize = 2;
      } else if (destRef.getType() instanceof ShortType) {
         elementSize = 2;
      } else if (destRef.getType() instanceof IntType) {
         elementSize = 4;
      } else if (destRef.getType() instanceof FloatType) {
         elementSize = 4;
      } else if (destRef.getType() instanceof LongType) {
         elementSize = 8;
      } else if (destRef.getType() instanceof DoubleType) {
         elementSize = 8;
      }

      ArrayDataPayload payload = new ArrayDataPayload(elementSize, numbers);
      this.payloads.add(payload);
      Insn31t insn = new Insn31t(Opcode.FILL_ARRAY_DATA, arrayReg);
      insn.setPayload(payload);
      return insn;
   }

   private Insn buildStaticFieldGetInsn(Register destinationReg, StaticFieldRef sourceRef) {
      FieldReference sourceField = DexPrinter.toFieldReference(sourceRef.getFieldRef());
      Opcode opc = this.getPutGetOpcodeWithTypeSuffix(SGET_OPCODE, sourceField.getType());
      return new Insn21c(opc, destinationReg, sourceField);
   }

   private Insn buildInstanceFieldGetInsn(Register destinationReg, InstanceFieldRef sourceRef) {
      Local instance = (Local)sourceRef.getBase();
      Register instanceReg = this.regAlloc.asLocal(instance);
      FieldReference sourceField = DexPrinter.toFieldReference(sourceRef.getFieldRef());
      Opcode opc = this.getPutGetOpcodeWithTypeSuffix(IGET_OPCODE, sourceField.getType());
      return new Insn22c(opc, destinationReg, instanceReg, sourceField);
   }

   private Insn buildArrayGetInsn(Register destinationReg, ArrayRef sourceRef) {
      Value index = sourceRef.getIndex();
      Register indexReg = this.regAlloc.asImmediate(index, this.constantV);
      Local array = (Local)sourceRef.getBase();
      Register arrayReg = this.regAlloc.asLocal(array);
      String arrayTypeDescriptor = SootToDexUtils.getArrayTypeDescriptor((ArrayType)array.getType());
      Opcode opc = this.getPutGetOpcodeWithTypeSuffix(AGET_OPCODE, arrayTypeDescriptor);
      return new Insn23x(opc, destinationReg, arrayReg, indexReg);
   }

   private Opcode getPutGetOpcodeWithTypeSuffix(int opcodeBase, String fieldType) {
      if (fieldType.equals("Z")) {
         return OPCODES[opcodeBase + 3];
      } else if (!fieldType.equals("I") && !fieldType.equals("F")) {
         if (fieldType.equals("B")) {
            return OPCODES[opcodeBase + 4];
         } else if (fieldType.equals("C")) {
            return OPCODES[opcodeBase + 5];
         } else if (fieldType.equals("S")) {
            return OPCODES[opcodeBase + 6];
         } else if (SootToDexUtils.isWide(fieldType)) {
            return OPCODES[opcodeBase + 1];
         } else if (SootToDexUtils.isObject(fieldType)) {
            return OPCODES[opcodeBase + 2];
         } else {
            throw new RuntimeException("unsupported field type for *put*/*get* opcode: " + fieldType);
         }
      } else {
         return OPCODES[opcodeBase];
      }
   }

   private Insn buildMoveResultInsn(Register destinationReg) {
      Opcode opc;
      if (SootToDexUtils.isObject(this.lastReturnTypeDescriptor)) {
         opc = Opcode.MOVE_RESULT_OBJECT;
      } else if (SootToDexUtils.isWide(this.lastReturnTypeDescriptor)) {
         opc = Opcode.MOVE_RESULT_WIDE;
      } else {
         opc = Opcode.MOVE_RESULT;
      }

      return new Insn11x(opc, destinationReg);
   }

   public void caseInvokeStmt(InvokeStmt stmt) {
      this.exprV.setOrigStmt(stmt);
      stmt.getInvokeExpr().apply(this.exprV);
   }

   public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
      this.addInsn(new Insn10x(Opcode.RETURN_VOID), stmt);
   }

   public void caseReturnStmt(ReturnStmt stmt) {
      Value returnValue = stmt.getOp();
      this.constantV.setOrigStmt(stmt);
      Register returnReg = this.regAlloc.asImmediate(returnValue, this.constantV);
      Type retType = returnValue.getType();
      Opcode opc;
      if (SootToDexUtils.isObject(retType)) {
         opc = Opcode.RETURN_OBJECT;
      } else if (SootToDexUtils.isWide(retType)) {
         opc = Opcode.RETURN_WIDE;
      } else {
         opc = Opcode.RETURN;
      }

      this.addInsn(new Insn11x(opc, returnReg), stmt);
   }

   public void caseIdentityStmt(IdentityStmt stmt) {
      Local lhs = (Local)stmt.getLeftOp();
      Value rhs = stmt.getRightOp();
      if (rhs instanceof CaughtExceptionRef) {
         Register localReg = this.regAlloc.asLocal(lhs);
         this.addInsn(new Insn11x(Opcode.MOVE_EXCEPTION, localReg), stmt);
         this.insnRegisterMap.put(this.insns.get(this.insns.size() - 1), LocalRegisterAssignmentInformation.v(localReg, lhs));
      } else {
         if (!(rhs instanceof ThisRef) && !(rhs instanceof ParameterRef)) {
            throw new Error("unknown Value as right-hand side of IdentityStmt: " + rhs);
         }

         this.regAlloc.asParameter(this.belongingMethod, lhs);
         this.parameterInstructionsList.add(LocalRegisterAssignmentInformation.v(this.regAlloc.asLocal(lhs).clone(), lhs));
      }

   }

   public void caseGotoStmt(GotoStmt stmt) {
      Stmt target = (Stmt)stmt.getTarget();
      this.addInsn(this.buildGotoInsn(target), stmt);
   }

   private Insn buildGotoInsn(Stmt target) {
      if (target == null) {
         throw new RuntimeException("Cannot jump to a NULL target");
      } else {
         Insn10t insn = new Insn10t(Opcode.GOTO);
         insn.setTarget(target);
         return insn;
      }
   }

   public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
      this.exprV.setOrigStmt(stmt);
      this.constantV.setOrigStmt(stmt);
      List<IntConstant> keyValues = stmt.getLookupValues();
      int[] keys = new int[keyValues.size()];

      for(int i = 0; i < keys.length; ++i) {
         keys[i] = ((IntConstant)keyValues.get(i)).value;
      }

      List<Unit> targets = stmt.getTargets();
      SparseSwitchPayload payload = new SparseSwitchPayload(keys, targets);
      this.payloads.add(payload);
      Value key = stmt.getKey();
      Stmt defaultTarget = (Stmt)stmt.getDefaultTarget();
      if (defaultTarget == stmt) {
         throw new RuntimeException("Looping switch block detected");
      } else {
         this.addInsn(this.buildSwitchInsn(Opcode.SPARSE_SWITCH, key, defaultTarget, payload, stmt), stmt);
      }
   }

   public void caseTableSwitchStmt(TableSwitchStmt stmt) {
      this.exprV.setOrigStmt(stmt);
      this.constantV.setOrigStmt(stmt);
      int firstKey = stmt.getLowIndex();
      List<Unit> targets = stmt.getTargets();
      PackedSwitchPayload payload = new PackedSwitchPayload(firstKey, targets);
      this.payloads.add(payload);
      Value key = stmt.getKey();
      Stmt defaultTarget = (Stmt)stmt.getDefaultTarget();
      this.addInsn(this.buildSwitchInsn(Opcode.PACKED_SWITCH, key, defaultTarget, payload, stmt), stmt);
   }

   private Insn buildSwitchInsn(Opcode opc, Value key, Stmt defaultTarget, SwitchPayload payload, Stmt stmt) {
      Register keyReg = this.regAlloc.asImmediate(key, this.constantV);
      Insn31t switchInsn = new Insn31t(opc, keyReg);
      switchInsn.setPayload(payload);
      payload.setSwitchInsn(switchInsn);
      this.addInsn(switchInsn, stmt);
      return this.buildGotoInsn(defaultTarget);
   }

   public void caseIfStmt(IfStmt stmt) {
      Stmt target = stmt.getTarget();
      this.exprV.setOrigStmt(stmt);
      this.exprV.setTargetForOffset(target);
      stmt.getCondition().apply(this.exprV);
   }

   public void preAllocateMonitorConsts(Set<ClassConstant> monitorConsts) {
      Iterator var2 = monitorConsts.iterator();

      while(var2.hasNext()) {
         ClassConstant c = (ClassConstant)var2.next();
         Register lhsReg = this.regAlloc.asImmediate(c, this.constantV);
         this.regAlloc.lockRegister(lhsReg);
         this.monitorRegs.put(c, lhsReg);
      }

   }

   static {
      AGET_OPCODE = Opcode.AGET.ordinal();
      APUT_OPCODE = Opcode.APUT.ordinal();
      IGET_OPCODE = Opcode.IGET.ordinal();
      IPUT_OPCODE = Opcode.IPUT.ordinal();
      SGET_OPCODE = Opcode.SGET.ordinal();
      SPUT_OPCODE = Opcode.SPUT.ordinal();
   }
}
