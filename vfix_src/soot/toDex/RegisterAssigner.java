package soot.toDex;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.jf.dexlib2.Opcode;
import soot.jimple.Stmt;
import soot.toDex.instructions.AddressInsn;
import soot.toDex.instructions.Insn;
import soot.toDex.instructions.Insn11n;
import soot.toDex.instructions.Insn21s;
import soot.toDex.instructions.Insn23x;
import soot.toDex.instructions.TwoRegInsn;

class RegisterAssigner {
   private RegisterAllocator regAlloc;

   public RegisterAssigner(RegisterAllocator regAlloc) {
      this.regAlloc = regAlloc;
   }

   public List<Insn> finishRegs(List<Insn> insns, Map<Insn, Stmt> insnsStmtMap, Map<Insn, LocalRegisterAssignmentInformation> instructionRegisterMap, List<LocalRegisterAssignmentInformation> parameterInstructionsList) {
      this.renumParamRegsToHigh(insns, parameterInstructionsList);
      this.reserveRegisters(insns, insnsStmtMap, parameterInstructionsList);
      RegisterAssigner.InstructionIterator insnIter = new RegisterAssigner.InstructionIterator(insns, insnsStmtMap, instructionRegisterMap);

      while(insnIter.hasNext()) {
         Insn oldInsn = insnIter.next();
         if (oldInsn.hasIncompatibleRegs()) {
            Insn fittingInsn = this.findFittingInsn(oldInsn);
            if (fittingInsn != null) {
               insnIter.set(fittingInsn, oldInsn);
            } else {
               this.fixIncompatRegs(oldInsn, insnIter);
            }
         }
      }

      return insns;
   }

   private void renumParamRegsToHigh(List<Insn> insns, List<LocalRegisterAssignmentInformation> parameterInstructionsList) {
      int regCount = this.regAlloc.getRegCount();
      int paramRegCount = this.regAlloc.getParamRegCount();
      if (paramRegCount != 0 && paramRegCount != regCount) {
         Iterator var5 = insns.iterator();

         while(var5.hasNext()) {
            Insn insn = (Insn)var5.next();
            Iterator var7 = insn.getRegs().iterator();

            while(var7.hasNext()) {
               Register r = (Register)var7.next();
               this.renumParamRegToHigh(r, regCount, paramRegCount);
            }
         }

         var5 = parameterInstructionsList.iterator();

         while(var5.hasNext()) {
            LocalRegisterAssignmentInformation parameter = (LocalRegisterAssignmentInformation)var5.next();
            this.renumParamRegToHigh(parameter.getRegister(), regCount, paramRegCount);
         }

      }
   }

   private void renumParamRegToHigh(Register r, int regCount, int paramRegCount) {
      int oldNum = r.getNumber();
      int newNormalRegNum;
      if (oldNum >= paramRegCount) {
         newNormalRegNum = oldNum - paramRegCount;
         r.setNumber(newNormalRegNum);
      } else {
         newNormalRegNum = oldNum + regCount - paramRegCount;
         r.setNumber(newNormalRegNum);
      }

   }

   private void reserveRegisters(List<Insn> insns, Map<Insn, Stmt> insnsStmtMap, List<LocalRegisterAssignmentInformation> parameterInstructionsList) {
      int reservedRegs = 0;

      while(true) {
         int regsNeeded = this.getRegsNeeded(reservedRegs, insns, insnsStmtMap);
         int regsToReserve = regsNeeded - reservedRegs;
         if (regsToReserve <= 0) {
            return;
         }

         this.regAlloc.increaseRegCount(regsToReserve);
         Iterator var7 = insns.iterator();

         while(var7.hasNext()) {
            Insn insn = (Insn)var7.next();
            this.shiftRegs(insn, regsToReserve);
         }

         var7 = parameterInstructionsList.iterator();

         while(var7.hasNext()) {
            LocalRegisterAssignmentInformation info = (LocalRegisterAssignmentInformation)var7.next();
            Register r = info.getRegister();
            r.setNumber(r.getNumber() + regsToReserve);
         }

         reservedRegs += regsToReserve;
      }
   }

   private int getRegsNeeded(int regsAlreadyReserved, List<Insn> insns, Map<Insn, Stmt> insnsStmtMap) {
      int regsNeeded = regsAlreadyReserved;

      for(int i = 0; i < insns.size(); ++i) {
         Insn insn = (Insn)insns.get(i);
         if (!(insn instanceof AddressInsn)) {
            Insn fittingInsn = this.findFittingInsn(insn);
            if (fittingInsn != null) {
               insns.set(i, fittingInsn);
               insnsStmtMap.put(fittingInsn, insnsStmtMap.get(insn));
               insnsStmtMap.remove(insn);
            } else {
               int newRegsNeeded = insn.getMinimumRegsNeeded();
               if (newRegsNeeded > regsNeeded) {
                  regsNeeded = newRegsNeeded;
               }
            }
         }
      }

      return regsNeeded;
   }

   private void shiftRegs(Insn insn, int shiftAmount) {
      Iterator var3 = insn.getRegs().iterator();

      while(var3.hasNext()) {
         Register r = (Register)var3.next();
         r.setNumber(r.getNumber() + shiftAmount);
      }

   }

   private void fixIncompatRegs(Insn insn, RegisterAssigner.InstructionIterator allInsns) {
      List<Register> regs = insn.getRegs();
      BitSet incompatRegs = insn.getIncompatibleRegs();
      Register resultReg = (Register)regs.get(0);
      boolean hasResultReg = insn.getOpcode().setsRegister() || insn.getOpcode().setsWideRegister();
      boolean isResultRegIncompat = incompatRegs.get(0);
      if (hasResultReg && isResultRegIncompat && !insn.getOpcode().name.endsWith("/2addr") && !insn.getOpcode().name.equals("check-cast")) {
         incompatRegs.clear(0);
      }

      if (incompatRegs.cardinality() > 0) {
         this.addMovesForIncompatRegs(insn, allInsns, regs, incompatRegs);
      }

      if (hasResultReg && isResultRegIncompat) {
         Register resultRegClone = resultReg.clone();
         this.addMoveForIncompatResultReg(allInsns, resultRegClone, resultReg, insn);
      }

   }

   private void addMoveForIncompatResultReg(RegisterAssigner.InstructionIterator insns, Register destReg, Register origResultReg, Insn curInsn) {
      if (destReg.getNumber() != 0) {
         origResultReg.setNumber(0);
         Register sourceReg = new Register(destReg.getType(), 0);
         Insn extraMove = StmtVisitor.buildMoveInsn(destReg, sourceReg);
         insns.add(extraMove, curInsn, destReg);
      }
   }

   private void addMovesForIncompatRegs(Insn curInsn, RegisterAssigner.InstructionIterator insns, List<Register> regs, BitSet incompatRegs) {
      Register newRegister = null;
      Register resultReg = (Register)regs.get(0);
      boolean hasResultReg = curInsn.getOpcode().setsRegister() || curInsn.getOpcode().setsWideRegister();
      Insn moveResultInsn = null;
      insns.previous();
      int nextNewDestination = 0;

      for(int regIdx = 0; regIdx < regs.size(); ++regIdx) {
         if (incompatRegs.get(regIdx)) {
            Register incompatReg = (Register)regs.get(regIdx);
            if (!incompatReg.isEmptyReg()) {
               Register source = incompatReg.clone();
               Register destination = new Register(source.getType(), nextNewDestination);
               nextNewDestination += SootToDexUtils.getDexWords(source.getType());
               if (source.getNumber() != destination.getNumber()) {
                  Insn extraMove = StmtVisitor.buildMoveInsn(destination, source);
                  insns.add(extraMove, curInsn, (Register)null);
                  incompatReg.setNumber(destination.getNumber());
                  if (hasResultReg && regIdx == resultReg.getNumber()) {
                     moveResultInsn = StmtVisitor.buildMoveInsn(source, destination);
                     newRegister = destination;
                  }
               }
            }
         }
      }

      insns.next();
      if (moveResultInsn != null) {
         insns.add(moveResultInsn, curInsn, newRegister);
      }

   }

   private Insn findFittingInsn(Insn insn) {
      if (!insn.hasIncompatibleRegs()) {
         return null;
      } else {
         Opcode opc = insn.getOpcode();
         if (insn instanceof Insn11n && opc.equals(Opcode.CONST_4)) {
            Insn11n unfittingInsn = (Insn11n)insn;
            if (unfittingInsn.getRegA().fitsShort()) {
               return new Insn21s(Opcode.CONST_16, unfittingInsn.getRegA(), (short)unfittingInsn.getLitB());
            }
         } else {
            Register regA;
            Register regB;
            if (insn instanceof TwoRegInsn && opc.name.endsWith("_2ADDR")) {
               regA = ((TwoRegInsn)insn).getRegA();
               regB = ((TwoRegInsn)insn).getRegB();
               if (regA.fitsShort() && regB.fitsShort()) {
                  int oldOpcLength = opc.name.length();
                  String newOpcName = opc.name.substring(0, oldOpcLength - 6);
                  Opcode newOpc = Opcode.valueOf(newOpcName);
                  Register regAClone = regA.clone();
                  return new Insn23x(newOpc, regA, regAClone, regB);
               }
            } else if (insn instanceof TwoRegInsn && SootToDexUtils.isNormalMove(opc)) {
               regA = ((TwoRegInsn)insn).getRegA();
               regB = ((TwoRegInsn)insn).getRegB();
               if (regA.getNumber() != regB.getNumber()) {
                  return StmtVisitor.buildMoveInsn(regA, regB);
               }
            }
         }

         return null;
      }
   }

   private class InstructionIterator implements Iterator<Insn> {
      private final ListIterator<Insn> insnsIterator;
      private final Map<Insn, Stmt> insnStmtMap;
      private final Map<Insn, LocalRegisterAssignmentInformation> insnRegisterMap;

      public InstructionIterator(List<Insn> insns, Map<Insn, Stmt> insnStmtMap, Map<Insn, LocalRegisterAssignmentInformation> insnRegisterMap) {
         this.insnStmtMap = insnStmtMap;
         this.insnsIterator = insns.listIterator();
         this.insnRegisterMap = insnRegisterMap;
      }

      public boolean hasNext() {
         return this.insnsIterator.hasNext();
      }

      public Insn next() {
         return (Insn)this.insnsIterator.next();
      }

      public Insn previous() {
         return (Insn)this.insnsIterator.previous();
      }

      public void remove() {
         this.insnsIterator.remove();
      }

      public void add(Insn element, Insn forOriginal, Register newRegister) {
         LocalRegisterAssignmentInformation originalRegisterLocal = (LocalRegisterAssignmentInformation)this.insnRegisterMap.get(forOriginal);
         if (originalRegisterLocal != null) {
            if (newRegister != null) {
               this.insnRegisterMap.put(element, LocalRegisterAssignmentInformation.v(newRegister, ((LocalRegisterAssignmentInformation)this.insnRegisterMap.get(forOriginal)).getLocal()));
            } else {
               this.insnRegisterMap.put(element, originalRegisterLocal);
            }
         }

         if (this.insnStmtMap.containsKey(forOriginal)) {
            this.insnStmtMap.put(element, this.insnStmtMap.get(forOriginal));
         }

         this.insnsIterator.add(element);
      }

      public void set(Insn element, Insn forOriginal) {
         LocalRegisterAssignmentInformation originalRegisterLocal = (LocalRegisterAssignmentInformation)this.insnRegisterMap.get(forOriginal);
         if (originalRegisterLocal != null) {
            this.insnRegisterMap.put(element, originalRegisterLocal);
            this.insnRegisterMap.remove(forOriginal);
         }

         if (this.insnStmtMap.containsKey(forOriginal)) {
            this.insnStmtMap.put(element, this.insnStmtMap.get(forOriginal));
            this.insnStmtMap.remove(forOriginal);
         }

         this.insnsIterator.set(element);
      }
   }
}
