package ua.hudyma.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import ua.hudyma.repository.RouteRepository;
import ua.hudyma.repository.TicketRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TicketGeneratorService {
    private final TicketRepository ticketRepository;
    private final RouteRepository routeRepository;
    private final TemplateEngine templateEngine;

    public void generateTicketPdf (String ticketId,
                                   HttpServletResponse response)
            throws DocumentException, IOException {
        var ticket = ticketRepository
                .findByTicketId (ticketId).orElseThrow(
                        () ->
                                new IllegalArgumentException("Ticket id = " + ticketId + " NOT found"));
        var route = routeRepository.findByRouteId(ticket.getRouteId()).orElseThrow();

        var context = new Context();
        var fullname = ticket.getPassenger().getProfile().getName() + " " + ticket.getPassenger().getProfile().getSurname();
        context.setVariable("name", fullname);
        context.setVariable("trainNumber", ticket.getRouteId());
        context.setVariable("date", ticket.getDepartureDate());
        context.setVariable("time", ticket.getDepartureTime());
        var trainCar = ticket.getSeat().getTrainCar();
        context.setVariable("traincar", trainCar.getOrderNumber() + " " + trainCar.getTrainCarType());
        context.setVariable("seat", ticket.getSeat().getSeatId());
        var departureStation = route.getDepartureStation();
        var arrivalStation = route.getArrivalStation();
        var depStation = departureStation.getName() + "(" + departureStation.getStationId() + ")";
        context.setVariable("from", depStation);
        var arrStation = arrivalStation.getName() + "(" + arrivalStation.getStationId() + ")";
        context.setVariable("to", arrStation);

        var htmlContent = templateEngine.process("ticket_pdf", context);

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
}
