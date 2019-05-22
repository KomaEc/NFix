package org.apache.maven.doxia.site.decoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DecorationModel implements Serializable {
   private String name;
   private Banner bannerLeft;
   private Banner bannerRight;
   private PublishDate publishDate;
   private Version version;
   private List poweredBy;
   private Skin skin;
   private Body body;
   private Object custom;
   private String modelEncoding = "UTF-8";
   private Map menusByRef;

   public void addPoweredBy(Logo logo) {
      if (!(logo instanceof Logo)) {
         throw new ClassCastException("DecorationModel.addPoweredBy(logo) parameter must be instanceof " + Logo.class.getName());
      } else {
         this.getPoweredBy().add(logo);
      }
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof DecorationModel)) {
         return false;
      } else {
         boolean var10000;
         DecorationModel that;
         boolean result;
         label153: {
            label152: {
               that = (DecorationModel)other;
               result = true;
               if (result) {
                  if (this.getName() == null) {
                     if (that.getName() == null) {
                        break label152;
                     }
                  } else if (this.getName().equals(that.getName())) {
                     break label152;
                  }
               }

               var10000 = false;
               break label153;
            }

            var10000 = true;
         }

         label144: {
            label143: {
               result = var10000;
               if (result) {
                  if (this.getBannerLeft() == null) {
                     if (that.getBannerLeft() == null) {
                        break label143;
                     }
                  } else if (this.getBannerLeft().equals(that.getBannerLeft())) {
                     break label143;
                  }
               }

               var10000 = false;
               break label144;
            }

            var10000 = true;
         }

         label135: {
            label134: {
               result = var10000;
               if (result) {
                  if (this.getBannerRight() == null) {
                     if (that.getBannerRight() == null) {
                        break label134;
                     }
                  } else if (this.getBannerRight().equals(that.getBannerRight())) {
                     break label134;
                  }
               }

               var10000 = false;
               break label135;
            }

            var10000 = true;
         }

         label163: {
            result = var10000;
            if (result) {
               label162: {
                  if (this.getPublishDate() == null) {
                     if (that.getPublishDate() != null) {
                        break label162;
                     }
                  } else if (!this.getPublishDate().equals(that.getPublishDate())) {
                     break label162;
                  }

                  var10000 = true;
                  break label163;
               }
            }

            var10000 = false;
         }

         label116: {
            label115: {
               result = var10000;
               if (result) {
                  if (this.getVersion() == null) {
                     if (that.getVersion() == null) {
                        break label115;
                     }
                  } else if (this.getVersion().equals(that.getVersion())) {
                     break label115;
                  }
               }

               var10000 = false;
               break label116;
            }

            var10000 = true;
         }

         label107: {
            label106: {
               result = var10000;
               if (result) {
                  if (this.getPoweredBy() == null) {
                     if (that.getPoweredBy() == null) {
                        break label106;
                     }
                  } else if (this.getPoweredBy().equals(that.getPoweredBy())) {
                     break label106;
                  }
               }

               var10000 = false;
               break label107;
            }

            var10000 = true;
         }

         label98: {
            label97: {
               result = var10000;
               if (result) {
                  if (this.getSkin() == null) {
                     if (that.getSkin() == null) {
                        break label97;
                     }
                  } else if (this.getSkin().equals(that.getSkin())) {
                     break label97;
                  }
               }

               var10000 = false;
               break label98;
            }

            var10000 = true;
         }

         label89: {
            label88: {
               result = var10000;
               if (result) {
                  if (this.getBody() == null) {
                     if (that.getBody() == null) {
                        break label88;
                     }
                  } else if (this.getBody().equals(that.getBody())) {
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
                  if (this.getCustom() == null) {
                     if (that.getCustom() == null) {
                        break label79;
                     }
                  } else if (this.getCustom().equals(that.getCustom())) {
                     break label79;
                  }
               }

               var10000 = false;
               break label80;
            }

            var10000 = true;
         }

         result = var10000;
         return result;
      }
   }

   public Banner getBannerLeft() {
      return this.bannerLeft;
   }

   public Banner getBannerRight() {
      return this.bannerRight;
   }

   public Body getBody() {
      return this.body;
   }

   public Object getCustom() {
      return this.custom;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }

   public String getName() {
      return this.name;
   }

   public List getPoweredBy() {
      if (this.poweredBy == null) {
         this.poweredBy = new ArrayList();
      }

      return this.poweredBy;
   }

   public PublishDate getPublishDate() {
      return this.publishDate;
   }

   public Skin getSkin() {
      return this.skin;
   }

   public Version getVersion() {
      return this.version;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.name != null ? this.name.hashCode() : 0);
      result = 37 * result + (this.bannerLeft != null ? this.bannerLeft.hashCode() : 0);
      result = 37 * result + (this.bannerRight != null ? this.bannerRight.hashCode() : 0);
      result = 37 * result + (this.publishDate != null ? this.publishDate.hashCode() : 0);
      result = 37 * result + (this.version != null ? this.version.hashCode() : 0);
      result = 37 * result + (this.poweredBy != null ? this.poweredBy.hashCode() : 0);
      result = 37 * result + (this.skin != null ? this.skin.hashCode() : 0);
      result = 37 * result + (this.body != null ? this.body.hashCode() : 0);
      result = 37 * result + (this.custom != null ? this.custom.hashCode() : 0);
      return result;
   }

   public void removePoweredBy(Logo logo) {
      if (!(logo instanceof Logo)) {
         throw new ClassCastException("DecorationModel.removePoweredBy(logo) parameter must be instanceof " + Logo.class.getName());
      } else {
         this.getPoweredBy().remove(logo);
      }
   }

   public void setBannerLeft(Banner bannerLeft) {
      this.bannerLeft = bannerLeft;
   }

   public void setBannerRight(Banner bannerRight) {
      this.bannerRight = bannerRight;
   }

   public void setBody(Body body) {
      this.body = body;
   }

   public void setCustom(Object custom) {
      this.custom = custom;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setPoweredBy(List poweredBy) {
      this.poweredBy = poweredBy;
   }

   public void setPublishDate(PublishDate publishDate) {
      this.publishDate = publishDate;
   }

   public void setSkin(Skin skin) {
      this.skin = skin;
   }

   public void setVersion(Version version) {
      this.version = version;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("name = '");
      buf.append(this.getName());
      buf.append("'");
      buf.append("\n");
      buf.append("bannerLeft = '");
      buf.append(this.getBannerLeft());
      buf.append("'");
      buf.append("\n");
      buf.append("bannerRight = '");
      buf.append(this.getBannerRight());
      buf.append("'");
      buf.append("\n");
      buf.append("publishDate = '");
      buf.append(this.getPublishDate());
      buf.append("'");
      buf.append("\n");
      buf.append("version = '");
      buf.append(this.getVersion());
      buf.append("'");
      buf.append("\n");
      buf.append("poweredBy = '");
      buf.append(this.getPoweredBy());
      buf.append("'");
      buf.append("\n");
      buf.append("skin = '");
      buf.append(this.getSkin());
      buf.append("'");
      buf.append("\n");
      buf.append("body = '");
      buf.append(this.getBody());
      buf.append("'");
      buf.append("\n");
      buf.append("custom = '");
      buf.append(this.getCustom());
      buf.append("'");
      return buf.toString();
   }

   public Menu getMenuRef(String key) {
      if (this.menusByRef == null) {
         this.menusByRef = new HashMap();
         if (this.body != null) {
            Iterator i = this.body.getMenus().iterator();

            while(i.hasNext()) {
               Menu menu = (Menu)i.next();
               if (menu.getRef() != null) {
                  this.menusByRef.put(menu.getRef(), menu);
               }
            }
         }
      }

      return (Menu)this.menusByRef.get(key);
   }

   public void removeMenuRef(String key) {
      if (this.body != null) {
         Iterator i = this.body.getMenus().iterator();

         while(i.hasNext()) {
            Menu menu = (Menu)i.next();
            if (key.equals(menu.getRef())) {
               i.remove();
            }
         }
      }

   }

   public List getMenus() {
      List menus;
      if (this.body != null && this.body.getMenus() != null) {
         menus = this.body.getMenus();
      } else {
         menus = Collections.EMPTY_LIST;
      }

      return menus;
   }
}
