package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class SignatureAttr {
   static CP attr = new AsciiCP("Signature");
   AsciiCP sig;

   void resolve(ClassEnv e) {
      e.addCPItem(attr);
      e.addCPItem(this.sig);
   }

   public SignatureAttr(String s) {
      this.sig = new AsciiCP(s);
   }

   int size() {
      return 2;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(attr));
      out.writeInt(2);
      out.writeShort(e.getCPIndex(this.sig));
   }
}
