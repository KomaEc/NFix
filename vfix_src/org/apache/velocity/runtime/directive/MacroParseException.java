package org.apache.velocity.runtime.directive;

import org.apache.velocity.exception.ExtendedParseException;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.Token;

public class MacroParseException extends ParseException implements ExtendedParseException {
   private final String templateName;
   private static final long serialVersionUID = -4985224672336070689L;

   public MacroParseException(String msg, String templateName, Token currentToken) {
      super(msg);
      this.currentToken = currentToken;
      this.templateName = templateName;
   }

   public String getTemplateName() {
      return this.templateName;
   }

   public int getLineNumber() {
      return this.currentToken != null && this.currentToken.next != null ? this.currentToken.next.beginLine : -1;
   }

   public int getColumnNumber() {
      return this.currentToken != null && this.currentToken.next != null ? this.currentToken.next.beginColumn : -1;
   }

   public String getMessage() {
      if (!this.specialConstructor) {
         StringBuffer sb = new StringBuffer(super.getMessage());
         this.appendTemplateInfo(sb);
         return sb.toString();
      } else {
         int maxSize = 0;
         StringBuffer expected = new StringBuffer();

         for(int i = 0; i < this.expectedTokenSequences.length; ++i) {
            if (maxSize < this.expectedTokenSequences[i].length) {
               maxSize = this.expectedTokenSequences[i].length;
            }

            for(int j = 0; j < this.expectedTokenSequences[i].length; ++j) {
               expected.append(this.tokenImage[this.expectedTokenSequences[i][j]]).append(" ");
            }

            if (this.expectedTokenSequences[i][this.expectedTokenSequences[i].length - 1] != 0) {
               expected.append("...");
            }

            expected.append(this.eol).append("    ");
         }

         StringBuffer retval = new StringBuffer("Encountered \"");
         Token tok = this.currentToken.next;

         for(int i = 0; i < maxSize; ++i) {
            if (i != 0) {
               retval.append(" ");
            }

            if (tok.kind == 0) {
               retval.append(this.tokenImage[0]);
               break;
            }

            retval.append(this.add_escapes(tok.image));
            tok = tok.next;
         }

         retval.append("\"");
         this.appendTemplateInfo(retval);
         if (this.expectedTokenSequences.length == 1) {
            retval.append("Was expecting:").append(this.eol).append("    ");
         } else {
            retval.append("Was expecting one of:").append(this.eol).append("    ");
         }

         retval.append(expected.toString());
         return retval.toString();
      }
   }

   protected void appendTemplateInfo(StringBuffer sb) {
      sb.append(" at line ").append(this.getLineNumber()).append(", column ").append(this.getColumnNumber());
      if (this.getTemplateName() != null) {
         sb.append(" of ").append(this.getTemplateName());
      } else {
         sb.append(".");
      }

      sb.append(this.eol);
   }
}
