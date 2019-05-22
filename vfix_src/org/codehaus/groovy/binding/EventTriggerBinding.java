package org.codehaus.groovy.binding;

import groovy.lang.Closure;
import org.codehaus.groovy.runtime.InvokerHelper;

public class EventTriggerBinding implements TriggerBinding {
   Object triggerBean;
   String eventName;

   public EventTriggerBinding(Object triggerBean, String eventName) {
      this.triggerBean = triggerBean;
      this.eventName = eventName;
   }

   public FullBinding createBinding(SourceBinding sourceBinding, TargetBinding targetBinding) {
      return new EventTriggerBinding.EventTriggerFullBinding(sourceBinding, targetBinding);
   }

   public Object getTriggerBean() {
      return this.triggerBean;
   }

   public void setTriggerBean(Object triggerBean) {
      this.triggerBean = triggerBean;
   }

   public String getEventName() {
      return this.eventName;
   }

   public void setEventName(String eventName) {
      this.eventName = eventName;
   }

   private class EventTriggerFullBinding extends AbstractFullBinding {
      Closure handler;

      public EventTriggerFullBinding(final SourceBinding sourceBinding, TargetBinding targetBinding) {
         this.setSourceBinding(sourceBinding);
         this.setTargetBinding(targetBinding);
         this.handler = new Closure(EventTriggerBinding.this.triggerBean) {
            public Object call(Object[] params) {
               if (sourceBinding instanceof ClosureSourceBinding) {
                  ((ClosureSourceBinding)sourceBinding).setClosureArguments(params);
               }

               EventTriggerFullBinding.this.update();
               return null;
            }
         };
      }

      public void bind() {
         InvokerHelper.setProperty(EventTriggerBinding.this.triggerBean, EventTriggerBinding.this.eventName, this.handler);
      }

      public void unbind() {
         throw new UnsupportedOperationException();
      }

      public void rebind() {
         throw new UnsupportedOperationException();
      }
   }
}
