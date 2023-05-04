package edu.ambryn.demo.repositories;

import edu.ambryn.demo.models.Country;
import edu.ambryn.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
}
