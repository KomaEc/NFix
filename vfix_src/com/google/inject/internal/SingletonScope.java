package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.DependencyAndSource;
import com.google.inject.spi.Message;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SingletonScope implements Scope {
   private static final Object NULL = new Object();
   private static final CycleDetectingLock.CycleDetectingLockFactory<Key<?>> cycleDetectingLockFactory = new CycleDetectingLock.CycleDetectingLockFactory();

   public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
      return new Provider<T>() {
         volatile Object instance;
         final ConstructionContext<T> constructionContext = new ConstructionContext();
         final CycleDetectingLock<Key<?>> creationLock;

         {
            this.creationLock = SingletonScope.cycleDetectingLockFactory.create(key);
         }

         public T get() {
            Object initialInstance = this.instance;
            if (initialInstance != null) {
               return initialInstance == SingletonScope.NULL ? null : initialInstance;
            } else {
               ListMultimap<Long, Key<?>> locksCycle = this.creationLock.lockOrDetectPotentialLocksCycle();
               Object provided;
               if (locksCycle.isEmpty()) {
                  label211: {
                     Object var25;
                     try {
                        if (this.instance != null) {
                           break label211;
                        }

                        provided = creator.get();
                        Object providedNotNull = provided == null ? SingletonScope.NULL : provided;
                        if (this.instance != null) {
                           Preconditions.checkState(this.instance == providedNotNull, "Singleton is called recursively returning different results");
                           break label211;
                        }

                        if (!Scopes.isCircularProxy(provided)) {
                           synchronized(this.constructionContext) {
                              this.instance = providedNotNull;
                              this.constructionContext.setProxyDelegates(provided);
                              break label211;
                           }
                        }

                        var25 = provided;
                     } catch (RuntimeException var22) {
                        synchronized(this.constructionContext) {
                           this.constructionContext.finishConstruction();
                        }

                        throw var22;
                     } finally {
                        this.creationLock.unlock();
                     }

                     return var25;
                  }
               } else {
                  synchronized(this.constructionContext) {
                     if (this.instance == null) {
                        Map<Thread, InternalContext> globalInternalContext = InjectorImpl.getGlobalInternalContext();
                        InternalContext internalContext = (InternalContext)globalInternalContext.get(Thread.currentThread());
                        Dependency<?> dependency = (Dependency)Preconditions.checkNotNull(internalContext.getDependency(), "globalInternalContext.get(currentThread()).getDependency()");
                        Class rawType = dependency.getKey().getTypeLiteral().getRawType();

                        Object var10000;
                        try {
                           T proxy = this.constructionContext.createProxy(new Errors(), internalContext.getInjectorOptions(), rawType);
                           var10000 = proxy;
                        } catch (ErrorsException var20) {
                           List<Message> exceptionErrorMessages = var20.getErrors().getMessages();
                           Preconditions.checkState(exceptionErrorMessages.size() == 1);
                           Message cycleDependenciesMessage = this.createCycleDependenciesMessage(ImmutableMap.copyOf(globalInternalContext), locksCycle, (Message)exceptionErrorMessages.get(0));
                           throw new ProvisionException(ImmutableList.of(cycleDependenciesMessage, exceptionErrorMessages.get(0)));
                        }

                        return var10000;
                     }
                  }
               }

               provided = this.instance;
               Preconditions.checkState(provided != null, "Internal error: Singleton is not initialized contrary to our expectations");
               return provided == SingletonScope.NULL ? null : provided;
            }
         }

         private Message createCycleDependenciesMessage(Map<Thread, InternalContext> globalInternalContext, ListMultimap<Long, Key<?>> locksCycle, Message proxyCreationError) {
            List<Object> sourcesCycle = Lists.newArrayList();
            sourcesCycle.add(Thread.currentThread());
            Map<Long, Thread> threadById = Maps.newHashMap();
            Iterator i$xx = globalInternalContext.keySet().iterator();

            while(i$xx.hasNext()) {
               Thread thread = (Thread)i$xx.next();
               threadById.put(thread.getId(), thread);
            }

            i$xx = locksCycle.keySet().iterator();

            while(true) {
               Thread lockedThread;
               List lockedKeys;
               do {
                  if (!i$xx.hasNext()) {
                     return new Message(sourcesCycle, String.format("Encountered circular dependency spanning several threads. %s", proxyCreationError.getMessage()), (Throwable)null);
                  }

                  long lockedThreadId = (Long)i$xx.next();
                  lockedThread = (Thread)threadById.get(lockedThreadId);
                  lockedKeys = Collections.unmodifiableList(locksCycle.get(lockedThreadId));
               } while(lockedThread == null);

               List<DependencyAndSource> dependencyChain = null;
               boolean allLockedKeysAreFoundInDependencies = false;
               InternalContext lockedThreadInternalContext = (InternalContext)globalInternalContext.get(lockedThread);
               if (lockedThreadInternalContext != null) {
                  dependencyChain = lockedThreadInternalContext.getDependencyChain();
                  List<Key<?>> lockedKeysToFind = Lists.newLinkedList(lockedKeys);
                  Iterator i$x = dependencyChain.iterator();

                  while(i$x.hasNext()) {
                     DependencyAndSource dx = (DependencyAndSource)i$x.next();
                     Dependency<?> dependency = dx.getDependency();
                     if (dependency != null && dependency.getKey().equals(lockedKeysToFind.get(0))) {
                        lockedKeysToFind.remove(0);
                        if (lockedKeysToFind.isEmpty()) {
                           allLockedKeysAreFoundInDependencies = true;
                           break;
                        }
                     }
                  }
               }

               if (allLockedKeysAreFoundInDependencies) {
                  Key<?> firstLockedKey = (Key)lockedKeys.get(0);
                  boolean firstLockedKeyFound = false;
                  Iterator i$ = dependencyChain.iterator();

                  while(i$.hasNext()) {
                     DependencyAndSource d = (DependencyAndSource)i$.next();
                     Dependency<?> dependencyx = d.getDependency();
                     if (dependencyx != null) {
                        if (firstLockedKeyFound) {
                           sourcesCycle.add(dependencyx);
                           sourcesCycle.add(d.getBindingSource());
                        } else if (dependencyx.getKey().equals(firstLockedKey)) {
                           firstLockedKeyFound = true;
                           sourcesCycle.add(d.getBindingSource());
                        }
                     }
                  }
               } else {
                  sourcesCycle.addAll(lockedKeys);
               }

               sourcesCycle.add(lockedThread);
            }
         }

         public String toString() {
            return String.format("%s[%s]", creator, Scopes.SINGLETON);
         }
      };
   }

   public String toString() {
      return "Scopes.SINGLETON";
   }
}
