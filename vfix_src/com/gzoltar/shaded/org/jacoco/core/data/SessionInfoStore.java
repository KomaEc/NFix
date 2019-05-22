package com.gzoltar.shaded.org.jacoco.core.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SessionInfoStore implements ISessionInfoVisitor {
   private final List<SessionInfo> infos = new ArrayList();

   public boolean isEmpty() {
      return this.infos.isEmpty();
   }

   public List<SessionInfo> getInfos() {
      List<SessionInfo> copy = new ArrayList(this.infos);
      Collections.sort(copy);
      return copy;
   }

   public SessionInfo getMerged(String id) {
      if (this.infos.isEmpty()) {
         return new SessionInfo(id, 0L, 0L);
      } else {
         long start = Long.MAX_VALUE;
         long dump = Long.MIN_VALUE;

         SessionInfo i;
         for(Iterator i$ = this.infos.iterator(); i$.hasNext(); dump = Math.max(dump, i.getDumpTimeStamp())) {
            i = (SessionInfo)i$.next();
            start = Math.min(start, i.getStartTimeStamp());
         }

         return new SessionInfo(id, start, dump);
      }
   }

   public void accept(ISessionInfoVisitor visitor) {
      Iterator i$ = this.getInfos().iterator();

      while(i$.hasNext()) {
         SessionInfo i = (SessionInfo)i$.next();
         visitor.visitSessionInfo(i);
      }

   }

   public void visitSessionInfo(SessionInfo info) {
      this.infos.add(info);
   }
}
