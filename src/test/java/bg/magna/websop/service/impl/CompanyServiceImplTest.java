package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.AddCompanyDTO;
import bg.magna.websop.model.entity.Company;
import bg.magna.websop.repository.CompanyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceImplTest {
    private CompanyServiceImpl toTest;
    private static final Company TEST_COMPANY = new Company("TestCompany1", "BG123456789", "address1", "0888888888", "company1@example.com");

    @Mock
    private CompanyRepository companyRepository;

    @Captor
    private ArgumentCaptor<Company> companyCaptor;

    @BeforeEach
    void setUp(){
        toTest = new CompanyServiceImpl(companyRepository, new ModelMapper());
    }

    @Test
    void testGetCompanyByName() {
        when(companyRepository.findByName(TEST_COMPANY.getName())).thenReturn(Optional.of(TEST_COMPANY));

        Company actual = toTest.getCompanyByName(TEST_COMPANY.getName());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_COMPANY.getName(), actual.getName());
        Assertions.assertEquals(TEST_COMPANY.getVatNumber(), actual.getVatNumber());
        Assertions.assertEquals(TEST_COMPANY.getRegisteredAddress(), actual.getRegisteredAddress());
        Assertions.assertEquals(TEST_COMPANY.getPhone(), actual.getPhone());
        Assertions.assertEquals(TEST_COMPANY.getEmail(), actual.getEmail());
    }

    @Test
    void testGetCompanyByName_throwsExceptionWhenNoCompanyFound() {
        when(companyRepository.findByName(TEST_COMPANY.getName())).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> toTest.getCompanyByName(TEST_COMPANY.getName()));
    }

    @Test
    void testAddCompany() {
        when(companyRepository.existsByName(TEST_COMPANY.getName())).thenReturn(false);
        AddCompanyDTO companyDTO = new AddCompanyDTO(
                TEST_COMPANY.getName(),
                TEST_COMPANY.getVatNumber(),
                TEST_COMPANY.getRegisteredAddress(),
                TEST_COMPANY.getPhone(),
                TEST_COMPANY.getEmail());

        toTest.addCompany(companyDTO);

        verify(companyRepository).saveAndFlush(companyCaptor.capture());
        Company actual = companyCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_COMPANY.getName(), actual.getName());
        Assertions.assertEquals(TEST_COMPANY.getVatNumber(), actual.getVatNumber());
        Assertions.assertEquals(TEST_COMPANY.getRegisteredAddress(), actual.getRegisteredAddress());
        Assertions.assertEquals(TEST_COMPANY.getPhone(), actual.getPhone());
        Assertions.assertEquals(TEST_COMPANY.getEmail(), actual.getEmail());
    }

    @Test
    void testAddCompany_throwsExceptionWhenCompanyAlreadyExists() {
        when(companyRepository.existsByName(TEST_COMPANY.getName())).thenReturn(true);

        AddCompanyDTO companyDTO = new AddCompanyDTO(
                TEST_COMPANY.getName(),
                TEST_COMPANY.getVatNumber(),
                TEST_COMPANY.getRegisteredAddress(),
                TEST_COMPANY.getPhone(),
                TEST_COMPANY.getEmail());

        Assertions.assertThrows(IllegalArgumentException.class, () -> toTest.addCompany(companyDTO));
    }

    @Test
    void testAddFirstTwoCompanies_doesNothingWhenDBNotEmpty() {
        when(companyRepository.count()).thenReturn(10L);

        toTest.addFirstTwoCompanies();

        verify(companyRepository).count();
        verifyNoMoreInteractions(companyRepository);
    }
}
