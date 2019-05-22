package org.apache.tools.ant.util;

import java.io.File;
import java.util.Vector;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceFactory;
import org.apache.tools.ant.types.resources.FileResource;

public class SourceFileScanner implements ResourceFactory {
   protected Task task;
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private File destDir;

   public SourceFileScanner(Task task) {
      this.task = task;
   }

   public String[] restrict(String[] files, File srcDir, File destDir, FileNameMapper mapper) {
      return this.restrict(files, srcDir, destDir, mapper, FILE_UTILS.getFileTimestampGranularity());
   }

   public String[] restrict(String[] files, File srcDir, File destDir, FileNameMapper mapper, long granularity) {
      this.destDir = destDir;
      Vector v = new Vector();

      for(int i = 0; i < files.length; ++i) {
         File src = FILE_UTILS.resolveFile(srcDir, files[i]);
         v.addElement(new Resource(files[i], src.exists(), src.lastModified(), src.isDirectory()));
      }

      Resource[] sourceresources = new Resource[v.size()];
      v.copyInto(sourceresources);
      Resource[] outofdate = ResourceUtils.selectOutOfDateSources(this.task, (Resource[])sourceresources, mapper, this, granularity);
      String[] result = new String[outofdate.length];

      for(int counter = 0; counter < outofdate.length; ++counter) {
         result[counter] = outofdate[counter].getName();
      }

      return result;
   }

   public File[] restrictAsFiles(String[] files, File srcDir, File destDir, FileNameMapper mapper) {
      return this.restrictAsFiles(files, srcDir, destDir, mapper, FILE_UTILS.getFileTimestampGranularity());
   }

   public File[] restrictAsFiles(String[] files, File srcDir, File destDir, FileNameMapper mapper, long granularity) {
      String[] res = this.restrict(files, srcDir, destDir, mapper, granularity);
      File[] result = new File[res.length];

      for(int i = 0; i < res.length; ++i) {
         result[i] = new File(srcDir, res[i]);
      }

      return result;
   }

   public Resource getResource(String name) {
      return new FileResource(this.destDir, name);
   }
}
