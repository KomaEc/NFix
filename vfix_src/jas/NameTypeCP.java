package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class NameTypeCP extends CP implements RuntimeConstants {
   AsciiCP name;
   AsciiCP sig;

   public NameTypeCP(String name, String sig) {
      this.uniq = ("NT : @#$%" + name + "SD#$" + sig).intern();
      this.name = new AsciiCP(name);
      this.sig = new AsciiCP(sig);
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.name);
      e.addCPItem(this.sig);
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeByte(12);
      out.writeShort(e.getCPIndex(this.name));
      out.writeShort(e.getCPIndex(this.sig));
   }
}
