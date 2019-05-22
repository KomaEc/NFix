package org.jf.dexlib2;

import com.google.common.collect.Maps;
import java.util.HashMap;
import javax.annotation.Nullable;
import org.jf.util.ExceptionWithContext;

public class VerificationError {
   public static final int GENERIC = 1;
   public static final int NO_SUCH_CLASS = 2;
   public static final int NO_SUCH_FIELD = 3;
   public static final int NO_SUCH_METHOD = 4;
   public static final int ILLEGAL_CLASS_ACCESS = 5;
   public static final int ILLEGAL_FIELD_ACCESS = 6;
   public static final int ILLEGAL_METHOD_ACCESS = 7;
   public static final int CLASS_CHANGE_ERROR = 8;
   public static final int INSTANTIATION_ERROR = 9;
   private static final HashMap<String, Integer> verificationErrorNames = Maps.newHashMap();

   @Nullable
   public static String getVerificationErrorName(int verificationError) {
      switch(verificationError) {
      case 1:
         return "generic-error";
      case 2:
         return "no-such-class";
      case 3:
         return "no-such-field";
      case 4:
         return "no-such-method";
      case 5:
         return "illegal-class-access";
      case 6:
         return "illegal-field-access";
      case 7:
         return "illegal-method-access";
      case 8:
         return "class-change-error";
      case 9:
         return "instantiation-error";
      default:
         return null;
      }
   }

   public static int getVerificationError(String verificationError) {
      Integer ret = (Integer)verificationErrorNames.get(verificationError);
      if (ret == null) {
         throw new ExceptionWithContext("Invalid verification error: %s", new Object[]{verificationError});
      } else {
         return ret;
      }
   }

   public static boolean isValidVerificationError(int verificationError) {
      return verificationError > 0 && verificationError < 10;
   }

   static {
      verificationErrorNames.put("generic-error", 1);
      verificationErrorNames.put("no-such-class", 2);
      verificationErrorNames.put("no-such-field", 3);
      verificationErrorNames.put("no-such-method", 4);
      verificationErrorNames.put("illegal-class-access", 5);
      verificationErrorNames.put("illegal-field-access", 6);
      verificationErrorNames.put("illegal-method-access", 7);
      verificationErrorNames.put("class-change-error", 8);
      verificationErrorNames.put("instantiation-error", 9);
   }
}
