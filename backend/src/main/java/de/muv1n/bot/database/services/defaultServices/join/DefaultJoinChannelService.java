package de.muv1n.bot.database.services.defaultServices.join;

import de.muv1n.bot.database.models.join.JoinChannelModel;
import de.muv1n.bot.database.repositorys.join.JoinChannelRepository;
import de.muv1n.bot.database.services.interfaces.join.JoinChannelService;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class DefaultJoinChannelService implements JoinChannelService {

    private final JoinChannelRepository channelRepository;

    public DefaultJoinChannelService(JoinChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void clearAll() {
        channelRepository.deleteAll();
    }

    @Override
    public Iterable<JoinChannelModel> list() {
        return channelRepository.findAll();
    }

    @Override
    public JoinChannelModel save(JoinChannelModel channelModel) {
        return channelRepository.save(channelModel);
    }

    @Override
    public Optional<JoinChannelModel> findById(String id){
        return channelRepository.findById(id);
    }

    @Override
    public Iterable<JoinChannelModel> findByRoleNameInList(List<String> names) {
        return channelRepository.findByNameInList(names);
    }

}
