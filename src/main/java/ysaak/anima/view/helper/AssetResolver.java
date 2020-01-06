package ysaak.anima.view.helper;

import com.google.common.base.Preconditions;

public class AssetResolver {
    private final String prefix;

    public AssetResolver(final String prefix) {
        Preconditions.checkNotNull(prefix,"prefix is null");

        this.prefix = prefix;
    }

    public String resolve(String asset) {
        String relativePathToAsset = asset;

        if (relativePathToAsset.startsWith("/"))
            relativePathToAsset = relativePathToAsset.substring(1);

        return String.format("%s/%s", prefix, relativePathToAsset);
    }
}
