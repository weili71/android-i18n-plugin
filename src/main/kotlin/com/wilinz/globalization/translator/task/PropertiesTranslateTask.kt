package com.wilinz.globalization.translator.task

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.vfs.VirtualFile
import com.wilinz.globalization.translator.translator.PropertiesTranslator
import com.wilinz.globalization.translator.translator.engine.GoogleTranslator
import com.wilinz.globalization.translator.ui.dialog.TranslationConfig
import org.codejive.properties.Properties

class PropertiesTranslateTask(
    project: Project?,
    private val baseFilename: String,
    private val isEncodeUnicode: Boolean,
    private val resourceDir: VirtualFile,
    private val properties: Properties,
    private val config: TranslationConfig,
    title: @NlsContexts.ProgressTitle String
) : TranslateTask(project, title) {
    override fun run(indicator: ProgressIndicator) {
        translator = GoogleTranslator(config.isFirstUppercase)
        PropertiesTranslator.translateForIntellij(
            translator = translator!!,
            baseFilename = baseFilename,
            isEncodeUnicode = isEncodeUnicode,
            resourceDir = resourceDir,
            properties = properties,
            form = config.sourceLanguage,
            to = config.targetLanguages,
            isOverwriteTargetFile = config.isOverwriteTargetFile,
            onEachStart = { index, language ->
                onEachStart(indicator, index, language)
            },
            onEachSuccess = { index, language ->
                onEachSuccess(index, language)
            },
            onEachError = { index, language, error ->
                onEachError(index, language, error)
            }
        )
        onComplete()
    }
}