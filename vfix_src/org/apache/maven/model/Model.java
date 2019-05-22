package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Model extends ModelBase implements Serializable {
   private String modelVersion;
   private Parent parent;
   private String groupId;
   private String artifactId;
   private String version;
   private String packaging = "jar";
   private String name;
   private String description;
   private String url;
   private String inceptionYear;
   private Organization organization;
   private List<License> licenses;
   private List<MailingList> mailingLists;
   private List<Developer> developers;
   private List<Contributor> contributors;
   private IssueManagement issueManagement;
   private Scm scm;
   private CiManagement ciManagement;
   private Prerequisites prerequisites;
   private Build build;
   private List<Profile> profiles;
   private String modelEncoding = "UTF-8";

   public void addContributor(Contributor contributor) {
      if (!(contributor instanceof Contributor)) {
         throw new ClassCastException("Model.addContributors(contributor) parameter must be instanceof " + Contributor.class.getName());
      } else {
         this.getContributors().add(contributor);
      }
   }

   public void addDeveloper(Developer developer) {
      if (!(developer instanceof Developer)) {
         throw new ClassCastException("Model.addDevelopers(developer) parameter must be instanceof " + Developer.class.getName());
      } else {
         this.getDevelopers().add(developer);
      }
   }

   public void addLicense(License license) {
      if (!(license instanceof License)) {
         throw new ClassCastException("Model.addLicenses(license) parameter must be instanceof " + License.class.getName());
      } else {
         this.getLicenses().add(license);
      }
   }

   public void addMailingList(MailingList mailingList) {
      if (!(mailingList instanceof MailingList)) {
         throw new ClassCastException("Model.addMailingLists(mailingList) parameter must be instanceof " + MailingList.class.getName());
      } else {
         this.getMailingLists().add(mailingList);
      }
   }

   public void addProfile(Profile profile) {
      if (!(profile instanceof Profile)) {
         throw new ClassCastException("Model.addProfiles(profile) parameter must be instanceof " + Profile.class.getName());
      } else {
         this.getProfiles().add(profile);
      }
   }

   public String getArtifactId() {
      return this.artifactId;
   }

   public Build getBuild() {
      return this.build;
   }

   public CiManagement getCiManagement() {
      return this.ciManagement;
   }

   public List<Contributor> getContributors() {
      if (this.contributors == null) {
         this.contributors = new ArrayList();
      }

      return this.contributors;
   }

   public String getDescription() {
      return this.description;
   }

   public List<Developer> getDevelopers() {
      if (this.developers == null) {
         this.developers = new ArrayList();
      }

      return this.developers;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public String getInceptionYear() {
      return this.inceptionYear;
   }

   public IssueManagement getIssueManagement() {
      return this.issueManagement;
   }

   public List<License> getLicenses() {
      if (this.licenses == null) {
         this.licenses = new ArrayList();
      }

      return this.licenses;
   }

   public List<MailingList> getMailingLists() {
      if (this.mailingLists == null) {
         this.mailingLists = new ArrayList();
      }

      return this.mailingLists;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }

   public String getModelVersion() {
      return this.modelVersion;
   }

   public String getName() {
      return this.name;
   }

   public Organization getOrganization() {
      return this.organization;
   }

   public String getPackaging() {
      return this.packaging;
   }

   public Parent getParent() {
      return this.parent;
   }

   public Prerequisites getPrerequisites() {
      return this.prerequisites;
   }

   public List<Profile> getProfiles() {
      if (this.profiles == null) {
         this.profiles = new ArrayList();
      }

      return this.profiles;
   }

   public Scm getScm() {
      return this.scm;
   }

   public String getUrl() {
      return this.url;
   }

   public String getVersion() {
      return this.version;
   }

   public void removeContributor(Contributor contributor) {
      if (!(contributor instanceof Contributor)) {
         throw new ClassCastException("Model.removeContributors(contributor) parameter must be instanceof " + Contributor.class.getName());
      } else {
         this.getContributors().remove(contributor);
      }
   }

   public void removeDeveloper(Developer developer) {
      if (!(developer instanceof Developer)) {
         throw new ClassCastException("Model.removeDevelopers(developer) parameter must be instanceof " + Developer.class.getName());
      } else {
         this.getDevelopers().remove(developer);
      }
   }

   public void removeLicense(License license) {
      if (!(license instanceof License)) {
         throw new ClassCastException("Model.removeLicenses(license) parameter must be instanceof " + License.class.getName());
      } else {
         this.getLicenses().remove(license);
      }
   }

   public void removeMailingList(MailingList mailingList) {
      if (!(mailingList instanceof MailingList)) {
         throw new ClassCastException("Model.removeMailingLists(mailingList) parameter must be instanceof " + MailingList.class.getName());
      } else {
         this.getMailingLists().remove(mailingList);
      }
   }

   public void removeProfile(Profile profile) {
      if (!(profile instanceof Profile)) {
         throw new ClassCastException("Model.removeProfiles(profile) parameter must be instanceof " + Profile.class.getName());
      } else {
         this.getProfiles().remove(profile);
      }
   }

   public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
   }

   public void setBuild(Build build) {
      this.build = build;
   }

   public void setCiManagement(CiManagement ciManagement) {
      this.ciManagement = ciManagement;
   }

   public void setContributors(List<Contributor> contributors) {
      this.contributors = contributors;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setDevelopers(List<Developer> developers) {
      this.developers = developers;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public void setInceptionYear(String inceptionYear) {
      this.inceptionYear = inceptionYear;
   }

   public void setIssueManagement(IssueManagement issueManagement) {
      this.issueManagement = issueManagement;
   }

   public void setLicenses(List<License> licenses) {
      this.licenses = licenses;
   }

   public void setMailingLists(List<MailingList> mailingLists) {
      this.mailingLists = mailingLists;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public void setModelVersion(String modelVersion) {
      this.modelVersion = modelVersion;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setOrganization(Organization organization) {
      this.organization = organization;
   }

   public void setPackaging(String packaging) {
      this.packaging = packaging;
   }

   public void setParent(Parent parent) {
      this.parent = parent;
   }

   public void setPrerequisites(Prerequisites prerequisites) {
      this.prerequisites = prerequisites;
   }

   public void setProfiles(List<Profile> profiles) {
      this.profiles = profiles;
   }

   public void setScm(Scm scm) {
      this.scm = scm;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getId() {
      StringBuffer id = new StringBuffer();
      id.append(this.getGroupId());
      id.append(":");
      id.append(this.getArtifactId());
      id.append(":");
      id.append(this.getPackaging());
      id.append(":");
      id.append(this.getVersion());
      return id.toString();
   }
}
