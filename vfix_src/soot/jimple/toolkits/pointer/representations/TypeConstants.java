package soot.jimple.toolkits.pointer.representations;

import soot.AnySubType;
import soot.G;
import soot.PhaseOptions;
import soot.RefType;
import soot.Singletons;
import soot.Type;
import soot.options.CGOptions;

public class TypeConstants {
   public Type OBJECTCLASS;
   public Type STRINGCLASS;
   public Type CLASSLOADERCLASS;
   public Type PROCESSCLASS;
   public Type THREADCLASS;
   public Type CLASSCLASS;
   public Type LEASTCLASS;
   public Type FIELDCLASS;
   public Type METHODCLASS;
   public Type CONSTRUCTORCLASS;
   public Type FILESYSTEMCLASS;
   public Type PRIVILEGEDACTIONEXCEPTION;

   public static TypeConstants v() {
      return G.v().soot_jimple_toolkits_pointer_representations_TypeConstants();
   }

   public TypeConstants(Singletons.Global g) {
      int jdkver = (new CGOptions(PhaseOptions.v().getPhaseOptions("cg"))).jdkver();
      this.OBJECTCLASS = RefType.v("java.lang.Object");
      this.STRINGCLASS = RefType.v("java.lang.String");
      this.CLASSLOADERCLASS = AnySubType.v(RefType.v("java.lang.ClassLoader"));
      this.PROCESSCLASS = AnySubType.v(RefType.v("java.lang.Process"));
      this.THREADCLASS = AnySubType.v(RefType.v("java.lang.Thread"));
      this.CLASSCLASS = RefType.v("java.lang.Class");
      this.LEASTCLASS = AnySubType.v(RefType.v("java.lang.Object"));
      this.FIELDCLASS = RefType.v("java.lang.reflect.Field");
      this.METHODCLASS = RefType.v("java.lang.reflect.Method");
      this.CONSTRUCTORCLASS = RefType.v("java.lang.reflect.Constructor");
      if (jdkver >= 2) {
         this.FILESYSTEMCLASS = AnySubType.v(RefType.v("java.io.FileSystem"));
      }

      if (jdkver >= 2) {
         this.PRIVILEGEDACTIONEXCEPTION = AnySubType.v(RefType.v("java.security.PrivilegedActionException"));
      }

   }
}
