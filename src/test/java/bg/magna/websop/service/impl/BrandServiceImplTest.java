package bg.magna.websop.service.impl;

import bg.magna.websop.model.entity.Brand;
import bg.magna.websop.repository.BrandRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BrandServiceImplTest {
    private BrandServiceImpl toTest;
    private final static Brand TEST_BRAND_1 = new Brand("TestBrand1", "testURL1.com");
    private final static Brand TEST_BRAND_2 = new Brand("TestBrand2", "testURL2.com");
    private final static Brand FAKE_BRAND = new Brand("FakeBrand", "fakeURL.com");

    @Captor
    private ArgumentCaptor<Brand> brandCaptor;

    @Mock
    private BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
        this.toTest = new BrandServiceImpl(brandRepository);
    }

    @Test
    void testGetAllBrands(){
        when(brandRepository.findAll()).thenReturn(List.of(TEST_BRAND_1, TEST_BRAND_2));

        List<String> actual = toTest.getAllBrandNames();
        List<String> expected = List.of(TEST_BRAND_1.getName(), TEST_BRAND_2.getName());

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @Test
    void testFindBrandByName(){
        when(brandRepository.findByName(TEST_BRAND_1.getName())).thenReturn(Optional.of(TEST_BRAND_1));

        Brand actual = toTest.findBrandByName(TEST_BRAND_1.getName());
        Assertions.assertEquals(TEST_BRAND_1, actual);
        Assertions.assertThrows(IllegalArgumentException.class, () -> toTest.findBrandByName(FAKE_BRAND.getName()));
    }

    @Test
    void testAddBrand(){
        when(brandRepository.existsByName(TEST_BRAND_1.getName())).thenReturn(false);

        toTest.addBrand(TEST_BRAND_1.getName(), TEST_BRAND_1.getLogoURL());

        verify(brandRepository).saveAndFlush(brandCaptor.capture());
        Brand actual = brandCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_BRAND_1.getName(), actual.getName());
        Assertions.assertEquals(TEST_BRAND_1.getLogoURL(), actual.getLogoURL());
    }

    @Test
    void testAddBrand_throwsExceptionIfBrandExists(){
        when(brandRepository.existsByName(TEST_BRAND_1.getName())).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> toTest.addBrand(TEST_BRAND_1.getName(), TEST_BRAND_1.getLogoURL()));
    }

    @Test
    void testInitializeMockDB_doesNotInitializeIfDatabaseIsNotEmpty() {
        when(brandRepository.count()).thenReturn(10L);

        toTest.initializeMockBrands();

        verify(brandRepository).count();
        verifyNoMoreInteractions(brandRepository);
    }
}
