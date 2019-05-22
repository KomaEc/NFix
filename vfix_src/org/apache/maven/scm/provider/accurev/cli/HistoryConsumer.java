package org.apache.maven.scm.provider.accurev.cli;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.accurev.Transaction;

public class HistoryConsumer extends XppStreamConsumer {
   private List<Transaction> transactions;
   private Transaction currentTran;
   private Long elementId;
   private String elementName;

   public HistoryConsumer(ScmLogger logger, List<Transaction> transactions) {
      super(logger);
      this.transactions = transactions;
   }

   protected void startTag(List<String> tagPath, Map<String, String> attributes) {
      String tagName = getTagName(tagPath);
      String ancestor;
      if ("transaction".equals(tagName)) {
         Long id = Long.parseLong((String)attributes.get("id"));
         Date when = new Date(Long.parseLong((String)attributes.get("time")) * 1000L);
         ancestor = (String)attributes.get("type");
         String user = (String)attributes.get("user");
         this.currentTran = new Transaction(id, when, ancestor, user);
         this.transactions.add(this.currentTran);
      } else if ("version".equals(tagName)) {
         if (this.currentTran != null) {
            if (attributes.containsKey("eid")) {
               this.elementId = Long.parseLong((String)attributes.get("eid"));
               this.elementName = (String)attributes.get("path");
            }

            String virtualSpec = (String)attributes.get("virtual");
            String realSpec = (String)attributes.get("real");
            ancestor = (String)attributes.get("ancestor");
            this.currentTran.addVersion(this.elementId, this.elementName, virtualSpec, realSpec, ancestor);
         }
      } else if ("element".equals(tagName)) {
         this.elementId = Long.parseLong((String)attributes.get("eid"));
         this.elementName = (String)attributes.get("name");
      }

   }

   protected void endTag(List<String> tagPath) {
      String tagName = getTagName(tagPath);
      if ("element".equals(tagName)) {
         this.elementId = null;
         this.elementName = null;
      } else if ("transaction".equals(tagName)) {
         this.currentTran = null;
      }

   }

   protected void text(List<String> tagPath, String text) {
      String tagName = getTagName(tagPath);
      if (this.currentTran != null && "comment".equals(tagName)) {
         this.currentTran.setComment(text);
      }

   }
}
