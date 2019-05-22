package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class AsciiCP extends CP implements RuntimeConstants {
   public AsciiCP(String s) {
      this.uniq = s.intern();
   }

   void resolve(ClassEnv e) {
   }

   public String toString() {
      return "AsciiCP: " + this.uniq;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException {
      out.writeByte(1);
      out.writeUTF(this.uniq);
   }
}
