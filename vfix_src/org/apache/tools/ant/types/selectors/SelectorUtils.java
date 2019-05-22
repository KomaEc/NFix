package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.FileUtils;

public final class SelectorUtils {
   private static SelectorUtils instance = new SelectorUtils();
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

   private SelectorUtils() {
   }

   public static SelectorUtils getInstance() {
      return instance;
   }

   public static boolean matchPatternStart(String pattern, String str) {
      return matchPatternStart(pattern, str, true);
   }

   public static boolean matchPatternStart(String pattern, String str, boolean isCaseSensitive) {
      if (str.startsWith(File.separator) != pattern.startsWith(File.separator)) {
         return false;
      } else {
         String[] patDirs = tokenizePathAsArray(pattern);
         String[] strDirs = tokenizePathAsArray(str);
         int patIdxStart = 0;
         int patIdxEnd = patDirs.length - 1;
         int strIdxStart = 0;

         int strIdxEnd;
         for(strIdxEnd = strDirs.length - 1; patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd; ++strIdxStart) {
            String patDir = patDirs[patIdxStart];
            if (patDir.equals("**")) {
               break;
            }

            if (!match(patDir, strDirs[strIdxStart], isCaseSensitive)) {
               return false;
            }

            ++patIdxStart;
         }

         if (strIdxStart > strIdxEnd) {
            return true;
         } else {
            return patIdxStart <= patIdxEnd;
         }
      }
   }

   public static boolean matchPath(String pattern, String str) {
      return matchPath(pattern, str, true);
   }

   public static boolean matchPath(String pattern, String str, boolean isCaseSensitive) {
      String[] patDirs = tokenizePathAsArray(pattern);
      String[] strDirs = tokenizePathAsArray(str);
      int patIdxStart = 0;
      int patIdxEnd = patDirs.length - 1;
      int strIdxStart = 0;

      int strIdxEnd;
      String patDir;
      for(strIdxEnd = strDirs.length - 1; patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd; ++strIdxStart) {
         patDir = patDirs[patIdxStart];
         if (patDir.equals("**")) {
            break;
         }

         if (!match(patDir, strDirs[strIdxStart], isCaseSensitive)) {
            patDirs = null;
            strDirs = null;
            return false;
         }

         ++patIdxStart;
      }

      int patIdxTmp;
      if (strIdxStart > strIdxEnd) {
         for(patIdxTmp = patIdxStart; patIdxTmp <= patIdxEnd; ++patIdxTmp) {
            if (!patDirs[patIdxTmp].equals("**")) {
               patDirs = null;
               strDirs = null;
               return false;
            }
         }

         return true;
      } else if (patIdxStart > patIdxEnd) {
         patDirs = null;
         strDirs = null;
         return false;
      } else {
         while(patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            patDir = patDirs[patIdxEnd];
            if (patDir.equals("**")) {
               break;
            }

            if (!match(patDir, strDirs[strIdxEnd], isCaseSensitive)) {
               patDirs = null;
               strDirs = null;
               return false;
            }

            --patIdxEnd;
            --strIdxEnd;
         }

         if (strIdxStart > strIdxEnd) {
            for(patIdxTmp = patIdxStart; patIdxTmp <= patIdxEnd; ++patIdxTmp) {
               if (!patDirs[patIdxTmp].equals("**")) {
                  patDirs = null;
                  strDirs = null;
                  return false;
               }
            }

            return true;
         } else {
            while(patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
               patIdxTmp = -1;

               int patLength;
               for(patLength = patIdxStart + 1; patLength <= patIdxEnd; ++patLength) {
                  if (patDirs[patLength].equals("**")) {
                     patIdxTmp = patLength;
                     break;
                  }
               }

               if (patIdxTmp == patIdxStart + 1) {
                  ++patIdxStart;
               } else {
                  patLength = patIdxTmp - patIdxStart - 1;
                  int strLength = strIdxEnd - strIdxStart + 1;
                  int foundIdx = -1;
                  int i = 0;

                  label106:
                  while(i <= strLength - patLength) {
                     for(int j = 0; j < patLength; ++j) {
                        String subPat = patDirs[patIdxStart + j + 1];
                        String subStr = strDirs[strIdxStart + i + j];
                        if (!match(subPat, subStr, isCaseSensitive)) {
                           ++i;
                           continue label106;
                        }
                     }

                     foundIdx = strIdxStart + i;
                     break;
                  }

                  if (foundIdx == -1) {
                     patDirs = null;
                     strDirs = null;
                     return false;
                  }

                  patIdxStart = patIdxTmp;
                  strIdxStart = foundIdx + patLength;
               }
            }

            for(patIdxTmp = patIdxStart; patIdxTmp <= patIdxEnd; ++patIdxTmp) {
               if (!patDirs[patIdxTmp].equals("**")) {
                  patDirs = null;
                  strDirs = null;
                  return false;
               }
            }

            return true;
         }
      }
   }

   public static boolean match(String pattern, String str) {
      return match(pattern, str, true);
   }

   public static boolean match(String pattern, String str, boolean isCaseSensitive) {
      char[] patArr = pattern.toCharArray();
      char[] strArr = str.toCharArray();
      int patIdxStart = 0;
      int patIdxEnd = patArr.length - 1;
      int strIdxStart = 0;
      int strIdxEnd = strArr.length - 1;
      boolean containsStar = false;

      int patIdxTmp;
      for(patIdxTmp = 0; patIdxTmp < patArr.length; ++patIdxTmp) {
         if (patArr[patIdxTmp] == '*') {
            containsStar = true;
            break;
         }
      }

      char ch;
      if (!containsStar) {
         if (patIdxEnd != strIdxEnd) {
            return false;
         } else {
            for(patIdxTmp = 0; patIdxTmp <= patIdxEnd; ++patIdxTmp) {
               ch = patArr[patIdxTmp];
               if (ch != '?') {
                  if (isCaseSensitive && ch != strArr[patIdxTmp]) {
                     return false;
                  }

                  if (!isCaseSensitive && Character.toUpperCase(ch) != Character.toUpperCase(strArr[patIdxTmp])) {
                     return false;
                  }
               }
            }

            return true;
         }
      } else if (patIdxEnd == 0) {
         return true;
      } else {
         while((ch = patArr[patIdxStart]) != '*' && strIdxStart <= strIdxEnd) {
            if (ch != '?') {
               if (isCaseSensitive && ch != strArr[strIdxStart]) {
                  return false;
               }

               if (!isCaseSensitive && Character.toUpperCase(ch) != Character.toUpperCase(strArr[strIdxStart])) {
                  return false;
               }
            }

            ++patIdxStart;
            ++strIdxStart;
         }

         if (strIdxStart > strIdxEnd) {
            for(patIdxTmp = patIdxStart; patIdxTmp <= patIdxEnd; ++patIdxTmp) {
               if (patArr[patIdxTmp] != '*') {
                  return false;
               }
            }

            return true;
         } else {
            while((ch = patArr[patIdxEnd]) != '*' && strIdxStart <= strIdxEnd) {
               if (ch != '?') {
                  if (isCaseSensitive && ch != strArr[strIdxEnd]) {
                     return false;
                  }

                  if (!isCaseSensitive && Character.toUpperCase(ch) != Character.toUpperCase(strArr[strIdxEnd])) {
                     return false;
                  }
               }

               --patIdxEnd;
               --strIdxEnd;
            }

            if (strIdxStart > strIdxEnd) {
               for(patIdxTmp = patIdxStart; patIdxTmp <= patIdxEnd; ++patIdxTmp) {
                  if (patArr[patIdxTmp] != '*') {
                     return false;
                  }
               }

               return true;
            } else {
               while(patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
                  patIdxTmp = -1;

                  int patLength;
                  for(patLength = patIdxStart + 1; patLength <= patIdxEnd; ++patLength) {
                     if (patArr[patLength] == '*') {
                        patIdxTmp = patLength;
                        break;
                     }
                  }

                  if (patIdxTmp == patIdxStart + 1) {
                     ++patIdxStart;
                  } else {
                     patLength = patIdxTmp - patIdxStart - 1;
                     int strLength = strIdxEnd - strIdxStart + 1;
                     int foundIdx = -1;

                     label166:
                     for(int i = 0; i <= strLength - patLength; ++i) {
                        for(int j = 0; j < patLength; ++j) {
                           ch = patArr[patIdxStart + j + 1];
                           if (ch != '?' && (isCaseSensitive && ch != strArr[strIdxStart + i + j] || !isCaseSensitive && Character.toUpperCase(ch) != Character.toUpperCase(strArr[strIdxStart + i + j]))) {
                              continue label166;
                           }
                        }

                        foundIdx = strIdxStart + i;
                        break;
                     }

                     if (foundIdx == -1) {
                        return false;
                     }

                     patIdxStart = patIdxTmp;
                     strIdxStart = foundIdx + patLength;
                  }
               }

               for(patIdxTmp = patIdxStart; patIdxTmp <= patIdxEnd; ++patIdxTmp) {
                  if (patArr[patIdxTmp] != '*') {
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   public static Vector tokenizePath(String path) {
      return tokenizePath(path, File.separator);
   }

   public static Vector tokenizePath(String path, String separator) {
      Vector ret = new Vector();
      if (FileUtils.isAbsolutePath(path)) {
         String[] s = FILE_UTILS.dissect(path);
         ret.add(s[0]);
         path = s[1];
      }

      StringTokenizer st = new StringTokenizer(path, separator);

      while(st.hasMoreTokens()) {
         ret.addElement(st.nextToken());
      }

      return ret;
   }

   private static String[] tokenizePathAsArray(String path) {
      String root = null;
      if (FileUtils.isAbsolutePath(path)) {
         String[] s = FILE_UTILS.dissect(path);
         root = s[0];
         path = s[1];
      }

      char sep = File.separatorChar;
      int start = 0;
      int len = path.length();
      int count = 0;

      for(int pos = 0; pos < len; ++pos) {
         if (path.charAt(pos) == sep) {
            if (pos != start) {
               ++count;
            }

            start = pos + 1;
         }
      }

      if (len != start) {
         ++count;
      }

      String[] l = new String[count + (root == null ? 0 : 1)];
      if (root != null) {
         l[0] = root;
         count = 1;
      } else {
         count = 0;
      }

      start = 0;

      for(int pos = 0; pos < len; ++pos) {
         if (path.charAt(pos) == sep) {
            if (pos != start) {
               String tok = path.substring(start, pos);
               l[count++] = tok;
            }

            start = pos + 1;
         }
      }

      if (len != start) {
         String tok = path.substring(start);
         l[count] = tok;
      }

      return l;
   }

   public static boolean isOutOfDate(File src, File target, int granularity) {
      if (!src.exists()) {
         return false;
      } else if (!target.exists()) {
         return true;
      } else {
         return src.lastModified() - (long)granularity > target.lastModified();
      }
   }

   public static boolean isOutOfDate(Resource src, Resource target, int granularity) {
      return isOutOfDate(src, target, (long)granularity);
   }

   public static boolean isOutOfDate(Resource src, Resource target, long granularity) {
      if (!src.isExists()) {
         return false;
      } else if (!target.isExists()) {
         return true;
      } else {
         return src.getLastModified() - granularity > target.getLastModified();
      }
   }

   public static String removeWhitespace(String input) {
      StringBuffer result = new StringBuffer();
      if (input != null) {
         StringTokenizer st = new StringTokenizer(input);

         while(st.hasMoreTokens()) {
            result.append(st.nextToken());
         }
      }

      return result.toString();
   }

   public static boolean hasWildcards(String input) {
      return input.indexOf(42) != -1 || input.indexOf(63) != -1;
   }

   public static String rtrimWildcardTokens(String input) {
      String[] tokens = tokenizePathAsArray(input);
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < tokens.length && !hasWildcards(tokens[i]); ++i) {
         if (i > 0 && sb.charAt(sb.length() - 1) != File.separatorChar) {
            sb.append(File.separator);
         }

         sb.append(tokens[i]);
      }

      return sb.toString();
   }
}
