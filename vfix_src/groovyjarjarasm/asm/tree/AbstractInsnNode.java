package groovyjarjarasm.asm.tree;

import groovyjarjarasm.asm.MethodVisitor;
import java.util.List;
import java.util.Map;

public abstract class AbstractInsnNode {
   public static final int INSN = 0;
   public static final int INT_INSN = 1;
   public static final int VAR_INSN = 2;
   public static final int TYPE_INSN = 3;
   public static final int FIELD_INSN = 4;
   public static final int METHOD_INSN = 5;
   public static final int JUMP_INSN = 6;
   public static final int LABEL = 7;
   public static final int LDC_INSN = 8;
   public static final int IINC_INSN = 9;
   public static final int TABLESWITCH_INSN = 10;
   public static final int LOOKUPSWITCH_INSN = 11;
   public static final int MULTIANEWARRAY_INSN = 12;
   public static final int FRAME = 13;
   public static final int LINE = 14;
   protected int opcode;
   AbstractInsnNode prev;
   AbstractInsnNode next;
   int index;

   protected AbstractInsnNode(int var1) {
      this.opcode = var1;
      this.index = -1;
   }

   public int getOpcode() {
      return this.opcode;
   }

   public abstract int getType();

   public AbstractInsnNode getPrevious() {
      return this.prev;
   }

   public AbstractInsnNode getNext() {
      return this.next;
   }

   public abstract void accept(MethodVisitor var1);

   public abstract AbstractInsnNode clone(Map var1);

   static LabelNode clone(LabelNode var0, Map var1) {
      return (LabelNode)var1.get(var0);
   }

   static LabelNode[] clone(List var0, Map var1) {
      LabelNode[] var2 = new LabelNode[var0.size()];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = (LabelNode)var1.get(var0.get(var3));
      }

      return var2;
   }
}
