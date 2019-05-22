package com.gzoltar.shaded.org.jacoco.core.data;

public class SessionInfo implements Comparable<SessionInfo> {
   private final String id;
   private final long start;
   private final long dump;

   public SessionInfo(String id, long start, long dump) {
      if (id == null) {
         throw new IllegalArgumentException();
      } else {
         this.id = id;
         this.start = start;
         this.dump = dump;
      }
   }

   public String getId() {
      return this.id;
   }

   public long getStartTimeStamp() {
      return this.start;
   }

   public long getDumpTimeStamp() {
      return this.dump;
   }

   public int compareTo(SessionInfo other) {
      if (this.dump < other.dump) {
         return -1;
      } else {
         return this.dump > other.dump ? 1 : 0;
      }
   }

   public String toString() {
      return "SessionInfo[" + this.id + "]";
   }
}
