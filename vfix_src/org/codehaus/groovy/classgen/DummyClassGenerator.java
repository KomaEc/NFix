package org.codehaus.groovy.classgen;

import groovy.lang.GroovyRuntimeException;
import groovyjarjarasm.asm.ClassVisitor;
import groovyjarjarasm.asm.MethodVisitor;
import java.util.Iterator;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;

public class DummyClassGenerator extends ClassGenerator {
   private ClassVisitor cv;
   private MethodVisitor mv;
   private GeneratorContext context;
   private String sourceFile;
   private ClassNode classNode;
   private String internalClassName;
   private String internalBaseClassName;

   public DummyClassGenerator(GeneratorContext context, ClassVisitor classVisitor, ClassLoader classLoader, String sourceFile) {
      super(classLoader);
      this.context = context;
      this.cv = classVisitor;
      this.sourceFile = sourceFile;
   }

   public void visitClass(ClassNode classNode) {
      try {
         this.classNode = classNode;
         this.internalClassName = BytecodeHelper.getClassInternalName(classNode);
         this.internalBaseClassName = BytecodeHelper.getClassInternalName(classNode.getSuperClass());
         this.cv.visit(47, classNode.getModifiers(), this.internalClassName, (String)null, this.internalBaseClassName, BytecodeHelper.getClassInternalNames(classNode.getInterfaces()));
         classNode.visitContents(this);

         ClassNode innerClass;
         String innerClassInternalName;
         String outerClassName;
         for(Iterator iter = this.innerClasses.iterator(); iter.hasNext(); this.cv.visitInnerClass(innerClassInternalName, outerClassName, innerClass.getName(), innerClass.getModifiers())) {
            innerClass = (ClassNode)iter.next();
            innerClassInternalName = BytecodeHelper.getClassInternalName(innerClass);
            outerClassName = this.internalClassName;
            MethodNode enclosingMethod = innerClass.getEnclosingMethod();
            if (enclosingMethod != null) {
               outerClassName = null;
            }
         }

         this.cv.visitEnd();
      } catch (GroovyRuntimeException var8) {
         var8.setModule(classNode.getModule());
         throw var8;
      }
   }

   public void visitConstructor(ConstructorNode node) {
      this.visitParameters(node, node.getParameters());
      String methodType = BytecodeHelper.getMethodDescriptor(ClassHelper.VOID_TYPE, node.getParameters());
      this.mv = this.cv.visitMethod(node.getModifiers(), "<init>", methodType, (String)null, (String[])null);
      this.mv.visitTypeInsn(187, "java/lang/RuntimeException");
      this.mv.visitInsn(89);
      this.mv.visitLdcInsn("not intended for execution");
      this.mv.visitMethodInsn(183, "java/lang/RuntimeException", "<init>", "(Ljava/lang/String;)V");
      this.mv.visitInsn(191);
      this.mv.visitMaxs(0, 0);
   }

   public void visitMethod(MethodNode node) {
      this.visitParameters(node, node.getParameters());
      String methodType = BytecodeHelper.getMethodDescriptor(node.getReturnType(), node.getParameters());
      this.mv = this.cv.visitMethod(node.getModifiers(), node.getName(), methodType, (String)null, (String[])null);
      this.mv.visitTypeInsn(187, "java/lang/RuntimeException");
      this.mv.visitInsn(89);
      this.mv.visitLdcInsn("not intended for execution");
      this.mv.visitMethodInsn(183, "java/lang/RuntimeException", "<init>", "(Ljava/lang/String;)V");
      this.mv.visitInsn(191);
      this.mv.visitMaxs(0, 0);
   }

   public void visitField(FieldNode fieldNode) {
      this.cv.visitField(fieldNode.getModifiers(), fieldNode.getName(), BytecodeHelper.getTypeDescription(fieldNode.getType()), (String)null, (Object)null);
   }

   public void visitProperty(PropertyNode statement) {
   }

   protected CompileUnit getCompileUnit() {
      CompileUnit answer = this.classNode.getCompileUnit();
      if (answer == null) {
         answer = this.context.getCompileUnit();
      }

      return answer;
   }

   protected void visitParameters(ASTNode node, Parameter[] parameters) {
      int i = 0;

      for(int size = parameters.length; i < size; ++i) {
         this.visitParameter(node, parameters[i]);
      }

   }

   protected void visitParameter(ASTNode node, Parameter parameter) {
   }

   public void visitAnnotations(AnnotatedNode node) {
   }
}
