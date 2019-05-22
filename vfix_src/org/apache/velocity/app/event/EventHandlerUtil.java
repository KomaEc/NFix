package org.apache.velocity.app.event;

import java.util.Iterator;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.ExceptionUtils;
import org.apache.velocity.util.introspection.Info;

public class EventHandlerUtil {
   public static Object referenceInsert(RuntimeServices rsvc, InternalContextAdapter context, String reference, Object value) {
      EventCartridge ev1 = rsvc.getApplicationEventCartridge();
      Iterator applicationEventHandlerIterator = ev1 == null ? null : ev1.getReferenceInsertionEventHandlers();
      EventCartridge ev2 = context.getEventCartridge();
      initializeEventCartridge(rsvc, ev2);
      Iterator contextEventHandlerIterator = ev2 == null ? null : ev2.getReferenceInsertionEventHandlers();

      try {
         EventHandlerMethodExecutor methodExecutor = new ReferenceInsertionEventHandler.referenceInsertExecutor(context, reference, value);
         callEventHandlers(applicationEventHandlerIterator, contextEventHandlerIterator, methodExecutor);
         return methodExecutor.getReturnValue();
      } catch (RuntimeException var9) {
         throw var9;
      } catch (Exception var10) {
         throw ExceptionUtils.createRuntimeException("Exception in event handler.", var10);
      }
   }

   public static boolean shouldLogOnNullSet(RuntimeServices rsvc, InternalContextAdapter context, String lhs, String rhs) {
      EventCartridge ev1 = rsvc.getApplicationEventCartridge();
      Iterator applicationEventHandlerIterator = ev1 == null ? null : ev1.getNullSetEventHandlers();
      EventCartridge ev2 = context.getEventCartridge();
      initializeEventCartridge(rsvc, ev2);
      Iterator contextEventHandlerIterator = ev2 == null ? null : ev2.getNullSetEventHandlers();

      try {
         EventHandlerMethodExecutor methodExecutor = new NullSetEventHandler.ShouldLogOnNullSetExecutor(context, lhs, rhs);
         callEventHandlers(applicationEventHandlerIterator, contextEventHandlerIterator, methodExecutor);
         return (Boolean)methodExecutor.getReturnValue();
      } catch (RuntimeException var9) {
         throw var9;
      } catch (Exception var10) {
         throw ExceptionUtils.createRuntimeException("Exception in event handler.", var10);
      }
   }

   public static Object methodException(RuntimeServices rsvc, InternalContextAdapter context, Class claz, String method, Exception e) throws Exception {
      EventCartridge ev1 = rsvc.getApplicationEventCartridge();
      Iterator applicationEventHandlerIterator = ev1 == null ? null : ev1.getMethodExceptionEventHandlers();
      EventCartridge ev2 = context.getEventCartridge();
      initializeEventCartridge(rsvc, ev2);
      Iterator contextEventHandlerIterator = ev2 == null ? null : ev2.getMethodExceptionEventHandlers();
      EventHandlerMethodExecutor methodExecutor = new MethodExceptionEventHandler.MethodExceptionExecutor(context, claz, method, e);
      if (applicationEventHandlerIterator != null && applicationEventHandlerIterator.hasNext() || contextEventHandlerIterator != null && contextEventHandlerIterator.hasNext()) {
         callEventHandlers(applicationEventHandlerIterator, contextEventHandlerIterator, methodExecutor);
         return methodExecutor.getReturnValue();
      } else {
         throw e;
      }
   }

   public static String includeEvent(RuntimeServices rsvc, InternalContextAdapter context, String includeResourcePath, String currentResourcePath, String directiveName) {
      EventCartridge ev1 = rsvc.getApplicationEventCartridge();
      Iterator applicationEventHandlerIterator = ev1 == null ? null : ev1.getIncludeEventHandlers();
      EventCartridge ev2 = context.getEventCartridge();
      initializeEventCartridge(rsvc, ev2);
      Iterator contextEventHandlerIterator = ev2 == null ? null : ev2.getIncludeEventHandlers();

      try {
         EventHandlerMethodExecutor methodExecutor = new IncludeEventHandler.IncludeEventExecutor(context, includeResourcePath, currentResourcePath, directiveName);
         callEventHandlers(applicationEventHandlerIterator, contextEventHandlerIterator, methodExecutor);
         return (String)methodExecutor.getReturnValue();
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Exception var11) {
         throw ExceptionUtils.createRuntimeException("Exception in event handler.", var11);
      }
   }

   public static Object invalidGetMethod(RuntimeServices rsvc, InternalContextAdapter context, String reference, Object object, String property, Info info) {
      return invalidReferenceHandlerCall(new InvalidReferenceEventHandler.InvalidGetMethodExecutor(context, reference, object, property, info), rsvc, context);
   }

   public static void invalidSetMethod(RuntimeServices rsvc, InternalContextAdapter context, String leftreference, String rightreference, Info info) {
      invalidReferenceHandlerCall(new InvalidReferenceEventHandler.InvalidSetMethodExecutor(context, leftreference, rightreference, info), rsvc, context);
   }

   public static Object invalidMethod(RuntimeServices rsvc, InternalContextAdapter context, String reference, Object object, String method, Info info) {
      return invalidReferenceHandlerCall(new InvalidReferenceEventHandler.InvalidMethodExecutor(context, reference, object, method, info), rsvc, context);
   }

   public static Object invalidReferenceHandlerCall(EventHandlerMethodExecutor methodExecutor, RuntimeServices rsvc, InternalContextAdapter context) {
      EventCartridge ev1 = rsvc.getApplicationEventCartridge();
      Iterator applicationEventHandlerIterator = ev1 == null ? null : ev1.getInvalidReferenceEventHandlers();
      EventCartridge ev2 = context.getEventCartridge();
      initializeEventCartridge(rsvc, ev2);
      Iterator contextEventHandlerIterator = ev2 == null ? null : ev2.getInvalidReferenceEventHandlers();

      try {
         callEventHandlers(applicationEventHandlerIterator, contextEventHandlerIterator, methodExecutor);
         return methodExecutor.getReturnValue();
      } catch (RuntimeException var8) {
         throw var8;
      } catch (Exception var9) {
         throw ExceptionUtils.createRuntimeException("Exception in event handler.", var9);
      }
   }

   private static void initializeEventCartridge(RuntimeServices rsvc, EventCartridge eventCartridge) {
      if (eventCartridge != null) {
         try {
            eventCartridge.initialize(rsvc);
         } catch (Exception var3) {
            throw ExceptionUtils.createRuntimeException("Couldn't initialize event cartridge : ", var3);
         }
      }

   }

   private static void callEventHandlers(Iterator applicationEventHandlerIterator, Iterator contextEventHandlerIterator, EventHandlerMethodExecutor eventExecutor) throws Exception {
      iterateOverEventHandlers(applicationEventHandlerIterator, eventExecutor);
      iterateOverEventHandlers(contextEventHandlerIterator, eventExecutor);
   }

   private static void iterateOverEventHandlers(Iterator handlerIterator, EventHandlerMethodExecutor eventExecutor) throws Exception {
      if (handlerIterator != null) {
         Iterator i = handlerIterator;

         while(i.hasNext()) {
            EventHandler eventHandler = (EventHandler)i.next();
            if (!eventExecutor.isDone()) {
               eventExecutor.execute(eventHandler);
            }
         }
      }

   }
}
