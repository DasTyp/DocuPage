package zork;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    /**
     * Method to test, if all objects of the map window get created and are visible.
     */
    @Test
    public void testMap(){
        Map testMap;
        testMap = new Map(650,400,"Map",Constants.MAP);
        assert testMap.isVisible();
        assert testMap.contains(0,0);
        assert testMap.contains(399,399);
        assert Objects.equals(testMap.getTitle(), "Map");
        assertNotNull(testMap.map);
        assert testMap.map.isVisible();
        assertNotNull(testMap.panel);
        assert testMap.panel.isVisible();
    }
}