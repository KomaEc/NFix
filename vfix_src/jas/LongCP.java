package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class LongCP extends CP implements RuntimeConstants {
   long val;

   public LongCP(long n) {
      this.uniq = ("Long: @#$" + n).intern();
      this.val = n;
   }

   void resolve(ClassEnv e) {
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException {
      out.writeByte(5);
      out.writeLong(this.val);
   }
}
