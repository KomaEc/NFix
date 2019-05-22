package org.apache.maven.scm.provider.accurev;

import java.util.Date;
import org.codehaus.plexus.util.StringUtils;

public class AccuRevVersion {
   private String basisStream;
   private String timeSpec;

   public AccuRevVersion(String basisStream, String tran) {
      this.basisStream = basisStream;
      this.timeSpec = tran;
   }

   public String getBasisStream() {
      return this.basisStream;
   }

   public String getTimeSpec() {
      return this.timeSpec;
   }

   public AccuRevVersion(String basis, Date startDate) {
      this(basis, AccuRev.ACCUREV_TIME_SPEC.format(startDate));
   }

   public AccuRevVersion(String basis, long transactionId) {
      this(basis, Long.toString(transactionId));
   }

   public boolean isNow() {
      return isNow(this.timeSpec);
   }

   public String toString() {
      return String.format("AccuRevVersion: stream = %s, transaction= %s", this.basisStream, this.timeSpec);
   }

   public static boolean isNow(String timeSpec) {
      return StringUtils.isBlank(timeSpec) || "highest".equalsIgnoreCase(timeSpec) || "now".equalsIgnoreCase(timeSpec);
   }
}
