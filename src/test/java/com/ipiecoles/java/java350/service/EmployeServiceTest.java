package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.persistence.EntityExistsException;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;
    @Mock
    private EmployeRepository employeRepository;

    @Test
    public void testEmbaucheEmployeWithEmptyDB() throws EmployeException {
        //given
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        Mockito.when(employeRepository.findByMatricule("M00001")).thenReturn(null);
        Mockito.when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        //when
        employeService.embaucheEmploye("Besiat", "Ivan", Poste.MANAGER, NiveauEtude.MASTER, 1.0);
        //then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeCaptor.capture());

        Assertions.assertThat(employeCaptor.getValue().getMatricule()).isEqualTo("M00001");
        Assertions.assertThat(employeCaptor.getValue().getNom()).isEqualTo("Besiat");
        Assertions.assertThat(employeCaptor.getValue().getPrenom()).isEqualTo("Ivan");
        Assertions.assertThat(employeCaptor.getValue().getSalaire()).isEqualTo(2129.71);
        Assertions.assertThat(employeCaptor.getValue().getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employeCaptor.getValue().getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employeCaptor.getValue().getTempsPartiel()).isEqualTo((1.0));
    }

    @Test
    public void testEmbaucheEmployeWith1Employe() throws EmployeException {
        //given
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("00001");
        Mockito.when(employeRepository.findByMatricule("C00002")).thenReturn(null);
        Mockito.when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        //when
        employeService.embaucheEmploye("Besiat", "Ivan", Poste.COMMERCIAL, NiveauEtude.DOCTORAT, 0.5);
        //then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeCaptor.capture());

        Assertions.assertThat(employeCaptor.getValue().getMatricule()).isEqualTo("C00002");
        Assertions.assertThat(employeCaptor.getValue().getNom()).isEqualTo("Besiat");
        Assertions.assertThat(employeCaptor.getValue().getPrenom()).isEqualTo("Ivan");
        Assertions.assertThat(employeCaptor.getValue().getSalaire()).isEqualTo(1293.04);
        Assertions.assertThat(employeCaptor.getValue().getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employeCaptor.getValue().getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employeCaptor.getValue().getTempsPartiel()).isEqualTo((0.5));
    }

    @Test
    public void testEmbaucheEmployeWithTempsPartielNull() throws EmployeException {
        //given
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(null);
        Mockito.when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        //when
        employeService.embaucheEmploye("Besiat", "Ivan", Poste.COMMERCIAL, NiveauEtude.DOCTORAT, null);
        //then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeCaptor.capture());

        Assertions.assertThat(employeCaptor.getValue().getMatricule()).isEqualTo("C00001");
        Assertions.assertThat(employeCaptor.getValue().getNom()).isEqualTo("Besiat");
        Assertions.assertThat(employeCaptor.getValue().getPrenom()).isEqualTo("Ivan");
        Assertions.assertThat(employeCaptor.getValue().getSalaire()).isEqualTo(2586.07);
        Assertions.assertThat(employeCaptor.getValue().getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employeCaptor.getValue().getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employeCaptor.getValue().getTempsPartiel()).isNull();
    }

    @Test
    public void testEmbaucheEmployeWithMaxMatricule() {
        //given
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");
        //when
        Throwable thrown = Assertions.catchThrowable(() -> {
            employeService.embaucheEmploye("Besiat", "Ivan", Poste.COMMERCIAL, NiveauEtude.DOCTORAT, 1.0);
        });
        //then
        Assertions.assertThat(thrown).isInstanceOf(EmployeException.class)
                .hasMessageContaining("Limite des 100000 matricules atteinte !");
    }

    @Test
    public void testEmbaucheEmployeWithExistingMatricule() {
        //given
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(new Employe());
        //when
        Throwable thrown = Assertions.catchThrowable(() -> {
                    employeService.embaucheEmploye("Besiat", "Ivan", Poste.COMMERCIAL, NiveauEtude.DOCTORAT, 1.0);
                });
        //then
        Assertions.assertThat(thrown).isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("L'employé de matricule C00001 existe déjà en BDD");
    }

    @Test
    public void testCalculPerformanceCommercialWithCANegative(){
            String matricule = "C12345";
            Long caTraite = -1L;
            Long objectifCa =1L;

        //given
        //when
        Throwable thrown = Assertions.catchThrowable(() -> {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
        });
        //then
        Assertions.assertThat(thrown).isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le chiffre d'affaire traité ne peut être négatif ou null !");
    }

    @Test
    public void testCalculPerformanceCommercialWithCANull(){
        String matricule = "C12345";
        Long caTraite = null;
        Long objectifCa =1L;

        //given
        //when
        Throwable thrown = Assertions.catchThrowable(() -> {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
        });
        //then
        Assertions.assertThat(thrown).isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le chiffre d'affaire traité ne peut être négatif ou null !");
    }

    @Test
    public void testCalculPerformanceCommercialWithObjectyifCaNull(){
        String matricule = "C12345";
        Long caTraite = 1L;
        Long objectifCa = null;

        //given
        //when
        Throwable thrown = Assertions.catchThrowable(() -> {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
        });
        //then
        Assertions.assertThat(thrown).isInstanceOf(EmployeException.class)
                .hasMessageContaining("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
    }

    @Test
    public void testCalculPerformanceCommercialWithObjectyifCaNegative(){
        String matricule = "C12345";
        Long caTraite = 1L;
        Long objectifCa = -1L;

        //given
        //when
        Throwable thrown = Assertions.catchThrowable(() -> {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
        });
        //then
        Assertions.assertThat(thrown).isInstanceOf(EmployeException.class)
                .hasMessageContaining("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
    }

    @Test
    public void testCalculPerformanceCommercialWithMatriculeNull(){
        String matricule = null;
        Long caTraite = 1L;
        Long objectifCa = 1L;

        //given
        //when
        Throwable thrown = Assertions.catchThrowable(() -> {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
        });
        //then
        Assertions.assertThat(thrown).isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le matricule ne peut être null et doit commencer par un C !");
    }

    @Test
    public void testCalculPerformanceCommercialWithMatriculeNotCommercial(){
        String matricule = "M12345";
        Long caTraite = 1L;
        Long objectifCa = 1L;

        //given
        //when
        Throwable thrown = Assertions.catchThrowable(() -> {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
        });
        //then
        Assertions.assertThat(thrown).isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le matricule ne peut être null et doit commencer par un C !");
    }

    @Test
    public void testEmbaucheEmployeWithEmployeNull() {
        //given
        String matricule = "C12345";
        Long caTraite = 1L;
        Long objectifCa = 1L;
        Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(null);
        //when
        Throwable thrown = Assertions.catchThrowable(() -> {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
        });
        //then
        Assertions.assertThat(thrown).isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le matricule " + matricule + " n'existe pas !");
    }

    @ParameterizedTest
    @CsvSource({
            "'M12345',1,0.7,1.0,1,1700.0",
            "'M12345',1,0.8,1.0,1,1900.0",
            "'M12345',1,0.95,1.0,0.5,1700.0",
            "'M12345',1,1,0.5,1,850.0",
            "'C12345',1,1.02,1.0,1,1000",
            "'C12345',1,1.05,1.0,0.5,1500",
            "'C12345',1,1.2,1.0,0.5,1200",
            "'C12345',1,1.3,1.0,1,2300.0"
    })
    public void testGetNbRtt(
            String matricule,
            Integer performance,
            Long caTraite,
            Long objectifCa,
            Double tauxActivite,
            Integer performanceMoy
    ) throws EmployeException {
        //Given
        Employe employe = new Employe("Manage","Manager",matricule,LocalDate.now(),2500d,performance,tauxActivite);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(null);
        //When
        employeService.calculPerformanceCommercial(matricule,caTraite,objectifCa);
        //Then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(performanceMoy);
    }
}
