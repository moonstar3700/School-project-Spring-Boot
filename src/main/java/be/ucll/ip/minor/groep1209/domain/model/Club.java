package be.ucll.ip.minor.groep1209.domain.model;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "name.missing")
    private String name;

    @NotBlank(message = "email.missing")
    @Email(message = "email.not.valid")
    private String email;

    @NotBlank(message = "region.missing")
    private String region;

    @Min(value = 5, message = "members.more.than.five")
    @Max(value = 100, message = "not.more.than.100.members")
    @NotNull(message = "not.a.number")
    private Integer maxMembers;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="club_collector", joinColumns={@JoinColumn(name="club_id", referencedColumnName="id")}
            , inverseJoinColumns={@JoinColumn(name="collector_id", referencedColumnName="id")})
    private List<Verzamelaar> collectors;

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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(Integer maxAantalLeden) {
        this.maxMembers = maxAantalLeden;
    }

    public List<Verzamelaar> getCollectors() {
        return this.collectors;
    }

    public void addCollector(Verzamelaar collector)
    {
        this.collectors.add(collector);
        collector.addClub(this);
    }

    public void removeCollector(Verzamelaar collector)
    {
        this.collectors.remove(collector);
        collector.removeClub(this);
    }

    public void setCollectors(List<Verzamelaar> collectors) {
        this.collectors = collectors;
    }

    public void removeCollectorWhileIterating(Verzamelaar verzamelaar) {
        Iterator<Verzamelaar> it = this.collectors.iterator();
        while (it.hasNext()){
            Verzamelaar collector = it.next();
            if (collector.getId() == verzamelaar.getId()){
                it.remove();
            }
        }
    }
}
