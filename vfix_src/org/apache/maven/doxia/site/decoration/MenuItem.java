package org.apache.maven.doxia.site.decoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MenuItem extends LinkItem implements Serializable {
   private String description;
   private boolean collapse = false;
   private String ref;
   private List items;

   public void addItem(MenuItem menuItem) {
      if (!(menuItem instanceof MenuItem)) {
         throw new ClassCastException("MenuItem.addItems(menuItem) parameter must be instanceof " + MenuItem.class.getName());
      } else {
         this.getItems().add(menuItem);
         menuItem.createMenuItemAssociation(this);
      }
   }

   public void breakMenuItemAssociation(MenuItem menuItem) {
      if (!this.getItems().contains(menuItem)) {
         throw new IllegalStateException("menuItem isn't associated.");
      } else {
         this.getItems().remove(menuItem);
      }
   }

   public void createMenuItemAssociation(MenuItem menuItem) {
      Collection items = this.getItems();
      if (items.contains(menuItem)) {
         throw new IllegalStateException("menuItem is already assigned.");
      } else {
         items.add(menuItem);
      }
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof MenuItem)) {
         return false;
      } else {
         boolean var10000;
         MenuItem that;
         boolean result;
         label74: {
            label73: {
               that = (MenuItem)other;
               result = true;
               if (result) {
                  if (this.getDescription() == null) {
                     if (that.getDescription() == null) {
                        break label73;
                     }
                  } else if (this.getDescription().equals(that.getDescription())) {
                     break label73;
                  }
               }

               var10000 = false;
               break label74;
            }

            var10000 = true;
         }

         label60: {
            label59: {
               result = var10000;
               result = result && this.collapse == that.collapse;
               if (result) {
                  if (this.getRef() == null) {
                     if (that.getRef() == null) {
                        break label59;
                     }
                  } else if (this.getRef().equals(that.getRef())) {
                     break label59;
                  }
               }

               var10000 = false;
               break label60;
            }

            var10000 = true;
         }

         label51: {
            label50: {
               result = var10000;
               if (result) {
                  if (this.getItems() == null) {
                     if (that.getItems() == null) {
                        break label50;
                     }
                  } else if (this.getItems().equals(that.getItems())) {
                     break label50;
                  }
               }

               var10000 = false;
               break label51;
            }

            var10000 = true;
         }

         result = var10000;
         result = result && super.equals(other);
         return result;
      }
   }

   public String getDescription() {
      return this.description;
   }

   public List getItems() {
      if (this.items == null) {
         this.items = new ArrayList();
      }

      return this.items;
   }

   public String getRef() {
      return this.ref;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.description != null ? this.description.hashCode() : 0);
      result = 37 * result + (this.collapse ? 0 : 1);
      result = 37 * result + (this.ref != null ? this.ref.hashCode() : 0);
      result = 37 * result + (this.items != null ? this.items.hashCode() : 0);
      result = 37 * result + super.hashCode();
      return result;
   }

   public boolean isCollapse() {
      return this.collapse;
   }

   public void removeItem(MenuItem menuItem) {
      if (!(menuItem instanceof MenuItem)) {
         throw new ClassCastException("MenuItem.removeItems(menuItem) parameter must be instanceof " + MenuItem.class.getName());
      } else {
         menuItem.breakMenuItemAssociation(this);
         this.getItems().remove(menuItem);
      }
   }

   public void setCollapse(boolean collapse) {
      this.collapse = collapse;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setItems(List items) {
      this.items = items;
   }

   public void setRef(String ref) {
      this.ref = ref;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("description = '");
      buf.append(this.getDescription());
      buf.append("'");
      buf.append("\n");
      buf.append("collapse = '");
      buf.append(this.isCollapse());
      buf.append("'");
      buf.append("\n");
      buf.append("ref = '");
      buf.append(this.getRef());
      buf.append("'");
      buf.append("\n");
      buf.append("items = '");
      buf.append(this.getItems());
      buf.append("'");
      buf.append("\n");
      buf.append(super.toString());
      return buf.toString();
   }
}
