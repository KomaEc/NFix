package org.apache.maven.scm.provider.accurev;

import java.util.Date;

public class Stream {
   private String name;
   private long id;
   private String basis;
   private long basisId;
   private String depot;
   private Date startDate;
   private String streamType;

   public Stream(String name, long id, String basis, long basisId, String depot, Date startDate, String streamType) {
      this.name = name;
      this.id = id;
      this.basis = basis;
      this.basisId = basisId;
      this.depot = depot;
      this.startDate = startDate;
      this.streamType = streamType;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.basis == null ? 0 : this.basis.hashCode());
      result = 31 * result + (int)(this.basisId ^ this.basisId >>> 32);
      result = 31 * result + (this.depot == null ? 0 : this.depot.hashCode());
      result = 31 * result + (int)(this.id ^ this.id >>> 32);
      result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
      result = 31 * result + (this.startDate == null ? 0 : this.startDate.hashCode());
      result = 31 * result + (this.streamType == null ? 0 : this.streamType.hashCode());
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
         Stream other = (Stream)obj;
         if (this.basis == null) {
            if (other.basis != null) {
               return false;
            }
         } else if (!this.basis.equals(other.basis)) {
            return false;
         }

         if (this.basisId != other.basisId) {
            return false;
         } else {
            if (this.depot == null) {
               if (other.depot != null) {
                  return false;
               }
            } else if (!this.depot.equals(other.depot)) {
               return false;
            }

            if (this.id != other.id) {
               return false;
            } else {
               if (this.name == null) {
                  if (other.name != null) {
                     return false;
                  }
               } else if (!this.name.equals(other.name)) {
                  return false;
               }

               if (this.startDate == null) {
                  if (other.startDate != null) {
                     return false;
                  }
               } else if (!this.startDate.equals(other.startDate)) {
                  return false;
               }

               if (this.streamType == null) {
                  if (other.streamType != null) {
                     return false;
                  }
               } else if (!this.streamType.equals(other.streamType)) {
                  return false;
               }

               return true;
            }
         }
      }
   }

   public String getName() {
      return this.name;
   }

   public long getId() {
      return this.id;
   }

   public String getBasis() {
      return this.basis;
   }

   public long getBasisId() {
      return this.basisId;
   }

   public String getDepot() {
      return this.depot;
   }

   public Date getStartDate() {
      return this.startDate;
   }

   public String getStreamType() {
      return this.streamType;
   }

   public Boolean isWorkspace() {
      return "workspace".equals(this.streamType);
   }
}
