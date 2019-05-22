package org.codehaus.plexus.configuration.xml;

import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class XmlPlexusConfiguration implements PlexusConfiguration {
   private Xpp3Dom dom;

   public XmlPlexusConfiguration(String name) {
      this.dom = new Xpp3Dom(name);
   }

   public XmlPlexusConfiguration(Xpp3Dom dom) {
      this.dom = dom;
   }

   public Xpp3Dom getXpp3Dom() {
      return this.dom;
   }

   public String getName() {
      return this.dom.getName();
   }

   public String getValue() {
      return this.dom.getValue();
   }

   public String getValue(String defaultValue) {
      String value = this.dom.getValue();
      if (value == null) {
         value = defaultValue;
      }

      return value;
   }

   public void setValue(String value) {
      this.dom.setValue(value);
   }

   public void setAttribute(String name, String value) {
      this.dom.setAttribute(name, value);
   }

   public String getAttribute(String name, String defaultValue) {
      String attribute = this.getAttribute(name);
      if (attribute == null) {
         attribute = defaultValue;
      }

      return attribute;
   }

   public String getAttribute(String name) {
      return this.dom.getAttribute(name);
   }

   public String[] getAttributeNames() {
      return this.dom.getAttributeNames();
   }

   public PlexusConfiguration getChild(String name) {
      return this.getChild(name, true);
   }

   public PlexusConfiguration getChild(int i) {
      return new XmlPlexusConfiguration(this.dom.getChild(i));
   }

   public PlexusConfiguration getChild(String name, boolean createChild) {
      Xpp3Dom child = this.dom.getChild(name);
      if (child == null) {
         if (!createChild) {
            return null;
         }

         child = new Xpp3Dom(name);
         this.dom.addChild(child);
      }

      return new XmlPlexusConfiguration(child);
   }

   public PlexusConfiguration[] getChildren() {
      Xpp3Dom[] doms = this.dom.getChildren();
      PlexusConfiguration[] children = new XmlPlexusConfiguration[doms.length];

      for(int i = 0; i < children.length; ++i) {
         children[i] = new XmlPlexusConfiguration(doms[i]);
      }

      return children;
   }

   public PlexusConfiguration[] getChildren(String name) {
      Xpp3Dom[] doms = this.dom.getChildren(name);
      PlexusConfiguration[] children = new XmlPlexusConfiguration[doms.length];

      for(int i = 0; i < children.length; ++i) {
         children[i] = new XmlPlexusConfiguration(doms[i]);
      }

      return children;
   }

   public void addChild(PlexusConfiguration configuration) {
      this.dom.addChild(((XmlPlexusConfiguration)configuration).getXpp3Dom());
   }

   public void addAllChildren(PlexusConfiguration other) {
      PlexusConfiguration[] children = other.getChildren();

      for(int i = 0; i < children.length; ++i) {
         this.addChild(children[i]);
      }

   }

   public int getChildCount() {
      return this.dom.getChildCount();
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      int depth = 0;
      this.display(this, sb, depth);
      return sb.toString();
   }

   private void display(PlexusConfiguration c, StringBuffer sb, int depth) {
      sb.append(this.indent(depth)).append('<').append(c.getName()).append('>').append('\n');
      int count = c.getChildCount();

      for(int i = 0; i < count; ++i) {
         PlexusConfiguration child = c.getChild(i);
         int childCount = child.getChildCount();
         ++depth;
         if (childCount > 0) {
            this.display(child, sb, depth);
         } else {
            String value = child.getValue((String)null);
            if (value != null) {
               sb.append(this.indent(depth)).append('<').append(child.getName());
               this.attributes(child, sb);
               sb.append('>').append(child.getValue((String)null)).append('<').append('/').append(child.getName()).append('>').append('\n');
            } else {
               sb.append(this.indent(depth)).append('<').append(child.getName());
               this.attributes(child, sb);
               sb.append('/').append('>').append("\n");
            }
         }

         --depth;
      }

      sb.append(this.indent(depth)).append('<').append('/').append(c.getName()).append('>').append('\n');
   }

   private void attributes(PlexusConfiguration c, StringBuffer sb) {
      String[] names = c.getAttributeNames();

      for(int i = 0; i < names.length; ++i) {
         sb.append(' ').append(names[i]).append('=').append('"').append(c.getAttribute(names[i], (String)null)).append('"');
      }

   }

   private String indent(int depth) {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < depth; ++i) {
         sb.append(' ');
      }

      return sb.toString();
   }
}
