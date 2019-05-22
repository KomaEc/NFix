package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractExecutorService implements ExecutorService {
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   protected RunnableFuture newTaskFor(Runnable runnable, Object value) {
      return new FutureTask(runnable, value);
   }

   protected RunnableFuture newTaskFor(Callable callable) {
      return new FutureTask(callable);
   }

   public Future submit(Runnable task) {
      if (task == null) {
         throw new NullPointerException();
      } else {
         RunnableFuture ftask = this.newTaskFor(task, (Object)null);
         this.execute(ftask);
         return ftask;
      }
   }

   public Future submit(Runnable task, Object result) {
      if (task == null) {
         throw new NullPointerException();
      } else {
         RunnableFuture ftask = this.newTaskFor(task, result);
         this.execute(ftask);
         return ftask;
      }
   }

   public Future submit(Callable task) {
      if (task == null) {
         throw new NullPointerException();
      } else {
         RunnableFuture ftask = this.newTaskFor(task);
         this.execute(ftask);
         return ftask;
      }
   }

   private Object doInvokeAny(Collection tasks, boolean timed, long nanos) throws InterruptedException, ExecutionException, TimeoutException {
      if (tasks == null) {
         throw new NullPointerException();
      } else {
         int ntasks = tasks.size();
         if (ntasks == 0) {
            throw new IllegalArgumentException();
         } else {
            List futures = new ArrayList(ntasks);
            ExecutorCompletionService ecs = new ExecutorCompletionService(this);

            try {
               ExecutionException ee = null;
               long lastTime = timed ? Utils.nanoTime() : 0L;
               Iterator it = tasks.iterator();
               futures.add(ecs.submit((Callable)it.next()));
               --ntasks;
               int active = 1;

               while(true) {
                  Future f = ecs.poll();
                  if (f == null) {
                     if (ntasks > 0) {
                        --ntasks;
                        futures.add(ecs.submit((Callable)it.next()));
                        ++active;
                     } else {
                        if (active == 0) {
                           if (ee == null) {
                              ee = new ExecutionException();
                           }

                           throw ee;
                        }

                        if (timed) {
                           f = ecs.poll(nanos, TimeUnit.NANOSECONDS);
                           if (f == null) {
                              throw new TimeoutException();
                           }

                           long now = Utils.nanoTime();
                           nanos -= now - lastTime;
                           lastTime = now;
                        } else {
                           f = ecs.take();
                        }
                     }
                  }

                  if (f != null) {
                     --active;

                     try {
                        Object var16 = f.get();
                        return var16;
                     } catch (InterruptedException var24) {
                        throw var24;
                     } catch (ExecutionException var25) {
                        ee = var25;
                     } catch (RuntimeException var26) {
                        ee = new ExecutionException(var26);
                     }
                  }
               }
            } finally {
               Iterator f = futures.iterator();

               while(f.hasNext()) {
                  ((Future)f.next()).cancel(true);
               }

            }
         }
      }
   }

   public Object invokeAny(Collection tasks) throws InterruptedException, ExecutionException {
      try {
         return this.doInvokeAny(tasks, false, 0L);
      } catch (TimeoutException var3) {
         if (!$assertionsDisabled) {
            throw new AssertionError();
         } else {
            return null;
         }
      }
   }

   public Object invokeAny(Collection tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
      return this.doInvokeAny(tasks, true, unit.toNanos(timeout));
   }

   public List invokeAll(Collection tasks) throws InterruptedException {
      if (tasks == null) {
         throw new NullPointerException();
      } else {
         List futures = new ArrayList(tasks.size());
         boolean done = false;

         ArrayList var17;
         try {
            Iterator i = tasks.iterator();

            while(i.hasNext()) {
               RunnableFuture f = this.newTaskFor((Callable)i.next());
               futures.add(f);
               this.execute(f);
            }

            i = futures.iterator();

            while(i.hasNext()) {
               Future f = (Future)i.next();
               if (!f.isDone()) {
                  try {
                     f.get();
                  } catch (CancellationException var14) {
                  } catch (ExecutionException var15) {
                  }
               }
            }

            done = true;
            var17 = futures;
         } finally {
            if (!done) {
               Iterator i = futures.iterator();

               while(i.hasNext()) {
                  Future f = (Future)i.next();
                  f.cancel(true);
               }
            }

         }

         return var17;
      }
   }

   public List invokeAll(Collection tasks, long timeout, TimeUnit unit) throws InterruptedException {
      if (tasks != null && unit != null) {
         long nanos = unit.toNanos(timeout);
         List futures = new ArrayList(tasks.size());
         boolean done = false;

         try {
            Iterator t = tasks.iterator();

            while(t.hasNext()) {
               futures.add(this.newTaskFor((Callable)t.next()));
            }

            long lastTime = Utils.nanoTime();
            Iterator it = futures.iterator();

            ArrayList var15;
            while(it.hasNext()) {
               this.execute((Runnable)it.next());
               long now = Utils.nanoTime();
               nanos -= now - lastTime;
               lastTime = now;
               if (nanos <= 0L) {
                  var15 = futures;
                  return var15;
               }
            }

            Iterator i = futures.iterator();

            while(i.hasNext()) {
               Future f = (Future)i.next();
               if (!f.isDone()) {
                  if (nanos <= 0L) {
                     var15 = futures;
                     return var15;
                  }

                  try {
                     f.get(nanos, TimeUnit.NANOSECONDS);
                  } catch (CancellationException var29) {
                  } catch (ExecutionException var30) {
                  } catch (TimeoutException var31) {
                     ArrayList var18 = futures;
                     return var18;
                  }

                  long now = Utils.nanoTime();
                  nanos -= now - lastTime;
                  lastTime = now;
               }
            }

            done = true;
            ArrayList var33 = futures;
            return var33;
         } finally {
            if (!done) {
               Iterator i = futures.iterator();

               while(i.hasNext()) {
                  Future f = (Future)i.next();
                  f.cancel(true);
               }
            }

         }
      } else {
         throw new NullPointerException();
      }
   }

   static {
      $assertionsDisabled = !AbstractExecutorService.class.desiredAssertionStatus();
   }
}
