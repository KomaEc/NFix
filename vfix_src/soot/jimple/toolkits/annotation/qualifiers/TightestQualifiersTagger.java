package soot.jimple.toolkits.annotation.qualifiers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.G;
import soot.MethodOrMethodContext;
import soot.MethodToContexts;
import soot.Modifier;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Value;
import soot.ValueBox;
import soot.jimple.FieldRef;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.ColorTag;
import soot.tagkit.StringTag;

public class TightestQualifiersTagger extends SceneTransformer {
   public static final int RESULT_PUBLIC = 0;
   public static final int RESULT_PACKAGE = 1;
   public static final int RESULT_PROTECTED = 2;
   public static final int RESULT_PRIVATE = 3;
   private final HashMap<SootMethod, Integer> methodResultsMap = new HashMap();
   private final HashMap<SootField, Integer> fieldResultsMap = new HashMap();
   private MethodToContexts methodToContexts;

   public TightestQualifiersTagger(Singletons.Global g) {
   }

   public static TightestQualifiersTagger v() {
      return G.v().soot_jimple_toolkits_annotation_qualifiers_TightestQualifiersTagger();
   }

   protected void internalTransform(String phaseName, Map options) {
      this.handleMethods();
      this.handleFields();
   }

   private void handleMethods() {
      Iterator classesIt = Scene.v().getApplicationClasses().iterator();

      while(classesIt.hasNext()) {
         SootClass appClass = (SootClass)classesIt.next();
         Iterator methsIt = appClass.getMethods().iterator();

         while(methsIt.hasNext()) {
            SootMethod sm = (SootMethod)methsIt.next();
            if (Scene.v().getReachableMethods().contains(sm)) {
               this.analyzeMethod(sm);
            }
         }
      }

      Iterator methStatIt = this.methodResultsMap.keySet().iterator();

      while(methStatIt.hasNext()) {
         SootMethod meth = (SootMethod)methStatIt.next();
         int result = (Integer)this.methodResultsMap.get(meth);
         String sRes = "Public";
         if (result == 0) {
            sRes = "Public";
         } else if (result == 2) {
            sRes = "Protected";
         } else if (result == 1) {
            sRes = "Package";
         } else if (result == 3) {
            sRes = "Private";
         }

         String actual = null;
         if (Modifier.isPublic(meth.getModifiers())) {
            actual = "Public";
         } else if (Modifier.isProtected(meth.getModifiers())) {
            actual = "Protected";
         } else if (Modifier.isPrivate(meth.getModifiers())) {
            actual = "Private";
         } else {
            actual = "Package";
         }

         if (!sRes.equals(actual)) {
            if (meth.getName().equals("<init>")) {
               meth.addTag(new StringTag("Constructor: " + meth.getDeclaringClass().getName() + " has " + actual + " level access, can have: " + sRes + " level access.", "Tightest Qualifiers"));
            } else {
               meth.addTag(new StringTag("Method: " + meth.getName() + " has " + actual + " level access, can have: " + sRes + " level access.", "Tightest Qualifiers"));
            }

            meth.addTag(new ColorTag(255, 10, 0, true, "Tightest Qualifiers"));
         }
      }

   }

   private void analyzeMethod(SootMethod sm) {
      CallGraph cg = Scene.v().getCallGraph();
      if (this.methodToContexts == null) {
         this.methodToContexts = new MethodToContexts(Scene.v().getReachableMethods().listener());
      }

      Iterator momcIt = this.methodToContexts.get(sm).iterator();

      while(momcIt.hasNext()) {
         MethodOrMethodContext momc = (MethodOrMethodContext)momcIt.next();
         Iterator callerEdges = cg.edgesInto(momc);

         while(callerEdges.hasNext()) {
            Edge callEdge = (Edge)callerEdges.next();
            if (callEdge.isExplicit()) {
               SootMethod methodCaller = callEdge.src();
               SootClass callingClass = methodCaller.getDeclaringClass();
               if (Modifier.isPublic(sm.getModifiers())) {
                  this.analyzePublicMethod(sm, callingClass);
               } else if (Modifier.isProtected(sm.getModifiers())) {
                  this.analyzeProtectedMethod(sm, callingClass);
               } else if (!Modifier.isPrivate(sm.getModifiers())) {
                  this.analyzePackageMethod(sm, callingClass);
               }
            }
         }
      }

   }

   private boolean analyzeProtectedMethod(SootMethod sm, SootClass callingClass) {
      SootClass methodClass = sm.getDeclaringClass();
      boolean insidePackageAccess = this.isCallSamePackage(callingClass, methodClass);
      boolean subClassAccess = this.isCallClassSubClass(callingClass, methodClass);
      boolean sameClassAccess = this.isCallClassMethodClass(callingClass, methodClass);
      if (!insidePackageAccess && subClassAccess) {
         this.methodResultsMap.put(sm, new Integer(2));
         return true;
      } else if (insidePackageAccess && !sameClassAccess) {
         this.updateToPackage(sm);
         return false;
      } else {
         this.updateToPrivate(sm);
         return false;
      }
   }

   private boolean analyzePackageMethod(SootMethod sm, SootClass callingClass) {
      SootClass methodClass = sm.getDeclaringClass();
      boolean insidePackageAccess = this.isCallSamePackage(callingClass, methodClass);
      this.isCallClassSubClass(callingClass, methodClass);
      boolean sameClassAccess = this.isCallClassMethodClass(callingClass, methodClass);
      if (insidePackageAccess && !sameClassAccess) {
         this.updateToPackage(sm);
         return true;
      } else {
         this.updateToPrivate(sm);
         return false;
      }
   }

   private boolean analyzePublicMethod(SootMethod sm, SootClass callingClass) {
      SootClass methodClass = sm.getDeclaringClass();
      boolean insidePackageAccess = this.isCallSamePackage(callingClass, methodClass);
      boolean subClassAccess = this.isCallClassSubClass(callingClass, methodClass);
      boolean sameClassAccess = this.isCallClassMethodClass(callingClass, methodClass);
      if (!insidePackageAccess && !subClassAccess) {
         this.methodResultsMap.put(sm, new Integer(0));
         return true;
      } else if (!insidePackageAccess && subClassAccess) {
         this.updateToProtected(sm);
         return false;
      } else if (insidePackageAccess && !sameClassAccess) {
         this.updateToPackage(sm);
         return false;
      } else {
         this.updateToPrivate(sm);
         return false;
      }
   }

   private void updateToProtected(SootMethod sm) {
      if (!this.methodResultsMap.containsKey(sm)) {
         this.methodResultsMap.put(sm, new Integer(2));
      } else if ((Integer)this.methodResultsMap.get(sm) != 0) {
         this.methodResultsMap.put(sm, new Integer(2));
      }

   }

   private void updateToPackage(SootMethod sm) {
      if (!this.methodResultsMap.containsKey(sm)) {
         this.methodResultsMap.put(sm, new Integer(1));
      } else if ((Integer)this.methodResultsMap.get(sm) == 3) {
         this.methodResultsMap.put(sm, new Integer(1));
      }

   }

   private void updateToPrivate(SootMethod sm) {
      if (!this.methodResultsMap.containsKey(sm)) {
         this.methodResultsMap.put(sm, new Integer(3));
      }

   }

   private boolean isCallClassMethodClass(SootClass call, SootClass check) {
      return call.equals(check);
   }

   private boolean isCallClassSubClass(SootClass call, SootClass check) {
      if (!call.hasSuperclass()) {
         return false;
      } else {
         return call.getSuperclass().equals(check);
      }
   }

   private boolean isCallSamePackage(SootClass call, SootClass check) {
      return call.getPackageName().equals(check.getPackageName());
   }

   private void handleFields() {
      Iterator classesIt = Scene.v().getApplicationClasses().iterator();

      while(classesIt.hasNext()) {
         SootClass appClass = (SootClass)classesIt.next();
         Iterator fieldsIt = appClass.getFields().iterator();

         while(fieldsIt.hasNext()) {
            SootField sf = (SootField)fieldsIt.next();
            this.analyzeField(sf);
         }
      }

      Iterator fieldStatIt = this.fieldResultsMap.keySet().iterator();

      while(fieldStatIt.hasNext()) {
         SootField f = (SootField)fieldStatIt.next();
         int result = (Integer)this.fieldResultsMap.get(f);
         String sRes = "Public";
         if (result == 0) {
            sRes = "Public";
         } else if (result == 2) {
            sRes = "Protected";
         } else if (result == 1) {
            sRes = "Package";
         } else if (result == 3) {
            sRes = "Private";
         }

         String actual = null;
         if (Modifier.isPublic(f.getModifiers())) {
            actual = "Public";
         } else if (Modifier.isProtected(f.getModifiers())) {
            actual = "Protected";
         } else if (Modifier.isPrivate(f.getModifiers())) {
            actual = "Private";
         } else {
            actual = "Package";
         }

         if (!sRes.equals(actual)) {
            f.addTag(new StringTag("Field: " + f.getName() + " has " + actual + " level access, can have: " + sRes + " level access.", "Tightest Qualifiers"));
            f.addTag(new ColorTag(255, 10, 0, true, "Tightest Qualifiers"));
         }
      }

   }

   private void analyzeField(SootField sf) {
      Iterator classesIt = Scene.v().getApplicationClasses().iterator();

      label54:
      while(classesIt.hasNext()) {
         SootClass appClass = (SootClass)classesIt.next();
         Iterator mIt = appClass.getMethods().iterator();

         while(true) {
            SootMethod sm;
            do {
               do {
                  if (!mIt.hasNext()) {
                     continue label54;
                  }

                  sm = (SootMethod)mIt.next();
               } while(!sm.hasActiveBody());
            } while(!Scene.v().getReachableMethods().contains(sm));

            Body b = sm.getActiveBody();
            Iterator usesIt = b.getUseBoxes().iterator();

            while(usesIt.hasNext()) {
               ValueBox vBox = (ValueBox)usesIt.next();
               Value v = vBox.getValue();
               if (v instanceof FieldRef) {
                  FieldRef fieldRef = (FieldRef)v;
                  SootField f = fieldRef.getField();
                  if (f.equals(sf)) {
                     if (Modifier.isPublic(sf.getModifiers())) {
                        if (this.analyzePublicField(sf, appClass)) {
                           return;
                        }
                     } else if (Modifier.isProtected(sf.getModifiers())) {
                        this.analyzeProtectedField(sf, appClass);
                     } else if (!Modifier.isPrivate(sf.getModifiers())) {
                        this.analyzePackageField(sf, appClass);
                     }
                  }
               }
            }
         }
      }

   }

   private boolean analyzePublicField(SootField sf, SootClass callingClass) {
      SootClass fieldClass = sf.getDeclaringClass();
      boolean insidePackageAccess = this.isCallSamePackage(callingClass, fieldClass);
      boolean subClassAccess = this.isCallClassSubClass(callingClass, fieldClass);
      boolean sameClassAccess = this.isCallClassMethodClass(callingClass, fieldClass);
      if (!insidePackageAccess && !subClassAccess) {
         this.fieldResultsMap.put(sf, new Integer(0));
         return true;
      } else if (!insidePackageAccess && subClassAccess) {
         this.updateToProtected(sf);
         return false;
      } else if (insidePackageAccess && !sameClassAccess) {
         this.updateToPackage(sf);
         return false;
      } else {
         this.updateToPrivate(sf);
         return false;
      }
   }

   private boolean analyzeProtectedField(SootField sf, SootClass callingClass) {
      SootClass fieldClass = sf.getDeclaringClass();
      boolean insidePackageAccess = this.isCallSamePackage(callingClass, fieldClass);
      boolean subClassAccess = this.isCallClassSubClass(callingClass, fieldClass);
      boolean sameClassAccess = this.isCallClassMethodClass(callingClass, fieldClass);
      if (!insidePackageAccess && subClassAccess) {
         this.fieldResultsMap.put(sf, new Integer(2));
         return true;
      } else if (insidePackageAccess && !sameClassAccess) {
         this.updateToPackage(sf);
         return false;
      } else {
         this.updateToPrivate(sf);
         return false;
      }
   }

   private boolean analyzePackageField(SootField sf, SootClass callingClass) {
      SootClass fieldClass = sf.getDeclaringClass();
      boolean insidePackageAccess = this.isCallSamePackage(callingClass, fieldClass);
      this.isCallClassSubClass(callingClass, fieldClass);
      boolean sameClassAccess = this.isCallClassMethodClass(callingClass, fieldClass);
      if (insidePackageAccess && !sameClassAccess) {
         this.updateToPackage(sf);
         return true;
      } else {
         this.updateToPrivate(sf);
         return false;
      }
   }

   private void updateToProtected(SootField sf) {
      if (!this.fieldResultsMap.containsKey(sf)) {
         this.fieldResultsMap.put(sf, new Integer(2));
      } else if ((Integer)this.fieldResultsMap.get(sf) != 0) {
         this.fieldResultsMap.put(sf, new Integer(2));
      }

   }

   private void updateToPackage(SootField sf) {
      if (!this.fieldResultsMap.containsKey(sf)) {
         this.fieldResultsMap.put(sf, new Integer(1));
      } else if ((Integer)this.fieldResultsMap.get(sf) == 3) {
         this.fieldResultsMap.put(sf, new Integer(1));
      }

   }

   private void updateToPrivate(SootField sf) {
      if (!this.fieldResultsMap.containsKey(sf)) {
         this.fieldResultsMap.put(sf, new Integer(3));
      }

   }
}
