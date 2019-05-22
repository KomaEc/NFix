package org.apache.maven.doxia.siterenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import org.apache.maven.doxia.module.xhtml.decoration.render.RenderingContext;
import org.apache.maven.doxia.site.decoration.DecorationModel;
import org.apache.maven.doxia.siterenderer.sink.SiteRendererSink;

public interface Renderer {
   String ROLE = Renderer.class.getName();

   void render(Collection var1, SiteRenderingContext var2, File var3) throws RendererException, IOException;

   void generateDocument(Writer var1, SiteRendererSink var2, SiteRenderingContext var3) throws RendererException;

   SiteRenderingContext createContextForSkin(File var1, Map var2, DecorationModel var3, String var4, Locale var5) throws IOException;

   SiteRenderingContext createContextForTemplate(File var1, File var2, Map var3, DecorationModel var4, String var5, Locale var6) throws MalformedURLException;

   void copyResources(SiteRenderingContext var1, File var2, File var3) throws IOException;

   Map locateDocumentFiles(SiteRenderingContext var1) throws IOException, RendererException;

   void renderDocument(Writer var1, RenderingContext var2, SiteRenderingContext var3) throws RendererException, FileNotFoundException, UnsupportedEncodingException;
}
