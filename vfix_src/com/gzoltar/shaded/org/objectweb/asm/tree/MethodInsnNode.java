package com.gzoltar.shaded.org.objectweb.asm.tree;

import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import java.util.Map;

public class MethodInsnNode extends AbstractInsnNode {
   public String owner;
   public String name;
   public String desc;
   public boolean itf;

   /** @deprecated */
   @Deprecated
   public MethodInsnNode(int opcode, String owner, String name, String desc) {
      this(opcode, owner, name, desc, opcode == 185);
   }

   public MethodInsnNode(int opcode, String owner, String name, String desc, boolean itf) {
      super(opcode);
      this.owner = owner;
      this.name = name;
      this.desc = desc;
      this.itf = itf;
   }

   public void setOpcode(int opcode) {
      this.opcode = opcode;
   }

   public int getType() {
      return 5;
   }

   public void accept(MethodVisitor mv) {
      mv.visitMethodInsn(this.opcode, this.owner, this.name, this.desc, this.itf);
      this.acceptAnnotations(mv);
   }

   public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
      return new MethodInsnNode(this.opcode, this.owner, this.name, this.desc, this.itf);
   }
}
