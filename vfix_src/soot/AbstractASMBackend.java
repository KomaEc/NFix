package soot;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.TraceClassVisitor;
import soot.baf.BafBody;
import soot.jimple.JimpleBody;
import soot.options.Options;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationBooleanElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.AnnotationDefaultTag;
import soot.tagkit.AnnotationDoubleElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationEnumElem;
import soot.tagkit.AnnotationFloatElem;
import soot.tagkit.AnnotationIntElem;
import soot.tagkit.AnnotationLongElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Attribute;
import soot.tagkit.EnclosingMethodTag;
import soot.tagkit.Host;
import soot.tagkit.InnerClassAttribute;
import soot.tagkit.InnerClassTag;
import soot.tagkit.OuterClassTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;
import soot.util.backend.ASMBackendUtils;
import soot.util.backend.SootASMClassWriter;

public abstract class AbstractASMBackend {
   protected ClassVisitor cv;
   protected SootClass sc;
   protected int javaVersion;
   private final Map<SootMethod, BafBody> bafBodyCache = new HashMap();

   public AbstractASMBackend(SootClass sc, int javaVersion) {
      this.sc = sc;
      int minVersion = this.getMinJavaVersion(sc);
      if (javaVersion == 0) {
         javaVersion = 1;
      }

      if (javaVersion != 1 && javaVersion < minVersion) {
         throw new IllegalArgumentException("Enforced Java version " + ASMBackendUtils.translateJavaVersion(javaVersion) + " too low to support required features (" + ASMBackendUtils.translateJavaVersion(minVersion) + " required)");
      } else {
         javaVersion = Math.max(javaVersion, minVersion);
         switch(javaVersion) {
         case 2:
            this.javaVersion = 196653;
            break;
         case 3:
            this.javaVersion = 46;
            break;
         case 4:
            this.javaVersion = 47;
            break;
         case 5:
            this.javaVersion = 48;
            break;
         case 6:
            this.javaVersion = 49;
            break;
         case 7:
            this.javaVersion = 50;
            break;
         case 8:
            this.javaVersion = 51;
            break;
         case 9:
            this.javaVersion = 52;
         }

      }
   }

   protected BafBody getBafBody(SootMethod method) {
      Body activeBody = method.getActiveBody();
      if (activeBody instanceof BafBody) {
         return (BafBody)activeBody;
      } else {
         BafBody body = (BafBody)this.bafBodyCache.get(method);
         if (body != null) {
            return body;
         } else if (activeBody instanceof JimpleBody) {
            body = PackManager.v().convertJimpleBodyToBaf(method);
            this.bafBodyCache.put(method, body);
            return body;
         } else {
            throw new RuntimeException("ASM-backend can only translate Baf- and JimpleBodies!");
         }
      }
   }

   private int getMinJavaVersion(SootClass sc) {
      int minVersion = 2;
      if (sc.hasTag("SignatureTag") && ((SignatureTag)sc.getTag("SignatureTag")).getSignature().contains("<")) {
         minVersion = 6;
      }

      if (sc.hasTag("VisibilityAnnotationTag")) {
         minVersion = 6;
      }

      if (Modifier.isAnnotation(sc.getModifiers())) {
         minVersion = 6;
      }

      Iterator var3 = sc.getFields().iterator();

      while(var3.hasNext()) {
         SootField sf = (SootField)var3.next();
         if (minVersion >= 6) {
            break;
         }

         if (sf.hasTag("SignatureTag") && ((SignatureTag)sf.getTag("SignatureTag")).getSignature().contains("<")) {
            minVersion = 6;
         }

         if (sf.hasTag("VisibilityAnnotationTag")) {
            minVersion = 6;
         }
      }

      var3 = sc.getMethods().iterator();

      while(var3.hasNext()) {
         SootMethod sm = (SootMethod)var3.next();
         if (minVersion >= 9) {
            break;
         }

         if (sm.hasTag("SignatureTag") && ((SignatureTag)sm.getTag("SignatureTag")).getSignature().contains("<")) {
            minVersion = Math.max(minVersion, 6);
         }

         if (sm.hasTag("VisibilityAnnotationTag") || sm.hasTag("VisibilityParameterAnnotationTag")) {
            minVersion = Math.max(minVersion, 6);
         }

         if (sm.hasActiveBody()) {
            minVersion = Math.max(minVersion, this.getMinJavaVersion(sm));
         }
      }

      return minVersion;
   }

   protected int getMinJavaVersion(SootMethod sm) {
      return 8;
   }

   public void generateClassFile(OutputStream os) {
      ClassWriter cw = new SootASMClassWriter(2);
      this.cv = cw;
      this.generateByteCode();

      try {
         os.write(cw.toByteArray());
      } catch (IOException var4) {
         throw new RuntimeException("Could not write class file in the ASM-backend!", var4);
      }
   }

   public void generateTextualRepresentation(PrintWriter pw) {
      this.cv = new TraceClassVisitor(pw);
      this.generateByteCode();
   }

   protected void generateByteCode() {
      this.generateClassHeader();
      if (this.sc.hasTag("SourceFileTag") && !Options.v().no_output_source_file_attribute()) {
         String srcName = ((SourceFileTag)this.sc.getTag("SourceFileTag")).getSourceFile();
         this.cv.visitSource(srcName, (String)null);
      }

      if (this.sc.hasOuterClass() || this.sc.hasTag("EnclosingMethodTag") || this.sc.hasTag("OuterClassTag")) {
         this.generateOuterClassReference();
      }

      this.generateAnnotations(this.cv, this.sc);
      this.generateAttributes();
      this.generateInnerClassReferences();
      this.generateFields();
      this.generateMethods();
      this.cv.visitEnd();
   }

   protected void generateMethods() {
      List<SootMethod> sortedMethods = new ArrayList(this.sc.getMethods());
      Collections.sort(sortedMethods, new AbstractASMBackend.SootMethodComparator());
      Iterator var2 = sortedMethods.iterator();

      label98:
      while(true) {
         SootMethod sm;
         Iterator var11;
         MethodVisitor mv;
         do {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               sm = (SootMethod)var2.next();
            } while(sm.isPhantom());

            int access = getModifiers(sm.getModifiers(), sm);
            String name = sm.getName();
            StringBuilder descBuilder = new StringBuilder(5);
            descBuilder.append('(');
            Iterator var7 = sm.getParameterTypes().iterator();

            while(var7.hasNext()) {
               Type t = (Type)var7.next();
               descBuilder.append(ASMBackendUtils.toTypeDesc(t));
            }

            descBuilder.append(')');
            descBuilder.append(ASMBackendUtils.toTypeDesc(sm.getReturnType()));
            String sig = null;
            if (sm.hasTag("SignatureTag")) {
               SignatureTag genericSignature = (SignatureTag)sm.getTag("SignatureTag");
               sig = genericSignature.getSignature();
            }

            List<SootClass> exceptionList = sm.getExceptionsUnsafe();
            String[] exceptions;
            if (exceptionList != null) {
               exceptions = new String[exceptionList.size()];
               int i = 0;

               for(var11 = sm.getExceptions().iterator(); var11.hasNext(); ++i) {
                  SootClass exc = (SootClass)var11.next();
                  exceptions[i] = ASMBackendUtils.slashify(exc.getName());
               }
            } else {
               exceptions = new String[0];
            }

            mv = this.cv.visitMethod(access, name, descBuilder.toString(), sig, exceptions);
         } while(mv == null);

         var11 = sm.getTags().iterator();

         while(true) {
            ArrayList tags;
            do {
               Tag t;
               do {
                  if (!var11.hasNext()) {
                     this.generateAnnotations(mv, sm);
                     this.generateAttributes(mv, sm);
                     if (sm.hasActiveBody()) {
                        mv.visitCode();
                        this.generateMethodBody(mv, sm);
                        mv.visitMaxs(0, 0);
                     }

                     mv.visitEnd();
                     continue label98;
                  }

                  t = (Tag)var11.next();
               } while(!(t instanceof VisibilityParameterAnnotationTag));

               VisibilityParameterAnnotationTag vpt = (VisibilityParameterAnnotationTag)t;
               tags = vpt.getVisibilityAnnotations();
            } while(tags == null);

            for(int j = 0; j < tags.size(); ++j) {
               VisibilityAnnotationTag va = (VisibilityAnnotationTag)tags.get(j);
               if (va != null) {
                  Iterator var17 = va.getAnnotations().iterator();

                  while(var17.hasNext()) {
                     AnnotationTag at = (AnnotationTag)var17.next();
                     AnnotationVisitor av = mv.visitParameterAnnotation(j, at.getType(), va.getVisibility() == 0);
                     this.generateAnnotationElems(av, at.getElems(), true);
                  }
               }
            }
         }
      }
   }

   protected void generateFields() {
      Iterator var1 = this.sc.getFields().iterator();

      while(var1.hasNext()) {
         SootField f = (SootField)var1.next();
         if (!f.isPhantom()) {
            String name = f.getName();
            String desc = ASMBackendUtils.toTypeDesc(f.getType());
            String sig = null;
            if (f.hasTag("SignatureTag")) {
               SignatureTag genericSignature = (SignatureTag)f.getTag("SignatureTag");
               sig = genericSignature.getSignature();
            }

            Object value = ASMBackendUtils.getDefaultValue(f);
            int access = getModifiers(f.getModifiers(), f);
            FieldVisitor fv = this.cv.visitField(access, name, desc, sig, value);
            if (fv != null) {
               this.generateAnnotations(fv, f);
               this.generateAttributes(fv, f);
               fv.visitEnd();
            }
         }
      }

   }

   protected void generateInnerClassReferences() {
      if (this.sc.hasTag("InnerClassAttribute") && !Options.v().no_output_inner_classes_attribute()) {
         InnerClassAttribute ica = (InnerClassAttribute)this.sc.getTag("InnerClassAttribute");
         List<InnerClassTag> sortedTags = new ArrayList(ica.getSpecs());
         Collections.sort(sortedTags, new AbstractASMBackend.SootInnerClassComparator());
         Iterator var3 = sortedTags.iterator();

         while(var3.hasNext()) {
            InnerClassTag ict = (InnerClassTag)var3.next();
            String name = ASMBackendUtils.slashify(ict.getInnerClass());
            String outerClassName = ASMBackendUtils.slashify(ict.getOuterClass());
            String innerName = ASMBackendUtils.slashify(ict.getShortName());
            int access = ict.getAccessFlags();
            this.cv.visitInnerClass(name, outerClassName, innerName, access);
         }
      }

   }

   protected void generateAttributes() {
      Iterator var1 = this.sc.getTags().iterator();

      while(var1.hasNext()) {
         Tag t = (Tag)var1.next();
         if (t instanceof Attribute) {
            this.cv.visitAttribute(ASMBackendUtils.createASMAttribute((Attribute)t));
         }
      }

   }

   protected void generateAttributes(FieldVisitor fv, SootField f) {
      Iterator var3 = f.getTags().iterator();

      while(var3.hasNext()) {
         Tag t = (Tag)var3.next();
         if (t instanceof Attribute) {
            org.objectweb.asm.Attribute a = ASMBackendUtils.createASMAttribute((Attribute)t);
            fv.visitAttribute(a);
         }
      }

   }

   protected void generateAttributes(MethodVisitor mv, SootMethod m) {
      Iterator var3 = m.getTags().iterator();

      while(var3.hasNext()) {
         Tag t = (Tag)var3.next();
         if (t instanceof Attribute) {
            org.objectweb.asm.Attribute a = ASMBackendUtils.createASMAttribute((Attribute)t);
            mv.visitAttribute(a);
         }
      }

   }

   protected void generateAnnotations(Object visitor, Host host) {
      Iterator var3 = host.getTags().iterator();

      while(true) {
         while(var3.hasNext()) {
            Tag t = (Tag)var3.next();
            if (t instanceof VisibilityAnnotationTag) {
               VisibilityAnnotationTag vat = (VisibilityAnnotationTag)t;
               boolean runTimeVisible = vat.getVisibility() == 0;

               AnnotationTag at;
               AnnotationVisitor av;
               for(Iterator var7 = vat.getAnnotations().iterator(); var7.hasNext(); this.generateAnnotationElems(av, at.getElems(), true)) {
                  at = (AnnotationTag)var7.next();
                  av = null;
                  if (visitor instanceof ClassVisitor) {
                     av = ((ClassVisitor)visitor).visitAnnotation(at.getType(), runTimeVisible);
                  } else if (visitor instanceof FieldVisitor) {
                     av = ((FieldVisitor)visitor).visitAnnotation(at.getType(), runTimeVisible);
                  } else if (visitor instanceof MethodVisitor) {
                     av = ((MethodVisitor)visitor).visitAnnotation(at.getType(), runTimeVisible);
                  }
               }
            } else if (host instanceof SootMethod && t instanceof AnnotationDefaultTag) {
               AnnotationDefaultTag adt = (AnnotationDefaultTag)t;
               AnnotationVisitor av = ((MethodVisitor)visitor).visitAnnotationDefault();
               this.generateAnnotationElems(av, Collections.singleton(adt.getDefaultVal()), true);
            }
         }

         return;
      }
   }

   protected void generateAnnotationElems(AnnotationVisitor av, Collection<AnnotationElem> elements, boolean addName) {
      if (av != null) {
         Iterator it = elements.iterator();

         while(it.hasNext()) {
            AnnotationElem elem = (AnnotationElem)it.next();
            if (elem instanceof AnnotationEnumElem) {
               AnnotationEnumElem enumElem = (AnnotationEnumElem)elem;
               av.visitEnum(enumElem.getName(), enumElem.getTypeName(), enumElem.getConstantName());
            } else {
               AnnotationVisitor aVisitor;
               if (elem instanceof AnnotationArrayElem) {
                  AnnotationArrayElem arrayElem = (AnnotationArrayElem)elem;
                  aVisitor = av.visitArray(arrayElem.getName());
                  this.generateAnnotationElems(aVisitor, arrayElem.getValues(), false);
               } else if (elem instanceof AnnotationAnnotationElem) {
                  AnnotationAnnotationElem aElem = (AnnotationAnnotationElem)elem;
                  aVisitor = av.visitAnnotation(aElem.getName(), aElem.getValue().getType());
                  this.generateAnnotationElems(aVisitor, aElem.getValue().getElems(), true);
               } else {
                  Object val = null;
                  if (elem instanceof AnnotationIntElem) {
                     AnnotationIntElem intElem = (AnnotationIntElem)elem;
                     int value = intElem.getValue();
                     switch(intElem.getKind()) {
                     case 'B':
                        val = (byte)value;
                        break;
                     case 'I':
                        val = value;
                        break;
                     case 'S':
                        val = (short)value;
                        break;
                     case 'Z':
                        val = value == 1;
                     }
                  } else if (elem instanceof AnnotationBooleanElem) {
                     AnnotationBooleanElem booleanElem = (AnnotationBooleanElem)elem;
                     val = booleanElem.getValue();
                  } else if (elem instanceof AnnotationFloatElem) {
                     AnnotationFloatElem floatElem = (AnnotationFloatElem)elem;
                     val = floatElem.getValue();
                  } else if (elem instanceof AnnotationLongElem) {
                     AnnotationLongElem longElem = (AnnotationLongElem)elem;
                     val = longElem.getValue();
                  } else if (elem instanceof AnnotationDoubleElem) {
                     AnnotationDoubleElem doubleElem = (AnnotationDoubleElem)elem;
                     val = doubleElem.getValue();
                  } else if (elem instanceof AnnotationStringElem) {
                     AnnotationStringElem stringElem = (AnnotationStringElem)elem;
                     val = stringElem.getValue();
                  } else if (elem instanceof AnnotationClassElem) {
                     AnnotationClassElem classElem = (AnnotationClassElem)elem;
                     val = org.objectweb.asm.Type.getType(classElem.getDesc());
                  }

                  if (addName) {
                     av.visit(elem.getName(), val);
                  } else {
                     av.visit((String)null, val);
                  }
               }
            }
         }

         av.visitEnd();
      }

   }

   protected void generateOuterClassReference() {
      SootClass outerClass = this.sc.getOuterClass();
      String outerClassName = ASMBackendUtils.slashify(outerClass.getName());
      String enclosingMethod = null;
      String enclosingMethodSig = null;
      if (this.sc.hasTag("EnclosingMethodTag")) {
         EnclosingMethodTag emTag = (EnclosingMethodTag)this.sc.getTag("EnclosingMethodTag");
         if (!this.sc.hasOuterClass()) {
            outerClassName = ASMBackendUtils.slashify(emTag.getEnclosingClass());
         }

         enclosingMethod = emTag.getEnclosingMethod();
         enclosingMethodSig = emTag.getEnclosingMethodSig();
      }

      if (!this.sc.hasOuterClass() && this.sc.hasTag("OuterClassTag")) {
         outerClassName = ASMBackendUtils.slashify(((OuterClassTag)this.sc.getTag("OuterClassTag")).getName());
      }

      this.cv.visitOuterClass(outerClassName, enclosingMethod, enclosingMethodSig);
   }

   protected void generateClassHeader() {
      int classModifiers = this.sc.getModifiers();
      int modifier = getModifiers(classModifiers, this.sc);
      String className = ASMBackendUtils.slashify(this.sc.getName());
      String signature = null;
      if (this.sc.hasTag("SignatureTag")) {
         signature = ((SignatureTag)this.sc.getTag("SignatureTag")).getSignature();
      }

      String superClass = "java/lang/Object";
      SootClass csuperClass = this.sc.getSuperclassUnsafe();
      if (csuperClass != null) {
         superClass = ASMBackendUtils.slashify(csuperClass.getName());
      }

      String[] interfaces = new String[this.sc.getInterfaceCount()];
      Iterator<SootClass> implementedInterfaces = this.sc.getInterfaces().iterator();

      for(int i = 0; implementedInterfaces.hasNext(); ++i) {
         SootClass interf = (SootClass)implementedInterfaces.next();
         interfaces[i] = ASMBackendUtils.slashify(interf.getName());
      }

      this.cv.visit(this.javaVersion, modifier, className, signature, superClass, interfaces);
   }

   protected static int getModifiers(int modVal, Host host) {
      int modifier = 0;
      if (Modifier.isPublic(modVal)) {
         modifier |= 1;
      } else if (Modifier.isPrivate(modVal)) {
         modifier |= 2;
      } else if (Modifier.isProtected(modVal)) {
         modifier |= 4;
      }

      if (Modifier.isStatic(modVal) && (host instanceof SootField || host instanceof SootMethod)) {
         modifier |= 8;
      }

      if (Modifier.isFinal(modVal)) {
         modifier |= 16;
      }

      if (Modifier.isSynchronized(modVal) && host instanceof SootMethod) {
         modifier |= 32;
      }

      if (Modifier.isVolatile(modVal) && !(host instanceof SootClass)) {
         modifier |= 64;
      }

      if (Modifier.isTransient(modVal) && !(host instanceof SootClass)) {
         modifier |= 128;
      }

      if (Modifier.isNative(modVal) && host instanceof SootMethod) {
         modifier |= 256;
      }

      if (Modifier.isInterface(modVal) && host instanceof SootClass) {
         modifier |= 512;
      } else if (host instanceof SootClass) {
         modifier |= 32;
      }

      if (Modifier.isAbstract(modVal) && !(host instanceof SootField)) {
         modifier |= 1024;
      }

      if (Modifier.isStrictFP(modVal) && host instanceof SootMethod) {
         modifier |= 2048;
      }

      if (Modifier.isSynthetic(modVal) || host.hasTag("SyntheticTag")) {
         modifier |= 4096;
      }

      if (Modifier.isAnnotation(modVal) && host instanceof SootClass) {
         modifier |= 8192;
      }

      if (Modifier.isEnum(modVal) && !(host instanceof SootMethod)) {
         modifier |= 16384;
      }

      return modifier;
   }

   protected abstract void generateMethodBody(MethodVisitor var1, SootMethod var2);

   private class SootInnerClassComparator implements Comparator<InnerClassTag> {
      private SootInnerClassComparator() {
      }

      public int compare(InnerClassTag o1, InnerClassTag o2) {
         return o1.getInnerClass() == null ? 0 : o1.getInnerClass().compareTo(o2.getInnerClass());
      }

      // $FF: synthetic method
      SootInnerClassComparator(Object x1) {
         this();
      }
   }

   private class SootMethodComparator implements Comparator<SootMethod> {
      private SootMethodComparator() {
      }

      public int compare(SootMethod o1, SootMethod o2) {
         return o1.getName().compareTo(o2.getName());
      }

      // $FF: synthetic method
      SootMethodComparator(Object x1) {
         this();
      }
   }
}
