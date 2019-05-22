package fj.test.reflect;

import fj.P2;
import fj.data.Array;
import fj.function.Effect1;
import fj.test.CheckResult;

public final class Main {
   private Main() {
      throw new UnsupportedOperationException();
   }

   public static void main(String... args) {
      if (args.length == 0) {
         System.err.println("<class> [category]*");
         System.exit(441);
      } else {
         try {
            Check.check(Class.forName(args[0]), Array.array((Object[])args).toList().tail()).foreachDoEffect(new Effect1<P2<String, CheckResult>>() {
               public void f(P2<String, CheckResult> r) {
                  CheckResult.summary.print(r._2());
                  System.out.println(" (" + (String)r._1() + ')');
               }
            });
         } catch (ClassNotFoundException var2) {
            System.err.println(var2);
            System.exit(144);
         }
      }

   }
}
