package groovy.swing.binding;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import org.codehaus.groovy.binding.PropertyBinding;
import org.codehaus.groovy.binding.TargetBinding;

class AbstractButtonSelectedBinding extends AbstractSyntheticBinding implements PropertyChangeListener, ItemListener {
   AbstractButton boundButton;

   public AbstractButtonSelectedBinding(PropertyBinding source, TargetBinding target) {
      super(source, target, AbstractButton.class, "selected");
   }

   public synchronized void syntheticBind() {
      this.boundButton = (AbstractButton)((PropertyBinding)this.sourceBinding).getBean();
      this.boundButton.addPropertyChangeListener("model", this);
      this.boundButton.getModel().addItemListener(this);
   }

   public synchronized void syntheticUnbind() {
      this.boundButton.removePropertyChangeListener("model", this);
      this.boundButton.getModel().removeItemListener(this);
      this.boundButton = null;
   }

   public void propertyChange(PropertyChangeEvent event) {
      this.update();
      ((ButtonModel)event.getOldValue()).removeItemListener(this);
      ((ButtonModel)event.getNewValue()).addItemListener(this);
   }

   public void itemStateChanged(ItemEvent e) {
      this.update();
   }
}
