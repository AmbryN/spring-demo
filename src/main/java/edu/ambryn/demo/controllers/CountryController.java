package edu.ambryn.demo.controllers;

import edu.ambryn.demo.models.Country;
import edu.ambryn.demo.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*")
public class CountryController {

    @Autowired
    CountryRepository countryRepository;

    @GetMapping("/countries")
    public Iterable<Country> getCountries() {
        return countryRepository.findAll();
    }

    @PostMapping("/countries")
    public ResponseEntity<Country> addCountry(@RequestBody Country newCountry) {
        if (newCountry.getId() != null) {
            return countryRepository.findById(newCountry.getId())
                    .map(country -> {
                        countryRepository.save(newCountry);
                        return new ResponseEntity<>(newCountry, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        else {
            countryRepository.save(newCountry);
            return new ResponseEntity<>(newCountry, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/countries/{id}")
    public boolean deleteCountry(@PathVariable("id") int id) {
        countryRepository.deleteById(id);
        return true;
    }
}
