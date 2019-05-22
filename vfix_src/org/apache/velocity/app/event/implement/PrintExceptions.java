package org.apache.velocity.app.event.implement;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.velocity.app.event.MethodExceptionEventHandler;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.RuntimeServicesAware;

public class PrintExceptions implements MethodExceptionEventHandler, RuntimeServicesAware {
   private static String SHOW_MESSAGE = "eventhandler.methodexception.message";
   private static String SHOW_STACK_TRACE = "eventhandler.methodexception.stacktrace";
   private RuntimeServices rs = null;

   public Object methodException(Class claz, String method, Exception e) throws Exception {
      boolean showMessage = this.rs.getBoolean(SHOW_MESSAGE, false);
      boolean showStackTrace = this.rs.getBoolean(SHOW_STACK_TRACE, false);
      StringBuffer st;
      if (showMessage && showStackTrace) {
         st = new StringBuffer(200);
         st.append(e.getClass().getName()).append("\n");
         st.append(e.getMessage()).append("\n");
         st.append(getStackTrace(e));
      } else if (showMessage) {
         st = new StringBuffer(50);
         st.append(e.getClass().getName()).append("\n");
         st.append(e.getMessage()).append("\n");
      } else if (showStackTrace) {
         st = new StringBuffer(200);
         st.append(e.getClass().getName()).append("\n");
         st.append(getStackTrace(e));
      } else {
         st = new StringBuffer(15);
         st.append(e.getClass().getName()).append("\n");
      }

      return st.toString();
   }

   private static String getStackTrace(Throwable throwable) {
      PrintWriter printWriter = null;

      String var3;
      try {
         StringWriter stackTraceWriter = new StringWriter();
         printWriter = new PrintWriter(stackTraceWriter);
         throwable.printStackTrace(printWriter);
         printWriter.flush();
         var3 = stackTraceWriter.toString();
      } finally {
         if (printWriter != null) {
            printWriter.close();
         }

      }

      return var3;
   }

   public void setRuntimeServices(RuntimeServices rs) {
      this.rs = rs;
   }
}
