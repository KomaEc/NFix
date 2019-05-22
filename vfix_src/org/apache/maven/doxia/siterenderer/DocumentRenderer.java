package org.apache.maven.doxia.siterenderer;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.apache.maven.doxia.module.xhtml.decoration.render.RenderingContext;

public interface DocumentRenderer {
   void renderDocument(Writer var1, Renderer var2, SiteRenderingContext var3) throws RendererException, FileNotFoundException, UnsupportedEncodingException;

   String getOutputName();

   RenderingContext getRenderingContext();

   boolean isOverwrite();
}
