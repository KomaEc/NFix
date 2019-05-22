package jas;

import java.io.DataOutputStream;
import java.io.IOException;

class InvokeinterfaceOperand extends InsnOperand {
   CP cpe;
   int nargs;

   InvokeinterfaceOperand(CP cpe, int nargs) {
      this.cpe = cpe;
      this.nargs = nargs;
   }

   int size(ClassEnv ce, CodeAttr code) {
      return 4;
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.cpe);
   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(this.cpe));
      out.writeByte((byte)(255 & this.nargs));
      out.writeByte(0);
   }
}
