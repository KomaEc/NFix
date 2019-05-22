package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class InnerClassAttr {
   static CP attr = new AsciiCP("InnerClasses");
   short attr_length = 0;
   short num = 0;
   ArrayList list = new ArrayList();

   void resolve(ClassEnv e) {
      e.addCPItem(attr);
      if (this.list != null) {
         Iterator it = this.list.iterator();

         while(it.hasNext()) {
            ((InnerClassSpecAttr)it.next()).resolve(e);
         }
      }

   }

   int size() {
      return 8 + 8 * this.list.size();
   }

   public void addInnerClassSpec(InnerClassSpecAttr attr) {
      this.list.add(attr);
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(attr));
      out.writeInt(2 + 8 * this.list.size());
      out.writeShort(this.list.size());
      Iterator it = this.list.iterator();

      while(it.hasNext()) {
         ((InnerClassSpecAttr)it.next()).write(e, out);
      }

   }
}
