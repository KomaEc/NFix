package org.codehaus.groovy.binding;

public class SwingTimerTriggerBinding implements TriggerBinding {
   public FullBinding createBinding(SourceBinding source, TargetBinding target) {
      return new SwingTimerFullBinding((ClosureSourceBinding)source, target);
   }
}
