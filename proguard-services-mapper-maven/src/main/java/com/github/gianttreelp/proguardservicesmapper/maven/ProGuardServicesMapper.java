package com.github.gianttreelp.proguardservicesmapper.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

import static com.github.gianttreelp.proguardservicesmapper.common.MappingKt.mapServices;


@Mojo(
        name = ProGuardServicesMapper.ID,
        defaultPhase = LifecyclePhase.PACKAGE
)
public class ProGuardServicesMapper extends AbstractMojo {
    static final String ID = "map-proguard";

    @Parameter(name = "input", required = true)
    private File input;

    @Parameter(name = "mapping", required = true)
    private File mapping;

    @Override
    public void execute() {
        mapServices(this.mapping.getAbsolutePath(), this.input.getAbsolutePath());
    }
}
