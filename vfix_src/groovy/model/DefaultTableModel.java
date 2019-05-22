package groovy.model;

import groovy.lang.Closure;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.codehaus.groovy.runtime.InvokerHelper;

public class DefaultTableModel extends AbstractTableModel {
   private ValueModel rowModel;
   private ValueModel rowsModel;
   private DefaultTableModel.MyTableColumnModel columnModel;

   public DefaultTableModel(ValueModel rowsModel) {
      this(rowsModel, new ValueHolder());
   }

   public DefaultTableModel(ValueModel rowsModel, ValueModel rowModel) {
      this.columnModel = new DefaultTableModel.MyTableColumnModel();
      this.rowModel = rowModel;
      this.rowsModel = rowsModel;
   }

   public List getColumnList() {
      return this.columnModel.getColumnList();
   }

   public TableColumnModel getColumnModel() {
      return this.columnModel;
   }

   public DefaultTableColumn addPropertyColumn(Object headerValue, String property, Class type) {
      return this.addColumn(headerValue, property, new PropertyModel(this.rowModel, property, type));
   }

   public DefaultTableColumn addPropertyColumn(Object headerValue, String property, Class type, boolean editable) {
      return this.addColumn(headerValue, property, new PropertyModel(this.rowModel, property, type, editable));
   }

   public DefaultTableColumn addClosureColumn(Object headerValue, Closure readClosure, Closure writeClosure, Class type) {
      return this.addColumn(headerValue, new ClosureModel(this.rowModel, readClosure, writeClosure, type));
   }

   public DefaultTableColumn addColumn(Object headerValue, ValueModel columnValueModel) {
      return this.addColumn(headerValue, headerValue, columnValueModel);
   }

   public DefaultTableColumn addColumn(Object headerValue, Object identifier, ValueModel columnValueModel) {
      DefaultTableColumn answer = new DefaultTableColumn(headerValue, identifier, columnValueModel);
      this.addColumn(answer);
      return answer;
   }

   public void addColumn(DefaultTableColumn column) {
      column.setModelIndex(this.columnModel.getColumnCount());
      this.columnModel.addColumn(column);
   }

   public void removeColumn(DefaultTableColumn column) {
      this.columnModel.removeColumn(column);
   }

   public int getRowCount() {
      return this.getRows().size();
   }

   public int getColumnCount() {
      return this.columnModel.getColumnCount();
   }

   public String getColumnName(int columnIndex) {
      String answer = null;
      if (columnIndex >= 0 && columnIndex < this.columnModel.getColumnCount()) {
         Object value = this.columnModel.getColumn(columnIndex).getHeaderValue();
         return (String)(value != null ? value.toString() : answer);
      } else {
         return (String)answer;
      }
   }

   public Class getColumnClass(int columnIndex) {
      return this.getColumnModel(columnIndex).getType();
   }

   public boolean isCellEditable(int rowIndex, int columnIndex) {
      return this.getColumnModel(columnIndex).isEditable();
   }

   public Object getValueAt(int rowIndex, int columnIndex) {
      List rows = this.getRows();
      Object answer = null;
      if (rowIndex >= 0 && rowIndex < rows.size()) {
         if (columnIndex >= 0 && columnIndex < this.columnModel.getColumnCount()) {
            Object row = this.getRows().get(rowIndex);
            this.rowModel.setValue(row);
            DefaultTableColumn column = (DefaultTableColumn)this.columnModel.getColumn(columnIndex);
            return row != null && column != null ? column.getValue(row, rowIndex, columnIndex) : answer;
         } else {
            return answer;
         }
      } else {
         return answer;
      }
   }

   public void setValueAt(Object value, int rowIndex, int columnIndex) {
      List rows = this.getRows();
      if (rowIndex >= 0 && rowIndex < rows.size()) {
         if (columnIndex >= 0 && columnIndex < this.columnModel.getColumnCount()) {
            Object row = this.getRows().get(rowIndex);
            this.rowModel.setValue(row);
            DefaultTableColumn column = (DefaultTableColumn)this.columnModel.getColumn(columnIndex);
            if (row != null && column != null) {
               column.setValue(row, value, rowIndex, columnIndex);
            }
         }
      }
   }

   protected ValueModel getColumnModel(int columnIndex) {
      DefaultTableColumn column = (DefaultTableColumn)this.columnModel.getColumn(columnIndex);
      return column.getValueModel();
   }

   protected List getRows() {
      Object value = this.rowsModel.getValue();
      return value == null ? Collections.EMPTY_LIST : InvokerHelper.asList(value);
   }

   public ValueModel getRowModel() {
      return this.rowModel;
   }

   public ValueModel getRowsModel() {
      return this.rowsModel;
   }

   protected static class MyTableColumnModel extends DefaultTableColumnModel {
      public List getColumnList() {
         return this.tableColumns;
      }

      public void removeColumn(TableColumn column) {
         super.removeColumn(column);
         this.renumberTableColumns();
      }

      public void moveColumn(int columnIndex, int newIndex) {
         super.moveColumn(columnIndex, newIndex);
         this.renumberTableColumns();
      }

      public void renumberTableColumns() {
         for(int i = this.tableColumns.size() - 1; i >= 0; --i) {
            ((DefaultTableColumn)this.tableColumns.get(i)).setModelIndex(i);
         }

      }
   }
}
