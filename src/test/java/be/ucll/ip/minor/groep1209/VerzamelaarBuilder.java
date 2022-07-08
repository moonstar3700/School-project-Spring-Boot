package be.ucll.ip.minor.groep1209;

import be.ucll.ip.minor.groep1209.domain.model.Verzamelaar;

public class VerzamelaarBuilder {
    private long id = 0;
    private String name, firstname, region;
    private Integer age;

    private VerzamelaarBuilder(){}

    public static VerzamelaarBuilder aVerzamelaar() {
        return new VerzamelaarBuilder();
    }

    public VerzamelaarBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public VerzamelaarBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public VerzamelaarBuilder withFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public VerzamelaarBuilder withRegion(String region) {
        this.region = region;
        return this;
    }

    public VerzamelaarBuilder withAge(Integer age) {
        this.age = age;
        return this;
    }

    public static VerzamelaarBuilder aValidVerzamelaarTestverzamelaar() {
        return aVerzamelaar().withName("Mensaert").withFirstname("Jarno").withRegion("Leuven").withAge(20);
    }

    public static VerzamelaarBuilder aValidVerzamelaarAnotherverzamelaar() {
        return aVerzamelaar().withName("Coppens").withFirstname("Thomas").withRegion("Brussel").withAge(32);
    }

    public static VerzamelaarBuilder aValidVerzamelaarYetanotherverzamelaar() {
        return aVerzamelaar().withName("Galbusera").withFirstname("Arthur").withRegion("Leuven").withAge(54);
    }

    public static VerzamelaarBuilder aValidVerzamelaarWithDuplicateName() {
        return aVerzamelaar().withName("Mensaert").withFirstname("Oliver").withRegion("Luik").withAge(69);
    }

    public static VerzamelaarBuilder anInvalidVerzamelaarWithLessThenThreeCharacters() {
        return aVerzamelaar().withName("Me").withFirstname("Jarno").withRegion("Leuven").withAge(20);
    }

    public static VerzamelaarBuilder anInvalidVerzamelaarWithAgeTooYoung() {
        return aVerzamelaar().withName("Jansens").withFirstname("Pieter").withRegion("Knokke").withAge(12);
    }

    public static VerzamelaarBuilder anInvalidVerzamelaarWithAgeTooOld() {
        return aVerzamelaar().withName("Jansens").withFirstname("Pieter").withRegion("Knokke").withAge(130);
    }

    public static VerzamelaarBuilder anInvalidVerzamelaarWithNoName() {
        return aVerzamelaar().withName("").withFirstname("Pieter").withRegion("Knokke").withAge(52);
    }

    public static VerzamelaarBuilder anInvalidVerzamelaarWithNoFirstname() {
        return aVerzamelaar().withName("Jansens").withFirstname("").withRegion("Knokke").withAge(52);
    }

    public static VerzamelaarBuilder anInvalidVerzamelaarWithNoRegion() {
        return aVerzamelaar().withName("Jansens").withFirstname("Pieter").withRegion("").withAge(52);
    }

    public static VerzamelaarBuilder anInvalidVerzamelaarWithNoAge() {
        return aVerzamelaar().withName("Jansens").withFirstname("Pieter").withRegion("Knokke");
    }

    public Verzamelaar build() {
        Verzamelaar verzamelaar = new Verzamelaar();
        verzamelaar.setId(id);
        verzamelaar.setName(name);
        verzamelaar.setFirstname(firstname);
        verzamelaar.setRegion(region);
        verzamelaar.setAge(age);
        return verzamelaar;
    }
}
