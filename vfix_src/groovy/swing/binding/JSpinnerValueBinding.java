package groovy.swing.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.codehaus.groovy.binding.PropertyBinding;
import org.codehaus.groovy.binding.TargetBinding;

class JSpinnerValueBinding extends AbstractSyntheticBinding implements PropertyChangeListener, ChangeListener {
   JSpinner boundSlider;

   public JSpinnerValueBinding(PropertyBinding source, TargetBinding target) {
      super(source, target, JSpinner.class, "value");
   }

   public synchronized void syntheticBind() {
      this.boundSlider = (JSpinner)((PropertyBinding)this.sourceBinding).getBean();
      this.boundSlider.addPropertyChangeListener("model", this);
      this.boundSlider.getModel().addChangeListener(this);
   }

   public synchronized void syntheticUnbind() {
      this.boundSlider.removePropertyChangeListener("model", this);
      this.boundSlider.getModel().removeChangeListener(this);
      this.boundSlider = null;
   }

   public void propertyChange(PropertyChangeEvent event) {
      this.update();
      ((SpinnerModel)event.getOldValue()).removeChangeListener(this);
      ((SpinnerModel)event.getNewValue()).addChangeListener(this);
   }

   public void stateChanged(ChangeEvent e) {
      this.update();
   }
}
