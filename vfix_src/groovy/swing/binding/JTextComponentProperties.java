package groovy.swing.binding;

import java.util.HashMap;
import java.util.Map;
import javax.swing.text.JTextComponent;
import org.codehaus.groovy.binding.FullBinding;
import org.codehaus.groovy.binding.PropertyBinding;
import org.codehaus.groovy.binding.SourceBinding;
import org.codehaus.groovy.binding.TargetBinding;
import org.codehaus.groovy.binding.TriggerBinding;

public class JTextComponentProperties {
   public static Map<String, TriggerBinding> getSyntheticProperties() {
      Map<String, TriggerBinding> result = new HashMap();
      result.put(JTextComponent.class.getName() + "#text", new TriggerBinding() {
         public FullBinding createBinding(SourceBinding source, TargetBinding target) {
            return new JTextComponentTextBinding((PropertyBinding)source, target);
         }
      });
      return result;
   }
}
