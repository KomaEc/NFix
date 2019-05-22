package org.apache.tools.ant;

import java.io.IOException;
import java.io.InputStream;

public class DemuxInputStream extends InputStream {
   private Project project;

   public DemuxInputStream(Project project) {
      this.project = project;
   }

   public int read() throws IOException {
      byte[] buffer = new byte[1];
      return this.project.demuxInput(buffer, 0, 1) == -1 ? -1 : buffer[0];
   }

   public int read(byte[] buffer, int offset, int length) throws IOException {
      return this.project.demuxInput(buffer, offset, length);
   }
}
