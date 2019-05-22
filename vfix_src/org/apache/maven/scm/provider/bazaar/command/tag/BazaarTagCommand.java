package org.apache.maven.scm.provider.bazaar.command.tag;

import java.io.File;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.tag.AbstractTagCommand;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.bazaar.BazaarUtils;
import org.apache.maven.scm.provider.bazaar.command.BazaarConsumer;
import org.apache.maven.scm.provider.bazaar.repository.BazaarScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;

public class BazaarTagCommand extends AbstractTagCommand {
   protected ScmResult executeTagCommand(ScmProviderRepository repository, ScmFileSet fileSet, String tagName, ScmTagParameters scmTagParameters) throws ScmException {
      if (tagName != null && !StringUtils.isEmpty(tagName.trim())) {
         if (!fileSet.getFileList().isEmpty()) {
            throw new ScmException("tagging specific files is not allowed");
         } else {
            File bazaarRoot = fileSet.getBasedir();
            BazaarConsumer consumer = new BazaarConsumer(this.getLogger());
            String[] tagCmd = new String[]{"tag", tagName};
            ScmResult tagResult = BazaarUtils.execute(consumer, this.getLogger(), bazaarRoot, tagCmd);
            if (!tagResult.isSuccess()) {
               return new TagScmResult((List)null, tagResult);
            } else {
               BazaarLsConsumer lsConsumer = new BazaarLsConsumer(this.getLogger(), bazaarRoot, ScmFileStatus.TAGGED);
               String[] lsCmd = new String[]{"ls", "--recursive", "--revision", "tag:" + tagName};
               ScmResult lsResult = BazaarUtils.execute(lsConsumer, this.getLogger(), bazaarRoot, lsCmd);
               if (!lsResult.isSuccess()) {
                  return new TagScmResult((List)null, lsResult);
               } else {
                  BazaarScmProviderRepository bazaarRepository = (BazaarScmProviderRepository)repository;
                  if (!bazaarRepository.getURI().equals(fileSet.getBasedir().getAbsolutePath()) && repository.isPushChanges()) {
                     String[] pushCmd = new String[]{"push", bazaarRepository.getURI()};
                     ScmResult pushResult = BazaarUtils.execute(new BazaarConsumer(this.getLogger()), this.getLogger(), fileSet.getBasedir(), pushCmd);
                     if (!pushResult.isSuccess()) {
                        return new TagScmResult((List)null, pushResult);
                     }
                  }

                  return new TagScmResult(lsConsumer.getListedFiles(), tagResult);
               }
            }
         }
      } else {
         throw new ScmException("tag name must be specified");
      }
   }
}
