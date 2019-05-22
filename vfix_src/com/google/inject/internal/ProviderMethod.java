package com.google.inject.internal;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Exposed;
import com.google.inject.Key;
import com.google.inject.PrivateBinder;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.internal.util.StackTraceElements;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.ProviderInstanceBinding;
import com.google.inject.spi.ProviderWithExtensionVisitor;
import com.google.inject.spi.ProvidesMethodBinding;
import com.google.inject.spi.ProvidesMethodTargetVisitor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

public abstract class ProviderMethod<T> implements ProviderWithExtensionVisitor<T>, HasDependencies, ProvidesMethodBinding<T> {
   protected final Object instance;
   protected final Method method;
   private final Key<T> key;
   private final Class<? extends Annotation> scopeAnnotation;
   private final ImmutableSet<Dependency<?>> dependencies;
   private final List<Provider<?>> parameterProviders;
   private final boolean exposed;
   private final Annotation annotation;

   static <T> ProviderMethod<T> create(Key<T> key, Method method, Object instance, ImmutableSet<Dependency<?>> dependencies, List<Provider<?>> parameterProviders, Class<? extends Annotation> scopeAnnotation, boolean skipFastClassGeneration, Annotation annotation) {
      int modifiers = method.getModifiers();
      if (!Modifier.isPublic(modifiers) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
         method.setAccessible(true);
      }

      return new ProviderMethod.ReflectionProviderMethod(key, method, instance, dependencies, parameterProviders, scopeAnnotation, annotation);
   }

   private ProviderMethod(Key<T> key, Method method, Object instance, ImmutableSet<Dependency<?>> dependencies, List<Provider<?>> parameterProviders, Class<? extends Annotation> scopeAnnotation, Annotation annotation) {
      this.key = key;
      this.scopeAnnotation = scopeAnnotation;
      this.instance = instance;
      this.dependencies = dependencies;
      this.method = method;
      this.parameterProviders = parameterProviders;
      this.exposed = method.isAnnotationPresent(Exposed.class);
      this.annotation = annotation;
   }

   public Key<T> getKey() {
      return this.key;
   }

   public Method getMethod() {
      return this.method;
   }

   public Object getInstance() {
      return this.instance;
   }

   public Object getEnclosingInstance() {
      return this.instance;
   }

   public Annotation getAnnotation() {
      return this.annotation;
   }

   public void configure(Binder binder) {
      binder = binder.withSource(this.method);
      if (this.scopeAnnotation != null) {
         binder.bind(this.key).toProvider((Provider)this).in(this.scopeAnnotation);
      } else {
         binder.bind(this.key).toProvider((Provider)this);
      }

      if (this.exposed) {
         ((PrivateBinder)binder).expose(this.key);
      }

   }

   public T get() {
      Object[] parameters = new Object[this.parameterProviders.size()];

      for(int i = 0; i < parameters.length; ++i) {
         parameters[i] = ((Provider)this.parameterProviders.get(i)).get();
      }

      try {
         T result = this.doProvision(parameters);
         return result;
      } catch (IllegalAccessException var3) {
         throw new AssertionError(var3);
      } catch (InvocationTargetException var4) {
         throw Exceptions.rethrowCause(var4);
      }
   }

   abstract Object doProvision(Object[] var1) throws IllegalAccessException, InvocationTargetException;

   public Set<Dependency<?>> getDependencies() {
      return this.dependencies;
   }

   public <B, V> V acceptExtensionVisitor(BindingTargetVisitor<B, V> visitor, ProviderInstanceBinding<? extends B> binding) {
      return visitor instanceof ProvidesMethodTargetVisitor ? ((ProvidesMethodTargetVisitor)visitor).visit(this) : visitor.visit(binding);
   }

   public String toString() {
      String annotationString = this.annotation.toString();
      if (this.annotation.annotationType() == Provides.class) {
         annotationString = "@Provides";
      } else if (annotationString.endsWith("()")) {
         annotationString = annotationString.substring(0, annotationString.length() - 2);
      }

      String var2 = String.valueOf(String.valueOf(annotationString));
      String var3 = String.valueOf(String.valueOf(StackTraceElements.forMember(this.method)));
      return (new StringBuilder(1 + var2.length() + var3.length())).append(var2).append(" ").append(var3).toString();
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ProviderMethod)) {
         return false;
      } else {
         ProviderMethod<?> o = (ProviderMethod)obj;
         return this.method.equals(o.method) && this.instance.equals(o.instance) && this.annotation.equals(o.annotation);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.method, this.annotation);
   }

   // $FF: synthetic method
   ProviderMethod(Key x0, Method x1, Object x2, ImmutableSet x3, List x4, Class x5, Annotation x6, Object x7) {
      this(x0, x1, x2, x3, x4, x5, x6);
   }

   private static final class ReflectionProviderMethod<T> extends ProviderMethod<T> {
      ReflectionProviderMethod(Key<T> key, Method method, Object instance, ImmutableSet<Dependency<?>> dependencies, List<Provider<?>> parameterProviders, Class<? extends Annotation> scopeAnnotation, Annotation annotation) {
         super(key, method, instance, dependencies, parameterProviders, scopeAnnotation, annotation, null);
      }

      Object doProvision(Object[] parameters) throws IllegalAccessException, InvocationTargetException {
         return this.method.invoke(this.instance, parameters);
      }
   }
}
