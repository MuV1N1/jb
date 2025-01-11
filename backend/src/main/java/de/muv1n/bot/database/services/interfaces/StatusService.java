package de.muv1n.bot.database.services.interfaces;

import de.muv1n.bot.database.models.StatusModel;
import io.micronaut.core.annotation.NonNull;

import java.util.List;
import java.util.Optional;

public interface StatusService {

    Iterable<StatusModel> list();

    void clearAll();

    StatusModel save(StatusModel statusModel);

    Optional<StatusModel> find(@NonNull String id);

    Iterable<StatusModel> findByStatusInList(@NonNull List<String> names);

}
