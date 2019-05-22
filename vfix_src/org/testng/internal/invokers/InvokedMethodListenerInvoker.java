package org.testng.internal.invokers;

import java.util.Map;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IInvokedMethodListener2;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.collections.Maps;

public class InvokedMethodListenerInvoker {
   private InvokedMethodListenerMethod m_listenerMethod;
   private ITestContext m_testContext;
   private ITestResult m_testResult;
   private static final Map<InvokedMethodListenerSubtype, Map<InvokedMethodListenerMethod, InvokedMethodListenerInvoker.InvocationStrategy>> strategies = Maps.newHashMap();
   private static final Map<InvokedMethodListenerMethod, InvokedMethodListenerInvoker.InvocationStrategy> INVOKE_WITH_CONTEXT_STRATEGIES = Maps.newHashMap();
   private static final Map<InvokedMethodListenerMethod, InvokedMethodListenerInvoker.InvocationStrategy> INVOKE_WITHOUT_CONTEXT_STRATEGIES = Maps.newHashMap();

   public InvokedMethodListenerInvoker(InvokedMethodListenerMethod listenerMethod, ITestResult testResult, ITestContext testContext) {
      this.m_listenerMethod = listenerMethod;
      this.m_testContext = testContext;
      this.m_testResult = testResult;
   }

   public void invokeListener(IInvokedMethodListener listenerInstance, IInvokedMethod invokedMethod) {
      InvokedMethodListenerInvoker.InvocationStrategy strategy = this.obtainStrategyFor(listenerInstance, this.m_listenerMethod);
      strategy.callMethod(listenerInstance, invokedMethod, this.m_testResult, this.m_testContext);
   }

   private InvokedMethodListenerInvoker.InvocationStrategy obtainStrategyFor(IInvokedMethodListener listenerInstance, InvokedMethodListenerMethod listenerMethod) {
      InvokedMethodListenerSubtype invokedMethodListenerSubtype = InvokedMethodListenerSubtype.fromListener(listenerInstance);
      Map<InvokedMethodListenerMethod, InvokedMethodListenerInvoker.InvocationStrategy> strategiesForListenerType = (Map)strategies.get(invokedMethodListenerSubtype);
      InvokedMethodListenerInvoker.InvocationStrategy invocationStrategy = (InvokedMethodListenerInvoker.InvocationStrategy)strategiesForListenerType.get(listenerMethod);
      return invocationStrategy;
   }

   static {
      INVOKE_WITH_CONTEXT_STRATEGIES.put(InvokedMethodListenerMethod.BEFORE_INVOCATION, new InvokedMethodListenerInvoker.InvokeBeforeInvocationWithContextStrategy());
      INVOKE_WITH_CONTEXT_STRATEGIES.put(InvokedMethodListenerMethod.AFTER_INVOCATION, new InvokedMethodListenerInvoker.InvokeAfterInvocationWithContextStrategy());
      INVOKE_WITHOUT_CONTEXT_STRATEGIES.put(InvokedMethodListenerMethod.BEFORE_INVOCATION, new InvokedMethodListenerInvoker.InvokeBeforeInvocationWithoutContextStrategy());
      INVOKE_WITHOUT_CONTEXT_STRATEGIES.put(InvokedMethodListenerMethod.AFTER_INVOCATION, new InvokedMethodListenerInvoker.InvokeAfterInvocationWithoutContextStrategy());
      strategies.put(InvokedMethodListenerSubtype.EXTENDED_LISTENER, INVOKE_WITH_CONTEXT_STRATEGIES);
      strategies.put(InvokedMethodListenerSubtype.SIMPLE_LISTENER, INVOKE_WITHOUT_CONTEXT_STRATEGIES);
   }

   private static class InvokeAfterInvocationWithContextStrategy implements InvokedMethodListenerInvoker.InvocationStrategy<IInvokedMethodListener2> {
      private InvokeAfterInvocationWithContextStrategy() {
      }

      public void callMethod(IInvokedMethodListener2 listener, IInvokedMethod invokedMethod, ITestResult testResult, ITestContext testContext) {
         listener.afterInvocation(invokedMethod, testResult, testContext);
      }

      // $FF: synthetic method
      InvokeAfterInvocationWithContextStrategy(Object x0) {
         this();
      }
   }

   private static class InvokeAfterInvocationWithoutContextStrategy implements InvokedMethodListenerInvoker.InvocationStrategy<IInvokedMethodListener> {
      private InvokeAfterInvocationWithoutContextStrategy() {
      }

      public void callMethod(IInvokedMethodListener listener, IInvokedMethod invokedMethod, ITestResult testResult, ITestContext testContext) {
         listener.afterInvocation(invokedMethod, testResult);
      }

      // $FF: synthetic method
      InvokeAfterInvocationWithoutContextStrategy(Object x0) {
         this();
      }
   }

   private static class InvokeBeforeInvocationWithContextStrategy implements InvokedMethodListenerInvoker.InvocationStrategy<IInvokedMethodListener2> {
      private InvokeBeforeInvocationWithContextStrategy() {
      }

      public void callMethod(IInvokedMethodListener2 listener, IInvokedMethod invokedMethod, ITestResult testResult, ITestContext testContext) {
         listener.beforeInvocation(invokedMethod, testResult, testContext);
      }

      // $FF: synthetic method
      InvokeBeforeInvocationWithContextStrategy(Object x0) {
         this();
      }
   }

   private static class InvokeBeforeInvocationWithoutContextStrategy implements InvokedMethodListenerInvoker.InvocationStrategy<IInvokedMethodListener> {
      private InvokeBeforeInvocationWithoutContextStrategy() {
      }

      public void callMethod(IInvokedMethodListener listener, IInvokedMethod invokedMethod, ITestResult testResult, ITestContext testContext) {
         listener.beforeInvocation(invokedMethod, testResult);
      }

      // $FF: synthetic method
      InvokeBeforeInvocationWithoutContextStrategy(Object x0) {
         this();
      }
   }

   private interface InvocationStrategy<LISTENER_TYPE extends IInvokedMethodListener> {
      void callMethod(LISTENER_TYPE var1, IInvokedMethod var2, ITestResult var3, ITestContext var4);
   }
}
