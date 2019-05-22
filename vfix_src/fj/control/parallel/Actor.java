package fj.control.parallel;

import fj.Effect;
import fj.F;
import fj.P1;
import fj.Unit;
import fj.function.Effect1;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Actor<A> {
   private final Strategy<Unit> s;
   private final F<A, P1<Unit>> f;

   public static <T> Actor<T> queueActor(final Strategy<Unit> s, final Effect1<T> ea) {
      return actor(Strategy.seqStrategy(), new Effect1<T>() {
         AtomicBoolean suspended = new AtomicBoolean(true);
         ConcurrentLinkedQueue<T> mbox = new ConcurrentLinkedQueue();
         P1<Unit> processor = new P1<Unit>() {
            public Unit _1() {
               T a = mbox.poll();
               if (a != null) {
                  ea.f(a);
                  s.par(this);
               } else {
                  suspended.set(true);
                  work();
               }

               return Unit.unit();
            }
         };

         public void f(T a) {
            this.mbox.offer(a);
            this.work();
         }

         protected void work() {
            if (!this.mbox.isEmpty() && this.suspended.compareAndSet(true, false)) {
               s.par(this.processor);
            }

         }
      });
   }

   private Actor(final Strategy<Unit> s, final F<A, P1<Unit>> e) {
      this.s = s;
      this.f = new F<A, P1<Unit>>() {
         public P1<Unit> f(A a) {
            return s.par((P1)e.f(a));
         }
      };
   }

   public static <A> Actor<A> actor(Strategy<Unit> s, Effect1<A> e) {
      return new Actor(s, P1.curry(Effect.f(e)));
   }

   public static <A> Actor<A> actor(Strategy<Unit> s, F<A, P1<Unit>> e) {
      return new Actor(s, e);
   }

   public P1<Unit> act(A a) {
      return (P1)this.f.f(a);
   }

   public <B> Actor<B> comap(final F<B, A> f) {
      return actor(this.s, new F<B, P1<Unit>>() {
         public P1<Unit> f(B b) {
            return Actor.this.act(f.f(b));
         }
      });
   }

   public Actor<Promise<A>> promise() {
      return actor(this.s, new Effect1<Promise<A>>() {
         public void f(Promise<A> b) {
            b.to(Actor.this);
         }
      });
   }
}
