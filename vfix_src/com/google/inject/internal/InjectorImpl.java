package com.google.inject.internal;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.ImplementedBy;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.ProvidedBy;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.Scope;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.util.SourceProvider;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.ConvertedConstantBinding;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.ProviderBinding;
import com.google.inject.spi.TypeConverterBinding;
import com.google.inject.util.Providers;
import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

final class InjectorImpl implements Injector, Lookups {
   public static final TypeLiteral<String> STRING_TYPE = TypeLiteral.get(String.class);
   final State state;
   final InjectorImpl parent;
   final InjectorImpl.BindingsMultimap bindingsMultimap = new InjectorImpl.BindingsMultimap();
   final InjectorImpl.InjectorOptions options;
   final Map<Key<?>, BindingImpl<?>> jitBindings = Maps.newHashMap();
   final Set<Key<?>> failedJitBindings = Sets.newHashSet();
   Lookups lookups = new DeferredLookups(this);
   final ConstructorInjectorStore constructors = new ConstructorInjectorStore(this);
   MembersInjectorStore membersInjectorStore;
   ProvisionListenerCallbackStore provisionListenerStore;
   private final ThreadLocal<Object[]> localContext;
   private static final ConcurrentMap<Thread, InternalContext> globalInternalContext = Maps.newConcurrentMap();

   InjectorImpl(InjectorImpl parent, State state, InjectorImpl.InjectorOptions injectorOptions) {
      this.parent = parent;
      this.state = state;
      this.options = injectorOptions;
      if (parent != null) {
         this.localContext = parent.localContext;
      } else {
         this.localContext = new ThreadLocal();
      }

   }

   void index() {
      Iterator i$ = this.state.getExplicitBindingsThisLevel().values().iterator();

      while(i$.hasNext()) {
         Binding<?> binding = (Binding)i$.next();
         this.index(binding);
      }

   }

   <T> void index(Binding<T> binding) {
      this.bindingsMultimap.put(binding.getKey().getTypeLiteral(), binding);
   }

   public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> type) {
      return this.bindingsMultimap.getAll(type);
   }

   public <T> BindingImpl<T> getBinding(Key<T> key) {
      Errors errors = new Errors(key);

      try {
         BindingImpl<T> result = this.getBindingOrThrow(key, errors, InjectorImpl.JitLimitation.EXISTING_JIT);
         errors.throwConfigurationExceptionIfErrorsExist();
         return result;
      } catch (ErrorsException var4) {
         throw new ConfigurationException(errors.merge(var4.getErrors()).getMessages());
      }
   }

   public <T> BindingImpl<T> getExistingBinding(Key<T> key) {
      BindingImpl<T> explicitBinding = this.state.getExplicitBinding(key);
      if (explicitBinding != null) {
         return explicitBinding;
      } else {
         synchronized(this.state.lock()) {
            InjectorImpl injector = this;

            while(true) {
               if (injector == null) {
                  break;
               }

               BindingImpl<T> jitBinding = (BindingImpl)injector.jitBindings.get(key);
               if (jitBinding != null) {
                  return jitBinding;
               }

               injector = injector.parent;
            }
         }

         if (isProvider(key)) {
            try {
               Key<?> providedKey = getProvidedKey(key, new Errors());
               if (this.getExistingBinding(providedKey) != null) {
                  return this.getBinding(key);
               }
            } catch (ErrorsException var7) {
               throw new ConfigurationException(var7.getErrors().getMessages());
            }
         }

         return null;
      }
   }

   <T> BindingImpl<T> getBindingOrThrow(Key<T> key, Errors errors, InjectorImpl.JitLimitation jitType) throws ErrorsException {
      BindingImpl<T> binding = this.state.getExplicitBinding(key);
      return binding != null ? binding : this.getJustInTimeBinding(key, errors, jitType);
   }

   public <T> Binding<T> getBinding(Class<T> type) {
      return this.getBinding(Key.get(type));
   }

   public Injector getParent() {
      return this.parent;
   }

   public Injector createChildInjector(Iterable<? extends Module> modules) {
      return (new InternalInjectorCreator()).parentInjector(this).addModules(modules).build();
   }

   public Injector createChildInjector(Module... modules) {
      return this.createChildInjector((Iterable)ImmutableList.copyOf((Object[])modules));
   }

   private <T> BindingImpl<T> getJustInTimeBinding(Key<T> key, Errors errors, InjectorImpl.JitLimitation jitType) throws ErrorsException {
      boolean jitOverride = isProvider(key) || isTypeLiteral(key) || isMembersInjector(key);
      synchronized(this.state.lock()) {
         for(InjectorImpl injector = this; injector != null; injector = injector.parent) {
            BindingImpl<T> binding = (BindingImpl)injector.jitBindings.get(key);
            if (binding != null) {
               if (this.options.jitDisabled && jitType == InjectorImpl.JitLimitation.NO_JIT && !jitOverride && !(binding instanceof InjectorImpl.ConvertedConstantBindingImpl)) {
                  throw errors.jitDisabled(key).toException();
               }

               return binding;
            }
         }

         if (this.failedJitBindings.contains(key) && errors.hasErrors()) {
            throw errors.toException();
         } else {
            return this.createJustInTimeBindingRecursive(key, errors, this.options.jitDisabled, jitType);
         }
      }
   }

   private static boolean isProvider(Key<?> key) {
      return key.getTypeLiteral().getRawType().equals(Provider.class);
   }

   private static boolean isTypeLiteral(Key<?> key) {
      return key.getTypeLiteral().getRawType().equals(TypeLiteral.class);
   }

   private static <T> Key<T> getProvidedKey(Key<Provider<T>> key, Errors errors) throws ErrorsException {
      Type providerType = key.getTypeLiteral().getType();
      if (!(providerType instanceof ParameterizedType)) {
         throw errors.cannotInjectRawProvider().toException();
      } else {
         Type entryType = ((ParameterizedType)providerType).getActualTypeArguments()[0];
         Key<T> providedKey = key.ofType(entryType);
         return providedKey;
      }
   }

   private static boolean isMembersInjector(Key<?> key) {
      return key.getTypeLiteral().getRawType().equals(MembersInjector.class) && key.getAnnotationType() == null;
   }

   private <T> BindingImpl<MembersInjector<T>> createMembersInjectorBinding(Key<MembersInjector<T>> key, Errors errors) throws ErrorsException {
      Type membersInjectorType = key.getTypeLiteral().getType();
      if (!(membersInjectorType instanceof ParameterizedType)) {
         throw errors.cannotInjectRawMembersInjector().toException();
      } else {
         TypeLiteral<T> instanceType = TypeLiteral.get(((ParameterizedType)membersInjectorType).getActualTypeArguments()[0]);
         MembersInjector<T> membersInjector = this.membersInjectorStore.get(instanceType, errors);
         InternalFactory<MembersInjector<T>> factory = new ConstantFactory(Initializables.of(membersInjector));
         return new InstanceBindingImpl(this, key, SourceProvider.UNKNOWN_SOURCE, factory, ImmutableSet.of(), membersInjector);
      }
   }

   private <T> BindingImpl<Provider<T>> createProviderBinding(Key<Provider<T>> key, Errors errors) throws ErrorsException {
      Key<T> providedKey = getProvidedKey(key, errors);
      BindingImpl<T> delegate = this.getBindingOrThrow(providedKey, errors, InjectorImpl.JitLimitation.NO_JIT);
      return new InjectorImpl.ProviderBindingImpl(this, key, delegate);
   }

   private <T> BindingImpl<T> convertConstantStringBinding(Key<T> key, Errors errors) throws ErrorsException {
      Key<String> stringKey = key.ofType(STRING_TYPE);
      BindingImpl<String> stringBinding = this.state.getExplicitBinding(stringKey);
      if (stringBinding != null && stringBinding.isConstant()) {
         String stringValue = (String)stringBinding.getProvider().get();
         Object source = stringBinding.getSource();
         TypeLiteral<T> type = key.getTypeLiteral();
         TypeConverterBinding typeConverterBinding = this.state.getConverter(stringValue, type, errors, source);
         if (typeConverterBinding == null) {
            return null;
         } else {
            try {
               T converted = typeConverterBinding.getTypeConverter().convert(stringValue, type);
               if (converted == null) {
                  throw errors.converterReturnedNull(stringValue, source, type, typeConverterBinding).toException();
               } else if (!type.getRawType().isInstance(converted)) {
                  throw errors.conversionTypeError(stringValue, source, type, typeConverterBinding, converted).toException();
               } else {
                  return new InjectorImpl.ConvertedConstantBindingImpl(this, key, converted, stringBinding, typeConverterBinding);
               }
            } catch (ErrorsException var10) {
               throw var10;
            } catch (RuntimeException var11) {
               throw errors.conversionError(stringValue, source, type, typeConverterBinding, var11).toException();
            }
         }
      } else {
         return null;
      }
   }

   <T> void initializeBinding(BindingImpl<T> binding, Errors errors) throws ErrorsException {
      if (binding instanceof DelayedInitialize) {
         ((DelayedInitialize)binding).initialize(this, errors);
      }

   }

   <T> void initializeJitBinding(BindingImpl<T> binding, Errors errors) throws ErrorsException {
      if (binding instanceof DelayedInitialize) {
         Key<T> key = binding.getKey();
         this.jitBindings.put(key, binding);
         boolean successful = false;
         DelayedInitialize delayed = (DelayedInitialize)binding;

         try {
            delayed.initialize(this, errors);
            successful = true;
         } finally {
            if (!successful) {
               this.removeFailedJitBinding(binding, (InjectionPoint)null);
               this.cleanup(binding, new HashSet());
            }

         }
      }

   }

   private boolean cleanup(BindingImpl<?> binding, Set<Key> encountered) {
      boolean bindingFailed = false;
      Set<Dependency<?>> deps = this.getInternalDependencies(binding);
      Iterator i$ = deps.iterator();

      while(i$.hasNext()) {
         Dependency dep = (Dependency)i$.next();
         Key<?> depKey = dep.getKey();
         InjectionPoint ip = dep.getInjectionPoint();
         if (encountered.add(depKey)) {
            BindingImpl depBinding = (BindingImpl)this.jitBindings.get(depKey);
            if (depBinding != null) {
               boolean failed = this.cleanup(depBinding, encountered);
               if (depBinding instanceof ConstructorBindingImpl) {
                  ConstructorBindingImpl ctorBinding = (ConstructorBindingImpl)depBinding;
                  ip = ctorBinding.getInternalConstructor();
                  if (!ctorBinding.isInitialized()) {
                     failed = true;
                  }
               }

               if (failed) {
                  this.removeFailedJitBinding(depBinding, ip);
                  bindingFailed = true;
               }
            } else if (this.state.getExplicitBinding(depKey) == null) {
               bindingFailed = true;
            }
         }
      }

      return bindingFailed;
   }

   private void removeFailedJitBinding(Binding<?> binding, InjectionPoint ip) {
      this.failedJitBindings.add(binding.getKey());
      this.jitBindings.remove(binding.getKey());
      this.membersInjectorStore.remove(binding.getKey().getTypeLiteral());
      this.provisionListenerStore.remove(binding);
      if (ip != null) {
         this.constructors.remove(ip);
      }

   }

   private Set<Dependency<?>> getInternalDependencies(BindingImpl<?> binding) {
      if (binding instanceof ConstructorBindingImpl) {
         return ((ConstructorBindingImpl)binding).getInternalDependencies();
      } else {
         return (Set)(binding instanceof HasDependencies ? ((HasDependencies)binding).getDependencies() : ImmutableSet.of());
      }
   }

   <T> BindingImpl<T> createUninitializedBinding(Key<T> key, Scoping scoping, Object source, Errors errors, boolean jitBinding) throws ErrorsException {
      Class<?> rawType = key.getTypeLiteral().getRawType();
      ImplementedBy implementedBy = (ImplementedBy)rawType.getAnnotation(ImplementedBy.class);
      if (!rawType.isArray() && (!rawType.isEnum() || implementedBy == null)) {
         if (rawType == TypeLiteral.class) {
            BindingImpl<T> binding = this.createTypeLiteralBinding(key, errors);
            return binding;
         } else if (implementedBy != null) {
            Annotations.checkForMisplacedScopeAnnotations(rawType, source, errors);
            return this.createImplementedByBinding(key, scoping, implementedBy, errors);
         } else {
            ProvidedBy providedBy = (ProvidedBy)rawType.getAnnotation(ProvidedBy.class);
            if (providedBy != null) {
               Annotations.checkForMisplacedScopeAnnotations(rawType, source, errors);
               return this.createProvidedByBinding(key, scoping, providedBy, errors);
            } else {
               return ConstructorBindingImpl.create(this, key, (InjectionPoint)null, source, scoping, errors, jitBinding && this.options.jitDisabled, this.options.atInjectRequired);
            }
         }
      } else {
         throw errors.missingImplementation(key).toException();
      }
   }

   private <T> BindingImpl<TypeLiteral<T>> createTypeLiteralBinding(Key<TypeLiteral<T>> key, Errors errors) throws ErrorsException {
      Type typeLiteralType = key.getTypeLiteral().getType();
      if (!(typeLiteralType instanceof ParameterizedType)) {
         throw errors.cannotInjectRawTypeLiteral().toException();
      } else {
         ParameterizedType parameterizedType = (ParameterizedType)typeLiteralType;
         Type innerType = parameterizedType.getActualTypeArguments()[0];
         if (!(innerType instanceof Class) && !(innerType instanceof GenericArrayType) && !(innerType instanceof ParameterizedType)) {
            throw errors.cannotInjectTypeLiteralOf(innerType).toException();
         } else {
            TypeLiteral<T> value = TypeLiteral.get(innerType);
            InternalFactory<TypeLiteral<T>> factory = new ConstantFactory(Initializables.of(value));
            return new InstanceBindingImpl(this, key, SourceProvider.UNKNOWN_SOURCE, factory, ImmutableSet.of(), value);
         }
      }
   }

   <T> BindingImpl<T> createProvidedByBinding(Key<T> key, Scoping scoping, ProvidedBy providedBy, Errors errors) throws ErrorsException {
      Class<?> rawType = key.getTypeLiteral().getRawType();
      Class<? extends Provider<?>> providerType = providedBy.value();
      if (providerType == rawType) {
         throw errors.recursiveProviderType().toException();
      } else {
         Key<? extends Provider<T>> providerKey = Key.get(providerType);
         ProvidedByInternalFactory<T> internalFactory = new ProvidedByInternalFactory(rawType, providerType, providerKey);
         BindingImpl<T> binding = LinkedProviderBindingImpl.createWithInitializer(this, key, rawType, Scoping.scope(key, this, internalFactory, rawType, scoping), scoping, providerKey, internalFactory);
         internalFactory.setProvisionListenerCallback(this.provisionListenerStore.get(binding));
         return binding;
      }
   }

   private <T> BindingImpl<T> createImplementedByBinding(Key<T> key, Scoping scoping, ImplementedBy implementedBy, Errors errors) throws ErrorsException {
      Class<?> rawType = key.getTypeLiteral().getRawType();
      Class<?> implementationType = implementedBy.value();
      if (implementationType == rawType) {
         throw errors.recursiveImplementationType().toException();
      } else if (!rawType.isAssignableFrom(implementationType)) {
         throw errors.notASubtype(implementationType, rawType).toException();
      } else {
         final Key<? extends T> targetKey = Key.get(implementationType);
         final BindingImpl<? extends T> targetBinding = this.getBindingOrThrow(targetKey, errors, InjectorImpl.JitLimitation.NEW_OR_EXISTING_JIT);
         InternalFactory<T> internalFactory = new InternalFactory<T>() {
            public T get(Errors errors, InternalContext context, Dependency<?> dependency, boolean linked) throws ErrorsException {
               context.pushState(targetKey, targetBinding.getSource());

               Object var5;
               try {
                  var5 = targetBinding.getInternalFactory().get(errors.withSource(targetKey), context, dependency, true);
               } finally {
                  context.popState();
               }

               return var5;
            }
         };
         return new LinkedBindingImpl(this, key, rawType, Scoping.scope(key, this, internalFactory, rawType, scoping), scoping, targetKey);
      }
   }

   private <T> BindingImpl<T> createJustInTimeBindingRecursive(Key<T> key, Errors errors, boolean jitDisabled, InjectorImpl.JitLimitation jitType) throws ErrorsException {
      if (this.parent != null) {
         if (jitType == InjectorImpl.JitLimitation.NEW_OR_EXISTING_JIT && jitDisabled && !this.parent.options.jitDisabled) {
            throw errors.jitDisabledInParent(key).toException();
         }

         try {
            return this.parent.createJustInTimeBindingRecursive(key, new Errors(), jitDisabled, this.parent.options.jitDisabled ? InjectorImpl.JitLimitation.NO_JIT : jitType);
         } catch (ErrorsException var7) {
         }
      }

      Set<Object> sources = this.state.getSourcesForBlacklistedKey(key);
      if (this.state.isBlacklisted(key)) {
         throw errors.childBindingAlreadySet(key, sources).toException();
      } else {
         key = MoreTypes.canonicalizeKey(key);
         BindingImpl<T> binding = this.createJustInTimeBinding(key, errors, jitDisabled, jitType);
         this.state.parent().blacklist(key, this.state, binding.getSource());
         this.jitBindings.put(key, binding);
         return binding;
      }
   }

   private <T> BindingImpl<T> createJustInTimeBinding(Key<T> key, Errors errors, boolean jitDisabled, InjectorImpl.JitLimitation jitType) throws ErrorsException {
      int numErrorsBefore = errors.size();
      Set<Object> sources = this.state.getSourcesForBlacklistedKey(key);
      if (this.state.isBlacklisted(key)) {
         throw errors.childBindingAlreadySet(key, sources).toException();
      } else {
         BindingImpl convertedBinding;
         if (isProvider(key)) {
            convertedBinding = this.createProviderBinding(key, errors);
            return convertedBinding;
         } else if (isMembersInjector(key)) {
            convertedBinding = this.createMembersInjectorBinding(key, errors);
            return convertedBinding;
         } else {
            convertedBinding = this.convertConstantStringBinding(key, errors);
            if (convertedBinding != null) {
               return convertedBinding;
            } else if (!isTypeLiteral(key) && jitDisabled && jitType != InjectorImpl.JitLimitation.NEW_OR_EXISTING_JIT) {
               throw errors.jitDisabled(key).toException();
            } else if (key.getAnnotationType() == null) {
               Object source = key.getTypeLiteral().getRawType();
               BindingImpl<T> binding = this.createUninitializedBinding(key, Scoping.UNSCOPED, source, errors, true);
               errors.throwIfNewErrors(numErrorsBefore);
               this.initializeJitBinding(binding, errors);
               return binding;
            } else {
               if (key.hasAttributes() && !this.options.exactBindingAnnotationsRequired) {
                  try {
                     Errors ignored = new Errors();
                     return this.getBindingOrThrow(key.withoutAttributes(), ignored, InjectorImpl.JitLimitation.NO_JIT);
                  } catch (ErrorsException var10) {
                  }
               }

               throw errors.missingImplementation(key).toException();
            }
         }
      }
   }

   <T> InternalFactory<? extends T> getInternalFactory(Key<T> key, Errors errors, InjectorImpl.JitLimitation jitType) throws ErrorsException {
      return this.getBindingOrThrow(key, errors, jitType).getInternalFactory();
   }

   public Map<Key<?>, Binding<?>> getBindings() {
      return this.state.getExplicitBindingsThisLevel();
   }

   public Map<Key<?>, Binding<?>> getAllBindings() {
      synchronized(this.state.lock()) {
         return (new ImmutableMap.Builder()).putAll(this.state.getExplicitBindingsThisLevel()).putAll(this.jitBindings).build();
      }
   }

   public Map<Class<? extends Annotation>, Scope> getScopeBindings() {
      return ImmutableMap.copyOf(this.state.getScopes());
   }

   public Set<TypeConverterBinding> getTypeConverterBindings() {
      return ImmutableSet.copyOf(this.state.getConvertersThisLevel());
   }

   SingleParameterInjector<?>[] getParametersInjectors(List<Dependency<?>> parameters, Errors errors) throws ErrorsException {
      if (parameters.isEmpty()) {
         return null;
      } else {
         int numErrorsBefore = errors.size();
         SingleParameterInjector<?>[] result = new SingleParameterInjector[parameters.size()];
         int i = 0;
         Iterator i$ = parameters.iterator();

         while(i$.hasNext()) {
            Dependency parameter = (Dependency)i$.next();

            try {
               result[i++] = this.createParameterInjector(parameter, errors.withSource(parameter));
            } catch (ErrorsException var9) {
            }
         }

         errors.throwIfNewErrors(numErrorsBefore);
         return result;
      }
   }

   <T> SingleParameterInjector<T> createParameterInjector(Dependency<T> dependency, Errors errors) throws ErrorsException {
      BindingImpl<? extends T> binding = this.getBindingOrThrow(dependency.getKey(), errors, InjectorImpl.JitLimitation.NO_JIT);
      return new SingleParameterInjector(dependency, binding);
   }

   public void injectMembers(Object instance) {
      MembersInjector membersInjector = this.getMembersInjector(instance.getClass());
      membersInjector.injectMembers(instance);
   }

   public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
      Errors errors = new Errors(typeLiteral);

      try {
         return this.membersInjectorStore.get(typeLiteral, errors);
      } catch (ErrorsException var4) {
         throw new ConfigurationException(errors.merge(var4.getErrors()).getMessages());
      }
   }

   public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
      return this.getMembersInjector(TypeLiteral.get(type));
   }

   public <T> Provider<T> getProvider(Class<T> type) {
      return this.getProvider(Key.get(type));
   }

   <T> Provider<T> getProviderOrThrow(final Dependency<T> dependency, Errors errors) throws ErrorsException {
      Key<T> key = dependency.getKey();
      final BindingImpl<? extends T> binding = this.getBindingOrThrow(key, errors, InjectorImpl.JitLimitation.NO_JIT);
      return new Provider<T>() {
         public T get() {
            final Errors errors = new Errors(dependency);

            try {
               T t = InjectorImpl.this.callInContext(new ContextualCallable<T>() {
                  public T call(InternalContext context) throws ErrorsException {
                     Dependency previous = context.pushDependency(dependency, binding.getSource());

                     Object var3;
                     try {
                        var3 = binding.getInternalFactory().get(errors, context, dependency, false);
                     } finally {
                        context.popStateAndSetDependency(previous);
                     }

                     return var3;
                  }
               });
               errors.throwIfNewErrors(0);
               return t;
            } catch (ErrorsException var3) {
               throw new ProvisionException(errors.merge(var3.getErrors()).getMessages());
            }
         }

         public String toString() {
            return binding.getInternalFactory().toString();
         }
      };
   }

   public <T> Provider<T> getProvider(Key<T> key) {
      Errors errors = new Errors(key);

      try {
         Provider<T> result = this.getProviderOrThrow(Dependency.get(key), errors);
         errors.throwIfNewErrors(0);
         return result;
      } catch (ErrorsException var4) {
         throw new ConfigurationException(errors.merge(var4.getErrors()).getMessages());
      }
   }

   public <T> T getInstance(Key<T> key) {
      return this.getProvider(key).get();
   }

   public <T> T getInstance(Class<T> type) {
      return this.getProvider(type).get();
   }

   static Map<Thread, InternalContext> getGlobalInternalContext() {
      return Collections.unmodifiableMap(globalInternalContext);
   }

   <T> T callInContext(ContextualCallable<T> callable) throws ErrorsException {
      Object[] reference = (Object[])this.localContext.get();
      if (reference == null) {
         reference = new Object[1];
         this.localContext.set(reference);
      }

      Thread currentThread = Thread.currentThread();
      Object previousGlobalInternalContext;
      if (reference[0] == null) {
         reference[0] = new InternalContext(this.options);
         globalInternalContext.put(currentThread, (InternalContext)reference[0]);

         try {
            previousGlobalInternalContext = callable.call((InternalContext)reference[0]);
         } finally {
            reference[0] = null;
            globalInternalContext.remove(currentThread);
         }

         return previousGlobalInternalContext;
      } else {
         previousGlobalInternalContext = globalInternalContext.get(currentThread);
         globalInternalContext.put(currentThread, (InternalContext)reference[0]);

         Object var5;
         try {
            var5 = callable.call((InternalContext)reference[0]);
         } finally {
            if (previousGlobalInternalContext != null) {
               globalInternalContext.put(currentThread, (InternalContext)previousGlobalInternalContext);
            } else {
               globalInternalContext.remove(currentThread);
            }

         }

         return var5;
      }
   }

   public String toString() {
      return Objects.toStringHelper(Injector.class).add("bindings", this.state.getExplicitBindingsThisLevel().values()).toString();
   }

   interface MethodInvoker {
      Object invoke(Object var1, Object... var2) throws IllegalAccessException, InvocationTargetException;
   }

   private static class BindingsMultimap {
      final Map<TypeLiteral<?>, List<Binding<?>>> multimap;

      private BindingsMultimap() {
         this.multimap = Maps.newHashMap();
      }

      <T> void put(TypeLiteral<T> type, Binding<T> binding) {
         List<Binding<?>> bindingsForType = (List)this.multimap.get(type);
         if (bindingsForType == null) {
            bindingsForType = Lists.newArrayList();
            this.multimap.put(type, bindingsForType);
         }

         ((List)bindingsForType).add(binding);
      }

      <T> List<Binding<T>> getAll(TypeLiteral<T> type) {
         List<Binding<?>> bindings = (List)this.multimap.get(type);
         return (List)(bindings != null ? Collections.unmodifiableList((List)this.multimap.get(type)) : ImmutableList.of());
      }

      // $FF: synthetic method
      BindingsMultimap(Object x0) {
         this();
      }
   }

   private static class ConvertedConstantBindingImpl<T> extends BindingImpl<T> implements ConvertedConstantBinding<T> {
      final T value;
      final Provider<T> provider;
      final Binding<String> originalBinding;
      final TypeConverterBinding typeConverterBinding;

      ConvertedConstantBindingImpl(InjectorImpl injector, Key<T> key, T value, Binding<String> originalBinding, TypeConverterBinding typeConverterBinding) {
         super(injector, key, originalBinding.getSource(), new ConstantFactory(Initializables.of(value)), Scoping.UNSCOPED);
         this.value = value;
         this.provider = Providers.of(value);
         this.originalBinding = originalBinding;
         this.typeConverterBinding = typeConverterBinding;
      }

      public Provider<T> getProvider() {
         return this.provider;
      }

      public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
         return visitor.visit((ConvertedConstantBinding)this);
      }

      public T getValue() {
         return this.value;
      }

      public TypeConverterBinding getTypeConverterBinding() {
         return this.typeConverterBinding;
      }

      public Key<String> getSourceKey() {
         return this.originalBinding.getKey();
      }

      public Set<Dependency<?>> getDependencies() {
         return ImmutableSet.of(Dependency.get(this.getSourceKey()));
      }

      public void applyTo(Binder binder) {
         throw new UnsupportedOperationException("This element represents a synthetic binding.");
      }

      public String toString() {
         return Objects.toStringHelper(ConvertedConstantBinding.class).add("key", this.getKey()).add("sourceKey", this.getSourceKey()).add("value", this.value).toString();
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof InjectorImpl.ConvertedConstantBindingImpl)) {
            return false;
         } else {
            InjectorImpl.ConvertedConstantBindingImpl<?> o = (InjectorImpl.ConvertedConstantBindingImpl)obj;
            return this.getKey().equals(o.getKey()) && this.getScoping().equals(o.getScoping()) && Objects.equal(this.value, o.value);
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.getKey(), this.getScoping(), this.value);
      }
   }

   private static class ProviderBindingImpl<T> extends BindingImpl<Provider<T>> implements ProviderBinding<Provider<T>>, HasDependencies {
      final BindingImpl<T> providedBinding;

      ProviderBindingImpl(InjectorImpl injector, Key<Provider<T>> key, Binding<T> providedBinding) {
         super(injector, key, providedBinding.getSource(), createInternalFactory(providedBinding), Scoping.UNSCOPED);
         this.providedBinding = (BindingImpl)providedBinding;
      }

      static <T> InternalFactory<Provider<T>> createInternalFactory(Binding<T> providedBinding) {
         final Provider<T> provider = providedBinding.getProvider();
         return new InternalFactory<Provider<T>>() {
            public Provider<T> get(Errors errors, InternalContext context, Dependency dependency, boolean linked) {
               return provider;
            }
         };
      }

      public Key<? extends T> getProvidedKey() {
         return this.providedBinding.getKey();
      }

      public <V> V acceptTargetVisitor(BindingTargetVisitor<? super Provider<T>, V> visitor) {
         return visitor.visit((ProviderBinding)this);
      }

      public void applyTo(Binder binder) {
         throw new UnsupportedOperationException("This element represents a synthetic binding.");
      }

      public String toString() {
         return Objects.toStringHelper(ProviderBinding.class).add("key", this.getKey()).add("providedKey", this.getProvidedKey()).toString();
      }

      public Set<Dependency<?>> getDependencies() {
         return ImmutableSet.of(Dependency.get(this.getProvidedKey()));
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof InjectorImpl.ProviderBindingImpl)) {
            return false;
         } else {
            InjectorImpl.ProviderBindingImpl<?> o = (InjectorImpl.ProviderBindingImpl)obj;
            return this.getKey().equals(o.getKey()) && this.getScoping().equals(o.getScoping()) && Objects.equal(this.providedBinding, o.providedBinding);
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.getKey(), this.getScoping(), this.providedBinding);
      }
   }

   static enum JitLimitation {
      NO_JIT,
      EXISTING_JIT,
      NEW_OR_EXISTING_JIT;
   }

   static class InjectorOptions {
      final Stage stage;
      final boolean jitDisabled;
      final boolean disableCircularProxies;
      final boolean atInjectRequired;
      final boolean exactBindingAnnotationsRequired;

      InjectorOptions(Stage stage, boolean jitDisabled, boolean disableCircularProxies, boolean atInjectRequired, boolean exactBindingAnnotationsRequired) {
         this.stage = stage;
         this.jitDisabled = jitDisabled;
         this.disableCircularProxies = disableCircularProxies;
         this.atInjectRequired = atInjectRequired;
         this.exactBindingAnnotationsRequired = exactBindingAnnotationsRequired;
      }

      public String toString() {
         return Objects.toStringHelper(this.getClass()).add("stage", this.stage).add("jitDisabled", this.jitDisabled).add("disableCircularProxies", this.disableCircularProxies).add("atInjectRequired", this.atInjectRequired).add("exactBindingAnnotationsRequired", this.exactBindingAnnotationsRequired).toString();
      }
   }
}
