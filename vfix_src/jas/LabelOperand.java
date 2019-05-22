package jas;

import java.io.DataOutputStream;
import java.io.IOException;

class LabelOperand extends InsnOperand {
   Label target;
   Insn source;
   boolean wide;

   LabelOperand(Label l, Insn source) {
      this.target = l;
      this.source = source;
      this.wide = false;
   }

   LabelOperand(Label l, Insn source, boolean wide) {
      this.target = l;
      this.source = source;
      this.wide = wide;
   }

   int size(ClassEnv ce, CodeAttr code) {
      return this.wide ? 4 : 2;
   }

   void resolve(ClassEnv e) {
   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
      if (this.wide) {
         this.target.writeWideOffset(ce, this.source, out);
      } else {
         this.target.writeOffset(ce, this.source, out);
      }

   }
}
