package org.apache.maven.doxia.site.decoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Menu implements Serializable {
   private String name;
   private String inherit;
   private boolean inheritAsRef = false;
   private String ref;
   private String img;
   private List items;

   public void addItem(MenuItem menuItem) {
      if (!(menuItem instanceof MenuItem)) {
         throw new ClassCastException("Menu.addItems(menuItem) parameter must be instanceof " + MenuItem.class.getName());
      } else {
         this.getItems().add(menuItem);
      }
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof Menu)) {
         return false;
      } else {
         boolean var10000;
         Menu that;
         boolean result;
         label89: {
            label88: {
               that = (Menu)other;
               result = true;
               if (result) {
                  if (this.getName() == null) {
                     if (that.getName() == null) {
                        break label88;
                     }
                  } else if (this.getName().equals(that.getName())) {
                     break label88;
                  }
               }

               var10000 = false;
               break label89;
            }

            var10000 = true;
         }

         label80: {
            label79: {
               result = var10000;
               if (result) {
                  if (this.getInherit() == null) {
                     if (that.getInherit() == null) {
                        break label79;
                     }
                  } else if (this.getInherit().equals(that.getInherit())) {
                     break label79;
                  }
               }

               var10000 = false;
               break label80;
            }

            var10000 = true;
         }

         label71: {
            label70: {
               result = var10000;
               if (result) {
                  if (this.getRef() == null) {
                     if (that.getRef() == null) {
                        break label70;
                     }
                  } else if (this.getRef().equals(that.getRef())) {
                     break label70;
                  }
               }

               var10000 = false;
               break label71;
            }

            var10000 = true;
         }

         label99: {
            result = var10000;
            if (result) {
               label98: {
                  if (this.getImg() == null) {
                     if (that.getImg() != null) {
                        break label98;
                     }
                  } else if (!this.getImg().equals(that.getImg())) {
                     break label98;
                  }

                  var10000 = true;
                  break label99;
               }
            }

            var10000 = false;
         }

         label52: {
            label51: {
               result = var10000;
               if (result) {
                  if (this.getItems() == null) {
                     if (that.getItems() == null) {
                        break label51;
                     }
                  } else if (this.getItems().equals(that.getItems())) {
                     break label51;
                  }
               }

               var10000 = false;
               break label52;
            }

            var10000 = true;
         }

         result = var10000;
         return result;
      }
   }

   public String getImg() {
      return this.img;
   }

   public String getInherit() {
      return this.inherit;
   }

   public List getItems() {
      if (this.items == null) {
         this.items = new ArrayList();
      }

      return this.items;
   }

   public String getName() {
      return this.name;
   }

   public String getRef() {
      return this.ref;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.name != null ? this.name.hashCode() : 0);
      result = 37 * result + (this.inherit != null ? this.inherit.hashCode() : 0);
      result = 37 * result + (this.ref != null ? this.ref.hashCode() : 0);
      result = 37 * result + (this.img != null ? this.img.hashCode() : 0);
      result = 37 * result + (this.items != null ? this.items.hashCode() : 0);
      return result;
   }

   public boolean isInheritAsRef() {
      return this.inheritAsRef;
   }

   public void removeItem(MenuItem menuItem) {
      if (!(menuItem instanceof MenuItem)) {
         throw new ClassCastException("Menu.removeItems(menuItem) parameter must be instanceof " + MenuItem.class.getName());
      } else {
         this.getItems().remove(menuItem);
      }
   }

   public void setImg(String img) {
      this.img = img;
   }

   public void setInherit(String inherit) {
      this.inherit = inherit;
   }

   public void setInheritAsRef(boolean inheritAsRef) {
      this.inheritAsRef = inheritAsRef;
   }

   public void setItems(List items) {
      this.items = items;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setRef(String ref) {
      this.ref = ref;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("name = '");
      buf.append(this.getName());
      buf.append("'");
      buf.append("\n");
      buf.append("inherit = '");
      buf.append(this.getInherit());
      buf.append("'");
      buf.append("\n");
      buf.append("ref = '");
      buf.append(this.getRef());
      buf.append("'");
      buf.append("\n");
      buf.append("img = '");
      buf.append(this.getImg());
      buf.append("'");
      buf.append("\n");
      buf.append("items = '");
      buf.append(this.getItems());
      buf.append("'");
      return buf.toString();
   }
}
