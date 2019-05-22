package org.apache.maven.scm.provider.svn;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;

public class SvnChangeSet extends ChangeSet {
   private static final long serialVersionUID = -4454710577968060741L;

   public SvnChangeSet() {
   }

   public SvnChangeSet(String strDate, String userDatePattern, String comment, String author, List<ChangeFile> files) {
      super(strDate, userDatePattern, comment, author, files);
   }

   public SvnChangeSet(Date date, String comment, String author, List<ChangeFile> files) {
      super(date, comment, author, files);
   }

   public boolean containsFilename(String filename, ScmProviderRepository repository) {
      SvnScmProviderRepository repo = (SvnScmProviderRepository)repository;
      String url = repo.getUrl();
      if (!url.endsWith("/")) {
         url = url + "/";
      }

      String currentFile = url + StringUtils.replace(filename, "\\", "/");
      if (this.getFiles() != null) {
         Iterator i = this.getFiles().iterator();

         while(i.hasNext()) {
            ChangeFile file = (ChangeFile)i.next();
            if (currentFile.endsWith(StringUtils.replace(file.getName(), "\\", "/"))) {
               return true;
            }
         }
      }

      return false;
   }
}
