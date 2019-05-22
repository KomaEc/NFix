package fj.data;

import fj.F;
import fj.F1Functions;
import fj.function.Effect1;
import java.util.Collection;
import java.util.Iterator;

public final class NonEmptyList<A> implements Iterable<A> {
   public final A head;
   public final List<A> tail;

   public Iterator<A> iterator() {
      return this.toCollection().iterator();
   }

   private NonEmptyList(A head, List<A> tail) {
      this.head = head;
      this.tail = tail;
   }

   public NonEmptyList<A> cons(A a) {
      return nel(a, this.tail.cons(this.head));
   }

   public NonEmptyList<A> append(NonEmptyList<A> as) {
      List.Buffer<A> b = new List.Buffer();
      b.append(this.tail);
      b.snoc(as.head);
      b.append(as.tail);
      List<A> bb = b.toList();
      return nel(this.head, bb);
   }

   public <B> NonEmptyList<B> map(F<A, B> f) {
      return nel(f.f(this.head), this.tail.map(f));
   }

   public <B> NonEmptyList<B> bind(final F<A, NonEmptyList<B>> f) {
      final List.Buffer<B> b = new List.Buffer();
      NonEmptyList<B> p = (NonEmptyList)f.f(this.head);
      b.snoc(p.head);
      b.append(p.tail);
      this.tail.foreachDoEffect(new Effect1<A>() {
         public void f(A a) {
            NonEmptyList<B> p = (NonEmptyList)f.f(a);
            b.snoc(p.head);
            b.append(p.tail);
         }
      });
      List<B> bb = b.toList();
      return nel(bb.head(), bb.tail());
   }

   public NonEmptyList<NonEmptyList<A>> sublists() {
      return (NonEmptyList)fromList(Option.somes(this.toList().toStream().substreams().map(F1Functions.o(new F<List<A>, Option<NonEmptyList<A>>>() {
         public Option<NonEmptyList<A>> f(List<A> list) {
            return NonEmptyList.fromList(list);
         }
      }, Conversions.Stream_List())).toList())).some();
   }

   public NonEmptyList<NonEmptyList<A>> tails() {
      return (NonEmptyList)fromList(Option.somes(this.toList().tails().map(new F<List<A>, Option<NonEmptyList<A>>>() {
         public Option<NonEmptyList<A>> f(List<A> list) {
            return NonEmptyList.fromList(list);
         }
      }))).some();
   }

   public <B> NonEmptyList<B> mapTails(F<NonEmptyList<A>, B> f) {
      return this.tails().map(f);
   }

   public List<A> toList() {
      return this.tail.cons(this.head);
   }

   public Collection<A> toCollection() {
      return this.toList().toCollection();
   }

   public static <A> F<NonEmptyList<A>, List<A>> toList_() {
      return new F<NonEmptyList<A>, List<A>>() {
         public List<A> f(NonEmptyList<A> as) {
            return as.toList();
         }
      };
   }

   public static <A> NonEmptyList<A> nel(A head, List<A> tail) {
      return new NonEmptyList(head, tail);
   }

   public static <A> NonEmptyList<A> nel(A head) {
      return nel(head, List.nil());
   }

   public static <A> F<A, NonEmptyList<A>> nel() {
      return new F<A, NonEmptyList<A>>() {
         public NonEmptyList<A> f(A a) {
            return NonEmptyList.nel(a);
         }
      };
   }

   public static <A> Option<NonEmptyList<A>> fromList(List<A> as) {
      return as.isEmpty() ? Option.none() : Option.some(nel(as.head(), as.tail()));
   }
}
