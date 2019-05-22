package jas;

import java.io.DataOutputStream;
import java.io.IOException;

class UnsignedByteOperand extends InsnOperand {
   int val;

   UnsignedByteOperand(int n) {
      this.val = n;
   }

   int size(ClassEnv ce, CodeAttr code) {
      return 1;
   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
      if (this.val >= 256) {
         throw new jasError("Operand is too large (" + this.val + ") for this instruction");
      } else {
         out.writeByte((byte)(255 & this.val));
      }
   }

   void resolve(ClassEnv e) {
   }
}
