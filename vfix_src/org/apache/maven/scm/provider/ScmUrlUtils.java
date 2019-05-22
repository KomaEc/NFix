package org.apache.maven.scm.provider;

import java.util.ArrayList;
import java.util.List;

public abstract class ScmUrlUtils {
   private static final String ILLEGAL_SCM_URL = "The scm url must be on the form 'scm:<scm provider><delimiter><provider specific part>' where <delimiter> can be either ':' or '|'.";

   public static String getDelimiter(String scmUrl) {
      scmUrl = scmUrl.substring(4);
      int index = scmUrl.indexOf(124);
      if (index == -1) {
         index = scmUrl.indexOf(58);
         if (index == -1) {
            throw new IllegalArgumentException("The scm url does not contain a valid delimiter.");
         }
      }

      return scmUrl.substring(index, index + 1);
   }

   public static String getProvider(String scmUrl) {
      String delimiter = getDelimiter(scmUrl);
      scmUrl = scmUrl.substring(4);
      int firstDelimiterIndex = scmUrl.indexOf(delimiter);
      return scmUrl.substring(0, firstDelimiterIndex);
   }

   public static String getProviderSpecificPart(String scmUrl) {
      String delimiter = getDelimiter(scmUrl);
      scmUrl = scmUrl.substring(4);
      int firstDelimiterIndex = scmUrl.indexOf(delimiter);
      return scmUrl.substring(firstDelimiterIndex + 1);
   }

   public static boolean isValid(String scmUrl) {
      List<String> messages = validate(scmUrl);
      return messages.isEmpty();
   }

   public static List<String> validate(String scmUrl) {
      List<String> messages = new ArrayList();
      if (scmUrl == null) {
         messages.add("The scm url cannot be null.");
         return messages;
      } else if (!scmUrl.startsWith("scm:")) {
         messages.add("The scm url must start with 'scm:'.");
         return messages;
      } else if (scmUrl.length() < 6) {
         messages.add("The scm url must be on the form 'scm:<scm provider><delimiter><provider specific part>' where <delimiter> can be either ':' or '|'.");
         return messages;
      } else {
         try {
            getDelimiter(scmUrl);
         } catch (IllegalArgumentException var3) {
            messages.add(var3.getMessage());
         }

         return messages;
      }
   }
}
