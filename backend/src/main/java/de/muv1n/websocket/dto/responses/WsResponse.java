package de.muv1n.websocket.dto.responses;

import java.util.List;

public record WsResponse<T>(List<T> data, String type) {}
