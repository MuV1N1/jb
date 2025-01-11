package de.muv1n.bot.events;

import de.muv1n.bot.database.models.join.JoinChannelModel;
import de.muv1n.bot.database.models.join.JoinMessageModel;
import de.muv1n.bot.database.models.join.JoinRoleModel;
import de.muv1n.bot.database.repositorys.join.JoinChannelRepository;
import de.muv1n.bot.database.repositorys.join.JoinMessageRepository;
import de.muv1n.bot.database.repositorys.join.JoinRoleRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.Channel;
import java.util.List;

public class GuildJoinEventListener extends ListenerAdapter {

    private final JoinRoleRepository roleRepository;
    private final JoinChannelRepository channelRepository;
    private final JoinMessageRepository messageRepository;

    public GuildJoinEventListener(JoinRoleRepository roleRepository, JoinChannelRepository channelRepository, JoinMessageRepository messageRepository) {
        this.roleRepository = roleRepository;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();
        Logger logger = LoggerFactory.getLogger(GuildJoinEventListener.class);
        List<JoinRoleModel> roleModels = roleRepository.findAll();
        for (JoinRoleModel roleModel : roleModels) {
            Role role = guild.getRoleById(roleModel.getId());
            if (role != null) {
                guild.addRoleToMember(member, role).complete();
            } else {
                logger.error("Role with ID " + roleModel.getId() + " not found in guild");
            }
        }

        List<JoinChannelModel> channelModels = channelRepository.findAll();
        List<JoinMessageModel> messageModels = messageRepository.findAll();

        for (JoinChannelModel channelModel : channelModels) {
            String title = "";
            String message = "";
            boolean showAvatar = false;
            for (JoinMessageModel messageModel : messageModels) {
                title = messageModel.getTitle();
                message = messageModel.getMessage();
                showAvatar = messageModel.getShowAvatar();
            }
            TextChannel channel = guild.getTextChannelById(channelModel.getId());
            channel.sendMessageEmbeds(builder(title, message, showAvatar, member).build()).queue();
        }

    }

    private EmbedBuilder builder (String title, String message, boolean showAvatar, Member member) {
        EmbedBuilder embed = new EmbedBuilder();

        if (showAvatar) {
            embed.setThumbnail(member.getAvatarUrl());
        }

        embed.setTitle(title);
        embed.setDescription(message);
        return embed;
    }
}
