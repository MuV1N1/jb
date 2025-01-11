package de.muv1n.bot.controller;

import de.muv1n.bot.BotService;
import de.muv1n.bot.database.models.StatusModel;
import de.muv1n.bot.database.services.interfaces.StatusService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/api/bot")
@ExecuteOn(TaskExecutors.BLOCKING)
public class StatusController {

    @Inject
    BotService service;
    StatusService statusService;
    StatusModel model;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @Get("/status")
    @Status(HttpStatus.CREATED)
    public HttpResponse<Iterable<StatusModel>> list() {
        return HttpResponse.ok(statusService.list());
    }

    @Put("/status")
    @Status(HttpStatus.CREATED)
    public HttpResponse<StatusModel> updateStatus(String status) {
        Logger logger = LoggerFactory.getLogger(StatusController.class);
        logger.info("Updating status to: " + status);
        service.updateStatus(status);
        statusService.clearAll();
        StatusModel newStatus = new StatusModel(status);
        statusService.save(newStatus);
        return HttpResponse.status(HttpStatus.CREATED).body(newStatus);
    }

}
