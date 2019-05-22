package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class IntegerCP extends CP implements RuntimeConstants {
   int val;

   public IntegerCP(int n) {
      this.uniq = ("Integer: @#$" + n).intern();
      this.val = n;
   }

   void resolve(ClassEnv e) {
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException {
      out.writeByte(3);
      out.writeInt(this.val);
   }
}
