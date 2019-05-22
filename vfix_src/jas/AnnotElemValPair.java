package jas;

import java.io.DataOutputStream;
import java.io.IOException;

public class AnnotElemValPair extends ElemValPair {
   AnnotationAttr attr;

   void resolve(ClassEnv e) {
      super.resolve(e);
      this.attr.resolve(e);
   }

   public AnnotElemValPair(String name, char kind, AnnotationAttr attr) {
      super(name, kind);
      this.attr = attr;
   }

   int size() {
      return super.size() + this.attr.size();
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      super.write(e, out);
      this.attr.write(e, out);
   }
}
