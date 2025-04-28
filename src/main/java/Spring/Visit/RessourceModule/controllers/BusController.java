package Spring.Visit.RessourceModule.controllers;

import Spring.Visit.RessourceModule.dto.BusDTO;
import Spring.Visit.RessourceModule.entities.Bus;
import Spring.Visit.RessourceModule.enums.BusAvailability;
import Spring.Visit.RessourceModule.services.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/bus")
public class BusController {
    private final BusService busService;

    @Autowired
    public BusController(BusService busService) {
        this.busService = busService;
    }

    @GetMapping
    public List<BusDTO> GetBus(){
        return busService.GetBus();
    }

    @PostMapping
    public BusDTO addBus(@RequestBody Bus bus){
        return busService.addBus(bus) ;
    }
    /*
@PostMapping
public void addBus(@RequestBody @Valid BusDTO busDTO) {
    busService.addBus(busDTO);
}*/
    @DeleteMapping(path= "{busId}")
    public void deleteBus(@PathVariable ("busId") Long busId){
        busService.deleteBus(busId);

    }
    @PutMapping(path = "{busId}")
    public void updateBus(
            @PathVariable("busId") Long busId,
            @RequestParam BusAvailability availability){
        busService.updateBus(busId, availability);}

    @PutMapping("/{busId}/agency/{agencyId}")
    public BusDTO addBusToAgency(@PathVariable Long busId,@PathVariable Long agencyId){
        return busService.addBusToAgency(busId,agencyId);
    }


    @PutMapping("/{busId}/driver/{driverId}")
    public BusDTO addDriverToBus(@PathVariable Long busId,@PathVariable Long driverId){
        return busService.addDriverToBus(busId,driverId);
    }
}
