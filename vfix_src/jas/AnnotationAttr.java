package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class AnnotationAttr {
   AsciiCP type;
   ArrayList list = new ArrayList();

   void resolve(ClassEnv e) {
      e.addCPItem(this.type);
      if (this.list != null) {
         Iterator it = this.list.iterator();

         while(it.hasNext()) {
            ((ElemValPair)it.next()).resolve(e);
         }
      }

   }

   public AnnotationAttr(String type, ArrayList elems) {
      this.type = new AsciiCP(type);
      this.list = elems;
   }

   public AnnotationAttr() {
   }

   public void setType(String type) {
      this.type = new AsciiCP(type);
   }

   public void addElemValPair(ElemValPair pair) {
      this.list.add(pair);
   }

   int size() {
      int i = 4;
      if (this.list != null) {
         for(Iterator it = this.list.iterator(); it.hasNext(); i += ((ElemValPair)it.next()).size()) {
         }
      }

      return i;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(this.type));
      if (this.list == null) {
         out.writeShort(0);
      } else {
         out.writeShort(this.list.size());
      }

      if (this.list != null) {
         Iterator it = this.list.iterator();

         while(it.hasNext()) {
            ((ElemValPair)it.next()).write(e, out);
         }
      }

   }
}
