package com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter;

import com.gzoltar.shaded.org.objectweb.asm.tree.AbstractInsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.MethodInsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.VarInsnNode;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractMatcher {
   final Map<String, VarInsnNode> vars = new HashMap();
   AbstractInsnNode cursor;

   final void nextIsAddSuppressed() {
      this.nextIs(182);
      if (this.cursor != null) {
         MethodInsnNode m = (MethodInsnNode)this.cursor;
         if (!"java/lang/Throwable".equals(m.owner) || !"addSuppressed".equals(m.name)) {
            this.cursor = null;
         }
      }
   }

   final void nextIsVar(int opcode, String name) {
      this.nextIs(opcode);
      if (this.cursor != null) {
         VarInsnNode actual = (VarInsnNode)this.cursor;
         VarInsnNode expected = (VarInsnNode)this.vars.get(name);
         if (expected == null) {
            this.vars.put(name, actual);
         } else if (expected.var != actual.var) {
            this.cursor = null;
         }

      }
   }

   final void nextIs(int opcode) {
      this.next();
      if (this.cursor != null) {
         if (this.cursor.getOpcode() != opcode) {
            this.cursor = null;
         }

      }
   }

   final void next() {
      if (this.cursor != null) {
         this.cursor = this.cursor.getNext();
         this.skipNonOpcodes();
      }
   }

   final void skipNonOpcodes() {
      while(this.cursor != null && (this.cursor.getType() == 14 || this.cursor.getType() == 8 || this.cursor.getType() == 15)) {
         this.cursor = this.cursor.getNext();
      }

   }
}
