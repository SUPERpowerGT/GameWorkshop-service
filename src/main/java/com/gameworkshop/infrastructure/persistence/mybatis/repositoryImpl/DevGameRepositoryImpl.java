package com.gameworkshop.infrastructure.persistence.mybatis.repositoryImpl;

import com.gameworkshop.domain.DevGame.model.DevGame;
import com.gameworkshop.domain.DevGame.repository.DevGameRepository;
import com.gameworkshop.infrastructure.persistence.mybatis.mapper.DevGameMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DevGameRepositoryImpl implements DevGameRepository {
    private final DevGameMapper devGameMapper;

    @Override
    public void save(DevGame devGame) {
        if (devGame.getId() == null || devGame.getId().isEmpty()) {
            devGame.setCreatedAt(LocalDateTime.now());
            devGameMapper.insert(devGame);
        } else {
            devGame.setUpdatedAt(LocalDateTime.now());
            devGameMapper.updateById(devGame);
        }
    }

    @Override
    public Optional<DevGame> findById(String id) {
        return Optional.ofNullable(devGameMapper.selectById(id));
    }

    @Override
    public List<DevGame> findByDeveloperProfileId(String developerProfileId) {
        return devGameMapper.selectByDeveloperProfileId(developerProfileId);
    }

    @Override
    public boolean deleteById(String id) {
        int affectedRows = devGameMapper.deleteById(id);
        return affectedRows > 0;
    }

    @Override
    public void insert(DevGame game) {
        devGameMapper.insert(game);
    }

    @Override
    public void updateById(DevGame game) {
        devGameMapper.updateById(game);
    }

}
