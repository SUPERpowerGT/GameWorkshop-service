package com.gameworkshop.infrastructure.persistence.mybatis.mapper;

import com.gameworkshop.domain.DevGameAsset.model.DevGameAsset;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface DevGameAssetMapper {
    //#{}会更加安全，因为这里是完全替换成？是占位符，如果使用？{}就会变成拼接，输入的内容会和整个sql语句拼接在一起，这样的话容易引发sql注入，用户输入未作安全处理
    @Select("SELECT * FROM dev_game_asset WHERE id = #{id}")
    Optional<DevGameAsset> findById(String id);


    List<DevGameAsset> findByDevGameId(String devGameId);


    List<DevGameAsset> findByDevGameIdAndAssetType(
            @Param("devGameId") String devGameId,
            @Param("assetType") String assetType);

    @Insert("INSERT INTO dev_game_asset " +
            "(id, dev_game_id, asset_type, file_name, storage_path, file_size, mime_type, uploaded_at) " +
            "VALUES (#{id}, #{devGameId}, #{assetType}, #{fileName}, #{storagePath}, #{fileSize}, #{mimeType}, #{uploadedAt})")
    int insert(DevGameAsset devGameAsset);

    Optional<DevGameAsset> findFirstByDevGameIdAndAssetType(String devGameId, String assetType);


    int batchInsert(@Param("assets") List<DevGameAsset> assets);

    int updateById(DevGameAsset devGameAsset);

    int deleteById(String id);

    int deleteByDevGameId(String devGameId);

    long countByDevGameId(String devGameId);
}
