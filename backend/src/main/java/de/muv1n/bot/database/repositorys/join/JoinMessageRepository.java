package de.muv1n.bot.database.repositorys.join;

import de.muv1n.bot.database.models.join.JoinMessageModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@MongoRepository(databaseName = "bot")
public interface JoinMessageRepository extends CrudRepository<JoinMessageModel, String> {
    @NonNull
    Optional<JoinMessageModel> findById(@NonNull String id);
}