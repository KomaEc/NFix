package org.apache.commons.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.validator.util.Flags;

public class CreditCardValidator {
   public static final int NONE = 0;
   public static final int AMEX = 1;
   public static final int VISA = 2;
   public static final int MASTERCARD = 4;
   public static final int DISCOVER = 8;
   private Collection cardTypes;

   public CreditCardValidator() {
      this(15);
   }

   public CreditCardValidator(int options) {
      this.cardTypes = new ArrayList();
      Flags f = new Flags((long)options);
      if (f.isOn(2L)) {
         this.cardTypes.add(new CreditCardValidator.Visa());
      }

      if (f.isOn(1L)) {
         this.cardTypes.add(new CreditCardValidator.Amex());
      }

      if (f.isOn(4L)) {
         this.cardTypes.add(new CreditCardValidator.Mastercard());
      }

      if (f.isOn(8L)) {
         this.cardTypes.add(new CreditCardValidator.Discover());
      }

   }

   public boolean isValid(String card) {
      if (card != null && card.length() >= 13 && card.length() <= 19) {
         if (!this.luhnCheck(card)) {
            return false;
         } else {
            Iterator types = this.cardTypes.iterator();

            while(types.hasNext()) {
               CreditCardValidator.CreditCardType type = (CreditCardValidator.CreditCardType)types.next();
               if (type.matches(card)) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public void addAllowedCardType(CreditCardValidator.CreditCardType type) {
      this.cardTypes.add(type);
   }

   protected boolean luhnCheck(String cardNumber) {
      int digits = cardNumber.length();
      int oddOrEven = digits & 1;
      long sum = 0L;

      for(int count = 0; count < digits; ++count) {
         boolean var7 = false;

         int digit;
         try {
            digit = Integer.parseInt(cardNumber.charAt(count) + "");
         } catch (NumberFormatException var9) {
            return false;
         }

         if ((count & 1 ^ oddOrEven) == 0) {
            digit *= 2;
            if (digit > 9) {
               digit -= 9;
            }
         }

         sum += (long)digit;
      }

      return sum == 0L ? false : sum % 10L == 0L;
   }

   private class Mastercard implements CreditCardValidator.CreditCardType {
      private static final String PREFIX = "51,52,53,54,55,";

      private Mastercard() {
      }

      public boolean matches(String card) {
         String prefix2 = card.substring(0, 2) + ",";
         return "51,52,53,54,55,".indexOf(prefix2) != -1 && card.length() == 16;
      }

      // $FF: synthetic method
      Mastercard(Object x1) {
         this();
      }
   }

   private class Discover implements CreditCardValidator.CreditCardType {
      private static final String PREFIX = "6011";

      private Discover() {
      }

      public boolean matches(String card) {
         return card.substring(0, 4).equals("6011") && card.length() == 16;
      }

      // $FF: synthetic method
      Discover(Object x1) {
         this();
      }
   }

   private class Amex implements CreditCardValidator.CreditCardType {
      private static final String PREFIX = "34,37,";

      private Amex() {
      }

      public boolean matches(String card) {
         String prefix2 = card.substring(0, 2) + ",";
         return "34,37,".indexOf(prefix2) != -1 && card.length() == 15;
      }

      // $FF: synthetic method
      Amex(Object x1) {
         this();
      }
   }

   private class Visa implements CreditCardValidator.CreditCardType {
      private static final String PREFIX = "4,5,";

      private Visa() {
      }

      public boolean matches(String card) {
         String prefix2 = card.substring(0, 1) + ",";
         return "4,5,".indexOf(prefix2) != -1 && (card.length() == 13 || card.length() == 16);
      }

      // $FF: synthetic method
      Visa(Object x1) {
         this();
      }
   }

   public interface CreditCardType {
      boolean matches(String var1);
   }
}
