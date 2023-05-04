package edu.ambryn.demo.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import edu.ambryn.demo.models.Company;
import edu.ambryn.demo.repositories.CompanyRepository;
import edu.ambryn.demo.views.CompanyView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

@RestController
@CrossOrigin(value = "*")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/companies")
    @JsonView(CompanyView.class)
    public Iterable<Company> getCompanies() {
        return companyRepository.findAll();
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable Integer id) {
        return companyRepository.findById(id)
                .map(company -> new ResponseEntity<>(company, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/companies/{id}")
    public boolean deleteCompany(@PathVariable Integer id) {
        companyRepository.deleteById(id);
        return true;
    }
}
