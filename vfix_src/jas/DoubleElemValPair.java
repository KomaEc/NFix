package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class DoubleElemValPair extends ElemValPair {
   DoubleCP val;

   void resolve(ClassEnv e) {
      super.resolve(e);
      e.addCPItem(this.val);
   }

   public DoubleElemValPair(String name, char kind, double val) {
      super(name, kind);
      this.val = new DoubleCP(val);
   }

   int size() {
      return super.size() + 2;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      super.write(e, out);
      out.writeShort(e.getCPIndex(this.val));
   }
}
