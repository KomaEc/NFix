package groovy.swing.impl;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableLayoutRow {
   private final TableLayout parent;
   private final List<TableLayoutCell> cells = new ArrayList();
   private int rowIndex;

   public TableLayoutRow(TableLayout tableLayoutTag) {
      this.parent = tableLayoutTag;
   }

   public void addCell(TableLayoutCell tag) {
      int gridx = 0;

      TableLayoutCell cell;
      for(Iterator iter = this.cells.iterator(); iter.hasNext(); gridx += cell.getColspan()) {
         cell = (TableLayoutCell)iter.next();
      }

      tag.getConstraints().gridx = gridx;
      this.cells.add(tag);
   }

   public void addComponentsForRow() {
      this.rowIndex = this.parent.nextRowIndex();
      Iterator iter = this.cells.iterator();

      while(iter.hasNext()) {
         TableLayoutCell cell = (TableLayoutCell)iter.next();
         GridBagConstraints c = cell.getConstraints();
         c.gridy = this.rowIndex;
         this.parent.addCell(cell);
      }

   }

   public int getRowIndex() {
      return this.rowIndex;
   }
}
