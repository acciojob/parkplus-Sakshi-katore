package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        Optional<User> userOptional=userRepository3.findById(userId);

        if(userOptional==null){
            throw new Exception("Cannot make reservation" );
        }
        User user=userOptional.get();

//      ParkingLot parkingLot=parkingLotRepository3.findById(parkingLotId).get();
        Optional<ParkingLot>optionalParkingLot=parkingLotRepository3.findById(parkingLotId);

        if(optionalParkingLot==null){
            throw new Exception("Cannot make reservation");
        }

        ParkingLot parkingLot=optionalParkingLot.get();

        List<Spot>spotList=parkingLot.getSpotList();

        Spot spot=null;
        int minP=Integer.MAX_VALUE;


        for(Spot spot1:spotList){
            if(spot1.getOccupied()==false){

                Integer noOfVehicle=0;

                if(spot1.getSpotType()== SpotType.TWO_WHEELER){
                    noOfVehicle=2;
                }else if(spot1.getSpotType()== SpotType.FOUR_WHEELER){
                    noOfVehicle=4;
                }else{
                    noOfVehicle=numberOfWheels;
                }

                if(noOfVehicle>=numberOfWheels && minP>spot1.getPricePerHour()){
                    minP=spot1.getPricePerHour();
                    spot=spot1;
                }
            }
        }

        if(spot==null){
            throw new Exception("Cannot make reservation");
        }

//      List<Spot>spotList=spotRepository3.findAllSpot();
//      Spot spot=null;
//      for(Spot spot1:spotList){
//          if(spot1.getOccupied()==false){
//              Integer noOfVehicle=0;
//
//              if(spot1.getSpotType()== TWO_WHEELER){
//                   noOfVehicle=2;
//              }else if(spot1.getSpotType()== FOUR_WHEELER){
//                  noOfVehicle=4;
//              }else{
//                  noOfVehicle=5;
//              }
//
//              if(numberOfWheels<=noOfVehicle){
//                  spot=spot1;
//              }
//          }
//      }
//
//
//       if(spot==null){
//           throw new Exception("Cannot make reservation");
//       }

//        assert spot!= null;

        Reservation reservation=new Reservation();
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(spot);
        reservation.setUser(user);

        spot.setOccupied(true);

        spot.getReservationList().add(reservation);
        user.getReservationList().add(reservation);

        spotRepository3.save(spot);
        userRepository3.save(user);


//       reservationRepository3.save(reservation);


        return reservation;
    }
}
