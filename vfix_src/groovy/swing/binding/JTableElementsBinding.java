package groovy.swing.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.codehaus.groovy.binding.PropertyBinding;
import org.codehaus.groovy.binding.TargetBinding;

class JTableElementsBinding extends AbstractSyntheticBinding implements TableModelListener, PropertyChangeListener {
   JTable boundTable;

   public JTableElementsBinding(PropertyBinding propertyBinding, TargetBinding target) {
      super(propertyBinding, target, JTable.class, "elements");
   }

   protected void syntheticBind() {
      this.boundTable = (JTable)((PropertyBinding)this.sourceBinding).getBean();
      this.boundTable.addPropertyChangeListener("model", this);
      this.boundTable.getModel().addTableModelListener(this);
   }

   protected void syntheticUnbind() {
      this.boundTable.removePropertyChangeListener("model", this);
      this.boundTable.getModel().removeTableModelListener(this);
   }

   public void tableChanged(TableModelEvent e) {
      this.update();
   }

   public void propertyChange(PropertyChangeEvent event) {
      this.update();
      ((TableModel)event.getOldValue()).removeTableModelListener(this);
      ((TableModel)event.getNewValue()).addTableModelListener(this);
   }
}
