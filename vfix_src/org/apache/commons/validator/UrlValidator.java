package org.apache.commons.validator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.validator.util.Flags;
import org.apache.oro.text.perl.Perl5Util;

public class UrlValidator implements Serializable {
   public static final int ALLOW_ALL_SCHEMES = 1;
   public static final int ALLOW_2_SLASHES = 2;
   public static final int NO_FRAGMENTS = 4;
   private static final String ALPHA_CHARS = "a-zA-Z";
   private static final String ALPHA_NUMERIC_CHARS = "a-zA-Z\\d";
   private static final String SPECIAL_CHARS = ";/@&=,.?:+$";
   private static final String VALID_CHARS = "[^\\s;/@&=,.?:+$]";
   private static final String SCHEME_CHARS = "a-zA-Z";
   private static final String AUTHORITY_CHARS = "a-zA-Z\\d\\-\\.";
   private static final String ATOM = "[^\\s;/@&=,.?:+$]+";
   private static final String URL_PATTERN = "/^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?/";
   private static final int PARSE_URL_SCHEME = 2;
   private static final int PARSE_URL_AUTHORITY = 4;
   private static final int PARSE_URL_PATH = 5;
   private static final int PARSE_URL_QUERY = 7;
   private static final int PARSE_URL_FRAGMENT = 9;
   private static final String SCHEME_PATTERN = "/^[a-zA-Z]/";
   private static final String AUTHORITY_PATTERN = "/^([a-zA-Z\\d\\-\\.]*)(:\\d*)?(.*)?/";
   private static final int PARSE_AUTHORITY_HOST_IP = 1;
   private static final int PARSE_AUTHORITY_PORT = 2;
   private static final int PARSE_AUTHORITY_EXTRA = 3;
   private static final String PATH_PATTERN = "/^(/[-\\w:@&?=+,.!/~*'%$]*)?$/";
   private static final String QUERY_PATTERN = "/^(.*)$/";
   private static final String LEGAL_ASCII_PATTERN = "/^[\\000-\\177]+$/";
   private static final String IP_V4_DOMAIN_PATTERN = "/^(\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})$/";
   private static final String DOMAIN_PATTERN = "/^[^\\s;/@&=,.?:+$]+(\\.[^\\s;/@&=,.?:+$]+)*$/";
   private static final String PORT_PATTERN = "/^:(\\d{1,5})$/";
   private static final String ATOM_PATTERN = "/([^\\s;/@&=,.?:+$]+)/";
   private static final String ALPHA_PATTERN = "/^[a-zA-Z]/";
   private Flags options;
   private Set allowedSchemes;
   protected String[] defaultSchemes;

   public UrlValidator() {
      this((String[])null);
   }

   public UrlValidator(String[] schemes) {
      this(schemes, 0);
   }

   public UrlValidator(int options) {
      this((String[])null, options);
   }

   public UrlValidator(String[] schemes, int options) {
      this.options = null;
      this.allowedSchemes = new HashSet();
      this.defaultSchemes = new String[]{"http", "https", "ftp"};
      this.options = new Flags((long)options);
      if (!this.options.isOn(1L)) {
         if (schemes == null) {
            schemes = this.defaultSchemes;
         }

         this.allowedSchemes.addAll(Arrays.asList(schemes));
      }
   }

   public boolean isValid(String value) {
      if (value == null) {
         return false;
      } else {
         Perl5Util matchUrlPat = new Perl5Util();
         Perl5Util matchAsciiPat = new Perl5Util();
         if (!matchAsciiPat.match("/^[\\000-\\177]+$/", value)) {
            return false;
         } else if (!matchUrlPat.match("/^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?/", value)) {
            return false;
         } else if (!this.isValidScheme(matchUrlPat.group(2))) {
            return false;
         } else if (!this.isValidAuthority(matchUrlPat.group(4))) {
            return false;
         } else if (!this.isValidPath(matchUrlPat.group(5))) {
            return false;
         } else if (!this.isValidQuery(matchUrlPat.group(7))) {
            return false;
         } else {
            return this.isValidFragment(matchUrlPat.group(9));
         }
      }
   }

   protected boolean isValidScheme(String scheme) {
      if (scheme == null) {
         return false;
      } else {
         Perl5Util schemeMatcher = new Perl5Util();
         if (!schemeMatcher.match("/^[a-zA-Z]/", scheme)) {
            return false;
         } else {
            return !this.options.isOff(1L) || this.allowedSchemes.contains(scheme);
         }
      }
   }

   protected boolean isValidAuthority(String authority) {
      if (authority == null) {
         return false;
      } else {
         Perl5Util authorityMatcher = new Perl5Util();
         Perl5Util matchIPV4Pat = new Perl5Util();
         if (!authorityMatcher.match("/^([a-zA-Z\\d\\-\\.]*)(:\\d*)?(.*)?/", authority)) {
            return false;
         } else {
            boolean ipV4Address = false;
            boolean hostname = false;
            String hostIP = authorityMatcher.group(1);
            ipV4Address = matchIPV4Pat.match("/^(\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})$/", hostIP);
            String extra;
            if (!ipV4Address) {
               Perl5Util domainMatcher = new Perl5Util();
               hostname = domainMatcher.match("/^[^\\s;/@&=,.?:+$]+(\\.[^\\s;/@&=,.?:+$]+)*$/", hostIP);
            } else {
               for(int i = 1; i <= 4; ++i) {
                  extra = matchIPV4Pat.group(i);
                  if (extra == null || extra.length() <= 0) {
                     return false;
                  }

                  try {
                     if (Integer.parseInt(extra) > 255) {
                        return false;
                     }
                  } catch (NumberFormatException var14) {
                     return false;
                  }
               }
            }

            if (hostname) {
               String[] domainSegment = new String[10];
               boolean match = true;
               int segmentCount = 0;
               int segmentLength = false;
               Perl5Util atomMatcher = new Perl5Util();

               while(match) {
                  match = atomMatcher.match("/([^\\s;/@&=,.?:+$]+)/", hostIP);
                  if (match) {
                     domainSegment[segmentCount] = atomMatcher.group(1);
                     int segmentLength = domainSegment[segmentCount].length() + 1;
                     hostIP = segmentLength >= hostIP.length() ? "" : hostIP.substring(segmentLength);
                     ++segmentCount;
                  }
               }

               String topLevel = domainSegment[segmentCount - 1];
               if (topLevel.length() < 2 || topLevel.length() > 4) {
                  return false;
               }

               Perl5Util alphaMatcher = new Perl5Util();
               if (!alphaMatcher.match("/^[a-zA-Z]/", topLevel.substring(0, 1))) {
                  return false;
               }

               if (segmentCount < 2) {
                  return false;
               }
            }

            if (!hostname && !ipV4Address) {
               return false;
            } else {
               String port = authorityMatcher.group(2);
               if (port != null) {
                  Perl5Util portMatcher = new Perl5Util();
                  if (!portMatcher.match("/^:(\\d{1,5})$/", port)) {
                     return false;
                  }
               }

               extra = authorityMatcher.group(3);
               if (!GenericValidator.isBlankOrNull(extra)) {
                  return false;
               } else {
                  return true;
               }
            }
         }
      }
   }

   protected boolean isValidPath(String path) {
      if (path == null) {
         return false;
      } else {
         Perl5Util pathMatcher = new Perl5Util();
         if (!pathMatcher.match("/^(/[-\\w:@&?=+,.!/~*'%$]*)?$/", path)) {
            return false;
         } else {
            int slash2Count = this.countToken("//", path);
            if (this.options.isOff(2L) && slash2Count > 0) {
               return false;
            } else {
               int slashCount = this.countToken("/", path);
               int dot2Count = this.countToken("..", path);
               return dot2Count <= 0 || slashCount - slash2Count - 1 > dot2Count;
            }
         }
      }
   }

   protected boolean isValidQuery(String query) {
      if (query == null) {
         return true;
      } else {
         Perl5Util queryMatcher = new Perl5Util();
         return queryMatcher.match("/^(.*)$/", query);
      }
   }

   protected boolean isValidFragment(String fragment) {
      return fragment == null ? true : this.options.isOff(4L);
   }

   protected int countToken(String token, String target) {
      int tokenIndex = 0;
      int count = 0;

      while(tokenIndex != -1) {
         tokenIndex = target.indexOf(token, tokenIndex);
         if (tokenIndex > -1) {
            ++tokenIndex;
            ++count;
         }
      }

      return count;
   }
}
