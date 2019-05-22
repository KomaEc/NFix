package jas;

import java.io.DataOutputStream;
import java.io.IOException;

class ShortOperand extends InsnOperand {
   int offset;

   ShortOperand(int n) {
      this.offset = n;
   }

   void resolve(ClassEnv e) {
   }

   int size(ClassEnv ce, CodeAttr code) {
      return 2;
   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException {
      out.writeShort((short)this.offset);
   }
}
