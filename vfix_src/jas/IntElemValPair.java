package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class IntElemValPair extends ElemValPair {
   IntegerCP val;

   void resolve(ClassEnv e) {
      super.resolve(e);
      e.addCPItem(this.val);
   }

   public IntElemValPair(String name, char kind, int val) {
      super(name, kind);
      this.val = new IntegerCP(val);
   }

   int size() {
      return super.size() + 2;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      super.write(e, out);
      out.writeShort(e.getCPIndex(this.val));
   }
}
