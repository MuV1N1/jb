package de.muv1n.bot.database.repositorys.join;

import de.muv1n.bot.database.models.join.JoinRoleModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@MongoRepository(databaseName = "bot")
public interface JoinRoleRepository extends CrudRepository<JoinRoleModel, String> {

    @NonNull
    Iterable<JoinRoleModel> findByNameInList(@NonNull List<String> names);
    
    @NonNull
    Optional<JoinRoleModel> findById(@NonNull String id);
}