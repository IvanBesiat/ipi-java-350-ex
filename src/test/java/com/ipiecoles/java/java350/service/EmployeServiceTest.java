package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
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
public class EmployeServiceTest {

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
        Assertions.assertThat(employeTest.getSalaire()).isEqualTo(2129.708);
        Assertions.assertThat(employeTest.getPerformance()).isEqualTo(1);
        Assertions.assertThat(employeTest.getNombreAnneeAnciennete()).isEqualTo(0);
        Assertions.assertThat(employeTest.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employeTest.getTempsPartiel()).isEqualTo((1.0));
    }
}
