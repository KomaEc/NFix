package soot;

public interface SootFieldRef {
   SootClass declaringClass();

   String name();

   Type type();

   boolean isStatic();

   String getSignature();

   SootField resolve();
}
