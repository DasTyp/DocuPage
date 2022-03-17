package zork;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class ZorkTest
{

    @Test
    public void parseData()
    {
        InputStream instream = ZorkTest.class.getResourceAsStream(Constants.NEW_GAME);
        Game storedGame = Zork.parseData(instream);

        assert storedGame != null;
        assert storedGame.player != null;
        assert !storedGame.rooms.isEmpty();

        //Check ways for room:
        for (Room room : storedGame.rooms)
        {
            //Check if the way list is defined
            Assertions.assertNotNull(room.roomWayList, "Room way list is null. There are no ways defined for the room " + room.getName());

            //Check if there is at least one way for this room
            if(room.roomWayList.isEmpty())
                throw new AssertionError("Each room needs to have at least one way.\nThis room has no way: " + room.getName());
        }
    }
}