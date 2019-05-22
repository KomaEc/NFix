package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class AnnotationDefaultAttr {
   static CP attr = new AsciiCP("AnnotationDefault");
   ElemValPair elem;

   void resolve(ClassEnv e) {
      e.addCPItem(attr);
      this.elem.resolve(e);
   }

   public AnnotationDefaultAttr(ElemValPair s) {
      this.elem = s;
   }

   int size() {
      return this.elem.size();
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(attr));
      out.writeInt(this.size());
      this.elem.write(e, out);
   }
}
