package org.codehaus.groovy.ant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

public class FileScanner extends Task {
   private List filesets = new ArrayList();

   public FileScanner() {
   }

   public FileScanner(Project project) {
      this.setProject(project);
   }

   public Iterator iterator() {
      return new FileIterator(this.getProject(), this.filesets.iterator());
   }

   public Iterator directories() {
      return new FileIterator(this.getProject(), this.filesets.iterator(), true);
   }

   public boolean hasFiles() {
      return this.filesets.size() > 0;
   }

   public void clear() {
      this.filesets.clear();
   }

   public void addFileset(FileSet set) {
      this.filesets.add(set);
   }
}
