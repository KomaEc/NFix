package org.apache.maven.scm.provider.accurev;

public class WorkSpace {
   private long transactionId;
   private String name;

   public String getName() {
      return this.name;
   }

   public WorkSpace(String name, long transactionId) {
      this.transactionId = transactionId;
      this.name = name;
   }

   public long getTransactionId() {
      return this.transactionId;
   }

   public String toString() {
      return this.name;
   }
}
