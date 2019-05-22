package org.apache.maven.wagon;

public class Streams {
   private String out = "";
   private String err = "";

   public String getOut() {
      return this.out;
   }

   public void setOut(String out) {
      this.out = out;
   }

   public String getErr() {
      return this.err;
   }

   public void setErr(String err) {
      this.err = err;
   }
}
