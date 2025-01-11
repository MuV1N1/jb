package de.muv1n.websocket.dto.responses;

import de.muv1n.websocket.dto.StatusDTO;

import java.util.List;

public record StatusWsResponse(List<StatusDTO> list) {
}
