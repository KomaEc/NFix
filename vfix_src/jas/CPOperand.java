package jas;

import java.io.DataOutputStream;
import java.io.IOException;

class CPOperand extends InsnOperand {
   CP cpe;
   boolean wide;

   int size(ClassEnv ce, CodeAttr code) {
      return this.wide ? 2 : 1;
   }

   CPOperand(CP cpe) {
      this.cpe = cpe;
      this.wide = true;
   }

   CPOperand(CP cpe, boolean wide) {
      this.cpe = cpe;
      this.wide = wide;
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.cpe);
   }

   void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
      int idx = e.getCPIndex(this.cpe);
      if (this.wide) {
         out.writeShort((short)idx);
      } else {
         if (idx > 255) {
            throw new jasError("exceeded size for small cpidx" + this.cpe);
         }

         out.writeByte((byte)(255 & idx));
      }

   }
}
