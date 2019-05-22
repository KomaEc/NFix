package soot.dexpler.instructions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.jf.dexlib2.iface.instruction.FiveRegisterInstruction;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.RegisterRangeInstruction;
import soot.Type;
import soot.Unit;
import soot.dexpler.DexBody;
import soot.options.Options;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.Host;
import soot.tagkit.LineNumberTag;
import soot.tagkit.SourceLineNumberTag;

public abstract class DexlibAbstractInstruction {
   protected int lineNumber = -1;
   protected Instruction instruction;
   protected int codeAddress;
   protected Unit unit;

   public Instruction getInstruction() {
      return this.instruction;
   }

   public abstract void jimplify(DexBody var1);

   int movesRegister(int register) {
      return -1;
   }

   int movesToRegister(int register) {
      return -1;
   }

   boolean overridesRegister(int register) {
      return false;
   }

   boolean isUsedAsFloatingPoint(DexBody body, int register) {
      return false;
   }

   public Set<Type> introducedTypes() {
      return Collections.emptySet();
   }

   public DexlibAbstractInstruction(Instruction instruction, int codeAddress) {
      this.instruction = instruction;
      this.codeAddress = codeAddress;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public void setLineNumber(int lineNumber) {
      this.lineNumber = lineNumber;
   }

   protected void addTags(Host host) {
      Options options = Options.v();
      if (options.keep_line_number() && this.lineNumber != -1) {
         host.addTag(new LineNumberTag(this.lineNumber));
         host.addTag(new SourceLineNumberTag(this.lineNumber));
      }

      if (options.keep_offset()) {
         host.addTag(new BytecodeOffsetTag(this.codeAddress));
      }

   }

   public Unit getUnit() {
      return this.unit;
   }

   protected void setUnit(Unit u) {
      this.unit = u;
   }

   protected List<Integer> getUsedRegistersNums(RegisterRangeInstruction instruction) {
      List<Integer> regs = new ArrayList();
      int start = instruction.getStartRegister();

      for(int i = start; i < start + instruction.getRegisterCount(); ++i) {
         regs.add(i);
      }

      return regs;
   }

   protected List<Integer> getUsedRegistersNums(FiveRegisterInstruction instruction) {
      int[] regs = new int[]{instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG()};
      List<Integer> l = new ArrayList();

      for(int i = 0; i < instruction.getRegisterCount(); ++i) {
         l.add(regs[i]);
      }

      return l;
   }
}
