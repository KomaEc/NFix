package org.apache.maven.doxia.site.decoration;

import java.io.Serializable;

public class Skin implements Serializable {
   private String groupId;
   private String artifactId;
   private String version;

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof Skin)) {
         return false;
      } else {
         boolean var10000;
         Skin that;
         boolean result;
         label56: {
            label55: {
               that = (Skin)other;
               result = true;
               if (result) {
                  if (this.getGroupId() == null) {
                     if (that.getGroupId() == null) {
                        break label55;
                     }
                  } else if (this.getGroupId().equals(that.getGroupId())) {
                     break label55;
                  }
               }

               var10000 = false;
               break label56;
            }

            var10000 = true;
         }

         label47: {
            label46: {
               result = var10000;
               if (result) {
                  if (this.getArtifactId() == null) {
                     if (that.getArtifactId() == null) {
                        break label46;
                     }
                  } else if (this.getArtifactId().equals(that.getArtifactId())) {
                     break label46;
                  }
               }

               var10000 = false;
               break label47;
            }

            var10000 = true;
         }

         label38: {
            label37: {
               result = var10000;
               if (result) {
                  if (this.getVersion() == null) {
                     if (that.getVersion() == null) {
                        break label37;
                     }
                  } else if (this.getVersion().equals(that.getVersion())) {
                     break label37;
                  }
               }

               var10000 = false;
               break label38;
            }

            var10000 = true;
         }

         result = var10000;
         return result;
      }
   }

   public String getArtifactId() {
      return this.artifactId;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public String getVersion() {
      return this.version;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.groupId != null ? this.groupId.hashCode() : 0);
      result = 37 * result + (this.artifactId != null ? this.artifactId.hashCode() : 0);
      result = 37 * result + (this.version != null ? this.version.hashCode() : 0);
      return result;
   }

   public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("groupId = '");
      buf.append(this.getGroupId());
      buf.append("'");
      buf.append("\n");
      buf.append("artifactId = '");
      buf.append(this.getArtifactId());
      buf.append("'");
      buf.append("\n");
      buf.append("version = '");
      buf.append(this.getVersion());
      buf.append("'");
      return buf.toString();
   }

   public static Skin getDefaultSkin() {
      Skin skin = new Skin();
      skin.setGroupId("org.apache.maven.skins");
      skin.setArtifactId("maven-default-skin");
      return skin;
   }
}
