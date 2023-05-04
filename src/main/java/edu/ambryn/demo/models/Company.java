package edu.ambryn.demo.models;

import com.fasterxml.jackson.annotation.JsonView;
import edu.ambryn.demo.views.CompanyView;
import edu.ambryn.demo.views.UserView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({UserView.class, CompanyView.class})
    private Integer id;

    @JsonView({UserView.class, CompanyView.class})
    private String name;

    @OneToMany(mappedBy = "company")
    @ToString.Exclude
    @JsonView(CompanyView.class)
    private Set<User> employees = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id) && Objects.equals(name, company.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
