package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.util.Vector;
import org.apache.tools.ant.types.Parameter;

public final class StripLineComments extends BaseParamFilterReader implements ChainableReader {
   private static final String COMMENTS_KEY = "comment";
   private Vector comments = new Vector();
   private String line = null;

   public StripLineComments() {
   }

   public StripLineComments(Reader in) {
      super(in);
   }

   public int read() throws IOException {
      if (!this.getInitialized()) {
         this.initialize();
         this.setInitialized(true);
      }

      int ch = -1;
      if (this.line != null) {
         ch = this.line.charAt(0);
         if (this.line.length() == 1) {
            this.line = null;
         } else {
            this.line = this.line.substring(1);
         }
      } else {
         this.line = this.readLine();

         for(int commentsSize = this.comments.size(); this.line != null; this.line = this.readLine()) {
            for(int i = 0; i < commentsSize; ++i) {
               String comment = (String)this.comments.elementAt(i);
               if (this.line.startsWith(comment)) {
                  this.line = null;
                  break;
               }
            }

            if (this.line != null) {
               break;
            }
         }

         if (this.line != null) {
            return this.read();
         }
      }

      return ch;
   }

   public void addConfiguredComment(StripLineComments.Comment comment) {
      this.comments.addElement(comment.getValue());
   }

   private void setComments(Vector comments) {
      this.comments = comments;
   }

   private Vector getComments() {
      return this.comments;
   }

   public Reader chain(Reader rdr) {
      StripLineComments newFilter = new StripLineComments(rdr);
      newFilter.setComments(this.getComments());
      newFilter.setInitialized(true);
      return newFilter;
   }

   private void initialize() {
      Parameter[] params = this.getParameters();
      if (params != null) {
         for(int i = 0; i < params.length; ++i) {
            if ("comment".equals(params[i].getType())) {
               this.comments.addElement(params[i].getValue());
            }
         }
      }

   }

   public static class Comment {
      private String value;

      public final void setValue(String comment) {
         this.value = comment;
      }

      public final String getValue() {
         return this.value;
      }
   }
}
