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
    public void testEmbaucheEmployeWithMaxMatricule() throws EmployeException {
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
    public void testEmbaucheEmployeWithExistingMatricule() throws EmployeException {
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
}
