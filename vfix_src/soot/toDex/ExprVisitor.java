package soot.toDex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import soot.ArrayType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.IntegerType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.PrimType;
import soot.RefType;
import soot.SootClass;
import soot.Type;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.CastExpr;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.DivExpr;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EqExpr;
import soot.jimple.Expr;
import soot.jimple.ExprSwitch;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import soot.toDex.instructions.Insn;
import soot.toDex.instructions.Insn10x;
import soot.toDex.instructions.Insn11x;
import soot.toDex.instructions.Insn12x;
import soot.toDex.instructions.Insn21c;
import soot.toDex.instructions.Insn21t;
import soot.toDex.instructions.Insn22b;
import soot.toDex.instructions.Insn22c;
import soot.toDex.instructions.Insn22s;
import soot.toDex.instructions.Insn22t;
import soot.toDex.instructions.Insn23x;
import soot.toDex.instructions.Insn35c;
import soot.toDex.instructions.Insn3rc;
import soot.toDex.instructions.InsnWithOffset;

class ExprVisitor implements ExprSwitch {
   private StmtVisitor stmtV;
   private ConstantVisitor constantV;
   private RegisterAllocator regAlloc;
   private Register destinationReg;
   private Stmt targetForOffset;
   private Stmt origStmt;
   private int lastInvokeInstructionPosition;

   public ExprVisitor(StmtVisitor stmtV, ConstantVisitor constantV, RegisterAllocator regAlloc) {
      this.stmtV = stmtV;
      this.constantV = constantV;
      this.regAlloc = regAlloc;
   }

   public void setDestinationReg(Register destinationReg) {
      this.destinationReg = destinationReg;
   }

   public void setOrigStmt(Stmt stmt) {
      this.origStmt = stmt;
   }

   public void setTargetForOffset(Stmt targetForOffset) {
      this.targetForOffset = targetForOffset;
   }

   public void defaultCase(Object o) {
      throw new Error("unknown Object (" + o.getClass() + ") as Expression: " + o);
   }

   public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
      throw new Error("DynamicInvokeExpr not supported: " + v);
   }

   public void caseSpecialInvokeExpr(SpecialInvokeExpr sie) {
      MethodReference method = DexPrinter.toMethodReference(sie.getMethodRef());
      List<Register> arguments = this.getInstanceInvokeArgumentRegs(sie);
      this.lastInvokeInstructionPosition = this.stmtV.getInstructionCount();
      if (!this.isCallToConstructor(sie) && !this.isCallToPrivate(sie)) {
         if (this.isCallToSuper(sie)) {
            this.stmtV.addInsn(this.buildInvokeInsn("INVOKE_SUPER", method, arguments), this.origStmt);
         } else {
            this.stmtV.addInsn(this.buildInvokeInsn("INVOKE_VIRTUAL", method, arguments), this.origStmt);
         }
      } else {
         this.stmtV.addInsn(this.buildInvokeInsn("INVOKE_DIRECT", method, arguments), this.origStmt);
      }

   }

   private Insn buildInvokeInsn(String invokeOpcode, MethodReference method, List<Register> argumentRegs) {
      int regCountForArguments = SootToDexUtils.getRealRegCount(argumentRegs);
      Object invokeInsn;
      if (regCountForArguments <= 5) {
         Register[] paddedArray = this.pad35cRegs(argumentRegs);
         Opcode opc = Opcode.valueOf(invokeOpcode);
         invokeInsn = new Insn35c(opc, regCountForArguments, paddedArray[0], paddedArray[1], paddedArray[2], paddedArray[3], paddedArray[4], method);
      } else {
         if (regCountForArguments > 255) {
            throw new Error("too many parameter registers for invoke-* (> 255): " + regCountForArguments + " or registers too big (> 4 bits)");
         }

         Opcode opc = Opcode.valueOf(invokeOpcode + "_RANGE");
         invokeInsn = new Insn3rc(opc, argumentRegs, (short)regCountForArguments, method);
      }

      this.stmtV.setLastReturnTypeDescriptor(method.getReturnType());
      return (Insn)invokeInsn;
   }

   private boolean isCallToPrivate(SpecialInvokeExpr sie) {
      return sie.getMethod().isPrivate();
   }

   private boolean isCallToConstructor(SpecialInvokeExpr sie) {
      return sie.getMethodRef().name().equals("<init>");
   }

   private boolean isCallToSuper(SpecialInvokeExpr sie) {
      SootClass classWithInvokation = sie.getMethod().getDeclaringClass();
      SootClass currentClass = this.stmtV.getBelongingClass();

      while(currentClass != null) {
         currentClass = currentClass.getSuperclassUnsafe();
         if (currentClass != null) {
            if (currentClass == classWithInvokation) {
               return true;
            }

            if (currentClass.isPhantom() && !currentClass.getName().equals("java.lang.Object")) {
               return true;
            }
         }
      }

      return false;
   }

   public void caseVirtualInvokeExpr(VirtualInvokeExpr vie) {
      MethodReference method = DexPrinter.toMethodReference(vie.getMethodRef());
      List<Register> argumentRegs = this.getInstanceInvokeArgumentRegs(vie);
      this.lastInvokeInstructionPosition = this.stmtV.getInstructionCount();
      this.stmtV.addInsn(this.buildInvokeInsn("INVOKE_VIRTUAL", method, argumentRegs), this.origStmt);
   }

   private List<Register> getInvokeArgumentRegs(InvokeExpr ie) {
      this.constantV.setOrigStmt(this.origStmt);
      List<Register> argumentRegs = new ArrayList();
      Iterator var3 = ie.getArgs().iterator();

      while(var3.hasNext()) {
         Value arg = (Value)var3.next();
         Register currentReg = this.regAlloc.asImmediate(arg, this.constantV);
         argumentRegs.add(currentReg);
      }

      return argumentRegs;
   }

   private List<Register> getInstanceInvokeArgumentRegs(InstanceInvokeExpr iie) {
      this.constantV.setOrigStmt(this.origStmt);
      List<Register> argumentRegs = this.getInvokeArgumentRegs(iie);
      Local callee = (Local)iie.getBase();
      Register calleeRegister = this.regAlloc.asLocal(callee);
      argumentRegs.add(0, calleeRegister);
      return argumentRegs;
   }

   private Register[] pad35cRegs(List<Register> realRegs) {
      Register[] paddedArray = new Register[5];
      int nextReg = 0;

      for(Iterator var4 = realRegs.iterator(); var4.hasNext(); ++nextReg) {
         Register realReg = (Register)var4.next();
         paddedArray[nextReg] = realReg;
         if (realReg.isWide()) {
            ++nextReg;
            paddedArray[nextReg] = Register.EMPTY_REGISTER;
         }
      }

      while(nextReg < 5) {
         paddedArray[nextReg] = Register.EMPTY_REGISTER;
         ++nextReg;
      }

      return paddedArray;
   }

   public void caseInterfaceInvokeExpr(InterfaceInvokeExpr iie) {
      MethodReference method = DexPrinter.toMethodReference(iie.getMethodRef());
      List<Register> arguments = this.getInstanceInvokeArgumentRegs(iie);
      this.lastInvokeInstructionPosition = this.stmtV.getInstructionCount();
      this.stmtV.addInsn(this.buildInvokeInsn("INVOKE_INTERFACE", method, arguments), this.origStmt);
   }

   public void caseStaticInvokeExpr(StaticInvokeExpr sie) {
      MethodReference method = DexPrinter.toMethodReference(sie.getMethodRef());
      List<Register> arguments = this.getInvokeArgumentRegs(sie);
      this.lastInvokeInstructionPosition = this.stmtV.getInstructionCount();
      this.stmtV.addInsn(this.buildInvokeInsn("INVOKE_STATIC", method, arguments), this.origStmt);
   }

   private void buildCalculatingBinaryInsn(String binaryOperation, Value firstOperand, Value secondOperand, Expr originalExpr) {
      this.constantV.setOrigStmt(this.origStmt);
      Register firstOpReg = this.regAlloc.asImmediate(firstOperand, this.constantV);
      if (this.destinationReg.getType() instanceof IntType && secondOperand instanceof IntConstant && !binaryOperation.equals("SUB")) {
         int secondOpConstant = ((IntConstant)secondOperand).value;
         if (SootToDexUtils.fitsSigned8((long)secondOpConstant)) {
            this.stmtV.addInsn(this.buildLit8BinaryInsn(binaryOperation, firstOpReg, (byte)secondOpConstant), this.origStmt);
            return;
         }

         if (SootToDexUtils.fitsSigned16((long)secondOpConstant) && !binaryOperation.equals("SHL") && !binaryOperation.equals("SHR") && !binaryOperation.equals("USHR")) {
            this.stmtV.addInsn(this.buildLit16BinaryInsn(binaryOperation, firstOpReg, (short)secondOpConstant), this.origStmt);
            return;
         }
      }

      if (!(secondOperand.getType() instanceof PrimType)) {
         throw new RuntimeException("Invalid value type for primitibe operation");
      } else {
         PrimitiveType destRegType = PrimitiveType.getByName(this.destinationReg.getType().toString());
         Register secondOpReg = this.regAlloc.asImmediate(secondOperand, this.constantV);
         Register orgDestReg = this.destinationReg;
         if (this.isSmallerThan(destRegType, PrimitiveType.INT)) {
            this.destinationReg = this.regAlloc.asTmpReg(IntType.v());
         } else if (this.isBiggerThan(PrimitiveType.getByName(secondOpReg.getType().toString()), destRegType)) {
            this.destinationReg = this.regAlloc.asTmpReg(secondOpReg.getType());
         } else if (this.isBiggerThan(PrimitiveType.getByName(firstOpReg.getType().toString()), destRegType)) {
            this.destinationReg = this.regAlloc.asTmpReg(firstOpReg.getType());
         }

         if (this.destinationReg.getNumber() == firstOpReg.getNumber()) {
            this.stmtV.addInsn(this.build2AddrBinaryInsn(binaryOperation, secondOpReg), this.origStmt);
         } else {
            this.stmtV.addInsn(this.buildNormalBinaryInsn(binaryOperation, firstOpReg, secondOpReg), this.origStmt);
         }

         if (orgDestReg != this.destinationReg) {
            Register tempReg = this.destinationReg.clone();
            this.destinationReg = orgDestReg.clone();
            this.castPrimitive(tempReg, originalExpr, this.destinationReg.getType());
         }

      }
   }

   private String fixIntTypeString(String typeString) {
      return !typeString.equals("boolean") && !typeString.equals("byte") && !typeString.equals("char") && !typeString.equals("short") ? typeString : "int";
   }

   private Insn build2AddrBinaryInsn(String binaryOperation, Register secondOpReg) {
      String localTypeString = this.destinationReg.getTypeString();
      localTypeString = this.fixIntTypeString(localTypeString);
      Opcode opc = Opcode.valueOf(binaryOperation + "_" + localTypeString.toUpperCase() + "_2ADDR");
      return new Insn12x(opc, this.destinationReg, secondOpReg);
   }

   private Insn buildNormalBinaryInsn(String binaryOperation, Register firstOpReg, Register secondOpReg) {
      String localTypeString = firstOpReg.getTypeString();
      localTypeString = this.fixIntTypeString(localTypeString);
      Opcode opc = Opcode.valueOf(binaryOperation + "_" + localTypeString.toUpperCase());
      return new Insn23x(opc, this.destinationReg, firstOpReg, secondOpReg);
   }

   private Insn buildLit16BinaryInsn(String binaryOperation, Register firstOpReg, short secondOpLieteral) {
      Opcode opc = Opcode.valueOf(binaryOperation + "_INT_LIT16");
      return new Insn22s(opc, this.destinationReg, firstOpReg, secondOpLieteral);
   }

   private Insn buildLit8BinaryInsn(String binaryOperation, Register firstOpReg, byte secondOpLiteral) {
      Opcode opc = Opcode.valueOf(binaryOperation + "_INT_LIT8");
      return new Insn22b(opc, this.destinationReg, firstOpReg, secondOpLiteral);
   }

   public void caseAddExpr(AddExpr ae) {
      this.buildCalculatingBinaryInsn("ADD", ae.getOp1(), ae.getOp2(), ae);
   }

   public void caseSubExpr(SubExpr se) {
      this.buildCalculatingBinaryInsn("SUB", se.getOp1(), se.getOp2(), se);
   }

   public void caseMulExpr(MulExpr me) {
      this.buildCalculatingBinaryInsn("MUL", me.getOp1(), me.getOp2(), me);
   }

   public void caseDivExpr(DivExpr de) {
      this.buildCalculatingBinaryInsn("DIV", de.getOp1(), de.getOp2(), de);
   }

   public void caseRemExpr(RemExpr re) {
      this.buildCalculatingBinaryInsn("REM", re.getOp1(), re.getOp2(), re);
   }

   public void caseAndExpr(AndExpr ae) {
      this.buildCalculatingBinaryInsn("AND", ae.getOp1(), ae.getOp2(), ae);
   }

   public void caseOrExpr(OrExpr oe) {
      this.buildCalculatingBinaryInsn("OR", oe.getOp1(), oe.getOp2(), oe);
   }

   public void caseXorExpr(XorExpr xe) {
      Value firstOperand = xe.getOp1();
      Value secondOperand = xe.getOp2();
      this.constantV.setOrigStmt(this.origStmt);
      if (!secondOperand.equals(IntConstant.v(-1)) && !secondOperand.equals(LongConstant.v(-1L))) {
         this.buildCalculatingBinaryInsn("XOR", firstOperand, secondOperand, xe);
      } else {
         PrimitiveType destRegType = PrimitiveType.getByName(this.destinationReg.getType().toString());
         Register orgDestReg = this.destinationReg;
         if (this.isBiggerThan(PrimitiveType.getByName(secondOperand.getType().toString()), destRegType)) {
            this.destinationReg = this.regAlloc.asTmpReg(IntType.v());
         }

         Register tempReg;
         if (secondOperand.equals(IntConstant.v(-1))) {
            tempReg = this.regAlloc.asImmediate(firstOperand, this.constantV);
            this.stmtV.addInsn(new Insn12x(Opcode.NOT_INT, this.destinationReg, tempReg), this.origStmt);
         } else if (secondOperand.equals(LongConstant.v(-1L))) {
            tempReg = this.regAlloc.asImmediate(firstOperand, this.constantV);
            this.stmtV.addInsn(new Insn12x(Opcode.NOT_LONG, this.destinationReg, tempReg), this.origStmt);
         }

         if (orgDestReg != this.destinationReg) {
            tempReg = this.destinationReg.clone();
            this.destinationReg = orgDestReg.clone();
            this.castPrimitive(tempReg, secondOperand, this.destinationReg.getType());
         }
      }

   }

   public void caseShlExpr(ShlExpr sle) {
      this.buildCalculatingBinaryInsn("SHL", sle.getOp1(), sle.getOp2(), sle);
   }

   public void caseShrExpr(ShrExpr sre) {
      this.buildCalculatingBinaryInsn("SHR", sre.getOp1(), sre.getOp2(), sre);
   }

   public void caseUshrExpr(UshrExpr usre) {
      this.buildCalculatingBinaryInsn("USHR", usre.getOp1(), usre.getOp2(), usre);
   }

   private Insn buildComparingBinaryInsn(String binaryOperation, Value firstOperand, Value secondOperand) {
      this.constantV.setOrigStmt(this.origStmt);
      Value realFirstOperand = this.fixNullConstant(firstOperand);
      Value realSecondOperand = this.fixNullConstant(secondOperand);
      Register firstOpReg = this.regAlloc.asImmediate(realFirstOperand, this.constantV);
      String opcodeName = "IF_" + binaryOperation;
      boolean secondOpIsInt = realSecondOperand instanceof IntConstant;
      boolean secondOpIsZero = secondOpIsInt && ((IntConstant)realSecondOperand).value == 0;
      Object comparingBinaryInsn;
      Opcode opc;
      if (secondOpIsZero) {
         opc = Opcode.valueOf(opcodeName.concat("Z"));
         comparingBinaryInsn = new Insn21t(opc, firstOpReg);
         ((InsnWithOffset)comparingBinaryInsn).setTarget(this.targetForOffset);
      } else {
         opc = Opcode.valueOf(opcodeName);
         Register secondOpReg = this.regAlloc.asImmediate(realSecondOperand, this.constantV);
         comparingBinaryInsn = new Insn22t(opc, firstOpReg, secondOpReg);
         ((InsnWithOffset)comparingBinaryInsn).setTarget(this.targetForOffset);
      }

      return (Insn)comparingBinaryInsn;
   }

   private Value fixNullConstant(Value potentialNullConstant) {
      return (Value)(potentialNullConstant instanceof NullConstant ? IntConstant.v(0) : potentialNullConstant);
   }

   public void caseEqExpr(EqExpr ee) {
      this.stmtV.addInsn(this.buildComparingBinaryInsn("EQ", ee.getOp1(), ee.getOp2()), this.origStmt);
   }

   public void caseGeExpr(GeExpr ge) {
      this.stmtV.addInsn(this.buildComparingBinaryInsn("GE", ge.getOp1(), ge.getOp2()), this.origStmt);
   }

   public void caseGtExpr(GtExpr ge) {
      this.stmtV.addInsn(this.buildComparingBinaryInsn("GT", ge.getOp1(), ge.getOp2()), this.origStmt);
   }

   public void caseLeExpr(LeExpr le) {
      this.stmtV.addInsn(this.buildComparingBinaryInsn("LE", le.getOp1(), le.getOp2()), this.origStmt);
   }

   public void caseLtExpr(LtExpr le) {
      this.stmtV.addInsn(this.buildComparingBinaryInsn("LT", le.getOp1(), le.getOp2()), this.origStmt);
   }

   public void caseNeExpr(NeExpr ne) {
      this.stmtV.addInsn(this.buildComparingBinaryInsn("NE", ne.getOp1(), ne.getOp2()), this.origStmt);
   }

   public void caseCmpExpr(CmpExpr v) {
      this.stmtV.addInsn(this.buildCmpInsn("CMP_LONG", v.getOp1(), v.getOp2()), this.origStmt);
   }

   public void caseCmpgExpr(CmpgExpr v) {
      this.stmtV.addInsn(this.buildCmpInsn("CMPG", v.getOp1(), v.getOp2()), this.origStmt);
   }

   public void caseCmplExpr(CmplExpr v) {
      this.stmtV.addInsn(this.buildCmpInsn("CMPL", v.getOp1(), v.getOp2()), this.origStmt);
   }

   private Insn buildCmpInsn(String opcodePrefix, Value firstOperand, Value secondOperand) {
      this.constantV.setOrigStmt(this.origStmt);
      Register firstReg = this.regAlloc.asImmediate(firstOperand, this.constantV);
      Register secondReg = this.regAlloc.asImmediate(secondOperand, this.constantV);
      Opcode opc = null;
      if (opcodePrefix.equals("CMP_LONG")) {
         opc = Opcode.CMP_LONG;
      } else if (firstReg.isFloat()) {
         opc = Opcode.valueOf(opcodePrefix + "_FLOAT");
      } else {
         if (!firstReg.isDouble()) {
            throw new RuntimeException("unsupported type of operands for cmp* opcode: " + firstOperand.getType());
         }

         opc = Opcode.valueOf(opcodePrefix + "_DOUBLE");
      }

      return new Insn23x(opc, this.destinationReg, firstReg, secondReg);
   }

   public void caseLengthExpr(LengthExpr le) {
      Value array = le.getOp();
      this.constantV.setOrigStmt(this.origStmt);
      Register arrayReg = this.regAlloc.asImmediate(array, this.constantV);
      this.stmtV.addInsn(new Insn12x(Opcode.ARRAY_LENGTH, this.destinationReg, arrayReg), this.origStmt);
   }

   public void caseNegExpr(NegExpr ne) {
      Value source = ne.getOp();
      this.constantV.setOrigStmt(this.origStmt);
      Register sourceReg = this.regAlloc.asImmediate(source, this.constantV);
      Type type = source.getType();
      Opcode opc;
      if (type instanceof IntegerType) {
         opc = Opcode.NEG_INT;
      } else if (type instanceof FloatType) {
         opc = Opcode.NEG_FLOAT;
      } else if (type instanceof DoubleType) {
         opc = Opcode.NEG_DOUBLE;
      } else {
         if (!(type instanceof LongType)) {
            throw new RuntimeException("unsupported value type for neg-* opcode: " + type);
         }

         opc = Opcode.NEG_LONG;
      }

      this.stmtV.addInsn(new Insn12x(opc, this.destinationReg, sourceReg), this.origStmt);
   }

   public void caseInstanceOfExpr(InstanceOfExpr ioe) {
      Value referenceToCheck = ioe.getOp();
      this.constantV.setOrigStmt(this.origStmt);
      Register referenceToCheckReg = this.regAlloc.asImmediate(referenceToCheck, this.constantV);
      TypeReference checkType = DexPrinter.toTypeReference(ioe.getCheckType());
      this.stmtV.addInsn(new Insn22c(Opcode.INSTANCE_OF, this.destinationReg, referenceToCheckReg, checkType), this.origStmt);
   }

   public void caseCastExpr(CastExpr ce) {
      Type castType = ce.getCastType();
      Value source = ce.getOp();
      this.constantV.setOrigStmt(this.origStmt);
      Register sourceReg = this.regAlloc.asImmediate(source, this.constantV);
      if (SootToDexUtils.isObject(castType)) {
         this.castObject(sourceReg, castType);
      } else {
         this.castPrimitive(sourceReg, source, castType);
      }

   }

   private void castObject(Register sourceReg, Type castType) {
      TypeReference castTypeItem = DexPrinter.toTypeReference(castType);
      if (sourceReg.getNumber() == this.destinationReg.getNumber()) {
         this.stmtV.addInsn(new Insn21c(Opcode.CHECK_CAST, this.destinationReg, castTypeItem), this.origStmt);
      } else {
         Register tmp = this.regAlloc.asTmpReg(sourceReg.getType());
         this.stmtV.addInsn(StmtVisitor.buildMoveInsn(tmp, sourceReg), this.origStmt);
         this.stmtV.addInsn(new Insn21c(Opcode.CHECK_CAST, tmp.clone(), castTypeItem), this.origStmt);
         this.stmtV.addInsn(StmtVisitor.buildMoveInsn(this.destinationReg, tmp.clone()), this.origStmt);
      }

   }

   private void castPrimitive(Register sourceReg, Value source, Type castSootType) {
      PrimitiveType castType = PrimitiveType.getByName(castSootType.toString());
      if (castType == PrimitiveType.INT && ((Value)source).getType() instanceof NullType) {
         source = IntConstant.v(0);
      }

      Type srcType = ((Value)source).getType();
      if (srcType instanceof RefType) {
         throw new RuntimeException("Trying to cast reference type " + srcType + " to a primitive");
      } else {
         PrimitiveType sourceType = PrimitiveType.getByName(srcType.toString());
         if (castType == PrimitiveType.BOOLEAN) {
            castType = PrimitiveType.INT;
            sourceType = PrimitiveType.INT;
         }

         Opcode castToIntOpc;
         if (this.shouldCastFromInt(sourceType, castType)) {
            sourceType = PrimitiveType.INT;
            castToIntOpc = this.getCastOpc(sourceType, castType);
            this.stmtV.addInsn(new Insn12x(castToIntOpc, this.destinationReg, sourceReg), this.origStmt);
         } else if (this.isMoveCompatible(sourceType, castType)) {
            if (this.destinationReg.getNumber() != sourceReg.getNumber()) {
               this.stmtV.addInsn(StmtVisitor.buildMoveInsn(this.destinationReg, sourceReg), this.origStmt);
            } else if (!this.origStmt.getBoxesPointingToThis().isEmpty()) {
               this.stmtV.addInsn(new Insn10x(Opcode.NOP), this.origStmt);
            }
         } else if (this.needsCastThroughInt(sourceType, castType)) {
            castToIntOpc = this.getCastOpc(sourceType, PrimitiveType.INT);
            Opcode castFromIntOpc = this.getCastOpc(PrimitiveType.INT, castType);
            Register tmp = this.regAlloc.asTmpReg(IntType.v());
            this.stmtV.addInsn(new Insn12x(castToIntOpc, tmp, sourceReg), this.origStmt);
            this.stmtV.addInsn(new Insn12x(castFromIntOpc, this.destinationReg, tmp.clone()), this.origStmt);
         } else {
            castToIntOpc = this.getCastOpc(sourceType, castType);
            this.stmtV.addInsn(new Insn12x(castToIntOpc, this.destinationReg, sourceReg), this.origStmt);
         }

      }
   }

   private boolean needsCastThroughInt(PrimitiveType sourceType, PrimitiveType castType) {
      return this.isEqualOrBigger(sourceType, PrimitiveType.LONG) && !this.isEqualOrBigger(castType, PrimitiveType.INT);
   }

   private boolean isMoveCompatible(PrimitiveType sourceType, PrimitiveType castType) {
      if (sourceType == castType) {
         return true;
      } else {
         return castType == PrimitiveType.INT && !this.isBiggerThan(sourceType, PrimitiveType.INT);
      }
   }

   private boolean shouldCastFromInt(PrimitiveType sourceType, PrimitiveType castType) {
      if (this.isEqualOrBigger(sourceType, PrimitiveType.INT)) {
         return false;
      } else {
         return castType != PrimitiveType.INT;
      }
   }

   private boolean isEqualOrBigger(PrimitiveType type, PrimitiveType relativeTo) {
      return type.compareTo(relativeTo) >= 0;
   }

   private boolean isBiggerThan(PrimitiveType type, PrimitiveType relativeTo) {
      return type.compareTo(relativeTo) > 0;
   }

   private boolean isSmallerThan(PrimitiveType type, PrimitiveType relativeTo) {
      return type.compareTo(relativeTo) < 0;
   }

   private Opcode getCastOpc(PrimitiveType sourceType, PrimitiveType castType) {
      Opcode opc = Opcode.valueOf(sourceType.getName().toUpperCase() + "_TO_" + castType.getName().toUpperCase());
      if (opc == null) {
         throw new RuntimeException("unsupported cast from " + sourceType + " to " + castType);
      } else {
         return opc;
      }
   }

   public void caseNewArrayExpr(NewArrayExpr nae) {
      Value size = nae.getSize();
      this.constantV.setOrigStmt(this.origStmt);
      Register sizeReg = this.regAlloc.asImmediate(size, this.constantV);
      Type baseType = nae.getBaseType();

      int numDimensions;
      ArrayType arrayType;
      for(numDimensions = 1; baseType instanceof ArrayType; ++numDimensions) {
         arrayType = (ArrayType)baseType;
         baseType = arrayType.getElementType();
      }

      arrayType = ArrayType.v(baseType, numDimensions);
      TypeReference arrayTypeItem = DexPrinter.toTypeReference(arrayType);
      this.stmtV.addInsn(new Insn22c(Opcode.NEW_ARRAY, this.destinationReg, sizeReg, arrayTypeItem), this.origStmt);
   }

   public void caseNewMultiArrayExpr(NewMultiArrayExpr nmae) {
      this.constantV.setOrigStmt(this.origStmt);
      if (nmae.getSizeCount() > 255) {
         throw new RuntimeException("number of dimensions is too high (> 255) for the filled-new-array* opcodes: " + nmae.getSizeCount());
      } else {
         short dimensions = (short)nmae.getSizeCount();
         ArrayType arrayType = ArrayType.v(nmae.getBaseType().baseType, dimensions);
         TypeReference arrayTypeItem = DexPrinter.toTypeReference(arrayType);
         List<Register> dimensionSizeRegs = new ArrayList();

         for(int i = 0; i < dimensions; ++i) {
            Value currentDimensionSize = nmae.getSize(i);
            Register currentReg = this.regAlloc.asImmediate(currentDimensionSize, this.constantV);
            dimensionSizeRegs.add(currentReg);
         }

         if (dimensions <= 5) {
            Register[] paddedRegs = this.pad35cRegs(dimensionSizeRegs);
            this.stmtV.addInsn(new Insn35c(Opcode.FILLED_NEW_ARRAY, dimensions, paddedRegs[0], paddedRegs[1], paddedRegs[2], paddedRegs[3], paddedRegs[4], arrayTypeItem), (Stmt)null);
         } else {
            this.stmtV.addInsn(new Insn3rc(Opcode.FILLED_NEW_ARRAY_RANGE, dimensionSizeRegs, dimensions, arrayTypeItem), (Stmt)null);
         }

         this.stmtV.addInsn(new Insn11x(Opcode.MOVE_RESULT_OBJECT, this.destinationReg), this.origStmt);
      }
   }

   public void caseNewExpr(NewExpr ne) {
      TypeReference baseType = DexPrinter.toTypeReference(ne.getBaseType());
      this.stmtV.addInsn(new Insn21c(Opcode.NEW_INSTANCE, this.destinationReg, baseType), this.origStmt);
   }

   public int getLastInvokeInstructionPosition() {
      return this.lastInvokeInstructionPosition;
   }
}
