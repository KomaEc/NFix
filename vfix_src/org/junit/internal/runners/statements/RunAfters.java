package org.junit.internal.runners.statements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

public class RunAfters extends Statement {
   private final Statement next;
   private final Object target;
   private final List<FrameworkMethod> afters;

   public RunAfters(Statement next, List<FrameworkMethod> afters, Object target) {
      this.next = next;
      this.afters = afters;
      this.target = target;
   }

   public void evaluate() throws Throwable {
      ArrayList errors = new ArrayList();
      boolean var14 = false;

      label133: {
         Iterator i$;
         FrameworkMethod each;
         label134: {
            try {
               var14 = true;
               this.next.evaluate();
               var14 = false;
               break label134;
            } catch (Throwable var18) {
               errors.add(var18);
               var14 = false;
            } finally {
               if (var14) {
                  Iterator i$ = this.afters.iterator();

                  while(i$.hasNext()) {
                     FrameworkMethod each = (FrameworkMethod)i$.next();

                     try {
                        each.invokeExplosively(this.target);
                     } catch (Throwable var15) {
                        errors.add(var15);
                     }
                  }

               }
            }

            i$ = this.afters.iterator();

            while(true) {
               if (!i$.hasNext()) {
                  break label133;
               }

               each = (FrameworkMethod)i$.next();

               try {
                  each.invokeExplosively(this.target);
               } catch (Throwable var16) {
                  errors.add(var16);
               }
            }
         }

         i$ = this.afters.iterator();

         while(i$.hasNext()) {
            each = (FrameworkMethod)i$.next();

            try {
               each.invokeExplosively(this.target);
            } catch (Throwable var17) {
               errors.add(var17);
            }
         }
      }

      MultipleFailureException.assertEmpty(errors);
   }
}
