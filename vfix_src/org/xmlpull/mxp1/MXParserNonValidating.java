package org.xmlpull.mxp1;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

public class MXParserNonValidating extends MXParserCachingStrings {
   private boolean processDocDecl;

   public void setFeature(String name, boolean state) throws XmlPullParserException {
      if ("http://xmlpull.org/v1/doc/features.html#process-docdecl".equals(name)) {
         if (this.eventType != 0) {
            throw new XmlPullParserException("process DOCDECL feature can only be changed before parsing", this, (Throwable)null);
         }

         this.processDocDecl = state;
         if (!state) {
         }
      } else {
         super.setFeature(name, state);
      }

   }

   public boolean getFeature(String name) {
      return "http://xmlpull.org/v1/doc/features.html#process-docdecl".equals(name) ? this.processDocDecl : super.getFeature(name);
   }

   protected char more() throws IOException, XmlPullParserException {
      return super.more();
   }

   protected char[] lookuEntityReplacement(int entitNameLen) throws XmlPullParserException, IOException {
      int i;
      if (!this.allStringsInterned) {
         i = MXParser.fastHash(this.buf, this.posStart, this.posEnd - this.posStart);

         label53:
         for(int i = this.entityEnd - 1; i >= 0; --i) {
            if (i == this.entityNameHash[i] && entitNameLen == this.entityNameBuf[i].length) {
               char[] entityBuf = this.entityNameBuf[i];

               for(int j = 0; j < entitNameLen; ++j) {
                  if (this.buf[this.posStart + j] != entityBuf[j]) {
                     continue label53;
                  }
               }

               if (this.tokenize) {
                  this.text = this.entityReplacement[i];
               }

               return this.entityReplacementBuf[i];
            }
         }
      } else {
         this.entityRefName = this.newString(this.buf, this.posStart, this.posEnd - this.posStart);

         for(i = this.entityEnd - 1; i >= 0; --i) {
            if (this.entityRefName == this.entityName[i]) {
               if (this.tokenize) {
                  this.text = this.entityReplacement[i];
               }

               return this.entityReplacementBuf[i];
            }
         }
      }

      return null;
   }

   protected void parseDocdecl() throws XmlPullParserException, IOException {
      boolean oldTokenize = this.tokenize;

      try {
         char ch = this.more();
         if (ch != 'O') {
            throw new XmlPullParserException("expected <!DOCTYPE", this, (Throwable)null);
         }

         ch = this.more();
         if (ch != 'C') {
            throw new XmlPullParserException("expected <!DOCTYPE", this, (Throwable)null);
         }

         ch = this.more();
         if (ch != 'T') {
            throw new XmlPullParserException("expected <!DOCTYPE", this, (Throwable)null);
         }

         ch = this.more();
         if (ch != 'Y') {
            throw new XmlPullParserException("expected <!DOCTYPE", this, (Throwable)null);
         }

         ch = this.more();
         if (ch != 'P') {
            throw new XmlPullParserException("expected <!DOCTYPE", this, (Throwable)null);
         }

         ch = this.more();
         if (ch != 'E') {
            throw new XmlPullParserException("expected <!DOCTYPE", this, (Throwable)null);
         }

         this.posStart = this.pos;
         ch = this.requireNextS();
         int nameStart = this.pos;
         ch = this.readName(ch);
         int nameEnd = this.pos;
         ch = this.skipS(ch);
         if (ch == 'S' || ch == 'P') {
            ch = this.processExternalId(ch);
            ch = this.skipS(ch);
         }

         if (ch == '[') {
            this.processInternalSubset();
         }

         ch = this.skipS(ch);
         if (ch != '>') {
            throw new XmlPullParserException("expected > to finish <[DOCTYPE but got " + this.printable(ch), this, (Throwable)null);
         }

         this.posEnd = this.pos - 1;
      } finally {
         this.tokenize = oldTokenize;
      }

   }

   protected char processExternalId(char ch) throws XmlPullParserException, IOException {
      return ch;
   }

   protected void processInternalSubset() throws XmlPullParserException, IOException {
      while(true) {
         char ch = this.more();
         if (ch == ']') {
            return;
         }

         if (ch == '%') {
            this.processPEReference();
         } else if (this.isS(ch)) {
            this.skipS(ch);
         } else {
            this.processMarkupDecl(ch);
         }
      }
   }

   protected void processPEReference() throws XmlPullParserException, IOException {
   }

   protected void processMarkupDecl(char ch) throws XmlPullParserException, IOException {
      if (ch != '<') {
         throw new XmlPullParserException("expected < for markupdecl in DTD not " + this.printable(ch), this, (Throwable)null);
      } else {
         ch = this.more();
         if (ch == '?') {
            this.parsePI();
         } else {
            if (ch != '!') {
               throw new XmlPullParserException("expected markupdecl in DTD not " + this.printable(ch), this, (Throwable)null);
            }

            ch = this.more();
            if (ch == '-') {
               this.parseComment();
            } else {
               ch = this.more();
               if (ch == 'A') {
                  this.processAttlistDecl(ch);
               } else if (ch == 'E') {
                  ch = this.more();
                  if (ch == 'L') {
                     this.processElementDecl(ch);
                  } else {
                     if (ch != 'N') {
                        throw new XmlPullParserException("expected ELEMENT or ENTITY after <! in DTD not " + this.printable(ch), this, (Throwable)null);
                     }

                     this.processEntityDecl(ch);
                  }
               } else {
                  if (ch != 'N') {
                     throw new XmlPullParserException("expected markupdecl after <! in DTD not " + this.printable(ch), this, (Throwable)null);
                  }

                  this.processNotationDecl(ch);
               }
            }
         }

      }
   }

   protected void processElementDecl(char ch) throws XmlPullParserException, IOException {
      ch = this.requireNextS();
      this.readName(ch);
      ch = this.requireNextS();
   }

   protected void processAttlistDecl(char ch) throws XmlPullParserException, IOException {
   }

   protected void processEntityDecl(char ch) throws XmlPullParserException, IOException {
   }

   protected void processNotationDecl(char ch) throws XmlPullParserException, IOException {
   }

   protected char readName(char ch) throws XmlPullParserException, IOException {
      if (this.isNameStartChar(ch)) {
         throw new XmlPullParserException("XML name must start with name start character not " + this.printable(ch), this, (Throwable)null);
      } else {
         while(this.isNameChar(ch)) {
            ch = this.more();
         }

         return ch;
      }
   }
}
