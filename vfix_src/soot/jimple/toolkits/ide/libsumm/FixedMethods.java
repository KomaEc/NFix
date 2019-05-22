package soot.jimple.toolkits.ide.libsumm;

import java.util.Iterator;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.InvokeExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;

public class FixedMethods {
   public static final boolean ASSUME_PACKAGES_SEALED = false;

   public static boolean isFixed(InvokeExpr ie) {
      return ie instanceof StaticInvokeExpr || ie instanceof SpecialInvokeExpr || !clientOverwriteableOverwrites(ie.getMethod());
   }

   private static boolean clientOverwriteableOverwrites(SootMethod m) {
      if (clientOverwriteable(m)) {
         return true;
      } else {
         SootClass c = m.getDeclaringClass();
         Iterator var2 = Scene.v().getFastHierarchy().getSubclassesOf(c).iterator();

         SootMethod mPrime;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            SootClass cPrime = (SootClass)var2.next();
            mPrime = cPrime.getMethodUnsafe(m.getSubSignature());
         } while(mPrime == null || !clientOverwriteable(mPrime));

         return true;
      }
   }

   private static boolean clientOverwriteable(SootMethod m) {
      SootClass c = m.getDeclaringClass();
      return !c.isFinal() && !m.isFinal() && visible(m) && clientCanInstantiate(c);
   }

   private static boolean clientCanInstantiate(SootClass cPrime) {
      if (cPrime.isInterface()) {
         return true;
      } else {
         Iterator var1 = cPrime.getMethods().iterator();

         SootMethod m;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            m = (SootMethod)var1.next();
         } while(!m.getName().equals("<init>") || !visible(m));

         return true;
      }
   }

   private static boolean visible(SootMethod mPrime) {
      SootClass cPrime = mPrime.getDeclaringClass();
      return (cPrime.isPublic() || cPrime.isProtected() || !cPrime.isPrivate()) && (mPrime.isPublic() || mPrime.isProtected() || !mPrime.isPrivate());
   }
}
