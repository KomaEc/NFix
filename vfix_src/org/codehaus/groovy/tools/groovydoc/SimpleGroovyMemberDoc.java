package org.codehaus.groovy.tools.groovydoc;

import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyMemberDoc;

public class SimpleGroovyMemberDoc extends SimpleGroovyAbstractableElementDoc implements GroovyMemberDoc {
   protected GroovyClassDoc belongsToClass;

   public SimpleGroovyMemberDoc(String name, GroovyClassDoc belongsToClass) {
      super(name);
      this.belongsToClass = belongsToClass;
   }

   public boolean isSynthetic() {
      return false;
   }

   public String firstSentenceCommentText() {
      if (super.firstSentenceCommentText() == null) {
         SimpleGroovyClassDoc classDoc = (SimpleGroovyClassDoc)this.belongsToClass;
         this.setFirstSentenceCommentText(classDoc.replaceTags(calculateFirstSentence(this.getRawCommentText())));
      }

      return super.firstSentenceCommentText();
   }

   public String commentText() {
      if (super.commentText() == null) {
         SimpleGroovyClassDoc classDoc = (SimpleGroovyClassDoc)this.belongsToClass;
         this.setCommentText(classDoc.replaceTags(this.getRawCommentText()));
      }

      return super.commentText();
   }
}
