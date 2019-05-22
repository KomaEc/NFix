package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common;

public interface PrintStreamCoordinator {
   void replaceSystemOutputsWithSLF4JPrintStreams();

   void restoreOriginalSystemOutputs();
}
