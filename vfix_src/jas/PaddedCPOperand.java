package jas;

import java.io.DataOutputStream;
import java.io.IOException;

class PaddedCPOperand extends CPOperand {
   PaddedCPOperand(CP cpe) {
      super(cpe);
   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
      super.write(e, ce, out);
      out.writeShort(0);
   }

   int size(ClassEnv ce, CodeAttr code) {
      return super.size(ce, code) + 2;
   }
}
