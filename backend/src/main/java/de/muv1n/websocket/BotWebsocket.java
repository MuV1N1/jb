package de.muv1n.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muv1n.bot.BotService;
import de.muv1n.bot.database.models.StatusModel;
import de.muv1n.bot.database.models.join.JoinChannelModel;
import de.muv1n.bot.database.models.join.JoinRoleModel;
import de.muv1n.bot.database.services.defaultServices.join.DefaultJoinRoleService;
import de.muv1n.bot.database.services.interfaces.StatusService;
import de.muv1n.bot.database.services.interfaces.join.JoinChannelService;
import de.muv1n.bot.database.services.interfaces.join.JoinMessageService;
import de.muv1n.bot.database.services.interfaces.join.JoinRoleService;
import de.muv1n.websocket.dto.*;
import de.muv1n.websocket.dto.responses.AllChannelsDTO;
import de.muv1n.websocket.dto.responses.RoleDTO;
import de.muv1n.websocket.dto.responses.WsResponse;
import io.micronaut.context.annotation.Requires;
import io.micronaut.scheduling.ScheduledExecutorTaskScheduler;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.*;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;

@ServerWebSocket("/ws/{topic}")
@Singleton
@Requires(classes = ObjectMapper.class)
public class BotWebsocket {

    private final ConcurrentHashMap<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

    @Inject
    private WebSocketBroadcaster broadcaster;
    private BotService botService;
    private StatusService statusService;
    private JoinMessageService joinMessageService;
    private JoinRoleService joinRoleService;
    private JoinChannelService joinChannelService;
    @Named("scheduled")
    @Inject
    private ScheduledExecutorTaskScheduler scheduledExecutorTaskScheduler;
    private final ObjectMapper objectMapper;
    private final Logger logger;
    @Inject
    private DefaultJoinRoleService defaultJoinRoleService;

    public BotWebsocket(WebSocketBroadcaster broadcaster, BotService botService, StatusService statusService, JoinMessageService joinMessageService, ObjectMapper objectMapper, JoinRoleService joinRoleService, JoinChannelService joinChannelService) {
        this.broadcaster = broadcaster;
        this.botService = botService;
        this.statusService = statusService;
        this.joinMessageService = joinMessageService;
        this.objectMapper = objectMapper;
        this.joinRoleService = joinRoleService;
        this.joinChannelService = joinChannelService;
        this.logger = LoggerFactory.getLogger(BotWebsocket.class);
    }

    @OnOpen
    public void onOpen(WebSocketSession session, String topic) throws JsonProcessingException {
        session.sendSync(objectMapper.writeValueAsString(new WsResponse<>(null, "Welcome")));
        activeSessions.put(session.getId(), session);
    }

    @OnMessage
    public void onMessage(String message, WebSocketSession session, String topic) {
        try {
            String[] parts = message.split(":", 2);
            String command = parts[0];
            String data = parts.length > 1 ? parts[1] : "";

            System.out.println(message);
            System.out.println("data: " + data);

            switch (command) {
                case "getStatus":
                    session.sendSync(getStatusJson());
                    break;
                case "updateStatus":
                    logger.info(data);
                    session.sendSync(data);
                    updateStatus(data);
                    break;
                case "getRoles":
                    session.sendSync(getRolesJson());
                    break;
                case "updateRole":
                    updateRole(data);
                    session.sendSync(data);
                    break;
                case "getChannels":
                    session.sendSync(getChannelsJson());
                    break;
                case "getMessage":
                    session.sendSync(getMessageJson());
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            session.sendSync("Error handling message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = "10s", scheduler = TaskExecutors.SCHEDULED)
    void sendPing() {
        activeSessions.values().forEach(session -> {
            try {
                session.sendSync(objectMapper.writeValueAsString(new WsResponse<>(null, "ping")));
            } catch (Exception e) {
                System.err.println("Error sending ping: " + e.getMessage());
                e.printStackTrace();
                activeSessions.remove(session.getId());
            }
        });
    }

    @OnClose
    public void onClose(WebSocketSession session, String topic) {
        System.out.println("Client disconnected from topic: " + topic);
        activeSessions.remove(session.getId(), session);
    }

    @OnError
    public void onError(WebSocketSession session, Throwable error) {
        System.err.println("Error in WebSocket session: " + error.getMessage());
        error.printStackTrace();
    }

    // Get the status as JSON
    private String getStatusJson() throws JsonProcessingException {
        List<StatusData> data = StreamSupport.stream(statusService.list().spliterator(), false)
                .map(message -> new StatusData(message.getStatus()))
                .toList();
        return objectMapper.writeValueAsString(new WsResponse<>(data, "status"));
    }

    // Update the status based on received JSON
    private void updateStatus(String statusJson) {
        logger.info("Received status update JSON: {}", statusJson);

        try {
            // Deserialize the JSON into StatusDTO
            StatusDTO statusDTO = objectMapper.readValue(statusJson, StatusDTO.class);

            // Extract the status data from the StatusDTO
            String newStatus = statusDTO.data().getFirst().status();
            logger.info("Extracted status: {}", newStatus);

            // Perform the update
            botService.updateStatus(newStatus);  // Update the bot's internal status
            statusService.clearAll();  // Clear old status entries
            StatusModel newStatusModel = new StatusModel(newStatus);  // Create a new status model
            statusService.save(newStatusModel);  // Save the updated status
            logger.info("Status successfully updated in the database.");
        } catch (Exception e) {
            logger.error("Error processing status update JSON: {}", e.getMessage(), e);
        }
    }

    // Get roles as JSON
    private String getRolesJson() throws JsonProcessingException {
        List<Role> roles = botService.getJda().getGuildById(botService.getGuildId()).getRoles();
        List<AllRoles> data = roles.stream().map(role -> new AllRoles(role.getId(), role.getName())).toList();
        return objectMapper.writeValueAsString(new WsResponse<>(data, "roles"));
    }

    // Update role (not yet implemented)
    private void updateRole(String roleJson) throws JsonProcessingException {
        logger.info("Received role update JSON: {}", roleJson);

        try {
            RoleDTO roleDTO = objectMapper.readValue(roleJson, RoleDTO.class);

            String newRoleId = roleDTO.data().getFirst().id();
            String newRoleName = roleDTO.data().getFirst().name();
            logger.info("Extracted role: {}, {}", newRoleId, newRoleName);

            joinRoleService.clearAll();  // Clear old status entries
            JoinRoleModel newJoinRoleModel = new JoinRoleModel(newRoleId, newRoleName);  // Create a new status model
            joinRoleService.save(newJoinRoleModel);  // Save the updated status
            logger.info("Role successfully updated in the database.");
        } catch (Exception e) {
            logger.error("Error processing status update JSON: {}", e.getMessage(), e);
        }

    }
    // Get channels as JSON
    private String getChannelsJson() throws JsonProcessingException {
        List<GuildChannel> channels = botService.getJda().getGuildById(botService.getGuildId()).getChannels().stream()
                .filter(channel -> channel instanceof TextChannel)
                .toList();
        List<AllChannels> data = channels.stream().map(channel -> new AllChannels(channel.getId(), channel.getName())).toList();
        return objectMapper.writeValueAsString(new WsResponse<>(data, "channels"));
    }
    private void updateChannel(String channelJson) throws JsonProcessingException {
        logger.info("Received channel update JSON: {}", channelJson);

        try {
            AllChannelsDTO allChannelsDTO = objectMapper.readValue(channelJson, AllChannelsDTO.class);

            String newChannelId = allChannelsDTO.data().get(0).id();
            String newChannelName = allChannelsDTO.data().get(0).name();
            logger.info("Extracted Channel: {}, {}", newChannelId, newChannelName);

            joinChannelService.clearAll();
            JoinChannelModel newJoinChannelModel = new JoinChannelModel(newChannelId, newChannelName);  // Create a new status model
            joinChannelService.save(newJoinChannelModel);  // Save the updated status
            logger.info("Channel successfully updated in the database.");
        } catch (Exception e) {
            logger.error("Error processing status update JSON: {}", e.getMessage(), e);
        }
    }
    //Joinmessage
    private String getMessageJson() throws JsonProcessingException {
        List<AllMessages> data = StreamSupport.stream(joinMessageService.list().spliterator(), false)
                .map(message -> new AllMessages(message.getTitle(), message.getMessage(), message.getShowAvatar()))
                .toList();
        return objectMapper.writeValueAsString(new WsResponse<>(data, "message"));
    }
    private void updateJoinMessage(){

    }
}
