package org.apache.tools.ant.types.resources.comparators;

import java.io.File;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileUtils;

public class FileSystem extends ResourceComparator {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

   protected int resourceCompare(Resource foo, Resource bar) {
      File foofile = ((FileResource)foo).getFile();
      File barfile = ((FileResource)bar).getFile();
      return foofile.equals(barfile) ? 0 : (FILE_UTILS.isLeadingPath(foofile, barfile) ? -1 : FILE_UTILS.normalize(foofile.getAbsolutePath()).compareTo(FILE_UTILS.normalize(barfile.getAbsolutePath())));
   }
}
