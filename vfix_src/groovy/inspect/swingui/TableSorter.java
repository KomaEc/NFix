package groovy.inspect.swingui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class TableSorter extends TableMap {
   int[] indexes;
   Vector sortingColumns = new Vector();
   boolean ascending = true;
   int lastSortedColumn = -1;

   public TableSorter() {
      this.indexes = new int[0];
   }

   public TableSorter(TableModel model) {
      this.setModel(model);
   }

   public void setModel(TableModel model) {
      super.setModel(model);
      this.reallocateIndexes();
   }

   public int compareRowsByColumn(int row1, int row2, int column) {
      Class type = this.model.getColumnClass(column);
      TableModel data = this.model;
      Object o1 = data.getValueAt(row1, column);
      Object o2 = data.getValueAt(row2, column);
      if (o1 == null && o2 == null) {
         return 0;
      } else if (o1 == null) {
         return -1;
      } else if (o2 == null) {
         return 1;
      } else if (type.getSuperclass() == Number.class) {
         return this.compareNumbers(data, row1, column, row2);
      } else if (type == Date.class) {
         return this.compareDates(data, row1, column, row2);
      } else if (type == String.class) {
         return this.compareStrings(data, row1, column, row2);
      } else {
         return type == Boolean.class ? this.compareBooleans(data, row1, column, row2) : this.compareObjects(data, row1, column, row2);
      }
   }

   private int compareObjects(TableModel data, int row1, int column, int row2) {
      Object v1 = data.getValueAt(row1, column);
      String s1 = v1.toString();
      Object v2 = data.getValueAt(row2, column);
      String s2 = v2.toString();
      int result = s1.compareTo(s2);
      if (result < 0) {
         return -1;
      } else {
         return result > 0 ? 1 : 0;
      }
   }

   private int compareBooleans(TableModel data, int row1, int column, int row2) {
      Boolean bool1 = (Boolean)data.getValueAt(row1, column);
      boolean b1 = bool1;
      Boolean bool2 = (Boolean)data.getValueAt(row2, column);
      boolean b2 = bool2;
      if (b1 == b2) {
         return 0;
      } else {
         return b1 ? 1 : -1;
      }
   }

   private int compareStrings(TableModel data, int row1, int column, int row2) {
      String s1 = (String)data.getValueAt(row1, column);
      String s2 = (String)data.getValueAt(row2, column);
      int result = s1.compareTo(s2);
      if (result < 0) {
         return -1;
      } else {
         return result > 0 ? 1 : 0;
      }
   }

   private int compareDates(TableModel data, int row1, int column, int row2) {
      Date d1 = (Date)data.getValueAt(row1, column);
      long n1 = d1.getTime();
      Date d2 = (Date)data.getValueAt(row2, column);
      long n2 = d2.getTime();
      if (n1 < n2) {
         return -1;
      } else {
         return n1 > n2 ? 1 : 0;
      }
   }

   private int compareNumbers(TableModel data, int row1, int column, int row2) {
      Number n1 = (Number)data.getValueAt(row1, column);
      double d1 = n1.doubleValue();
      Number n2 = (Number)data.getValueAt(row2, column);
      double d2 = n2.doubleValue();
      if (d1 < d2) {
         return -1;
      } else {
         return d1 > d2 ? 1 : 0;
      }
   }

   public int compare(int row1, int row2) {
      for(int level = 0; level < this.sortingColumns.size(); ++level) {
         Integer column = (Integer)this.sortingColumns.elementAt(level);
         int result = this.compareRowsByColumn(row1, row2, column);
         if (result != 0) {
            return this.ascending ? result : -result;
         }
      }

      return 0;
   }

   public void reallocateIndexes() {
      int rowCount = this.model.getRowCount();
      this.indexes = new int[rowCount];

      for(int row = 0; row < rowCount; this.indexes[row] = row++) {
      }

   }

   public void tableChanged(TableModelEvent e) {
      System.out.println("Sorter: tableChanged");
      this.reallocateIndexes();
      super.tableChanged(e);
   }

   public void checkModel() {
      if (this.indexes.length != this.model.getRowCount()) {
         System.err.println("Sorter not informed of a change in model.");
      }

   }

   public void sort(Object sender) {
      this.checkModel();
      this.shuttlesort((int[])((int[])this.indexes.clone()), this.indexes, 0, this.indexes.length);
   }

   public void n2sort() {
      for(int i = 0; i < this.getRowCount(); ++i) {
         for(int j = i + 1; j < this.getRowCount(); ++j) {
            if (this.compare(this.indexes[i], this.indexes[j]) == -1) {
               this.swap(i, j);
            }
         }
      }

   }

   public void shuttlesort(int[] from, int[] to, int low, int high) {
      if (high - low >= 2) {
         int middle = (low + high) / 2;
         this.shuttlesort(to, from, low, middle);
         this.shuttlesort(to, from, middle, high);
         int p = low;
         int q = middle;
         if (high - low >= 4 && this.compare(from[middle - 1], from[middle]) <= 0) {
            System.arraycopy(from, low, to, low, high - low);
         } else {
            for(int i = low; i < high; ++i) {
               if (q < high && (p >= middle || this.compare(from[p], from[q]) > 0)) {
                  to[i] = from[q++];
               } else {
                  to[i] = from[p++];
               }
            }

         }
      }
   }

   public void swap(int i, int j) {
      int tmp = this.indexes[i];
      this.indexes[i] = this.indexes[j];
      this.indexes[j] = tmp;
   }

   public Object getValueAt(int aRow, int aColumn) {
      this.checkModel();
      return this.model.getValueAt(this.indexes[aRow], aColumn);
   }

   public void setValueAt(Object aValue, int aRow, int aColumn) {
      this.checkModel();
      this.model.setValueAt(aValue, this.indexes[aRow], aColumn);
   }

   public void sortByColumn(int column) {
      this.sortByColumn(column, true);
   }

   public void sortByColumn(int column, boolean ascending) {
      this.ascending = ascending;
      this.sortingColumns.removeAllElements();
      this.sortingColumns.addElement(column);
      this.sort(this);
      super.tableChanged(new TableModelEvent(this));
   }

   public void addMouseListenerToHeaderInTable(final JTable table) {
      table.setColumnSelectionAllowed(false);
      MouseAdapter listMouseListener = new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            TableColumnModel columnModel = table.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            int column = table.convertColumnIndexToModel(viewColumn);
            if (e.getClickCount() == 1 && column != -1) {
               if (TableSorter.this.lastSortedColumn == column) {
                  TableSorter.this.ascending = !TableSorter.this.ascending;
               }

               TableSorter.this.sortByColumn(column, TableSorter.this.ascending);
               TableSorter.this.lastSortedColumn = column;
            }

         }
      };
      JTableHeader th = table.getTableHeader();
      th.addMouseListener(listMouseListener);
   }
}
