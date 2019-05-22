package org.apache.maven.scm.provider.clearcase.repository;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.providers.clearcase.settings.Settings;
import org.apache.maven.scm.repository.ScmRepositoryException;

public class ClearCaseScmProviderRepository extends ScmProviderRepository {
   private ScmLogger logger;
   private boolean viewNameGivenByUser = false;
   private String viewName;
   private File configSpec;
   private String loadDirectory;
   private String streamName;
   private String vobName;
   private Settings settings;
   private String elementName;
   public static final String CLEARCASE_LT = "LT";
   public static final String CLEARCASE_UCM = "UCM";
   public static final String CLEARCASE_DEFAULT = null;

   public ClearCaseScmProviderRepository(ScmLogger logger, String url, Settings settings) throws ScmRepositoryException {
      this.logger = logger;
      this.settings = settings;

      try {
         this.parseUrl(url);
      } catch (MalformedURLException var5) {
         throw new ScmRepositoryException("Illegal URL: " + url + "(" + var5.getMessage() + ")");
      } catch (URISyntaxException var6) {
         throw new ScmRepositoryException("Illegal URL: " + url + "(" + var6.getMessage() + ")");
      } catch (UnknownHostException var7) {
         throw new ScmRepositoryException("Illegal URL: " + url + "(" + var7.getMessage() + ")");
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
      String configSpecString = null;
      if ("UCM".equals(this.settings.getClearcaseType())) {
         configSpecString = this.fillUCMProperties(tokenizer);
      } else {
         configSpecString = this.fillDefaultProperties(tokenizer);
      }

      if (!configSpecString.startsWith("load ")) {
         this.configSpec = this.createConfigSpecFile(configSpecString);
         this.loadDirectory = null;
      } else {
         this.configSpec = null;
         this.loadDirectory = configSpecString.substring(5);
      }

   }

   private String fillDefaultProperties(StringTokenizer tokenizer) throws UnknownHostException {
      int tokenNumber = tokenizer.countTokens();
      String configSpecString;
      if (tokenNumber == 1) {
         this.viewName = this.getDefaultViewName();
         configSpecString = tokenizer.nextToken();
      } else {
         configSpecString = this.checkViewName(tokenizer);
         this.checkUnexpectedParameter(tokenizer, tokenNumber, 2);
      }

      if (this.logger.isDebugEnabled()) {
         this.logger.debug("viewName = '" + this.viewName + "' ; configSpec = '" + configSpecString + "'");
      }

      return configSpecString;
   }

   private String fillUCMProperties(StringTokenizer tokenizer) throws UnknownHostException, MalformedURLException {
      int tokenNumber = tokenizer.countTokens();
      if (tokenNumber <= 2) {
         throw new MalformedURLException("ClearCaseUCM need more parameters. Expected url format : [view_name]|[configspec]|[vob_name]|[stream_name]");
      } else {
         String configSpecString;
         if (tokenNumber == 3) {
            this.viewName = this.getDefaultViewName();
            configSpecString = tokenizer.nextToken();
            this.vobName = tokenizer.nextToken();
            this.streamName = tokenizer.nextToken();
         } else if (tokenNumber == 4) {
            String[] tokens = new String[]{tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken()};
            if (tokens[3].startsWith("/main/")) {
               this.viewName = this.getDefaultViewName();
               configSpecString = tokens[0];
               this.vobName = tokens[1];
               this.streamName = tokens[2];
               this.elementName = tokens[3];
            } else {
               this.viewName = tokens[0];
               this.viewNameGivenByUser = true;
               configSpecString = tokens[1];
               this.vobName = tokens[2];
               this.streamName = tokens[3];
            }
         } else {
            configSpecString = this.checkViewName(tokenizer);
            this.vobName = tokenizer.nextToken();
            this.streamName = tokenizer.nextToken();
            this.elementName = tokenizer.nextToken();
            this.checkUnexpectedParameter(tokenizer, tokenNumber, 5);
         }

         if (this.logger.isInfoEnabled()) {
            this.logger.info("viewName = '" + this.viewName + "' ; configSpec = '" + configSpecString + "' ; vobName = '" + this.vobName + "' ; streamName = '" + this.streamName + "' ; elementName = '" + this.elementName + "'");
         }

         return configSpecString;
      }
   }

   private String checkViewName(StringTokenizer tokenizer) throws UnknownHostException {
      this.viewName = tokenizer.nextToken();
      if (this.viewName.length() > 0) {
         this.viewNameGivenByUser = true;
      } else {
         this.viewName = this.getDefaultViewName();
      }

      return tokenizer.nextToken();
   }

   private void checkUnexpectedParameter(StringTokenizer tokenizer, int tokenNumber, int maxTokenNumber) {
      if (tokenNumber > maxTokenNumber) {
         String unexpectedToken = tokenizer.nextToken();
         if (this.logger.isInfoEnabled()) {
            this.logger.info("The SCM URL contains unused parameter : " + unexpectedToken);
         }
      }

   }

   private File createConfigSpecFile(String spec) throws URISyntaxException, MalformedURLException {
      File result;
      if (spec.indexOf(58) == -1) {
         result = new File(spec);
      } else {
         result = new File(new URI((new URL(spec)).toString()));
      }

      return result;
   }

   private String getDefaultViewName() throws UnknownHostException {
      String username = System.getProperty("user.name", "nouser");
      String hostname = this.getHostName();
      return username + "-" + hostname + "-maven";
   }

   private String getHostName() throws UnknownHostException {
      return InetAddress.getLocalHost().getHostName();
   }

   public String getViewName(String uniqueId) {
      String result;
      if (this.viewNameGivenByUser) {
         result = this.viewName;
      } else {
         result = this.viewName + "-" + uniqueId;
      }

      return result;
   }

   public File getConfigSpec() {
      return this.configSpec;
   }

   public boolean isAutoConfigSpec() {
      return this.configSpec == null;
   }

   public String getLoadDirectory() {
      return this.loadDirectory;
   }

   public String getStreamName() {
      return this.streamName;
   }

   public String getVobName() {
      return this.vobName;
   }

   public String getElementName() {
      return this.elementName;
   }

   public boolean hasElements() {
      return this.elementName != null;
   }
}
