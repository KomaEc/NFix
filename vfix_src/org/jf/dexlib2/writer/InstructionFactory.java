package org.jf.dexlib2.writer;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.reference.Reference;

public interface InstructionFactory<Ref extends Reference> {
   Instruction makeInstruction10t(@Nonnull Opcode var1, int var2);

   Instruction makeInstruction10x(@Nonnull Opcode var1);

   Instruction makeInstruction11n(@Nonnull Opcode var1, int var2, int var3);

   Instruction makeInstruction11x(@Nonnull Opcode var1, int var2);

   Instruction makeInstruction12x(@Nonnull Opcode var1, int var2, int var3);

   Instruction makeInstruction20bc(@Nonnull Opcode var1, int var2, @Nonnull Ref var3);

   Instruction makeInstruction20t(@Nonnull Opcode var1, int var2);

   Instruction makeInstruction21c(@Nonnull Opcode var1, int var2, @Nonnull Ref var3);

   Instruction makeInstruction21ih(@Nonnull Opcode var1, int var2, int var3);

   Instruction makeInstruction21lh(@Nonnull Opcode var1, int var2, long var3);

   Instruction makeInstruction21s(@Nonnull Opcode var1, int var2, int var3);

   Instruction makeInstruction21t(@Nonnull Opcode var1, int var2, int var3);

   Instruction makeInstruction22b(@Nonnull Opcode var1, int var2, int var3, int var4);

   Instruction makeInstruction22c(@Nonnull Opcode var1, int var2, int var3, @Nonnull Ref var4);

   Instruction makeInstruction22s(@Nonnull Opcode var1, int var2, int var3, int var4);

   Instruction makeInstruction22t(@Nonnull Opcode var1, int var2, int var3, int var4);

   Instruction makeInstruction22x(@Nonnull Opcode var1, int var2, int var3);

   Instruction makeInstruction23x(@Nonnull Opcode var1, int var2, int var3, int var4);

   Instruction makeInstruction30t(@Nonnull Opcode var1, int var2);

   Instruction makeInstruction31c(@Nonnull Opcode var1, int var2, @Nonnull Ref var3);

   Instruction makeInstruction31i(@Nonnull Opcode var1, int var2, int var3);

   Instruction makeInstruction31t(@Nonnull Opcode var1, int var2, int var3);

   Instruction makeInstruction32x(@Nonnull Opcode var1, int var2, int var3);

   Instruction makeInstruction35c(@Nonnull Opcode var1, int var2, int var3, int var4, int var5, int var6, int var7, @Nonnull Ref var8);

   Instruction makeInstruction3rc(@Nonnull Opcode var1, int var2, int var3, @Nonnull Ref var4);

   Instruction makeInstruction51l(@Nonnull Opcode var1, int var2, long var3);

   Instruction makeSparseSwitchPayload(@Nullable List<? extends SwitchElement> var1);

   Instruction makePackedSwitchPayload(@Nullable List<? extends SwitchElement> var1);

   Instruction makeArrayPayload(int var1, @Nullable List<Number> var2);
}
