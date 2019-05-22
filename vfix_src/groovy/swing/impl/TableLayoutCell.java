package groovy.swing.impl;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableLayoutCell {
   protected static final Logger LOG = Logger.getLogger(TableLayoutCell.class.getName());
   private TableLayoutRow parent;
   private Component component;
   private GridBagConstraints constraints;
   private String align;
   private String valign;
   private int colspan = 1;
   private int rowspan = 1;
   private boolean colfill;
   private boolean rowfill;

   public int getColspan() {
      return this.colspan;
   }

   public int getRowspan() {
      return this.rowspan;
   }

   public TableLayoutCell(TableLayoutRow parent) {
      this.parent = parent;
   }

   public void addComponent(Component component) {
      if (this.component != null) {
         LOG.log(Level.WARNING, "This td cell already has a component: " + component);
      }

      this.component = component;
      this.parent.addCell(this);
   }

   public Component getComponent() {
      return this.component;
   }

   public void setAlign(String align) {
      this.align = align;
   }

   public void setValign(String valign) {
      this.valign = valign;
   }

   public void setColspan(int colspan) {
      this.colspan = colspan;
   }

   public void setRowspan(int rowspan) {
      this.rowspan = rowspan;
   }

   public boolean isColfill() {
      return this.colfill;
   }

   public boolean isRowfill() {
      return this.rowfill;
   }

   public void setColfill(boolean colfill) {
      this.colfill = colfill;
   }

   public void setRowfill(boolean rowfill) {
      this.rowfill = rowfill;
   }

   public GridBagConstraints getConstraints() {
      if (this.constraints == null) {
         this.constraints = this.createConstraints();
      }

      return this.constraints;
   }

   protected GridBagConstraints createConstraints() {
      GridBagConstraints answer = new GridBagConstraints();
      answer.anchor = this.getAnchor();
      if (this.colspan < 1) {
         this.colspan = 1;
      }

      if (this.rowspan < 1) {
         this.rowspan = 1;
      }

      if (this.isColfill()) {
         answer.fill = this.isRowfill() ? 1 : 2;
      } else {
         answer.fill = this.isRowfill() ? 3 : 0;
      }

      answer.weightx = 0.2D;
      answer.weighty = 0.0D;
      answer.gridwidth = this.colspan;
      answer.gridheight = this.rowspan;
      return answer;
   }

   protected int getAnchor() {
      boolean isTop = "top".equalsIgnoreCase(this.valign);
      boolean isBottom = "bottom".equalsIgnoreCase(this.valign);
      if ("center".equalsIgnoreCase(this.align)) {
         if (isTop) {
            return 11;
         } else {
            return isBottom ? 15 : 10;
         }
      } else if ("right".equalsIgnoreCase(this.align)) {
         if (isTop) {
            return 12;
         } else {
            return isBottom ? 14 : 13;
         }
      } else if (isTop) {
         return 18;
      } else {
         return isBottom ? 16 : 17;
      }
   }
}
