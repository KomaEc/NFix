package soot.toDex;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.immutable.reference.ImmutableStringReference;
import org.jf.dexlib2.immutable.reference.ImmutableTypeReference;
import soot.jimple.AbstractConstantSwitch;
import soot.jimple.ClassConstant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.toDex.instructions.Insn;
import soot.toDex.instructions.Insn11n;
import soot.toDex.instructions.Insn21c;
import soot.toDex.instructions.Insn21s;
import soot.toDex.instructions.Insn31i;
import soot.toDex.instructions.Insn51l;

class ConstantVisitor extends AbstractConstantSwitch {
   private StmtVisitor stmtV;
   private Register destinationReg;
   private Stmt origStmt;

   public ConstantVisitor(StmtVisitor stmtV) {
      this.stmtV = stmtV;
   }

   public void setDestination(Register destinationReg) {
      this.destinationReg = destinationReg;
   }

   public void setOrigStmt(Stmt stmt) {
      this.origStmt = stmt;
   }

   public void defaultCase(Object o) {
      throw new Error("unknown Object (" + o.getClass() + ") as Constant: " + o);
   }

   public void caseStringConstant(StringConstant s) {
      StringReference ref = new ImmutableStringReference(s.value);
      this.stmtV.addInsn(new Insn21c(Opcode.CONST_STRING, this.destinationReg, ref), this.origStmt);
   }

   public void caseClassConstant(ClassConstant c) {
      TypeReference referencedClass = new ImmutableTypeReference(c.getValue());
      this.stmtV.addInsn(new Insn21c(Opcode.CONST_CLASS, this.destinationReg, referencedClass), this.origStmt);
   }

   public void caseLongConstant(LongConstant l) {
      long constant = l.value;
      this.stmtV.addInsn(this.buildConstWideInsn(constant), this.origStmt);
   }

   private Insn buildConstWideInsn(long literal) {
      if (SootToDexUtils.fitsSigned16(literal)) {
         return new Insn21s(Opcode.CONST_WIDE_16, this.destinationReg, (short)((int)literal));
      } else {
         return (Insn)(SootToDexUtils.fitsSigned32(literal) ? new Insn31i(Opcode.CONST_WIDE_32, this.destinationReg, (int)literal) : new Insn51l(Opcode.CONST_WIDE, this.destinationReg, literal));
      }
   }

   public void caseDoubleConstant(DoubleConstant d) {
      long longBits = Double.doubleToLongBits(d.value);
      this.stmtV.addInsn(this.buildConstWideInsn(longBits), this.origStmt);
   }

   public void caseFloatConstant(FloatConstant f) {
      int intBits = Float.floatToIntBits(f.value);
      this.stmtV.addInsn(this.buildConstInsn(intBits), this.origStmt);
   }

   private Insn buildConstInsn(int literal) {
      if (SootToDexUtils.fitsSigned4((long)literal)) {
         return new Insn11n(Opcode.CONST_4, this.destinationReg, (byte)literal);
      } else {
         return (Insn)(SootToDexUtils.fitsSigned16((long)literal) ? new Insn21s(Opcode.CONST_16, this.destinationReg, (short)literal) : new Insn31i(Opcode.CONST, this.destinationReg, literal));
      }
   }

   public void caseIntConstant(IntConstant i) {
      this.stmtV.addInsn(this.buildConstInsn(i.value), this.origStmt);
   }

   public void caseNullConstant(NullConstant v) {
      this.stmtV.addInsn(this.buildConstInsn(0), this.origStmt);
   }
}
