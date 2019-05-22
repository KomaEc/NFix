package soot.jbco.jimpleTransformations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.name.JunkNameGenerator;
import soot.jbco.name.NameGenerator;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.HierarchyUtils;
import soot.jbco.util.Rand;
import soot.jimple.InvokeExpr;

public class MethodRenamer extends SceneTransformer implements IJbcoTransform {
   private static final Logger logger = LoggerFactory.getLogger(MethodRenamer.class);
   public static final String name = "wjtp.jbco_mr";
   public static final String[] dependencies = new String[]{"wjtp.jbco_mr"};
   private static final String MAIN_METHOD_SUB_SIGNATURE = SootMethod.getSubSignature("main", Collections.singletonList(ArrayType.v(RefType.v("java.lang.String"), 1)), VoidType.v());
   private static final Function<SootClass, Map<String, String>> RENAMING_MAP_CREATOR = (key) -> {
      return new HashMap();
   };
   private final Map<SootClass, Map<String, String>> classToRenamingMap = new HashMap();
   private final NameGenerator nameGenerator;

   public MethodRenamer(Singletons.Global global) {
      if (global == null) {
         throw new NullPointerException("Cannot instantiate MethodRenamer with null Singletons.Global");
      } else {
         this.nameGenerator = new JunkNameGenerator();
      }
   }

   public static MethodRenamer v() {
      return G.v().soot_jbco_jimpleTransformations_MethodRenamer();
   }

   public String getName() {
      return "wjtp.jbco_mr";
   }

   public String[] getDependencies() {
      return (String[])Arrays.copyOf(dependencies, dependencies.length);
   }

   public void outputSummary() {
      Integer newNames = (Integer)this.classToRenamingMap.values().stream().map(Map::values).flatMap(Collection::stream).collect(Collectors.collectingAndThen(Collectors.toSet(), Set::size));
      logger.info((String)"{} methods were renamed.", (Object)newNames);
   }

   public Map<String, String> getRenamingMap(String className) {
      return (Map)this.classToRenamingMap.entrySet().stream().filter((entry) -> {
         return ((SootClass)entry.getKey()).getName().equals(className);
      }).flatMap((entry) -> {
         return ((Map)entry.getValue()).entrySet().stream();
      }).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      if (this.isVerbose()) {
         logger.info("Transforming method names...");
      }

      BodyBuilder.retrieveAllBodies();
      BodyBuilder.retrieveAllNames();
      Scene.v().releaseActiveHierarchy();
      Iterator var3 = Scene.v().getApplicationClasses().iterator();

      SootClass applicationClass;
      label150:
      while(var3.hasNext()) {
         applicationClass = (SootClass)var3.next();
         List<String> fieldNames = (List)applicationClass.getFields().stream().map(SootField::getName).collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
         List<String> leftFieldNames = new ArrayList(fieldNames);
         List<SootMethod> methods = new ArrayList(applicationClass.getMethods());
         Iterator var8 = methods.iterator();

         while(true) {
            while(true) {
               SootMethod method;
               do {
                  if (!var8.hasNext()) {
                     continue label150;
                  }

                  method = (SootMethod)var8.next();
               } while(!this.isRenamingAllowed(method));

               Set<SootClass> declaringClasses = this.getDeclaringClasses(applicationClass, method);
               if (declaringClasses.isEmpty()) {
                  throw new IllegalStateException("Cannot find classes that declare " + method.getSignature() + ".");
               }

               Optional<SootClass> libraryClass = declaringClasses.stream().filter(SootClass::isLibraryClass).findAny();
               if (libraryClass.isPresent()) {
                  if (this.isVerbose()) {
                     logger.info((String)"Skipping renaming {} method as it overrides library one from {}.", (Object)method.getSignature(), (Object)((SootClass)libraryClass.get()).getName());
                  }
               } else {
                  Set<SootClass> union = this.uniteWithApplicationParents(applicationClass, declaringClasses);
                  String newName = this.getNewName(union, method.getName());
                  if (newName == null) {
                     if (leftFieldNames.isEmpty()) {
                        newName = this.getNewName();
                     } else {
                        int randomIndex = Rand.getInt(leftFieldNames.size());
                        String randomFieldName = (String)leftFieldNames.remove(randomIndex);
                        if (!this.isNotUnique(randomFieldName) && !fieldNames.contains(randomFieldName)) {
                           newName = randomFieldName;
                        } else {
                           newName = this.getNewName();
                        }
                     }
                  }

                  Iterator var29 = union.iterator();

                  while(var29.hasNext()) {
                     SootClass declaringClass = (SootClass)var29.next();
                     ((Map)this.classToRenamingMap.computeIfAbsent(declaringClass, RENAMING_MAP_CREATOR)).put(method.getName(), newName);
                  }
               }
            }
         }
      }

      var3 = Scene.v().getApplicationClasses().iterator();

      ArrayList methods;
      Iterator var21;
      SootMethod method;
      while(var3.hasNext()) {
         applicationClass = (SootClass)var3.next();
         methods = new ArrayList(applicationClass.getMethods());
         var21 = methods.iterator();

         while(var21.hasNext()) {
            method = (SootMethod)var21.next();
            String newName = this.getNewName(Collections.singleton(applicationClass), method.getName());
            if (newName != null) {
               if (this.isVerbose()) {
                  logger.info((String)"Method \"{}\" is being renamed to \"{}\".", (Object)method.getSignature(), (Object)newName);
               }

               method.setName(newName);
            }
         }
      }

      var3 = Scene.v().getApplicationClasses().iterator();

      label111:
      while(var3.hasNext()) {
         applicationClass = (SootClass)var3.next();
         methods = new ArrayList(applicationClass.getMethods());
         var21 = methods.iterator();

         while(true) {
            Body body;
            do {
               do {
                  do {
                     if (!var21.hasNext()) {
                        continue label111;
                     }

                     method = (SootMethod)var21.next();
                  } while(!method.isConcrete());
               } while(method.getDeclaringClass().isLibraryClass());

               body = getActiveBodySafely(method);
            } while(body == null);

            Iterator var25 = body.getUnits().iterator();

            while(var25.hasNext()) {
               Unit unit = (Unit)var25.next();
               Iterator var27 = unit.getUseBoxes().iterator();

               while(var27.hasNext()) {
                  ValueBox valueBox = (ValueBox)var27.next();
                  Value v = valueBox.getValue();
                  if (v instanceof InvokeExpr) {
                     InvokeExpr invokeExpr = (InvokeExpr)v;
                     SootMethodRef methodRef = invokeExpr.getMethodRef();
                     Set<SootClass> parents = this.getParents(methodRef.declaringClass());
                     Optional<SootClass> declaringLibraryClass = this.findDeclaringLibraryClass(parents, methodRef);
                     if (declaringLibraryClass.isPresent()) {
                        if (this.isVerbose()) {
                           logger.info("Skipping replacing method call \"{}\" in \"{}\" as it is overrides one  from library {}.", methodRef.getSignature(), method.getSignature(), ((SootClass)declaringLibraryClass.get()).getName());
                        }
                     } else {
                        String newName = this.getNewName(parents, methodRef.name());
                        if (newName != null) {
                           SootMethodRef newMethodRef = Scene.v().makeMethodRef(methodRef.declaringClass(), newName, methodRef.parameterTypes(), methodRef.returnType(), methodRef.isStatic());
                           invokeExpr.setMethodRef(newMethodRef);
                           if (this.isVerbose()) {
                              logger.info("Method call \"{}\" is being replaced with \"{}\" in {}.", methodRef.getSignature(), newMethodRef.getSignature(), method.getSignature());
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      Scene.v().releaseActiveHierarchy();
      Scene.v().setFastHierarchy(new FastHierarchy());
      if (this.isVerbose()) {
         logger.info("Transforming method names is completed.");
      }

   }

   public String getNewName() {
      int size = 5;
      int tries = 0;

      String newName;
      for(newName = this.nameGenerator.generateName(size); this.isNotUnique(newName) || BodyBuilder.nameList.contains(newName); newName = this.nameGenerator.generateName(size)) {
         if (tries++ > size) {
            ++size;
            tries = 0;
         }
      }

      BodyBuilder.nameList.add(newName);
      return newName;
   }

   private boolean isRenamingAllowed(SootMethod method) {
      if (Main.getWeight("wjtp.jbco_mr", method.getName()) == 0) {
         return false;
      } else {
         String subSignature = method.getSubSignature();
         if (MAIN_METHOD_SUB_SIGNATURE.equals(subSignature) && method.isPublic() && method.isStatic()) {
            if (this.isVerbose()) {
               logger.info((String)"Skipping renaming \"{}\" method as it is main one.", (Object)subSignature);
            }

            return false;
         } else if (!method.getName().equals("<init>") && !method.getName().equals("<clinit>")) {
            return true;
         } else {
            if (this.isVerbose()) {
               logger.info((String)"Skipping renaming \"{}\" method as it is constructor or static initializer.", (Object)subSignature);
            }

            return false;
         }
      }
   }

   private boolean isNotUnique(String methodName) {
      Stream var10000 = this.classToRenamingMap.values().stream().map(Map::values).flatMap(Collection::stream);
      methodName.getClass();
      return var10000.anyMatch(methodName::equals);
   }

   private Set<SootClass> uniteWithApplicationParents(SootClass applicationClass, Collection<SootClass> classes) {
      Set<SootClass> parents = this.getApplicationParents(applicationClass);
      Set<SootClass> result = new HashSet(parents.size() + classes.size());
      result.addAll(parents);
      result.addAll(classes);
      return result;
   }

   private Optional<SootClass> findDeclaringLibraryClass(Collection<SootClass> classes, SootMethodRef methodRef) {
      return classes.stream().filter(SootClass::isLibraryClass).filter((sootClass) -> {
         return this.isDeclared(sootClass, methodRef.name(), methodRef.parameterTypes());
      }).findAny();
   }

   private Set<SootClass> getDeclaringClasses(SootClass applicationClass, SootMethod method) {
      return (Set)this.getTree(applicationClass).stream().filter((sootClass) -> {
         return this.isDeclared(sootClass, method.getName(), method.getParameterTypes());
      }).collect(Collectors.toSet());
   }

   private Set<SootClass> getTree(SootClass applicationClass) {
      Set<SootClass> children = this.getChildrenOfIncluding(this.getParentsOfIncluding(applicationClass));
      boolean var3 = false;

      int count;
      do {
         count = children.size();
         children.addAll(this.getChildrenOfIncluding(this.getParentsOfIncluding((Collection)children)));
      } while(count < children.size());

      return children;
   }

   private Set<SootClass> getParents(SootClass applicationClass) {
      Set<SootClass> parents = new HashSet(this.getParentsOfIncluding(applicationClass));
      boolean var3 = false;

      int count;
      do {
         count = parents.size();
         parents.addAll(this.getParentsOfIncluding((Collection)parents));
      } while(count < parents.size());

      return parents;
   }

   private Set<SootClass> getApplicationParents(SootClass applicationClass) {
      return (Set)this.getParents(applicationClass).stream().filter((parent) -> {
         return !parent.isLibraryClass();
      }).collect(Collectors.toSet());
   }

   private List<SootClass> getParentsOfIncluding(SootClass applicationClass) {
      List<SootClass> result = HierarchyUtils.getAllInterfacesOf(applicationClass);
      result.addAll(applicationClass.isInterface() ? Scene.v().getActiveHierarchy().getSuperinterfacesOfIncluding(applicationClass) : Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(applicationClass));
      return result;
   }

   private Set<SootClass> getChildrenOfIncluding(Collection<SootClass> classes) {
      return (Set)Stream.concat(classes.stream().filter((c) -> {
         return !c.getName().equals("java.lang.Object");
      }).map((c) -> {
         return c.isInterface() ? Scene.v().getActiveHierarchy().getImplementersOf(c) : Scene.v().getActiveHierarchy().getSubclassesOf(c);
      }).flatMap(Collection::stream), classes.stream()).collect(Collectors.toSet());
   }

   private Set<SootClass> getParentsOfIncluding(Collection<SootClass> classes) {
      return (Set)classes.stream().map((sootClass) -> {
         return sootClass.isInterface() ? Scene.v().getActiveHierarchy().getSuperinterfacesOfIncluding(sootClass) : Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(sootClass);
      }).flatMap(Collection::stream).collect(Collectors.toSet());
   }

   private String getNewName(Collection<SootClass> classes, String name) {
      Set<String> names = (Set)this.classToRenamingMap.entrySet().stream().filter((entry) -> {
         return classes.contains(entry.getKey());
      }).map(Entry::getValue).map(Map::entrySet).flatMap(Collection::stream).filter((entry) -> {
         return ((String)entry.getKey()).equals(name);
      }).map(Entry::getValue).collect(Collectors.toSet());
      if (names.size() > 1) {
         logger.warn("Found {} names for method \"{}\": {}.", names.size(), name, String.join(", ", names));
      }

      return names.isEmpty() ? null : (String)names.iterator().next();
   }

   private boolean isDeclared(SootClass sootClass, String methodName, List<Type> parameterTypes) {
      Iterator var4 = sootClass.getMethods().iterator();

      SootMethod declared;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         declared = (SootMethod)var4.next();
      } while(!declared.getName().equals(methodName) || declared.getParameterCount() != parameterTypes.size());

      return true;
   }

   private static Body getActiveBodySafely(SootMethod method) {
      try {
         return method.getActiveBody();
      } catch (Exception var2) {
         logger.warn((String)"Getting Body from SootMethod {} caused exception that was suppressed.", (Throwable)var2);
         return null;
      }
   }
}
