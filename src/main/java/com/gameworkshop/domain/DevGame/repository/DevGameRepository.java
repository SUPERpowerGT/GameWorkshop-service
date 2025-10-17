package com.gameworkshop.domain.DevGame.repository;

import com.gameworkshop.domain.DevGame.model.DevGame;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevGameRepository {

    void insert(DevGame game);

    void update(DevGame game);

    Optional<DevGame> findById(String id);

    List<DevGame> findByDeveloperProfileId(String developerProfileId);

    boolean deleteById(String id);

    boolean existsById(String gameId);

    List<DevGame> findAllPaged(@Param("offset") int offset, @Param("pageSize") int pageSize);

    long countAll();
}
