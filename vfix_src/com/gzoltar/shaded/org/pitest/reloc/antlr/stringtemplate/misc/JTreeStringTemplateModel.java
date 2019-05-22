package com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.misc;

import com.gzoltar.shaded.org.pitest.reloc.antlr.common.collections.AST;
import com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.StringTemplate;
import com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.StringTemplateGroup;
import com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.language.ASTExpr;
import com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.language.ConditionalExpr;
import com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.language.Expr;
import com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.language.StringRef;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class JTreeStringTemplateModel implements TreeModel {
   static Map classNameToWrapperMap = new HashMap();
   JTreeStringTemplateModel.Wrapper root = null;

   public static Object wrap(Object o) {
      Object wrappedObject = o;
      Class wrapperClass = null;

      try {
         wrapperClass = (Class)classNameToWrapperMap.get(o.getClass().getName());
         Constructor ctor = wrapperClass.getConstructor(Object.class);
         wrappedObject = ctor.newInstance(o);
      } catch (Exception var4) {
      }

      return wrappedObject;
   }

   public JTreeStringTemplateModel(StringTemplate st) {
      if (st == null) {
         throw new IllegalArgumentException("root is null");
      } else {
         this.root = new JTreeStringTemplateModel.StringTemplateWrapper(st);
      }
   }

   public void addTreeModelListener(TreeModelListener l) {
   }

   public Object getChild(Object parent, int index) {
      return parent == null ? null : ((JTreeStringTemplateModel.Wrapper)parent).getChild(parent, index);
   }

   public int getChildCount(Object parent) {
      if (parent == null) {
         throw new IllegalArgumentException("root is null");
      } else {
         return ((JTreeStringTemplateModel.Wrapper)parent).getChildCount(parent);
      }
   }

   public int getIndexOfChild(Object parent, Object child) {
      if (parent != null && child != null) {
         return ((JTreeStringTemplateModel.Wrapper)parent).getIndexOfChild(parent, child);
      } else {
         throw new IllegalArgumentException("root or child is null");
      }
   }

   public Object getRoot() {
      return this.root;
   }

   public boolean isLeaf(Object node) {
      if (node == null) {
         throw new IllegalArgumentException("node is null");
      } else {
         return node instanceof JTreeStringTemplateModel.Wrapper ? ((JTreeStringTemplateModel.Wrapper)node).isLeaf(node) : true;
      }
   }

   public void removeTreeModelListener(TreeModelListener l) {
   }

   public void valueForPathChanged(TreePath path, Object newValue) {
      System.out.println("heh, who is calling this mystery method?");
   }

   static {
      classNameToWrapperMap.put("com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.StringTemplate", JTreeStringTemplateModel.StringTemplateWrapper.class);
      classNameToWrapperMap.put("com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.language.ASTExpr", JTreeStringTemplateModel.ExprWrapper.class);
      classNameToWrapperMap.put("java.util.Hashtable", JTreeStringTemplateModel.HashMapWrapper.class);
      classNameToWrapperMap.put("java.util.ArrayList", JTreeStringTemplateModel.ListWrapper.class);
      classNameToWrapperMap.put("java.util.Vector", JTreeStringTemplateModel.ListWrapper.class);
   }

   static class HashMapWrapper extends JTreeStringTemplateModel.Wrapper {
      HashMap table;

      public HashMapWrapper(Object o) {
         this.table = (HashMap)o;
      }

      public Object getWrappedObject() {
         return this.table;
      }

      public Object getChild(Object parent, int index) {
         List attributes = this.getTableAsListOfKeys();
         String key = (String)attributes.get(index);
         Object attr = this.table.get(key);
         Object wrappedAttr = JTreeStringTemplateModel.wrap(attr);
         return new JTreeStringTemplateModel.MapEntryWrapper(key, wrappedAttr);
      }

      public int getChildCount(Object parent) {
         List attributes = this.getTableAsListOfKeys();
         return attributes.size();
      }

      public int getIndexOfChild(Object parent, Object child) {
         List attributes = this.getTableAsListOfKeys();
         return attributes.indexOf(child);
      }

      public boolean isLeaf(Object node) {
         return false;
      }

      public String toString() {
         return "attributes";
      }

      private List getTableAsListOfKeys() {
         if (this.table == null) {
            return new LinkedList();
         } else {
            Set keys = this.table.keySet();
            List v = new LinkedList();
            Iterator itr = keys.iterator();

            while(itr.hasNext()) {
               String attributeName = (String)itr.next();
               v.add(attributeName);
            }

            return v;
         }
      }
   }

   static class MapEntryWrapper extends JTreeStringTemplateModel.Wrapper {
      Object key;
      Object value;

      public MapEntryWrapper(Object key, Object value) {
         this.key = key;
         this.value = value;
      }

      public Object getWrappedObject() {
         return JTreeStringTemplateModel.wrap(this.value);
      }

      public int getChildCount(Object parent) {
         return this.value instanceof JTreeStringTemplateModel.Wrapper ? ((JTreeStringTemplateModel.Wrapper)this.value).getChildCount(this.value) : 1;
      }

      public int getIndexOfChild(Object parent, Object child) {
         return this.value instanceof JTreeStringTemplateModel.Wrapper ? ((JTreeStringTemplateModel.Wrapper)this.value).getIndexOfChild(this.value, child) : 0;
      }

      public Object getChild(Object parent, int index) {
         return this.value instanceof JTreeStringTemplateModel.Wrapper ? ((JTreeStringTemplateModel.Wrapper)this.value).getChild(this.value, index) : this.value;
      }

      public boolean isLeaf(Object node) {
         return false;
      }

      public String toString() {
         return this.key.toString();
      }
   }

   static class ListWrapper extends JTreeStringTemplateModel.Wrapper {
      List v = null;

      public ListWrapper(Object o) {
         this.v = (List)o;
      }

      public int getChildCount(Object parent) {
         return this.v.size();
      }

      public int getIndexOfChild(Object parent, Object child) {
         if (child instanceof JTreeStringTemplateModel.Wrapper) {
            child = ((JTreeStringTemplateModel.Wrapper)child).getWrappedObject();
         }

         return this.v.indexOf(child);
      }

      public Object getChild(Object parent, int index) {
         return this.v.get(index);
      }

      public Object getWrappedObject() {
         return this.v;
      }

      public boolean isLeaf(Object node) {
         return false;
      }
   }

   static class ExprWrapper extends JTreeStringTemplateModel.Wrapper {
      Expr expr = null;

      public ExprWrapper(Object o) {
         this.expr = (Expr)o;
      }

      public Expr getExpr() {
         return this.expr;
      }

      public Object getWrappedObject() {
         return this.expr;
      }

      public Object getChild(Object parent, int index) {
         Expr expr = ((JTreeStringTemplateModel.ExprWrapper)parent).getExpr();
         if (expr instanceof ConditionalExpr) {
            return new JTreeStringTemplateModel.StringTemplateWrapper(((ConditionalExpr)expr).getSubtemplate());
         } else {
            if (expr instanceof ASTExpr) {
               ASTExpr astExpr = (ASTExpr)expr;
               AST root = astExpr.getAST();
               if (root.getType() == 7) {
                  switch(index) {
                  case 0:
                     return root.getFirstChild().getNextSibling().toStringList();
                  case 1:
                     String templateName = root.getFirstChild().getText();
                     StringTemplate enclosingST = expr.getEnclosingTemplate();
                     StringTemplateGroup group = enclosingST.getGroup();
                     StringTemplate embedded = group.getEmbeddedInstanceOf(enclosingST, templateName);
                     return new JTreeStringTemplateModel.StringTemplateWrapper(embedded);
                  }
               }
            }

            return "<invalid>";
         }
      }

      public int getChildCount(Object parent) {
         if (this.expr instanceof ConditionalExpr) {
            return 1;
         } else {
            AST tree = ((ASTExpr)this.expr).getAST();
            return tree.getType() == 7 ? 2 : 0;
         }
      }

      public int getIndexOfChild(Object parent, Object child) {
         return this.expr instanceof ConditionalExpr ? 0 : -1;
      }

      public boolean isLeaf(Object node) {
         if (this.expr instanceof ConditionalExpr) {
            return false;
         } else {
            if (this.expr instanceof ASTExpr) {
               AST tree = ((ASTExpr)this.expr).getAST();
               if (tree.getType() == 7) {
                  return false;
               }
            }

            return true;
         }
      }

      public String toString() {
         if (this.expr instanceof ASTExpr) {
            AST tree = ((ASTExpr)this.expr).getAST();
            return tree.getType() == 7 ? "$include$" : "$" + ((ASTExpr)this.expr).getAST().toStringList() + "$";
         } else {
            return this.expr instanceof StringRef ? this.expr.toString() : "<invalid node type>";
         }
      }
   }

   static class StringTemplateWrapper extends JTreeStringTemplateModel.Wrapper {
      StringTemplate st = null;

      public StringTemplateWrapper(Object o) {
         this.st = (StringTemplate)o;
      }

      public Object getWrappedObject() {
         return this.getStringTemplate();
      }

      public StringTemplate getStringTemplate() {
         return this.st;
      }

      public Object getChild(Object parent, int index) {
         StringTemplate st = ((JTreeStringTemplateModel.StringTemplateWrapper)parent).getStringTemplate();
         if (index == 0) {
            return new JTreeStringTemplateModel.HashMapWrapper(st.getAttributes());
         } else {
            Expr chunk = (Expr)st.getChunks().get(index - 1);
            return chunk instanceof StringRef ? chunk : new JTreeStringTemplateModel.ExprWrapper(chunk);
         }
      }

      public int getChildCount(Object parent) {
         return this.st.getChunks().size() + 1;
      }

      public int getIndexOfChild(Object parent, Object child) {
         if (child instanceof JTreeStringTemplateModel.Wrapper) {
            child = ((JTreeStringTemplateModel.Wrapper)child).getWrappedObject();
         }

         int index = this.st.getChunks().indexOf(child) + 1;
         return index;
      }

      public boolean isLeaf(Object node) {
         return false;
      }

      public String toString() {
         return this.st == null ? "<invalid template>" : this.st.getName();
      }
   }

   abstract static class Wrapper {
      public abstract int getChildCount(Object var1);

      public abstract int getIndexOfChild(Object var1, Object var2);

      public abstract Object getChild(Object var1, int var2);

      public abstract Object getWrappedObject();

      public boolean isLeaf(Object node) {
         return true;
      }
   }
}
