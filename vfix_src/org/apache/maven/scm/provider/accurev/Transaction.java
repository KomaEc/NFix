package org.apache.maven.scm.provider.accurev;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class Transaction {
   private Collection<Transaction.Version> versions = new HashSet();
   private long id;
   private Date when;
   private String tranType;
   private String author;
   private String comment;

   public Transaction(Long id, Date when, String tranType, String user) {
      this.id = id;
      this.tranType = tranType;
      this.when = when;
      this.author = user;
   }

   public long getId() {
      return this.id;
   }

   public String getTranType() {
      return this.tranType;
   }

   public String getComment() {
      return this.comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public long getTranId() {
      return this.id;
   }

   private Transaction getOuterTransaction() {
      return this;
   }

   public Collection<Transaction.Version> getVersions() {
      return this.versions;
   }

   public Date getWhen() {
      return this.when;
   }

   public String getType() {
      return this.tranType;
   }

   public String getAuthor() {
      return this.author;
   }

   public void addVersion(Long id, String name, String virtualSpec, String realSpec, String ancestor) {
      Transaction.Version v = new Transaction.Version(id, name, virtualSpec, realSpec, ancestor);
      this.versions.add(v);
   }

   public String toString() {
      return String.format("Transaction: %d, %s at %tc by %s -'%s'", this.getId(), this.getTranType(), this.getWhen(), this.getAuthor(), this.getComment());
   }

   public class Version {
      private String realSpec;
      private String virtualSpec;
      private String ancestorSpec;
      private Long elementId;
      private String elementName;

      private Version(Long id, String elementName, String virtualSpec, String realSpec, String ancestor) {
         this.elementId = id;
         this.virtualSpec = virtualSpec;
         this.realSpec = realSpec;
         this.ancestorSpec = ancestor;
         this.elementName = elementName;
      }

      public String getVirtualSpec() {
         return this.virtualSpec;
      }

      public void setVirtualSpec(String virtualSpec) {
         this.virtualSpec = virtualSpec;
      }

      public String getAncestorSpec() {
         return this.ancestorSpec;
      }

      public void setAncestorSpec(String ancestorSpec) {
         this.ancestorSpec = ancestorSpec;
      }

      public void setRealSpec(String realSpec) {
         this.realSpec = realSpec;
      }

      public void setElementId(Long elementId) {
         this.elementId = elementId;
      }

      public String getRealSpec() {
         return this.realSpec;
      }

      public Long getElementId() {
         return this.elementId;
      }

      public Transaction getTransaction() {
         return Transaction.this.getOuterTransaction();
      }

      public String getElementName() {
         return this.elementName;
      }

      public String toString() {
         return String.format("Version: %s (%d) %s (%s) anc=%s", this.elementName, this.elementId, this.virtualSpec, this.realSpec, this.ancestorSpec);
      }

      // $FF: synthetic method
      Version(Long x1, String x2, String x3, String x4, String x5, Object x6) {
         this(x1, x2, x3, x4, x5);
      }
   }
}
