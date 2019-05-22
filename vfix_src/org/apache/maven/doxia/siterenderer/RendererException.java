package org.apache.maven.doxia.siterenderer;

public class RendererException extends Exception {
   private static final long serialVersionUID = 3141592653589793238L;

   public RendererException(String message) {
      super(message);
   }

   public RendererException(String message, Throwable t) {
      super(message, t);
   }
}
