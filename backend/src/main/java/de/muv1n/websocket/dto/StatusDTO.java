package de.muv1n.websocket.dto;

import java.util.List;

public record StatusDTO(List<StatusData> data, String type) {}
