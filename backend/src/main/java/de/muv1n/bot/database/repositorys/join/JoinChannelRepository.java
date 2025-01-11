package de.muv1n.bot.database.repositorys.join;

import de.muv1n.bot.database.models.join.JoinChannelModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@MongoRepository(databaseName = "bot")
public interface JoinChannelRepository extends CrudRepository<JoinChannelModel, String> {

    @NonNull
    Iterable<JoinChannelModel> findByNameInList(@NonNull List<String> names);
    
    @NonNull
    Optional<JoinChannelModel> findById(@NonNull String id);
}