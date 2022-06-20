package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

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
    public void testEmbaucheEmployeWithEmptyDB() throws EmployeException {
        //given

        //when
        employeService.embaucheEmploye("Besiat", "Ivan", Poste.MANAGER, NiveauEtude.MASTER, 1.0);
        //then
        Employe employeTest = employeRepository.findByMatricule("M00001");
        Assertions.assertThat(employeRepository.findLastMatricule()).isEqualTo("00001");
        Assertions.assertThat(employeTest.getMatricule()).isEqualTo("M00001");
        Assertions.assertThat(employeTest.getNom()).isEqualTo("Besiat");
        Assertions.assertThat(employeTest.getPrenom()).isEqualTo("Ivan");
        Assertions.assertThat(employeTest.getSalaire()).isEqualTo(2129.71);
        Assertions.assertThat(employeTest.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employeTest.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employeTest.getTempsPartiel()).isEqualTo((1.0));
    }

    @Test
    public void testEmbaucheEmployeWith1Employe() throws EmployeException {
        //given
        Employe manager = new Employe("BESIAT","Ivan","M00001", LocalDate.now(),2500d,2,1.0);
        employeRepository.save(manager);
        //when
        employeService.embaucheEmploye("Besiat", "Ivan", Poste.COMMERCIAL, NiveauEtude.DOCTORAT, 1.0);
        //then
        Employe employeTest = employeRepository.findByMatricule("C00002");
        Assertions.assertThat(employeRepository.findLastMatricule()).isEqualTo("00002");
        Assertions.assertThat(employeTest.getMatricule()).isEqualTo("C00002");
        Assertions.assertThat(employeTest.getNom()).isEqualTo("Besiat");
        Assertions.assertThat(employeTest.getPrenom()).isEqualTo("Ivan");
        Assertions.assertThat(employeTest.getSalaire()).isEqualTo(2586.07);
        Assertions.assertThat(employeTest.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employeTest.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employeTest.getTempsPartiel()).isEqualTo((1.0));
    }

    @Test
    public void testEmbaucheEmployeWithTempsPartielNull() throws EmployeException {
        //given

        //when
        employeService.embaucheEmploye("Besiat", "Ivan", Poste.COMMERCIAL, NiveauEtude.DOCTORAT, null);
        //then
        Employe employeTest = employeRepository.findByMatricule("C00001");
        Assertions.assertThat(employeRepository.findLastMatricule()).isEqualTo("00001");
        Assertions.assertThat(employeTest.getMatricule()).isEqualTo("C00001");
        Assertions.assertThat(employeTest.getNom()).isEqualTo("Besiat");
        Assertions.assertThat(employeTest.getPrenom()).isEqualTo("Ivan");
        Assertions.assertThat(employeTest.getSalaire()).isEqualTo(2586.07);
        Assertions.assertThat(employeTest.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employeTest.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employeTest.getTempsPartiel()).isNull();
    }

//    @Test
//    public void testEmbaucheEmployeWithMaxMatricule() throws EmployeException {
//        //given
//        Employe manager = new Employe("BESIAT","Ivan","M99999", LocalDate.now(),2500d,2,1.0);
//        employeRepository.save(manager);
//        //when
//        employeService.embaucheEmploye("Besiat", "Ivan", Poste.COMMERCIAL, NiveauEtude.DOCTORAT, 1.0);
//        //then
//        Employe employeTest = employeRepository.findByMatricule("C00002");
//        Assertions.assertThat(employeRepository.findLastMatricule()).isEqualTo("00002");
//        Assertions.assertThat(employeTest.getMatricule()).isEqualTo("C00002");
//        Assertions.assertThat(employeTest.getNom()).isEqualTo("Besiat");
//        Assertions.assertThat(employeTest.getPrenom()).isEqualTo("Ivan");
//        Assertions.assertThat(employeTest.getSalaire()).isEqualTo(2586.07);
//        Assertions.assertThat(employeTest.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
//        Assertions.assertThat(employeTest.getDateEmbauche()).isEqualTo(LocalDate.now());
//        Assertions.assertThat(employeTest.getTempsPartiel()).isEqualTo((1.0));
//
//    }
}
