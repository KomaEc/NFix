package polyglot.frontend;

import java.util.Date;

public class Source {
   protected String name;
   protected String path;
   protected Date lastModified;
   protected boolean userSpecified;

   protected Source(String name) {
      this(name, (String)null, (Date)null, false);
   }

   protected Source(String name, boolean userSpecified) {
      this(name, (String)null, (Date)null, userSpecified);
   }

   public Source(String name, String path, Date lastModified) {
      this(name, path, lastModified, false);
   }

   public Source(String name, String path, Date lastModified, boolean userSpecified) {
      this.name = name;
      this.path = path;
      this.lastModified = lastModified;
      this.userSpecified = userSpecified;
   }

   public boolean equals(Object o) {
      if (!(o instanceof Source)) {
         return false;
      } else {
         Source s = (Source)o;
         return this.name.equals(s.name) && this.path.equals(s.path);
      }
   }

   public int hashCode() {
      return this.path.hashCode() + this.name.hashCode();
   }

   public String name() {
      return this.name;
   }

   public String path() {
      return this.path;
   }

   public Date lastModified() {
      return this.lastModified;
   }

   public String toString() {
      return this.path;
   }

   public void setUserSpecified(boolean userSpecified) {
      this.userSpecified = userSpecified;
   }

   public boolean userSpecified() {
      return this.userSpecified;
   }
}
