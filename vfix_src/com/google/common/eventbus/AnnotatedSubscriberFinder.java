package com.google.common.eventbus;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

class AnnotatedSubscriberFinder implements SubscriberFindingStrategy {
   private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class<?>, ImmutableList<Method>>() {
      public ImmutableList<Method> load(Class<?> concreteClass) throws Exception {
         return AnnotatedSubscriberFinder.getAnnotatedMethodsInternal(concreteClass);
      }
   });

   public Multimap<Class<?>, EventSubscriber> findAllSubscribers(Object listener) {
      Multimap<Class<?>, EventSubscriber> methodsInListener = HashMultimap.create();
      Class<?> clazz = listener.getClass();
      Iterator i$ = getAnnotatedMethods(clazz).iterator();

      while(i$.hasNext()) {
         Method method = (Method)i$.next();
         Class<?>[] parameterTypes = method.getParameterTypes();
         Class<?> eventType = parameterTypes[0];
         EventSubscriber subscriber = makeSubscriber(listener, method);
         methodsInListener.put(eventType, subscriber);
      }

      return methodsInListener;
   }

   private static ImmutableList<Method> getAnnotatedMethods(Class<?> clazz) {
      try {
         return (ImmutableList)subscriberMethodsCache.getUnchecked(clazz);
      } catch (UncheckedExecutionException var2) {
         throw Throwables.propagate(var2.getCause());
      }
   }

   private static ImmutableList<Method> getAnnotatedMethodsInternal(Class<?> clazz) {
      Set<? extends Class<?>> supers = TypeToken.of(clazz).getTypes().rawTypes();
      Map<AnnotatedSubscriberFinder.MethodIdentifier, Method> identifiers = Maps.newHashMap();
      Iterator i$ = supers.iterator();

      while(i$.hasNext()) {
         Class<?> superClazz = (Class)i$.next();
         Method[] arr$ = superClazz.getMethods();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Method superClazzMethod = arr$[i$];
            if (superClazzMethod.isAnnotationPresent(Subscribe.class)) {
               Class<?>[] parameterTypes = superClazzMethod.getParameterTypes();
               if (parameterTypes.length != 1) {
                  throw new IllegalArgumentException("Method " + superClazzMethod + " has @Subscribe annotation, but requires " + parameterTypes.length + " arguments.  Event subscriber methods must require a single argument.");
               }

               AnnotatedSubscriberFinder.MethodIdentifier ident = new AnnotatedSubscriberFinder.MethodIdentifier(superClazzMethod);
               if (!identifiers.containsKey(ident)) {
                  identifiers.put(ident, superClazzMethod);
               }
            }
         }
      }

      return ImmutableList.copyOf(identifiers.values());
   }

   private static EventSubscriber makeSubscriber(Object listener, Method method) {
      Object wrapper;
      if (methodIsDeclaredThreadSafe(method)) {
         wrapper = new EventSubscriber(listener, method);
      } else {
         wrapper = new SynchronizedEventSubscriber(listener, method);
      }

      return (EventSubscriber)wrapper;
   }

   private static boolean methodIsDeclaredThreadSafe(Method method) {
      return method.getAnnotation(AllowConcurrentEvents.class) != null;
   }

   private static final class MethodIdentifier {
      private final String name;
      private final List<Class<?>> parameterTypes;

      MethodIdentifier(Method method) {
         this.name = method.getName();
         this.parameterTypes = Arrays.asList(method.getParameterTypes());
      }

      public int hashCode() {
         return Objects.hashCode(this.name, this.parameterTypes);
      }

      public boolean equals(@Nullable Object o) {
         if (!(o instanceof AnnotatedSubscriberFinder.MethodIdentifier)) {
            return false;
         } else {
            AnnotatedSubscriberFinder.MethodIdentifier ident = (AnnotatedSubscriberFinder.MethodIdentifier)o;
            return this.name.equals(ident.name) && this.parameterTypes.equals(ident.parameterTypes);
         }
      }
   }
}
