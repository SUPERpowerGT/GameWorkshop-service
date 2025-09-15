package com.gameworkshop.unit.infrastructure.persistence.mybatis.mapper;

import com.gameworkshop.domain.DeveloperProfile.model.DeveloperProfile;
import com.gameworkshop.infrastructure.persistence.mybatis.mapper.DeveloperProfileMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DeveloperProfileMapperTest {

    @Autowired
    private DeveloperProfileMapper profileMapper;

    // Test data constants
    private final String testId = "test_profile_123";
    private final String testUserId = "test_user_456";

    /**
     * Sets up test data before each test method execution.
     * Creates and inserts a base developer profile for testing.
     */
    @BeforeEach
    void setUp() {
        DeveloperProfile profile = new DeveloperProfile();
        profile.setId(testId);
        profile.setUserId(testUserId);
        profile.setProjectCount(0);
        profileMapper.insert(profile);
    }

    /**
     * Cleans up test data after each test method execution.
     * Removes all test-specific records to prevent interference between tests.
     */
    @AfterEach
    void tearDown() {
        profileMapper.deleteById(testId);
        profileMapper.deleteByUserId("batch_user_1");
        profileMapper.deleteByUserId("batch_user_2");
    }

    /**
     * Tests the findById method to verify it correctly retrieves a developer profile by its ID.
     */
    @Test
    void testFindById() {
        Optional<DeveloperProfile> profile = profileMapper.findById(testId);

        // Verify the profile was found
        assertTrue(profile.isPresent(), "Profile should be found by the given ID");
        // Verify the retrieved profile has the correct user ID
        assertEquals(testUserId, profile.get().getUserId(), "Retrieved profile has incorrect user ID");
        // Verify the mapper count is initialized correctly
        assertEquals(0, profile.get().getProjectCount(), "Retrieved profile has incorrect mapper count");
    }

    /**
     * Tests the findByUserId method to verify it correctly retrieves a developer profile by its associated user ID.
     */
    @Test
    void testFindByUserId() {
        Optional<DeveloperProfile> profile = profileMapper.findByUserId(testUserId);

        // Verify the profile was found
        assertTrue(profile.isPresent(), "Profile should be found by the given user ID");
        // Verify the retrieved profile has the correct ID
        assertEquals(testId, profile.get().getId(), "Retrieved profile has incorrect ID");
    }

    /**
     * Tests the findAll method to verify it retrieves all existing developer profiles.
     */
    @Test
    void testFindAll() {
        List<DeveloperProfile> profiles = profileMapper.findAll();

        // Verify at least the test profile is returned
        assertTrue(profiles.size() >= 1, "findAll should return at least one profile");
    }

    /**
     * Tests the insert method to verify new developer profiles can be created.
     */
    @Test
    void testInsert() {
        String newId = "new_profile_789";
        String newUserId = "new_user_000";

        // Create a new profile to insert
        DeveloperProfile newProfile = new DeveloperProfile();
        newProfile.setId(newId);
        newProfile.setUserId(newUserId);
        newProfile.setProjectCount(2);

        // Perform the insert operation
        profileMapper.insert(newProfile);

        // Verify the profile was inserted successfully
        Optional<DeveloperProfile> inserted = profileMapper.findById(newId);
        assertTrue(inserted.isPresent(), "Insert operation failed - new profile not found");
        assertEquals(2, inserted.get().getProjectCount(), "Inserted profile has incorrect mapper count");

        // Clean up the test data
        profileMapper.deleteById(newId);
    }

    /**
     * Tests the batchInsert method to verify multiple developer profiles can be created in one operation.
     */
    @Test
    void testBatchInsert() {
        // Create multiple profiles for batch insertion
        DeveloperProfile profile1 = new DeveloperProfile();
        profile1.setId("batch_profile_1");
        profile1.setUserId("batch_user_1");
        profile1.setProjectCount(1);

        DeveloperProfile profile2 = new DeveloperProfile();
        profile2.setId("batch_profile_2");
        profile2.setUserId("batch_user_2");
        profile2.setProjectCount(3);

        List<DeveloperProfile> profiles = Arrays.asList(profile1, profile2);

        // Perform batch insertion
        int affectedRows = profileMapper.batchInsert(profiles);
        assertEquals(2, affectedRows, "Batch insert affected an incorrect number of rows");

        // Verify both profiles were inserted
        Optional<DeveloperProfile> inserted1 = profileMapper.findById("batch_profile_1");
        Optional<DeveloperProfile> inserted2 = profileMapper.findById("batch_profile_2");
        assertTrue(inserted1.isPresent() && inserted2.isPresent(), "Batch insert failed - not all profiles were created");
    }

    /**
     * Tests the updateById method to verify existing developer profiles can be updated.
     */
    @Test
    void testUpdateById() {
        // Create an updated version of the test profile
        DeveloperProfile updateProfile = new DeveloperProfile();
        updateProfile.setId(testId);
        updateProfile.setUserId(testUserId); // Keep the same user ID
        updateProfile.setProjectCount(5); // Update the mapper count

        // Perform the update operation
        int affectedRows = profileMapper.updateById(updateProfile);
        assertEquals(1, affectedRows, "Update operation affected an incorrect number of rows");

        // Verify the profile was updated correctly
        Optional<DeveloperProfile> updatedProfile = profileMapper.findById(testId);
        assertTrue(updatedProfile.isPresent());
        assertEquals(5, updatedProfile.get().getProjectCount(), "Profile mapper count was not updated correctly");
    }

    /**
     * Tests the updateProjectCountById method to verify mapper counts can be updated independently.
     */
    @Test
    void testUpdateProjectCountById() {
        // Update just the mapper count for the test profile
        int affectedRows = profileMapper.updateProjectCountById(testId, 10);
        assertEquals(1, affectedRows, "Project count update affected an incorrect number of rows");

        // Verify the mapper count was updated correctly
        Optional<DeveloperProfile> updatedProfile = profileMapper.findById(testId);
        assertTrue(updatedProfile.isPresent());
        assertEquals(10, updatedProfile.get().getProjectCount(), "Project count was not updated correctly");
    }

    /**
     * Tests the deleteById method to verify developer profiles can be deleted by their ID.
     */
    @Test
    void testDeleteById() {
        // Perform the delete operation
        int affectedRows = profileMapper.deleteById(testId);
        assertEquals(1, affectedRows, "Delete operation affected an incorrect number of rows");

        // Verify the profile was actually deleted
        Optional<DeveloperProfile> deletedProfile = profileMapper.findById(testId);
        assertFalse(deletedProfile.isPresent(), "Delete operation failed - profile still exists");
    }

    /**
     * Tests the deleteByUserId method to verify developer profiles can be deleted by their associated user ID.
     */
    @Test
    void testDeleteByUserId() {
        // Perform the delete operation
        int affectedRows = profileMapper.deleteByUserId(testUserId);
        assertEquals(1, affectedRows, "Delete by user ID affected an incorrect number of rows");

        // Verify the profile was actually deleted
        Optional<DeveloperProfile> deletedProfile = profileMapper.findByUserId(testUserId);
        assertFalse(deletedProfile.isPresent(), "Delete by user ID failed - profile still exists");
    }

    /**
     * Tests the count method to verify it correctly counts all existing developer profiles.
     */
    @Test
    void testCount() {
        // Get the initial count of profiles
        long initialCount = profileMapper.count();

        // Create and insert a temporary profile
        DeveloperProfile tempProfile = new DeveloperProfile();
        tempProfile.setId("temp_profile_999");
        tempProfile.setUserId("temp_user_999");
        tempProfile.setProjectCount(0);
        profileMapper.insert(tempProfile);

        // Verify the count increased by 1 after insertion
        assertEquals(initialCount + 1, profileMapper.count(), "Count did not increase correctly after insertion");

        // Clean up the temporary profile
        profileMapper.deleteById("temp_profile_999");
    }

}
