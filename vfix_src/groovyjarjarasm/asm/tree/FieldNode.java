package groovyjarjarasm.asm.tree;

import groovyjarjarasm.asm.Attribute;
import groovyjarjarasm.asm.ClassVisitor;
import groovyjarjarasm.asm.FieldVisitor;

public class FieldNode extends MemberNode implements FieldVisitor {
   public int access;
   public String name;
   public String desc;
   public String signature;
   public Object value;

   public FieldNode(int var1, String var2, String var3, String var4, Object var5) {
      this.access = var1;
      this.name = var2;
      this.desc = var3;
      this.signature = var4;
      this.value = var5;
   }

   public void accept(ClassVisitor var1) {
      FieldVisitor var2 = var1.visitField(this.access, this.name, this.desc, this.signature, this.value);
      if (var2 != null) {
         int var3 = this.visibleAnnotations == null ? 0 : this.visibleAnnotations.size();

         int var4;
         AnnotationNode var5;
         for(var4 = 0; var4 < var3; ++var4) {
            var5 = (AnnotationNode)this.visibleAnnotations.get(var4);
            var5.accept(var2.visitAnnotation(var5.desc, true));
         }

         var3 = this.invisibleAnnotations == null ? 0 : this.invisibleAnnotations.size();

         for(var4 = 0; var4 < var3; ++var4) {
            var5 = (AnnotationNode)this.invisibleAnnotations.get(var4);
            var5.accept(var2.visitAnnotation(var5.desc, false));
         }

         var3 = this.attrs == null ? 0 : this.attrs.size();

         for(var4 = 0; var4 < var3; ++var4) {
            var2.visitAttribute((Attribute)this.attrs.get(var4));
         }

         var2.visitEnd();
      }
   }
}
