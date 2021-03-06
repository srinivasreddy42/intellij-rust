/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.cargo.runconfig.command

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import org.rust.ide.icons.RsIcons

class CargoCommandConfigurationType : ConfigurationTypeBase(
    "CargoCommandRunConfiguration",
    "Cargo Command",
    "Cargo command run configuration",
    RsIcons.RUST
) {
    init {
        addFactory(object : ConfigurationFactory(this) {
            override fun createTemplateConfiguration(project: Project): RunConfiguration =
                CargoCommandConfiguration(project, "Cargo", this)

            override fun isConfigurationSingletonByDefault(): Boolean = true
        })
    }

    val factory: ConfigurationFactory get() = configurationFactories.single()

    companion object {
        fun getInstance(): CargoCommandConfigurationType =
            ConfigurationTypeUtil.findConfigurationType(CargoCommandConfigurationType::class.java)
    }
}
