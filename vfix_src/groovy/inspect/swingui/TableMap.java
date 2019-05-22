package groovy.inspect.swingui;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class TableMap extends AbstractTableModel implements TableModelListener {
   protected TableModel model;

   public TableModel getModel() {
      return this.model;
   }

   public void setModel(TableModel model) {
      this.model = model;
      model.addTableModelListener(this);
   }

   public Object getValueAt(int aRow, int aColumn) {
      return this.model.getValueAt(aRow, aColumn);
   }

   public void setValueAt(Object aValue, int aRow, int aColumn) {
      this.model.setValueAt(aValue, aRow, aColumn);
   }

   public int getRowCount() {
      return this.model == null ? 0 : this.model.getRowCount();
   }

   public int getColumnCount() {
      return this.model == null ? 0 : this.model.getColumnCount();
   }

   public String getColumnName(int aColumn) {
      return this.model.getColumnName(aColumn);
   }

   public Class getColumnClass(int aColumn) {
      return this.model.getColumnClass(aColumn);
   }

   public boolean isCellEditable(int row, int column) {
      return this.model.isCellEditable(row, column);
   }

   public void tableChanged(TableModelEvent e) {
      this.fireTableChanged(e);
   }
}
