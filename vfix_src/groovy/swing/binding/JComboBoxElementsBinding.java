package groovy.swing.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.codehaus.groovy.binding.PropertyBinding;
import org.codehaus.groovy.binding.TargetBinding;

class JComboBoxElementsBinding extends AbstractSyntheticBinding implements ListDataListener, PropertyChangeListener {
   JComboBox boundComboBox;

   public JComboBoxElementsBinding(PropertyBinding propertyBinding, TargetBinding target) {
      super(propertyBinding, target, JComboBox.class, "elements");
   }

   protected void syntheticBind() {
      this.boundComboBox = (JComboBox)((PropertyBinding)this.sourceBinding).getBean();
      this.boundComboBox.addPropertyChangeListener("model", this);
      this.boundComboBox.getModel().addListDataListener(this);
   }

   protected void syntheticUnbind() {
      this.boundComboBox.removePropertyChangeListener("model", this);
      this.boundComboBox.getModel().removeListDataListener(this);
   }

   public void propertyChange(PropertyChangeEvent event) {
      this.update();
      ((ComboBoxModel)event.getOldValue()).removeListDataListener(this);
      ((ComboBoxModel)event.getNewValue()).addListDataListener(this);
   }

   public void intervalAdded(ListDataEvent e) {
      this.update();
   }

   public void intervalRemoved(ListDataEvent e) {
      this.update();
   }

   public void contentsChanged(ListDataEvent e) {
      this.update();
   }
}
