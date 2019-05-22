package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.PrivateBinder;
import com.google.inject.PrivateModule;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;
import com.google.inject.binder.AnnotatedElementBuilder;
import com.google.inject.internal.AbstractBindingBuilder;
import com.google.inject.internal.BindingBuilder;
import com.google.inject.internal.ConstantBindingBuilderImpl;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ExposureBuilder;
import com.google.inject.internal.InternalFlags;
import com.google.inject.internal.MoreTypes;
import com.google.inject.internal.PrivateElementsImpl;
import com.google.inject.internal.ProviderMethodsModule;
import com.google.inject.internal.util.SourceProvider;
import com.google.inject.internal.util.StackTraceElements;
import com.google.inject.matcher.Matcher;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public final class Elements {
   private static final BindingTargetVisitor<Object, Object> GET_INSTANCE_VISITOR = new DefaultBindingTargetVisitor<Object, Object>() {
      public Object visit(InstanceBinding<?> binding) {
         return binding.getInstance();
      }

      protected Object visitOther(Binding<?> binding) {
         throw new IllegalArgumentException();
      }
   };

   public static List<Element> getElements(Module... modules) {
      return getElements(Stage.DEVELOPMENT, (Iterable)Arrays.asList(modules));
   }

   public static List<Element> getElements(Stage stage, Module... modules) {
      return getElements(stage, (Iterable)Arrays.asList(modules));
   }

   public static List<Element> getElements(Iterable<? extends Module> modules) {
      return getElements(Stage.DEVELOPMENT, modules);
   }

   public static List<Element> getElements(Stage stage, Iterable<? extends Module> modules) {
      Elements.RecordingBinder binder = new Elements.RecordingBinder(stage);
      Iterator i$ = modules.iterator();

      while(i$.hasNext()) {
         Module module = (Module)i$.next();
         binder.install(module);
      }

      binder.scanForAnnotatedMethods();
      i$ = binder.privateBinders.iterator();

      while(i$.hasNext()) {
         Elements.RecordingBinder child = (Elements.RecordingBinder)i$.next();
         child.scanForAnnotatedMethods();
      }

      StackTraceElements.clearCache();
      return Collections.unmodifiableList(binder.elements);
   }

   public static Module getModule(Iterable<? extends Element> elements) {
      return new Elements.ElementsAsModule(elements);
   }

   static <T> BindingTargetVisitor<T, T> getInstanceVisitor() {
      return GET_INSTANCE_VISITOR;
   }

   private static class RecordingBinder implements Binder, PrivateBinder {
      private final Stage stage;
      private final Map<Module, Elements.ModuleInfo> modules;
      private final List<Element> elements;
      private final Object source;
      private ModuleSource moduleSource;
      private final SourceProvider sourceProvider;
      private final Set<ModuleAnnotatedMethodScanner> scanners;
      private final Elements.RecordingBinder parent;
      private final PrivateElementsImpl privateElements;
      private final List<Elements.RecordingBinder> privateBinders;

      private RecordingBinder(Stage stage) {
         this.moduleSource = null;
         this.stage = stage;
         this.modules = Maps.newLinkedHashMap();
         this.scanners = Sets.newLinkedHashSet();
         this.elements = Lists.newArrayList();
         this.source = null;
         this.sourceProvider = SourceProvider.DEFAULT_INSTANCE.plusSkippedClasses(Elements.class, Elements.RecordingBinder.class, AbstractModule.class, ConstantBindingBuilderImpl.class, AbstractBindingBuilder.class, BindingBuilder.class);
         this.parent = null;
         this.privateElements = null;
         this.privateBinders = Lists.newArrayList();
      }

      private RecordingBinder(Elements.RecordingBinder prototype, Object source, SourceProvider sourceProvider) {
         this.moduleSource = null;
         Preconditions.checkArgument(source == null ^ sourceProvider == null);
         this.stage = prototype.stage;
         this.modules = prototype.modules;
         this.elements = prototype.elements;
         this.scanners = prototype.scanners;
         this.source = source;
         this.moduleSource = prototype.moduleSource;
         this.sourceProvider = sourceProvider;
         this.parent = prototype.parent;
         this.privateElements = prototype.privateElements;
         this.privateBinders = prototype.privateBinders;
      }

      private RecordingBinder(Elements.RecordingBinder parent, PrivateElementsImpl privateElements) {
         this.moduleSource = null;
         this.stage = parent.stage;
         this.modules = Maps.newLinkedHashMap();
         this.scanners = Sets.newLinkedHashSet(parent.scanners);
         this.elements = privateElements.getElementsMutable();
         this.source = parent.source;
         this.moduleSource = parent.moduleSource;
         this.sourceProvider = parent.sourceProvider;
         this.parent = parent;
         this.privateElements = privateElements;
         this.privateBinders = parent.privateBinders;
      }

      public void bindScope(Class<? extends Annotation> annotationType, Scope scope) {
         this.elements.add(new ScopeBinding(this.getElementSource(), annotationType, scope));
      }

      public void requestInjection(Object instance) {
         this.requestInjection(TypeLiteral.get(instance.getClass()), instance);
      }

      public <T> void requestInjection(TypeLiteral<T> type, T instance) {
         this.elements.add(new InjectionRequest(this.getElementSource(), MoreTypes.canonicalizeForKey(type), instance));
      }

      public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
         MembersInjectorLookup<T> element = new MembersInjectorLookup(this.getElementSource(), MoreTypes.canonicalizeForKey(typeLiteral));
         this.elements.add(element);
         return element.getMembersInjector();
      }

      public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
         return this.getMembersInjector(TypeLiteral.get(type));
      }

      public void bindListener(Matcher<? super TypeLiteral<?>> typeMatcher, TypeListener listener) {
         this.elements.add(new TypeListenerBinding(this.getElementSource(), listener, typeMatcher));
      }

      public void bindListener(Matcher<? super Binding<?>> bindingMatcher, ProvisionListener... listeners) {
         this.elements.add(new ProvisionListenerBinding(this.getElementSource(), bindingMatcher, listeners));
      }

      public void requestStaticInjection(Class<?>... types) {
         Class[] arr$ = types;
         int len$ = types.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class<?> type = arr$[i$];
            this.elements.add(new StaticInjectionRequest(this.getElementSource(), type));
         }

      }

      void scanForAnnotatedMethods() {
         Iterator i$ = this.scanners.iterator();

         label38:
         while(i$.hasNext()) {
            ModuleAnnotatedMethodScanner scanner = (ModuleAnnotatedMethodScanner)i$.next();
            Iterator i$ = Maps.newLinkedHashMap(this.modules).entrySet().iterator();

            while(true) {
               Entry entry;
               Module module;
               Elements.ModuleInfo info;
               do {
                  if (!i$.hasNext()) {
                     continue label38;
                  }

                  entry = (Entry)i$.next();
                  module = (Module)entry.getKey();
                  info = (Elements.ModuleInfo)entry.getValue();
               } while(info.skipScanning);

               this.moduleSource = ((Elements.ModuleInfo)entry.getValue()).moduleSource;

               try {
                  info.binder.install(ProviderMethodsModule.forModule(module, scanner));
               } catch (RuntimeException var9) {
                  Collection<Message> messages = Errors.getMessagesFromThrowable(var9);
                  if (!messages.isEmpty()) {
                     this.elements.addAll(messages);
                  } else {
                     this.addError((Throwable)var9);
                  }
               }
            }
         }

         this.moduleSource = null;
      }

      public void install(Module module) {
         if (!this.modules.containsKey(module)) {
            Elements.RecordingBinder binder = this;
            boolean unwrapModuleSource = false;
            if (module instanceof ProviderMethodsModule) {
               Object delegate = ((ProviderMethodsModule)module).getDelegateModule();
               if (this.moduleSource == null || !this.moduleSource.getModuleClassName().equals(delegate.getClass().getName())) {
                  this.moduleSource = this.getModuleSource(delegate);
                  unwrapModuleSource = true;
               }
            } else {
               this.moduleSource = this.getModuleSource(module);
               unwrapModuleSource = true;
            }

            boolean skipScanning = false;
            if (module instanceof PrivateModule) {
               binder = (Elements.RecordingBinder)this.newPrivateBinder();
               binder.modules.put(module, new Elements.ModuleInfo(binder, this.moduleSource, false));
               skipScanning = true;
            }

            this.modules.put(module, new Elements.ModuleInfo(binder, this.moduleSource, skipScanning));

            try {
               module.configure(binder);
            } catch (RuntimeException var7) {
               Collection<Message> messages = Errors.getMessagesFromThrowable(var7);
               if (!messages.isEmpty()) {
                  this.elements.addAll(messages);
               } else {
                  this.addError((Throwable)var7);
               }
            }

            binder.install(ProviderMethodsModule.forModule(module));
            if (unwrapModuleSource) {
               this.moduleSource = this.moduleSource.getParent();
            }
         }

      }

      public Stage currentStage() {
         return this.stage;
      }

      public void addError(String message, Object... arguments) {
         this.elements.add(new Message(this.getElementSource(), Errors.format(message, arguments)));
      }

      public void addError(Throwable t) {
         String var10001 = String.valueOf(t.getMessage());
         String var10000;
         if (var10001.length() != 0) {
            var10000 = "An exception was caught and reported. Message: ".concat(var10001);
         } else {
            String var10002 = new String;
            var10000 = var10002;
            var10002.<init>("An exception was caught and reported. Message: ");
         }

         String message = var10000;
         this.elements.add(new Message(ImmutableList.of(this.getElementSource()), message, t));
      }

      public void addError(Message message) {
         this.elements.add(message);
      }

      public <T> AnnotatedBindingBuilder<T> bind(Key<T> key) {
         BindingBuilder<T> builder = new BindingBuilder(this, this.elements, this.getElementSource(), MoreTypes.canonicalizeKey(key));
         return builder;
      }

      public <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
         return this.bind(Key.get(typeLiteral));
      }

      public <T> AnnotatedBindingBuilder<T> bind(Class<T> type) {
         return this.bind(Key.get(type));
      }

      public AnnotatedConstantBindingBuilder bindConstant() {
         return new ConstantBindingBuilderImpl(this, this.elements, this.getElementSource());
      }

      public <T> Provider<T> getProvider(Key<T> key) {
         return this.getProvider(Dependency.get(key));
      }

      public <T> Provider<T> getProvider(Dependency<T> dependency) {
         ProviderLookup<T> element = new ProviderLookup(this.getElementSource(), dependency);
         this.elements.add(element);
         return element.getProvider();
      }

      public <T> Provider<T> getProvider(Class<T> type) {
         return this.getProvider(Key.get(type));
      }

      public void convertToTypes(Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter converter) {
         this.elements.add(new TypeConverterBinding(this.getElementSource(), typeMatcher, converter));
      }

      public Elements.RecordingBinder withSource(Object source) {
         return source == this.source ? this : new Elements.RecordingBinder(this, source, (SourceProvider)null);
      }

      public Elements.RecordingBinder skipSources(Class... classesToSkip) {
         if (this.source != null) {
            return this;
         } else {
            SourceProvider newSourceProvider = this.sourceProvider.plusSkippedClasses(classesToSkip);
            return new Elements.RecordingBinder(this, (Object)null, newSourceProvider);
         }
      }

      public PrivateBinder newPrivateBinder() {
         PrivateElementsImpl privateElements = new PrivateElementsImpl(this.getElementSource());
         Elements.RecordingBinder binder = new Elements.RecordingBinder(this, privateElements);
         this.privateBinders.add(binder);
         this.elements.add(privateElements);
         return binder;
      }

      public void disableCircularProxies() {
         this.elements.add(new DisableCircularProxiesOption(this.getElementSource()));
      }

      public void requireExplicitBindings() {
         this.elements.add(new RequireExplicitBindingsOption(this.getElementSource()));
      }

      public void requireAtInjectOnConstructors() {
         this.elements.add(new RequireAtInjectOnConstructorsOption(this.getElementSource()));
      }

      public void requireExactBindingAnnotations() {
         this.elements.add(new RequireExactBindingAnnotationsOption(this.getElementSource()));
      }

      public void scanModulesForAnnotatedMethods(ModuleAnnotatedMethodScanner scanner) {
         this.scanners.add(scanner);
         this.elements.add(new ModuleAnnotatedMethodScannerBinding(this.getElementSource(), scanner));
      }

      public void expose(Key<?> key) {
         this.exposeInternal(key);
      }

      public AnnotatedElementBuilder expose(Class<?> type) {
         return this.exposeInternal(Key.get(type));
      }

      public AnnotatedElementBuilder expose(TypeLiteral<?> type) {
         return this.exposeInternal(Key.get(type));
      }

      private <T> AnnotatedElementBuilder exposeInternal(Key<T> key) {
         if (this.privateElements == null) {
            this.addError("Cannot expose %s on a standard binder. Exposed bindings are only applicable to private binders.", key);
            return new AnnotatedElementBuilder() {
               public void annotatedWith(Class<? extends Annotation> annotationType) {
               }

               public void annotatedWith(Annotation annotation) {
               }
            };
         } else {
            ExposureBuilder<T> builder = new ExposureBuilder(this, this.getElementSource(), MoreTypes.canonicalizeKey(key));
            this.privateElements.addExposureBuilder(builder);
            return builder;
         }
      }

      private ModuleSource getModuleSource(Object module) {
         StackTraceElement[] partialCallStack;
         if (InternalFlags.getIncludeStackTraceOption() == InternalFlags.IncludeStackTraceOption.COMPLETE) {
            partialCallStack = this.getPartialCallStack((new Throwable()).getStackTrace());
         } else {
            partialCallStack = new StackTraceElement[0];
         }

         return this.moduleSource == null ? new ModuleSource(module, partialCallStack) : this.moduleSource.createChild(module, partialCallStack);
      }

      private ElementSource getElementSource() {
         StackTraceElement[] callStack = null;
         StackTraceElement[] partialCallStack = new StackTraceElement[0];
         ElementSource originalSource = null;
         Object declaringSource = this.source;
         if (declaringSource instanceof ElementSource) {
            originalSource = (ElementSource)declaringSource;
            declaringSource = originalSource.getDeclaringSource();
         }

         InternalFlags.IncludeStackTraceOption stackTraceOption = InternalFlags.getIncludeStackTraceOption();
         if (stackTraceOption == InternalFlags.IncludeStackTraceOption.COMPLETE || stackTraceOption == InternalFlags.IncludeStackTraceOption.ONLY_FOR_DECLARING_SOURCE && declaringSource == null) {
            callStack = (new Throwable()).getStackTrace();
         }

         if (stackTraceOption == InternalFlags.IncludeStackTraceOption.COMPLETE) {
            partialCallStack = this.getPartialCallStack(callStack);
         }

         if (declaringSource == null) {
            if (stackTraceOption != InternalFlags.IncludeStackTraceOption.COMPLETE && stackTraceOption != InternalFlags.IncludeStackTraceOption.ONLY_FOR_DECLARING_SOURCE) {
               declaringSource = this.sourceProvider.getFromClassNames(this.moduleSource.getModuleClassNames());
            } else {
               declaringSource = this.sourceProvider.get(callStack);
            }
         }

         return new ElementSource(originalSource, declaringSource, this.moduleSource, partialCallStack);
      }

      private StackTraceElement[] getPartialCallStack(StackTraceElement[] callStack) {
         int toSkip = 0;
         if (this.moduleSource != null) {
            toSkip = this.moduleSource.getStackTraceSize();
         }

         int chunkSize = callStack.length - toSkip - 1;
         StackTraceElement[] partialCallStack = new StackTraceElement[chunkSize];
         System.arraycopy(callStack, 1, partialCallStack, 0, chunkSize);
         return partialCallStack;
      }

      public String toString() {
         return "Binder";
      }

      // $FF: synthetic method
      RecordingBinder(Stage x0, Object x1) {
         this(x0);
      }
   }

   private static class ModuleInfo {
      private final Binder binder;
      private final ModuleSource moduleSource;
      private final boolean skipScanning;

      private ModuleInfo(Binder binder, ModuleSource moduleSource, boolean skipScanning) {
         this.binder = binder;
         this.moduleSource = moduleSource;
         this.skipScanning = skipScanning;
      }

      // $FF: synthetic method
      ModuleInfo(Binder x0, ModuleSource x1, boolean x2, Object x3) {
         this(x0, x1, x2);
      }
   }

   private static class ElementsAsModule implements Module {
      private final Iterable<? extends Element> elements;

      ElementsAsModule(Iterable<? extends Element> elements) {
         this.elements = elements;
      }

      public void configure(Binder binder) {
         Iterator i$ = this.elements.iterator();

         while(i$.hasNext()) {
            Element element = (Element)i$.next();
            element.applyTo(binder);
         }

      }
   }
}
