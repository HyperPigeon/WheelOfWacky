package net.hyper_pigeon.wacky_wheel.config;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.values.ValueMap;

import java.util.Map;

public class WackyWheelConfig extends WrappedConfig {
    public Map<String, Boolean> enabledSpells = ValueMap.builder(true).build();
    public Map<String, Boolean> enabledTokens = ValueMap.builder(true).build();
}