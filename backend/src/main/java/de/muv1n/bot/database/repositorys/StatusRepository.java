package de.muv1n.bot.database.repositorys;

import de.muv1n.bot.database.models.StatusModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@MongoRepository(databaseName = "bot")
public interface StatusRepository extends CrudRepository<StatusModel, String> {

    @NonNull
    Iterable<StatusModel> findByStatusInList(@NonNull List<String> names);

    @NonNull
    Optional<StatusModel> findById(@NonNull String id);

}
