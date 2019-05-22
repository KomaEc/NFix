package com.gzoltar.shaded.org.pitest.reloc.xstream.io.path;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ErrorWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.ReaderWrapper;

public class PathTrackingReader extends ReaderWrapper {
   private final PathTracker pathTracker;

   public PathTrackingReader(HierarchicalStreamReader reader, PathTracker pathTracker) {
      super(reader);
      this.pathTracker = pathTracker;
      pathTracker.pushElement(this.getNodeName());
   }

   public void moveDown() {
      super.moveDown();
      this.pathTracker.pushElement(this.getNodeName());
   }

   public void moveUp() {
      super.moveUp();
      this.pathTracker.popElement();
   }

   public void appendErrors(ErrorWriter errorWriter) {
      errorWriter.add("path", this.pathTracker.getPath().toString());
      super.appendErrors(errorWriter);
   }
}
