package soot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import soot.util.NumberedString;

public class EntryPoints {
   final NumberedString sigMain = Scene.v().getSubSigNumberer().findOrAdd("void main(java.lang.String[])");
   final NumberedString sigFinalize = Scene.v().getSubSigNumberer().findOrAdd("void finalize()");
   final NumberedString sigExit = Scene.v().getSubSigNumberer().findOrAdd("void exit()");
   final NumberedString sigClinit = Scene.v().getSubSigNumberer().findOrAdd("void <clinit>()");
   final NumberedString sigInit = Scene.v().getSubSigNumberer().findOrAdd("void <init>()");
   final NumberedString sigStart = Scene.v().getSubSigNumberer().findOrAdd("void start()");
   final NumberedString sigRun = Scene.v().getSubSigNumberer().findOrAdd("void run()");
   final NumberedString sigObjRun = Scene.v().getSubSigNumberer().findOrAdd("java.lang.Object run()");
   final NumberedString sigForName = Scene.v().getSubSigNumberer().findOrAdd("java.lang.Class forName(java.lang.String)");

   public EntryPoints(Singletons.Global g) {
   }

   public static EntryPoints v() {
      return G.v().soot_EntryPoints();
   }

   protected void addMethod(List<SootMethod> set, SootClass cls, NumberedString methodSubSig) {
      SootMethod sm = cls.getMethodUnsafe(methodSubSig);
      if (sm != null) {
         set.add(sm);
      }

   }

   protected void addMethod(List<SootMethod> set, String methodSig) {
      if (Scene.v().containsMethod(methodSig)) {
         set.add(Scene.v().getMethod(methodSig));
      }

   }

   public List<SootMethod> application() {
      List<SootMethod> ret = new ArrayList();
      if (Scene.v().hasMainClass()) {
         this.addMethod(ret, Scene.v().getMainClass(), this.sigMain);
         Iterator var2 = this.clinitsOf(Scene.v().getMainClass()).iterator();

         while(var2.hasNext()) {
            SootMethod clinit = (SootMethod)var2.next();
            ret.add(clinit);
         }
      }

      return ret;
   }

   public List<SootMethod> implicit() {
      List<SootMethod> ret = new ArrayList();
      this.addMethod(ret, "<java.lang.System: void initializeSystemClass()>");
      this.addMethod(ret, "<java.lang.ThreadGroup: void <init>()>");
      this.addMethod(ret, "<java.lang.Thread: void exit()>");
      this.addMethod(ret, "<java.lang.ThreadGroup: void uncaughtException(java.lang.Thread,java.lang.Throwable)>");
      this.addMethod(ret, "<java.lang.ClassLoader: void <init>()>");
      this.addMethod(ret, "<java.lang.ClassLoader: java.lang.Class loadClassInternal(java.lang.String)>");
      this.addMethod(ret, "<java.lang.ClassLoader: void checkPackageAccess(java.lang.Class,java.security.ProtectionDomain)>");
      this.addMethod(ret, "<java.lang.ClassLoader: void addClass(java.lang.Class)>");
      this.addMethod(ret, "<java.lang.ClassLoader: long findNative(java.lang.ClassLoader,java.lang.String)>");
      this.addMethod(ret, "<java.security.PrivilegedActionException: void <init>(java.lang.Exception)>");
      this.addMethod(ret, "<java.lang.ref.Finalizer: void runFinalizer()>");
      this.addMethod(ret, "<java.lang.Thread: void <init>(java.lang.ThreadGroup,java.lang.Runnable)>");
      this.addMethod(ret, "<java.lang.Thread: void <init>(java.lang.ThreadGroup,java.lang.String)>");
      return ret;
   }

   public List<SootMethod> all() {
      List<SootMethod> ret = new ArrayList();
      ret.addAll(this.application());
      ret.addAll(this.implicit());
      return ret;
   }

   public List<SootMethod> clinits() {
      List<SootMethod> ret = new ArrayList();
      Iterator clIt = Scene.v().getClasses().iterator();

      while(clIt.hasNext()) {
         SootClass cl = (SootClass)clIt.next();
         this.addMethod(ret, cl, this.sigClinit);
      }

      return ret;
   }

   public List<SootMethod> inits() {
      List<SootMethod> ret = new ArrayList();
      Iterator clIt = Scene.v().getClasses().iterator();

      while(clIt.hasNext()) {
         SootClass cl = (SootClass)clIt.next();
         this.addMethod(ret, cl, this.sigInit);
      }

      return ret;
   }

   public List<SootMethod> allInits() {
      List<SootMethod> ret = new ArrayList();
      Iterator clIt = Scene.v().getClasses().iterator();

      while(clIt.hasNext()) {
         SootClass cl = (SootClass)clIt.next();
         Iterator var4 = cl.getMethods().iterator();

         while(var4.hasNext()) {
            SootMethod m = (SootMethod)var4.next();
            if (m.getName().equals("<init>")) {
               ret.add(m);
            }
         }
      }

      return ret;
   }

   public List<SootMethod> methodsOfApplicationClasses() {
      List<SootMethod> ret = new ArrayList();
      Iterator clIt = Scene.v().getApplicationClasses().iterator();

      while(clIt.hasNext()) {
         SootClass cl = (SootClass)clIt.next();
         Iterator mIt = cl.getMethods().iterator();

         while(mIt.hasNext()) {
            SootMethod m = (SootMethod)mIt.next();
            if (m.isConcrete()) {
               ret.add(m);
            }
         }
      }

      return ret;
   }

   public List<SootMethod> mainsOfApplicationClasses() {
      List<SootMethod> ret = new ArrayList();
      Iterator clIt = Scene.v().getApplicationClasses().iterator();

      while(clIt.hasNext()) {
         SootClass cl = (SootClass)clIt.next();
         SootMethod m = cl.getMethodUnsafe("void main(java.lang.String[])");
         if (m != null && m.isConcrete()) {
            ret.add(m);
         }
      }

      return ret;
   }

   public Iterable<SootMethod> clinitsOf(SootClass cl) {
      final SootMethod initStart = cl.getMethodUnsafe(this.sigClinit);
      return (Iterable)(initStart == null ? Collections.emptyList() : new Iterable<SootMethod>() {
         public Iterator<SootMethod> iterator() {
            return new Iterator<SootMethod>() {
               SootMethod current = initStart;

               public SootMethod next() {
                  if (!this.hasNext()) {
                     throw new NoSuchElementException();
                  } else {
                     SootMethod n = this.current;
                     this.current = null;
                     SootClass currentClass = n.getDeclaringClass();

                     while(true) {
                        SootClass superClass = currentClass.getSuperclassUnsafe();
                        if (superClass == null) {
                           break;
                        }

                        SootMethod m = superClass.getMethodUnsafe(EntryPoints.this.sigClinit);
                        if (m != null) {
                           this.current = m;
                           break;
                        }

                        currentClass = superClass;
                     }

                     return n;
                  }
               }

               public boolean hasNext() {
                  return this.current != null;
               }
            };
         }
      });
   }
}
