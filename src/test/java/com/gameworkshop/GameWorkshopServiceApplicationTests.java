package com.gameworkshop;

import com.gameworkshop.domain.project.model.DeveloperProfile;
import com.gameworkshop.domain.project.repository.DeveloperProfileMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class GameWorkshopServiceApplicationTests {
    @Autowired
    private DeveloperProfileMapper mapper;

    @Test
    @Transactional
    @Rollback(false)
    void testInsertAndFind() {
        //DeveloperProfile profile = new DeveloperProfile();
        //profile.setId("test_1");
        //profile.setUserId("user_123");
        //profile.setProjectCount(5);

        //mapper.insert(profile);

        DeveloperProfile found = mapper.findById("test_1");
        System.out.println("✅ the result is: " + found);
    }

}
