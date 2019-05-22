package org.apache.tools.ant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.tools.ant.util.CollectionUtils;
import org.xml.sax.AttributeList;
import org.xml.sax.helpers.AttributeListImpl;

public class RuntimeConfigurable implements Serializable {
   private static final Hashtable EMPTY_HASHTABLE = new Hashtable(0);
   private String elementTag = null;
   private List children = null;
   private transient Object wrappedObject = null;
   private transient IntrospectionHelper.Creator creator;
   /** @deprecated */
   private transient AttributeList attributes;
   private List attributeNames = null;
   private Map attributeMap = null;
   private StringBuffer characters = null;
   private boolean proxyConfigured = false;
   private String polyType = null;
   private String id = null;

   public RuntimeConfigurable(Object proxy, String elementTag) {
      this.setProxy(proxy);
      this.setElementTag(elementTag);
      if (proxy instanceof Task) {
         ((Task)proxy).setRuntimeConfigurableWrapper(this);
      }

   }

   public synchronized void setProxy(Object proxy) {
      this.wrappedObject = proxy;
      this.proxyConfigured = false;
   }

   synchronized void setCreator(IntrospectionHelper.Creator creator) {
      this.creator = creator;
   }

   public synchronized Object getProxy() {
      return this.wrappedObject;
   }

   public synchronized String getId() {
      return this.id;
   }

   public synchronized String getPolyType() {
      return this.polyType;
   }

   public synchronized void setPolyType(String polyType) {
      this.polyType = polyType;
   }

   /** @deprecated */
   public synchronized void setAttributes(AttributeList attributes) {
      this.attributes = new AttributeListImpl(attributes);

      for(int i = 0; i < attributes.getLength(); ++i) {
         this.setAttribute(attributes.getName(i), attributes.getValue(i));
      }

   }

   public synchronized void setAttribute(String name, String value) {
      if (name.equalsIgnoreCase("ant-type")) {
         this.polyType = value;
      } else {
         if (this.attributeNames == null) {
            this.attributeNames = new ArrayList();
            this.attributeMap = new HashMap();
         }

         if (name.toLowerCase(Locale.US).equals("refid")) {
            this.attributeNames.add(0, name);
         } else {
            this.attributeNames.add(name);
         }

         this.attributeMap.put(name, value);
         if (name.equals("id")) {
            this.id = value;
         }
      }

   }

   public synchronized void removeAttribute(String name) {
      this.attributeNames.remove(name);
      this.attributeMap.remove(name);
   }

   public synchronized Hashtable getAttributeMap() {
      return this.attributeMap == null ? EMPTY_HASHTABLE : new Hashtable(this.attributeMap);
   }

   /** @deprecated */
   public synchronized AttributeList getAttributes() {
      return this.attributes;
   }

   public synchronized void addChild(RuntimeConfigurable child) {
      this.children = (List)(this.children == null ? new ArrayList() : this.children);
      this.children.add(child);
   }

   synchronized RuntimeConfigurable getChild(int index) {
      return (RuntimeConfigurable)this.children.get(index);
   }

   public synchronized Enumeration getChildren() {
      return (Enumeration)(this.children == null ? new CollectionUtils.EmptyEnumeration() : Collections.enumeration(this.children));
   }

   public synchronized void addText(String data) {
      if (data.length() != 0) {
         this.characters = this.characters == null ? new StringBuffer(data) : this.characters.append(data);
      }
   }

   public synchronized void addText(char[] buf, int start, int count) {
      if (count != 0) {
         this.characters = (this.characters == null ? new StringBuffer(count) : this.characters).append(buf, start, count);
      }
   }

   public synchronized StringBuffer getText() {
      return this.characters == null ? new StringBuffer(0) : this.characters;
   }

   public synchronized void setElementTag(String elementTag) {
      this.elementTag = elementTag;
   }

   public synchronized String getElementTag() {
      return this.elementTag;
   }

   public void maybeConfigure(Project p) throws BuildException {
      this.maybeConfigure(p, true);
   }

   public synchronized void maybeConfigure(Project p, boolean configureChildren) throws BuildException {
      if (!this.proxyConfigured) {
         Object target = this.wrappedObject instanceof TypeAdapter ? ((TypeAdapter)this.wrappedObject).getProxy() : this.wrappedObject;
         IntrospectionHelper ih = IntrospectionHelper.getHelper(p, target.getClass());
         if (this.attributeNames != null) {
            for(int i = 0; i < this.attributeNames.size(); ++i) {
               String name = (String)this.attributeNames.get(i);
               String value = (String)this.attributeMap.get(name);
               value = p.replaceProperties(value);

               try {
                  ih.setAttribute(p, target, name, value);
               } catch (UnsupportedAttributeException var9) {
                  if (!name.equals("id")) {
                     if (this.getElementTag() == null) {
                        throw var9;
                     }

                     throw new BuildException(this.getElementTag() + " doesn't support the \"" + var9.getAttribute() + "\" attribute", var9);
                  }
               } catch (BuildException var10) {
                  if (!name.equals("id")) {
                     throw var10;
                  }
               }
            }
         }

         if (this.characters != null) {
            ProjectHelper.addText(p, this.wrappedObject, this.characters.substring(0));
         }

         if (this.id != null) {
            p.addReference(this.id, this.wrappedObject);
         }

         this.proxyConfigured = true;
      }
   }

   public void reconfigure(Project p) {
      this.proxyConfigured = false;
      this.maybeConfigure(p);
   }

   public void applyPreSet(RuntimeConfigurable r) {
      if (r.attributeMap != null) {
         Iterator i = r.attributeMap.keySet().iterator();

         label44:
         while(true) {
            String name;
            do {
               if (!i.hasNext()) {
                  break label44;
               }

               name = (String)i.next();
            } while(this.attributeMap != null && this.attributeMap.get(name) != null);

            this.setAttribute(name, (String)r.attributeMap.get(name));
         }
      }

      this.polyType = this.polyType == null ? r.polyType : this.polyType;
      if (r.children != null) {
         List newChildren = new ArrayList();
         newChildren.addAll(r.children);
         if (this.children != null) {
            newChildren.addAll(this.children);
         }

         this.children = newChildren;
      }

      if (r.characters != null && (this.characters == null || this.characters.toString().trim().length() == 0)) {
         this.characters = new StringBuffer(r.characters.toString());
      }

   }
}
