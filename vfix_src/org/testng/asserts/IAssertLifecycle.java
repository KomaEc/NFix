package org.testng.asserts;

public interface IAssertLifecycle {
   void executeAssert(IAssert<?> var1);

   void onAssertSuccess(IAssert<?> var1);

   /** @deprecated */
   void onAssertFailure(IAssert<?> var1);

   void onAssertFailure(IAssert<?> var1, AssertionError var2);

   void onBeforeAssert(IAssert<?> var1);

   void onAfterAssert(IAssert<?> var1);
}
