package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class StringCP extends CP implements RuntimeConstants {
   AsciiCP val;

   public StringCP(String s) {
      this.uniq = ("String: @#$" + s).intern();
      this.val = new AsciiCP(s);
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.val);
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeByte(8);
      out.writeShort(e.getCPIndex(this.val));
   }
}
