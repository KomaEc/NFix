package com.gzoltar.shaded.org.jacoco.report.internal.html;

import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.xml.XMLElement;
import java.io.IOException;
import java.io.Writer;

public class HTMLElement extends XMLElement {
   protected HTMLElement(Writer writer, String name) {
      super(writer, name);
   }

   public HTMLElement element(String name) throws IOException {
      HTMLElement element = new HTMLElement(this.writer, name);
      this.addChildElement(element);
      return element;
   }

   private void classattr(String classattr) throws IOException {
      this.attr("class", classattr);
   }

   public HTMLElement meta(String httpequivattr, String contentattr) throws IOException {
      HTMLElement meta = this.element("meta");
      meta.attr("http-equiv", httpequivattr);
      meta.attr("content", contentattr);
      return meta;
   }

   public HTMLElement link(String relattr, String hrefattr, String typeattr) throws IOException {
      HTMLElement link = this.element("link");
      link.attr("rel", relattr);
      link.attr("href", hrefattr);
      link.attr("type", typeattr);
      return link;
   }

   public HTMLElement title() throws IOException {
      return this.element("title");
   }

   public HTMLElement h1() throws IOException {
      return this.element("h1");
   }

   public HTMLElement p() throws IOException {
      return this.element("p");
   }

   public HTMLElement span() throws IOException {
      return this.element("span");
   }

   public HTMLElement span(String classattr) throws IOException {
      HTMLElement span = this.span();
      span.classattr(classattr);
      return span;
   }

   public HTMLElement span(String classattr, String idattr) throws IOException {
      HTMLElement span = this.span(classattr);
      span.attr("id", idattr);
      return span;
   }

   public HTMLElement div(String classattr) throws IOException {
      HTMLElement div = this.element("div");
      div.classattr(classattr);
      return div;
   }

   public HTMLElement code() throws IOException {
      return this.element("code");
   }

   public HTMLElement pre(String classattr) throws IOException {
      HTMLElement pre = this.element("pre");
      pre.classattr(classattr);
      return pre;
   }

   public HTMLElement a(String hrefattr) throws IOException {
      HTMLElement a = this.element("a");
      a.attr("href", hrefattr);
      return a;
   }

   public HTMLElement a(String hrefattr, String classattr) throws IOException {
      HTMLElement a = this.a(hrefattr);
      a.classattr(classattr);
      return a;
   }

   public HTMLElement a(ILinkable linkable, ReportOutputFolder base) throws IOException {
      String link = linkable.getLink(base);
      HTMLElement a;
      if (link == null) {
         a = this.span(linkable.getLinkStyle());
      } else {
         a = this.a(link, linkable.getLinkStyle());
      }

      a.text(linkable.getLinkLabel());
      return a;
   }

   public HTMLElement table(String classattr) throws IOException {
      HTMLElement table = this.element("table");
      table.classattr(classattr);
      table.attr("cellspacing", "0");
      return table;
   }

   public HTMLElement thead() throws IOException {
      return this.element("thead");
   }

   public HTMLElement tfoot() throws IOException {
      return this.element("tfoot");
   }

   public HTMLElement tbody() throws IOException {
      return this.element("tbody");
   }

   public HTMLElement tr() throws IOException {
      return this.element("tr");
   }

   public HTMLElement td() throws IOException {
      return this.element("td");
   }

   public HTMLElement td(String classattr) throws IOException {
      HTMLElement td = this.td();
      td.classattr(classattr);
      return td;
   }

   public void img(String srcattr, int widthattr, int heightattr, String titleattr) throws IOException {
      HTMLElement img = this.element("img");
      img.attr("src", srcattr);
      img.attr("width", widthattr);
      img.attr("height", heightattr);
      img.attr("title", titleattr);
      img.attr("alt", titleattr);
      img.close();
   }

   public void script(String typeattr, String srcattr) throws IOException {
      HTMLElement script = this.element("script");
      script.attr("type", typeattr);
      script.attr("src", srcattr);
      script.text("");
      script.close();
   }
}
