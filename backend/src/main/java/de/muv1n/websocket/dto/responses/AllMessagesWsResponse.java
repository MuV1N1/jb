package de.muv1n.websocket.dto.responses;

import de.muv1n.websocket.dto.AllMessages;

import java.util.List;

public record AllMessagesWsResponse(List<AllMessages> list) {
}
