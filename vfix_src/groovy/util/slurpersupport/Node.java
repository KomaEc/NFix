package groovy.util.slurpersupport;

import groovy.lang.Buildable;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.Writable;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

public class Node implements Writable {
   private final String name;
   private final Map attributes;
   private final Map attributeNamespaces;
   private final String namespaceURI;
   private final List children = new LinkedList();
   private final Stack replacementNodeStack = new Stack();

   public Node(Node parent, String name, Map attributes, Map attributeNamespaces, String namespaceURI) {
      this.name = name;
      this.attributes = attributes;
      this.attributeNamespaces = attributeNamespaces;
      this.namespaceURI = namespaceURI;
   }

   public String name() {
      return this.name;
   }

   public String namespaceURI() {
      return this.namespaceURI;
   }

   public Map attributes() {
      return this.attributes;
   }

   public List children() {
      return this.children;
   }

   public void addChild(Object child) {
      this.children.add(child);
   }

   public void replaceNode(final Closure replacementClosure, final GPathResult result) {
      this.replacementNodeStack.push(new ReplacementNode() {
         public void build(GroovyObject builder, Map namespaceMap, Map<String, String> namespaceTagHints) {
            Closure c = (Closure)replacementClosure.clone();
            Node.this.replacementNodeStack.pop();
            c.setDelegate(builder);
            c.call(new Object[]{result});
            Node.this.replacementNodeStack.push(this);
         }
      });
   }

   protected void replaceBody(Object newValue) {
      this.children.clear();
      this.children.add(newValue);
   }

   protected void appendNode(final Object newValue, final GPathResult result) {
      if (newValue instanceof Closure) {
         this.children.add(new ReplacementNode() {
            public void build(GroovyObject builder, Map namespaceMap, Map<String, String> namespaceTagHints) {
               Closure c = (Closure)((Closure)newValue).clone();
               c.setDelegate(builder);
               c.call(new Object[]{result});
            }
         });
      } else {
         this.children.add(newValue);
      }

   }

   public String text() {
      StringBuffer buff = new StringBuffer();
      Iterator iter = this.children.iterator();

      while(iter.hasNext()) {
         Object child = iter.next();
         if (child instanceof Node) {
            buff.append(((Node)child).text());
         } else {
            buff.append(child);
         }
      }

      return buff.toString();
   }

   public Iterator childNodes() {
      return new Iterator() {
         private final Iterator iter;
         private Object nextElementNodes;

         {
            this.iter = Node.this.children.iterator();
            this.nextElementNodes = this.getNextElementNodes();
         }

         public boolean hasNext() {
            return this.nextElementNodes != null;
         }

         public Object next() {
            Object var1;
            try {
               var1 = this.nextElementNodes;
            } finally {
               this.nextElementNodes = this.getNextElementNodes();
            }

            return var1;
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }

         private Object getNextElementNodes() {
            while(true) {
               if (this.iter.hasNext()) {
                  Object node = this.iter.next();
                  if (!(node instanceof Node)) {
                     continue;
                  }

                  return node;
               }

               return null;
            }
         }
      };
   }

   public Writer writeTo(Writer out) throws IOException {
      if (this.replacementNodeStack.empty()) {
         Iterator iter = this.children.iterator();

         while(iter.hasNext()) {
            Object child = iter.next();
            if (child instanceof Writable) {
               ((Writable)child).writeTo(out);
            } else {
               out.write(child.toString());
            }
         }

         return out;
      } else {
         return ((Writable)this.replacementNodeStack.peek()).writeTo(out);
      }
   }

   public void build(final GroovyObject builder, final Map namespaceMap, final Map<String, String> namespaceTagHints) {
      if (this.replacementNodeStack.empty()) {
         Closure rest = new Closure((Object)null) {
            public Object doCall(Object o) {
               Node.this.buildChildren(builder, namespaceMap, namespaceTagHints);
               return null;
            }
         };
         if (this.namespaceURI.length() == 0 && this.attributeNamespaces.isEmpty()) {
            builder.invokeMethod(this.name, new Object[]{this.attributes, rest});
         } else {
            List newTags = new LinkedList();
            builder.getProperty("mkp");
            List namespaces = (List)builder.invokeMethod("getNamespaces", new Object[0]);
            Map current = (Map)namespaces.get(0);
            Map pending = (Map)namespaces.get(1);
            if (this.attributeNamespaces.isEmpty()) {
               builder.getProperty(getTagFor(this.namespaceURI, current, pending, namespaceMap, namespaceTagHints, newTags, builder));
               builder.invokeMethod(this.name, new Object[]{this.attributes, rest});
            } else {
               Map attributesWithNamespaces = new HashMap(this.attributes);
               Iterator attrs = this.attributes.keySet().iterator();

               while(attrs.hasNext()) {
                  Object key = attrs.next();
                  Object attributeNamespaceURI = this.attributeNamespaces.get(key);
                  if (attributeNamespaceURI != null) {
                     attributesWithNamespaces.put(getTagFor(attributeNamespaceURI, current, pending, namespaceMap, namespaceTagHints, newTags, builder) + "$" + key, attributesWithNamespaces.remove(key));
                  }
               }

               builder.getProperty(getTagFor(this.namespaceURI, current, pending, namespaceMap, namespaceTagHints, newTags, builder));
               builder.invokeMethod(this.name, new Object[]{attributesWithNamespaces, rest});
            }

            if (!newTags.isEmpty()) {
               Iterator iter = newTags.iterator();

               do {
                  pending.remove(iter.next());
               } while(iter.hasNext());
            }
         }
      } else {
         ((ReplacementNode)this.replacementNodeStack.peek()).build(builder, namespaceMap, namespaceTagHints);
      }

   }

   private static String getTagFor(Object namespaceURI, Map current, Map pending, Map local, Map tagHints, List newTags, GroovyObject builder) {
      String tag = findNamespaceTag(pending, namespaceURI);
      if (tag == null) {
         tag = findNamespaceTag(current, namespaceURI);
         if (tag == null) {
            tag = findNamespaceTag(local, namespaceURI);
            if (tag == null || tag.length() == 0) {
               tag = findNamespaceTag(tagHints, namespaceURI);
            }

            if (tag == null || tag.length() == 0) {
               int var8 = 0;

               do {
                  String posibleTag = "tag" + var8++;
                  if (!pending.containsKey(posibleTag) && !current.containsKey(posibleTag) && !local.containsKey(posibleTag)) {
                     tag = posibleTag;
                  }
               } while(tag == null);
            }

            Map newNamespace = new HashMap();
            newNamespace.put(tag, namespaceURI);
            builder.getProperty("mkp");
            builder.invokeMethod("declareNamespace", new Object[]{newNamespace});
            newTags.add(tag);
         }
      }

      return tag;
   }

   private static String findNamespaceTag(Map tagMap, Object namespaceURI) {
      if (tagMap.containsValue(namespaceURI)) {
         Iterator entries = tagMap.entrySet().iterator();

         while(entries.hasNext()) {
            Entry entry = (Entry)entries.next();
            if (namespaceURI.equals(entry.getValue())) {
               return (String)entry.getKey();
            }
         }
      }

      return null;
   }

   private void buildChildren(GroovyObject builder, Map namespaceMap, Map<String, String> namespaceTagHints) {
      Iterator iter = this.children.iterator();

      while(iter.hasNext()) {
         Object child = iter.next();
         if (child instanceof Node) {
            ((Node)child).build(builder, namespaceMap, namespaceTagHints);
         } else if (child instanceof Buildable) {
            ((Buildable)child).build(builder);
         } else {
            builder.getProperty("mkp");
            builder.invokeMethod("yield", new Object[]{child});
         }
      }

   }
}
