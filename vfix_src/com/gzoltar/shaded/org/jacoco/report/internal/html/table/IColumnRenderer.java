package com.gzoltar.shaded.org.jacoco.report.internal.html.table;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.HTMLElement;
import com.gzoltar.shaded.org.jacoco.report.internal.html.resources.Resources;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public interface IColumnRenderer {
   boolean init(List<? extends ITableItem> var1, ICoverageNode var2);

   void footer(HTMLElement var1, ICoverageNode var2, Resources var3, ReportOutputFolder var4) throws IOException;

   void item(HTMLElement var1, ITableItem var2, Resources var3, ReportOutputFolder var4) throws IOException;

   Comparator<ITableItem> getComparator();
}
