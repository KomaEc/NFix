package soot.dexpler;

import java.util.Iterator;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootResolver;
import soot.javaToJimple.IInitialResolver;
import soot.options.Options;
import soot.tagkit.InnerClassAttribute;
import soot.tagkit.InnerClassTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.Tag;

public class DexClassLoader {
   protected void loadMethod(Method method, SootClass declaringClass, DexAnnotation annotations, DexMethod dexMethodFactory) {
      SootMethod sm = dexMethodFactory.makeSootMethod(method);
      if (!declaringClass.declaresMethod(sm.getName(), sm.getParameterTypes(), sm.getReturnType())) {
         declaringClass.addMethod(sm);
         annotations.handleMethodAnnotation(sm, method);
      }
   }

   public IInitialResolver.Dependencies makeSootClass(SootClass sc, ClassDef defItem, DexFile dexFile) {
      String superClass = defItem.getSuperclass();
      IInitialResolver.Dependencies deps = new IInitialResolver.Dependencies();
      String sourceFile = defItem.getSourceFile();
      if (sourceFile != null) {
         sc.addTag(new SourceFileTag(sourceFile));
      }

      if (superClass != null) {
         String superClassName = Util.dottedClassName(superClass);
         SootClass sootSuperClass = SootResolver.v().makeClassRef(superClassName);
         sc.setSuperclass(sootSuperClass);
         deps.typesToHierarchy.add(sootSuperClass.getType());
      }

      int accessFlags = defItem.getAccessFlags();
      sc.setModifiers(accessFlags);
      if (defItem.getInterfaces() != null) {
         Iterator var20 = defItem.getInterfaces().iterator();

         while(var20.hasNext()) {
            String interfaceName = (String)var20.next();
            String interfaceClassName = Util.dottedClassName(interfaceName);
            if (!sc.implementsInterface(interfaceClassName)) {
               SootClass interfaceClass = SootResolver.v().makeClassRef(interfaceClassName);
               interfaceClass.setModifiers(interfaceClass.getModifiers() | 512);
               sc.addInterface(interfaceClass);
               deps.typesToHierarchy.add(interfaceClass.getType());
            }
         }
      }

      if (Options.v().oaat() && sc.resolvingLevel() <= 1) {
         return deps;
      } else {
         DexAnnotation da = new DexAnnotation(sc, deps);
         Iterator var22 = defItem.getStaticFields().iterator();

         Field f;
         while(var22.hasNext()) {
            f = (Field)var22.next();
            this.loadField(sc, da, f);
         }

         var22 = defItem.getInstanceFields().iterator();

         while(var22.hasNext()) {
            f = (Field)var22.next();
            this.loadField(sc, da, f);
         }

         DexMethod dexMethod = this.createDexMethodFactory(dexFile, sc);
         Iterator var25 = defItem.getDirectMethods().iterator();

         Method method;
         while(var25.hasNext()) {
            method = (Method)var25.next();
            this.loadMethod(method, sc, da, dexMethod);
         }

         var25 = defItem.getVirtualMethods().iterator();

         while(var25.hasNext()) {
            method = (Method)var25.next();
            this.loadMethod(method, sc, da, dexMethod);
         }

         da.handleClassAnnotation(defItem);
         InnerClassAttribute ica = (InnerClassAttribute)sc.getTag("InnerClassAttribute");
         if (ica != null) {
            Iterator innerTagIt = ica.getSpecs().iterator();

            while(true) {
               while(true) {
                  Tag t;
                  do {
                     if (!innerTagIt.hasNext()) {
                        if (ica.getSpecs().isEmpty()) {
                           sc.getTags().remove(ica);
                        }

                        return deps;
                     }

                     t = (Tag)innerTagIt.next();
                  } while(!(t instanceof InnerClassTag));

                  InnerClassTag ict = (InnerClassTag)t;
                  String outer = DexInnerClassParser.getOuterClassNameFromTag(ict);
                  if (outer == null) {
                     innerTagIt.remove();
                  } else if (!outer.equals(sc.getName())) {
                     String inner = ict.getInnerClass().replaceAll("/", ".");
                     if (!inner.equals(sc.getName())) {
                        innerTagIt.remove();
                     } else {
                        SootClass osc = SootResolver.v().makeClassRef(outer);
                        if (osc == sc) {
                           if (!sc.hasOuterClass()) {
                              continue;
                           }

                           osc = sc.getOuterClass();
                        } else {
                           deps.typesToHierarchy.add(osc.getType());
                        }

                        InnerClassAttribute icat = (InnerClassAttribute)osc.getTag("InnerClassAttribute");
                        if (icat == null) {
                           icat = new InnerClassAttribute();
                           osc.addTag(icat);
                        }

                        InnerClassTag newt = new InnerClassTag(ict.getInnerClass(), ict.getOuterClass(), ict.getShortName(), ict.getAccessFlags());
                        icat.add(newt);
                        innerTagIt.remove();
                        if (!sc.hasTag("InnerClassTag") && ((InnerClassTag)t).getInnerClass().replaceAll("/", ".").equals(sc.toString())) {
                           sc.addTag(t);
                        }
                     }
                  }
               }
            }
         } else {
            return deps;
         }
      }
   }

   protected DexMethod createDexMethodFactory(DexFile dexFile, SootClass sc) {
      return new DexMethod(dexFile, sc);
   }

   protected void loadField(SootClass declaringClass, DexAnnotation annotations, Field sf) {
      if (!declaringClass.declaresField(sf.getName(), DexType.toSoot(sf.getType()))) {
         SootField sootField = DexField.makeSootField(sf);
         sootField = declaringClass.getOrAddField(sootField);
         annotations.handleFieldAnnotation(sootField, sf);
      }
   }
}
