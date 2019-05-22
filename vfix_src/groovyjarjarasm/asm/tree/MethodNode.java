package groovyjarjarasm.asm.tree;

import groovyjarjarasm.asm.AnnotationVisitor;
import groovyjarjarasm.asm.Attribute;
import groovyjarjarasm.asm.ClassVisitor;
import groovyjarjarasm.asm.Label;
import groovyjarjarasm.asm.MethodVisitor;
import groovyjarjarasm.asm.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodNode extends MemberNode implements MethodVisitor {
   public int access;
   public String name;
   public String desc;
   public String signature;
   public List exceptions;
   public Object annotationDefault;
   public List[] visibleParameterAnnotations;
   public List[] invisibleParameterAnnotations;
   public InsnList instructions;
   public List tryCatchBlocks;
   public int maxStack;
   public int maxLocals;
   public List localVariables;

   public MethodNode() {
      this.instructions = new InsnList();
   }

   public MethodNode(int var1, String var2, String var3, String var4, String[] var5) {
      this();
      this.access = var1;
      this.name = var2;
      this.desc = var3;
      this.signature = var4;
      this.exceptions = new ArrayList(var5 == null ? 0 : var5.length);
      boolean var6 = (var1 & 1024) != 0;
      if (!var6) {
         this.localVariables = new ArrayList(5);
      }

      this.tryCatchBlocks = new ArrayList();
      if (var5 != null) {
         this.exceptions.addAll(Arrays.asList(var5));
      }

   }

   public AnnotationVisitor visitAnnotationDefault() {
      return new AnnotationNode(new MethodNode$1(this, 0));
   }

   public AnnotationVisitor visitParameterAnnotation(int var1, String var2, boolean var3) {
      AnnotationNode var4 = new AnnotationNode(var2);
      int var5;
      if (var3) {
         if (this.visibleParameterAnnotations == null) {
            var5 = Type.getArgumentTypes(this.desc).length;
            this.visibleParameterAnnotations = new List[var5];
         }

         if (this.visibleParameterAnnotations[var1] == null) {
            this.visibleParameterAnnotations[var1] = new ArrayList(1);
         }

         this.visibleParameterAnnotations[var1].add(var4);
      } else {
         if (this.invisibleParameterAnnotations == null) {
            var5 = Type.getArgumentTypes(this.desc).length;
            this.invisibleParameterAnnotations = new List[var5];
         }

         if (this.invisibleParameterAnnotations[var1] == null) {
            this.invisibleParameterAnnotations[var1] = new ArrayList(1);
         }

         this.invisibleParameterAnnotations[var1].add(var4);
      }

      return var4;
   }

   public void visitCode() {
   }

   public void visitFrame(int var1, int var2, Object[] var3, int var4, Object[] var5) {
      this.instructions.add((AbstractInsnNode)(new FrameNode(var1, var2, var3 == null ? null : this.getLabelNodes(var3), var4, var5 == null ? null : this.getLabelNodes(var5))));
   }

   public void visitInsn(int var1) {
      this.instructions.add((AbstractInsnNode)(new InsnNode(var1)));
   }

   public void visitIntInsn(int var1, int var2) {
      this.instructions.add((AbstractInsnNode)(new IntInsnNode(var1, var2)));
   }

   public void visitVarInsn(int var1, int var2) {
      this.instructions.add((AbstractInsnNode)(new VarInsnNode(var1, var2)));
   }

   public void visitTypeInsn(int var1, String var2) {
      this.instructions.add((AbstractInsnNode)(new TypeInsnNode(var1, var2)));
   }

   public void visitFieldInsn(int var1, String var2, String var3, String var4) {
      this.instructions.add((AbstractInsnNode)(new FieldInsnNode(var1, var2, var3, var4)));
   }

   public void visitMethodInsn(int var1, String var2, String var3, String var4) {
      this.instructions.add((AbstractInsnNode)(new MethodInsnNode(var1, var2, var3, var4)));
   }

   public void visitJumpInsn(int var1, Label var2) {
      this.instructions.add((AbstractInsnNode)(new JumpInsnNode(var1, this.getLabelNode(var2))));
   }

   public void visitLabel(Label var1) {
      this.instructions.add((AbstractInsnNode)this.getLabelNode(var1));
   }

   public void visitLdcInsn(Object var1) {
      this.instructions.add((AbstractInsnNode)(new LdcInsnNode(var1)));
   }

   public void visitIincInsn(int var1, int var2) {
      this.instructions.add((AbstractInsnNode)(new IincInsnNode(var1, var2)));
   }

   public void visitTableSwitchInsn(int var1, int var2, Label var3, Label[] var4) {
      this.instructions.add((AbstractInsnNode)(new TableSwitchInsnNode(var1, var2, this.getLabelNode(var3), this.getLabelNodes(var4))));
   }

   public void visitLookupSwitchInsn(Label var1, int[] var2, Label[] var3) {
      this.instructions.add((AbstractInsnNode)(new LookupSwitchInsnNode(this.getLabelNode(var1), var2, this.getLabelNodes(var3))));
   }

   public void visitMultiANewArrayInsn(String var1, int var2) {
      this.instructions.add((AbstractInsnNode)(new MultiANewArrayInsnNode(var1, var2)));
   }

   public void visitTryCatchBlock(Label var1, Label var2, Label var3, String var4) {
      this.tryCatchBlocks.add(new TryCatchBlockNode(this.getLabelNode(var1), this.getLabelNode(var2), this.getLabelNode(var3), var4));
   }

   public void visitLocalVariable(String var1, String var2, String var3, Label var4, Label var5, int var6) {
      this.localVariables.add(new LocalVariableNode(var1, var2, var3, this.getLabelNode(var4), this.getLabelNode(var5), var6));
   }

   public void visitLineNumber(int var1, Label var2) {
      this.instructions.add((AbstractInsnNode)(new LineNumberNode(var1, this.getLabelNode(var2))));
   }

   public void visitMaxs(int var1, int var2) {
      this.maxStack = var1;
      this.maxLocals = var2;
   }

   protected LabelNode getLabelNode(Label var1) {
      if (!(var1.info instanceof LabelNode)) {
         var1.info = new LabelNode(var1);
      }

      return (LabelNode)var1.info;
   }

   private LabelNode[] getLabelNodes(Label[] var1) {
      LabelNode[] var2 = new LabelNode[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = this.getLabelNode(var1[var3]);
      }

      return var2;
   }

   private Object[] getLabelNodes(Object[] var1) {
      Object[] var2 = new Object[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         Object var4 = var1[var3];
         if (var4 instanceof Label) {
            var4 = this.getLabelNode((Label)var4);
         }

         var2[var3] = var4;
      }

      return var2;
   }

   public void accept(ClassVisitor var1) {
      String[] var2 = new String[this.exceptions.size()];
      this.exceptions.toArray(var2);
      MethodVisitor var3 = var1.visitMethod(this.access, this.name, this.desc, this.signature, var2);
      if (var3 != null) {
         this.accept(var3);
      }

   }

   public void accept(MethodVisitor var1) {
      if (this.annotationDefault != null) {
         AnnotationVisitor var2 = var1.visitAnnotationDefault();
         AnnotationNode.accept(var2, (String)null, this.annotationDefault);
         if (var2 != null) {
            var2.visitEnd();
         }
      }

      int var3 = this.visibleAnnotations == null ? 0 : this.visibleAnnotations.size();

      int var4;
      AnnotationNode var7;
      for(var4 = 0; var4 < var3; ++var4) {
         var7 = (AnnotationNode)this.visibleAnnotations.get(var4);
         var7.accept(var1.visitAnnotation(var7.desc, true));
      }

      var3 = this.invisibleAnnotations == null ? 0 : this.invisibleAnnotations.size();

      for(var4 = 0; var4 < var3; ++var4) {
         var7 = (AnnotationNode)this.invisibleAnnotations.get(var4);
         var7.accept(var1.visitAnnotation(var7.desc, false));
      }

      var3 = this.visibleParameterAnnotations == null ? 0 : this.visibleParameterAnnotations.length;

      int var5;
      AnnotationNode var6;
      List var8;
      for(var4 = 0; var4 < var3; ++var4) {
         var8 = this.visibleParameterAnnotations[var4];
         if (var8 != null) {
            for(var5 = 0; var5 < var8.size(); ++var5) {
               var6 = (AnnotationNode)var8.get(var5);
               var6.accept(var1.visitParameterAnnotation(var4, var6.desc, true));
            }
         }
      }

      var3 = this.invisibleParameterAnnotations == null ? 0 : this.invisibleParameterAnnotations.length;

      for(var4 = 0; var4 < var3; ++var4) {
         var8 = this.invisibleParameterAnnotations[var4];
         if (var8 != null) {
            for(var5 = 0; var5 < var8.size(); ++var5) {
               var6 = (AnnotationNode)var8.get(var5);
               var6.accept(var1.visitParameterAnnotation(var4, var6.desc, false));
            }
         }
      }

      var3 = this.attrs == null ? 0 : this.attrs.size();

      for(var4 = 0; var4 < var3; ++var4) {
         var1.visitAttribute((Attribute)this.attrs.get(var4));
      }

      if (this.instructions.size() > 0) {
         var1.visitCode();
         var3 = this.tryCatchBlocks == null ? 0 : this.tryCatchBlocks.size();

         for(var4 = 0; var4 < var3; ++var4) {
            ((TryCatchBlockNode)this.tryCatchBlocks.get(var4)).accept(var1);
         }

         this.instructions.accept(var1);
         var3 = this.localVariables == null ? 0 : this.localVariables.size();

         for(var4 = 0; var4 < var3; ++var4) {
            ((LocalVariableNode)this.localVariables.get(var4)).accept(var1);
         }

         var1.visitMaxs(this.maxStack, this.maxLocals);
      }

      var1.visitEnd();
   }
}
