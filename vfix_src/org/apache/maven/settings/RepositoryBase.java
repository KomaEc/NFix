package org.apache.maven.settings;

import java.io.Serializable;

public class RepositoryBase implements Serializable {
   private String id;
   private String name;
   private String url;
   private String layout = "default";

   public String getId() {
      return this.id;
   }

   public String getLayout() {
      return this.layout;
   }

   public String getName() {
      return this.name;
   }

   public String getUrl() {
      return this.url;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setLayout(String layout) {
      this.layout = layout;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public boolean equals(Object obj) {
      RepositoryBase other = (RepositoryBase)obj;
      boolean retValue = false;
      if (this.id != null) {
         retValue = this.id.equals(other.id);
      }

      return retValue;
   }
}
