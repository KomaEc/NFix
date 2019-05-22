package fj.test;

import fj.F;
import fj.P1;
import fj.data.List;
import fj.data.Option;

public final class Result {
   private final Option<List<Arg<?>>> args;
   private final Result.R r;
   private final Option<Throwable> t;

   private Result(Option<List<Arg<?>>> args, Result.R r, Option<Throwable> t) {
      this.args = args;
      this.r = r;
      this.t = t;
   }

   public Option<List<Arg<?>>> args() {
      return this.args;
   }

   public Option<Throwable> exception() {
      return this.t;
   }

   public boolean isUnfalsified() {
      return this.r == Result.R.Unfalsified;
   }

   public boolean isFalsified() {
      return this.r == Result.R.Falsified;
   }

   public boolean isProven() {
      return this.r == Result.R.Proven;
   }

   public boolean isException() {
      return this.r == Result.R.Exception;
   }

   public boolean isNoResult() {
      return this.r == Result.R.NoResult;
   }

   public boolean failed() {
      return this.isFalsified() || this.isException();
   }

   public boolean passed() {
      return this.isUnfalsified() || this.isProven();
   }

   public Result provenAsUnfalsified() {
      return this.isProven() ? unfalsified((List)this.args.some()) : this;
   }

   public Result addArg(Arg<?> a) {
      F<Arg<?>, F<List<Arg<?>>, List<Arg<?>>>> cons = List.cons();
      return new Result(this.args.map((F)cons.f(a)), this.r, this.t);
   }

   public Option<Result> toOption() {
      return this.isNoResult() ? Option.none() : Option.some(this);
   }

   public static Result noResult(Option<Result> r) {
      return (Result)r.orSome(new P1<Result>() {
         public Result _1() {
            return Result.noResult();
         }
      });
   }

   public static Result noResult() {
      return new Result(Option.none(), Result.R.NoResult, Option.none());
   }

   public static Result unfalsified(List<Arg<?>> args) {
      return new Result(Option.some(args), Result.R.Unfalsified, Option.none());
   }

   public static Result falsified(List<Arg<?>> args) {
      return new Result(Option.some(args), Result.R.Falsified, Option.none());
   }

   public static Result proven(List<Arg<?>> args) {
      return new Result(Option.some(args), Result.R.Proven, Option.none());
   }

   public static Result exception(List<Arg<?>> args, Throwable t) {
      return new Result(Option.some(args), Result.R.Exception, Option.some(t));
   }

   private static enum R {
      Unfalsified,
      Falsified,
      Proven,
      Exception,
      NoResult;
   }
}
