package org.apache.maven.scm.provider.tfs;

import org.apache.maven.scm.provider.ScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;

public class TfsScmProviderRepository extends ScmProviderRepository {
   private final String tfsUrl;
   private final String serverPath;
   private final String workspace;
   private final boolean useCheckinPolicies;

   public TfsScmProviderRepository(String tfsUrl, String user, String password, String serverPath, String workspace, boolean useCheckinPolicies) {
      this.setUser(user);
      this.setPassword(password);
      this.tfsUrl = tfsUrl;
      this.serverPath = serverPath;
      this.workspace = workspace;
      this.useCheckinPolicies = useCheckinPolicies;
   }

   public String getTfsUrl() {
      return this.tfsUrl;
   }

   public String getWorkspace() {
      return this.workspace;
   }

   public String getServerPath() {
      return this.serverPath;
   }

   public String getUserPassword() {
      String userPassword = null;
      if (!StringUtils.isEmpty(this.getUser())) {
         userPassword = this.getUser();
         if (!StringUtils.isEmpty(this.getPassword())) {
            userPassword = userPassword + ";" + this.getPassword();
         }
      }

      return userPassword;
   }

   public boolean isUseCheckinPolicies() {
      return this.useCheckinPolicies;
   }
}
