package com.google.inject.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.util.Stopwatch;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.TypeConverterBinding;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class InternalInjectorCreator {
   private final Stopwatch stopwatch = new Stopwatch();
   private final Errors errors = new Errors();
   private final Initializer initializer = new Initializer();
   private final ProcessedBindingData bindingData;
   private final InjectionRequestProcessor injectionRequestProcessor;
   private final InjectorShell.Builder shellBuilder = new InjectorShell.Builder();
   private List<InjectorShell> shells;

   public InternalInjectorCreator() {
      this.injectionRequestProcessor = new InjectionRequestProcessor(this.errors, this.initializer);
      this.bindingData = new ProcessedBindingData();
   }

   public InternalInjectorCreator stage(Stage stage) {
      this.shellBuilder.stage(stage);
      return this;
   }

   public InternalInjectorCreator parentInjector(InjectorImpl parent) {
      this.shellBuilder.parent(parent);
      return this;
   }

   public InternalInjectorCreator addModules(Iterable<? extends Module> modules) {
      this.shellBuilder.addModules(modules);
      return this;
   }

   public Injector build() {
      if (this.shellBuilder == null) {
         throw new AssertionError("Already built, builders are not reusable.");
      } else {
         synchronized(this.shellBuilder.lock()) {
            this.shells = this.shellBuilder.build(this.initializer, this.bindingData, this.stopwatch, this.errors);
            this.stopwatch.resetAndLog("Injector construction");
            this.initializeStatically();
         }

         this.injectDynamically();
         return (Injector)(this.shellBuilder.getStage() == Stage.TOOL ? new InternalInjectorCreator.ToolStageInjector(this.primaryInjector()) : this.primaryInjector());
      }
   }

   private void initializeStatically() {
      this.bindingData.initializeBindings();
      this.stopwatch.resetAndLog("Binding initialization");
      Iterator i$ = this.shells.iterator();

      InjectorShell shell;
      while(i$.hasNext()) {
         shell = (InjectorShell)i$.next();
         shell.getInjector().index();
      }

      this.stopwatch.resetAndLog("Binding indexing");
      this.injectionRequestProcessor.process(this.shells);
      this.stopwatch.resetAndLog("Collecting injection requests");
      this.bindingData.runCreationListeners(this.errors);
      this.stopwatch.resetAndLog("Binding validation");
      this.injectionRequestProcessor.validate();
      this.stopwatch.resetAndLog("Static validation");
      this.initializer.validateOustandingInjections(this.errors);
      this.stopwatch.resetAndLog("Instance member validation");
      (new LookupProcessor(this.errors)).process(this.shells);
      i$ = this.shells.iterator();

      while(i$.hasNext()) {
         shell = (InjectorShell)i$.next();
         ((DeferredLookups)shell.getInjector().lookups).initialize(this.errors);
      }

      this.stopwatch.resetAndLog("Provider verification");
      i$ = this.shells.iterator();

      do {
         if (!i$.hasNext()) {
            this.errors.throwCreationExceptionIfErrorsExist();
            return;
         }

         shell = (InjectorShell)i$.next();
      } while(shell.getElements().isEmpty());

      String var3 = String.valueOf(String.valueOf(shell.getElements()));
      throw new AssertionError((new StringBuilder(18 + var3.length())).append("Failed to execute ").append(var3).toString());
   }

   private Injector primaryInjector() {
      return ((InjectorShell)this.shells.get(0)).getInjector();
   }

   private void injectDynamically() {
      this.injectionRequestProcessor.injectMembers();
      this.stopwatch.resetAndLog("Static member injection");
      this.initializer.injectAll(this.errors);
      this.stopwatch.resetAndLog("Instance injection");
      this.errors.throwCreationExceptionIfErrorsExist();
      if (this.shellBuilder.getStage() != Stage.TOOL) {
         Iterator i$ = this.shells.iterator();

         while(i$.hasNext()) {
            InjectorShell shell = (InjectorShell)i$.next();
            this.loadEagerSingletons(shell.getInjector(), this.shellBuilder.getStage(), this.errors);
         }

         this.stopwatch.resetAndLog("Preloading singletons");
      }

      this.errors.throwCreationExceptionIfErrorsExist();
   }

   void loadEagerSingletons(InjectorImpl injector, Stage stage, final Errors errors) {
      Iterable<BindingImpl<?>> candidateBindings = ImmutableList.copyOf(Iterables.concat(injector.state.getExplicitBindingsThisLevel().values(), injector.jitBindings.values()));
      Iterator i$ = candidateBindings.iterator();

      while(i$.hasNext()) {
         final BindingImpl<?> binding = (BindingImpl)i$.next();
         if (this.isEagerSingleton(injector, binding, stage)) {
            try {
               injector.callInContext(new ContextualCallable<Void>() {
                  Dependency<?> dependency = Dependency.get(binding.getKey());

                  public Void call(InternalContext context) {
                     Dependency previous = context.pushDependency(this.dependency, binding.getSource());
                     Errors errorsForBinding = errors.withSource(this.dependency);

                     try {
                        binding.getInternalFactory().get(errorsForBinding, context, this.dependency, false);
                     } catch (ErrorsException var8) {
                        errorsForBinding.merge(var8.getErrors());
                     } finally {
                        context.popStateAndSetDependency(previous);
                     }

                     return null;
                  }
               });
            } catch (ErrorsException var8) {
               throw new AssertionError();
            }
         }
      }

   }

   private boolean isEagerSingleton(InjectorImpl injector, BindingImpl<?> binding, Stage stage) {
      if (binding.getScoping().isEagerSingleton(stage)) {
         return true;
      } else if (binding instanceof LinkedBindingImpl) {
         Key<?> linkedBinding = ((LinkedBindingImpl)binding).getLinkedKey();
         return this.isEagerSingleton(injector, injector.getBinding(linkedBinding), stage);
      } else {
         return false;
      }
   }

   static class ToolStageInjector implements Injector {
      private final Injector delegateInjector;

      ToolStageInjector(Injector delegateInjector) {
         this.delegateInjector = delegateInjector;
      }

      public void injectMembers(Object o) {
         throw new UnsupportedOperationException("Injector.injectMembers(Object) is not supported in Stage.TOOL");
      }

      public Map<Key<?>, Binding<?>> getBindings() {
         return this.delegateInjector.getBindings();
      }

      public Map<Key<?>, Binding<?>> getAllBindings() {
         return this.delegateInjector.getAllBindings();
      }

      public <T> Binding<T> getBinding(Key<T> key) {
         return this.delegateInjector.getBinding(key);
      }

      public <T> Binding<T> getBinding(Class<T> type) {
         return this.delegateInjector.getBinding(type);
      }

      public <T> Binding<T> getExistingBinding(Key<T> key) {
         return this.delegateInjector.getExistingBinding(key);
      }

      public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> type) {
         return this.delegateInjector.findBindingsByType(type);
      }

      public Injector getParent() {
         return this.delegateInjector.getParent();
      }

      public Injector createChildInjector(Iterable<? extends Module> modules) {
         return this.delegateInjector.createChildInjector(modules);
      }

      public Injector createChildInjector(Module... modules) {
         return this.delegateInjector.createChildInjector(modules);
      }

      public Map<Class<? extends Annotation>, Scope> getScopeBindings() {
         return this.delegateInjector.getScopeBindings();
      }

      public Set<TypeConverterBinding> getTypeConverterBindings() {
         return this.delegateInjector.getTypeConverterBindings();
      }

      public <T> Provider<T> getProvider(Key<T> key) {
         throw new UnsupportedOperationException("Injector.getProvider(Key<T>) is not supported in Stage.TOOL");
      }

      public <T> Provider<T> getProvider(Class<T> type) {
         throw new UnsupportedOperationException("Injector.getProvider(Class<T>) is not supported in Stage.TOOL");
      }

      public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
         throw new UnsupportedOperationException("Injector.getMembersInjector(TypeLiteral<T>) is not supported in Stage.TOOL");
      }

      public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
         throw new UnsupportedOperationException("Injector.getMembersInjector(Class<T>) is not supported in Stage.TOOL");
      }

      public <T> T getInstance(Key<T> key) {
         throw new UnsupportedOperationException("Injector.getInstance(Key<T>) is not supported in Stage.TOOL");
      }

      public <T> T getInstance(Class<T> type) {
         throw new UnsupportedOperationException("Injector.getInstance(Class<T>) is not supported in Stage.TOOL");
      }
   }
}
