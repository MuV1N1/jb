package de.muv1n.bot.database.services.interfaces.join;

import de.muv1n.bot.database.models.join.JoinChannelModel;
import io.micronaut.core.annotation.NonNull;

import java.util.List;
import java.util.Optional;

public interface JoinChannelService {
    void clearAll();
    Iterable<JoinChannelModel> list();
    JoinChannelModel save(JoinChannelModel channelModel);
    @NonNull Optional<JoinChannelModel> findById(@NonNull String id);
    Iterable<JoinChannelModel> findByRoleNameInList(@NonNull List<String> names);
}
