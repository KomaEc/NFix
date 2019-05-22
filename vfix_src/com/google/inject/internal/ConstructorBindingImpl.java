package com.google.inject.internal;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.internal.util.Classes;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.ConstructorBinding;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

final class ConstructorBindingImpl<T> extends BindingImpl<T> implements ConstructorBinding<T>, DelayedInitialize {
   private final ConstructorBindingImpl.Factory<T> factory;
   private final InjectionPoint constructorInjectionPoint;

   private ConstructorBindingImpl(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> scopedFactory, Scoping scoping, ConstructorBindingImpl.Factory<T> factory, InjectionPoint constructorInjectionPoint) {
      super(injector, key, source, scopedFactory, scoping);
      this.factory = factory;
      this.constructorInjectionPoint = constructorInjectionPoint;
   }

   public ConstructorBindingImpl(Key<T> key, Object source, Scoping scoping, InjectionPoint constructorInjectionPoint, Set<InjectionPoint> injectionPoints) {
      super(source, key, scoping);
      this.factory = new ConstructorBindingImpl.Factory(false, key);
      ConstructionProxy<T> constructionProxy = (new DefaultConstructionProxyFactory(constructorInjectionPoint)).create();
      this.constructorInjectionPoint = constructorInjectionPoint;
      this.factory.constructorInjector = new ConstructorInjector(injectionPoints, constructionProxy, (SingleParameterInjector[])null, (MembersInjectorImpl)null);
   }

   static <T> ConstructorBindingImpl<T> create(InjectorImpl injector, Key<T> key, InjectionPoint constructorInjector, Object source, Scoping scoping, Errors errors, boolean failIfNotLinked, boolean failIfNotExplicit) throws ErrorsException {
      int numErrors = errors.size();
      Class<? super T> rawType = constructorInjector == null ? key.getTypeLiteral().getRawType() : constructorInjector.getDeclaringType().getRawType();
      if (Modifier.isAbstract(rawType.getModifiers())) {
         errors.missingImplementation(key);
      }

      if (Classes.isInnerClass(rawType)) {
         errors.cannotInjectInnerClass(rawType);
      }

      errors.throwIfNewErrors(numErrors);
      if (constructorInjector == null) {
         try {
            constructorInjector = InjectionPoint.forConstructorOf(key.getTypeLiteral());
            if (failIfNotExplicit && !hasAtInject((Constructor)constructorInjector.getMember())) {
               errors.atInjectRequired(rawType);
            }
         } catch (ConfigurationException var12) {
            throw errors.merge(var12.getErrorMessages()).toException();
         }
      }

      if (!scoping.isExplicitlyScoped()) {
         Class<?> annotatedType = constructorInjector.getMember().getDeclaringClass();
         Class<? extends Annotation> scopeAnnotation = Annotations.findScopeAnnotation(errors, annotatedType);
         if (scopeAnnotation != null) {
            scoping = Scoping.makeInjectable(Scoping.forAnnotation(scopeAnnotation), injector, errors.withSource(rawType));
         }
      }

      errors.throwIfNewErrors(numErrors);
      ConstructorBindingImpl.Factory<T> factoryFactory = new ConstructorBindingImpl.Factory(failIfNotLinked, key);
      InternalFactory<? extends T> scopedFactory = Scoping.scope(key, injector, factoryFactory, source, scoping);
      return new ConstructorBindingImpl(injector, key, source, scopedFactory, scoping, factoryFactory, constructorInjector);
   }

   private static boolean hasAtInject(Constructor cxtor) {
      return cxtor.isAnnotationPresent(Inject.class) || cxtor.isAnnotationPresent(javax.inject.Inject.class);
   }

   public void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
      this.factory.constructorInjector = injector.constructors.get(this.constructorInjectionPoint, errors);
      this.factory.provisionCallback = injector.provisionListenerStore.get(this);
   }

   boolean isInitialized() {
      return this.factory.constructorInjector != null;
   }

   InjectionPoint getInternalConstructor() {
      return this.factory.constructorInjector != null ? this.factory.constructorInjector.getConstructionProxy().getInjectionPoint() : this.constructorInjectionPoint;
   }

   Set<Dependency<?>> getInternalDependencies() {
      ImmutableSet.Builder<InjectionPoint> builder = ImmutableSet.builder();
      if (this.factory.constructorInjector == null) {
         builder.add((Object)this.constructorInjectionPoint);

         try {
            builder.addAll((Iterable)InjectionPoint.forInstanceMethodsAndFields(this.constructorInjectionPoint.getDeclaringType()));
         } catch (ConfigurationException var3) {
         }
      } else {
         builder.add((Object)this.getConstructor()).addAll((Iterable)this.getInjectableMembers());
      }

      return Dependency.forInjectionPoints(builder.build());
   }

   public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
      Preconditions.checkState(this.factory.constructorInjector != null, "not initialized");
      return visitor.visit((ConstructorBinding)this);
   }

   public InjectionPoint getConstructor() {
      Preconditions.checkState(this.factory.constructorInjector != null, "Binding is not ready");
      return this.factory.constructorInjector.getConstructionProxy().getInjectionPoint();
   }

   public Set<InjectionPoint> getInjectableMembers() {
      Preconditions.checkState(this.factory.constructorInjector != null, "Binding is not ready");
      return this.factory.constructorInjector.getInjectableMembers();
   }

   public Set<Dependency<?>> getDependencies() {
      return Dependency.forInjectionPoints((new ImmutableSet.Builder()).add((Object)this.getConstructor()).addAll((Iterable)this.getInjectableMembers()).build());
   }

   protected BindingImpl<T> withScoping(Scoping scoping) {
      return new ConstructorBindingImpl((InjectorImpl)null, this.getKey(), this.getSource(), this.factory, scoping, this.factory, this.constructorInjectionPoint);
   }

   protected BindingImpl<T> withKey(Key<T> key) {
      return new ConstructorBindingImpl((InjectorImpl)null, key, this.getSource(), this.factory, this.getScoping(), this.factory, this.constructorInjectionPoint);
   }

   public void applyTo(Binder binder) {
      InjectionPoint constructor = this.getConstructor();
      this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()).toConstructor((Constructor)this.getConstructor().getMember(), constructor.getDeclaringType()));
   }

   public String toString() {
      return Objects.toStringHelper(ConstructorBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("scope", this.getScoping()).toString();
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ConstructorBindingImpl)) {
         return false;
      } else {
         ConstructorBindingImpl<?> o = (ConstructorBindingImpl)obj;
         return this.getKey().equals(o.getKey()) && this.getScoping().equals(o.getScoping()) && Objects.equal(this.constructorInjectionPoint, o.constructorInjectionPoint);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.getKey(), this.getScoping(), this.constructorInjectionPoint);
   }

   private static class Factory<T> implements InternalFactory<T> {
      private final boolean failIfNotLinked;
      private final Key<?> key;
      private ConstructorInjector<T> constructorInjector;
      private ProvisionListenerStackCallback<T> provisionCallback;

      Factory(boolean failIfNotLinked, Key<?> key) {
         this.failIfNotLinked = failIfNotLinked;
         this.key = key;
      }

      public T get(Errors errors, InternalContext context, Dependency<?> dependency, boolean linked) throws ErrorsException {
         Preconditions.checkState(this.constructorInjector != null, "Constructor not ready");
         if (this.failIfNotLinked && !linked) {
            throw errors.jitDisabled(this.key).toException();
         } else {
            return this.constructorInjector.construct(errors, context, dependency.getKey().getTypeLiteral().getRawType(), this.provisionCallback);
         }
      }
   }
}
