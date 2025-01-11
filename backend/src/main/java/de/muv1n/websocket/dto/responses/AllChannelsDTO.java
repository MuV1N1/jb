package de.muv1n.websocket.dto.responses;

import de.muv1n.websocket.dto.AllChannels;
import de.muv1n.websocket.dto.RoleData;

import java.util.List;

public record AllChannelsDTO(List<AllChannels> data, String type) {}
