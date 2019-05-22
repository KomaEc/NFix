package com.gzoltar.shaded.org.pitest.testapi.execute;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.testapi.TestListener;
import com.gzoltar.shaded.org.pitest.testapi.TestResult;

public enum ResultType {
   PASS(new ResultType.ResultToListenerSideEffect() {
      public SideEffect1<TestListener> apply(TestResult a) {
         return ResultType.success(a);
      }
   }),
   FAIL(new ResultType.ResultToListenerSideEffect() {
      public SideEffect1<TestListener> apply(TestResult a) {
         return ResultType.failure(a);
      }
   }),
   SKIPPED(new ResultType.ResultToListenerSideEffect() {
      public SideEffect1<TestListener> apply(TestResult a) {
         return ResultType.skipped(a);
      }
   }),
   STARTED(new ResultType.ResultToListenerSideEffect() {
      public SideEffect1<TestListener> apply(TestResult a) {
         return ResultType.started(a);
      }
   });

   private final F<TestResult, SideEffect1<TestListener>> function;

   private ResultType(ResultType.ResultToListenerSideEffect f) {
      this.function = f;
   }

   public SideEffect1<TestListener> getListenerFunction(TestResult result) {
      return (SideEffect1)this.function.apply(result);
   }

   public static SideEffect1<TestListener> success(final TestResult result) {
      return new SideEffect1<TestListener>() {
         public void apply(TestListener a) {
            a.onTestSuccess(result);
         }
      };
   }

   public static SideEffect1<TestListener> failure(final TestResult result) {
      return new SideEffect1<TestListener>() {
         public void apply(TestListener a) {
            a.onTestFailure(result);
         }
      };
   }

   public static SideEffect1<TestListener> skipped(final TestResult result) {
      return new SideEffect1<TestListener>() {
         public void apply(TestListener a) {
            a.onTestSkipped(result);
         }
      };
   }

   public static SideEffect1<TestListener> started(final TestResult result) {
      return new SideEffect1<TestListener>() {
         public void apply(TestListener a) {
            a.onTestStart(result.getDescription());
         }
      };
   }

   private interface ResultToListenerSideEffect extends F<TestResult, SideEffect1<TestListener>> {
   }
}
