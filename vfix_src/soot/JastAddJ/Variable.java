package soot.JastAddJ;

import java.util.Collection;

public interface Variable {
   String name();

   TypeDecl type();

   Collection<TypeDecl> throwTypes();

   boolean isParameter();

   boolean isClassVariable();

   boolean isInstanceVariable();

   boolean isMethodParameter();

   boolean isConstructorParameter();

   boolean isExceptionHandlerParameter();

   boolean isLocalVariable();

   boolean isFinal();

   boolean isVolatile();

   boolean isBlank();

   boolean isStatic();

   boolean isSynthetic();

   TypeDecl hostType();

   Expr getInit();

   boolean hasInit();

   Constant constant();

   Modifiers getModifiers();

   Variable sourceVariableDecl();
}
