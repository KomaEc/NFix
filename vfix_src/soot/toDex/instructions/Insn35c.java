package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction35c;
import org.jf.dexlib2.iface.reference.Reference;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;

public class Insn35c extends AbstractInsn implements FiveRegInsn {
   private int regCount;
   private final Reference referencedItem;

   public Insn35c(Opcode opc, int regCount, Register regD, Register regE, Register regF, Register regG, Register regA, Reference referencedItem) {
      super(opc);
      this.regCount = regCount;
      this.regs.add(regD);
      this.regs.add(regE);
      this.regs.add(regF);
      this.regs.add(regG);
      this.regs.add(regA);
      this.referencedItem = referencedItem;
   }

   public Register getRegD() {
      return (Register)this.regs.get(0);
   }

   public Register getRegE() {
      return (Register)this.regs.get(1);
   }

   public Register getRegF() {
      return (Register)this.regs.get(2);
   }

   public Register getRegG() {
      return (Register)this.regs.get(3);
   }

   public Register getRegA() {
      return (Register)this.regs.get(4);
   }

   private static boolean isImplicitWide(Register firstReg, Register secondReg) {
      return firstReg.isWide() && secondReg.isEmptyReg();
   }

   private static int getPossiblyWideNumber(Register reg, Register previousReg) {
      return isImplicitWide(previousReg, reg) ? previousReg.getNumber() + 1 : reg.getNumber();
   }

   private int[] getRealRegNumbers() {
      int[] realRegNumbers = new int[5];
      Register regD = this.getRegD();
      Register regE = this.getRegE();
      Register regF = this.getRegF();
      Register regG = this.getRegG();
      Register regA = this.getRegA();
      realRegNumbers[0] = regD.getNumber();
      realRegNumbers[1] = getPossiblyWideNumber(regE, regD);
      realRegNumbers[2] = getPossiblyWideNumber(regF, regE);
      realRegNumbers[3] = getPossiblyWideNumber(regG, regF);
      realRegNumbers[4] = getPossiblyWideNumber(regA, regG);
      return realRegNumbers;
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      int[] realRegNumbers = this.getRealRegNumbers();
      byte regDNumber = (byte)realRegNumbers[0];
      byte regENumber = (byte)realRegNumbers[1];
      byte regFNumber = (byte)realRegNumbers[2];
      byte regGNumber = (byte)realRegNumbers[3];
      byte regANumber = (byte)realRegNumbers[4];
      return new BuilderInstruction35c(this.opc, this.regCount, regDNumber, regENumber, regFNumber, regGNumber, regANumber, this.referencedItem);
   }

   public BitSet getIncompatibleRegs() {
      BitSet incompatRegs = new BitSet(5);
      int[] realRegNumbers = this.getRealRegNumbers();

      for(int i = 0; i < realRegNumbers.length; ++i) {
         boolean isCompatible = Register.fitsByte(realRegNumbers[i], false);
         if (!isCompatible) {
            incompatRegs.set(i);
            Register possibleSecondHalf = (Register)this.regs.get(i);
            if (possibleSecondHalf.isEmptyReg() && i > 0) {
               Register possibleFirstHalf = (Register)this.regs.get(i - 1);
               if (possibleFirstHalf.isWide()) {
                  incompatRegs.set(i - 1);
               }
            }
         }
      }

      return incompatRegs;
   }

   public String toString() {
      return super.toString() + " (" + this.regCount + " regs), ref: " + this.referencedItem;
   }
}
