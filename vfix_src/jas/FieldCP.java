package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class FieldCP extends CP implements RuntimeConstants {
   ClassCP clazz;
   NameTypeCP nt;

   public FieldCP(String clazz, String name, String sig) {
      this.uniq = (clazz + "&%$#&" + name + "*()#$" + sig).intern();
      this.clazz = new ClassCP(clazz);
      this.nt = new NameTypeCP(name, sig);
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.clazz);
      e.addCPItem(this.nt);
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeByte(9);
      out.writeShort(e.getCPIndex(this.clazz));
      out.writeShort(e.getCPIndex(this.nt));
   }
}
