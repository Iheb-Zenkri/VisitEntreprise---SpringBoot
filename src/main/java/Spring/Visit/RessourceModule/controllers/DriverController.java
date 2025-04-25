package Spring.Visit.RessourceModule.controllers;

import Spring.Visit.RessourceModule.entities.Driver;
import Spring.Visit.RessourceModule.services.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import Spring.Visit.RessourceModule.dto.DriverDTO;

import java.util.List;

@RestController
@RequestMapping(path = "api/driver")
public class DriverController {

    private final DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public List<Driver> getDrivers() {
        return driverService.getDrivers();
    }


    @PostMapping
    public void addDriver(@RequestBody @Valid DriverDTO driverDTO) {
        driverService.addDriver(driverDTO);
    }


    @DeleteMapping(path = "{driverId}")
    public void deleteDriver(@PathVariable("driverId") Long driverId) {
        driverService.deleteDriver(driverId);
    }

    @PutMapping(path = "{driverId}")
    public void updateDriver(
            @PathVariable("driverId") Long driverId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long licenseNumber,
            @RequestParam(required = false) Long contactPhone) {
        driverService.updateDriver(driverId, name, licenseNumber, contactPhone);
    }
}
