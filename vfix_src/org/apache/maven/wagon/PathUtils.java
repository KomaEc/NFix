package org.apache.maven.wagon;

import java.io.File;
import java.util.StringTokenizer;

public final class PathUtils {
   private PathUtils() {
   }

   public static String dirname(String path) {
      int i = path.lastIndexOf("/");
      return i >= 0 ? path.substring(0, i) : "";
   }

   public static String filename(String path) {
      int i = path.lastIndexOf("/");
      return i >= 0 ? path.substring(i + 1) : path;
   }

   public static String[] dirnames(String path) {
      String dirname = dirname(path);
      return split(dirname, "/", -1);
   }

   private static String[] split(String str, String separator, int max) {
      StringTokenizer tok;
      if (separator == null) {
         tok = new StringTokenizer(str);
      } else {
         tok = new StringTokenizer(str, separator);
      }

      int listSize = tok.countTokens();
      if (max > 0 && listSize > max) {
         listSize = max;
      }

      String[] list = new String[listSize];
      int i = 0;

      for(int lastTokenEnd = 0; tok.hasMoreTokens(); ++i) {
         int lastTokenBegin;
         if (max > 0 && i == listSize - 1) {
            String endToken = tok.nextToken();
            lastTokenBegin = str.indexOf(endToken, lastTokenEnd);
            list[i] = str.substring(lastTokenBegin);
            break;
         }

         list[i] = tok.nextToken();
         lastTokenBegin = str.indexOf(list[i], lastTokenEnd);
         lastTokenEnd = lastTokenBegin + list[i].length();
      }

      return list;
   }

   public static String host(String url) {
      String authorization = authorization(url);
      int index = authorization.indexOf(64);
      return index >= 0 ? authorization.substring(index + 1) : authorization;
   }

   private static String authorization(String url) {
      if (url == null) {
         return "localhost";
      } else {
         String protocol = protocol(url);
         if (protocol != null && !protocol.equalsIgnoreCase("file")) {
            String host = url;
            if (protocol.equalsIgnoreCase("scm")) {
               host = url.substring(url.indexOf(":", 4) + 1).trim();
            }

            host = host.substring(host.indexOf(":") + 1).trim();
            if (host.startsWith("//")) {
               host = host.substring(2);
            }

            int pos = host.indexOf("/");
            if (pos > 0) {
               host = host.substring(0, pos);
            }

            pos = host.indexOf(64);
            if (pos > 0) {
               pos = host.indexOf(58, pos);
            } else {
               pos = host.indexOf(":");
            }

            if (pos > 0) {
               host = host.substring(0, pos);
            }

            return host;
         } else {
            return "localhost";
         }
      }
   }

   public static String protocol(String url) {
      int pos = url.indexOf(":");
      return pos == -1 ? "" : url.substring(0, pos).trim();
   }

   public static int port(String url) {
      String protocol = protocol(url);
      if (protocol != null && !protocol.equalsIgnoreCase("file")) {
         String authorization = authorization(url);
         if (authorization == null) {
            return -1;
         } else {
            if (protocol.equalsIgnoreCase("scm")) {
               url = url.substring(url.indexOf(":", 4) + 1).trim();
            }

            if (!url.regionMatches(true, 0, "file:", 0, 5) && !url.regionMatches(true, 0, "local:", 0, 6)) {
               url = url.substring(url.indexOf(":") + 1).trim();
               if (url.startsWith("//")) {
                  url = url.substring(2);
               }

               int start = authorization.length();
               if (url.length() > start && url.charAt(start) == ':') {
                  int end = url.indexOf(47, start);
                  if (end == start + 1) {
                     return -1;
                  } else {
                     if (end == -1) {
                        end = url.length();
                     }

                     return Integer.parseInt(url.substring(start + 1, end));
                  }
               } else {
                  return -1;
               }
            } else {
               return -1;
            }
         }
      } else {
         return -1;
      }
   }

   public static String basedir(String url) {
      String protocol = protocol(url);
      String retValue = null;
      if (protocol.equalsIgnoreCase("scm") && url.regionMatches(true, 0, "scm:svn:", 0, 8)) {
         url = url.substring(url.indexOf(":", 4) + 1);
         protocol = protocol(url);
      }

      if (protocol.equalsIgnoreCase("file")) {
         retValue = url.substring(protocol.length() + 1);
         retValue = decode(retValue);
         if (retValue.startsWith("//")) {
            retValue = retValue.substring(2);
            if (retValue.length() < 2 || retValue.charAt(1) != '|' && retValue.charAt(1) != ':') {
               int index = retValue.indexOf("/");
               if (index >= 0) {
                  retValue = retValue.substring(index + 1);
               }

               if (retValue.length() < 2 || retValue.charAt(1) != '|' && retValue.charAt(1) != ':') {
                  if (index >= 0) {
                     retValue = "/" + retValue;
                  }
               } else {
                  retValue = retValue.charAt(0) + ":" + retValue.substring(2);
               }
            } else {
               retValue = retValue.charAt(0) + ":" + retValue.substring(2);
            }
         }

         if (retValue.length() >= 2 && retValue.charAt(1) == '|') {
            retValue = retValue.charAt(0) + ":" + retValue.substring(2);
         }
      } else {
         String authorization = authorization(url);
         int port = port(url);
         int pos;
         if (protocol.equalsIgnoreCase("scm")) {
            pos = url.indexOf(":", 4) + 1;
            pos = url.indexOf(":", pos) + 1;
         } else {
            pos = url.indexOf("://") + 3;
         }

         pos += authorization.length();
         if (port != -1) {
            pos = pos + Integer.toString(port).length() + 1;
         }

         if (url.length() > pos) {
            retValue = url.substring(pos);
            if (retValue.startsWith(":")) {
               retValue = retValue.substring(1);
            }

            retValue = retValue.replace(':', '/');
         }
      }

      if (retValue == null) {
         retValue = "/";
      }

      return retValue.trim();
   }

   private static String decode(String url) {
      String decoded = url;
      if (url != null) {
         int pos = -1;

         while((pos = decoded.indexOf(37, pos + 1)) >= 0) {
            if (pos + 2 < decoded.length()) {
               String hexStr = decoded.substring(pos + 1, pos + 3);
               char ch = (char)Integer.parseInt(hexStr, 16);
               decoded = decoded.substring(0, pos) + ch + decoded.substring(pos + 3);
            }
         }
      }

      return decoded;
   }

   public static String user(String url) {
      String host = authorization(url);
      int index = host.indexOf(64);
      if (index > 0) {
         String userInfo = host.substring(0, index);
         index = userInfo.indexOf(58);
         if (index > 0) {
            return userInfo.substring(0, index);
         }

         if (index < 0) {
            return userInfo;
         }
      }

      return null;
   }

   public static String password(String url) {
      String host = authorization(url);
      int index = host.indexOf(64);
      if (index > 0) {
         String userInfo = host.substring(0, index);
         index = userInfo.indexOf(58);
         if (index >= 0) {
            return userInfo.substring(index + 1);
         }
      }

      return null;
   }

   public static String toRelative(File basedir, String absolutePath) {
      absolutePath = absolutePath.replace('\\', '/');
      String basedirPath = basedir.getAbsolutePath().replace('\\', '/');
      String relative;
      if (absolutePath.startsWith(basedirPath)) {
         relative = absolutePath.substring(basedirPath.length());
         if (relative.startsWith("/")) {
            relative = relative.substring(1);
         }

         if (relative.length() <= 0) {
            relative = ".";
         }
      } else {
         relative = absolutePath;
      }

      return relative;
   }
}
