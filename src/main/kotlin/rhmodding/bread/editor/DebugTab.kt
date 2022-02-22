package rhmodding.bread.editor

import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextArea
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import rhmodding.bread.model.IDataModel
import java.io.File
import kotlinx.serialization.*
import kotlinx.serialization.json.*


open class DebugTab<F : IDataModel>(editor: Editor<F>) : EditorSubTab<F>(editor, "Debug") {
    
    val body: VBox = VBox().apply {
        isFillWidth = true
    }
    val infoBox: TextArea
    
    init {
        content = ScrollPane(body).apply {
            this.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
            this.vbarPolicy = ScrollPane.ScrollBarPolicy.AS_NEEDED
        }
        
        infoBox = TextArea().apply {
            editableProperty().set(false)
            prefWidthProperty().bind(body.prefWidthProperty())
            prefHeightProperty().bind(body.prefHeightProperty())
        }

        body.children += infoBox
        body.children += Button("Refresh Debug").apply {
            setOnAction {
                populate()
            }
        }

        body.children += Button("Export to JSON").apply {
            setOnAction {
                populate()
                exportToJSON()
            }
        }
        populate()
    }
    
    protected open fun populate() {
        infoBox.text = "$data"
    }

    protected open fun exportToJSON() {
        val json = Json.encodeToString(data)
        val fileChooser = FileChooser()
        fileChooser.title = "Export debug info as a JSON file."
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("json", "*.json"))
        fileChooser.initialDirectory = editor.dataFile.parentFile
        fileChooser.initialFileName = "name of file go here.json"

        val file = fileChooser.showSaveDialog(null)
        if (file != null) {
            file.writeText(json)
        }
    }
    
}