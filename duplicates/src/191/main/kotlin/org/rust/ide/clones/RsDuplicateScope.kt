/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.clones

import com.intellij.lang.LighterAST
import com.intellij.lang.LighterASTNode
import com.intellij.lang.LighterASTTokenNode
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.clones.configuration.DuplicateIndexConfiguration
import com.jetbrains.clones.configuration.DuplicateInspectionConfiguration
import com.jetbrains.clones.core.LightAstNodeHasher
import com.jetbrains.clones.core.longHash
import com.jetbrains.clones.languagescope.DuplicateScope
import com.jetbrains.clones.languagescope.common.CommonDuplicateIndexConfiguration
import com.jetbrains.clones.languagescope.common.CommonDuplicateInspectionConfiguration
import com.jetbrains.clones.languagescope.common.DuplicatesCommonConfigurable
import com.jetbrains.clones.languagescope.common.createCommonOptionsPanel
import com.jetbrains.clones.structures.TextClone
import org.rust.lang.core.psi.RS_COMMENTS
import org.rust.lang.core.psi.RS_LITERALS
import org.rust.lang.core.psi.RsElementTypes
import org.rust.lang.core.psi.RsElementTypes.*
import org.rust.lang.core.psi.tokenSetOf
import javax.swing.JPanel

class RsDuplicateScope : DuplicateScope {

    override val languageName: String = "Rust"

    private val settings = CommonDuplicateIndexConfiguration(languageName)

    override val indexConfiguration: DuplicateIndexConfiguration
        get() = settings

    override fun acceptsFile(project: Project, file: VirtualFile): Boolean =
        ProjectFileIndex.getInstance(project).isInSourceContent(file)

    override fun createConfigurationPanel(state: DuplicateInspectionConfiguration): JPanel {
        return createCommonOptionsPanel(this, state as CommonDuplicateInspectionConfiguration)
    }

    override fun createIndexConfigurable(): Configurable {
        return DuplicatesCommonConfigurable(getPresentableName(), settings)
    }

    override fun createDefaultConfiguration(): DuplicateInspectionConfiguration =
        CommonDuplicateInspectionConfiguration(minSize = 45, isEnabled = true)

    override fun findIndexedElements(ast: LighterAST): List<LighterASTNode> {
        // TODO
        return listOf(ast.root)
    }

    override fun getNormalizedHash(hasher: LightAstNodeHasher, ast: LighterAST, node: LighterASTNode, nodeChildren: List<LighterASTNode>): Long {
        // TODO
        return nodeChildren.map { hasher.getHashInfo(it).hash }.longHash()
    }

    override fun isAnonymized(ast: LighterAST, node: LighterASTTokenNode): Boolean {
        // TODO
        return when (node.tokenType) {
            in RS_LITERALS -> settings.anonymizeLiterals
            else -> true

        }
    }

    override fun isIgnoredAsDuplicate(ast: LighterAST, node: LighterASTNode): Boolean {
        // TODO
        return false
    }

    override fun isInseparableAsDuplicate(ast: LighterAST, node: LighterASTNode): Boolean {
        // TODO
        return false
    }

    override fun isNoise(ast: LighterAST, node: LighterASTNode): Boolean {
        return node.tokenType in RS_COMMENTS
    }

    override fun processDuplicate(project: Project, textClone: TextClone): TextClone = textClone

    override fun weightOf(ast: LighterAST, node: LighterASTNode): Int {
        // TODO:
        return when {
            node is LighterASTTokenNode -> 1
            else -> 0
        }
    }

    companion object {
    }
}
