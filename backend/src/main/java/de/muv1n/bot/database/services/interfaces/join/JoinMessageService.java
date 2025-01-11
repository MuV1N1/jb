package de.muv1n.bot.database.services.interfaces.join;

import de.muv1n.bot.database.models.join.JoinChannelModel;
import de.muv1n.bot.database.models.join.JoinMessageModel;
import io.micronaut.core.annotation.NonNull;

import java.util.List;
import java.util.Optional;

public interface JoinMessageService {
    void clearAll();
    Iterable<JoinMessageModel> list();
    JoinMessageModel save(JoinMessageModel messageModel);
    @NonNull Optional<JoinMessageModel> findById(@NonNull String id);
}
