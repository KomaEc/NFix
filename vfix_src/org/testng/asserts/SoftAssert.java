package org.testng.asserts;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.testng.collections.Maps;

public class SoftAssert extends Assertion {
   private Map<AssertionError, IAssert<?>> m_errors = Maps.newLinkedHashMap();

   protected void doAssert(IAssert<?> a) {
      this.onBeforeAssert(a);

      try {
         a.doAssert();
         this.onAssertSuccess(a);
      } catch (AssertionError var6) {
         this.onAssertFailure(a, var6);
         this.m_errors.put(var6, a);
      } finally {
         this.onAfterAssert(a);
      }

   }

   public void assertAll() {
      if (!this.m_errors.isEmpty()) {
         StringBuilder sb = new StringBuilder("The following asserts failed:");
         boolean first = true;

         Entry ae;
         for(Iterator i$ = this.m_errors.entrySet().iterator(); i$.hasNext(); sb.append(((AssertionError)ae.getKey()).getMessage())) {
            ae = (Entry)i$.next();
            if (first) {
               first = false;
            } else {
               sb.append(", ");
            }

            sb.append("\n\t");
            String message = ((IAssert)ae.getValue()).getMessage();
            if (message != null) {
               sb.append(message).append("\t");
            }
         }

         throw new AssertionError(sb.toString());
      }
   }
}
