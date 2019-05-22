package groovy.swing.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.codehaus.groovy.binding.PropertyBinding;
import org.codehaus.groovy.binding.TargetBinding;

class JTableSelectedElementBinding extends AbstractSyntheticBinding implements PropertyChangeListener, ListSelectionListener {
   JTable boundTable;

   protected JTableSelectedElementBinding(PropertyBinding source, TargetBinding target, String propertyName) {
      super(source, target, JTable.class, propertyName);
   }

   public synchronized void syntheticBind() {
      this.boundTable = (JTable)((PropertyBinding)this.sourceBinding).getBean();
      this.boundTable.addPropertyChangeListener("selectionModel", this);
      this.boundTable.getSelectionModel().addListSelectionListener(this);
   }

   public synchronized void syntheticUnbind() {
      this.boundTable.removePropertyChangeListener("selectionModel", this);
      this.boundTable.getSelectionModel().removeListSelectionListener(this);
      this.boundTable = null;
   }

   public void propertyChange(PropertyChangeEvent event) {
      this.update();
      ((ListSelectionModel)event.getOldValue()).removeListSelectionListener(this);
      ((ListSelectionModel)event.getNewValue()).addListSelectionListener(this);
   }

   public void valueChanged(ListSelectionEvent e) {
      this.update();
   }
}
