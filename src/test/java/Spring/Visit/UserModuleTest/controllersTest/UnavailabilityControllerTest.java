package Spring.Visit.UserModuleTest.controllersTest;

import Spring.Visit.UserModule.controllers.UnavailabilityController;
import Spring.Visit.UserModule.entities.Unavailability;
import Spring.Visit.UserModule.services.UnavailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnavailabilityControllerTest {

    @Mock
    private UnavailabilityService unavailabilityService;

    @InjectMocks
    private UnavailabilityController unavailabilityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUnavailability_ShouldReturnList() {
        Long teacherId = 1L;
        List<Unavailability> mockList = Arrays.asList(new Unavailability(), new Unavailability());
        when(unavailabilityService.getUnavailability(teacherId)).thenReturn(mockList);

        List<Unavailability> response = unavailabilityController.getUnavailability(teacherId);

        assertNotNull(response);
        assertEquals(2, response.size());
        verify(unavailabilityService, times(1)).getUnavailability(teacherId);
    }
    @Test
    @WithMockUser(roles = "TEACHER")
    void addUnavailability_ShouldReturnUnavailability() {
        Unavailability mockUnavailability = new Unavailability();
        when(unavailabilityService.addUnavailability(mockUnavailability)).thenReturn(mockUnavailability);

        Unavailability response = unavailabilityController.addUnavailability(mockUnavailability);

        assertNotNull(response);
        verify(unavailabilityService, times(1)).addUnavailability(mockUnavailability);
    }
    @Test
    @WithMockUser(roles = "TEACHER")
    void updateUnavailability_ShouldReturnUpdatedUnavailability() {
        Long id = 1L;
        Unavailability updatedUnavailability = new Unavailability();
        when(unavailabilityService.updateUnavailability(id, updatedUnavailability)).thenReturn(updatedUnavailability);

        Unavailability response = unavailabilityController.updateUnavailability(id, updatedUnavailability);

        assertNotNull(response);
        verify(unavailabilityService, times(1)).updateUnavailability(id, updatedUnavailability);
    }
    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteUnavailability_ShouldCallService() {
        Long id = 1L;
        doNothing().when(unavailabilityService).deleteUnavailability(id);

        unavailabilityController.deleteUnavailability(id);

        verify(unavailabilityService, times(1)).deleteUnavailability(id);
    }
}
