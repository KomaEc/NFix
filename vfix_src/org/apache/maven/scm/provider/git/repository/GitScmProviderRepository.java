package org.apache.maven.scm.provider.git.repository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.ScmProviderRepositoryWithHost;

public class GitScmProviderRepository extends ScmProviderRepositoryWithHost {
   public static final String URL_DELIMITER_FETCH = "[fetch=]";
   public static final String URL_DELIMITER_PUSH = "[push=]";
   public static final String PROTOCOL_SEPARATOR = "://";
   public static final String PROTOCOL_FILE = "file";
   public static final String PROTOCOL_GIT = "git";
   public static final String PROTOCOL_SSH = "ssh";
   public static final String PROTOCOL_HTTP = "http";
   public static final String PROTOCOL_HTTPS = "https";
   public static final String PROTOCOL_RSYNC = "rsync";
   private static final Pattern HOST_AND_PORT_EXTRACTOR = Pattern.compile("([^:/\\\\~]*)(?::(\\d*))?(?:([:/\\\\~])(.*))?");
   public static final String PROTOCOL_NONE = "";
   private String provider;
   private RepositoryUrl fetchInfo;
   private RepositoryUrl pushInfo;

   public GitScmProviderRepository(String url) throws ScmException {
      if (url == null) {
         throw new ScmException("url must not be null");
      } else {
         String push;
         int indexFetchDelimiter;
         String fetch;
         if (url.startsWith("[fetch=]")) {
            push = url.substring("[fetch=]".length());
            indexFetchDelimiter = push.indexOf("[push=]");
            if (indexFetchDelimiter >= 0) {
               fetch = push.substring(indexFetchDelimiter + "[push=]".length());
               this.pushInfo = this.parseUrl(fetch);
               push = push.substring(0, indexFetchDelimiter);
            }

            this.fetchInfo = this.parseUrl(push);
            if (this.pushInfo == null) {
               this.pushInfo = this.fetchInfo;
            }
         } else if (url.startsWith("[push=]")) {
            push = url.substring("[push=]".length());
            indexFetchDelimiter = push.indexOf("[fetch=]");
            if (indexFetchDelimiter >= 0) {
               fetch = push.substring(indexFetchDelimiter + "[fetch=]".length());
               this.fetchInfo = this.parseUrl(fetch);
               push = push.substring(0, indexFetchDelimiter);
            }

            this.pushInfo = this.parseUrl(push);
            if (this.fetchInfo == null) {
               this.fetchInfo = this.pushInfo;
            }
         } else {
            this.fetchInfo = this.pushInfo = this.parseUrl(url);
         }

         this.setUser(this.pushInfo.getUserName());
         this.setPassword(this.pushInfo.getPassword());
         this.setHost(this.pushInfo.getHost());
         if (this.pushInfo.getPort() != null && this.pushInfo.getPort().length() > 0) {
            this.setPort(Integer.parseInt(this.pushInfo.getPort()));
         }

      }
   }

   public GitScmProviderRepository(String url, String user, String password) throws ScmException {
      this(url);
      this.setUser(user);
      this.setPassword(password);
   }

   public String getProvider() {
      return this.provider;
   }

   public RepositoryUrl getFetchInfo() {
      return this.fetchInfo;
   }

   public RepositoryUrl getPushInfo() {
      return this.pushInfo;
   }

   public String getFetchUrl() {
      return this.getUrl(this.fetchInfo);
   }

   public String getPushUrl() {
      return this.getUrl(this.pushInfo);
   }

   private RepositoryUrl parseUrl(String url) throws ScmException {
      RepositoryUrl repoUrl = new RepositoryUrl();
      url = this.parseProtocol(repoUrl, url);
      url = this.parseUserInfo(repoUrl, url);
      url = this.parseHostAndPort(repoUrl, url);
      repoUrl.setPath(url);
      return repoUrl;
   }

   private String getUrl(RepositoryUrl repoUrl) {
      StringBuilder urlSb = new StringBuilder(repoUrl.getProtocol());
      boolean urlSupportsUserInformation = false;
      if ("ssh".equals(repoUrl.getProtocol()) || "rsync".equals(repoUrl.getProtocol()) || "git".equals(repoUrl.getProtocol()) || "http".equals(repoUrl.getProtocol()) || "https".equals(repoUrl.getProtocol()) || "".equals(repoUrl.getProtocol())) {
         urlSupportsUserInformation = true;
      }

      if (repoUrl.getProtocol() != null && repoUrl.getProtocol().length() > 0) {
         urlSb.append("://");
      }

      if (urlSupportsUserInformation) {
         String userName = repoUrl.getUserName();
         if (this.getUser() != null && this.getUser().length() > 0) {
            userName = this.getUser();
         }

         String password = repoUrl.getPassword();
         if (this.getPassword() != null && this.getPassword().length() > 0) {
            password = this.getPassword();
         }

         if (userName != null && userName.length() > 0) {
            try {
               urlSb.append(URLEncoder.encode(userName, "UTF-8"));
            } catch (UnsupportedEncodingException var8) {
               var8.printStackTrace();
            }

            if (password != null && password.length() > 0) {
               urlSb.append(':');

               try {
                  urlSb.append(URLEncoder.encode(password, "UTF-8"));
               } catch (UnsupportedEncodingException var7) {
                  var7.printStackTrace();
               }
            }

            urlSb.append('@');
         }
      }

      urlSb.append(repoUrl.getHost());
      if (repoUrl.getPort() != null && repoUrl.getPort().length() > 0) {
         urlSb.append(':').append(repoUrl.getPort());
      }

      urlSb.append(repoUrl.getPath());
      return urlSb.toString();
   }

   private String parseProtocol(RepositoryUrl repoUrl, String url) throws ScmException {
      if (url.startsWith("file://")) {
         repoUrl.setProtocol("file");
      } else if (url.startsWith("https://")) {
         repoUrl.setProtocol("https");
      } else if (url.startsWith("http://")) {
         repoUrl.setProtocol("http");
      } else if (url.startsWith("ssh://")) {
         repoUrl.setProtocol("ssh");
      } else if (url.startsWith("git://")) {
         repoUrl.setProtocol("git");
      } else {
         if (!url.startsWith("rsync://")) {
            repoUrl.setProtocol("");
            return url;
         }

         repoUrl.setProtocol("rsync");
      }

      url = url.substring(repoUrl.getProtocol().length() + 3);
      return url;
   }

   private String parseUserInfo(RepositoryUrl repoUrl, String url) throws ScmException {
      int indexAt = url.indexOf(64);
      if (indexAt >= 0) {
         String userInfo = url.substring(0, indexAt);
         int indexPwdSep = userInfo.indexOf(58);
         if (indexPwdSep < 0) {
            repoUrl.setUserName(userInfo);
         } else {
            repoUrl.setUserName(userInfo.substring(0, indexPwdSep));
            repoUrl.setPassword(userInfo.substring(indexPwdSep + 1));
         }

         url = url.substring(indexAt + 1);
      }

      return url;
   }

   private String parseHostAndPort(RepositoryUrl repoUrl, String url) throws ScmException {
      repoUrl.setPort("");
      repoUrl.setHost("");
      if ("file".equals(repoUrl.getProtocol())) {
         return url;
      } else {
         Matcher hostAndPortMatcher = HOST_AND_PORT_EXTRACTOR.matcher(url);
         if (hostAndPortMatcher.matches()) {
            if (hostAndPortMatcher.groupCount() > 1 && hostAndPortMatcher.group(1) != null) {
               repoUrl.setHost(hostAndPortMatcher.group(1));
            }

            if (hostAndPortMatcher.groupCount() > 2 && hostAndPortMatcher.group(2) != null) {
               repoUrl.setPort(hostAndPortMatcher.group(2));
            }

            StringBuilder computedUrl = new StringBuilder();
            if (hostAndPortMatcher.group(hostAndPortMatcher.groupCount() - 1) != null) {
               computedUrl.append(hostAndPortMatcher.group(hostAndPortMatcher.groupCount() - 1));
            }

            if (hostAndPortMatcher.group(hostAndPortMatcher.groupCount()) != null) {
               computedUrl.append(hostAndPortMatcher.group(hostAndPortMatcher.groupCount()));
            }

            return computedUrl.toString();
         } else {
            return url;
         }
      }
   }

   public String getRelativePath(ScmProviderRepository ancestor) {
      if (ancestor instanceof GitScmProviderRepository) {
         GitScmProviderRepository gitAncestor = (GitScmProviderRepository)ancestor;
         String url = this.getFetchUrl();
         String path = url.replaceFirst(gitAncestor.getFetchUrl() + "/", "");
         if (!path.equals(url)) {
            return path;
         }
      }

      return null;
   }

   public String toString() {
      return this.fetchInfo == this.pushInfo ? this.getUrl(this.fetchInfo) : "[fetch=]" + this.getUrl(this.fetchInfo) + "[push=]" + this.getUrl(this.pushInfo);
   }
}
