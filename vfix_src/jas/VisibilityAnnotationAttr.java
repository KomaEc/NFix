package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class VisibilityAnnotationAttr {
   AsciiCP attr;
   ArrayList list = new ArrayList();
   protected String kind;

   void resolve(ClassEnv e) {
      e.addCPItem(this.attr);
      if (this.list != null) {
         Iterator it = this.list.iterator();

         while(it.hasNext()) {
            ((AnnotationAttr)it.next()).resolve(e);
         }
      }

   }

   public VisibilityAnnotationAttr(String kind, ArrayList annotations) {
      this.attr = new AsciiCP(kind + "Annotations");
      this.list = annotations;
      this.kind = kind;
   }

   public VisibilityAnnotationAttr() {
   }

   public void setKind(String k) {
      this.kind = k;
      this.attr = new AsciiCP(k + "Annotations");
   }

   public void addAnnotation(AnnotationAttr annot) {
      this.list.add(annot);
   }

   public ArrayList getList() {
      return this.list;
   }

   int size() {
      int i = 2;
      if (this.list != null) {
         for(Iterator it = this.list.iterator(); it.hasNext(); i += ((AnnotationAttr)it.next()).size()) {
         }
      }

      return i;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      out.writeShort(e.getCPIndex(this.attr));
      out.writeInt(this.size());
      if (this.list == null) {
         out.writeShort(0);
      } else {
         out.writeShort(this.list.size());
      }

      if (this.list != null) {
         Iterator it = this.list.iterator();

         while(it.hasNext()) {
            ((AnnotationAttr)it.next()).write(e, out);
         }
      }

   }

   public String getKind() {
      return this.kind;
   }
}
