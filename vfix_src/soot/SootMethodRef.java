package soot;

import java.util.List;
import soot.util.NumberedString;

public interface SootMethodRef {
   SootClass declaringClass();

   String name();

   List<Type> parameterTypes();

   Type returnType();

   boolean isStatic();

   NumberedString getSubSignature();

   String getSignature();

   Type parameterType(int var1);

   SootMethod resolve();

   SootMethod tryResolve();
}
