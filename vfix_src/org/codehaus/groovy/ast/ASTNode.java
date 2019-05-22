package org.codehaus.groovy.ast;

import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.util.ListHashMap;

public class ASTNode {
   private int lineNumber = -1;
   private int columnNumber = -1;
   private int lastLineNumber = -1;
   private int lastColumnNumber = -1;
   private ListHashMap metaDataMap = new ListHashMap();

   public void visit(GroovyCodeVisitor visitor) {
      throw new RuntimeException("No visit() method implemented for class: " + this.getClass().getName());
   }

   public String getText() {
      return "<not implemented yet for class: " + this.getClass().getName() + ">";
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public void setLineNumber(int lineNumber) {
      this.lineNumber = lineNumber;
   }

   public int getColumnNumber() {
      return this.columnNumber;
   }

   public void setColumnNumber(int columnNumber) {
      this.columnNumber = columnNumber;
   }

   public int getLastLineNumber() {
      return this.lastLineNumber;
   }

   public void setLastLineNumber(int lastLineNumber) {
      this.lastLineNumber = lastLineNumber;
   }

   public int getLastColumnNumber() {
      return this.lastColumnNumber;
   }

   public void setLastColumnNumber(int lastColumnNumber) {
      this.lastColumnNumber = lastColumnNumber;
   }

   public void setSourcePosition(ASTNode node) {
      this.columnNumber = node.getColumnNumber();
      this.lastLineNumber = node.getLastLineNumber();
      this.lastColumnNumber = node.getLastColumnNumber();
      this.lineNumber = node.getLineNumber();
   }

   public Object getNodeMetaData(Object key) {
      return this.metaDataMap.get(key);
   }

   public void copyNodeMetaData(ASTNode other) {
      this.metaDataMap.putAll(other.metaDataMap);
   }

   public void setNodeMetaData(Object key, Object value) {
      if (key == null) {
         throw new GroovyBugError("Tried to set meta data with null key on " + this + ".");
      } else {
         Object old = this.metaDataMap.put(key, value);
         if (old != null) {
            throw new GroovyBugError("Tried to overwrite existing meta data " + this + ".");
         }
      }
   }

   public void removeNodeMetaData(Object key) {
      if (key == null) {
         throw new GroovyBugError("Tried to remove meta data with null key.");
      } else {
         this.metaDataMap.remove(key);
         if (this.metaDataMap.size() == 0) {
            this.metaDataMap = null;
         }

      }
   }
}
