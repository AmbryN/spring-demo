package edu.ambryn.demo.models;

import com.fasterxml.jackson.annotation.JsonView;
import edu.ambryn.demo.views.CompanyView;
import edu.ambryn.demo.views.UserView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({UserView.class, CompanyView.class})
    private Integer id;
    @JsonView({UserView.class, CompanyView.class})
    private String email;
    private String password;
    @JsonView({UserView.class, CompanyView.class})
    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();
    @JsonView({UserView.class, CompanyView.class})
    private String lastname;
    @JsonView({UserView.class, CompanyView.class})
    private String firstname;
    @ManyToOne
    @JoinColumn(name = "id_country")
    @JsonView({UserView.class})
    private Country country;

    @ManyToMany
    @JoinTable(name = "user_jobs",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "job_id", referencedColumnName = "id")
    )
    @ToString.Exclude
    @JsonView(UserView.class)
    private Set<Job> jobs = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id_company")
    @JsonView(UserView.class)
    private Company company;

    @JsonView(UserView.class)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonView(UserView.class)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String profilePictureUrl;


    public void addRole(Role role) {
        this.roles.add(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(roles, user.roles) && Objects.equals(lastname, user.lastname) && Objects.equals(firstname, user.firstname) && Objects.equals(country, user.country) && Objects.equals(jobs, user.jobs) && Objects.equals(company, user.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, roles, lastname, firstname, country, jobs, company);
    }
}
