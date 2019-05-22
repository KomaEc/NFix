package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class ClassElemValPair extends ElemValPair {
   AsciiCP cval;

   void resolve(ClassEnv e) {
      super.resolve(e);
      e.addCPItem(this.cval);
   }

   public ClassElemValPair(String name, char kind, String cval) {
      super(name, kind);
      this.cval = new AsciiCP(cval);
   }

   int size() {
      return super.size() + 2;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      super.write(e, out);
      out.writeShort(e.getCPIndex(this.cval));
   }
}
