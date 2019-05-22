package com.gzoltar.shaded.org.jacoco.report.internal.html.table;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.report.internal.html.ILinkable;

public interface ITableItem extends ILinkable {
   ICoverageNode getNode();
}
