package com.gzoltar.shaded.org.jacoco.report.internal.html.page;

import com.gzoltar.shaded.org.jacoco.core.data.ExecutionData;
import com.gzoltar.shaded.org.jacoco.core.data.SessionInfo;
import com.gzoltar.shaded.org.jacoco.report.ILanguageNames;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.HTMLElement;
import com.gzoltar.shaded.org.jacoco.report.internal.html.IHTMLReportContext;
import com.gzoltar.shaded.org.jacoco.report.internal.html.index.ElementIndex;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SessionsPage extends ReportPage {
   private static final String MSG_SESSIONS = "This coverage report is based on execution data from the following sessions:";
   private static final String MSG_NO_SESSIONS = "No session information available.";
   private static final String MSG_EXECDATA = "Execution data for the following classes is considered in this report:";
   private static final String MSG_NO_EXECDATA = "No execution data available.";
   private final List<SessionInfo> sessionInfos;
   private final DateFormat dateFormat;
   private final List<ExecutionData> executionData;
   private final ElementIndex index;

   public SessionsPage(List<SessionInfo> sessionInfos, Collection<ExecutionData> executionData, ElementIndex index, ReportPage parent, ReportOutputFolder folder, IHTMLReportContext context) {
      super(parent, folder, context);
      this.sessionInfos = sessionInfos;
      this.executionData = new ArrayList(executionData);
      this.index = index;
      this.dateFormat = DateFormat.getDateTimeInstance(2, 2, context.getLocale());
      final ILanguageNames names = context.getLanguageNames();
      Collections.sort(this.executionData, new Comparator<ExecutionData>() {
         public int compare(ExecutionData e1, ExecutionData e2) {
            return names.getQualifiedClassName(e1.getName()).compareTo(names.getQualifiedClassName(e2.getName()));
         }
      });
   }

   protected void content(HTMLElement body) throws IOException {
      if (this.sessionInfos.isEmpty()) {
         body.p().text("No session information available.");
      } else {
         body.p().text("This coverage report is based on execution data from the following sessions:");
         this.sessionTable(body);
      }

      if (this.executionData.isEmpty()) {
         body.p().text("No execution data available.");
      } else {
         body.p().text("Execution data for the following classes is considered in this report:");
         this.executionDataTable(body);
      }

   }

   private void sessionTable(HTMLElement body) throws IOException {
      HTMLElement table = body.table("coverage");
      HTMLElement tbody = table.thead().tr();
      tbody.td().text("Session");
      tbody.td().text("Start Time");
      tbody.td().text("Dump Time");
      tbody = table.tbody();
      Iterator i$ = this.sessionInfos.iterator();

      while(i$.hasNext()) {
         SessionInfo i = (SessionInfo)i$.next();
         HTMLElement tr = tbody.tr();
         tr.td().span("el_session").text(i.getId());
         tr.td().text(this.dateFormat.format(new Date(i.getStartTimeStamp())));
         tr.td().text(this.dateFormat.format(new Date(i.getDumpTimeStamp())));
      }

   }

   private void executionDataTable(HTMLElement body) throws IOException {
      HTMLElement table = body.table("coverage");
      HTMLElement tbody = table.thead().tr();
      tbody.td().text("Class");
      tbody.td().text("Id");
      tbody = table.tbody();
      ILanguageNames names = this.context.getLanguageNames();
      Iterator i$ = this.executionData.iterator();

      while(i$.hasNext()) {
         ExecutionData e = (ExecutionData)i$.next();
         HTMLElement tr = tbody.tr();
         String link = this.index.getLinkToClass(e.getId());
         String qualifiedName = names.getQualifiedClassName(e.getName());
         if (link == null) {
            tr.td().span("el_class").text(qualifiedName);
         } else {
            tr.td().a(link, "el_class").text(qualifiedName);
         }

         String id = String.format("%016x", e.getId());
         tr.td().code().text(id);
      }

   }

   protected String getFileName() {
      return "jacoco-sessions.html";
   }

   public String getLinkStyle() {
      return "el_session";
   }

   public String getLinkLabel() {
      return "Sessions";
   }
}
