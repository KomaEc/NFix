package jas;

import java.io.DataOutputStream;
import java.io.IOException;

class LdcOperand extends InsnOperand implements RuntimeConstants {
   CP cpe;
   Insn source;
   boolean wide;

   int size(ClassEnv ce, CodeAttr code) throws jasError {
      if (this.wide) {
         return 2;
      } else {
         int idx = ce.getCPIndex(this.cpe);
         if (idx > 255) {
            this.wide = true;
            this.source.opc = 19;
            return 2;
         } else {
            return 1;
         }
      }
   }

   LdcOperand(Insn s, CP cpe) {
      this.source = s;
      this.cpe = cpe;
      this.wide = true;
   }

   LdcOperand(Insn s, CP cpe, boolean wide) {
      this.source = s;
      this.cpe = cpe;
      this.wide = wide;
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.cpe);
      if (this.cpe instanceof ClassCP) {
         e.requireJava5();
      }

   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
      int idx = e.getCPIndex(this.cpe);
      if (this.wide) {
         out.writeShort((short)idx);
      } else {
         if (idx > 255) {
            throw new jasError("exceeded size for small cpidx" + this.cpe);
         }

         out.writeByte((byte)(255 & idx));
      }

   }
}
