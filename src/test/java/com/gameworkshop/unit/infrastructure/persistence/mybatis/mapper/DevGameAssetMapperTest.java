package com.gameworkshop.unit.infrastructure.persistence.mybatis.mapper;

import com.gameworkshop.domain.DevGameAsset.model.DevGameAsset;
import com.gameworkshop.domain.DeveloperProfile.model.DeveloperProfile;
import com.gameworkshop.domain.DevGame.model.DevGame;
import com.gameworkshop.infrastructure.persistence.mybatis.mapper.DevGameAssetMapper;
import com.gameworkshop.infrastructure.persistence.mybatis.mapper.DeveloperProfileMapper;
import com.gameworkshop.infrastructure.persistence.mybatis.mapper.DevGameMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DevGameAssetMapperTest {
    @Autowired
    private DevGameAssetMapper assetMapper;

    @Autowired
    private DeveloperProfileMapper profileMapper;

    @Autowired
    private DevGameMapper gameMapper;

    private final String testProfileId = "test_profile_001";
    private final String testUserId = "test_user_001";
    private final String testGameId = "test_game_001";
    private final String testAssetId = "test_asset_001";
    private final String testAssetType = "IMAGES";

    private final String batchGameId = "batch_game_001";
    private final String batchProfileId = "batch_profile_001";

    @BeforeEach
    void setUp() {
        DeveloperProfile profile = new DeveloperProfile();
        profile.setId(testProfileId);
        profile.setUserId(testUserId);
        profile.setProjectCount(1);
        profileMapper.insert(profile);

        DeveloperProfile batchProfile = new DeveloperProfile();
        batchProfile.setId(batchProfileId);
        batchProfile.setUserId("batch_user_001");
        batchProfile.setProjectCount(1);
        profileMapper.insert(batchProfile);

        DevGame game = new DevGame();
        game.setId(testGameId);
        game.setDeveloperProfileId(testProfileId);
        game.setName("Test Game");
        game.setDescription("Test Game Description");
        game.setCreatedAt(LocalDateTime.now());
        game.setUpdatedAt(LocalDateTime.now());
        gameMapper.insert(game);

        DevGame batchGame = new DevGame();
        batchGame.setId(batchGameId);
        batchGame.setDeveloperProfileId(batchProfileId);
        batchGame.setName("Batch Test Game");
        batchGame.setDescription("Batch Test Game Description");
        batchGame.setCreatedAt(LocalDateTime.now());
        batchGame.setUpdatedAt(LocalDateTime.now());
        gameMapper.insert(batchGame);

        DevGameAsset asset = new DevGameAsset();
        asset.setId(testAssetId);
        asset.setDevGameId(testGameId);
        asset.setAssetType(testAssetType);
        asset.setFileName("test_screenshot.png");
        asset.setStoragePath("/test/path/screenshot.png");
        asset.setFileSize(102400L);
        asset.setMimeType("image/png");
        asset.setUploadedAt(LocalDateTime.now());

        assetMapper.insert(asset);
    }

    @AfterEach
    void tearDown() {

        assetMapper.deleteById(testAssetId);
        assetMapper.deleteByDevGameId(batchGameId);
        assetMapper.deleteById("test_asset_002");
        assetMapper.deleteById("test_asset_003");
        assetMapper.deleteById("test_asset_004");

        gameMapper.deleteById(testGameId);
        gameMapper.deleteById(batchGameId);

        profileMapper.deleteById(testProfileId);
        profileMapper.deleteById(batchProfileId);
    }

    @Test
    void testFindById() {
        // Execute query
        Optional<DevGameAsset> result = assetMapper.findById(testAssetId);

        // Verify results
        assertTrue(result.isPresent(), "Query by ID should return data");
        DevGameAsset asset = result.get();
        assertEquals(testGameId, asset.getDevGameId(), "Game ID mismatch");
        assertEquals(testAssetType, asset.getAssetType(), "Asset type mismatch");
        assertEquals("test_screenshot.png", asset.getFileName(), "File name mismatch");
    }

    @Test
    void testFindByDevGameId() {
        // Execute query
        List<DevGameAsset> assets = assetMapper.findByDevGameId(testGameId);

        // Verify results
        assertFalse(assets.isEmpty(), "Query by game ID should return at least one record");
        // Verify all results belong to the target game
        assets.forEach(asset -> assertEquals(testGameId, asset.getDevGameId()));
    }

    @Test
    void testFindByDevGameIdAndAssetType() {
        // Execute query
        List<DevGameAsset> assets = assetMapper.findByDevGameIdAndAssetType(testGameId, testAssetType);

        // Verify results
        assertFalse(assets.isEmpty(), "Query by game ID and type should return data");
        assets.forEach(asset -> {
            assertEquals(testGameId, asset.getDevGameId());
            assertEquals(testAssetType, asset.getAssetType());
        });

        // Test non-existent type
        List<DevGameAsset> emptyResult = assetMapper.findByDevGameIdAndAssetType(testGameId, "TRAILER");
        assertTrue(emptyResult.isEmpty(), "Query for non-existent type should return empty list");
    }

    @Test
    void testInsert() {
        // Prepare new data
        String newAssetId = "test_asset_002";
        DevGameAsset newAsset = new DevGameAsset();
        newAsset.setId(newAssetId);
        newAsset.setDevGameId(testGameId);
        newAsset.setAssetType("ICON");
        newAsset.setFileName("test_icon.ico");
        newAsset.setStoragePath("/test/path/icon.ico");
        newAsset.setFileSize(20480L);
        newAsset.setMimeType("image/x-icon");
        newAsset.setUploadedAt(LocalDateTime.now());

        // Execute insertion
        int insertCount = assetMapper.insert(newAsset);
        assertEquals(1, insertCount, "Insert operation should affect 1 row");

        // Verify insertion result
        Optional<DevGameAsset> inserted = assetMapper.findById(newAssetId);
        assertTrue(inserted.isPresent(), "Inserted data not found");
        assertEquals("ICON", inserted.get().getAssetType(), "Asset type insertion error");
    }

    @Test
    void testBatchInsert() {
        DevGameAsset asset1 = new DevGameAsset();
        asset1.setId("batch_asset_001");
        asset1.setDevGameId(batchGameId);
        asset1.setAssetType("INSTALLER");
        asset1.setFileName("setup.exe");
        asset1.setStoragePath("/batch/path/setup.exe");
        asset1.setFileSize(10485760L);
        asset1.setMimeType("application/exe");
        asset1.setUploadedAt(LocalDateTime.now());

        DevGameAsset asset2 = new DevGameAsset();
        asset2.setId("batch_asset_002");
        asset2.setDevGameId(batchGameId);
        asset2.setAssetType("TRAILER");
        asset2.setFileName("trailer.mp4");
        asset2.setStoragePath("/batch/path/trailer.mp4");
        asset2.setFileSize(52428800L);
        asset2.setMimeType("video/mp4");
        asset2.setUploadedAt(LocalDateTime.now());

        List<DevGameAsset> assets = Arrays.asList(asset1, asset2);

        // Execute batch insertion
        int batchCount = assetMapper.batchInsert(assets);
        assertEquals(2, batchCount, "Batch insert should affect 2 rows");

        // Verify results
        assertNotNull(assetMapper.findById("batch_asset_001").orElse(null));
        assertNotNull(assetMapper.findById("batch_asset_002").orElse(null));
    }

    @Test
    void testUpdateById() {
        // Prepare update data
        DevGameAsset updateAsset = new DevGameAsset();
        updateAsset.setId(testAssetId);
        updateAsset.setDevGameId(testGameId);
        updateAsset.setAssetType(testAssetType);
        updateAsset.setFileName("updated_screenshot.png"); // Update file name
        updateAsset.setStoragePath("/test/path/updated_screenshot.png"); // Update path
        updateAsset.setFileSize(204800L); // Update file size
        updateAsset.setMimeType("image/png");
        updateAsset.setUploadedAt(LocalDateTime.now());

        // Execute update
        int updateCount = assetMapper.updateById(updateAsset);
        assertEquals(1, updateCount, "Update operation should affect 1 row");

        // Verify update result
        DevGameAsset updated = assetMapper.findById(testAssetId).orElseThrow();
        assertEquals("updated_screenshot.png", updated.getFileName(), "File name not updated");
        assertEquals(204800L, updated.getFileSize(), "File size not updated");
    }

    @Test
    void testDeleteById() {
        // Execute deletion
        int deleteCount = assetMapper.deleteById(testAssetId);
        assertEquals(1, deleteCount, "Delete operation should affect 1 row");

        // Verify deletion result
        Optional<DevGameAsset> deleted = assetMapper.findById(testAssetId);
        assertFalse(deleted.isPresent(), "Data should not be found after deletion");
    }

    @Test
    void testDeleteByDevGameId() {
        // Insert additional test data first
        DevGameAsset extraAsset = new DevGameAsset();
        extraAsset.setId("test_asset_003");
        extraAsset.setDevGameId(testGameId);
        extraAsset.setAssetType("TRAILER");
        extraAsset.setFileName("extra_trailer.mp4");
        extraAsset.setStoragePath("/test/path/extra_trailer.mp4");
        extraAsset.setFileSize(307200L);
        extraAsset.setMimeType("video/mp4");
        extraAsset.setUploadedAt(LocalDateTime.now());
        assetMapper.insert(extraAsset);

        // Execute batch deletion
        int deleteCount = assetMapper.deleteByDevGameId(testGameId);
        assertEquals(2, deleteCount, "Delete operation should affect 2 rows (initial + extra data)");

        // Verify results
        assertTrue(assetMapper.findByDevGameId(testGameId).isEmpty(), "Deletion by game ID is incomplete");
    }

    @Test
    void testCountByDevGameId() {
        // Initial count
        long initialCount = assetMapper.countByDevGameId(testGameId);
        assertEquals(1, initialCount, "Initial count is incorrect");

        // Add a new record
        DevGameAsset newAsset = new DevGameAsset();
        newAsset.setId("test_asset_004");
        newAsset.setDevGameId(testGameId);
        newAsset.setAssetType("IMAGES");
        newAsset.setFileName("count_test.png");
        newAsset.setStoragePath("/test/path/count_test.png");
        newAsset.setFileSize(51200L);
        newAsset.setMimeType("image/png");
        newAsset.setUploadedAt(LocalDateTime.now());
        assetMapper.insert(newAsset);

        // Verify count increment
        long updatedCount = assetMapper.countByDevGameId(testGameId);
        assertEquals(initialCount + 1, updatedCount, "Count update is incorrect");
    }
}
