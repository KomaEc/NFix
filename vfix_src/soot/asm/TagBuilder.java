package soot.asm;

import java.lang.reflect.Field;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import soot.tagkit.AnnotationTag;
import soot.tagkit.GenericAttribute;
import soot.tagkit.Host;
import soot.tagkit.VisibilityAnnotationTag;

final class TagBuilder {
   private VisibilityAnnotationTag invisibleTag;
   private VisibilityAnnotationTag visibleTag;
   private final Host host;
   private final SootClassBuilder scb;

   TagBuilder(Host host, SootClassBuilder scb) {
      this.host = host;
      this.scb = scb;
   }

   public AnnotationVisitor visitAnnotation(final String desc, boolean visible) {
      final VisibilityAnnotationTag tag;
      if (visible) {
         tag = this.visibleTag;
         if (tag == null) {
            this.visibleTag = tag = new VisibilityAnnotationTag(0);
            this.host.addTag(tag);
         }
      } else {
         tag = this.invisibleTag;
         if (tag == null) {
            this.invisibleTag = tag = new VisibilityAnnotationTag(1);
            this.host.addTag(tag);
         }
      }

      this.scb.addDep(AsmUtil.toQualifiedName(desc.substring(1, desc.length() - 1)));
      return new AnnotationElemBuilder() {
         public void visitEnd() {
            AnnotationTag annotTag = new AnnotationTag(desc, this.elems);
            tag.addAnnotation(annotTag);
         }
      };
   }

   public void visitAttribute(Attribute attr) {
      byte[] value = null;

      try {
         Field fld = Attribute.class.getDeclaredField("value");
         fld.setAccessible(true);
         value = (byte[])((byte[])fld.get(attr));
      } catch (Exception var4) {
      }

      this.host.addTag(new GenericAttribute(attr.type, value));
   }
}
