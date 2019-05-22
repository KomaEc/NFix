package com.google.inject.internal;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.Message;
import com.google.inject.spi.ModuleAnnotatedMethodScanner;
import com.google.inject.util.Modules;
import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class ProviderMethodsModule implements Module {
   private static ModuleAnnotatedMethodScanner PROVIDES_BUILDER = new ModuleAnnotatedMethodScanner() {
      public <T> Key<T> prepareMethod(Binder binder, Annotation annotation, Key<T> key, InjectionPoint injectionPoint) {
         return key;
      }

      public Set<? extends Class<? extends Annotation>> annotationClasses() {
         return ImmutableSet.of(Provides.class);
      }
   };
   private final Object delegate;
   private final TypeLiteral<?> typeLiteral;
   private final boolean skipFastClassGeneration;
   private final ModuleAnnotatedMethodScanner scanner;

   private ProviderMethodsModule(Object delegate, boolean skipFastClassGeneration, ModuleAnnotatedMethodScanner scanner) {
      this.delegate = Preconditions.checkNotNull(delegate, "delegate");
      this.typeLiteral = TypeLiteral.get(this.delegate.getClass());
      this.skipFastClassGeneration = skipFastClassGeneration;
      this.scanner = scanner;
   }

   public static Module forModule(Module module) {
      return forObject(module, false, PROVIDES_BUILDER);
   }

   public static Module forModule(Object module, ModuleAnnotatedMethodScanner scanner) {
      return forObject(module, false, scanner);
   }

   public static Module forObject(Object object) {
      return forObject(object, true, PROVIDES_BUILDER);
   }

   private static Module forObject(Object object, boolean skipFastClassGeneration, ModuleAnnotatedMethodScanner scanner) {
      return (Module)(object instanceof ProviderMethodsModule ? Modules.EMPTY_MODULE : new ProviderMethodsModule(object, skipFastClassGeneration, scanner));
   }

   public Object getDelegateModule() {
      return this.delegate;
   }

   public synchronized void configure(Binder binder) {
      Iterator i$ = this.getProviderMethods(binder).iterator();

      while(i$.hasNext()) {
         ProviderMethod<?> providerMethod = (ProviderMethod)i$.next();
         providerMethod.configure(binder);
      }

   }

   public List<ProviderMethod<?>> getProviderMethods(Binder binder) {
      List<ProviderMethod<?>> result = Lists.newArrayList();
      Multimap<ProviderMethodsModule.Signature, Method> methodsBySignature = HashMultimap.create();

      Method matchingSignature;
      for(Class c = this.delegate.getClass(); c != Object.class; c = c.getSuperclass()) {
         Method[] arr$ = c.getDeclaredMethods();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            matchingSignature = arr$[i$];
            if ((matchingSignature.getModifiers() & 10) == 0 && !matchingSignature.isBridge() && !matchingSignature.isSynthetic()) {
               methodsBySignature.put(new ProviderMethodsModule.Signature(matchingSignature), matchingSignature);
            }

            Optional<Annotation> annotation = this.isProvider(binder, matchingSignature);
            if (annotation.isPresent()) {
               result.add(this.createProviderMethod(binder, matchingSignature, (Annotation)annotation.get()));
            }
         }
      }

      Iterator i$ = result.iterator();

      while(true) {
         while(i$.hasNext()) {
            ProviderMethod<?> provider = (ProviderMethod)i$.next();
            Method method = provider.getMethod();
            Iterator i$ = methodsBySignature.get(new ProviderMethodsModule.Signature(method)).iterator();

            while(i$.hasNext()) {
               matchingSignature = (Method)i$.next();
               if (!matchingSignature.getDeclaringClass().isAssignableFrom(method.getDeclaringClass()) && overrides(matchingSignature, method)) {
                  String var10000;
                  if (provider.getAnnotation().annotationType() == Provides.class) {
                     var10000 = "@Provides";
                  } else {
                     String var10001 = String.valueOf(provider.getAnnotation().annotationType().getCanonicalName());
                     if (var10001.length() != 0) {
                        var10000 = "@".concat(var10001);
                     } else {
                        String var10002 = new String;
                        var10000 = var10002;
                        var10002.<init>("@");
                     }
                  }

                  String annotationString = var10000;
                  String var10 = String.valueOf(String.valueOf(annotationString));
                  String var11 = String.valueOf(String.valueOf(annotationString));
                  binder.addError((new StringBuilder(67 + var10.length() + var11.length())).append("Overriding ").append(var10).append(" methods is not allowed.").append("\n\t").append(var11).append(" method: %s\n\toverridden by: %s").toString(), method, matchingSignature);
                  break;
               }
            }
         }

         return result;
      }
   }

   private Optional<Annotation> isProvider(Binder binder, Method method) {
      if (!method.isBridge() && !method.isSynthetic()) {
         Annotation annotation = null;
         Iterator i$ = this.scanner.annotationClasses().iterator();

         while(i$.hasNext()) {
            Class<? extends Annotation> annotationClass = (Class)i$.next();
            Annotation foundAnnotation = method.getAnnotation(annotationClass);
            if (foundAnnotation != null) {
               if (annotation != null) {
                  binder.addError("More than one annotation claimed by %s on method %s. Methods can only have one annotation claimed per scanner.", this.scanner, method);
                  return Optional.absent();
               }

               annotation = foundAnnotation;
            }
         }

         return Optional.fromNullable(annotation);
      } else {
         return Optional.absent();
      }
   }

   private static boolean overrides(Method a, Method b) {
      int modifiers = b.getModifiers();
      if (!Modifier.isPublic(modifiers) && !Modifier.isProtected(modifiers)) {
         return Modifier.isPrivate(modifiers) ? false : a.getDeclaringClass().getPackage().equals(b.getDeclaringClass().getPackage());
      } else {
         return true;
      }
   }

   private <T> ProviderMethod<T> createProviderMethod(Binder binder, Method method, Annotation annotation) {
      binder = binder.withSource(method);
      Errors errors = new Errors(method);
      InjectionPoint point = InjectionPoint.forMethod(method, this.typeLiteral);
      List<Dependency<?>> dependencies = point.getDependencies();
      List<Provider<?>> parameterProviders = Lists.newArrayList();
      Iterator i$ = point.getDependencies().iterator();

      while(i$.hasNext()) {
         Dependency<?> dependency = (Dependency)i$.next();
         parameterProviders.add(binder.getProvider(dependency));
      }

      TypeLiteral<T> returnType = this.typeLiteral.getReturnType(method);
      Key key = this.getKey(errors, returnType, method, method.getAnnotations());

      try {
         key = this.scanner.prepareMethod(binder, annotation, key, point);
      } catch (Throwable var13) {
         binder.addError(var13);
      }

      Class<? extends Annotation> scopeAnnotation = Annotations.findScopeAnnotation(errors, method.getAnnotations());
      Iterator i$ = errors.getMessages().iterator();

      while(i$.hasNext()) {
         Message message = (Message)i$.next();
         binder.addError(message);
      }

      return ProviderMethod.create(key, method, this.delegate, ImmutableSet.copyOf((Collection)dependencies), parameterProviders, scopeAnnotation, this.skipFastClassGeneration, annotation);
   }

   <T> Key<T> getKey(Errors errors, TypeLiteral<T> type, Member member, Annotation[] annotations) {
      Annotation bindingAnnotation = Annotations.findBindingAnnotation(errors, member, annotations);
      return bindingAnnotation == null ? Key.get(type) : Key.get(type, bindingAnnotation);
   }

   public boolean equals(Object o) {
      return o instanceof ProviderMethodsModule && ((ProviderMethodsModule)o).delegate == this.delegate && ((ProviderMethodsModule)o).scanner == this.scanner;
   }

   public int hashCode() {
      return this.delegate.hashCode();
   }

   private final class Signature {
      final Class<?>[] parameters;
      final String name;
      final int hashCode;

      Signature(Method method) {
         this.name = method.getName();
         List<TypeLiteral<?>> resolvedParameterTypes = ProviderMethodsModule.this.typeLiteral.getParameterTypes(method);
         this.parameters = new Class[resolvedParameterTypes.size()];
         int i = 0;

         TypeLiteral type;
         for(Iterator i$ = resolvedParameterTypes.iterator(); i$.hasNext(); this.parameters[i] = type.getRawType()) {
            type = (TypeLiteral)i$.next();
         }

         this.hashCode = this.name.hashCode() + 31 * Arrays.hashCode(this.parameters);
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof ProviderMethodsModule.Signature)) {
            return false;
         } else {
            ProviderMethodsModule.Signature other = (ProviderMethodsModule.Signature)obj;
            return other.name.equals(this.name) && Arrays.equals(this.parameters, other.parameters);
         }
      }

      public int hashCode() {
         return this.hashCode;
      }
   }
}
