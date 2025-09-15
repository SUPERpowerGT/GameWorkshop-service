package com.gameworkshop.unit.infrastructure.persistence.mybatis.mapper;

import com.gameworkshop.domain.DeveloperProfile.model.DeveloperProfile;
import com.gameworkshop.domain.DevGame.model.DevGame;
import com.gameworkshop.infrastructure.persistence.mybatis.mapper.DeveloperProfileMapper;
import com.gameworkshop.infrastructure.persistence.mybatis.mapper.DevGameMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
public class DevGameMapperTest {

    @Autowired
    private DevGameMapper devGameMapper;
    @Autowired
    private DeveloperProfileMapper developerProfileMapper;


    private final String TEST_PROFILE_ID = "test_profile_002"; // Parent table (developer) ID
    private final String TEST_USER_ID = "test_user_002";       // User ID associated with developer
    private final String TEST_GAME_ID = "test_game_002";       // Current test game ID
    private final String TEST_GAME_NAME = "Test Adventure Game";// Game name
    private final String TEST_GAME_DESC = "A test game for CRUD operations"; // Game description

    // Test game entity (reusable)
    private DevGame testGame;

    /**
     * Pre-test preparation: First insert parent table (developer_profile) data, then initialize test game entity
     * Ensure parent table data exists before each test case execution (to satisfy foreign key constraints)
     */
    @BeforeEach
    void setUp() {
        // 1. First insert parent table (developer profile) data (dev_game depends on this table's foreign key)
        DeveloperProfile testProfile = new DeveloperProfile();
        testProfile.setId(TEST_PROFILE_ID);
        testProfile.setUserId(TEST_USER_ID);
        testProfile.setProjectCount(0); // Initial project count is 0, can verify update after inserting game
        developerProfileMapper.insert(testProfile);

        // 2. Initialize test game entity (associate with inserted parent table ID)
        testGame = new DevGame();
        testGame.setId(TEST_GAME_ID);
        testGame.setDeveloperProfileId(TEST_PROFILE_ID); // Bind to parent table developer ID
        testGame.setName(TEST_GAME_NAME);
        testGame.setDescription(TEST_GAME_DESC);
        testGame.setReleaseDate(LocalDateTime.of(2025, 12, 25, 10, 0)); // Simulated release date
        testGame.setCreatedAt(LocalDateTime.now());
        testGame.setUpdatedAt(LocalDateTime.now());

        // 3. Insert test game data (prepare for subsequent query/update/delete tests)
        devGameMapper.insert(testGame);
    }

    /**
     * Post-test cleanup: Delete data in "child table → parent table" order to avoid foreign key constraint blocking deletion
     * Ensure data is cleared after each test case to prevent impact on subsequent tests
     */
    @AfterEach
    void tearDown() {
        // 1. First delete child table (dev_game) data
        devGameMapper.deleteById(TEST_GAME_ID);
        // 2. Then delete parent table (developer_profile) data
        developerProfileMapper.deleteById(TEST_PROFILE_ID);
    }

    /**
     * Test insert method: Verify game data can be successfully inserted
     */
    @Test
    void testInsert() {
        // 1. Prepare new test data (different from data in setUp to avoid conflicts)
        String newGameId = "test_game_003";
        DevGame newGame = new DevGame();
        newGame.setId(newGameId);
        newGame.setDeveloperProfileId(TEST_PROFILE_ID); // Associate with existing parent table ID
        newGame.setName("New Test Puzzle Game");
        newGame.setDescription("A new test game for insert operation");
        newGame.setReleaseDate(LocalDateTime.of(2026, 3, 15, 14, 30));
        newGame.setCreatedAt(LocalDateTime.now());
        newGame.setUpdatedAt(LocalDateTime.now());

        // 2. Execute insert operation
        int insertRows = devGameMapper.insert(newGame);

        // 3. Verify results
        assertEquals(1, insertRows, "Failed to insert game data: Should affect 1 row"); // Verify insert row count
        DevGame insertedGame = devGameMapper.selectById(newGameId);    // Verify existence in database
        assertNotNull(insertedGame, "Failed to insert game data: Newly inserted game not found in database");
        assertEquals(newGame.getName(), insertedGame.getName(), "Inserted game name does not match");
        assertEquals(newGame.getDescription(), insertedGame.getDescription(), "Inserted game description does not match");

        // 4. Clean up newly added data from current test (to avoid affecting other tests)
        devGameMapper.deleteById(newGameId);
    }

    /**
     * Test selectById method: Verify game query by ID is accurate
     */
    @Test
    void testSelectById() {
        // 1. Execute query operation (using test game ID inserted in setUp)
        DevGame queriedGame = devGameMapper.selectById(TEST_GAME_ID);

        // 2. Verify results
        assertNotNull(queriedGame, "Failed to query game by ID: No corresponding game found");
        assertEquals(TEST_GAME_ID, queriedGame.getId(), "Queried game ID does not match");
        assertEquals(TEST_GAME_NAME, queriedGame.getName(), "Queried game name does not match");
        assertEquals(TEST_PROFILE_ID, queriedGame.getDeveloperProfileId(), "Queried game's associated developer ID does not match");
        assertEquals(TEST_GAME_DESC, queriedGame.getDescription(), "Queried game description does not match");
    }

    /**
     * Test selectByDeveloperProfileId method: Verify game list query by developer ID is accurate
     */
    @Test
    void testSelectByDeveloperProfileId() {
        // 1. First add another game for current developer (ensure list has multiple data for comprehensive testing)
        String secondGameId = "test_game_004";
        DevGame secondGame = new DevGame();
        secondGame.setId(secondGameId);
        secondGame.setDeveloperProfileId(TEST_PROFILE_ID);
        secondGame.setName("Second Test Racing Game");
        secondGame.setDescription("A second test game for list query");
        secondGame.setReleaseDate(LocalDateTime.of(2026, 5, 20, 9, 15));
        secondGame.setCreatedAt(LocalDateTime.now());
        secondGame.setUpdatedAt(LocalDateTime.now());
        devGameMapper.insert(secondGame);

        // 2. Execute query: Get all games by developer ID
        List<DevGame> gameList = devGameMapper.selectByDeveloperProfileId(TEST_PROFILE_ID);

        // 3. Verify results
        assertNotNull(gameList, "Failed to query game list by developer ID: Returned null");
        assertFalse(gameList.isEmpty(), "Failed to query game list by developer ID: Returned empty list");
        assertEquals(2, gameList.size(), "Mismatch in number of games queried by developer ID: Should be 2"); // 1 from setUp + 1 new

        // Verify all games in list belong to target developer
        boolean allGamesBelongToTargetProfile = gameList.stream()
                .allMatch(game -> TEST_PROFILE_ID.equals(game.getDeveloperProfileId()));
        assertTrue(allGamesBelongToTargetProfile, "Game list contains games not belonging to target developer");

        // 4. Clean up newly added data from current test
        devGameMapper.deleteById(secondGameId);
    }

    /**
     * Test updateById method: Verify game data update by ID is effective
     */
    @Test
    void testUpdateById() {
        // 1. Prepare update data (modify name, description, release date)
        DevGame updateGame = new DevGame();
        updateGame.setId(TEST_GAME_ID); // Must specify ID of game to update
        updateGame.setDeveloperProfileId(TEST_PROFILE_ID); // Foreign key should not be modified, keep original value
        updateGame.setName("Updated Test Adventure Game"); // New name
        updateGame.setDescription("Updated description for test game"); // New description
        updateGame.setReleaseDate(LocalDateTime.of(2025, 11, 11, 0, 0)); // New release date
        updateGame.setUpdatedAt(LocalDateTime.now()); // Update timestamp

        // 2. Execute update operation
        int updateRows = devGameMapper.updateById(updateGame);

        // 3. Verify results
        assertEquals(1, updateRows, "Failed to update game data: Should affect 1 row");
        DevGame updatedGame = devGameMapper.selectById(TEST_GAME_ID);
        assertNotNull(updatedGame, "Failed to query game after update: No corresponding game found");
        assertEquals(updateGame.getName(), updatedGame.getName(), "Game name update failed");
        assertEquals(updateGame.getDescription(), updatedGame.getDescription(), "Game description update failed");
        assertEquals(updateGame.getReleaseDate(), updatedGame.getReleaseDate(), "Game release date update failed");
    }

    /**
     * Test deleteById method: Verify game data deletion by ID is effective
     */
    @Test
    void testDeleteById() {
        // 1. Execute delete operation
        int deleteRows = devGameMapper.deleteById(TEST_GAME_ID);

        // 2. Verify results
        assertEquals(1, deleteRows, "Failed to delete game data: Should affect 1 row");
        DevGame deletedGame = devGameMapper.selectById(TEST_GAME_ID);
        assertNull(deletedGame, "Failed to delete game data: Deleted game still exists in database");
    }
}
