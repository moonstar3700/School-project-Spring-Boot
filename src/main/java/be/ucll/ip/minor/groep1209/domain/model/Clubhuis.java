package be.ucll.ip.minor.groep1209.domain.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@IdClass(CompPK.class)
public class Clubhuis {

    @Id
    @NotBlank(message = "naam.missing")
    private String name;

    @NotBlank(message = "email.missing")
    @Email(message = "email.not.valid")
    private String email;

    @NotNull(message = "not.a.number")
    private Integer maxMembers;

    @Id
    @NotBlank(message = "gemeente.missing")
    private String gemeente;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Integer getMaxMembers() { return maxMembers; }

    public void setMaxMembers(Integer maxMembers) { this.maxMembers = maxMembers; }

    public String getGemeente() { return gemeente; }

    public void setGemeente(String gemeente) { this.gemeente = gemeente; }
}
