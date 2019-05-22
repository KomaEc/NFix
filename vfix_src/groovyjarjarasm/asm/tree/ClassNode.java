package groovyjarjarasm.asm.tree;

import groovyjarjarasm.asm.Attribute;
import groovyjarjarasm.asm.ClassVisitor;
import groovyjarjarasm.asm.FieldVisitor;
import groovyjarjarasm.asm.MethodVisitor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassNode extends MemberNode implements ClassVisitor {
   public int version;
   public int access;
   public String name;
   public String signature;
   public String superName;
   public List interfaces = new ArrayList();
   public String sourceFile;
   public String sourceDebug;
   public String outerClass;
   public String outerMethod;
   public String outerMethodDesc;
   public List innerClasses = new ArrayList();
   public List fields = new ArrayList();
   public List methods = new ArrayList();

   public void visit(int var1, int var2, String var3, String var4, String var5, String[] var6) {
      this.version = var1;
      this.access = var2;
      this.name = var3;
      this.signature = var4;
      this.superName = var5;
      if (var6 != null) {
         this.interfaces.addAll(Arrays.asList(var6));
      }

   }

   public void visitSource(String var1, String var2) {
      this.sourceFile = var1;
      this.sourceDebug = var2;
   }

   public void visitOuterClass(String var1, String var2, String var3) {
      this.outerClass = var1;
      this.outerMethod = var2;
      this.outerMethodDesc = var3;
   }

   public void visitInnerClass(String var1, String var2, String var3, int var4) {
      InnerClassNode var5 = new InnerClassNode(var1, var2, var3, var4);
      this.innerClasses.add(var5);
   }

   public FieldVisitor visitField(int var1, String var2, String var3, String var4, Object var5) {
      FieldNode var6 = new FieldNode(var1, var2, var3, var4, var5);
      this.fields.add(var6);
      return var6;
   }

   public MethodVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5) {
      MethodNode var6 = new MethodNode(var1, var2, var3, var4, var5);
      this.methods.add(var6);
      return var6;
   }

   public void accept(ClassVisitor var1) {
      String[] var2 = new String[this.interfaces.size()];
      this.interfaces.toArray(var2);
      var1.visit(this.version, this.access, this.name, this.signature, this.superName, var2);
      if (this.sourceFile != null || this.sourceDebug != null) {
         var1.visitSource(this.sourceFile, this.sourceDebug);
      }

      if (this.outerClass != null) {
         var1.visitOuterClass(this.outerClass, this.outerMethod, this.outerMethodDesc);
      }

      int var3 = this.visibleAnnotations == null ? 0 : this.visibleAnnotations.size();

      int var4;
      AnnotationNode var5;
      for(var4 = 0; var4 < var3; ++var4) {
         var5 = (AnnotationNode)this.visibleAnnotations.get(var4);
         var5.accept(var1.visitAnnotation(var5.desc, true));
      }

      var3 = this.invisibleAnnotations == null ? 0 : this.invisibleAnnotations.size();

      for(var4 = 0; var4 < var3; ++var4) {
         var5 = (AnnotationNode)this.invisibleAnnotations.get(var4);
         var5.accept(var1.visitAnnotation(var5.desc, false));
      }

      var3 = this.attrs == null ? 0 : this.attrs.size();

      for(var4 = 0; var4 < var3; ++var4) {
         var1.visitAttribute((Attribute)this.attrs.get(var4));
      }

      for(var4 = 0; var4 < this.innerClasses.size(); ++var4) {
         ((InnerClassNode)this.innerClasses.get(var4)).accept(var1);
      }

      for(var4 = 0; var4 < this.fields.size(); ++var4) {
         ((FieldNode)this.fields.get(var4)).accept(var1);
      }

      for(var4 = 0; var4 < this.methods.size(); ++var4) {
         ((MethodNode)this.methods.get(var4)).accept(var1);
      }

      var1.visitEnd();
   }
}
