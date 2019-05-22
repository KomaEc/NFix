package fj.test;

import fj.Bottom;
import fj.F;
import fj.Show;
import fj.data.List;
import fj.data.Option;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class CheckResult {
   private final CheckResult.R r;
   private final Option<List<Arg<?>>> args;
   private final Option<Throwable> ex;
   private final int succeeded;
   private final int discarded;
   public static final Show<CheckResult> summary;
   public static final Show<CheckResult> summaryEx;

   private CheckResult(CheckResult.R r, Option<List<Arg<?>>> args, Option<Throwable> ex, int succeeded, int discarded) {
      this.r = r;
      this.args = args;
      this.ex = ex;
      this.succeeded = succeeded;
      this.discarded = discarded;
   }

   public static CheckResult passed(int succeeded, int discarded) {
      return new CheckResult(CheckResult.R.Passed, Option.none(), Option.none(), succeeded, discarded);
   }

   public static CheckResult proven(List<Arg<?>> args, int succeeded, int discarded) {
      return new CheckResult(CheckResult.R.Proven, Option.some(args), Option.none(), succeeded, discarded);
   }

   public static CheckResult falsified(List<Arg<?>> args, int succeeded, int discarded) {
      return new CheckResult(CheckResult.R.Falsified, Option.some(args), Option.none(), succeeded, discarded);
   }

   public static CheckResult exhausted(int succeeded, int discarded) {
      return new CheckResult(CheckResult.R.Exhausted, Option.none(), Option.none(), succeeded, discarded);
   }

   public static CheckResult propException(List<Arg<?>> args, Throwable ex, int succeeded, int discarded) {
      return new CheckResult(CheckResult.R.PropException, Option.some(args), Option.some(ex), succeeded, discarded);
   }

   public static CheckResult genException(Throwable ex, int succeeded, int discarded) {
      return new CheckResult(CheckResult.R.GenException, Option.none(), Option.some(ex), succeeded, discarded);
   }

   public boolean isPassed() {
      return this.r == CheckResult.R.Passed;
   }

   public boolean isProven() {
      return this.r == CheckResult.R.Proven;
   }

   public boolean isFalsified() {
      return this.r == CheckResult.R.Falsified;
   }

   public boolean isExhausted() {
      return this.r == CheckResult.R.Exhausted;
   }

   public boolean isPropException() {
      return this.r == CheckResult.R.PropException;
   }

   public boolean isGenException() {
      return this.r == CheckResult.R.GenException;
   }

   public Option<List<Arg<?>>> args() {
      return this.args;
   }

   public Option<Throwable> exception() {
      return this.ex;
   }

   public int succeeded() {
      return this.succeeded;
   }

   public int discarded() {
      return this.discarded;
   }

   public static Show<CheckResult> summary(final Show<Arg<?>> sa) {
      return Show.showS(new F<CheckResult, String>() {
         private String test(CheckResult r) {
            return r.succeeded() == 1 ? "test" : "tests";
         }

         private String arguments(CheckResult r) {
            List<Arg<?>> args = (List)r.args().some();
            return args.length() == 1 ? "argument: " + sa.showS(args.head()) : "arguments: " + Show.listShow(sa).showS((Object)args);
         }

         public String f(CheckResult r) {
            if (r.isProven()) {
               return "OK, property proven with " + this.arguments(r);
            } else if (r.isPassed()) {
               return "OK, passed " + r.succeeded() + ' ' + this.test(r) + (r.discarded() > 0 ? " (" + r.discarded() + " discarded)" : "") + '.';
            } else if (r.isFalsified()) {
               return "Falsified after " + r.succeeded() + " passed " + this.test(r) + " with " + this.arguments(r);
            } else if (r.isExhausted()) {
               return "Gave up after " + r.succeeded() + " passed " + this.test(r) + " and " + r.discarded() + " discarded tests.";
            } else {
               StringWriter sw;
               PrintWriter pw;
               if (r.isPropException()) {
                  sw = new StringWriter();
                  pw = new PrintWriter(sw);
                  ((Throwable)r.exception().some()).printStackTrace(pw);
                  return "Exception on property evaluation with " + this.arguments(r) + System.getProperty("line.separator") + sw;
               } else if (r.isGenException()) {
                  sw = new StringWriter();
                  pw = new PrintWriter(sw);
                  ((Throwable)r.exception().some()).printStackTrace(pw);
                  return "Exception on argument generation " + System.getProperty("line.separator") + sw;
               } else {
                  throw Bottom.decons(r.getClass());
               }
            }
         }
      });
   }

   public static Show<CheckResult> summaryEx(final Show<Arg<?>> sa) {
      return Show.showS(new F<CheckResult, String>() {
         public String f(CheckResult r) {
            String s = CheckResult.summary(sa).show((Object)r).toString();
            if (!r.isProven() && !r.isPassed() && !r.isExhausted()) {
               if (!r.isFalsified() && !r.isPropException() && !r.isGenException()) {
                  throw Bottom.decons(r.getClass());
               } else {
                  throw new Error(s);
               }
            } else {
               return s;
            }
         }
      });
   }

   static {
      summary = summary(Arg.argShow);
      summaryEx = summaryEx(Arg.argShow);
   }

   private static enum R {
      Passed,
      Proven,
      Falsified,
      Exhausted,
      PropException,
      GenException;
   }
}
