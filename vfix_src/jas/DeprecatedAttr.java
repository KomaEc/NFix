package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class DeprecatedAttr {
   static CP attr = new AsciiCP("Deprecated");

   void resolve(ClassEnv e) {
      e.addCPItem(attr);
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(attr));
      out.writeInt(0);
   }
}
