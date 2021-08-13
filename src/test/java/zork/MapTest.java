package zork;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {
    @Test
    /**
     * Method to test, if all objects of the map window get created and are visible.
     */
    public void testMap(){
        Map testMap;
        testMap = new Map(650,400,"Map",Constants.MAP);
        assertNotNull(testMap.map);
        assert testMap.map.isVisible();
        assertNotNull(testMap.panel);
        assert testMap.panel.isVisible();
    }
}