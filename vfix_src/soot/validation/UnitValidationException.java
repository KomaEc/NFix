package soot.validation;

import soot.Body;
import soot.Unit;

public class UnitValidationException extends ValidationException {
   private static final long serialVersionUID = 1L;

   public UnitValidationException(Unit concerned, Body body, String strMessage, boolean isWarning) {
      super(concerned, strMessage, formatMsg(strMessage, concerned, body), isWarning);
   }

   public UnitValidationException(Unit concerned, Body body, String strMessage) {
      super(concerned, strMessage, formatMsg(strMessage, concerned, body), false);
   }

   private static String formatMsg(String s, Unit u, Body b) {
      StringBuilder sb = new StringBuilder();
      sb.append(s + "\n");
      sb.append("in unit: " + u + "\n");
      sb.append("in body: \n " + b + "\n");
      return sb.toString();
   }
}
