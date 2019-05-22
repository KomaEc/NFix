package org.jboss.util.naming;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;

public class ReadOnlyContext implements Context {
   private Context delegate;

   public ReadOnlyContext(Context delegate) {
      this.delegate = delegate;
   }

   public void close() throws NamingException {
      this.delegate.close();
   }

   public String composeName(String name, String prefix) throws NamingException {
      return this.delegate.composeName(name, prefix);
   }

   public Name composeName(Name name, Name prefix) throws NamingException {
      return this.delegate.composeName(name, prefix);
   }

   public String getNameInNamespace() throws NamingException {
      return this.delegate.getNameInNamespace();
   }

   public Hashtable getEnvironment() throws NamingException {
      return this.delegate.getEnvironment();
   }

   public Object lookup(String name) throws NamingException {
      return this.delegate.lookup(name);
   }

   public Object lookupLink(String name) throws NamingException {
      return this.delegate.lookupLink(name);
   }

   public Object lookup(Name name) throws NamingException {
      return this.delegate.lookup(name);
   }

   public Object lookupLink(Name name) throws NamingException {
      return this.delegate.lookupLink(name);
   }

   public NameParser getNameParser(String name) throws NamingException {
      return this.delegate.getNameParser(name);
   }

   public NameParser getNameParser(Name name) throws NamingException {
      return this.delegate.getNameParser(name);
   }

   public NamingEnumeration list(String name) throws NamingException {
      return this.delegate.list(name);
   }

   public NamingEnumeration listBindings(String name) throws NamingException {
      return this.delegate.listBindings(name);
   }

   public NamingEnumeration list(Name name) throws NamingException {
      return this.delegate.list(name);
   }

   public NamingEnumeration listBindings(Name name) throws NamingException {
      return this.delegate.listBindings(name);
   }

   public Object addToEnvironment(String propName, Object propVal) throws NamingException {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void bind(String name, Object obj) throws NamingException {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void bind(Name name, Object obj) throws NamingException {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public Context createSubcontext(String name) throws NamingException {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public Context createSubcontext(Name name) throws NamingException {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void destroySubcontext(String name) throws NamingException {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void destroySubcontext(Name name) throws NamingException {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public Object removeFromEnvironment(String propName) throws NamingException {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void rebind(String name, Object obj) throws NamingException {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void rebind(Name name, Object obj) throws NamingException {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void rename(String oldName, String newName) throws NamingException {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void rename(Name oldName, Name newName) throws NamingException {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void unbind(String name) throws NamingException {
      throw new OperationNotSupportedException("This is a read-only Context");
   }

   public void unbind(Name name) throws NamingException {
      throw new OperationNotSupportedException("This is a read-only Context");
   }
}
