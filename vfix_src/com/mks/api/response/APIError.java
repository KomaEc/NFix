package com.mks.api.response;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class APIError extends Error implements FieldContainer {
   protected String message;
   protected List fields;
   protected boolean showStackTrace;
   protected Throwable cause;

   public APIError() {
      this((Throwable)null);
   }

   public APIError(String msg) {
      this((Throwable)null);
      this.setMessage(msg);
   }

   public APIError(Throwable cause) {
      this.cause = cause;
      this.fields = new ArrayList();
      this.showStackTrace = true;
   }

   public String getExceptionId() {
      return this.contains("exception-name") ? this.getField("exception-name").getValueAsString() : null;
   }

   public String getMessage() {
      return this.cause != null ? this.cause.getMessage() : this.message;
   }

   public void setMessage(String message) {
      if (this.cause != null && this.cause instanceof APIError) {
         ((APIError)this.cause).setMessage(message);
      } else {
         this.message = message;
      }

   }

   public void add(Field field) {
      this.fields.add(field);
   }

   public Field getField(String name) {
      Iterator it = this.fields.iterator();

      Field f;
      do {
         if (!it.hasNext()) {
            throw new NoSuchElementException(name);
         }

         f = (Field)it.next();
      } while(!f.getName().equals(name));

      return f;
   }

   public Field getField(int idx) {
      return (Field)this.fields.get(idx);
   }

   public Iterator getFields() {
      return this.fields.iterator();
   }

   public int getFieldListSize() {
      return this.fields.size();
   }

   public boolean contains(String id) {
      Iterator it = this.fields.iterator();

      Field f;
      do {
         if (!it.hasNext()) {
            return false;
         }

         f = (Field)it.next();
      } while(!f.getName().equals(id));

      return true;
   }

   public void printStackTrace() {
      if (this.cause != null) {
         if (this.cause instanceof APIException || this.showStackTrace) {
            this.cause.printStackTrace();
         }
      } else if (this.showStackTrace) {
         super.printStackTrace();
      }

   }

   public void printStackTrace(PrintStream s) {
      if (this.cause != null) {
         if (this.cause instanceof APIException || this.showStackTrace) {
            this.cause.printStackTrace(s);
         }
      } else if (this.showStackTrace) {
         super.printStackTrace(s);
      }

   }

   public void printStackTrace(PrintWriter s) {
      if (this.cause != null) {
         if (this.cause instanceof APIException || this.showStackTrace) {
            this.cause.printStackTrace(s);
         }
      } else if (this.showStackTrace) {
         super.printStackTrace(s);
      }

   }

   protected void setShowStackTrace(boolean showStackTrace) {
      if (this.cause != null && this.cause instanceof APIError) {
         ((APIError)this.cause).setShowStackTrace(showStackTrace);
      } else {
         this.showStackTrace = showStackTrace;
      }

   }
}
