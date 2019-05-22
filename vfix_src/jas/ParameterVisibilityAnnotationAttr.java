package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ParameterVisibilityAnnotationAttr {
   AsciiCP attr;
   ArrayList list = new ArrayList();

   void resolve(ClassEnv e) {
      e.addCPItem(this.attr);
      if (this.list != null) {
         Iterator it = this.list.iterator();

         while(it.hasNext()) {
            ((VisibilityAnnotationAttr)it.next()).resolve(e);
         }
      }

   }

   public ParameterVisibilityAnnotationAttr(String kind, ArrayList vis_annotations) {
      this.attr = new AsciiCP(kind + "Annotations");
      this.list = vis_annotations;
   }

   public ParameterVisibilityAnnotationAttr() {
   }

   public void setKind(String k) {
      this.attr = new AsciiCP(k + "Annotations");
   }

   public void addAnnotation(VisibilityAnnotationAttr annot) {
      this.list.add(annot);
   }

   int size() {
      int i = 1;
      if (this.list != null) {
         for(Iterator it = this.list.iterator(); it.hasNext(); i += ((VisibilityAnnotationAttr)it.next()).size()) {
         }
      }

      return i;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(this.attr));
      out.writeInt(this.size());
      if (this.list == null) {
         out.writeByte(0);
      } else {
         out.writeByte(this.list.size());
      }

      if (this.list != null) {
         Iterator it = this.list.iterator();

         while(true) {
            VisibilityAnnotationAttr vAttr;
            do {
               if (!it.hasNext()) {
                  return;
               }

               vAttr = (VisibilityAnnotationAttr)it.next();
               if (vAttr.getList() == null) {
                  out.writeShort(0);
               } else {
                  out.writeShort(vAttr.getList().size());
               }
            } while(vAttr.getList() == null);

            Iterator ait = vAttr.getList().iterator();

            while(ait.hasNext()) {
               ((AnnotationAttr)ait.next()).write(e, out);
            }
         }
      }
   }
}
