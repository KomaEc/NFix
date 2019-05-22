package org.apache.commons.validator;

import org.apache.oro.text.perl.Perl5Util;

public class EmailValidator {
   private static final String SPECIAL_CHARS = "\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]";
   private static final String VALID_CHARS = "[^\\s\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]";
   private static final String QUOTED_USER = "(\"[^\"]*\")";
   private static final String ATOM = "[^\\s\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]+";
   private static final String WORD = "(([^\\s\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]|')+|(\"[^\"]*\"))";
   private static final String LEGAL_ASCII_PATTERN = "/^[\\000-\\177]+$/";
   private static final String EMAIL_PATTERN = "/^(.+)@(.+)$/";
   private static final String IP_DOMAIN_PATTERN = "/^\\[(\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})\\]$/";
   private static final String TLD_PATTERN = "/^([a-zA-Z]+)$/";
   private static final String USER_PATTERN = "/^\\s*(([^\\s\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]|')+|(\"[^\"]*\"))(\\.(([^\\s\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]|')+|(\"[^\"]*\")))*\\s*$/";
   private static final String DOMAIN_PATTERN = "/^\\s*[^\\s\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]+(\\.[^\\s\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]+)*\\s*$/";
   private static final String ATOM_PATTERN = "/([^\\s\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]+)/";
   private static final EmailValidator instance = new EmailValidator();

   public static EmailValidator getInstance() {
      return instance;
   }

   protected EmailValidator() {
   }

   public boolean isValid(String email) {
      if (email == null) {
         return false;
      } else {
         Perl5Util matchAsciiPat = new Perl5Util();
         if (!matchAsciiPat.match("/^[\\000-\\177]+$/", email)) {
            return false;
         } else {
            email = this.stripComments(email);
            Perl5Util emailMatcher = new Perl5Util();
            if (!emailMatcher.match("/^(.+)@(.+)$/", email)) {
               return false;
            } else if (email.endsWith(".")) {
               return false;
            } else if (!this.isValidUser(emailMatcher.group(1))) {
               return false;
            } else {
               return this.isValidDomain(emailMatcher.group(2));
            }
         }
      }
   }

   protected boolean isValidDomain(String domain) {
      boolean symbolic = false;
      Perl5Util ipAddressMatcher = new Perl5Util();
      if (ipAddressMatcher.match("/^\\[(\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})\\]$/", domain)) {
         return this.isValidIpAddress(ipAddressMatcher);
      } else {
         Perl5Util domainMatcher = new Perl5Util();
         symbolic = domainMatcher.match("/^\\s*[^\\s\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]+(\\.[^\\s\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]+)*\\s*$/", domain);
         if (symbolic) {
            return this.isValidSymbolicDomain(domain);
         } else {
            return false;
         }
      }
   }

   protected boolean isValidUser(String user) {
      Perl5Util userMatcher = new Perl5Util();
      return userMatcher.match("/^\\s*(([^\\s\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]|')+|(\"[^\"]*\"))(\\.(([^\\s\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]|')+|(\"[^\"]*\")))*\\s*$/", user);
   }

   protected boolean isValidIpAddress(Perl5Util ipAddressMatcher) {
      for(int i = 1; i <= 4; ++i) {
         String ipSegment = ipAddressMatcher.group(i);
         if (ipSegment == null || ipSegment.length() <= 0) {
            return false;
         }

         boolean var4 = false;

         int iIpSegment;
         try {
            iIpSegment = Integer.parseInt(ipSegment);
         } catch (NumberFormatException var6) {
            return false;
         }

         if (iIpSegment > 255) {
            return false;
         }
      }

      return true;
   }

   protected boolean isValidSymbolicDomain(String domain) {
      String[] domainSegment = new String[10];
      boolean match = true;
      int i = 0;
      Perl5Util atomMatcher = new Perl5Util();

      while(match) {
         match = atomMatcher.match("/([^\\s\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]+)/", domain);
         if (match) {
            domainSegment[i] = atomMatcher.group(1);
            int l = domainSegment[i].length() + 1;
            domain = l >= domain.length() ? "" : domain.substring(l);
            ++i;
         }
      }

      if (i < 2) {
         return false;
      } else {
         String tld = domainSegment[i - 1];
         if (tld.length() > 1) {
            Perl5Util matchTldPat = new Perl5Util();
            if (!matchTldPat.match("/^([a-zA-Z]+)$/", tld)) {
               return false;
            } else {
               return true;
            }
         } else {
            return false;
         }
      }
   }

   protected String stripComments(String emailStr) {
      String input = emailStr;
      String commentPat = "s/^((?:[^\"\\\\]|\\\\.)*(?:\"(?:[^\"\\\\]|\\\\.)*\"(?:[^\"\\\\]|I111\\\\.)*)*)\\((?:[^()\\\\]|\\\\.)*\\)/$1 /osx";
      Perl5Util commentMatcher = new Perl5Util();

      String result;
      for(result = commentMatcher.substitute(commentPat, emailStr); !result.equals(input); result = commentMatcher.substitute(commentPat, result)) {
         input = result;
      }

      return result;
   }
}
