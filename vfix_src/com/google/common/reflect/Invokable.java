package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import javax.annotation.Nullable;

@Beta
public abstract class Invokable<T, R> extends Element implements GenericDeclaration {
   <M extends AccessibleObject & Member> Invokable(M member) {
      super(member);
   }

   public static Invokable<?, Object> from(Method method) {
      return new Invokable.MethodInvokable(method);
   }

   public static <T> Invokable<T, T> from(Constructor<T> constructor) {
      return new Invokable.ConstructorInvokable(constructor);
   }

   public abstract boolean isOverridable();

   public abstract boolean isVarArgs();

   public final R invoke(@Nullable T receiver, Object... args) throws InvocationTargetException, IllegalAccessException {
      return this.invokeInternal(receiver, (Object[])Preconditions.checkNotNull(args));
   }

   public final TypeToken<? extends R> getReturnType() {
      return TypeToken.of(this.getGenericReturnType());
   }

   public final ImmutableList<Parameter> getParameters() {
      Type[] parameterTypes = this.getGenericParameterTypes();
      Annotation[][] annotations = this.getParameterAnnotations();
      ImmutableList.Builder<Parameter> builder = ImmutableList.builder();

      for(int i = 0; i < parameterTypes.length; ++i) {
         builder.add((Object)(new Parameter(this, i, TypeToken.of(parameterTypes[i]), annotations[i])));
      }

      return builder.build();
   }

   public final ImmutableList<TypeToken<? extends Throwable>> getExceptionTypes() {
      ImmutableList.Builder<TypeToken<? extends Throwable>> builder = ImmutableList.builder();
      Type[] arr$ = this.getGenericExceptionTypes();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Type type = arr$[i$];
         TypeToken<? extends Throwable> exceptionType = TypeToken.of(type);
         builder.add((Object)exceptionType);
      }

      return builder.build();
   }

   public final <R1 extends R> Invokable<T, R1> returning(Class<R1> returnType) {
      return this.returning(TypeToken.of(returnType));
   }

   public final <R1 extends R> Invokable<T, R1> returning(TypeToken<R1> returnType) {
      if (!returnType.isAssignableFrom(this.getReturnType())) {
         throw new IllegalArgumentException("Invokable is known to return " + this.getReturnType() + ", not " + returnType);
      } else {
         return this;
      }
   }

   public final Class<? super T> getDeclaringClass() {
      return super.getDeclaringClass();
   }

   public TypeToken<T> getOwnerType() {
      return TypeToken.of(this.getDeclaringClass());
   }

   abstract Object invokeInternal(@Nullable Object var1, Object[] var2) throws InvocationTargetException, IllegalAccessException;

   abstract Type[] getGenericParameterTypes();

   abstract Type[] getGenericExceptionTypes();

   abstract Annotation[][] getParameterAnnotations();

   abstract Type getGenericReturnType();

   static class ConstructorInvokable<T> extends Invokable<T, T> {
      final Constructor<?> constructor;

      ConstructorInvokable(Constructor<?> constructor) {
         super(constructor);
         this.constructor = constructor;
      }

      final Object invokeInternal(@Nullable Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
         try {
            return this.constructor.newInstance(args);
         } catch (InstantiationException var4) {
            throw new RuntimeException(this.constructor + " failed.", var4);
         }
      }

      Type getGenericReturnType() {
         Class<?> declaringClass = this.getDeclaringClass();
         TypeVariable<?>[] typeParams = declaringClass.getTypeParameters();
         return (Type)(typeParams.length > 0 ? Types.newParameterizedType(declaringClass, typeParams) : declaringClass);
      }

      Type[] getGenericParameterTypes() {
         Type[] types = this.constructor.getGenericParameterTypes();
         if (types.length > 0 && this.mayNeedHiddenThis()) {
            Class<?>[] rawParamTypes = this.constructor.getParameterTypes();
            if (types.length == rawParamTypes.length && rawParamTypes[0] == this.getDeclaringClass().getEnclosingClass()) {
               return (Type[])Arrays.copyOfRange(types, 1, types.length);
            }
         }

         return types;
      }

      Type[] getGenericExceptionTypes() {
         return this.constructor.getGenericExceptionTypes();
      }

      final Annotation[][] getParameterAnnotations() {
         return this.constructor.getParameterAnnotations();
      }

      public final TypeVariable<?>[] getTypeParameters() {
         TypeVariable<?>[] declaredByClass = this.getDeclaringClass().getTypeParameters();
         TypeVariable<?>[] declaredByConstructor = this.constructor.getTypeParameters();
         TypeVariable<?>[] result = new TypeVariable[declaredByClass.length + declaredByConstructor.length];
         System.arraycopy(declaredByClass, 0, result, 0, declaredByClass.length);
         System.arraycopy(declaredByConstructor, 0, result, declaredByClass.length, declaredByConstructor.length);
         return result;
      }

      public final boolean isOverridable() {
         return false;
      }

      public final boolean isVarArgs() {
         return this.constructor.isVarArgs();
      }

      private boolean mayNeedHiddenThis() {
         Class<?> declaringClass = this.constructor.getDeclaringClass();
         if (declaringClass.getEnclosingConstructor() != null) {
            return true;
         } else {
            Method enclosingMethod = declaringClass.getEnclosingMethod();
            if (enclosingMethod != null) {
               return !Modifier.isStatic(enclosingMethod.getModifiers());
            } else {
               return declaringClass.getEnclosingClass() != null && !Modifier.isStatic(declaringClass.getModifiers());
            }
         }
      }
   }

   static class MethodInvokable<T> extends Invokable<T, Object> {
      final Method method;

      MethodInvokable(Method method) {
         super(method);
         this.method = method;
      }

      final Object invokeInternal(@Nullable Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
         return this.method.invoke(receiver, args);
      }

      Type getGenericReturnType() {
         return this.method.getGenericReturnType();
      }

      Type[] getGenericParameterTypes() {
         return this.method.getGenericParameterTypes();
      }

      Type[] getGenericExceptionTypes() {
         return this.method.getGenericExceptionTypes();
      }

      final Annotation[][] getParameterAnnotations() {
         return this.method.getParameterAnnotations();
      }

      public final TypeVariable<?>[] getTypeParameters() {
         return this.method.getTypeParameters();
      }

      public final boolean isOverridable() {
         return !this.isFinal() && !this.isPrivate() && !this.isStatic() && !Modifier.isFinal(this.getDeclaringClass().getModifiers());
      }

      public final boolean isVarArgs() {
         return this.method.isVarArgs();
      }
   }
}
