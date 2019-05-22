package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class ClassCP extends CP implements RuntimeConstants {
   AsciiCP name;

   public ClassCP(String name) {
      this.uniq = ("CLASS: #$%^#$" + name).intern();
      this.name = new AsciiCP(name);
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.name);
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeByte(7);
      out.writeShort(e.getCPIndex(this.name));
   }
}
