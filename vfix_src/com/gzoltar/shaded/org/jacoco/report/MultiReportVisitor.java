package com.gzoltar.shaded.org.jacoco.report;

import com.gzoltar.shaded.org.jacoco.core.data.ExecutionData;
import com.gzoltar.shaded.org.jacoco.core.data.SessionInfo;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MultiReportVisitor extends MultiGroupVisitor implements IReportVisitor {
   private final List<IReportVisitor> visitors;

   public MultiReportVisitor(List<IReportVisitor> visitors) {
      super(visitors);
      this.visitors = visitors;
   }

   public void visitInfo(List<SessionInfo> sessionInfos, Collection<ExecutionData> executionData) throws IOException {
      Iterator i$ = this.visitors.iterator();

      while(i$.hasNext()) {
         IReportVisitor v = (IReportVisitor)i$.next();
         v.visitInfo(sessionInfos, executionData);
      }

   }

   public void visitEnd() throws IOException {
      Iterator i$ = this.visitors.iterator();

      while(i$.hasNext()) {
         IReportVisitor v = (IReportVisitor)i$.next();
         v.visitEnd();
      }

   }
}
