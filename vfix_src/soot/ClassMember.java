package soot;

public interface ClassMember {
   SootClass getDeclaringClass();

   boolean isDeclared();

   boolean isPhantom();

   void setPhantom(boolean var1);

   boolean isProtected();

   boolean isPrivate();

   boolean isPublic();

   boolean isStatic();

   void setModifiers(int var1);

   int getModifiers();
}
