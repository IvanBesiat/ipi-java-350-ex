package com.ipiecoles.java.java350.integration;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootTest
public class EmployeServiceIntegrationTest {

    @Autowired
    EmployeService employeService;

    @Autowired
    EmployeRepository employeRepository;

    @BeforeEach
    @AfterEach
    public void clearDB(){
        employeRepository.deleteAll();
    }

    @Test
    public void testEmbaucheEmployeWith1Employe() throws EmployeException {
        //given
        Employe manager = new Employe("BESIAT","Ivan","M00001", LocalDate.now(),2500d,2,1.0);
        employeRepository.save(manager);
        //when
        employeService.embaucheEmploye("Besiat", "Ivan", Poste.COMMERCIAL, NiveauEtude.DOCTORAT, 0.5);
        //then
        Employe employeTest = employeRepository.findByMatricule("C00002");
        Assertions.assertThat(employeRepository.findLastMatricule()).isEqualTo("00002");
        Assertions.assertThat(employeTest.getMatricule()).isEqualTo("C00002");
        Assertions.assertThat(employeTest.getNom()).isEqualTo("Besiat");
        Assertions.assertThat(employeTest.getPrenom()).isEqualTo("Ivan");
        Assertions.assertThat(employeTest.getSalaire()).isEqualTo(1293.04);
        Assertions.assertThat(employeTest.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employeTest.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employeTest.getTempsPartiel()).isEqualTo((0.5));
    }

    @Test
    void testAvgPerformanceWhereMatriculeStartsWith() throws EmployeException {
        // given
        Employe commercial1 = new Employe("Doe", "John", "C00001", LocalDate.now(), 2500d, 2, 1.0);
        Employe commercial2 = new Employe("Doe", "John", "C00002", LocalDate.now(), 2500d, 1, 1.0);
        Employe commercial3 = new Employe("Doe", "John", "C00003", LocalDate.now(), 2500d, 3, 1.0);
        Employe commercial4 = new Employe("Doe", "John", "C00004", LocalDate.now(), 2500d, 1, 1.0);
        Employe commercial5 = new Employe("Doe", "John", "C00005", LocalDate.now(), 2500d, 2, 1.0);
        Employe manager = new Employe("Doe", "John", "M00005", LocalDate.now(), 2500d, 2, 1.0);
        employeRepository.saveAll(Arrays.asList(commercial1, commercial2, commercial3, commercial4, commercial5, manager));

        //when
        double perfMoyenne = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");

        // then
        Assertions.assertThat(perfMoyenne).isEqualTo(1.8);
    }
}
