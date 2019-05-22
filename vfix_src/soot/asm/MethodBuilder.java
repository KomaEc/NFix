package soot.asm;

import java.util.Iterator;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.JSRInlinerAdapter;
import soot.ArrayType;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.tagkit.AnnotationDefaultTag;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;

class MethodBuilder extends JSRInlinerAdapter {
   private TagBuilder tb;
   private VisibilityAnnotationTag[] visibleParamAnnotations;
   private VisibilityAnnotationTag[] invisibleParamAnnotations;
   private final SootMethod method;
   private final SootClassBuilder scb;

   MethodBuilder(SootMethod method, SootClassBuilder scb, String desc, String[] ex) {
      super(327680, (MethodVisitor)null, method.getModifiers(), method.getName(), desc, (String)null, ex);
      this.method = method;
      this.scb = scb;
   }

   private TagBuilder getTagBuilder() {
      TagBuilder t = this.tb;
      if (t == null) {
         t = this.tb = new TagBuilder(this.method, this.scb);
      }

      return t;
   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
      return this.getTagBuilder().visitAnnotation(desc, visible);
   }

   public AnnotationVisitor visitAnnotationDefault() {
      return new AnnotationElemBuilder(1) {
         public void visitEnd() {
            MethodBuilder.this.method.addTag(new AnnotationDefaultTag((AnnotationElem)this.elems.get(0)));
         }
      };
   }

   public void visitAttribute(Attribute attr) {
      this.getTagBuilder().visitAttribute(attr);
   }

   public AnnotationVisitor visitParameterAnnotation(int parameter, final String desc, boolean visible) {
      final VisibilityAnnotationTag vat;
      VisibilityAnnotationTag[] vats;
      if (visible) {
         vats = this.visibleParamAnnotations;
         if (vats == null) {
            vats = new VisibilityAnnotationTag[this.method.getParameterCount()];
            this.visibleParamAnnotations = vats;
         }

         vat = vats[parameter];
         if (vat == null) {
            vat = new VisibilityAnnotationTag(0);
            vats[parameter] = vat;
         }
      } else {
         vats = this.invisibleParamAnnotations;
         if (vats == null) {
            vats = new VisibilityAnnotationTag[this.method.getParameterCount()];
            this.invisibleParamAnnotations = vats;
         }

         vat = vats[parameter];
         if (vat == null) {
            vat = new VisibilityAnnotationTag(1);
            vats[parameter] = vat;
         }
      }

      return new AnnotationElemBuilder() {
         public void visitEnd() {
            AnnotationTag annotTag = new AnnotationTag(desc, this.elems);
            vat.addAnnotation(annotTag);
         }
      };
   }

   public void visitTypeInsn(int op, String t) {
      super.visitTypeInsn(op, t);
      Type rt = AsmUtil.toJimpleRefType(t);
      if (rt instanceof ArrayType) {
         this.scb.addDep(((ArrayType)rt).baseType);
      } else {
         this.scb.addDep(rt);
      }

   }

   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
      super.visitFieldInsn(opcode, owner, name, desc);
      Iterator var5 = AsmUtil.toJimpleDesc(desc).iterator();

      while(var5.hasNext()) {
         Type t = (Type)var5.next();
         if (t instanceof RefType) {
            this.scb.addDep(t);
         }
      }

      this.scb.addDep(AsmUtil.toQualifiedName(owner));
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean isInterf) {
      super.visitMethodInsn(opcode, owner, name, desc, isInterf);
      Iterator var6 = AsmUtil.toJimpleDesc(desc).iterator();

      while(var6.hasNext()) {
         Type t = (Type)var6.next();
         this.addDeps(t);
      }

      this.scb.addDep(AsmUtil.toBaseType(owner));
   }

   public void visitLdcInsn(Object cst) {
      super.visitLdcInsn(cst);
      if (cst instanceof Handle) {
         Handle methodHandle = (Handle)cst;
         this.scb.addDep(AsmUtil.toBaseType(methodHandle.getOwner()));
      }

   }

   private void addDeps(Type t) {
      if (t instanceof RefType) {
         this.scb.addDep(t);
      } else if (t instanceof ArrayType) {
         ArrayType at = (ArrayType)t;
         this.addDeps(at.getElementType());
      }

   }

   public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
      super.visitTryCatchBlock(start, end, handler, type);
      if (type != null) {
         this.scb.addDep(AsmUtil.toQualifiedName(type));
      }

   }

   public void visitEnd() {
      super.visitEnd();
      VisibilityParameterAnnotationTag tag;
      VisibilityAnnotationTag[] var2;
      int var3;
      int var4;
      VisibilityAnnotationTag vat;
      if (this.visibleParamAnnotations != null) {
         tag = new VisibilityParameterAnnotationTag(this.visibleParamAnnotations.length, 0);
         var2 = this.visibleParamAnnotations;
         var3 = var2.length;

         for(var4 = 0; var4 < var3; ++var4) {
            vat = var2[var4];
            tag.addVisibilityAnnotation(vat);
         }

         this.method.addTag(tag);
      }

      if (this.invisibleParamAnnotations != null) {
         tag = new VisibilityParameterAnnotationTag(this.invisibleParamAnnotations.length, 1);
         var2 = this.invisibleParamAnnotations;
         var3 = var2.length;

         for(var4 = 0; var4 < var3; ++var4) {
            vat = var2[var4];
            tag.addVisibilityAnnotation(vat);
         }

         this.method.addTag(tag);
      }

      if (this.method.isConcrete()) {
         this.method.setSource(new AsmMethodSource(this.maxLocals, this.instructions, this.localVariables, this.tryCatchBlocks));
      }

   }
}
