package groovy.swing.binding;

import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import org.codehaus.groovy.binding.FullBinding;
import org.codehaus.groovy.binding.PropertyBinding;
import org.codehaus.groovy.binding.SourceBinding;
import org.codehaus.groovy.binding.TargetBinding;
import org.codehaus.groovy.binding.TriggerBinding;

public class JComponentProperties {
   public static Map<String, TriggerBinding> getSyntheticProperties() {
      Map<String, TriggerBinding> result = new HashMap();
      result.put(JComponent.class.getName() + "#size", new TriggerBinding() {
         public FullBinding createBinding(SourceBinding source, TargetBinding target) {
            return new AbstractJComponentBinding((PropertyBinding)source, target, "size") {
               public void componentResized(ComponentEvent event) {
                  this.update();
               }
            };
         }
      });
      result.put(JComponent.class.getName() + "#width", new TriggerBinding() {
         public FullBinding createBinding(SourceBinding source, TargetBinding target) {
            return new AbstractJComponentBinding((PropertyBinding)source, target, "width") {
               public void componentResized(ComponentEvent event) {
                  this.update();
               }
            };
         }
      });
      result.put(JComponent.class.getName() + "#height", new TriggerBinding() {
         public FullBinding createBinding(SourceBinding source, TargetBinding target) {
            return new AbstractJComponentBinding((PropertyBinding)source, target, "height") {
               public void componentResized(ComponentEvent event) {
                  this.update();
               }
            };
         }
      });
      result.put(JComponent.class.getName() + "#bounds", new TriggerBinding() {
         public FullBinding createBinding(SourceBinding source, TargetBinding target) {
            return new AbstractJComponentBinding((PropertyBinding)source, target, "bounds") {
               public void componentResized(ComponentEvent event) {
                  this.update();
               }

               public void componentMoved(ComponentEvent event) {
                  this.update();
               }
            };
         }
      });
      result.put(JComponent.class.getName() + "#x", new TriggerBinding() {
         public FullBinding createBinding(SourceBinding source, TargetBinding target) {
            return new AbstractJComponentBinding((PropertyBinding)source, target, "x") {
               public void componentMoved(ComponentEvent event) {
                  this.update();
               }
            };
         }
      });
      result.put(JComponent.class.getName() + "#y", new TriggerBinding() {
         public FullBinding createBinding(SourceBinding source, TargetBinding target) {
            return new AbstractJComponentBinding((PropertyBinding)source, target, "y") {
               public void componentMoved(ComponentEvent event) {
                  this.update();
               }
            };
         }
      });
      result.put(JComponent.class.getName() + "#visible", new TriggerBinding() {
         public FullBinding createBinding(SourceBinding source, TargetBinding target) {
            return new AbstractJComponentBinding((PropertyBinding)source, target, "visible") {
               public void componentHidden(ComponentEvent event) {
                  this.update();
               }

               public void componentShown(ComponentEvent event) {
                  this.update();
               }
            };
         }
      });
      return result;
   }
}
