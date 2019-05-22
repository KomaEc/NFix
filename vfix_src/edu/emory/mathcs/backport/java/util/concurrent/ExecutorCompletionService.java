package edu.emory.mathcs.backport.java.util.concurrent;

public class ExecutorCompletionService implements CompletionService {
   private final Executor executor;
   private final AbstractExecutorService aes;
   private final BlockingQueue completionQueue;

   private RunnableFuture newTaskFor(Callable task) {
      return (RunnableFuture)(this.aes == null ? new FutureTask(task) : this.aes.newTaskFor(task));
   }

   private RunnableFuture newTaskFor(Runnable task, Object result) {
      return (RunnableFuture)(this.aes == null ? new FutureTask(task, result) : this.aes.newTaskFor(task, result));
   }

   public ExecutorCompletionService(Executor executor) {
      if (executor == null) {
         throw new NullPointerException();
      } else {
         this.executor = executor;
         this.aes = executor instanceof AbstractExecutorService ? (AbstractExecutorService)executor : null;
         this.completionQueue = new LinkedBlockingQueue();
      }
   }

   public ExecutorCompletionService(Executor executor, BlockingQueue completionQueue) {
      if (executor != null && completionQueue != null) {
         this.executor = executor;
         this.aes = executor instanceof AbstractExecutorService ? (AbstractExecutorService)executor : null;
         this.completionQueue = completionQueue;
      } else {
         throw new NullPointerException();
      }
   }

   public Future submit(Callable task) {
      if (task == null) {
         throw new NullPointerException();
      } else {
         RunnableFuture f = this.newTaskFor(task);
         this.executor.execute(new ExecutorCompletionService.QueueingFuture(f));
         return f;
      }
   }

   public Future submit(Runnable task, Object result) {
      if (task == null) {
         throw new NullPointerException();
      } else {
         RunnableFuture f = this.newTaskFor(task, result);
         this.executor.execute(new ExecutorCompletionService.QueueingFuture(f));
         return f;
      }
   }

   public Future take() throws InterruptedException {
      return (Future)this.completionQueue.take();
   }

   public Future poll() {
      return (Future)this.completionQueue.poll();
   }

   public Future poll(long timeout, TimeUnit unit) throws InterruptedException {
      return (Future)this.completionQueue.poll(timeout, unit);
   }

   private class QueueingFuture extends FutureTask {
      private final Future task;

      QueueingFuture(RunnableFuture task) {
         super(task, (Object)null);
         this.task = task;
      }

      protected void done() {
         ExecutorCompletionService.this.completionQueue.add(this.task);
      }
   }
}
