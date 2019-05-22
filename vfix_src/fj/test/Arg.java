package fj.test;

import fj.F;
import fj.Show;

public final class Arg<T> {
   private final T value;
   private final int shrinks;
   public static final Show<Arg<?>> argShow = Show.showS(new F<Arg<?>, String>() {
      public String f(Arg<?> arg) {
         return Show.anyShow().showS(arg.value) + (arg.shrinks > 0 ? " (" + arg.shrinks + " shrink" + (arg.shrinks == 1 ? "" : 's') + ')' : "");
      }
   });

   private Arg(T value, int shrinks) {
      this.value = value;
      this.shrinks = shrinks;
   }

   public static <T> Arg<T> arg(T value, int shrinks) {
      return new Arg(value, shrinks);
   }

   public Object value() {
      return this.value;
   }

   public int shrinks() {
      return this.shrinks;
   }
}
