package be.ucll.ip.minor.groep1209.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Munt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "name.missing")
    private String name;

    @NotBlank(message = "country.missing")
    private String country;

    @NotBlank(message = "currency.missing")
    private String currency;

    @Min(value = 0, message = "value.too.low")
    @NotNull(message = "value.missing")
    private Double value;

    @Min(value = -600, message = "year.too.low")
    @NotNull(message = "not.a.number")
    private Integer year;

    @JsonBackReference /** zorgt ervoor dat er geen loop wordt gemaakt*/
    @ManyToOne(fetch = FetchType.EAGER) /** maakt extra colom aan  */
    @Valid
    private MuntCollectie collectie;

    public MuntCollectie getCollectie() {
        return collectie;
    }

    public void setCollectie(MuntCollectie collectie) {
        this.collectie = collectie;
    }

    public String getCollectieName()  {
        if (collectie == null) {
            return null;
        }else {return collectie.getTitle();}

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
