package org.apache.tools.ant.taskdefs.condition;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

public class Http extends ProjectComponent implements Condition {
   private static final int ERROR_BEGINS = 400;
   private String spec = null;
   private int errorsBeginAt = 400;

   public void setUrl(String url) {
      this.spec = url;
   }

   public void setErrorsBeginAt(int errorsBeginAt) {
      this.errorsBeginAt = errorsBeginAt;
   }

   public boolean eval() throws BuildException {
      if (this.spec == null) {
         throw new BuildException("No url specified in http condition");
      } else {
         this.log("Checking for " + this.spec, 3);

         try {
            URL url = new URL(this.spec);

            try {
               URLConnection conn = url.openConnection();
               if (conn instanceof HttpURLConnection) {
                  HttpURLConnection http = (HttpURLConnection)conn;
                  int code = http.getResponseCode();
                  this.log("Result code for " + this.spec + " was " + code, 3);
                  return code > 0 && code < this.errorsBeginAt;
               } else {
                  return true;
               }
            } catch (IOException var5) {
               return false;
            }
         } catch (MalformedURLException var6) {
            throw new BuildException("Badly formed URL: " + this.spec, var6);
         }
      }
   }
}
