package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.Route;

import java.util.Optional;

public interface RouteRepository extends JpaRepository <Route, Long> {
    Optional<Route> findByRouteId(String routeId);
}
