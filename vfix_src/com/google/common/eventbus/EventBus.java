package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Beta
public class EventBus {
   private static final LoadingCache<Class<?>, Set<Class<?>>> flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class<?>, Set<Class<?>>>() {
      public Set<Class<?>> load(Class<?> concreteClass) {
         return TypeToken.of(concreteClass).getTypes().rawTypes();
      }
   });
   private final SetMultimap<Class<?>, EventSubscriber> subscribersByType;
   private final ReadWriteLock subscribersByTypeLock;
   private final SubscriberFindingStrategy finder;
   private final ThreadLocal<Queue<EventBus.EventWithSubscriber>> eventsToDispatch;
   private final ThreadLocal<Boolean> isDispatching;
   private SubscriberExceptionHandler subscriberExceptionHandler;

   public EventBus() {
      this("default");
   }

   public EventBus(String identifier) {
      this((SubscriberExceptionHandler)(new EventBus.LoggingSubscriberExceptionHandler(identifier)));
   }

   public EventBus(SubscriberExceptionHandler subscriberExceptionHandler) {
      this.subscribersByType = HashMultimap.create();
      this.subscribersByTypeLock = new ReentrantReadWriteLock();
      this.finder = new AnnotatedSubscriberFinder();
      this.eventsToDispatch = new ThreadLocal<Queue<EventBus.EventWithSubscriber>>() {
         protected Queue<EventBus.EventWithSubscriber> initialValue() {
            return new LinkedList();
         }
      };
      this.isDispatching = new ThreadLocal<Boolean>() {
         protected Boolean initialValue() {
            return false;
         }
      };
      this.subscriberExceptionHandler = (SubscriberExceptionHandler)Preconditions.checkNotNull(subscriberExceptionHandler);
   }

   public void register(Object object) {
      Multimap<Class<?>, EventSubscriber> methodsInListener = this.finder.findAllSubscribers(object);
      this.subscribersByTypeLock.writeLock().lock();

      try {
         this.subscribersByType.putAll(methodsInListener);
      } finally {
         this.subscribersByTypeLock.writeLock().unlock();
      }

   }

   public void unregister(Object object) {
      Multimap<Class<?>, EventSubscriber> methodsInListener = this.finder.findAllSubscribers(object);
      Iterator i$ = methodsInListener.asMap().entrySet().iterator();

      while(i$.hasNext()) {
         Entry<Class<?>, Collection<EventSubscriber>> entry = (Entry)i$.next();
         Class<?> eventType = (Class)entry.getKey();
         Collection<EventSubscriber> eventMethodsInListener = (Collection)entry.getValue();
         this.subscribersByTypeLock.writeLock().lock();

         try {
            Set<EventSubscriber> currentSubscribers = this.subscribersByType.get(eventType);
            if (!currentSubscribers.containsAll(eventMethodsInListener)) {
               throw new IllegalArgumentException("missing event subscriber for an annotated method. Is " + object + " registered?");
            }

            currentSubscribers.removeAll(eventMethodsInListener);
         } finally {
            this.subscribersByTypeLock.writeLock().unlock();
         }
      }

   }

   public void post(Object event) {
      Set<Class<?>> dispatchTypes = this.flattenHierarchy(event.getClass());
      boolean dispatched = false;
      Iterator i$ = dispatchTypes.iterator();

      while(i$.hasNext()) {
         Class<?> eventType = (Class)i$.next();
         this.subscribersByTypeLock.readLock().lock();

         try {
            Set<EventSubscriber> wrappers = this.subscribersByType.get(eventType);
            if (!wrappers.isEmpty()) {
               dispatched = true;
               Iterator i$ = wrappers.iterator();

               while(i$.hasNext()) {
                  EventSubscriber wrapper = (EventSubscriber)i$.next();
                  this.enqueueEvent(event, wrapper);
               }
            }
         } finally {
            this.subscribersByTypeLock.readLock().unlock();
         }
      }

      if (!dispatched && !(event instanceof DeadEvent)) {
         this.post(new DeadEvent(this, event));
      }

      this.dispatchQueuedEvents();
   }

   void enqueueEvent(Object event, EventSubscriber subscriber) {
      ((Queue)this.eventsToDispatch.get()).offer(new EventBus.EventWithSubscriber(event, subscriber));
   }

   void dispatchQueuedEvents() {
      if (!(Boolean)this.isDispatching.get()) {
         this.isDispatching.set(true);

         try {
            Queue events = (Queue)this.eventsToDispatch.get();

            EventBus.EventWithSubscriber eventWithSubscriber;
            while((eventWithSubscriber = (EventBus.EventWithSubscriber)events.poll()) != null) {
               this.dispatch(eventWithSubscriber.event, eventWithSubscriber.subscriber);
            }
         } finally {
            this.isDispatching.remove();
            this.eventsToDispatch.remove();
         }

      }
   }

   void dispatch(Object event, EventSubscriber wrapper) {
      try {
         wrapper.handleEvent(event);
      } catch (InvocationTargetException var6) {
         InvocationTargetException e = var6;

         try {
            this.subscriberExceptionHandler.handleException(e.getCause(), new SubscriberExceptionContext(this, event, wrapper.getSubscriber(), wrapper.getMethod()));
         } catch (Throwable var5) {
            Logger.getLogger(EventBus.class.getName()).log(Level.SEVERE, String.format("Exception %s thrown while handling exception: %s", var5, var6.getCause()), var5);
         }
      }

   }

   @VisibleForTesting
   Set<Class<?>> flattenHierarchy(Class<?> concreteClass) {
      try {
         return (Set)flattenHierarchyCache.getUnchecked(concreteClass);
      } catch (UncheckedExecutionException var3) {
         throw Throwables.propagate(var3.getCause());
      }
   }

   static class EventWithSubscriber {
      final Object event;
      final EventSubscriber subscriber;

      public EventWithSubscriber(Object event, EventSubscriber subscriber) {
         this.event = Preconditions.checkNotNull(event);
         this.subscriber = (EventSubscriber)Preconditions.checkNotNull(subscriber);
      }
   }

   private static final class LoggingSubscriberExceptionHandler implements SubscriberExceptionHandler {
      private final Logger logger;

      public LoggingSubscriberExceptionHandler(String identifier) {
         this.logger = Logger.getLogger(EventBus.class.getName() + "." + (String)Preconditions.checkNotNull(identifier));
      }

      public void handleException(Throwable exception, SubscriberExceptionContext context) {
         this.logger.log(Level.SEVERE, "Could not dispatch event: " + context.getSubscriber() + " to " + context.getSubscriberMethod(), exception.getCause());
      }
   }
}
