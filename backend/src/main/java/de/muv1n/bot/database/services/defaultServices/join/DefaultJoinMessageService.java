package de.muv1n.bot.database.services.defaultServices.join;

import de.muv1n.bot.database.models.join.JoinChannelModel;
import de.muv1n.bot.database.models.join.JoinMessageModel;
import de.muv1n.bot.database.repositorys.join.JoinChannelRepository;
import de.muv1n.bot.database.repositorys.join.JoinMessageRepository;
import de.muv1n.bot.database.services.interfaces.join.JoinChannelService;
import de.muv1n.bot.database.services.interfaces.join.JoinMessageService;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class DefaultJoinMessageService implements JoinMessageService {

    private final JoinMessageRepository joinMessageRepository;

    public DefaultJoinMessageService(JoinMessageRepository joinMessageRepository) {
        this.joinMessageRepository = joinMessageRepository;
    }

    @Override
    public void clearAll() {
        joinMessageRepository.deleteAll();
    }

    @Override
    public Iterable<JoinMessageModel> list() {
        return joinMessageRepository.findAll();
    }

    @Override
    public JoinMessageModel save(JoinMessageModel joinMessageModel) {
        return joinMessageRepository.save(joinMessageModel);
    }

    @Override
    public Optional<JoinMessageModel> findById(String id){
        return joinMessageRepository.findById(id);
    }

}
