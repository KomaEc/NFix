package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class InnerClassSpecAttr {
   String inner_class_name;
   String outer_class_name;
   String inner_name;
   short access;

   void resolve(ClassEnv e) {
      e.addCPItem(new ClassCP(this.inner_class_name));
      if (!this.outer_class_name.equals("null")) {
         e.addCPItem(new ClassCP(this.outer_class_name));
      }

      if (!this.inner_name.equals("null")) {
         e.addCPItem(new AsciiCP(this.inner_name));
      }

   }

   public InnerClassSpecAttr(String a, String b, String c, short d) {
      this.inner_class_name = a;
      this.outer_class_name = b;
      this.inner_name = c;
      this.access = d;
   }

   int size() {
      return 8;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(new ClassCP(this.inner_class_name)));
      if (this.outer_class_name.equals("null")) {
         out.writeShort(0);
      } else {
         out.writeShort(e.getCPIndex(new ClassCP(this.outer_class_name)));
      }

      if (this.inner_name.equals("null")) {
         out.writeShort(0);
      } else {
         out.writeShort(e.getCPIndex(new AsciiCP(this.inner_name)));
      }

      out.writeShort(this.access);
   }
}
