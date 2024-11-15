package org.example.csit314bce.Controllers.Buyer;

import org.example.csit314bce.Entity.CarListingEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuyerRecordViewController {

    private final CarListingEntity carListingEntity;

    public BuyerRecordViewController(CarListingEntity carListingEntity) {
        this.carListingEntity = carListingEntity;
    }

    @GetMapping(value = "/InfinityNetwork/buyer/recordView")
    public String buyerRecordView(@RequestParam String carPlateNumber) {
        //Biz logic, if any

        //Pass info to Entity
        String result = this.carListingEntity.recordView(carPlateNumber);

        //Get back from Entity class
        return result;
    }
}