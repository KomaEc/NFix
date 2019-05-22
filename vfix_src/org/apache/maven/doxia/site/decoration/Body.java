package org.apache.maven.doxia.site.decoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Body implements Serializable {
   private Object head;
   private List links;
   private List breadcrumbs;
   private List menus;

   public void addBreadcrumb(LinkItem linkItem) {
      if (!(linkItem instanceof LinkItem)) {
         throw new ClassCastException("Body.addBreadcrumbs(linkItem) parameter must be instanceof " + LinkItem.class.getName());
      } else {
         this.getBreadcrumbs().add(linkItem);
      }
   }

   public void addLink(LinkItem linkItem) {
      if (!(linkItem instanceof LinkItem)) {
         throw new ClassCastException("Body.addLinks(linkItem) parameter must be instanceof " + LinkItem.class.getName());
      } else {
         this.getLinks().add(linkItem);
      }
   }

   public void addMenu(Menu menu) {
      if (!(menu instanceof Menu)) {
         throw new ClassCastException("Body.addMenus(menu) parameter must be instanceof " + Menu.class.getName());
      } else {
         this.getMenus().add(menu);
      }
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof Body)) {
         return false;
      } else {
         boolean var10000;
         Body that;
         boolean result;
         label73: {
            label72: {
               that = (Body)other;
               result = true;
               if (result) {
                  if (this.getHead() == null) {
                     if (that.getHead() == null) {
                        break label72;
                     }
                  } else if (this.getHead().equals(that.getHead())) {
                     break label72;
                  }
               }

               var10000 = false;
               break label73;
            }

            var10000 = true;
         }

         label64: {
            label63: {
               result = var10000;
               if (result) {
                  if (this.getLinks() == null) {
                     if (that.getLinks() == null) {
                        break label63;
                     }
                  } else if (this.getLinks().equals(that.getLinks())) {
                     break label63;
                  }
               }

               var10000 = false;
               break label64;
            }

            var10000 = true;
         }

         label55: {
            label54: {
               result = var10000;
               if (result) {
                  if (this.getBreadcrumbs() == null) {
                     if (that.getBreadcrumbs() == null) {
                        break label54;
                     }
                  } else if (this.getBreadcrumbs().equals(that.getBreadcrumbs())) {
                     break label54;
                  }
               }

               var10000 = false;
               break label55;
            }

            var10000 = true;
         }

         label83: {
            result = var10000;
            if (result) {
               label82: {
                  if (this.getMenus() == null) {
                     if (that.getMenus() != null) {
                        break label82;
                     }
                  } else if (!this.getMenus().equals(that.getMenus())) {
                     break label82;
                  }

                  var10000 = true;
                  break label83;
               }
            }

            var10000 = false;
         }

         result = var10000;
         return result;
      }
   }

   public List getBreadcrumbs() {
      if (this.breadcrumbs == null) {
         this.breadcrumbs = new ArrayList();
      }

      return this.breadcrumbs;
   }

   public Object getHead() {
      return this.head;
   }

   public List getLinks() {
      if (this.links == null) {
         this.links = new ArrayList();
      }

      return this.links;
   }

   public List getMenus() {
      if (this.menus == null) {
         this.menus = new ArrayList();
      }

      return this.menus;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.head != null ? this.head.hashCode() : 0);
      result = 37 * result + (this.links != null ? this.links.hashCode() : 0);
      result = 37 * result + (this.breadcrumbs != null ? this.breadcrumbs.hashCode() : 0);
      result = 37 * result + (this.menus != null ? this.menus.hashCode() : 0);
      return result;
   }

   public void removeBreadcrumb(LinkItem linkItem) {
      if (!(linkItem instanceof LinkItem)) {
         throw new ClassCastException("Body.removeBreadcrumbs(linkItem) parameter must be instanceof " + LinkItem.class.getName());
      } else {
         this.getBreadcrumbs().remove(linkItem);
      }
   }

   public void removeLink(LinkItem linkItem) {
      if (!(linkItem instanceof LinkItem)) {
         throw new ClassCastException("Body.removeLinks(linkItem) parameter must be instanceof " + LinkItem.class.getName());
      } else {
         this.getLinks().remove(linkItem);
      }
   }

   public void removeMenu(Menu menu) {
      if (!(menu instanceof Menu)) {
         throw new ClassCastException("Body.removeMenus(menu) parameter must be instanceof " + Menu.class.getName());
      } else {
         this.getMenus().remove(menu);
      }
   }

   public void setBreadcrumbs(List breadcrumbs) {
      this.breadcrumbs = breadcrumbs;
   }

   public void setHead(Object head) {
      this.head = head;
   }

   public void setLinks(List links) {
      this.links = links;
   }

   public void setMenus(List menus) {
      this.menus = menus;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("head = '");
      buf.append(this.getHead());
      buf.append("'");
      buf.append("\n");
      buf.append("links = '");
      buf.append(this.getLinks());
      buf.append("'");
      buf.append("\n");
      buf.append("breadcrumbs = '");
      buf.append(this.getBreadcrumbs());
      buf.append("'");
      buf.append("\n");
      buf.append("menus = '");
      buf.append(this.getMenus());
      buf.append("'");
      return buf.toString();
   }
}
