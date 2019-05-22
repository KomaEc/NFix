package soot.jbco.jimpleTransformations;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.FastHierarchy;
import soot.G;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.name.JunkNameGenerator;
import soot.jbco.name.NameGenerator;
import soot.jbco.util.BodyBuilder;
import soot.jimple.CastExpr;
import soot.jimple.ClassConstant;
import soot.jimple.Expr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.Ref;

public class ClassRenamer extends SceneTransformer implements IJbcoTransform {
   private static final Logger logger = LoggerFactory.getLogger(ClassRenamer.class);
   private boolean removePackages = false;
   private boolean renamePackages = false;
   public static final String name = "wjtp.jbco_cr";
   private static final String[] dependencies = new String[]{"wjtp.jbco_cr"};
   private final Map<String, String> oldToNewPackageNames = new HashMap();
   private final Map<String, String> oldToNewClassNames = new HashMap();
   private final Map<String, SootClass> newNameToClass = new HashMap();
   private final Object classNamesMapLock = new Object();
   private final Object packageNamesMapLock = new Object();
   private final NameGenerator nameGenerator;

   public ClassRenamer(Singletons.Global global) {
      if (global == null) {
         throw new NullPointerException("Cannot instantiate ClassRenamer with null Singletons.Global");
      } else {
         this.nameGenerator = new JunkNameGenerator();
      }
   }

   public static ClassRenamer v() {
      return G.v().soot_jbco_jimpleTransformations_ClassRenamer();
   }

   public String getName() {
      return "wjtp.jbco_cr";
   }

   public String[] getDependencies() {
      return dependencies;
   }

   public void outputSummary() {
   }

   public boolean isRemovePackages() {
      return this.removePackages;
   }

   public void setRemovePackages(boolean removePackages) {
      this.removePackages = removePackages;
   }

   public boolean isRenamePackages() {
      return this.renamePackages;
   }

   public void setRenamePackages(boolean renamePackages) {
      this.renamePackages = renamePackages;
   }

   public void addClassNameMapping(String classNameSource, String classNameTarget) {
      synchronized(this.classNamesMapLock) {
         if (!this.oldToNewClassNames.containsKey(classNameSource) && !this.oldToNewClassNames.containsValue(classNameTarget) && !BodyBuilder.nameList.contains(classNameTarget)) {
            this.oldToNewClassNames.put(classNameSource, classNameTarget);
            BodyBuilder.nameList.add(classNameTarget);
            return;
         }
      }

      throw new IllegalStateException("Cannot generate unique name: too long for JVM.");
   }

   public Map<String, String> getClassNameMapping(BiPredicate<String, String> predicate) {
      return (Map)(predicate == null ? new HashMap(this.oldToNewClassNames) : (Map)this.oldToNewClassNames.entrySet().stream().filter((entry) -> {
         return predicate.test(entry.getKey(), entry.getValue());
      }).collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      if (this.isVerbose()) {
         logger.debug("Transforming Class Names...");
      }

      BodyBuilder.retrieveAllBodies();
      BodyBuilder.retrieveAllNames();
      SootClass mainClass = getMainClassSafely();
      Iterator var4 = Scene.v().getApplicationClasses().iterator();

      SootClass sootClass;
      while(var4.hasNext()) {
         sootClass = (SootClass)var4.next();
         String className = sootClass.getName();
         if (!sootClass.equals(mainClass) && !this.oldToNewClassNames.containsValue(className) && Main.getWeight(phaseName, className) != 0) {
            String newClassName = (String)this.oldToNewClassNames.get(className);
            if (newClassName == null) {
               newClassName = this.getNewName(getPackageName(className), className);
            }

            sootClass.setName(newClassName);
            RefType crt = RefType.v(newClassName);
            crt.setSootClass(sootClass);
            sootClass.setRefType(crt);
            sootClass.setResolvingLevel(3);
            this.newNameToClass.put(newClassName, sootClass);
            if (this.isVerbose()) {
               logger.info("\tRenaming " + className + " to " + newClassName);
            }
         }
      }

      Scene.v().releaseActiveHierarchy();
      Scene.v().setFastHierarchy(new FastHierarchy());
      if (this.isVerbose()) {
         logger.info("\r\tUpdating bytecode class references");
      }

      var4 = Scene.v().getApplicationClasses().iterator();

      label94:
      while(var4.hasNext()) {
         sootClass = (SootClass)var4.next();
         Iterator var18 = sootClass.getMethods().iterator();

         while(true) {
            Body aBody;
            while(true) {
               SootMethod sootMethod;
               do {
                  if (!var18.hasNext()) {
                     continue label94;
                  }

                  sootMethod = (SootMethod)var18.next();
               } while(!sootMethod.isConcrete());

               if (this.isVerbose()) {
                  logger.info("\t\t" + sootMethod.getSignature());
               }

               try {
                  aBody = sootMethod.getActiveBody();
                  break;
               } catch (Exception var17) {
               }
            }

            Iterator var9 = aBody.getUnits().iterator();

            while(var9.hasNext()) {
               Unit u = (Unit)var9.next();
               Iterator var11 = u.getUseAndDefBoxes().iterator();

               while(var11.hasNext()) {
                  ValueBox vb = (ValueBox)var11.next();
                  Value v = vb.getValue();
                  if (v instanceof ClassConstant) {
                     ClassConstant constant = (ClassConstant)v;
                     RefType type = (RefType)constant.toSootType();
                     RefType updatedType = type.getSootClass().getType();
                     vb.setValue(ClassConstant.fromType(updatedType));
                  } else if (v instanceof Expr) {
                     if (v instanceof CastExpr) {
                        CastExpr castExpr = (CastExpr)v;
                        this.updateType(castExpr.getCastType());
                     } else if (v instanceof InstanceOfExpr) {
                        InstanceOfExpr instanceOfExpr = (InstanceOfExpr)v;
                        this.updateType(instanceOfExpr.getCheckType());
                     }
                  } else if (v instanceof Ref) {
                     this.updateType(v.getType());
                  }
               }
            }
         }
      }

      Scene.v().releaseActiveHierarchy();
      Scene.v().setFastHierarchy(new FastHierarchy());
   }

   private void updateType(Type type) {
      if (type instanceof RefType) {
         RefType rt = (RefType)type;
         if (!rt.getSootClass().isLibraryClass() && this.oldToNewClassNames.containsKey(rt.getClassName())) {
            rt.setSootClass((SootClass)this.newNameToClass.get(this.oldToNewClassNames.get(rt.getClassName())));
            rt.setClassName((String)this.oldToNewClassNames.get(rt.getClassName()));
         }
      } else if (type instanceof ArrayType) {
         ArrayType at = (ArrayType)type;
         if (at.baseType instanceof RefType) {
            RefType rt = (RefType)at.baseType;
            if (!rt.getSootClass().isLibraryClass() && this.oldToNewClassNames.containsKey(rt.getClassName())) {
               rt.setSootClass((SootClass)this.newNameToClass.get(this.oldToNewClassNames.get(rt.getClassName())));
            }
         }
      }

   }

   public String getNewName(String packageName, String className) {
      int size = 5;
      int var4 = 0;

      while(true) {
         String junkName = this.nameGenerator.generateName(size);
         String newClassName = this.removePackages ? junkName : (this.renamePackages ? this.getNewPackageName(packageName) : packageName) + junkName;
         synchronized(this.classNamesMapLock) {
            if (!this.oldToNewClassNames.containsKey(newClassName) && !this.oldToNewClassNames.containsValue(newClassName) && !BodyBuilder.nameList.contains(newClassName)) {
               String classNameSource = className != null && !className.isEmpty() ? className : newClassName;
               this.addClassNameMapping(classNameSource, newClassName);
               return newClassName;
            }
         }

         if (var4++ > size) {
            ++size;
            var4 = 0;
         }
      }
   }

   public static String getPackageName(String fullyQualifiedClassName) {
      if (fullyQualifiedClassName != null && !fullyQualifiedClassName.isEmpty()) {
         int idx = fullyQualifiedClassName.lastIndexOf(46);
         return idx >= 0 ? fullyQualifiedClassName.substring(0, idx + 1) : "";
      } else {
         return "";
      }
   }

   private static SootClass getMainClassSafely() {
      return Scene.v().hasMainClass() ? Scene.v().getMainClass() : null;
   }

   private String getNewPackageName(String packageName) {
      if (packageName != null && !packageName.isEmpty()) {
         String[] packageNameParts = packageName.split("\\.");
         StringBuilder newPackageName = new StringBuilder((int)((double)(5 * (packageNameParts.length + 1)) * 1.5D));
         String[] var4 = packageNameParts;
         int var5 = packageNameParts.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String packageNamePart = var4[var6];
            newPackageName.append(this.getNewPackageNamePart(packageNamePart)).append('.');
         }

         return newPackageName.toString();
      } else {
         return this.getNewPackageNamePart((String)null);
      }
   }

   private String getNewPackageNamePart(String oldPackageNamePart) {
      if (oldPackageNamePart != null && !oldPackageNamePart.isEmpty() && this.oldToNewPackageNames.containsKey(oldPackageNamePart)) {
         return (String)this.oldToNewPackageNames.get(oldPackageNamePart);
      } else {
         int size = 5;
         int tries = 0;
         String newPackageNamePart = "";

         while(newPackageNamePart.length() < 21845) {
            newPackageNamePart = this.nameGenerator.generateName(size);
            synchronized(this.packageNamesMapLock) {
               if (!this.oldToNewPackageNames.containsValue(newPackageNamePart) || !this.oldToNewPackageNames.containsKey(newPackageNamePart)) {
                  String key = oldPackageNamePart != null && !oldPackageNamePart.isEmpty() ? oldPackageNamePart : newPackageNamePart;
                  this.oldToNewPackageNames.put(key, newPackageNamePart);
                  return newPackageNamePart;
               }
            }

            if (tries++ > size) {
               ++size;
               tries = 0;
            }
         }

         throw new IllegalStateException("Cannot generate unique package name part: too long for JVM.");
      }
   }
}
