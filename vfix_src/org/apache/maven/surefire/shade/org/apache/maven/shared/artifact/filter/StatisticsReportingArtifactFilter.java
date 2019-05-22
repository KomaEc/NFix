package org.apache.maven.surefire.shade.org.apache.maven.shared.artifact.filter;

import org.codehaus.plexus.logging.Logger;

public interface StatisticsReportingArtifactFilter {
   void reportMissedCriteria(Logger var1);

   void reportFilteredArtifacts(Logger var1);

   boolean hasMissedCriteria();
}
