package groovy.swing.binding;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JComboBox;
import org.codehaus.groovy.binding.FullBinding;
import org.codehaus.groovy.binding.PropertyBinding;
import org.codehaus.groovy.binding.SourceBinding;
import org.codehaus.groovy.binding.TargetBinding;
import org.codehaus.groovy.binding.TriggerBinding;

public class JComboBoxProperties {
   public static Map<String, TriggerBinding> getSyntheticProperties() {
      Map<String, TriggerBinding> result = new HashMap();
      result.put(JComboBox.class.getName() + "#selectedItem", new TriggerBinding() {
         public FullBinding createBinding(SourceBinding source, TargetBinding target) {
            return new JComboBoxSelectedElementBinding((PropertyBinding)source, target, "selectedItem");
         }
      });
      result.put(JComboBox.class.getName() + "#selectedElement", new TriggerBinding() {
         public FullBinding createBinding(SourceBinding source, TargetBinding target) {
            return new JComboBoxSelectedElementBinding((PropertyBinding)source, target, "selectedElement");
         }
      });
      result.put(JComboBox.class.getName() + "#selectedIndex", new TriggerBinding() {
         public FullBinding createBinding(SourceBinding source, TargetBinding target) {
            return new JComboBoxSelectedIndexBinding((PropertyBinding)source, target);
         }
      });
      result.put(JComboBox.class.getName() + "#elements", new TriggerBinding() {
         public FullBinding createBinding(SourceBinding source, TargetBinding target) {
            return new JComboBoxElementsBinding((PropertyBinding)source, target);
         }
      });
      return result;
   }
}
