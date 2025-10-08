package ua.hudyma.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import ua.hudyma.domain.Route;
import ua.hudyma.domain.StationTiming;
import ua.hudyma.repository.RouteRepository;
import ua.hudyma.repository.TicketRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketGeneratorService {
    private final TicketRepository ticketRepository;
    private final RouteRepository routeRepository;
    private final TemplateEngine templateEngine;

    @Transactional(readOnly = true)
    public void generateTicketPdf (String ticketId,
                                   HttpServletResponse response) throws IOException, DocumentException {
        var ticket = ticketRepository
                .findByTicketId (ticketId).orElseThrow(
                        () ->
                                new IllegalArgumentException("Ticket id = " + ticketId + " NOT found"));
        var route = routeRepository.findByRouteId(ticket.getRouteId()).orElseThrow();

        var context = new Context();
        var fullname = ticket.getPassenger().getProfile().getName() + " " + ticket.getPassenger().getProfile().getSurname();
        context.setVariable("ticketId", ticketId);
        context.setVariable("name", fullname);
        context.setVariable("trainNumber", ticket.getRouteId());
        context.setVariable("datetimeDeparture", ticket.getDepartureDate() + " " + ticket.getDepartureTime());
        context.setVariable("datetimeArrival", getDatetimeArrival(route));
        var trainCar = ticket.getSeat().getTrainCar();
        context.setVariable("traincar", trainCar.getOrderNumber() + "" + trainCar.getTrainCarType().getDisplayName());
        context.setVariable("seat", ticket.getSeat().getSeatId());
        var departureStation = route.getDepartureStation();
        var arrivalStation = route.getArrivalStation();
        context.setVariable("from", departureStation.getName());
        context.setVariable("fromId", departureStation.getStationId());
        context.setVariable("to", arrivalStation.getName());
        context.setVariable("toId", arrivalStation.getStationId());
        context.setVariable("price", ticket.getTicketPrice());
        context.setVariable("issuedOn", ticket.getCreatedOn());

        //var htmlContent = templateEngine.process("ticket_template", context);
        var htmlContent = templateEngine.process("ticket_template", context);

        var baos = new ByteArrayOutputStream();
        var renderer = new ITextRenderer();
        renderer.getFontResolver().addFont("src/main/resources/fonts/DejaVuSans.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(baos);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ticket.pdf");
        response.getOutputStream().write(baos.toByteArray());
        response.getOutputStream().flush();
    }

    @Transactional(readOnly = true)
    private static String getDatetimeArrival(Route route) {
        var timingsList = route.getTimetable().getInterStationsList();
        var arrivalStationTiming = timingsList.get(timingsList.size() - 1);
        var arrivalDate = route.getTimetable().getClosestDepartureDateAssigned();
        //todo arrival date is never monitored in database. should add to ticket.
        // Here arrival = departure which is invalid for long-haul trains
        return LocalDate.from(arrivalDate) + " " + arrivalStationTiming.getArrivalTime();
    }
}
