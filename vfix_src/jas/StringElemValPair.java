package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class StringElemValPair extends ElemValPair {
   AsciiCP val;

   void resolve(ClassEnv e) {
      super.resolve(e);
      e.addCPItem(this.val);
   }

   public StringElemValPair(String name, char kind, String val) {
      super(name, kind);
      this.val = new AsciiCP(val);
   }

   int size() {
      return super.size() + 2;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      super.write(e, out);
      out.writeShort(e.getCPIndex(this.val));
   }
}
