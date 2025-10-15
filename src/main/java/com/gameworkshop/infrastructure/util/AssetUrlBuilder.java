package com.gameworkshop.infrastructure.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * AssetUrlBuilder
 * 负责生成资源（图片、视频、压缩包等）的完整访问 URL。
 * 属于基础设施层组件，可供 ApplicationService、Controller 等调用。
 */
@Component
public class AssetUrlBuilder {

    @Value("${app.asset-base-url}")
    private String assetBaseUrl;

    private static final String DOWNLOAD_PATH = "/api/developer/devgameasset/download/";

    public String buildDownloadUrl(String assetId) {
        return assetBaseUrl + DOWNLOAD_PATH + assetId;
    }
}
