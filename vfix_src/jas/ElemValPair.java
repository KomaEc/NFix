package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class ElemValPair {
   AsciiCP name;
   byte kind;
   boolean noName;

   void resolve(ClassEnv e) {
      e.addCPItem(this.name);
   }

   public ElemValPair(String name, char kind) {
      this.name = new AsciiCP(name);
      this.kind = (byte)kind;
   }

   int size() {
      return this.noName ? 1 : 3;
   }

   public void setNoName() {
      this.noName = true;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      if (!this.noName) {
         out.writeShort(e.getCPIndex(this.name));
      }

      out.writeByte(this.kind);
   }
}
