package soot.jimple.toolkits.annotation.j5anno;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Host;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;

public class AnnotationGenerator {
   public AnnotationGenerator(Singletons.Global g) {
   }

   public static AnnotationGenerator v() {
      return G.v().soot_jimple_toolkits_annotation_j5anno_AnnotationGenerator();
   }

   public void annotate(Host h, Class<? extends Annotation> klass, AnnotationElem... elems) {
      this.annotate(h, klass, Arrays.asList(elems));
   }

   public void annotate(Host h, Class<? extends Annotation> klass, List<AnnotationElem> elems) {
      Target t = (Target)klass.getAnnotation(Target.class);
      Collection<ElementType> elementTypes = Arrays.asList(t.value());
      String ERR = "Annotation class " + klass + " not applicable to host of type " + h.getClass() + ".";
      if (h instanceof SootClass) {
         if (!elementTypes.contains(ElementType.TYPE)) {
            throw new RuntimeException(ERR);
         }
      } else if (h instanceof SootMethod) {
         if (!elementTypes.contains(ElementType.METHOD)) {
            throw new RuntimeException(ERR);
         }
      } else {
         if (!(h instanceof SootField)) {
            throw new RuntimeException("Tried to attach annotation to host of type " + h.getClass() + ".");
         }

         if (!elementTypes.contains(ElementType.FIELD)) {
            throw new RuntimeException(ERR);
         }
      }

      Retention r = (Retention)klass.getAnnotation(Retention.class);
      int retPolicy = 1;
      if (r != null) {
         switch(r.value()) {
         case CLASS:
            retPolicy = 1;
            break;
         case RUNTIME:
            retPolicy = 0;
            break;
         default:
            throw new RuntimeException("Unexpected retention policy: " + retPolicy);
         }
      }

      this.annotate(h, klass.getCanonicalName(), retPolicy, elems);
   }

   public void annotate(Host h, String annotationName, int visibility, List<AnnotationElem> elems) {
      annotationName = annotationName.replace('.', '/');
      if (!annotationName.endsWith(";")) {
         annotationName = "L" + annotationName + ';';
      }

      VisibilityAnnotationTag tagToAdd = this.findOrAdd(h, visibility);
      AnnotationTag at = new AnnotationTag(annotationName, elems);
      tagToAdd.addAnnotation(at);
   }

   private VisibilityAnnotationTag findOrAdd(Host h, int visibility) {
      ArrayList<VisibilityAnnotationTag> va_tags = new ArrayList();
      Iterator var4 = h.getTags().iterator();

      while(var4.hasNext()) {
         Tag t = (Tag)var4.next();
         if (t instanceof VisibilityAnnotationTag) {
            VisibilityAnnotationTag vat = (VisibilityAnnotationTag)t;
            if (vat.getVisibility() == visibility) {
               va_tags.add(vat);
            }
         }
      }

      if (va_tags.isEmpty()) {
         VisibilityAnnotationTag vat = new VisibilityAnnotationTag(visibility);
         h.addTag(vat);
         return vat;
      } else {
         return (VisibilityAnnotationTag)va_tags.get(0);
      }
   }
}
