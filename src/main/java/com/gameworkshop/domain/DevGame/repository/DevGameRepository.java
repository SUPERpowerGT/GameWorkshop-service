package com.gameworkshop.domain.DevGame.repository;

import com.gameworkshop.domain.DevGame.model.DevGame;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevGameRepository {
    void save(DevGame devGame);

    Optional<DevGame> findById(String id);

    List<DevGame> findByDeveloperProfileId(String developerProfileId);

    boolean deleteById(String id);
}
