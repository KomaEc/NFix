package fj.control.db;

import fj.F;
import fj.Function;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public abstract class DB<A> {
   public abstract A run(Connection var1) throws SQLException;

   public static <A> DB<A> db(final F<Connection, A> f) {
      return new DB<A>() {
         public A run(Connection c) {
            return f.f(c);
         }
      };
   }

   public final F<Connection, Callable<A>> asFunction() {
      return new F<Connection, Callable<A>>() {
         public Callable<A> f(final Connection c) {
            return new Callable<A>() {
               public A call() throws Exception {
                  return DB.this.run(c);
               }
            };
         }
      };
   }

   public final <B> DB<B> map(final F<A, B> f) {
      return new DB<B>() {
         public B run(Connection c) throws SQLException {
            return f.f(DB.this.run(c));
         }
      };
   }

   public static <A, B> F<DB<A>, DB<B>> liftM(final F<A, B> f) {
      return new F<DB<A>, DB<B>>() {
         public DB<B> f(DB<A> a) {
            return a.map(f);
         }
      };
   }

   public static <A> DB<A> unit(final A a) {
      return new DB<A>() {
         public A run(Connection c) {
            return a;
         }
      };
   }

   public final <B> DB<B> bind(final F<A, DB<B>> f) {
      return new DB<B>() {
         public B run(Connection c) throws SQLException {
            return ((DB)f.f(DB.this.run(c))).run(c);
         }
      };
   }

   public static <A> DB<A> join(DB<DB<A>> a) {
      return a.bind(Function.identity());
   }
}
