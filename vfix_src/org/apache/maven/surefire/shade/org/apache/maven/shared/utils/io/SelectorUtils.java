package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.Nonnull;

public final class SelectorUtils {
   private static final String PATTERN_HANDLER_PREFIX = "[";
   public static final String PATTERN_HANDLER_SUFFIX = "]";
   public static final String REGEX_HANDLER_PREFIX = "%regex[";
   public static final String ANT_HANDLER_PREFIX = "%ant[";

   private SelectorUtils() {
   }

   public static boolean matchPatternStart(String pattern, String str) {
      return matchPatternStart(pattern, str, true);
   }

   public static boolean matchPatternStart(String pattern, String str, boolean isCaseSensitive) {
      if (isRegexPrefixedPattern(pattern)) {
         return true;
      } else {
         if (isAntPrefixedPattern(pattern)) {
            pattern = pattern.substring("%ant[".length(), pattern.length() - "]".length());
         }

         String altPattern = pattern.replace('\\', '/');
         String altStr = str.replace('\\', '/');
         return matchAntPathPatternStart(altPattern, altStr, "/", isCaseSensitive);
      }
   }

   private static boolean matchAntPathPatternStart(String pattern, String str, String separator, boolean isCaseSensitive) {
      if (separatorPatternStartSlashMismatch(pattern, str, separator)) {
         return false;
      } else {
         List<String> patDirs = tokenizePath(pattern, separator);
         List<String> strDirs = tokenizePath(str, separator);
         int patIdxStart = 0;
         int patIdxEnd = patDirs.size() - 1;
         int strIdxStart = 0;

         int strIdxEnd;
         for(strIdxEnd = strDirs.size() - 1; patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd; ++strIdxStart) {
            String patDir = (String)patDirs.get(patIdxStart);
            if ("**".equals(patDir)) {
               break;
            }

            if (!match(patDir, (String)strDirs.get(strIdxStart), isCaseSensitive)) {
               return false;
            }

            ++patIdxStart;
         }

         return strIdxStart > strIdxEnd || patIdxStart <= patIdxEnd;
      }
   }

   public static boolean matchPath(String pattern, String str) {
      return matchPath(pattern, str, true);
   }

   public static boolean matchPath(String pattern, String str, boolean isCaseSensitive) {
      if (pattern.length() > "%regex[".length() + "]".length() + 1 && pattern.startsWith("%regex[") && pattern.endsWith("]")) {
         pattern = pattern.substring("%regex[".length(), pattern.length() - "]".length());
         return str.matches(pattern);
      } else {
         if (pattern.length() > "%ant[".length() + "]".length() + 1 && pattern.startsWith("%ant[") && pattern.endsWith("]")) {
            pattern = pattern.substring("%ant[".length(), pattern.length() - "]".length());
         }

         return matchAntPathPattern(pattern, str, isCaseSensitive);
      }
   }

   private static boolean matchAntPathPattern(String pattern, String str, boolean isCaseSensitive) {
      if (str.startsWith(File.separator) != pattern.startsWith(File.separator)) {
         return false;
      } else {
         List<String> patDirs = tokenizePath(pattern, File.separator);
         List<String> strDirs = tokenizePath(str, File.separator);
         int patIdxStart = 0;
         int patIdxEnd = patDirs.size() - 1;
         int strIdxStart = 0;

         int strIdxEnd;
         String patDir;
         for(strIdxEnd = strDirs.size() - 1; patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd; ++strIdxStart) {
            patDir = (String)patDirs.get(patIdxStart);
            if ("**".equals(patDir)) {
               break;
            }

            if (!match(patDir, (String)strDirs.get(strIdxStart), isCaseSensitive)) {
               return false;
            }

            ++patIdxStart;
         }

         int patIdxTmp;
         if (strIdxStart > strIdxEnd) {
            for(patIdxTmp = patIdxStart; patIdxTmp <= patIdxEnd; ++patIdxTmp) {
               if (!"**".equals(patDirs.get(patIdxTmp))) {
                  return false;
               }
            }

            return true;
         } else if (patIdxStart > patIdxEnd) {
            return false;
         } else {
            while(patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
               patDir = (String)patDirs.get(patIdxEnd);
               if ("**".equals(patDir)) {
                  break;
               }

               if (!match(patDir, (String)strDirs.get(strIdxEnd), isCaseSensitive)) {
                  return false;
               }

               --patIdxEnd;
               --strIdxEnd;
            }

            if (strIdxStart > strIdxEnd) {
               for(patIdxTmp = patIdxStart; patIdxTmp <= patIdxEnd; ++patIdxTmp) {
                  if (!"**".equals(patDirs.get(patIdxTmp))) {
                     return false;
                  }
               }

               return true;
            } else {
               while(patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
                  patIdxTmp = -1;

                  int patLength;
                  for(patLength = patIdxStart + 1; patLength <= patIdxEnd; ++patLength) {
                     if ("**".equals(patDirs.get(patLength))) {
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

                     label110:
                     while(i <= strLength - patLength) {
                        for(int j = 0; j < patLength; ++j) {
                           String subPat = (String)patDirs.get(patIdxStart + j + 1);
                           String subStr = (String)strDirs.get(strIdxStart + i + j);
                           if (!match(subPat, subStr, isCaseSensitive)) {
                              ++i;
                              continue label110;
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
                  if (!"**".equals(patDirs.get(patIdxTmp))) {
                     return false;
                  }
               }

               return true;
            }
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
      char[] arr$ = patArr;
      int patLength = patArr.length;

      int strLength;
      for(strLength = 0; strLength < patLength; ++strLength) {
         char aPatArr = arr$[strLength];
         if (aPatArr == '*') {
            containsStar = true;
            break;
         }
      }

      char ch;
      int patIdxTmp;
      if (!containsStar) {
         if (patIdxEnd != strIdxEnd) {
            return false;
         } else {
            for(patIdxTmp = 0; patIdxTmp <= patIdxEnd; ++patIdxTmp) {
               ch = patArr[patIdxTmp];
               if (ch != '?' && !equals(ch, strArr[patIdxTmp], isCaseSensitive)) {
                  return false;
               }
            }

            return true;
         }
      } else if (patIdxEnd == 0) {
         return true;
      } else {
         while((ch = patArr[patIdxStart]) != '*' && strIdxStart <= strIdxEnd) {
            if (ch != '?' && !equals(ch, strArr[strIdxStart], isCaseSensitive)) {
               return false;
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
               if (ch != '?' && !equals(ch, strArr[strIdxEnd], isCaseSensitive)) {
                  return false;
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
                     strLength = strIdxEnd - strIdxStart + 1;
                     int foundIdx = -1;
                     int i = 0;

                     label132:
                     while(i <= strLength - patLength) {
                        for(int j = 0; j < patLength; ++j) {
                           ch = patArr[patIdxStart + j + 1];
                           if (ch != '?' && !equals(ch, strArr[strIdxStart + i + j], isCaseSensitive)) {
                              ++i;
                              continue label132;
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

   private static boolean equals(char c1, char c2, boolean isCaseSensitive) {
      if (c1 == c2) {
         return true;
      } else {
         return !isCaseSensitive && (Character.toUpperCase(c1) == Character.toUpperCase(c2) || Character.toLowerCase(c1) == Character.toLowerCase(c2));
      }
   }

   private static List<String> tokenizePath(String path, String separator) {
      List<String> ret = new ArrayList();
      StringTokenizer st = new StringTokenizer(path, separator);

      while(st.hasMoreTokens()) {
         ret.add(st.nextToken());
      }

      return ret;
   }

   static boolean matchAntPathPatternStart(@Nonnull MatchPattern pattern, @Nonnull String str, @Nonnull String separator, boolean isCaseSensitive) {
      return separatorPatternStartSlashMismatch(pattern, str, separator) ? false : matchAntPathPatternStart(pattern.getTokenizedPathString(), str, separator, isCaseSensitive);
   }

   private static String[] tokenizePathToString(@Nonnull String path, @Nonnull String separator) {
      List<String> ret = new ArrayList();
      StringTokenizer st = new StringTokenizer(path, separator);

      while(st.hasMoreTokens()) {
         ret.add(st.nextToken());
      }

      return (String[])ret.toArray(new String[ret.size()]);
   }

   private static boolean matchAntPathPatternStart(@Nonnull String[] patDirs, @Nonnull String str, @Nonnull String separator, boolean isCaseSensitive) {
      String[] strDirs = tokenizePathToString(str, separator);
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

      return strIdxStart > strIdxEnd || patIdxStart <= patIdxEnd;
   }

   private static boolean separatorPatternStartSlashMismatch(@Nonnull MatchPattern matchPattern, @Nonnull String str, @Nonnull String separator) {
      return str.startsWith(separator) != matchPattern.startsWith(separator);
   }

   private static boolean separatorPatternStartSlashMismatch(String pattern, String str, String separator) {
      return str.startsWith(separator) != pattern.startsWith(separator);
   }

   static boolean matchAntPathPattern(String[] patDirs, String[] strDirs, boolean isCaseSensitive) {
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
            return false;
         }

         ++patIdxStart;
      }

      int patIdxTmp;
      if (strIdxStart > strIdxEnd) {
         for(patIdxTmp = patIdxStart; patIdxTmp <= patIdxEnd; ++patIdxTmp) {
            if (!patDirs[patIdxTmp].equals("**")) {
               return false;
            }
         }

         return true;
      } else if (patIdxStart > patIdxEnd) {
         return false;
      } else {
         while(patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            patDir = patDirs[patIdxEnd];
            if (patDir.equals("**")) {
               break;
            }

            if (!match(patDir, strDirs[strIdxEnd], isCaseSensitive)) {
               return false;
            }

            --patIdxEnd;
            --strIdxEnd;
         }

         if (strIdxStart > strIdxEnd) {
            for(patIdxTmp = patIdxStart; patIdxTmp <= patIdxEnd; ++patIdxTmp) {
               if (!patDirs[patIdxTmp].equals("**")) {
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
                     return false;
                  }

                  patIdxStart = patIdxTmp;
                  strIdxStart = foundIdx + patLength;
               }
            }

            for(patIdxTmp = patIdxStart; patIdxTmp <= patIdxEnd; ++patIdxTmp) {
               if (!patDirs[patIdxTmp].equals("**")) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   static boolean isRegexPrefixedPattern(String pattern) {
      return pattern.length() > "%regex[".length() + "]".length() + 1 && pattern.startsWith("%regex[") && pattern.endsWith("]");
   }

   static boolean isAntPrefixedPattern(String pattern) {
      return pattern.length() > "%ant[".length() + "]".length() + 1 && pattern.startsWith("%ant[") && pattern.endsWith("]");
   }

   static boolean matchAntPathPattern(@Nonnull MatchPattern matchPattern, @Nonnull String str, @Nonnull String separator, boolean isCaseSensitive) {
      if (separatorPatternStartSlashMismatch(matchPattern, str, separator)) {
         return false;
      } else {
         String[] patDirs = matchPattern.getTokenizedPathString();
         String[] strDirs = tokenizePathToString(str, separator);
         return matchAntPathPattern(patDirs, strDirs, isCaseSensitive);
      }
   }
}
