package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@Beta
public final class Futures {
   private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new AsyncFunction<ListenableFuture<Object>, Object>() {
      public ListenableFuture<Object> apply(ListenableFuture<Object> input) {
         return input;
      }
   };
   private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf(new Function<Constructor<?>, Boolean>() {
      public Boolean apply(Constructor<?> input) {
         return Arrays.asList(input.getParameterTypes()).contains(String.class);
      }
   }).reverse();

   private Futures() {
   }

   public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> future, Function<Exception, X> mapper) {
      return new Futures.MappingCheckedFuture((ListenableFuture)Preconditions.checkNotNull(future), mapper);
   }

   public static <V> ListenableFuture<V> immediateFuture(@Nullable V value) {
      return new Futures.ImmediateSuccessfulFuture(value);
   }

   public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V value) {
      return new Futures.ImmediateSuccessfulCheckedFuture(value);
   }

   public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
      Preconditions.checkNotNull(throwable);
      return new Futures.ImmediateFailedFuture(throwable);
   }

   public static <V> ListenableFuture<V> immediateCancelledFuture() {
      return new Futures.ImmediateCancelledFuture();
   }

   public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X exception) {
      Preconditions.checkNotNull(exception);
      return new Futures.ImmediateFailedCheckedFuture(exception);
   }

   public static <V> ListenableFuture<V> withFallback(ListenableFuture<? extends V> input, FutureFallback<? extends V> fallback) {
      return withFallback(input, fallback, MoreExecutors.sameThreadExecutor());
   }

   public static <V> ListenableFuture<V> withFallback(ListenableFuture<? extends V> input, FutureFallback<? extends V> fallback, Executor executor) {
      Preconditions.checkNotNull(fallback);
      return new Futures.FallbackFuture(input, fallback, executor);
   }

   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function) {
      return transform(input, (AsyncFunction)function, MoreExecutors.sameThreadExecutor());
   }

   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
      Futures.ChainingListenableFuture<I, O> output = new Futures.ChainingListenableFuture(function, input);
      input.addListener(output, executor);
      return output;
   }

   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function) {
      return transform(input, (Function)function, MoreExecutors.sameThreadExecutor());
   }

   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, final Function<? super I, ? extends O> function, Executor executor) {
      Preconditions.checkNotNull(function);
      AsyncFunction<I, O> wrapperFunction = new AsyncFunction<I, O>() {
         public ListenableFuture<O> apply(I input) {
            O output = function.apply(input);
            return Futures.immediateFuture(output);
         }
      };
      return transform(input, wrapperFunction, executor);
   }

   public static <I, O> Future<O> lazyTransform(final Future<I> input, final Function<? super I, ? extends O> function) {
      Preconditions.checkNotNull(input);
      Preconditions.checkNotNull(function);
      return new Future<O>() {
         public boolean cancel(boolean mayInterruptIfRunning) {
            return input.cancel(mayInterruptIfRunning);
         }

         public boolean isCancelled() {
            return input.isCancelled();
         }

         public boolean isDone() {
            return input.isDone();
         }

         public O get() throws InterruptedException, ExecutionException {
            return this.applyTransformation(input.get());
         }

         public O get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return this.applyTransformation(input.get(timeout, unit));
         }

         private O applyTransformation(I inputx) throws ExecutionException {
            try {
               return function.apply(inputx);
            } catch (Throwable var3) {
               throw new ExecutionException(var3);
            }
         }
      };
   }

   public static <V> ListenableFuture<V> dereference(ListenableFuture<? extends ListenableFuture<? extends V>> nested) {
      return transform(nested, DEREFERENCER);
   }

   @Beta
   public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures) {
      return listFuture(ImmutableList.copyOf((Object[])futures), true, MoreExecutors.sameThreadExecutor());
   }

   @Beta
   public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
      return listFuture(ImmutableList.copyOf(futures), true, MoreExecutors.sameThreadExecutor());
   }

   public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future) {
      return new Futures.NonCancellationPropagatingFuture(future);
   }

   @Beta
   public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures) {
      return listFuture(ImmutableList.copyOf((Object[])futures), false, MoreExecutors.sameThreadExecutor());
   }

   @Beta
   public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
      return listFuture(ImmutableList.copyOf(futures), false, MoreExecutors.sameThreadExecutor());
   }

   public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback) {
      addCallback(future, callback, MoreExecutors.sameThreadExecutor());
   }

   public static <V> void addCallback(final ListenableFuture<V> future, final FutureCallback<? super V> callback, Executor executor) {
      Preconditions.checkNotNull(callback);
      Runnable callbackListener = new Runnable() {
         public void run() {
            Object value;
            try {
               value = Uninterruptibles.getUninterruptibly(future);
            } catch (ExecutionException var3) {
               callback.onFailure(var3.getCause());
               return;
            } catch (RuntimeException var4) {
               callback.onFailure(var4);
               return;
            } catch (Error var5) {
               callback.onFailure(var5);
               return;
            }

            callback.onSuccess(value);
         }
      };
      future.addListener(callbackListener, executor);
   }

   public static <V, X extends Exception> V get(Future<V> future, Class<X> exceptionClass) throws X {
      Preconditions.checkNotNull(future);
      Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass), "Futures.get exception type (%s) must not be a RuntimeException", exceptionClass);

      try {
         return future.get();
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         throw newWithCause(exceptionClass, var3);
      } catch (ExecutionException var4) {
         wrapAndThrowExceptionOrError(var4.getCause(), exceptionClass);
         throw new AssertionError();
      }
   }

   public static <V, X extends Exception> V get(Future<V> future, long timeout, TimeUnit unit, Class<X> exceptionClass) throws X {
      Preconditions.checkNotNull(future);
      Preconditions.checkNotNull(unit);
      Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass), "Futures.get exception type (%s) must not be a RuntimeException", exceptionClass);

      try {
         return future.get(timeout, unit);
      } catch (InterruptedException var6) {
         Thread.currentThread().interrupt();
         throw newWithCause(exceptionClass, var6);
      } catch (TimeoutException var7) {
         throw newWithCause(exceptionClass, var7);
      } catch (ExecutionException var8) {
         wrapAndThrowExceptionOrError(var8.getCause(), exceptionClass);
         throw new AssertionError();
      }
   }

   private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable cause, Class<X> exceptionClass) throws X {
      if (cause instanceof Error) {
         throw new ExecutionError((Error)cause);
      } else if (cause instanceof RuntimeException) {
         throw new UncheckedExecutionException(cause);
      } else {
         throw newWithCause(exceptionClass, cause);
      }
   }

   public static <V> V getUnchecked(Future<V> future) {
      Preconditions.checkNotNull(future);

      try {
         return Uninterruptibles.getUninterruptibly(future);
      } catch (ExecutionException var2) {
         wrapAndThrowUnchecked(var2.getCause());
         throw new AssertionError();
      }
   }

   private static void wrapAndThrowUnchecked(Throwable cause) {
      if (cause instanceof Error) {
         throw new ExecutionError((Error)cause);
      } else {
         throw new UncheckedExecutionException(cause);
      }
   }

   private static <X extends Exception> X newWithCause(Class<X> exceptionClass, Throwable cause) {
      List<Constructor<X>> constructors = Arrays.asList(exceptionClass.getConstructors());
      Iterator i$ = preferringStrings(constructors).iterator();

      Exception instance;
      do {
         if (!i$.hasNext()) {
            throw new IllegalArgumentException("No appropriate constructor for exception of type " + exceptionClass + " in response to chained exception", cause);
         }

         Constructor<X> constructor = (Constructor)i$.next();
         instance = (Exception)newFromConstructor(constructor, cause);
      } while(instance == null);

      if (instance.getCause() == null) {
         instance.initCause(cause);
      }

      return instance;
   }

   private static <X extends Exception> List<Constructor<X>> preferringStrings(List<Constructor<X>> constructors) {
      return WITH_STRING_PARAM_FIRST.sortedCopy(constructors);
   }

   @Nullable
   private static <X> X newFromConstructor(Constructor<X> constructor, Throwable cause) {
      Class<?>[] paramTypes = constructor.getParameterTypes();
      Object[] params = new Object[paramTypes.length];

      for(int i = 0; i < paramTypes.length; ++i) {
         Class<?> paramType = paramTypes[i];
         if (paramType.equals(String.class)) {
            params[i] = cause.toString();
         } else {
            if (!paramType.equals(Throwable.class)) {
               return null;
            }

            params[i] = cause;
         }
      }

      try {
         return constructor.newInstance(params);
      } catch (IllegalArgumentException var6) {
         return null;
      } catch (InstantiationException var7) {
         return null;
      } catch (IllegalAccessException var8) {
         return null;
      } catch (InvocationTargetException var9) {
         return null;
      }
   }

   private static <V> ListenableFuture<List<V>> listFuture(ImmutableList<ListenableFuture<? extends V>> futures, boolean allMustSucceed, Executor listenerExecutor) {
      return new Futures.CombinedFuture(futures, allMustSucceed, listenerExecutor, new Futures.FutureCombiner<V, List<V>>() {
         public List<V> combine(List<Optional<V>> values) {
            List<V> result = Lists.newArrayList();
            Iterator i$ = values.iterator();

            while(i$.hasNext()) {
               Optional<V> element = (Optional)i$.next();
               result.add(element != null ? element.orNull() : null);
            }

            return Collections.unmodifiableList(result);
         }
      });
   }

   private static class MappingCheckedFuture<V, X extends Exception> extends AbstractCheckedFuture<V, X> {
      final Function<Exception, X> mapper;

      MappingCheckedFuture(ListenableFuture<V> delegate, Function<Exception, X> mapper) {
         super(delegate);
         this.mapper = (Function)Preconditions.checkNotNull(mapper);
      }

      protected X mapException(Exception e) {
         return (Exception)this.mapper.apply(e);
      }
   }

   private static class CombinedFuture<V, C> extends AbstractFuture<C> {
      private static final Logger logger = Logger.getLogger(Futures.CombinedFuture.class.getName());
      ImmutableCollection<? extends ListenableFuture<? extends V>> futures;
      final boolean allMustSucceed;
      final AtomicInteger remaining;
      Futures.FutureCombiner<V, C> combiner;
      List<Optional<V>> values;
      final Object seenExceptionsLock = new Object();
      Set<Throwable> seenExceptions;

      CombinedFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed, Executor listenerExecutor, Futures.FutureCombiner<V, C> combiner) {
         this.futures = futures;
         this.allMustSucceed = allMustSucceed;
         this.remaining = new AtomicInteger(futures.size());
         this.combiner = combiner;
         this.values = Lists.newArrayListWithCapacity(futures.size());
         this.init(listenerExecutor);
      }

      protected void init(Executor listenerExecutor) {
         this.addListener(new Runnable() {
            public void run() {
               if (CombinedFuture.this.isCancelled()) {
                  Iterator i$ = CombinedFuture.this.futures.iterator();

                  while(i$.hasNext()) {
                     ListenableFuture<?> future = (ListenableFuture)i$.next();
                     future.cancel(CombinedFuture.this.wasInterrupted());
                  }
               }

               CombinedFuture.this.futures = null;
               CombinedFuture.this.values = null;
               CombinedFuture.this.combiner = null;
            }
         }, MoreExecutors.sameThreadExecutor());
         if (this.futures.isEmpty()) {
            this.set(this.combiner.combine(ImmutableList.of()));
         } else {
            int i;
            for(i = 0; i < this.futures.size(); ++i) {
               this.values.add((Object)null);
            }

            i = 0;
            Iterator i$ = this.futures.iterator();

            while(i$.hasNext()) {
               final ListenableFuture<? extends V> listenable = (ListenableFuture)i$.next();
               final int index = i++;
               listenable.addListener(new Runnable() {
                  public void run() {
                     CombinedFuture.this.setOneValue(index, listenable);
                  }
               }, listenerExecutor);
            }

         }
      }

      private void setExceptionAndMaybeLog(Throwable throwable) {
         boolean visibleFromOutputFuture = false;
         boolean firstTimeSeeingThisException = true;
         if (this.allMustSucceed) {
            visibleFromOutputFuture = super.setException(throwable);
            synchronized(this.seenExceptionsLock) {
               if (this.seenExceptions == null) {
                  this.seenExceptions = Sets.newHashSet();
               }

               firstTimeSeeingThisException = this.seenExceptions.add(throwable);
            }
         }

         if (throwable instanceof Error || this.allMustSucceed && !visibleFromOutputFuture && firstTimeSeeingThisException) {
            logger.log(Level.SEVERE, "input future failed.", throwable);
         }

      }

      private void setOneValue(int index, Future<? extends V> future) {
         List<Optional<V>> localValues = this.values;
         if (this.isDone() || localValues == null) {
            Preconditions.checkState(this.allMustSucceed || this.isCancelled(), "Future was done before all dependencies completed");
         }

         boolean var13 = false;

         int newRemaining;
         Futures.FutureCombiner localCombiner;
         label303: {
            label304: {
               label305: {
                  try {
                     var13 = true;
                     Preconditions.checkState(future.isDone(), "Tried to set value from future which is not done");
                     Object returnValue = Uninterruptibles.getUninterruptibly(future);
                     if (localValues != null) {
                        localValues.set(index, Optional.fromNullable(returnValue));
                        var13 = false;
                     } else {
                        var13 = false;
                     }
                     break label303;
                  } catch (CancellationException var14) {
                     if (this.allMustSucceed) {
                        this.cancel(false);
                        var13 = false;
                     } else {
                        var13 = false;
                     }
                  } catch (ExecutionException var15) {
                     this.setExceptionAndMaybeLog(var15.getCause());
                     var13 = false;
                     break label305;
                  } catch (Throwable var16) {
                     this.setExceptionAndMaybeLog(var16);
                     var13 = false;
                     break label304;
                  } finally {
                     if (var13) {
                        int newRemaining = this.remaining.decrementAndGet();
                        Preconditions.checkState(newRemaining >= 0, "Less than 0 remaining futures");
                        if (newRemaining == 0) {
                           Futures.FutureCombiner<V, C> localCombiner = this.combiner;
                           if (localCombiner != null && localValues != null) {
                              this.set(localCombiner.combine(localValues));
                           } else {
                              Preconditions.checkState(this.isDone());
                           }
                        }

                     }
                  }

                  newRemaining = this.remaining.decrementAndGet();
                  Preconditions.checkState(newRemaining >= 0, "Less than 0 remaining futures");
                  if (newRemaining == 0) {
                     localCombiner = this.combiner;
                     if (localCombiner != null && localValues != null) {
                        this.set(localCombiner.combine(localValues));
                     } else {
                        Preconditions.checkState(this.isDone());
                     }

                     return;
                  }

                  return;
               }

               newRemaining = this.remaining.decrementAndGet();
               Preconditions.checkState(newRemaining >= 0, "Less than 0 remaining futures");
               if (newRemaining == 0) {
                  localCombiner = this.combiner;
                  if (localCombiner != null && localValues != null) {
                     this.set(localCombiner.combine(localValues));
                  } else {
                     Preconditions.checkState(this.isDone());
                  }

                  return;
               }

               return;
            }

            newRemaining = this.remaining.decrementAndGet();
            Preconditions.checkState(newRemaining >= 0, "Less than 0 remaining futures");
            if (newRemaining == 0) {
               localCombiner = this.combiner;
               if (localCombiner != null && localValues != null) {
                  this.set(localCombiner.combine(localValues));
               } else {
                  Preconditions.checkState(this.isDone());
               }

               return;
            }

            return;
         }

         newRemaining = this.remaining.decrementAndGet();
         Preconditions.checkState(newRemaining >= 0, "Less than 0 remaining futures");
         if (newRemaining == 0) {
            localCombiner = this.combiner;
            if (localCombiner != null && localValues != null) {
               this.set(localCombiner.combine(localValues));
            } else {
               Preconditions.checkState(this.isDone());
            }
         }

      }
   }

   private interface FutureCombiner<V, C> {
      C combine(List<Optional<V>> var1);
   }

   private static class NonCancellationPropagatingFuture<V> extends AbstractFuture<V> {
      NonCancellationPropagatingFuture(final ListenableFuture<V> delegate) {
         Preconditions.checkNotNull(delegate);
         Futures.addCallback(delegate, new FutureCallback<V>() {
            public void onSuccess(V result) {
               NonCancellationPropagatingFuture.this.set(result);
            }

            public void onFailure(Throwable t) {
               if (delegate.isCancelled()) {
                  NonCancellationPropagatingFuture.this.cancel(false);
               } else {
                  NonCancellationPropagatingFuture.this.setException(t);
               }

            }
         }, MoreExecutors.sameThreadExecutor());
      }
   }

   private static class ChainingListenableFuture<I, O> extends AbstractFuture<O> implements Runnable {
      private AsyncFunction<? super I, ? extends O> function;
      private ListenableFuture<? extends I> inputFuture;
      private volatile ListenableFuture<? extends O> outputFuture;
      private final CountDownLatch outputCreated;

      private ChainingListenableFuture(AsyncFunction<? super I, ? extends O> function, ListenableFuture<? extends I> inputFuture) {
         this.outputCreated = new CountDownLatch(1);
         this.function = (AsyncFunction)Preconditions.checkNotNull(function);
         this.inputFuture = (ListenableFuture)Preconditions.checkNotNull(inputFuture);
      }

      public boolean cancel(boolean mayInterruptIfRunning) {
         if (super.cancel(mayInterruptIfRunning)) {
            this.cancel(this.inputFuture, mayInterruptIfRunning);
            this.cancel(this.outputFuture, mayInterruptIfRunning);
            return true;
         } else {
            return false;
         }
      }

      private void cancel(@Nullable Future<?> future, boolean mayInterruptIfRunning) {
         if (future != null) {
            future.cancel(mayInterruptIfRunning);
         }

      }

      public void run() {
         try {
            try {
               Object sourceResult;
               try {
                  sourceResult = Uninterruptibles.getUninterruptibly(this.inputFuture);
               } catch (CancellationException var9) {
                  this.cancel(false);
                  return;
               } catch (ExecutionException var10) {
                  this.setException(var10.getCause());
                  return;
               }

               final ListenableFuture<? extends O> outputFuture = this.outputFuture = this.function.apply(sourceResult);
               if (this.isCancelled()) {
                  outputFuture.cancel(this.wasInterrupted());
                  this.outputFuture = null;
                  return;
               }

               outputFuture.addListener(new Runnable() {
                  public void run() {
                     try {
                        ChainingListenableFuture.this.set(Uninterruptibles.getUninterruptibly(outputFuture));
                        return;
                     } catch (CancellationException var6) {
                        ChainingListenableFuture.this.cancel(false);
                     } catch (ExecutionException var7) {
                        ChainingListenableFuture.this.setException(var7.getCause());
                        return;
                     } finally {
                        ChainingListenableFuture.this.outputFuture = null;
                     }

                  }
               }, MoreExecutors.sameThreadExecutor());
            } catch (UndeclaredThrowableException var11) {
               this.setException(var11.getCause());
            } catch (Throwable var12) {
               this.setException(var12);
            }

         } finally {
            this.function = null;
            this.inputFuture = null;
            this.outputCreated.countDown();
         }
      }

      // $FF: synthetic method
      ChainingListenableFuture(AsyncFunction x0, ListenableFuture x1, Object x2) {
         this(x0, x1);
      }
   }

   private static class FallbackFuture<V> extends AbstractFuture<V> {
      private volatile ListenableFuture<? extends V> running;

      FallbackFuture(ListenableFuture<? extends V> input, final FutureFallback<? extends V> fallback, Executor executor) {
         this.running = input;
         Futures.addCallback(this.running, new FutureCallback<V>() {
            public void onSuccess(V value) {
               FallbackFuture.this.set(value);
            }

            public void onFailure(Throwable t) {
               if (!FallbackFuture.this.isCancelled()) {
                  try {
                     FallbackFuture.this.running = fallback.create(t);
                     if (FallbackFuture.this.isCancelled()) {
                        FallbackFuture.this.running.cancel(FallbackFuture.this.wasInterrupted());
                        return;
                     }

                     Futures.addCallback(FallbackFuture.this.running, new FutureCallback<V>() {
                        public void onSuccess(V value) {
                           FallbackFuture.this.set(value);
                        }

                        public void onFailure(Throwable t) {
                           if (FallbackFuture.this.running.isCancelled()) {
                              FallbackFuture.this.cancel(false);
                           } else {
                              FallbackFuture.this.setException(t);
                           }

                        }
                     }, MoreExecutors.sameThreadExecutor());
                  } catch (Throwable var3) {
                     FallbackFuture.this.setException(var3);
                  }

               }
            }
         }, executor);
      }

      public boolean cancel(boolean mayInterruptIfRunning) {
         if (super.cancel(mayInterruptIfRunning)) {
            this.running.cancel(mayInterruptIfRunning);
            return true;
         } else {
            return false;
         }
      }
   }

   private static class ImmediateFailedCheckedFuture<V, X extends Exception> extends Futures.ImmediateFuture<V> implements CheckedFuture<V, X> {
      private final X thrown;

      ImmediateFailedCheckedFuture(X thrown) {
         super(null);
         this.thrown = thrown;
      }

      public V get() throws ExecutionException {
         throw new ExecutionException(this.thrown);
      }

      public V checkedGet() throws X {
         throw this.thrown;
      }

      public V checkedGet(long timeout, TimeUnit unit) throws X {
         Preconditions.checkNotNull(unit);
         throw this.thrown;
      }
   }

   private static class ImmediateCancelledFuture<V> extends Futures.ImmediateFuture<V> {
      private final CancellationException thrown = new CancellationException("Immediate cancelled future.");

      ImmediateCancelledFuture() {
         super(null);
      }

      public boolean isCancelled() {
         return true;
      }

      public V get() {
         throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.thrown);
      }
   }

   private static class ImmediateFailedFuture<V> extends Futures.ImmediateFuture<V> {
      private final Throwable thrown;

      ImmediateFailedFuture(Throwable thrown) {
         super(null);
         this.thrown = thrown;
      }

      public V get() throws ExecutionException {
         throw new ExecutionException(this.thrown);
      }
   }

   private static class ImmediateSuccessfulCheckedFuture<V, X extends Exception> extends Futures.ImmediateFuture<V> implements CheckedFuture<V, X> {
      @Nullable
      private final V value;

      ImmediateSuccessfulCheckedFuture(@Nullable V value) {
         super(null);
         this.value = value;
      }

      public V get() {
         return this.value;
      }

      public V checkedGet() {
         return this.value;
      }

      public V checkedGet(long timeout, TimeUnit unit) {
         Preconditions.checkNotNull(unit);
         return this.value;
      }
   }

   private static class ImmediateSuccessfulFuture<V> extends Futures.ImmediateFuture<V> {
      @Nullable
      private final V value;

      ImmediateSuccessfulFuture(@Nullable V value) {
         super(null);
         this.value = value;
      }

      public V get() {
         return this.value;
      }
   }

   private abstract static class ImmediateFuture<V> implements ListenableFuture<V> {
      private static final Logger log = Logger.getLogger(Futures.ImmediateFuture.class.getName());

      private ImmediateFuture() {
      }

      public void addListener(Runnable listener, Executor executor) {
         Preconditions.checkNotNull(listener, "Runnable was null.");
         Preconditions.checkNotNull(executor, "Executor was null.");

         try {
            executor.execute(listener);
         } catch (RuntimeException var4) {
            log.log(Level.SEVERE, "RuntimeException while executing runnable " + listener + " with executor " + executor, var4);
         }

      }

      public boolean cancel(boolean mayInterruptIfRunning) {
         return false;
      }

      public abstract V get() throws ExecutionException;

      public V get(long timeout, TimeUnit unit) throws ExecutionException {
         Preconditions.checkNotNull(unit);
         return this.get();
      }

      public boolean isCancelled() {
         return false;
      }

      public boolean isDone() {
         return true;
      }

      // $FF: synthetic method
      ImmediateFuture(Object x0) {
         this();
      }
   }
}
