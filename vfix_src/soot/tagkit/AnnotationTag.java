package soot.tagkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AnnotationTag implements Tag {
   private String type;
   private List<AnnotationElem> elems;

   public AnnotationTag(String type) {
      this.type = type;
      this.elems = null;
   }

   public AnnotationTag(String type, Collection<AnnotationElem> elements) {
      this.type = type;
      if (elements != null && !elements.isEmpty()) {
         if (elements instanceof List) {
            this.elems = (List)elements;
         } else {
            this.elems = new ArrayList(elements);
         }
      } else {
         this.elems = null;
      }

   }

   /** @deprecated */
   @Deprecated
   public AnnotationTag(String type, int numElem) {
      this.type = type;
      this.elems = new ArrayList(numElem);
   }

   public String toString() {
      if (this.elems == null) {
         return "Annotation type: " + this.type + " without elements";
      } else {
         StringBuffer sb = new StringBuffer("Annotation: type: " + this.type + " num elems: " + this.elems.size() + " elems: ");
         Iterator it = this.elems.iterator();

         while(it.hasNext()) {
            sb.append("\n");
            sb.append(it.next());
         }

         sb.append("\n");
         return sb.toString();
      }
   }

   public String getName() {
      return "AnnotationTag";
   }

   public String getInfo() {
      return "Annotation";
   }

   public String getType() {
      return this.type;
   }

   public byte[] getValue() {
      throw new RuntimeException("AnnotationTag has no value for bytecode");
   }

   public void addElem(AnnotationElem elem) {
      if (this.elems == null) {
         this.elems = new ArrayList();
      }

      this.elems.add(elem);
   }

   public void setElems(List<AnnotationElem> list) {
      this.elems = list;
   }

   public Collection<AnnotationElem> getElems() {
      return (Collection)(this.elems == null ? Collections.emptyList() : Collections.unmodifiableCollection(this.elems));
   }
}
