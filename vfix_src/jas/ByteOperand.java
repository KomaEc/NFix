package jas;

import java.io.DataOutputStream;
import java.io.IOException;

class ByteOperand extends InsnOperand {
   int val;

   ByteOperand(int n) {
      this.val = n;
   }

   int size(ClassEnv ce, CodeAttr code) {
      return 1;
   }

   void resolve(ClassEnv e) {
   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException {
      out.writeByte((byte)this.val);
   }
}
