package org.apache.velocity.app.event.implement;

import java.util.ArrayList;
import java.util.List;
import org.apache.velocity.app.event.InvalidReferenceEventHandler;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.RuntimeServicesAware;
import org.apache.velocity.util.introspection.Info;

public class ReportInvalidReferences implements InvalidReferenceEventHandler, RuntimeServicesAware {
   public static final String EVENTHANDLER_INVALIDREFERENCE_EXCEPTION = "eventhandler.invalidreference.exception";
   List invalidReferences = new ArrayList();
   private boolean stopOnFirstInvalidReference = false;

   public Object invalidGetMethod(Context context, String reference, Object object, String property, Info info) {
      this.reportInvalidReference(reference, info);
      return null;
   }

   public Object invalidMethod(Context context, String reference, Object object, String method, Info info) {
      if (reference == null) {
         this.reportInvalidReference(object.getClass().getName() + "." + method, info);
      } else {
         this.reportInvalidReference(reference, info);
      }

      return null;
   }

   public boolean invalidSetMethod(Context context, String leftreference, String rightreference, Info info) {
      this.reportInvalidReference(leftreference, info);
      return false;
   }

   private void reportInvalidReference(String reference, Info info) {
      InvalidReferenceInfo invalidReferenceInfo = new InvalidReferenceInfo(reference, info);
      this.invalidReferences.add(invalidReferenceInfo);
      if (this.stopOnFirstInvalidReference) {
         throw new ParseErrorException("Error in page - invalid reference.  ", info, invalidReferenceInfo.getInvalidReference());
      }
   }

   public List getInvalidReferences() {
      return this.invalidReferences;
   }

   public void setRuntimeServices(RuntimeServices rs) {
      this.stopOnFirstInvalidReference = rs.getConfiguration().getBoolean("eventhandler.invalidreference.exception", false);
   }
}
