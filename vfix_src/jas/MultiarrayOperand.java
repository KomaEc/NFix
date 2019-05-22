package jas;

import java.io.DataOutputStream;
import java.io.IOException;

class MultiarrayOperand extends InsnOperand {
   CP cpe;
   int sz;

   MultiarrayOperand(CP cpe, int sz) {
      this.cpe = cpe;
      this.sz = sz;
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.cpe);
   }

   int size(ClassEnv ce, CodeAttr code) {
      return 3;
   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(this.cpe));
      out.writeByte((byte)(255 & this.sz));
   }
}
