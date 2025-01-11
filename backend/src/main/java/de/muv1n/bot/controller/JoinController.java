package de.muv1n.bot.controller;

import de.muv1n.bot.BotService;
import de.muv1n.bot.database.models.join.JoinChannelModel;
import de.muv1n.bot.database.models.join.JoinMessageModel;
import de.muv1n.bot.database.models.join.JoinRoleModel;
import de.muv1n.bot.database.repositorys.join.JoinChannelRepository;
import de.muv1n.bot.database.repositorys.join.JoinMessageRepository;
import de.muv1n.bot.database.repositorys.join.JoinRoleRepository;
import de.muv1n.bot.database.services.interfaces.join.JoinChannelService;
import de.muv1n.bot.database.services.interfaces.join.JoinMessageService;
import de.muv1n.bot.database.services.interfaces.join.JoinRoleService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller("/api/bot/join")
public class JoinController {

    JoinRoleModel model;
    BotService botService;
    @Inject private JoinRoleService roleService;
    @Inject private JoinRoleRepository roleRepository;
    @Inject private JoinChannelService channelService;
    @Inject private JoinChannelRepository channelRepository;
    @Inject private JoinMessageRepository messageRepository;
    @Inject private JoinMessageService joinMessageService;
    @Inject
    private JoinMessageRepository joinMessageRepository;

    JoinController(BotService botService) {
        this.botService = botService;
    }

    @Get("/role")
    public HttpResponse<Iterable<JoinRoleModel>> joinRole() {
        return HttpResponse.ok(roleService.list());
    }



    @Put("/role")
    public HttpResponse<JoinRoleModel> updateJoinRole(String id, String name){
        JoinRoleModel newRole = new JoinRoleModel(id, name);
        roleService.clearAll();
        roleService.save(newRole);
        return HttpResponse.status(HttpStatus.CREATED).body(newRole);
    }

    @Get("/roles")
    public HttpResponse<List<Map<String, String>>> getRoles() {
        List<Role> roles = Objects.requireNonNull(botService.getJda().getGuildById(botService.getGuildId())).getRoles();
        List<Map<String, String>> roleList = roles.stream()
                .map(role -> Map.of("id", role.getId(), "name", role.getName()))
                .collect(Collectors.toList());
        return HttpResponse.ok(roleList);
    }
    @Put("/channel")
    public HttpResponse<JoinChannelModel> updateJoinChannel(String id, String name){
        channelService.clearAll();
        JoinChannelModel newChannel = new JoinChannelModel(id, name);
        channelService.save(newChannel);
        return HttpResponse.status(HttpStatus.CREATED).body(newChannel);
    }

    @Get("/channels")
    public HttpResponse<List<Map<String, String>>> getChannels() {
        List<GuildChannel> channels = Objects.requireNonNull(
                botService.getJda().getGuildById(botService.getGuildId())
        ).getChannels();

        List<Map<String, String>> channelList = channels.stream()
                .filter(channel -> channel instanceof TextChannel)
                .map(channel -> Map.of("id", channel.getId(), "name", channel.getName()))
                .collect(Collectors.toList());

        return HttpResponse.ok(channelList);
    }

    @Get("/channel")
    public HttpResponse<Iterable<JoinChannelModel>> joinChannel() {
        return HttpResponse.ok(channelService.list());
    }

    @Get("/message")
    public HttpResponse<Iterable<JoinMessageModel>> joinMessage() {
        return HttpResponse.ok(joinMessageService.list());
    }

    @Put("/message")
    public HttpResponse<JoinMessageModel> updateJoinMessage(String id, String title, String message, boolean showAvatar){
        channelService.clearAll();
        JoinMessageModel newJoinMessage = new JoinMessageModel(id, title, message, showAvatar);
        joinMessageService.save(newJoinMessage);
        return HttpResponse.status(HttpStatus.CREATED).body(newJoinMessage);
    }

}
