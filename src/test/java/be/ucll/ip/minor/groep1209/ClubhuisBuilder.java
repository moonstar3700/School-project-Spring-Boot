package be.ucll.ip.minor.groep1209;

import be.ucll.ip.minor.groep1209.domain.model.Clubhuis;

public class ClubhuisBuilder {
    private long id =  0;
    private String name;
    private String email;
    private String gemeente;
    private Integer maxMembers;

    private ClubhuisBuilder(){}

    public static ClubhuisBuilder aclubhuis() {return new ClubhuisBuilder();}

    public ClubhuisBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public ClubhuisBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ClubhuisBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public ClubhuisBuilder withGemeente(String gemeente) {
        this.gemeente = gemeente;
        return this;
    }

    public ClubhuisBuilder withMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
        return this;
    }

    public static ClubhuisBuilder aValidClubhuisTestHuis(){
        return aclubhuis().withName("valid").withEmail("valid@email.be").withGemeente("validGemeente").withMaxMembers(20);
    }

    public static ClubhuisBuilder aValidAnotherClubhuisTestHuis(){
        return aclubhuis().withName("another").withEmail("another@email.be").withGemeente("anotherGemeente").withMaxMembers(20);
    }

    public static ClubhuisBuilder aValidYetAnotherClubhuisTestHuis(){
        return aclubhuis().withName("another").withEmail("another@email.be").withGemeente("ssssss").withMaxMembers(20);
    }

    public static ClubhuisBuilder anIvalidClubhuisWithNoNameTestHuis(){
        return aclubhuis().withName("").withEmail("valid@email.be").withGemeente("validGemeente").withMaxMembers(20);
    }

    public static ClubhuisBuilder anIvalidClubhuisWithNoEmailTestHuis(){
        return aclubhuis().withName("valid").withEmail("").withGemeente("validGemeente").withMaxMembers(20);
    }

    public static ClubhuisBuilder anIvalidClubhuisWithNoMunicipalTestHuis(){
        return aclubhuis().withName("valid").withEmail("valid@email.be").withGemeente("").withMaxMembers(20);
    }

    public static ClubhuisBuilder anIvalidClubhuisWithNoMaxMembersTestHuis(){
        return aclubhuis().withName("valid").withEmail("valid@email.be").withGemeente("validGemeente");
    }

    public static ClubhuisBuilder anIvalidClubhuisWithNegativeMaxMembersTestHuis(){
        return aclubhuis().withName("valid").withEmail("valid@email.be").withGemeente("validGemeente").withMaxMembers(-10);
    }

    public static ClubhuisBuilder anIvalidClubhuisWithNotAEmailTestHuis(){
        return aclubhuis().withName("valid").withEmail("invalidemail.be").withGemeente("validGemeente").withMaxMembers(20);
    }


    public Clubhuis build(){
        Clubhuis clubhuis = new Clubhuis();
        clubhuis.setId(id);
        clubhuis.setName(name);
        clubhuis.setEmail(email);
        clubhuis.setGemeente(gemeente);
        clubhuis.setMaxMembers(maxMembers);
        return clubhuis;
    }
}
