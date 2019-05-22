package org.apache.maven.wagon.repository;

import java.io.Serializable;
import java.util.Properties;
import org.apache.maven.wagon.PathUtils;
import org.codehaus.plexus.util.StringUtils;

public class Repository implements Serializable {
   private static final long serialVersionUID = 1312227676322136247L;
   private String id;
   private String name;
   private String host;
   private int port = -1;
   private String basedir;
   private String protocol;
   private String url;
   private RepositoryPermissions permissions;
   private Properties parameters = new Properties();
   private String username = null;
   private String password = null;

   /** @deprecated */
   public Repository() {
   }

   public Repository(String id, String url) {
      if (id == null) {
         throw new NullPointerException("id can not be null");
      } else {
         this.setId(id);
         if (url == null) {
            throw new NullPointerException("url can not be null");
         } else {
            this.setUrl(url);
         }
      }
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getBasedir() {
      return this.basedir;
   }

   public void setBasedir(String basedir) {
      this.basedir = basedir;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getPort() {
      return this.port;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public void setUrl(String url) {
      this.url = url;
      this.protocol = PathUtils.protocol(url);
      this.host = PathUtils.host(url);
      this.port = PathUtils.port(url);
      this.basedir = PathUtils.basedir(url);
      String username = PathUtils.user(url);
      this.username = username;
      if (username != null) {
         String password = PathUtils.password(url);
         if (password != null) {
            this.password = password;
            username = username + ":" + password;
         }

         username = username + "@";
         int index = url.indexOf(username);
         this.url = url.substring(0, index) + url.substring(index + username.length());
      }

   }

   public String getUrl() {
      if (this.url != null) {
         return this.url;
      } else {
         StringBuffer sb = new StringBuffer();
         sb.append(this.protocol);
         sb.append("://");
         sb.append(this.host);
         if (this.port != -1) {
            sb.append(":");
            sb.append(this.port);
         }

         sb.append(this.basedir);
         return sb.toString();
      }
   }

   public String getHost() {
      return this.host == null ? "localhost" : this.host;
   }

   public String getName() {
      return this.name == null ? this.getId() : this.name;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("Repository[");
      if (StringUtils.isNotEmpty(this.getName())) {
         sb.append(this.getName()).append("|");
      }

      sb.append(this.getUrl());
      sb.append("]");
      return sb.toString();
   }

   public String getProtocol() {
      return this.protocol;
   }

   public RepositoryPermissions getPermissions() {
      return this.permissions;
   }

   public void setPermissions(RepositoryPermissions permissions) {
      this.permissions = permissions;
   }

   public String getParameter(String key) {
      return this.parameters.getProperty(key);
   }

   public void setParameters(Properties parameters) {
      this.parameters = parameters;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         Repository other = (Repository)obj;
         if (this.id == null) {
            if (other.id != null) {
               return false;
            }
         } else if (!this.id.equals(other.id)) {
            return false;
         }

         return true;
      }
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }

   public void setProtocol(String protocol) {
      this.protocol = protocol;
   }
}
