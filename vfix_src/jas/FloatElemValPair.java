package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class FloatElemValPair extends ElemValPair {
   FloatCP val;

   void resolve(ClassEnv e) {
      super.resolve(e);
      e.addCPItem(this.val);
   }

   public FloatElemValPair(String name, char kind, float val) {
      super(name, kind);
      this.val = new FloatCP(val);
   }

   int size() {
      return super.size() + 2;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      super.write(e, out);
      out.writeShort(e.getCPIndex(this.val));
   }
}
