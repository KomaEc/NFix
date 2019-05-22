package org.xmlpull.v1.builder.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.builder.XmlBuilderException;
import org.xmlpull.v1.builder.XmlComment;
import org.xmlpull.v1.builder.XmlDoctype;
import org.xmlpull.v1.builder.XmlDocument;
import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlNamespace;
import org.xmlpull.v1.builder.XmlNotation;
import org.xmlpull.v1.builder.XmlProcessingInstruction;

public class XmlDocumentImpl implements XmlDocument {
   private List children = new ArrayList();
   private XmlElement root;
   private String version;
   private Boolean standalone;
   private String characterEncoding;

   public XmlDocumentImpl(String version, Boolean standalone, String characterEncoding) {
      this.version = version;
      this.standalone = standalone;
      this.characterEncoding = characterEncoding;
   }

   public String getVersion() {
      return this.version;
   }

   public Boolean isStandalone() {
      return this.standalone;
   }

   public String getCharacterEncodingScheme() {
      return this.characterEncoding;
   }

   public void setCharacterEncodingScheme(String characterEncoding) {
      this.characterEncoding = characterEncoding;
   }

   public XmlProcessingInstruction newProcessingInstruction(String target, String content) {
      throw new XmlBuilderException("not implemented");
   }

   public XmlProcessingInstruction addProcessingInstruction(String target, String content) {
      throw new XmlBuilderException("not implemented");
   }

   public Iterator children() {
      return this.children.iterator();
   }

   public void remocveAllUnparsedEntities() {
      throw new XmlBuilderException("not implemented");
   }

   public void setDocumentElement(XmlElement rootElement) {
      boolean replaced = false;

      for(int i = 0; i < this.children.size(); ++i) {
         Object element = this.children.get(i);
         if (element == this.root) {
            this.children.set(i, rootElement);
            replaced = true;
         }
      }

      if (!replaced) {
         this.children.add(rootElement);
      }

      this.root = rootElement;
      rootElement.setParent(this);
   }

   public void insertChild(int pos, Object child) {
      throw new XmlBuilderException("not implemented");
   }

   public XmlComment addComment(String content) {
      throw new XmlBuilderException("not implemented");
   }

   public XmlDoctype newDoctype(String systemIdentifier, String publicIdentifier) {
      throw new XmlBuilderException("not implemented");
   }

   public Iterator unparsedEntities() {
      throw new XmlBuilderException("not implemented");
   }

   public void removeAllChildren() {
      throw new XmlBuilderException("not implemented");
   }

   public XmlComment newComment(String content) {
      throw new XmlBuilderException("not implemented");
   }

   public void removeAllNotations() {
      throw new XmlBuilderException("not implemented");
   }

   public XmlDoctype addDoctype(String systemIdentifier, String publicIdentifier) {
      throw new XmlBuilderException("not implemented");
   }

   public void addChild(Object child) {
      throw new XmlBuilderException("not implemented");
   }

   public XmlNotation addNotation(String name, String systemIdentifier, String publicIdentifier, String declarationBaseUri) {
      throw new XmlBuilderException("not implemented");
   }

   public String getBaseUri() {
      throw new XmlBuilderException("not implemented");
   }

   public Iterator notations() {
      throw new XmlBuilderException("not implemented");
   }

   public XmlElement addDocumentElement(String name) {
      return this.addDocumentElement((XmlNamespace)null, name);
   }

   public XmlElement addDocumentElement(XmlNamespace namespace, String name) {
      XmlElement el = new XmlElementImpl(namespace, name);
      if (this.getDocumentElement() != null) {
         throw new XmlBuilderException("document already has root element");
      } else {
         this.setDocumentElement(el);
         return el;
      }
   }

   public boolean isAllDeclarationsProcessed() {
      throw new XmlBuilderException("not implemented");
   }

   public XmlElement getDocumentElement() {
      return this.root;
   }
}
