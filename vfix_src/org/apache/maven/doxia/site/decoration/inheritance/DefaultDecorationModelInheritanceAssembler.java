package org.apache.maven.doxia.site.decoration.inheritance;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.doxia.site.decoration.Banner;
import org.apache.maven.doxia.site.decoration.Body;
import org.apache.maven.doxia.site.decoration.DecorationModel;
import org.apache.maven.doxia.site.decoration.LinkItem;
import org.apache.maven.doxia.site.decoration.Logo;
import org.apache.maven.doxia.site.decoration.Menu;
import org.apache.maven.doxia.site.decoration.MenuItem;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class DefaultDecorationModelInheritanceAssembler implements DecorationModelInheritanceAssembler {
   public void assembleModelInheritance(String name, DecorationModel child, DecorationModel parent, String childBaseUrl, String parentBaseUrl) {
      DefaultDecorationModelInheritanceAssembler.URLContainer urlContainer = new DefaultDecorationModelInheritanceAssembler.URLContainer(parentBaseUrl, childBaseUrl);
      if (parent != null) {
         if (child.getBannerLeft() == null) {
            child.setBannerLeft(parent.getBannerLeft());
            this.resolveBannerPaths(child.getBannerLeft(), urlContainer);
         }

         if (child.getBannerRight() == null) {
            child.setBannerRight(parent.getBannerRight());
            this.resolveBannerPaths(child.getBannerRight(), urlContainer);
         }

         if (child.getPublishDate() == null) {
            child.setPublishDate(parent.getPublishDate());
         }

         if (child.getVersion() == null) {
            child.setVersion(parent.getVersion());
         }

         if (child.getSkin() == null) {
            child.setSkin(parent.getSkin());
         }

         child.setPoweredBy(this.mergePoweredByLists(child.getPoweredBy(), parent.getPoweredBy(), urlContainer));
         this.assembleBodyInheritance(name, child, parent, urlContainer);
         this.assembleCustomInheritance(child, parent);
      }

   }

   public void resolvePaths(DecorationModel decoration, String baseUrl) {
      DefaultDecorationModelInheritanceAssembler.URLContainer urlContainer = new DefaultDecorationModelInheritanceAssembler.URLContainer((String)null, baseUrl);
      if (decoration.getBannerLeft() != null) {
         this.resolveBannerPaths(decoration.getBannerLeft(), urlContainer);
      }

      if (decoration.getBannerRight() != null) {
         this.resolveBannerPaths(decoration.getBannerRight(), urlContainer);
      }

      Iterator i = decoration.getPoweredBy().iterator();

      while(i.hasNext()) {
         Logo logo = (Logo)i.next();
         this.resolveLogoPaths(logo, urlContainer);
      }

      if (decoration.getBody() != null) {
         i = decoration.getBody().getLinks().iterator();

         LinkItem linkItem;
         while(i.hasNext()) {
            linkItem = (LinkItem)i.next();
            this.resolveLinkItemPaths(linkItem, urlContainer);
         }

         i = decoration.getBody().getBreadcrumbs().iterator();

         while(i.hasNext()) {
            linkItem = (LinkItem)i.next();
            this.resolveLinkItemPaths(linkItem, urlContainer);
         }

         i = decoration.getBody().getMenus().iterator();

         while(i.hasNext()) {
            Menu menu = (Menu)i.next();
            this.resolveMenuPaths(menu.getItems(), urlContainer);
         }
      }

   }

   private void resolveBannerPaths(Banner banner, DefaultDecorationModelInheritanceAssembler.URLContainer urlContainer) {
      if (banner != null) {
         banner.setHref(this.convertPath(banner.getHref(), urlContainer));
         banner.setSrc(this.convertPath(banner.getSrc(), urlContainer));
      }

   }

   private void assembleCustomInheritance(DecorationModel child, DecorationModel parent) {
      if (child.getCustom() == null) {
         child.setCustom(parent.getCustom());
      } else {
         child.setCustom(Xpp3Dom.mergeXpp3Dom((Xpp3Dom)child.getCustom(), (Xpp3Dom)parent.getCustom()));
      }

   }

   private void assembleBodyInheritance(String name, DecorationModel child, DecorationModel parent, DefaultDecorationModelInheritanceAssembler.URLContainer urlContainer) {
      Body cBody = child.getBody();
      Body pBody = parent.getBody();
      if (cBody != null || pBody != null) {
         if (cBody == null) {
            cBody = new Body();
            child.setBody(cBody);
         }

         if (pBody == null) {
            pBody = new Body();
         }

         if (cBody.getHead() == null) {
            cBody.setHead(pBody.getHead());
         } else {
            cBody.setHead(Xpp3Dom.mergeXpp3Dom((Xpp3Dom)cBody.getHead(), (Xpp3Dom)pBody.getHead()));
         }

         cBody.setLinks(this.mergeLinkItemLists(cBody.getLinks(), pBody.getLinks(), urlContainer));
         if (cBody.getBreadcrumbs().isEmpty() && !pBody.getBreadcrumbs().isEmpty()) {
            LinkItem breadcrumb = new LinkItem();
            breadcrumb.setName(name);
            breadcrumb.setHref(urlContainer.getNewPath());
            cBody.getBreadcrumbs().add(breadcrumb);
         }

         cBody.setBreadcrumbs(this.mergeLinkItemLists(cBody.getBreadcrumbs(), pBody.getBreadcrumbs(), urlContainer));
         cBody.setMenus(this.mergeMenus(cBody.getMenus(), pBody.getMenus(), urlContainer));
      }

   }

   private List mergeMenus(List childMenus, List parentMenus, DefaultDecorationModelInheritanceAssembler.URLContainer urlContainer) {
      List menus = new ArrayList();
      Iterator it = childMenus.iterator();

      while(it.hasNext()) {
         Menu menu = (Menu)it.next();
         menus.add(menu);
      }

      int topCounter = 0;
      Iterator it = parentMenus.iterator();

      while(it.hasNext()) {
         Menu menu = (Menu)it.next();
         if ("top".equals(menu.getInherit())) {
            menus.add(topCounter, menu);
            ++topCounter;
            this.resolveMenuPaths(menu.getItems(), urlContainer);
         } else if ("bottom".equals(menu.getInherit())) {
            menus.add(menu);
            this.resolveMenuPaths(menu.getItems(), urlContainer);
         }
      }

      return menus;
   }

   private void resolveMenuPaths(List items, DefaultDecorationModelInheritanceAssembler.URLContainer urlContainer) {
      Iterator i = items.iterator();

      while(i.hasNext()) {
         MenuItem item = (MenuItem)i.next();
         this.resolveLinkItemPaths(item, urlContainer);
         this.resolveMenuPaths(item.getItems(), urlContainer);
      }

   }

   private void resolveLinkItemPaths(LinkItem item, DefaultDecorationModelInheritanceAssembler.URLContainer urlContainer) {
      if (StringUtils.isNotEmpty(item.getHref())) {
         String href = this.convertPath(item.getHref(), urlContainer);
         if (StringUtils.isNotEmpty(href)) {
            item.setHref(href);
         }
      } else {
         item.setHref(this.convertPath("", urlContainer));
      }

   }

   private void resolveLogoPaths(Logo logo, DefaultDecorationModelInheritanceAssembler.URLContainer urlContainer) {
      logo.setImg(this.convertPath(logo.getImg(), urlContainer));
      this.resolveLinkItemPaths(logo, urlContainer);
   }

   private List mergeLinkItemLists(List childList, List parentList, DefaultDecorationModelInheritanceAssembler.URLContainer urlContainer) {
      List items = new ArrayList();
      Iterator it = parentList.iterator();

      LinkItem item;
      while(it.hasNext()) {
         item = (LinkItem)it.next();
         this.resolveLinkItemPaths(item, urlContainer);
         if (!items.contains(item)) {
            items.add(item);
         }
      }

      it = childList.iterator();

      while(it.hasNext()) {
         item = (LinkItem)it.next();
         if (!items.contains(item)) {
            items.add(item);
         }
      }

      return items;
   }

   private List mergePoweredByLists(List childList, List parentList, DefaultDecorationModelInheritanceAssembler.URLContainer urlContainer) {
      List logos = new ArrayList();

      Iterator it;
      Logo logo;
      for(it = parentList.iterator(); it.hasNext(); this.resolveLogoPaths(logo, urlContainer)) {
         logo = (Logo)it.next();
         if (!logos.contains(logo)) {
            logos.add(logo);
         }
      }

      it = childList.iterator();

      while(it.hasNext()) {
         logo = (Logo)it.next();
         if (!logos.contains(logo)) {
            logos.add(logo);
         }
      }

      return logos;
   }

   private String convertPath(String relativePath, DefaultDecorationModelInheritanceAssembler.URLContainer urlContainer) {
      try {
         PathDescriptor oldPathDescriptor = new PathDescriptor(urlContainer.getOldPath(), relativePath);
         PathDescriptor newPathDescriptor = new PathDescriptor(urlContainer.getNewPath(), "");
         PathDescriptor relativePathDescriptor = PathUtils.convertPath(oldPathDescriptor, newPathDescriptor);
         return relativePathDescriptor.getLocation();
      } catch (MalformedURLException var6) {
         throw new RuntimeException("While converting Pathes:", var6);
      }
   }

   public final class URLContainer {
      private final String oldPath;
      private final String newPath;

      public URLContainer(String oldPath, String newPath) {
         this.oldPath = oldPath;
         this.newPath = newPath;
      }

      public String getNewPath() {
         return this.newPath;
      }

      public String getOldPath() {
         return this.oldPath;
      }
   }
}
