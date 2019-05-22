package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class EnumElemValPair extends ElemValPair {
   AsciiCP tval;
   AsciiCP cval;

   void resolve(ClassEnv e) {
      super.resolve(e);
      e.addCPItem(this.tval);
      e.addCPItem(this.cval);
   }

   public EnumElemValPair(String name, char kind, String tval, String cval) {
      super(name, kind);
      this.tval = new AsciiCP(tval);
      this.cval = new AsciiCP(cval);
   }

   int size() {
      return super.size() + 4;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      super.write(e, out);
      out.writeShort(e.getCPIndex(this.tval));
      out.writeShort(e.getCPIndex(this.cval));
   }
}
