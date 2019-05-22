package soot.jimple.toolkits.reflection;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.tagkit.Host;
import soot.tagkit.LineNumberTag;
import soot.tagkit.SourceLnPosTag;

public class ReflectionTraceInfo {
   private static final Logger logger = LoggerFactory.getLogger(ReflectionTraceInfo.class);
   protected Map<SootMethod, Set<String>> classForNameReceivers = new LinkedHashMap();
   protected Map<SootMethod, Set<String>> classNewInstanceReceivers = new LinkedHashMap();
   protected Map<SootMethod, Set<String>> constructorNewInstanceReceivers = new LinkedHashMap();
   protected Map<SootMethod, Set<String>> methodInvokeReceivers = new LinkedHashMap();
   protected Map<SootMethod, Set<String>> fieldSetReceivers = new LinkedHashMap();
   protected Map<SootMethod, Set<String>> fieldGetReceivers = new LinkedHashMap();

   public ReflectionTraceInfo(String logFile) {
      if (logFile == null) {
         throw new InternalError("Trace based refection model enabled but no trace file given!?");
      } else {
         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
            int lines = 0;
            HashSet ignoredKinds = new HashSet();

            while(true) {
               String line;
               String kind;
               do {
                  if ((line = reader.readLine()) == null) {
                     if (!ignoredKinds.isEmpty()) {
                        logger.debug("Encountered reflective calls entries of the following kinds that\ncannot currently be handled:");
                        Iterator var17 = ignoredKinds.iterator();

                        while(var17.hasNext()) {
                           kind = (String)var17.next();
                           logger.debug("" + kind);
                        }
                     }

                     return;
                  }
               } while(line.length() == 0);

               String[] portions = line.split(";", -1);
               kind = portions[0];
               String target = portions[1];
               String source = portions[2];
               int lineNumber = portions[3].length() == 0 ? -1 : Integer.parseInt(portions[3]);
               Set<SootMethod> possibleSourceMethods = this.inferSource(source, lineNumber);
               Iterator var12 = possibleSourceMethods.iterator();

               while(var12.hasNext()) {
                  SootMethod sourceMethod = (SootMethod)var12.next();
                  Object receiverNames;
                  if (kind.equals("Class.forName")) {
                     if ((receiverNames = (Set)this.classForNameReceivers.get(sourceMethod)) == null) {
                        this.classForNameReceivers.put(sourceMethod, receiverNames = new LinkedHashSet());
                     }

                     ((Set)receiverNames).add(target);
                  } else if (kind.equals("Class.newInstance")) {
                     if ((receiverNames = (Set)this.classNewInstanceReceivers.get(sourceMethod)) == null) {
                        this.classNewInstanceReceivers.put(sourceMethod, receiverNames = new LinkedHashSet());
                     }

                     ((Set)receiverNames).add(target);
                  } else if (kind.equals("Method.invoke")) {
                     if (!Scene.v().containsMethod(target)) {
                        throw new RuntimeException("Unknown method for signature: " + target);
                     }

                     if ((receiverNames = (Set)this.methodInvokeReceivers.get(sourceMethod)) == null) {
                        this.methodInvokeReceivers.put(sourceMethod, receiverNames = new LinkedHashSet());
                     }

                     ((Set)receiverNames).add(target);
                  } else if (kind.equals("Constructor.newInstance")) {
                     if (!Scene.v().containsMethod(target)) {
                        throw new RuntimeException("Unknown method for signature: " + target);
                     }

                     if ((receiverNames = (Set)this.constructorNewInstanceReceivers.get(sourceMethod)) == null) {
                        this.constructorNewInstanceReceivers.put(sourceMethod, receiverNames = new LinkedHashSet());
                     }

                     ((Set)receiverNames).add(target);
                  } else if (kind.equals("Field.set*")) {
                     if (!Scene.v().containsField(target)) {
                        throw new RuntimeException("Unknown method for signature: " + target);
                     }

                     if ((receiverNames = (Set)this.fieldSetReceivers.get(sourceMethod)) == null) {
                        this.fieldSetReceivers.put(sourceMethod, receiverNames = new LinkedHashSet());
                     }

                     ((Set)receiverNames).add(target);
                  } else if (kind.equals("Field.get*")) {
                     if (!Scene.v().containsField(target)) {
                        throw new RuntimeException("Unknown method for signature: " + target);
                     }

                     if ((receiverNames = (Set)this.fieldGetReceivers.get(sourceMethod)) == null) {
                        this.fieldGetReceivers.put(sourceMethod, receiverNames = new LinkedHashSet());
                     }

                     ((Set)receiverNames).add(target);
                  } else {
                     ignoredKinds.add(kind);
                  }
               }

               ++lines;
            }
         } catch (FileNotFoundException var15) {
            throw new RuntimeException("Trace file not found.", var15);
         } catch (IOException var16) {
            throw new RuntimeException(var16);
         }
      }
   }

   private Set<SootMethod> inferSource(String source, int lineNumber) {
      String className = source.substring(0, source.lastIndexOf("."));
      String methodName = source.substring(source.lastIndexOf(".") + 1);
      if (!Scene.v().containsClass(className)) {
         Scene.v().addBasicClass(className, 3);
         Scene.v().loadBasicClasses();
         if (!Scene.v().containsClass(className)) {
            throw new RuntimeException("Trace file refers to unknown class: " + className);
         }
      }

      SootClass sootClass = Scene.v().getSootClass(className);
      Set<SootMethod> methodsWithRightName = new LinkedHashSet();
      Iterator var7 = sootClass.getMethods().iterator();

      SootMethod sootMethod;
      while(var7.hasNext()) {
         sootMethod = (SootMethod)var7.next();
         if (sootMethod.isConcrete() && sootMethod.getName().equals(methodName)) {
            methodsWithRightName.add(sootMethod);
         }
      }

      if (methodsWithRightName.isEmpty()) {
         throw new RuntimeException("Trace file refers to unknown method with name " + methodName + " in Class " + className);
      } else if (methodsWithRightName.size() == 1) {
         return Collections.singleton(methodsWithRightName.iterator().next());
      } else {
         var7 = methodsWithRightName.iterator();

         while(true) {
            do {
               if (!var7.hasNext()) {
                  return methodsWithRightName;
               }

               sootMethod = (SootMethod)var7.next();
               if (this.coversLineNumber(lineNumber, sootMethod)) {
                  return Collections.singleton(sootMethod);
               }
            } while(!sootMethod.isConcrete());

            if (!sootMethod.hasActiveBody()) {
               sootMethod.retrieveActiveBody();
            }

            Body body = sootMethod.getActiveBody();
            if (this.coversLineNumber(lineNumber, body)) {
               return Collections.singleton(sootMethod);
            }

            Iterator var10 = body.getUnits().iterator();

            while(var10.hasNext()) {
               Unit u = (Unit)var10.next();
               if (this.coversLineNumber(lineNumber, u)) {
                  return Collections.singleton(sootMethod);
               }
            }
         }
      }
   }

   private boolean coversLineNumber(int lineNumber, Host host) {
      SourceLnPosTag tag = (SourceLnPosTag)host.getTag("SourceLnPosTag");
      if (tag != null && tag.startLn() <= lineNumber && tag.endLn() >= lineNumber) {
         return true;
      } else {
         LineNumberTag tag = (LineNumberTag)host.getTag("LineNumberTag");
         return tag != null && tag.getLineNumber() == lineNumber;
      }
   }

   public Set<String> classForNameClassNames(SootMethod container) {
      return !this.classForNameReceivers.containsKey(container) ? Collections.emptySet() : (Set)this.classForNameReceivers.get(container);
   }

   public Set<SootClass> classForNameClasses(SootMethod container) {
      Set<SootClass> result = new LinkedHashSet();
      Iterator var3 = this.classForNameClassNames(container).iterator();

      while(var3.hasNext()) {
         String className = (String)var3.next();
         result.add(Scene.v().getSootClass(className));
      }

      return result;
   }

   public Set<String> classNewInstanceClassNames(SootMethod container) {
      return !this.classNewInstanceReceivers.containsKey(container) ? Collections.emptySet() : (Set)this.classNewInstanceReceivers.get(container);
   }

   public Set<SootClass> classNewInstanceClasses(SootMethod container) {
      Set<SootClass> result = new LinkedHashSet();
      Iterator var3 = this.classNewInstanceClassNames(container).iterator();

      while(var3.hasNext()) {
         String className = (String)var3.next();
         result.add(Scene.v().getSootClass(className));
      }

      return result;
   }

   public Set<String> constructorNewInstanceSignatures(SootMethod container) {
      return !this.constructorNewInstanceReceivers.containsKey(container) ? Collections.emptySet() : (Set)this.constructorNewInstanceReceivers.get(container);
   }

   public Set<SootMethod> constructorNewInstanceConstructors(SootMethod container) {
      Set<SootMethod> result = new LinkedHashSet();
      Iterator var3 = this.constructorNewInstanceSignatures(container).iterator();

      while(var3.hasNext()) {
         String signature = (String)var3.next();
         result.add(Scene.v().getMethod(signature));
      }

      return result;
   }

   public Set<String> methodInvokeSignatures(SootMethod container) {
      return !this.methodInvokeReceivers.containsKey(container) ? Collections.emptySet() : (Set)this.methodInvokeReceivers.get(container);
   }

   public Set<SootMethod> methodInvokeMethods(SootMethod container) {
      Set<SootMethod> result = new LinkedHashSet();
      Iterator var3 = this.methodInvokeSignatures(container).iterator();

      while(var3.hasNext()) {
         String signature = (String)var3.next();
         result.add(Scene.v().getMethod(signature));
      }

      return result;
   }

   public Set<SootMethod> methodsContainingReflectiveCalls() {
      Set<SootMethod> res = new LinkedHashSet();
      res.addAll(this.classForNameReceivers.keySet());
      res.addAll(this.classNewInstanceReceivers.keySet());
      res.addAll(this.constructorNewInstanceReceivers.keySet());
      res.addAll(this.methodInvokeReceivers.keySet());
      return res;
   }

   public Set<String> fieldSetSignatures(SootMethod container) {
      return !this.fieldSetReceivers.containsKey(container) ? Collections.emptySet() : (Set)this.fieldSetReceivers.get(container);
   }

   public Set<String> fieldGetSignatures(SootMethod container) {
      return !this.fieldGetReceivers.containsKey(container) ? Collections.emptySet() : (Set)this.fieldGetReceivers.get(container);
   }

   public static enum Kind {
      ClassForName,
      ClassNewInstance,
      ConstructorNewInstance,
      MethodInvoke,
      FieldSet,
      FieldGet;
   }
}
