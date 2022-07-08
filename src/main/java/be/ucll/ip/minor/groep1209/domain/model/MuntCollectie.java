package be.ucll.ip.minor.groep1209.domain.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@Entity
public class MuntCollectie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "title.missing")
    private String title;

    @NotBlank(message = "country.missing")
    private String country;

    @Max(value = 2022, message = "year.too.big")
    @Min(value = -600, message = "year.too.low")
    @NotNull(message = "not.a.number")
    private Integer year;
    /** creeert join table*/
    /** eerst moet munt (kind) persistent gemaakt worden, dan pas kan colectie(parent) geupdate worden. --> cascade zorgt hiervoor */
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "collectie")
    private List<Munt> munten;

    public List<Munt> getMunten() {
        return munten;
    }

    public void setMunten(List<Munt> munten) {
        this.munten = munten;
    }

    public void addMunt(Munt munt){
        this.munten.add(munt);
        /** voeg dan collectie toe aan munt*/
        munt.setCollectie(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void deleteMunt(Munt munt) {
        munten.remove(munt);
        munt.setCollectie(null);
    }
}
