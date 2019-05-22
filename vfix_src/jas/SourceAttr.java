package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class SourceAttr {
   static CP attr = new AsciiCP("SourceFile");
   CP name;

   public SourceAttr(String name) {
      this.name = new AsciiCP(name);
   }

   public SourceAttr(CP name) {
      this.name = name;
   }

   void resolve(ClassEnv e) {
      e.addCPItem(attr);
      e.addCPItem(this.name);
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(attr));
      out.writeInt(2);
      out.writeShort(e.getCPIndex(this.name));
   }
}
