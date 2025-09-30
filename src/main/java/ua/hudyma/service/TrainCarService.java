package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hudyma.domain.Seat;
import ua.hudyma.domain.TrainCar;
import ua.hudyma.enums.TrainCarType;
import ua.hudyma.exception.EntityNotCreatedException;
import ua.hudyma.repository.SeatRepository;
import ua.hudyma.repository.TrainCarRepository;

import java.util.List;

import static ua.hudyma.enums.TrainCarType.SEAT;

@Service
@RequiredArgsConstructor
@Log4j2
public class TrainCarService {
    private final TrainCarRepository trainCarRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public HttpStatus bindSeatsWithTraincarWhereMissing (Integer trainCarNumber) {
        var trainCar = new TrainCar();
        trainCar.setTrainCarType(SEAT);
        trainCar.setOrderNumber(1);
        try {
            trainCarRepository.save(trainCar);
        } catch (Exception e) {
            throw new EntityNotCreatedException("Traincar " + trainCarNumber + " has NOT been created");
        }
        try {
            seatRepository
                    .findAll()
                    .stream()
                    .filter(seat -> seat.getTrainCar() == null)
                    .forEach(seat -> seat.setTrainCar(trainCar));
        } catch (Exception e) {
            throw new EntityNotCreatedException("seats NOT bound to trainCar " + trainCarNumber);
        }
        return HttpStatus.CREATED;
    }

    public List<String> getAllTrainCarSoldSeats(Integer trainCarNumber) {
        return seatRepository
                .findByTrainCarId (trainCarNumber)
                .stream()
                .map(Seat::getSeatId)
                .toList();
    }
}
