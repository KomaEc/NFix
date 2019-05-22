package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class DoubleCP extends CP implements RuntimeConstants {
   double val;

   public DoubleCP(double n) {
      this.uniq = ("Double: @#$" + n).intern();
      this.val = n;
   }

   void resolve(ClassEnv e) {
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException {
      out.writeByte(6);
      out.writeDouble(this.val);
   }
}
