package org.apache.tools.ant.taskdefs.cvslib;

import java.io.ByteArrayOutputStream;
import java.util.StringTokenizer;
import org.apache.tools.ant.taskdefs.AbstractCvsTask;

public class CvsVersion extends AbstractCvsTask {
   static final long VERSION_1_11_2 = 11102L;
   static final long MULTIPLY = 100L;
   private String clientVersion;
   private String serverVersion;
   private String clientVersionProperty;
   private String serverVersionProperty;

   public String getClientVersion() {
      return this.clientVersion;
   }

   public String getServerVersion() {
      return this.serverVersion;
   }

   public void setClientVersionProperty(String clientVersionProperty) {
      this.clientVersionProperty = clientVersionProperty;
   }

   public void setServerVersionProperty(String serverVersionProperty) {
      this.serverVersionProperty = serverVersionProperty;
   }

   public boolean supportsCvsLogWithSOption() {
      if (this.serverVersion == null) {
         return false;
      } else {
         StringTokenizer tokenizer = new StringTokenizer(this.serverVersion, ".");
         long counter = 10000L;

         long version;
         for(version = 0L; tokenizer.hasMoreTokens(); counter /= 100L) {
            String s = tokenizer.nextToken();
            int i = false;

            int i;
            for(i = 0; i < s.length() && Character.isDigit(s.charAt(i)); ++i) {
            }

            String s2 = s.substring(0, i);
            version += counter * Long.parseLong(s2);
            if (counter == 1L) {
               break;
            }
         }

         return version >= 11102L;
      }
   }

   public void execute() {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      this.setOutputStream(bos);
      ByteArrayOutputStream berr = new ByteArrayOutputStream();
      this.setErrorStream(berr);
      this.setCommand("version");
      super.execute();
      String output = bos.toString();
      StringTokenizer st = new StringTokenizer(output);
      boolean client = false;
      boolean server = false;
      boolean cvs = false;

      while(true) {
         while(st.hasMoreTokens()) {
            String currentToken = st.nextToken();
            if (currentToken.equals("Client:")) {
               client = true;
            } else if (currentToken.equals("Server:")) {
               server = true;
            } else if (currentToken.equals("(CVS)")) {
               cvs = true;
            }

            if (client && cvs) {
               if (st.hasMoreTokens()) {
                  this.clientVersion = st.nextToken();
               }

               client = false;
               cvs = false;
            } else if (server && cvs) {
               if (st.hasMoreTokens()) {
                  this.serverVersion = st.nextToken();
               }

               server = false;
               cvs = false;
            }
         }

         if (this.clientVersionProperty != null) {
            this.getProject().setNewProperty(this.clientVersionProperty, this.clientVersion);
         }

         if (this.serverVersionProperty != null) {
            this.getProject().setNewProperty(this.serverVersionProperty, this.serverVersion);
         }

         return;
      }
   }
}
