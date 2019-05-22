package com.google.inject.spi;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.MoreTypes;
import com.google.inject.internal.Nullability;
import com.google.inject.internal.util.Classes;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class InjectionPoint {
   private static final Logger logger = Logger.getLogger(InjectionPoint.class.getName());
   private final boolean optional;
   private final Member member;
   private final TypeLiteral<?> declaringType;
   private final ImmutableList<Dependency<?>> dependencies;

   InjectionPoint(TypeLiteral<?> declaringType, Method method, boolean optional) {
      this.member = method;
      this.declaringType = declaringType;
      this.optional = optional;
      this.dependencies = this.forMember(method, declaringType, method.getParameterAnnotations());
   }

   InjectionPoint(TypeLiteral<?> declaringType, Constructor<?> constructor) {
      this.member = constructor;
      this.declaringType = declaringType;
      this.optional = false;
      this.dependencies = this.forMember(constructor, declaringType, constructor.getParameterAnnotations());
   }

   InjectionPoint(TypeLiteral<?> declaringType, Field field, boolean optional) {
      this.member = field;
      this.declaringType = declaringType;
      this.optional = optional;
      Annotation[] annotations = field.getAnnotations();
      Errors errors = new Errors(field);
      Key key = null;

      try {
         key = Annotations.getKey(declaringType.getFieldType(field), field, annotations, errors);
      } catch (ConfigurationException var8) {
         errors.merge(var8.getErrorMessages());
      } catch (ErrorsException var9) {
         errors.merge(var9.getErrors());
      }

      errors.throwConfigurationExceptionIfErrorsExist();
      this.dependencies = ImmutableList.of(this.newDependency(key, Nullability.allowsNull(annotations), -1));
   }

   private ImmutableList<Dependency<?>> forMember(Member member, TypeLiteral<?> type, Annotation[][] paramterAnnotations) {
      Errors errors = new Errors(member);
      Iterator<Annotation[]> annotationsIterator = Arrays.asList(paramterAnnotations).iterator();
      List<Dependency<?>> dependencies = Lists.newArrayList();
      int index = 0;
      Iterator i$ = type.getParameterTypes(member).iterator();

      while(i$.hasNext()) {
         TypeLiteral parameterType = (TypeLiteral)i$.next();

         try {
            Annotation[] parameterAnnotations = (Annotation[])annotationsIterator.next();
            Key<?> key = Annotations.getKey(parameterType, member, parameterAnnotations, errors);
            dependencies.add(this.newDependency(key, Nullability.allowsNull(parameterAnnotations), index));
            ++index;
         } catch (ConfigurationException var12) {
            errors.merge(var12.getErrorMessages());
         } catch (ErrorsException var13) {
            errors.merge(var13.getErrors());
         }
      }

      errors.throwConfigurationExceptionIfErrorsExist();
      return ImmutableList.copyOf((Collection)dependencies);
   }

   private <T> Dependency<T> newDependency(Key<T> key, boolean allowsNull, int parameterIndex) {
      return new Dependency(this, key, allowsNull, parameterIndex);
   }

   public Member getMember() {
      return this.member;
   }

   public List<Dependency<?>> getDependencies() {
      return this.dependencies;
   }

   public boolean isOptional() {
      return this.optional;
   }

   public boolean isToolable() {
      return ((AnnotatedElement)this.member).isAnnotationPresent(Toolable.class);
   }

   public TypeLiteral<?> getDeclaringType() {
      return this.declaringType;
   }

   public boolean equals(Object o) {
      return o instanceof InjectionPoint && this.member.equals(((InjectionPoint)o).member) && this.declaringType.equals(((InjectionPoint)o).declaringType);
   }

   public int hashCode() {
      return this.member.hashCode() ^ this.declaringType.hashCode();
   }

   public String toString() {
      return Classes.toString(this.member);
   }

   public static <T> InjectionPoint forConstructor(Constructor<T> constructor) {
      return new InjectionPoint(TypeLiteral.get(constructor.getDeclaringClass()), constructor);
   }

   public static <T> InjectionPoint forConstructor(Constructor<T> constructor, TypeLiteral<? extends T> type) {
      if (type.getRawType() != constructor.getDeclaringClass()) {
         (new Errors(type)).constructorNotDefinedByType(constructor, type).throwConfigurationExceptionIfErrorsExist();
      }

      return new InjectionPoint(type, constructor);
   }

   public static InjectionPoint forConstructorOf(TypeLiteral<?> type) {
      Class<?> rawType = MoreTypes.getRawType(type.getType());
      Errors errors = new Errors(rawType);
      Constructor<?> injectableConstructor = null;
      Constructor[] arr$ = rawType.getDeclaredConstructors();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Constructor<?> constructor = arr$[i$];
         Inject guiceInject = (Inject)constructor.getAnnotation(Inject.class);
         boolean optional;
         if (guiceInject == null) {
            javax.inject.Inject javaxInject = (javax.inject.Inject)constructor.getAnnotation(javax.inject.Inject.class);
            if (javaxInject == null) {
               continue;
            }

            optional = false;
         } else {
            optional = guiceInject.optional();
         }

         if (optional) {
            errors.optionalConstructor(constructor);
         }

         if (injectableConstructor != null) {
            errors.tooManyConstructors(rawType);
         }

         injectableConstructor = constructor;
         checkForMisplacedBindingAnnotations(constructor, errors);
      }

      errors.throwConfigurationExceptionIfErrorsExist();
      if (injectableConstructor != null) {
         return new InjectionPoint(type, injectableConstructor);
      } else {
         try {
            Constructor<?> noArgConstructor = rawType.getDeclaredConstructor();
            if (Modifier.isPrivate(noArgConstructor.getModifiers()) && !Modifier.isPrivate(rawType.getModifiers())) {
               errors.missingConstructor(rawType);
               throw new ConfigurationException(errors.getMessages());
            } else {
               checkForMisplacedBindingAnnotations(noArgConstructor, errors);
               return new InjectionPoint(type, noArgConstructor);
            }
         } catch (NoSuchMethodException var11) {
            errors.missingConstructor(rawType);
            throw new ConfigurationException(errors.getMessages());
         }
      }
   }

   public static InjectionPoint forConstructorOf(Class<?> type) {
      return forConstructorOf(TypeLiteral.get(type));
   }

   public static <T> InjectionPoint forMethod(Method method, TypeLiteral<T> type) {
      return new InjectionPoint(type, method, false);
   }

   public static Set<InjectionPoint> forStaticMethodsAndFields(TypeLiteral<?> type) {
      Errors errors = new Errors();
      Set result;
      if (type.getRawType().isInterface()) {
         errors.staticInjectionOnInterface(type.getRawType());
         result = null;
      } else {
         result = getInjectionPoints(type, true, errors);
      }

      if (errors.hasErrors()) {
         throw (new ConfigurationException(errors.getMessages())).withPartialValue(result);
      } else {
         return result;
      }
   }

   public static Set<InjectionPoint> forStaticMethodsAndFields(Class<?> type) {
      return forStaticMethodsAndFields(TypeLiteral.get(type));
   }

   public static Set<InjectionPoint> forInstanceMethodsAndFields(TypeLiteral<?> type) {
      Errors errors = new Errors();
      Set<InjectionPoint> result = getInjectionPoints(type, false, errors);
      if (errors.hasErrors()) {
         throw (new ConfigurationException(errors.getMessages())).withPartialValue(result);
      } else {
         return result;
      }
   }

   public static Set<InjectionPoint> forInstanceMethodsAndFields(Class<?> type) {
      return forInstanceMethodsAndFields(TypeLiteral.get(type));
   }

   private static boolean checkForMisplacedBindingAnnotations(Member member, Errors errors) {
      Annotation misplacedBindingAnnotation = Annotations.findBindingAnnotation(errors, member, ((AnnotatedElement)member).getAnnotations());
      if (misplacedBindingAnnotation == null) {
         return false;
      } else {
         if (member instanceof Method) {
            try {
               if (member.getDeclaringClass().getDeclaredField(member.getName()) != null) {
                  return false;
               }
            } catch (NoSuchFieldException var4) {
            }
         }

         errors.misplacedBindingAnnotation(member, misplacedBindingAnnotation);
         return true;
      }
   }

   static Annotation getAtInject(AnnotatedElement member) {
      Annotation a = member.getAnnotation(javax.inject.Inject.class);
      return a == null ? member.getAnnotation(Inject.class) : a;
   }

   private static Set<InjectionPoint> getInjectionPoints(TypeLiteral<?> type, boolean statics, Errors errors) {
      InjectionPoint.InjectableMembers injectableMembers = new InjectionPoint.InjectableMembers();
      InjectionPoint.OverrideIndex overrideIndex = null;
      List<TypeLiteral<?>> hierarchy = hierarchyFor(type);
      int topIndex = hierarchy.size() - 1;

      for(int i = topIndex; i >= 0; --i) {
         if (overrideIndex != null && i < topIndex) {
            if (i == 0) {
               overrideIndex.position = InjectionPoint.Position.BOTTOM;
            } else {
               overrideIndex.position = InjectionPoint.Position.MIDDLE;
            }
         }

         TypeLiteral<?> current = (TypeLiteral)hierarchy.get(i);
         Field[] arr$ = current.getRawType().getDeclaredFields();
         int len$ = arr$.length;

         int i$;
         Annotation atInject;
         for(i$ = 0; i$ < len$; ++i$) {
            Field field = arr$[i$];
            if (Modifier.isStatic(field.getModifiers()) == statics) {
               atInject = getAtInject(field);
               if (atInject != null) {
                  InjectionPoint.InjectableField injectableField = new InjectionPoint.InjectableField(current, field, atInject);
                  if (injectableField.jsr330 && Modifier.isFinal(field.getModifiers())) {
                     errors.cannotInjectFinalField(field);
                  }

                  injectableMembers.add(injectableField);
               }
            }
         }

         Method[] arr$ = current.getRawType().getDeclaredMethods();
         len$ = arr$.length;

         for(i$ = 0; i$ < len$; ++i$) {
            Method method = arr$[i$];
            if (isEligibleForInjection(method, statics)) {
               atInject = getAtInject(method);
               if (atInject != null) {
                  InjectionPoint.InjectableMethod injectableMethod = new InjectionPoint.InjectableMethod(current, method, atInject);
                  if (!checkForMisplacedBindingAnnotations(method, errors) && isValidMethod(injectableMethod, errors)) {
                     if (statics) {
                        injectableMembers.add(injectableMethod);
                     } else {
                        if (overrideIndex == null) {
                           overrideIndex = new InjectionPoint.OverrideIndex(injectableMembers);
                        } else {
                           overrideIndex.removeIfOverriddenBy(method, true, injectableMethod);
                        }

                        overrideIndex.add(injectableMethod);
                     }
                  } else if (overrideIndex != null) {
                     boolean removed = overrideIndex.removeIfOverriddenBy(method, false, injectableMethod);
                     if (removed) {
                        logger.log(Level.WARNING, "Method: {0} is not a valid injectable method (because it either has misplaced binding annotations or specifies type parameters) but is overriding a method that is valid. Because it is not valid, the method will not be injected. To fix this, make the method a valid injectable method.", method);
                     }
                  }
               } else if (overrideIndex != null) {
                  boolean removed = overrideIndex.removeIfOverriddenBy(method, false, (InjectionPoint.InjectableMethod)null);
                  if (removed) {
                     logger.log(Level.WARNING, "Method: {0} is not annotated with @Inject but is overriding a method that is annotated with @javax.inject.Inject.  Because it is not annotated with @Inject, the method will not be injected. To fix this, annotate the method with @Inject.", method);
                  }
               }
            }
         }
      }

      if (injectableMembers.isEmpty()) {
         return Collections.emptySet();
      } else {
         ImmutableSet.Builder<InjectionPoint> builder = ImmutableSet.builder();

         for(InjectionPoint.InjectableMember im = injectableMembers.head; im != null; im = im.next) {
            try {
               builder.add((Object)im.toInjectionPoint());
            } catch (ConfigurationException var16) {
               if (!im.optional) {
                  errors.merge(var16.getErrorMessages());
               }
            }
         }

         return builder.build();
      }
   }

   private static boolean isEligibleForInjection(Method method, boolean statics) {
      return Modifier.isStatic(method.getModifiers()) == statics && !method.isBridge() && !method.isSynthetic();
   }

   private static boolean isValidMethod(InjectionPoint.InjectableMethod injectableMethod, Errors errors) {
      boolean result = true;
      if (injectableMethod.jsr330) {
         Method method = injectableMethod.method;
         if (Modifier.isAbstract(method.getModifiers())) {
            errors.cannotInjectAbstractMethod(method);
            result = false;
         }

         if (method.getTypeParameters().length > 0) {
            errors.cannotInjectMethodWithTypeParameters(method);
            result = false;
         }
      }

      return result;
   }

   private static List<TypeLiteral<?>> hierarchyFor(TypeLiteral<?> type) {
      List<TypeLiteral<?>> hierarchy = new ArrayList();

      for(TypeLiteral current = type; current.getRawType() != Object.class; current = current.getSupertype(current.getRawType().getSuperclass())) {
         hierarchy.add(current);
      }

      return hierarchy;
   }

   private static boolean overrides(Method a, Method b) {
      int modifiers = b.getModifiers();
      if (!Modifier.isPublic(modifiers) && !Modifier.isProtected(modifiers)) {
         return Modifier.isPrivate(modifiers) ? false : a.getDeclaringClass().getPackage().equals(b.getDeclaringClass().getPackage());
      } else {
         return true;
      }
   }

   static class Signature {
      final String name;
      final Class[] parameterTypes;
      final int hash;

      Signature(Method method) {
         this.name = method.getName();
         this.parameterTypes = method.getParameterTypes();
         int h = this.name.hashCode();
         h = h * 31 + this.parameterTypes.length;
         Class[] arr$ = this.parameterTypes;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class parameterType = arr$[i$];
            h = h * 31 + parameterType.hashCode();
         }

         this.hash = h;
      }

      public int hashCode() {
         return this.hash;
      }

      public boolean equals(Object o) {
         if (!(o instanceof InjectionPoint.Signature)) {
            return false;
         } else {
            InjectionPoint.Signature other = (InjectionPoint.Signature)o;
            if (!this.name.equals(other.name)) {
               return false;
            } else if (this.parameterTypes.length != other.parameterTypes.length) {
               return false;
            } else {
               for(int i = 0; i < this.parameterTypes.length; ++i) {
                  if (this.parameterTypes[i] != other.parameterTypes[i]) {
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   static class OverrideIndex {
      final InjectionPoint.InjectableMembers injectableMembers;
      Map<InjectionPoint.Signature, List<InjectionPoint.InjectableMethod>> bySignature;
      InjectionPoint.Position position;
      Method lastMethod;
      InjectionPoint.Signature lastSignature;

      OverrideIndex(InjectionPoint.InjectableMembers injectableMembers) {
         this.position = InjectionPoint.Position.TOP;
         this.injectableMembers = injectableMembers;
      }

      boolean removeIfOverriddenBy(Method method, boolean alwaysRemove, InjectionPoint.InjectableMethod injectableMethod) {
         if (this.position == InjectionPoint.Position.TOP) {
            return false;
         } else {
            if (this.bySignature == null) {
               this.bySignature = new HashMap();

               for(InjectionPoint.InjectableMember member = this.injectableMembers.head; member != null; member = member.next) {
                  if (member instanceof InjectionPoint.InjectableMethod) {
                     InjectionPoint.InjectableMethod im = (InjectionPoint.InjectableMethod)member;
                     if (!im.isFinal()) {
                        List<InjectionPoint.InjectableMethod> methods = new ArrayList();
                        methods.add(im);
                        this.bySignature.put(new InjectionPoint.Signature(im.method), methods);
                     }
                  }
               }
            }

            this.lastMethod = method;
            InjectionPoint.Signature signature = this.lastSignature = new InjectionPoint.Signature(method);
            List<InjectionPoint.InjectableMethod> methods = (List)this.bySignature.get(signature);
            boolean removed = false;
            if (methods != null) {
               Iterator iterator = methods.iterator();

               while(true) {
                  InjectionPoint.InjectableMethod possiblyOverridden;
                  boolean wasGuiceInject;
                  do {
                     do {
                        if (!iterator.hasNext()) {
                           return removed;
                        }

                        possiblyOverridden = (InjectionPoint.InjectableMethod)iterator.next();
                     } while(!InjectionPoint.overrides(method, possiblyOverridden.method));

                     wasGuiceInject = !possiblyOverridden.jsr330 || possiblyOverridden.overrodeGuiceInject;
                     if (injectableMethod != null) {
                        injectableMethod.overrodeGuiceInject = wasGuiceInject;
                     }
                  } while(!alwaysRemove && wasGuiceInject);

                  removed = true;
                  iterator.remove();
                  this.injectableMembers.remove(possiblyOverridden);
               }
            } else {
               return removed;
            }
         }
      }

      void add(InjectionPoint.InjectableMethod injectableMethod) {
         this.injectableMembers.add(injectableMethod);
         if (this.position != InjectionPoint.Position.BOTTOM && !injectableMethod.isFinal()) {
            if (this.bySignature != null) {
               InjectionPoint.Signature signature = injectableMethod.method == this.lastMethod ? this.lastSignature : new InjectionPoint.Signature(injectableMethod.method);
               List<InjectionPoint.InjectableMethod> methods = (List)this.bySignature.get(signature);
               if (methods == null) {
                  methods = new ArrayList();
                  this.bySignature.put(signature, methods);
               }

               ((List)methods).add(injectableMethod);
            }

         }
      }
   }

   static enum Position {
      TOP,
      MIDDLE,
      BOTTOM;
   }

   static class InjectableMembers {
      InjectionPoint.InjectableMember head;
      InjectionPoint.InjectableMember tail;

      void add(InjectionPoint.InjectableMember member) {
         if (this.head == null) {
            this.head = this.tail = member;
         } else {
            member.previous = this.tail;
            this.tail.next = member;
            this.tail = member;
         }

      }

      void remove(InjectionPoint.InjectableMember member) {
         if (member.previous != null) {
            member.previous.next = member.next;
         }

         if (member.next != null) {
            member.next.previous = member.previous;
         }

         if (this.head == member) {
            this.head = member.next;
         }

         if (this.tail == member) {
            this.tail = member.previous;
         }

      }

      boolean isEmpty() {
         return this.head == null;
      }
   }

   static class InjectableMethod extends InjectionPoint.InjectableMember {
      final Method method;
      boolean overrodeGuiceInject;

      InjectableMethod(TypeLiteral<?> declaringType, Method method, Annotation atInject) {
         super(declaringType, atInject);
         this.method = method;
      }

      InjectionPoint toInjectionPoint() {
         return new InjectionPoint(this.declaringType, this.method, this.optional);
      }

      public boolean isFinal() {
         return Modifier.isFinal(this.method.getModifiers());
      }
   }

   static class InjectableField extends InjectionPoint.InjectableMember {
      final Field field;

      InjectableField(TypeLiteral<?> declaringType, Field field, Annotation atInject) {
         super(declaringType, atInject);
         this.field = field;
      }

      InjectionPoint toInjectionPoint() {
         return new InjectionPoint(this.declaringType, this.field, this.optional);
      }
   }

   abstract static class InjectableMember {
      final TypeLiteral<?> declaringType;
      final boolean optional;
      final boolean jsr330;
      InjectionPoint.InjectableMember previous;
      InjectionPoint.InjectableMember next;

      InjectableMember(TypeLiteral<?> declaringType, Annotation atInject) {
         this.declaringType = declaringType;
         if (atInject.annotationType() == javax.inject.Inject.class) {
            this.optional = false;
            this.jsr330 = true;
         } else {
            this.jsr330 = false;
            this.optional = ((Inject)atInject).optional();
         }
      }

      abstract InjectionPoint toInjectionPoint();
   }
}
