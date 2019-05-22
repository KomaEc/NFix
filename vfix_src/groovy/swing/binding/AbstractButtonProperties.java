package groovy.swing.binding;

import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractButton;
import org.codehaus.groovy.binding.FullBinding;
import org.codehaus.groovy.binding.PropertyBinding;
import org.codehaus.groovy.binding.SourceBinding;
import org.codehaus.groovy.binding.TargetBinding;
import org.codehaus.groovy.binding.TriggerBinding;

public class AbstractButtonProperties {
   public static Map<String, TriggerBinding> getSyntheticProperties() {
      Map<String, TriggerBinding> result = new HashMap();
      result.put(AbstractButton.class.getName() + "#selected", new TriggerBinding() {
         public FullBinding createBinding(SourceBinding source, TargetBinding target) {
            return new AbstractButtonSelectedBinding((PropertyBinding)source, target);
         }
      });
      return result;
   }
}
