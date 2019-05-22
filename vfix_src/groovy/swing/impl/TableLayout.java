package groovy.swing.impl;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;

public class TableLayout extends JPanel {
   private int rowCount;
   private int cellpadding;

   public TableLayout() {
      this.setLayout(new GridBagLayout());
   }

   /** @deprecated */
   public Component getComponent() {
      return this;
   }

   public int getCellpadding() {
      return this.cellpadding;
   }

   public void setCellpadding(int cellpadding) {
      this.cellpadding = cellpadding;
   }

   public void addCell(TableLayoutCell cell) {
      GridBagConstraints constraints = cell.getConstraints();
      constraints.insets = new Insets(this.cellpadding, this.cellpadding, this.cellpadding, this.cellpadding);
      this.add(cell.getComponent(), constraints);
   }

   public int nextRowIndex() {
      return this.rowCount++;
   }
}
