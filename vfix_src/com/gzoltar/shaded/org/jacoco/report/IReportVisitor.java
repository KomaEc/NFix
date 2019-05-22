package com.gzoltar.shaded.org.jacoco.report;

import com.gzoltar.shaded.org.jacoco.core.data.ExecutionData;
import com.gzoltar.shaded.org.jacoco.core.data.SessionInfo;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface IReportVisitor extends IReportGroupVisitor {
   void visitInfo(List<SessionInfo> var1, Collection<ExecutionData> var2) throws IOException;

   void visitEnd() throws IOException;
}
