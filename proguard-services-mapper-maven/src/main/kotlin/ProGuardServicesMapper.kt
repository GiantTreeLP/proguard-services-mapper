package de.gianttree.proguardservicesmapper.maven

import de.gianttree.proguardservicesmapper.common.mapServices
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import java.nio.file.Path

const val ID = "proguard-services-mapper"

@Mojo(
    name = ID,
    defaultPhase = LifecyclePhase.PACKAGE,
)
class ProGuardServicesMapper : AbstractMojo() {

    @Parameter(name = "$ID.input", required = true)
    private var inputPath: Path? = null

    @Parameter(name = "$ID.mapping", required = true)
    private var mappingPath: Path? = null

    override fun execute() {
        mapServices(inputPath.toString(), mappingPath.toString())
    }
}
