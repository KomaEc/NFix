package com.gzoltar.shaded.org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class CanReadFileFilter extends AbstractFileFilter implements Serializable {
   public static final IOFileFilter CAN_READ = new CanReadFileFilter();
   public static final IOFileFilter CANNOT_READ;
   public static final IOFileFilter READ_ONLY;

   protected CanReadFileFilter() {
   }

   public boolean accept(File file) {
      return file.canRead();
   }

   static {
      CANNOT_READ = new NotFileFilter(CAN_READ);
      READ_ONLY = new AndFileFilter(CAN_READ, CanWriteFileFilter.CANNOT_WRITE);
   }
}
