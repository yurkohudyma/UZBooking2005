package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hudyma.domain.Route;
import ua.hudyma.domain.Seat;
import ua.hudyma.domain.TrainCar;
import ua.hudyma.exception.EntityNotCreatedException;
import ua.hudyma.repository.RouteRepository;
import ua.hudyma.repository.SeatRepository;
import ua.hudyma.repository.TrainCarRepository;

import java.util.List;

import static java.util.Comparator.comparing;
import static ua.hudyma.enums.TrainCarType.SEAT;

@Service
@RequiredArgsConstructor
@Log4j2
public class TrainCarService {
    private final TrainCarRepository trainCarRepository;
    private final SeatRepository seatRepository;
    private final RouteRepository routeRepository;

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
                .sorted(comparing(Seat::getSeatId))
                .map(Seat::getSeatId)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> getAllRouteCoupledTrainCars (String routeId){
        var route = getRoute(routeId);
        return route
                .getTimetable()
                .getTrainCarList()
                .stream()
                .map(TrainCar::getTrainCarId)
                .toList();
    }

    @Transactional
    public HttpStatus setTrainCarToRoute (String routeId, String trainCarId){
        var timetable = getRoute(routeId).getTimetable();
        var trainCar = getTrainCar(trainCarId);
        try {
            trainCar.setTimetable(timetable);
        } catch (Exception e) {
            throw new IllegalStateException("Traincar " + trainCarId + " has NOT been set to route " + routeId);
        }
        return HttpStatus.CREATED;
    }

    public TrainCar getTrainCar(Integer trainOrderNumber) {
        return trainCarRepository.findByOrderNumber(trainOrderNumber)
                .orElseThrow(() -> new IllegalArgumentException("TrainCar "
                        + trainOrderNumber + " has NOT been found"));
    }
    public TrainCar getTrainCar(String trainCarId) {
        return trainCarRepository.findByTrainCarId(trainCarId)
                .orElseThrow(() -> new IllegalArgumentException("TrainCar "
                        + trainCarId + " has NOT been found"));
    }

    public Route getRoute(String routeId) {
        return routeRepository
                .findByRouteId(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route "
                        + routeId + " has NOT been found"));
    }
}


/**
 * [A][BB][C][DDD][K]
 * A — тип вагона (0 = пасажирський)
 * BB — код залізниці приписки
 * C — тип вагона (2 = міжобласний, 0 = м’який)
 * DDD — технічні характеристики
 * K — контрольна цифра
 */