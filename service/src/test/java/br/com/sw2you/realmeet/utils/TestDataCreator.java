package br.com.sw2you.realmeet.utils;

import static br.com.sw2you.realmeet.utils.ConstantsTest.*;

import br.com.sw2you.realmeet.domain.entity.Room;

public final class TestDataCreator {

    private TestDataCreator() {}

    public static Room.RoomBuilder newRoomBuilderDefault() {
        return Room.newRoomBuilder().name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }
}
