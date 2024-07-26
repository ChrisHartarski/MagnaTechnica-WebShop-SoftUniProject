package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.part.PartDataDTO;
import bg.magna.websop.model.dto.part.ShortPartDataDTO;
import bg.magna.websop.model.entity.Brand;
import bg.magna.websop.model.entity.Company;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.PartRepository;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.UserService;
import bg.magna.websop.service.exception.ResourceNotFoundException;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PartServiceImplTest {
    private PartServiceImpl toTest;
    private static final Brand TEST_BRAND = new Brand("TestBrand1", "testURL1.com");
    private static final Part TEST_PART_1 = new Part("UUID1", "TestPart1", 10, "descriptionEnPart1", "descriptionBgPart1", "magna.bg/imagePart1", TEST_BRAND, BigDecimal.valueOf(20), "part1Size", 10.5, "part1MoreInfo", "part1SuitableFor");
    private static final Part TEST_PART_2 = new Part("UUID2", "TestPart2", 15, "descriptionEnPart2", "descriptionBgPart2", "magna.bg/imagePart2", TEST_BRAND, BigDecimal.valueOf(10), "part1Size", 5, "part2MoreInfo", "part2SuitableFor");
    private static final UserEntity TEST_USER = new UserEntity("someUUID", "testUser@example.com", "password", "Test", "User", "0888888888", UserRole.USER, Map.of(TEST_PART_1, 5, TEST_PART_2, 6), new ArrayList<>(), new Company());

    @Mock
    private PartRepository partRepository;

    @Mock
    private BrandService brandService;

    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<Part> partCaptor;


    @BeforeEach
    public void setUp() {
        toTest = new PartServiceImpl(partRepository, brandService, userService, new ModelMapper(), new Gson());
    }


    @Test
    void testGetTotalParts(){
        when(partRepository.findAll()).thenReturn(List.of(TEST_PART_1, TEST_PART_2));

        long expected = TEST_PART_1.getQuantity() + TEST_PART_2.getQuantity();
        long actual = toTest.getTotalParts();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testAddPart() {
        PartDataDTO testPartData = getTestPartDataDTO(TEST_PART_1);

        when(brandService.findBrandByName(TEST_BRAND.getName())).thenReturn(TEST_BRAND);

        toTest.addPart(testPartData);

        verify(partRepository).saveAndFlush(partCaptor.capture());
        Part actual = partCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_PART_1.getPartCode(), actual.getPartCode());
        Assertions.assertEquals(TEST_PART_1.getQuantity(), actual.getQuantity());
        Assertions.assertEquals(TEST_PART_1.getDescriptionEn(), actual.getDescriptionEn());
        Assertions.assertEquals(TEST_PART_1.getDescriptionBg(), actual.getDescriptionBg());
        Assertions.assertEquals(TEST_PART_1.getImageURL(), actual.getImageURL());
        Assertions.assertEquals(TEST_PART_1.getBrand().getName(), actual.getBrand().getName());
        Assertions.assertEquals(TEST_PART_1.getBrand().getLogoURL(), actual.getBrand().getLogoURL());
        Assertions.assertEquals(TEST_PART_1.getPrice(), actual.getPrice());
        Assertions.assertEquals(TEST_PART_1.getSize(), actual.getSize());
        Assertions.assertEquals(TEST_PART_1.getWeight(), actual.getWeight());
        Assertions.assertEquals(TEST_PART_1.getSuitableFor(), actual.getSuitableFor());
        Assertions.assertEquals(TEST_PART_1.getMoreInfo(), actual.getMoreInfo());
    }

    @Test
    void testEditPart() {
        PartDataDTO testPartData = getTestPartDataDTO(TEST_PART_1);
        when(partRepository.findByPartCode(TEST_PART_1.getPartCode())).thenReturn(Optional.of(TEST_PART_1));
        when(brandService.findBrandByName(TEST_BRAND.getName())).thenReturn(TEST_BRAND);

        toTest.editPart(testPartData);

        verify(partRepository).saveAndFlush(partCaptor.capture());
        Part actual = partCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_PART_1.getPartCode(), actual.getPartCode());
        Assertions.assertEquals(TEST_PART_1.getQuantity(), actual.getQuantity());
        Assertions.assertEquals(TEST_PART_1.getDescriptionEn(), actual.getDescriptionEn());
        Assertions.assertEquals(TEST_PART_1.getDescriptionBg(), actual.getDescriptionBg());
        Assertions.assertEquals(TEST_PART_1.getImageURL(), actual.getImageURL());
        Assertions.assertEquals(TEST_PART_1.getBrand().getName(), actual.getBrand().getName());
        Assertions.assertEquals(TEST_PART_1.getBrand().getLogoURL(), actual.getBrand().getLogoURL());
        Assertions.assertEquals(TEST_PART_1.getPrice(), actual.getPrice());
        Assertions.assertEquals(TEST_PART_1.getSize(), actual.getSize());
        Assertions.assertEquals(TEST_PART_1.getWeight(), actual.getWeight());
        Assertions.assertEquals(TEST_PART_1.getSuitableFor(), actual.getSuitableFor());
        Assertions.assertEquals(TEST_PART_1.getMoreInfo(), actual.getMoreInfo());
    }

    @Test
    void testGetAllShortPartDTOs() {
        when(partRepository.findAll()).thenReturn(List.of(TEST_PART_1, TEST_PART_2));
        List<ShortPartDataDTO> expected = List.of(getTestShortPartDataDTO(TEST_PART_1), getTestShortPartDataDTO(TEST_PART_2));

        List<ShortPartDataDTO> actual = toTest.getAllShortPartDTOs();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0).getPartCode(), actual.get(0).getPartCode());
        Assertions.assertEquals(expected.get(1).getPartCode(), actual.get(1).getPartCode());
    }

    @Test
    void testInitializeMockParts() throws IOException {
        when(partRepository.count()).thenReturn(0L);
        Part expected = new Part("P5S58101149", BigDecimal.valueOf(55.67), 0);

        toTest.initializeMockParts();
        verify(partRepository, atMost(21)).saveAndFlush(partCaptor.capture());

        toTest.initializeMockParts();
        verify(partRepository, atLeast(21)).saveAndFlush(partCaptor.capture());
        Part actual = partCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getPartCode(), actual.getPartCode());
    }

    @Test
    void testInitializeMockParts_doesNotStartWhenRepositoryIsNotEmpty() throws IOException {
        when(partRepository.count()).thenReturn(5L);

        toTest.initializeMockParts();
        verifyNoMoreInteractions(partRepository);
    }

    @Test
    void testGetPartDTOFromPartCode() {
        when(partRepository.findByPartCode(TEST_PART_1.getPartCode())).thenReturn(Optional.of(TEST_PART_1));

        PartDataDTO expected = getTestPartDataDTO(TEST_PART_1);
        PartDataDTO actual = toTest.getPartDTOFromPartCode(TEST_PART_1.getPartCode());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getPartCode(), actual.getPartCode());
        Assertions.assertEquals(expected.getQuantity(), actual.getQuantity());
        Assertions.assertEquals(expected.getDescriptionEn(), actual.getDescriptionEn());
        Assertions.assertEquals(expected.getDescriptionBg(), actual.getDescriptionBg());
        Assertions.assertEquals(expected.getImageURL(), actual.getImageURL());
        Assertions.assertEquals(expected.getBrandLogoURL(), actual.getBrandLogoURL());
        Assertions.assertEquals(expected.getBrandName(), actual.getBrandName());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
        Assertions.assertEquals(expected.getSize(), actual.getSize());
        Assertions.assertEquals(expected.getWeight(), actual.getWeight());
        Assertions.assertEquals(expected.getSuitableFor(), actual.getSuitableFor());
        Assertions.assertEquals(expected.getMoreInfo(), actual.getMoreInfo());
    }

    @Test
    void testGetPartByPartCode() {
        when(partRepository.findByPartCode(TEST_PART_1.getPartCode())).thenReturn(Optional.of(TEST_PART_1));
        Part actual = toTest.getPartByPartCode(TEST_PART_1.getPartCode());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_PART_1.getPartCode(), actual.getPartCode());
        Assertions.assertEquals(TEST_PART_1.getQuantity(), actual.getQuantity());
        Assertions.assertEquals(TEST_PART_1.getDescriptionEn(), actual.getDescriptionEn());
        Assertions.assertEquals(TEST_PART_1.getDescriptionBg(), actual.getDescriptionBg());
        Assertions.assertEquals(TEST_PART_1.getImageURL(), actual.getImageURL());
        Assertions.assertEquals(TEST_PART_1.getBrand().getName(), actual.getBrand().getName());
        Assertions.assertEquals(TEST_PART_1.getBrand().getLogoURL(), actual.getBrand().getLogoURL());
        Assertions.assertEquals(TEST_PART_1.getPrice(), actual.getPrice());
        Assertions.assertEquals(TEST_PART_1.getSize(), actual.getSize());
        Assertions.assertEquals(TEST_PART_1.getWeight(), actual.getWeight());
        Assertions.assertEquals(TEST_PART_1.getSuitableFor(), actual.getSuitableFor());
        Assertions.assertEquals(TEST_PART_1.getMoreInfo(), actual.getMoreInfo());
    }

    @Test
    void testGetPartByPartCode_throwsExceptionWhenPartNotFound(){
        when(partRepository.findByPartCode(TEST_PART_1.getPartCode())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> toTest.getPartByPartCode(TEST_PART_1.getPartCode()));
    }

    @Test
    void testSavePartToDB() {
        toTest.savePartToDB(TEST_PART_1);
        verify(partRepository).saveAndFlush(partCaptor.capture());
        Part actual = partCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_PART_1.getPartCode(), actual.getPartCode());
        Assertions.assertEquals(TEST_PART_1.getQuantity(), actual.getQuantity());
        Assertions.assertEquals(TEST_PART_1.getDescriptionEn(), actual.getDescriptionEn());
        Assertions.assertEquals(TEST_PART_1.getDescriptionBg(), actual.getDescriptionBg());
        Assertions.assertEquals(TEST_PART_1.getImageURL(), actual.getImageURL());
        Assertions.assertEquals(TEST_PART_1.getBrand().getName(), actual.getBrand().getName());
        Assertions.assertEquals(TEST_PART_1.getBrand().getLogoURL(), actual.getBrand().getLogoURL());
        Assertions.assertEquals(TEST_PART_1.getPrice(), actual.getPrice());
        Assertions.assertEquals(TEST_PART_1.getSize(), actual.getSize());
        Assertions.assertEquals(TEST_PART_1.getWeight(), actual.getWeight());
        Assertions.assertEquals(TEST_PART_1.getSuitableFor(), actual.getSuitableFor());
        Assertions.assertEquals(TEST_PART_1.getMoreInfo(), actual.getMoreInfo());
    }

    @Test
    void testRemoveQuantitiesFromParts() {
        Map<Part, Integer> testMap = Map.of(TEST_PART_1, 5);
        int expectedQuantity = TEST_PART_1.getQuantity() - 5;

        toTest.removeQuantitiesFromParts(testMap);
        verify(partRepository).saveAndFlush(partCaptor.capture());
        Part actual = partCaptor.getValue();

        Assertions.assertEquals(expectedQuantity, actual.getQuantity());
    }

    @Test
    void testIncreaseQuantity() {
        int expectedQuantity = TEST_PART_1.getQuantity() + 5;

        toTest.increaseQuantity(TEST_PART_1, 5);
        verify(partRepository).saveAndFlush(partCaptor.capture());
        Part actual = partCaptor.getValue();

        Assertions.assertEquals(expectedQuantity, actual.getQuantity());
    }

    @Test
    void testGetCartTotalPrice() {
        when(userService.getUserById(TEST_USER.getId())).thenReturn(TEST_USER);
        BigDecimal expected1 = TEST_PART_1.getPrice().multiply(new BigDecimal(TEST_USER.getCart().get(TEST_PART_1)));
        BigDecimal expected2 = TEST_PART_2.getPrice().multiply(new BigDecimal(TEST_USER.getCart().get(TEST_PART_2)));
        BigDecimal expected = expected1.add(expected2);

        BigDecimal actual = toTest.getCartTotalPrice(TEST_USER.getId());

        Assertions.assertEquals(expected, actual);
    }



    private PartDataDTO getTestPartDataDTO(Part part) {
        return new PartDataDTO(
                part.getPartCode(),
                part.getQuantity(),
                part.getDescriptionEn(),
                part.getDescriptionBg(),
                part.getImageURL(),
                part.getBrand().getLogoURL(),
                part.getBrand().getName(),
                part.getPrice(),
                part.getSize(),
                part.getWeight(),
                part.getSuitableFor(),
                part.getMoreInfo());
    }

    private ShortPartDataDTO getTestShortPartDataDTO(Part part) {
        return new ShortPartDataDTO(
                part.getPartCode(),
                part.getImageURL(),
                part.getBrand().getName(),
                part.getBrand().getLogoURL(),
                part.getDescriptionEn(),
                part.getDescriptionBg(),
                part.getPrice(),
                part.getQuantity());
    }
}
