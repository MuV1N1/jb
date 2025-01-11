package de.muv1n.bot;

import de.muv1n.bot.database.repositorys.join.JoinChannelRepository;
import de.muv1n.bot.database.repositorys.join.JoinMessageRepository;
import de.muv1n.bot.database.repositorys.join.JoinRoleRepository;
import de.muv1n.bot.database.services.interfaces.join.JoinRoleService;
import de.muv1n.bot.events.GuildJoinEventListener;
import de.muv1n.bot.database.repositorys.StatusRepository;
import io.micronaut.context.annotation.Value;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.event.ApplicationStartupEvent;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class BotService implements ApplicationEventListener<ApplicationStartupEvent> {
    private static final Logger logger = LoggerFactory.getLogger(BotService.class);
    private final String token;
    private final String guildId;
    private final StatusRepository statusRepository;
    private final JoinRoleRepository roleRepository;
    private final JoinChannelRepository channelRepository;
    private final JoinMessageRepository messageRepository;
    private final JoinRoleService roleService;
    private boolean isStarted = false;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private JDA jda;

    //TODO: User Role on join
    //TODO: Twitch Notifications
    //TODO: Reaction roles
    //TODO: send message

    public BotService(@Value("${discord.bot.test.token}") String token, @Value("${discord.bot.test.guild.id}") String guildId, StatusRepository statusRepository, JoinRoleRepository roleRepository, JoinRoleService roleService, JoinChannelRepository channelRepository, JoinMessageRepository messageRepository) {
        this.statusRepository = statusRepository;
        this.token = token;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.guildId = guildId;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }
    @Override
    public void onApplicationEvent(ApplicationStartupEvent event) {
        if (!isStarted) {
            startBot();
        }
    }

    @PostConstruct
    public void startBot() {
        try {
            jda = JDABuilder.createDefault(token)
                    .setActivity(Activity.playing( "Starting..."))
                    .addEventListeners(new GuildJoinEventListener(roleRepository, channelRepository, messageRepository))
                    .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_MODERATION)
                    .build()
                    .awaitReady();
            isStarted = true;
            logger.info("Bot started successfully!" + jda.getGatewayIntents());
            scheduler.schedule(() -> {
                try {
                    String status = statusRepository.findAll().toString().substring(1, statusRepository.findAll().toString().length() - 1);
                    updateStatus(status);
                } catch (Exception e) {
                    logger.error("Error updating bot status: " + e.getMessage());
                }
            }, 5, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Error starting bot: " + e.getMessage());
            e.printStackTrace();
        }
    }

//    public void uploadRoles(){
//        roleService.clearAll();
//        final int roleCount = jda.getGuildById(guildId).getRoles().size();
//        this.jda.getGuildById(this.guildId).getRoles()
//                .forEach(role -> {
//                    roleRepository.save(new RoleModel(role.getId(), role.getName()));
//                });
//        logger.info("Uploaded " + roleCount + " roles to database");
//    }



    @Singleton
    public void updateStatus(String status) {
        if (jda != null) {
            String newStatus = status;
            logger.info(status);
            if(status.isBlank()) {
                newStatus = "Started";
            }
            jda.getPresence().setActivity(Activity.playing(newStatus));

        }
    }

    public void shutdown() {
        if (jda != null) {
            jda.shutdown();
        }
    }

    public JDA getJda() {
        return jda;
    }

    public String getGuildId() {
        return guildId;
    }
}