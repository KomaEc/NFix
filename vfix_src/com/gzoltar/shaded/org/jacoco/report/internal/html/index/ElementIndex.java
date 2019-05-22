package com.gzoltar.shaded.org.jacoco.report.internal.html.index;

import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.ILinkable;
import java.util.HashMap;
import java.util.Map;

public class ElementIndex implements IIndexUpdate {
   private final ReportOutputFolder baseFolder;
   private final Map<Long, String> allClasses = new HashMap();

   public ElementIndex(ReportOutputFolder baseFolder) {
      this.baseFolder = baseFolder;
   }

   public String getLinkToClass(long classid) {
      return (String)this.allClasses.get(classid);
   }

   public void addClass(ILinkable link, long classid) {
      this.allClasses.put(classid, link.getLink(this.baseFolder));
   }
}
