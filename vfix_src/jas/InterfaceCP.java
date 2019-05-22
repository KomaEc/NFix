package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class InterfaceCP extends CP implements RuntimeConstants {
   ClassCP clazz;
   NameTypeCP nt;

   public InterfaceCP(String cname, String varname, String sig) {
      this.uniq = (cname + "&%$#&" + varname + "*()#$" + sig).intern();
      this.clazz = new ClassCP(cname);
      this.nt = new NameTypeCP(varname, sig);
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.clazz);
      e.addCPItem(this.nt);
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeByte(11);
      out.writeShort(e.getCPIndex(this.clazz));
      out.writeShort(e.getCPIndex(this.nt));
   }
}
