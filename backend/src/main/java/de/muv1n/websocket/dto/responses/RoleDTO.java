package de.muv1n.websocket.dto.responses;

import de.muv1n.websocket.dto.RoleData;

import java.util.List;

public record RoleDTO(List<RoleData> data, String type) {}
