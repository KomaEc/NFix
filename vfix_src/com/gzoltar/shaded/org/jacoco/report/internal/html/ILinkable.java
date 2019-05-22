package com.gzoltar.shaded.org.jacoco.report.internal.html;

import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;

public interface ILinkable {
   String getLink(ReportOutputFolder var1);

   String getLinkLabel();

   String getLinkStyle();
}
