package soot.asm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootResolver;
import soot.Type;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.EnclosingMethodTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.InnerClassTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;

class SootClassBuilder extends ClassVisitor {
   private TagBuilder tb;
   private final SootClass klass;
   final Set<Type> deps;

   SootClassBuilder(SootClass klass) {
      super(327680);
      this.klass = klass;
      this.deps = new HashSet();
   }

   private TagBuilder getTagBuilder() {
      TagBuilder t = this.tb;
      if (t == null) {
         t = this.tb = new TagBuilder(this.klass, this);
      }

      return t;
   }

   void addDep(String s) {
      this.addDep((Type)RefType.v(AsmUtil.baseTypeName(s)));
   }

   void addDep(Type s) {
      this.deps.add(s);
   }

   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
      name = AsmUtil.toQualifiedName(name);
      if (!name.equals(this.klass.getName())) {
         throw new RuntimeException("Class names not equal! " + name + " != " + this.klass.getName());
      } else {
         this.klass.setModifiers(access & -33);
         if (superName != null) {
            superName = AsmUtil.toQualifiedName(superName);
            this.addDep((Type)RefType.v(superName));
            this.klass.setSuperclass(SootResolver.v().makeClassRef(superName));
         }

         String[] var7 = interfaces;
         int var8 = interfaces.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String intrf = var7[var9];
            intrf = AsmUtil.toQualifiedName(intrf);
            this.addDep((Type)RefType.v(intrf));
            SootClass interfaceClass = SootResolver.v().makeClassRef(intrf);
            interfaceClass.setModifiers(interfaceClass.getModifiers() | 512);
            this.klass.addInterface(interfaceClass);
         }

         if (signature != null) {
            this.klass.addTag(new SignatureTag(signature));
         }

      }
   }

   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
      Type type = AsmUtil.toJimpleType(desc);
      this.addDep(type);
      SootField field = Scene.v().makeSootField(name, type, access);
      Object tag;
      if (value instanceof Integer) {
         tag = new IntegerConstantValueTag((Integer)value);
      } else if (value instanceof Float) {
         tag = new FloatConstantValueTag((Float)value);
      } else if (value instanceof Long) {
         tag = new LongConstantValueTag((Long)value);
      } else if (value instanceof Double) {
         tag = new DoubleConstantValueTag((Double)value);
      } else if (value instanceof String) {
         tag = new StringConstantValueTag(value.toString());
      } else {
         tag = null;
      }

      if (tag != null) {
         field.addTag((Tag)tag);
      }

      if (signature != null) {
         field.addTag(new SignatureTag(signature));
      }

      field = this.klass.getOrAddField(field);
      return new FieldBuilder(field, this);
   }

   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      Object thrownExceptions;
      if (exceptions != null && exceptions.length != 0) {
         int len = exceptions.length;
         thrownExceptions = new ArrayList(len);

         for(int i = 0; i != len; ++i) {
            String ex = AsmUtil.toQualifiedName(exceptions[i]);
            this.addDep((Type)RefType.v(ex));
            ((List)thrownExceptions).add(SootResolver.v().makeClassRef(ex));
         }
      } else {
         thrownExceptions = Collections.emptyList();
      }

      List<Type> sigTypes = AsmUtil.toJimpleDesc(desc);
      Iterator var11 = sigTypes.iterator();

      while(var11.hasNext()) {
         Type type = (Type)var11.next();
         this.addDep(type);
      }

      SootMethod method = Scene.v().makeSootMethod(name, sigTypes, (Type)sigTypes.remove(sigTypes.size() - 1), access, (List)thrownExceptions);
      if (signature != null) {
         method.addTag(new SignatureTag(signature));
      }

      method = this.klass.getOrAddMethod(method);
      return new MethodBuilder(method, this, desc, exceptions);
   }

   public void visitSource(String source, String debug) {
      if (source != null) {
         this.klass.addTag(new SourceFileTag(source));
      }

   }

   public void visitInnerClass(String name, String outerName, String innerName, int access) {
      this.klass.addTag(new InnerClassTag(name, outerName, innerName, access));
      String innerClassname = AsmUtil.toQualifiedName(name);
      this.deps.add(RefType.v(innerClassname));
   }

   public void visitOuterClass(String owner, String name, String desc) {
      if (name != null) {
         this.klass.addTag(new EnclosingMethodTag(owner, name, desc));
      }

      owner = AsmUtil.toQualifiedName(owner);
      this.deps.add(RefType.v(owner));
      this.klass.setOuterClass(SootResolver.v().makeClassRef(owner));
   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
      return this.getTagBuilder().visitAnnotation(desc, visible);
   }

   public void visitAttribute(Attribute attr) {
      this.getTagBuilder().visitAttribute(attr);
   }
}
