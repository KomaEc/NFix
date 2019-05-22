package soot.dexpler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import soot.ArrayType;
import soot.CompilationDeathException;
import soot.PrimType;
import soot.Scene;
import soot.SootClass;
import soot.SootResolver;
import soot.Type;
import soot.VoidType;
import soot.javaToJimple.IInitialResolver;

public class DexlibWrapper {
   private static final Set<String> systemAnnotationNames;
   private final DexClassLoader dexLoader = this.createDexClassLoader();
   private final Map<String, DexlibWrapper.ClassInformation> classesToDefItems = new HashMap();
   private final Collection<DexBackedDexFile> dexFiles;

   public DexlibWrapper(File dexSource) {
      try {
         List<DexFileProvider.DexContainer> containers = DexFileProvider.v().getDexFromSource(dexSource);
         this.dexFiles = new ArrayList(containers.size());
         Iterator var3 = containers.iterator();

         while(var3.hasNext()) {
            DexFileProvider.DexContainer container = (DexFileProvider.DexContainer)var3.next();
            this.dexFiles.add(container.getBase());
         }

      } catch (IOException var5) {
         throw new CompilationDeathException("IOException during dex parsing", var5);
      }
   }

   protected DexClassLoader createDexClassLoader() {
      return new DexClassLoader();
   }

   public void initialize() {
      Iterator var1 = this.dexFiles.iterator();

      DexBackedDexFile dexFile;
      while(var1.hasNext()) {
         dexFile = (DexBackedDexFile)var1.next();
         Iterator var3 = dexFile.getClasses().iterator();

         while(var3.hasNext()) {
            ClassDef defItem = (ClassDef)var3.next();
            String forClassName = Util.dottedClassName(defItem.getType());
            this.classesToDefItems.put(forClassName, new DexlibWrapper.ClassInformation(dexFile, defItem));
         }
      }

      var1 = this.dexFiles.iterator();

      while(var1.hasNext()) {
         dexFile = (DexBackedDexFile)var1.next();

         for(int i = 0; i < dexFile.getTypeCount(); ++i) {
            String t = dexFile.getType(i);
            Type st = DexType.toSoot(t);
            if (st instanceof ArrayType) {
               st = ((ArrayType)st).baseType;
            }

            String sootTypeName = st.toString();
            if (!Scene.v().containsClass(sootTypeName)) {
               if (st instanceof PrimType || st instanceof VoidType || systemAnnotationNames.contains(sootTypeName)) {
                  continue;
               }

               SootResolver.v().makeClassRef(sootTypeName);
            }

            SootResolver.v().resolveClass(sootTypeName, 2);
         }
      }

   }

   public IInitialResolver.Dependencies makeSootClass(SootClass sc, String className) {
      if (Util.isByteCodeClassName(className)) {
         className = Util.dottedClassName(className);
      }

      DexlibWrapper.ClassInformation defItem = (DexlibWrapper.ClassInformation)this.classesToDefItems.get(className);
      if (defItem != null) {
         return this.dexLoader.makeSootClass(sc, defItem.classDefinition, defItem.dexFile);
      } else {
         throw new RuntimeException("Error: class not found in DEX files: " + className);
      }
   }

   static {
      Set<String> systemAnnotationNamesModifiable = new HashSet();
      systemAnnotationNamesModifiable.add("dalvik.annotation.AnnotationDefault");
      systemAnnotationNamesModifiable.add("dalvik.annotation.EnclosingClass");
      systemAnnotationNamesModifiable.add("dalvik.annotation.EnclosingMethod");
      systemAnnotationNamesModifiable.add("dalvik.annotation.InnerClass");
      systemAnnotationNamesModifiable.add("dalvik.annotation.MemberClasses");
      systemAnnotationNamesModifiable.add("dalvik.annotation.Signature");
      systemAnnotationNamesModifiable.add("dalvik.annotation.Throws");
      systemAnnotationNames = Collections.unmodifiableSet(systemAnnotationNamesModifiable);
   }

   private static class ClassInformation {
      public DexFile dexFile;
      public ClassDef classDefinition;

      public ClassInformation(DexFile file, ClassDef classDef) {
         this.dexFile = file;
         this.classDefinition = classDef;
      }
   }
}
