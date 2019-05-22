package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class ConstAttr {
   static CP attr = new AsciiCP("ConstantValue");
   CP val;

   public ConstAttr(CP val) {
      this.val = val;
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.val);
      e.addCPItem(attr);
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(attr));
      out.writeInt(2);
      out.writeShort(e.getCPIndex(this.val));
   }
}
