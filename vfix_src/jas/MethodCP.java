package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class MethodCP extends CP implements RuntimeConstants {
   ClassCP clazz;
   NameTypeCP nt;

   public MethodCP(String cname, String varname, String sig) {
      this.uniq = cname + "&%$91&" + varname + "*(012$" + sig;
      this.clazz = new ClassCP(cname);
      this.nt = new NameTypeCP(varname, sig);
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.clazz);
      e.addCPItem(this.nt);
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeByte(10);
      out.writeShort(e.getCPIndex(this.clazz));
      out.writeShort(e.getCPIndex(this.nt));
   }
}
