package be.ucll.ip.minor.groep1209.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
public class Verzamelaar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Size(min = 3, message = "name.min.length.3")
    @NotBlank(message = "name.missing")
    private String name;

    @NotBlank(message = "firstname.missing")
    private String firstname;

    @NotBlank(message = "region.missing")
    private String region;

    @Min(value = 18, message = "age.lower.than.18")
    @Max(value = 110, message = "age.higher.than.110")
    @NotNull(message = "not.a.number")
    private Integer age;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "collectors")
    private List<Club> clubs = new ArrayList<>();

    public void addClub(Club club){
        clubs.add(club);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Club> getClubs() {
        return clubs;
    }

    public void setClubs(List<Club> clubs) {
        this.clubs = clubs;
    }

    public void removeClub(Club club) {
        this.clubs.remove(club);
    }

    public List<String> getClubNames() {
        List<String> names = new ArrayList<>();
        for (Club club : this.clubs ) {
            names.add(club.getName());
        }
        return names;
    }

    public void removeClubWhileIterating(Club club) {
        Iterator<Club> it = this.clubs.iterator();
        while (it.hasNext()){
            Club c = it.next();
            if (c.getId() == club.getId()){
                it.remove();
                c.removeCollector(this);
            }
        }
    }
}
