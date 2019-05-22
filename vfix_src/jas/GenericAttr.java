package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class GenericAttr {
   CP attr_name;
   byte[] data;

   public GenericAttr(String name, byte[] data) {
      this.attr_name = new AsciiCP(name);
      this.data = data;
   }

   public GenericAttr(CP name, byte[] data) {
      this.attr_name = name;
      this.data = data;
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.attr_name);
   }

   int size() {
      return 6 + this.data.length;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(this.attr_name));
      out.writeInt(this.data.length);
      out.write(this.data);
   }
}
