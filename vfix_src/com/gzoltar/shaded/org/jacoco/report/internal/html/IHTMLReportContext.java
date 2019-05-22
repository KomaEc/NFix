package com.gzoltar.shaded.org.jacoco.report.internal.html;

import com.gzoltar.shaded.org.jacoco.report.ILanguageNames;
import com.gzoltar.shaded.org.jacoco.report.internal.html.index.IIndexUpdate;
import com.gzoltar.shaded.org.jacoco.report.internal.html.resources.Resources;
import com.gzoltar.shaded.org.jacoco.report.internal.html.table.Table;
import java.util.Locale;

public interface IHTMLReportContext {
   Resources getResources();

   ILanguageNames getLanguageNames();

   Table getTable();

   String getFooterText();

   ILinkable getSessionsPage();

   String getOutputEncoding();

   IIndexUpdate getIndexUpdate();

   Locale getLocale();
}
