package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class FloatCP extends CP implements RuntimeConstants {
   float val;

   public FloatCP(float n) {
      this.uniq = ("Float: @#$" + n).intern();
      this.val = n;
   }

   void resolve(ClassEnv e) {
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException {
      out.writeByte(4);
      out.writeFloat(this.val);
   }
}
