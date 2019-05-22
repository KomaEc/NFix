package jas;

import java.io.DataOutputStream;
import java.io.IOException;

class IntegerOperand extends InsnOperand {
   int val;

   IntegerOperand(int n) {
      this.val = n;
   }

   int size(ClassEnv ce, CodeAttr code) {
      return 4;
   }

   void resolve(ClassEnv e) {
   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException {
      out.writeInt(this.val);
   }
}
