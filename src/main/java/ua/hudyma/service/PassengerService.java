package ua.hudyma.service;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hudyma.domain.Passenger;
import ua.hudyma.domain.Profile;
import ua.hudyma.exception.EntityNotCreatedException;
import ua.hudyma.repository.PassengerRepository;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

import static ua.hudyma.enums.PassengerStatus.ACTIVE;

@Service
@RequiredArgsConstructor
@Log4j2
public class PassengerService {
    private final PassengerRepository passengerRepository;

    public HttpStatus addAll(Passenger[] passengers) {
        try {
            passengerRepository.saveAll(Arrays.asList(passengers));
        } catch (Exception e) {
            throw new EntityNotCreatedException("Cannot add all passengers");
        }
        return HttpStatus.CREATED;
    }

    public HttpStatus generatePassengers(Integer number) {
        var list = Stream
                .generate(this::create)
                .limit(number)
                .toList();
        try {
            passengerRepository.saveAll(list);
        } catch (Exception e) {
            throw new EntityNotCreatedException("Passengers have not been generated");
        }
        return HttpStatus.CREATED;
    }

    @Transactional(readOnly = true)
    public Passenger getUserById(Long passengerId) {
        return passengerRepository.findById(passengerId).orElseThrow();
    }

    private Passenger create() {
        var fairy = Fairy.create((Locale.forLanguageTag("uk")));
        var generatedPassenger = fairy.person();
        return populatePassengerFields (generatedPassenger);
    }

    private Passenger populatePassengerFields(Person generatedPassenger) {
        var passenger = new Passenger();
        var profile = new Profile();
        profile.setBirthday(generatedPassenger.getDateOfBirth());
        profile.setName(generatedPassenger.getFirstName());
        profile.setSurname(generatedPassenger.getLastName());
        profile.setEmail(generatedPassenger.getEmail());
        profile.setPhoneNumber(generatedPassenger.getTelephoneNumber());
        passenger.setProfile(profile);
        passenger.setPassengerStatus(ACTIVE);
        return passenger;
    }
}
