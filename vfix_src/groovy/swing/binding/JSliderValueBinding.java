package groovy.swing.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoundedRangeModel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.codehaus.groovy.binding.PropertyBinding;
import org.codehaus.groovy.binding.TargetBinding;

class JSliderValueBinding extends AbstractSyntheticBinding implements PropertyChangeListener, ChangeListener {
   JSlider boundSlider;

   public JSliderValueBinding(PropertyBinding source, TargetBinding target) {
      super(source, target, JSlider.class, "value");
   }

   public synchronized void syntheticBind() {
      this.boundSlider = (JSlider)((PropertyBinding)this.sourceBinding).getBean();
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
      ((BoundedRangeModel)event.getOldValue()).removeChangeListener(this);
      ((BoundedRangeModel)event.getNewValue()).addChangeListener(this);
   }

   public void stateChanged(ChangeEvent e) {
      this.update();
   }
}
