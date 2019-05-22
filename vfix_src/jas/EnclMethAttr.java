package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class EnclMethAttr {
   static CP attr = new AsciiCP("EnclosingMethod");
   ClassCP cls;
   NameTypeCP meth;

   void resolve(ClassEnv e) {
      e.addCPItem(attr);
      e.addCPItem(this.cls);
      if (null != this.meth) {
         e.addCPItem(this.meth);
      }

   }

   public EnclMethAttr(String a, String b, String c) {
      this.cls = new ClassCP(a);
      if (!b.isEmpty() && !c.isEmpty()) {
         this.meth = new NameTypeCP(b, c);
      }

   }

   int size() {
      return 4;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(attr));
      out.writeInt(4);
      out.writeShort(e.getCPIndex(this.cls));
      out.writeShort(null == this.meth ? 0 : e.getCPIndex(this.meth));
   }
}
