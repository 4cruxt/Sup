package com.fole_Studios.sup.custom;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class CustomGlideModule extends AppGlideModule
{

    @Override
    public boolean isManifestParsingEnabled()
    {
        return false;
    }

}