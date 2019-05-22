package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class LongElemValPair extends ElemValPair {
   LongCP val;

   void resolve(ClassEnv e) {
      super.resolve(e);
      e.addCPItem(this.val);
   }

   public LongElemValPair(String name, char kind, long val) {
      super(name, kind);
      this.val = new LongCP(val);
   }

   int size() {
      return super.size() + 2;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      super.write(e, out);
      out.writeShort(e.getCPIndex(this.val));
   }
}
