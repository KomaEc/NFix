package jas;

import java.io.DataOutputStream;
import java.io.IOException;

class LookupswitchOperand extends InsnOperand {
   Label dflt;
   Insn source;
   int[] match;
   Label[] jmp;

   LookupswitchOperand(Insn s, Label def, int[] m, Label[] j) {
      this.dflt = def;
      this.jmp = j;
      this.match = m;
      this.source = s;
   }

   void resolve(ClassEnv e) {
   }

   int size(ClassEnv ce, CodeAttr code) throws jasError {
      int sz = 8;
      int source_pc = code.getPc(this.source);
      if ((source_pc + 1) % 4 != 0) {
         sz += 4 - (source_pc + 1) % 4;
      }

      if (this.jmp != null) {
         sz += 8 * this.jmp.length;
      }

      return sz;
   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
      int source_pc = ce.getPc(this.source);
      int x;
      if ((source_pc + 1) % 4 != 0) {
         int pad = 4 - (source_pc + 1) % 4;

         for(x = 0; x < pad; ++x) {
            out.writeByte(0);
         }
      }

      this.dflt.writeWideOffset(ce, this.source, out);
      if (this.jmp == null) {
         out.writeInt(0);
      } else {
         out.writeInt(this.jmp.length);

         for(x = 0; x < this.jmp.length; ++x) {
            out.writeInt(this.match[x]);
            this.jmp[x].writeWideOffset(ce, this.source, out);
         }
      }

   }
}
