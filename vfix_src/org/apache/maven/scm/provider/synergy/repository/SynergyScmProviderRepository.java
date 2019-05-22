package org.apache.maven.scm.provider.synergy.repository;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;

public class SynergyScmProviderRepository extends ScmProviderRepository {
   private String projectSpec;
   private String projectName;
   private String projectVersion;
   private String projectRelease;
   private String projectPurpose;
   private String delimiter;
   private String instance;

   public SynergyScmProviderRepository(String url) throws ScmRepositoryException {
      try {
         this.parseUrl(url);
      } catch (MalformedURLException var3) {
         throw new ScmRepositoryException("Illegal URL: " + url + "(" + var3.getMessage() + ")");
      } catch (URISyntaxException var4) {
         throw new ScmRepositoryException("Illegal URL: " + url + "(" + var4.getMessage() + ")");
      } catch (UnknownHostException var5) {
         throw new ScmRepositoryException("Illegal URL: " + url + "(" + var5.getMessage() + ")");
      }
   }

   private void parseUrl(String url) throws MalformedURLException, URISyntaxException, UnknownHostException {
      StringTokenizer tokenizer;
      if (url.indexOf(124) != -1) {
         tokenizer = new StringTokenizer(url, "|");
         this.fillInProperties(tokenizer);
      } else {
         tokenizer = new StringTokenizer(url, ":");
         this.fillInProperties(tokenizer);
      }

   }

   private void fillInProperties(StringTokenizer tokenizer) throws UnknownHostException, URISyntaxException, MalformedURLException {
      if (tokenizer.countTokens() == 5) {
         this.projectName = tokenizer.nextToken();
         this.delimiter = tokenizer.nextToken();
         this.projectVersion = tokenizer.nextToken();
         this.projectRelease = tokenizer.nextToken();
         this.projectPurpose = tokenizer.nextToken();
         this.instance = "1";
         this.projectSpec = this.projectName + this.delimiter + this.projectVersion + ":project:" + this.instance;
      } else {
         if (tokenizer.countTokens() != 6) {
            throw new MalformedURLException();
         }

         this.projectName = tokenizer.nextToken();
         this.delimiter = tokenizer.nextToken();
         this.projectVersion = tokenizer.nextToken();
         this.projectRelease = tokenizer.nextToken();
         this.projectPurpose = tokenizer.nextToken();
         this.instance = tokenizer.nextToken();
         this.projectSpec = this.projectName + this.delimiter + this.projectVersion + ":project:" + this.instance;
      }

   }

   public String getProjectSpec() {
      return this.projectSpec;
   }

   public String getProjectName() {
      return this.projectName;
   }

   public String getProjectVersion() {
      return this.projectVersion;
   }

   public String getProjectPurpose() {
      return this.projectPurpose;
   }

   public String getProjectRelease() {
      return this.projectRelease;
   }

   public String getInstance() {
      return this.instance;
   }
}
