package org.apache.maven.scm.provider.hg.repository;

import java.io.File;
import org.apache.maven.scm.provider.ScmProviderRepositoryWithHost;
import org.codehaus.plexus.util.StringUtils;

public class HgScmProviderRepository extends ScmProviderRepositoryWithHost {
   private static final String FILE = "";
   private static final String SFTP = "sftp://";
   private static final String FTP = "ftp://";
   private static final String AFTP = "aftp://";
   private static final String HTTP = "http://";
   private static final String HTTPS = "https://";
   private final String path;
   private final String protocol;
   private final String orgUrl;

   public HgScmProviderRepository(String url) {
      this.orgUrl = url;
      this.protocol = this.getProtocol(url);
      this.path = this.parseUrl(url);
   }

   public String getURI() {
      return this.protocol + this.addAuthority() + this.addHost() + this.addPort() + this.addPath();
   }

   public String validateURI() {
      String msg = null;
      if (this.needsAuthentication()) {
         if (this.getUser() == null) {
            msg = "Username is missing for protocol " + this.protocol;
         } else if (this.getPassword() == null) {
            msg = "Password is missing for protocol " + this.protocol;
         } else if (this.getHost() == null) {
            msg = "Host (eg. www.myhost.com) is missing for protocol " + this.protocol;
         }
      } else if (this.getPort() != 0 && this.getHost() == null) {
         msg = "Got port information without any host for protocol " + this.protocol;
      }

      if (msg != null) {
         msg = "Something could be wrong about the repository URL: " + this.orgUrl + "\nReason: " + msg + "\nCheck http://maven.apache.org/scm for usage and hints.";
      }

      return msg;
   }

   private String getProtocol(String url) {
      String prot = "";
      if (url.startsWith("sftp://")) {
         prot = "sftp://";
      } else if (url.startsWith("http://")) {
         prot = "http://";
      } else if (url.startsWith("https://")) {
         prot = "https://";
      }

      return prot;
   }

   private String parseUrl(String url) {
      if (this.protocol == "") {
         return url;
      } else {
         url = url.substring(this.protocol.length());
         url = this.parseUsernameAndPassword(url);
         url = this.parseHostAndPort(url);
         url = this.parsePath(url);
         return url;
      }
   }

   private String parseHostAndPort(String url) {
      if (this.protocol != "") {
         int indexSlash = url.indexOf(47);
         String hostPort = url;
         if (indexSlash > 0) {
            hostPort = url.substring(0, indexSlash);
         }

         int indexColon = hostPort.indexOf(58);
         if (indexColon > 0) {
            this.setHost(hostPort.substring(0, indexColon));
            url = StringUtils.replace(url, this.getHost(), "");
            this.setPort(Integer.parseInt(hostPort.substring(indexColon + 1)));
            url = StringUtils.replace(url, ":" + this.getPort(), "");
         } else {
            this.setHost(hostPort);
            url = StringUtils.replace(url, this.getHost(), "");
         }
      }

      return url;
   }

   private String parseUsernameAndPassword(String url) {
      if (this.canAuthenticate()) {
         String[] split = url.split("@");
         if (split.length == 2) {
            url = split[1];
            split = split[0].split(":");
            if (split.length == 2) {
               this.setUser(split[0]);
               this.setPassword(split[1]);
            } else {
               this.setUser(split[0]);
            }
         }
      }

      return url;
   }

   private String parsePath(String url) {
      if (this.protocol == "") {
         url = StringUtils.replace(url, "/", File.separator);
         File tmpFile = new File(url);
         String url2 = url.substring(File.pathSeparator.length());
         File tmpFile2 = new File(url2);
         if (!tmpFile.exists() && !tmpFile2.exists()) {
         }

         url = tmpFile2.exists() ? url2 : url;
      }

      return url;
   }

   private String addUser() {
      return this.getUser() == null ? "" : this.getUser();
   }

   private String addPassword() {
      return this.getPassword() == null ? "" : ":" + this.getPassword();
   }

   private String addHost() {
      return this.getHost() == null ? "" : this.getHost();
   }

   private String addPort() {
      return this.getPort() == 0 ? "" : ":" + this.getPort();
   }

   private String addPath() {
      return this.path;
   }

   private boolean needsAuthentication() {
      return this.protocol == "sftp://" || this.protocol == "ftp://" || this.protocol == "https://" || this.protocol == "aftp://";
   }

   private String addAuthority() {
      return this.canAuthenticate() && this.getUser() != null ? this.addUser() + this.addPassword() + "@" : "";
   }

   private boolean canAuthenticate() {
      return this.needsAuthentication() || this.protocol == "http://";
   }

   public String toString() {
      return "Hg Repository Interpreted from: " + this.orgUrl + ":\nProtocol: " + this.protocol + "\nHost: " + this.getHost() + "\nPort: " + this.getPort() + "\nUsername: " + this.getUser() + "\nPassword: " + this.getPassword() + "\nPath: " + this.path;
   }
}
