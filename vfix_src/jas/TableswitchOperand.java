package jas;

import java.io.DataOutputStream;
import java.io.IOException;

class TableswitchOperand extends InsnOperand {
   int min;
   int max;
   Label dflt;
   Label[] jmp;
   Insn source;

   TableswitchOperand(Insn s, int min, int max, Label def, Label[] j) {
      this.min = min;
      this.max = max;
      this.dflt = def;
      this.jmp = j;
      this.source = s;
   }

   void resolve(ClassEnv e) {
   }

   int size(ClassEnv ce, CodeAttr code) throws jasError {
      int sz = 12;
      int source_pc = code.getPc(this.source);
      if ((source_pc + 1) % 4 != 0) {
         sz += 4 - (source_pc + 1) % 4;
      }

      if (this.jmp != null) {
         sz += 4 * this.jmp.length;
      }

      return sz;
   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
      int source_pc = ce.getPc(this.source);
      int cnt;
      if ((source_pc + 1) % 4 != 0) {
         int pad = 4 - (source_pc + 1) % 4;

         for(cnt = 0; cnt < pad; ++cnt) {
            out.writeByte(0);
         }
      }

      this.dflt.writeWideOffset(ce, this.source, out);
      out.writeInt(this.min);
      out.writeInt(this.max);
      cnt = this.jmp.length;

      for(int x = 0; x < cnt; ++x) {
         this.jmp[x].writeWideOffset(ce, this.source, out);
      }

   }
}
