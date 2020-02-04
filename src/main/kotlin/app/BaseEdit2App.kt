package main.kotlin.app

import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.util.converter.DefaultStringConverter
import javafx.util.converter.IntegerStringConverter
import tornadofx.*
import app.*
import javafx.scene.input.KeyCharacterCombination
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.paint.Color


fun main(args : Array<String>) {
    launch<BaseEdit2>()

}


class BaseEdit2: App(ParentView::class)

class ParentView : View(){
    val controller: GenController by inject()

    var selected: Area? = null
    var selectedRow: Int = 0
    var selectedCol: TableColumn<Area, *>? = null
    var tableView: TableView<Area>? = null
    var progressBar: ProgressBar = ProgressBar()
    val model: AreaModel by inject()
    val dataTypes = DataTypes()
    var colum: TableColumn<Area, String?>? = null
    var tableViewEditModel: TableViewEditModel<Area> by singleAssign()
    init {
        primaryStage.setOnCloseRequest {
            alert(Alert.AlertType.CONFIRMATION,
                "Подтверждение", "Сохранить?",
                ButtonType.OK,
                ButtonType.NO){
                if(it == ButtonType.OK) {
                    if (!preSaveCheck()) return@setOnCloseRequest
                    controller.save(null)
                }
            }
        }
    }


    override val root = vbox {

        hbox {

            button ("Открыть"){
                action {
                    val files = chooseFile("Выберите файл", owner = primaryStage, mode = FileChooserMode.Single, filters = arrayOf())
                    if (files.isEmpty()) return@action
                    controller.tableData.clear()
                    //todo filters
                    //progressBar.isVisible = true
                    println("start")
                    runAsyncWithProgress/*(progress = progressBar)*/ {
                        controller.initData(files[0])
                        println("end init")
                    }ui{
                        //progressBar.isVisible = false
                        println("visible false")
                    }
                    println("end")



                }
                hboxConstraints { margin = Insets(10.0) }


            }


             button("Добавить"){
                 hboxConstraints { margin = Insets(10.0) }
                action {
                    if(selected == null) {
                        println("selected is null")
                        return@action
                    }
                    val item = selected!!
                    controller.tableData.add(selectedRow,
                        Area(0, item.numberKv, 0.0, item.categoryArea, "0", item.ozu, item.lesb, item.rawData)
                    )



                    tableView?.selectionModel?.select(selectedRow,  selectedCol)
                    colum?.cellFactory.toProperty().get().call(colum).startEdit()



                }
                //shortcut(KeyCharacterCombination("+"))
                shortcut(KeyCodeCombination(KeyCode.ADD))


            }
            button("Удалить"){
                hboxConstraints { margin = Insets(10.0) }
                action {
                    //find(app.main.kotlin.app.Modal::class).openModal()
                    alert(Alert.AlertType.CONFIRMATION, "Удалить?", owner = primaryStage, actionFn = {buttonType ->
                        if (buttonType == ButtonType.OK) {
                            controller.tableData.removeAt(selectedRow)
                            tableView!!.selectionModel.select(selectedRow + 1, selectedCol)

                        }
                    } )
                        //val res = alert.showAndWait()
                   /* if (res.get() == ButtonType.OK){
                        controller.tableData.removeAt(selectedRow)
                    }else alert.close()*/

                }
                shortcut(KeyCodeCombination(KeyCode.SUBTRACT))
            }
            button("Сохранить") {
                hboxConstraints { margin = Insets(10.0) }
                action {
                    if(!preSaveCheck()) return@action
                    runAsyncWithProgress {
                        controller.save(null)
                    }
                }

            }

            button("Сохранить как") {
                hboxConstraints { margin = Insets(10.0) }
                action {
                    if(!preSaveCheck()) return@action
                    val list = chooseFile("Выберите файл", mode = FileChooserMode.Save, filters = arrayOf(), owner = primaryStage)
                    val path = list[0].absolutePath
                    runAsyncWithProgress {
                        controller.save(path)
                    }
                }
            }




        }
        tabpane {
            vgrow = Priority.ALWAYS

            tab("Редактор"){
                vgrow = Priority.ALWAYS
                isClosable = false
                tableView = tableview(controller.getData()) {
                    isEditable = true
                    // readonlyColumn("№", )
                    readonlyColumn("Кв", Area::numberKv)
                     column("Выд", Area::number).makeEditable()
                    val areaColumn = column("Площадь", Area::area).makeEditable()
                    colum = column("К. защитности", Area::categoryProtection).makeEditable().useComboBox(dataTypes.categoryProtection.values.toList().asObservable())
                    //useComboBox<Int>(dataTypes.categoryProtection.keys.toList().asObservable())
                    readonlyColumn("К. земель", Area::categoryArea)
                    column("ОЗУ", Area::ozu).makeEditable().useComboBox(dataTypes.ozu.values.toList().asObservable())
                    column("lesb", Area::lesb).makeEditable()
                    selectionModel.selectedItemProperty().onChange {
                        selected = this.selectedItem
                        selectedRow = this.selectedCell?.row ?: selectedRow
                        selectedCol = this.selectedColumn
                    }

                    enableCellEditing() //enables easier cell navigation/editing
                    //enableDirtyTracking() //flags cells that are dirty

                    tableViewEditModel = editModel






                   /* areaColumn.setOnEditCommit {
                        try{
                            it.newValue
                        }
                    }*/


                    areaColumn.setOnEditCommit {
                        if (it.newValue < 0) alert(Alert.AlertType.ERROR, "Error", "Отрицательная площадь")
                    }






                    //column("Кат. защ")

                }
            }

            tab("Утилиты"){
                var par1Key: ComboBox<String>? = null
                var par2Key: ComboBox<String>? = null
                var par1Val: TextField? = null
                var par2Val: TextField? = null
                var parRes: ComboBox<String>? = null
                var parResVal: TextField? = null

                isClosable = false
                val margins = Insets(20.0)
                vbox {
                    hbox{
                        val values = dataTypes.parameters.keys.toList()

                        vbox {

                            label("Параметр 1")
                            par1Key = combobox(values = values) { }
                            label("Параметр 2")
                            par2Key = combobox(values = values) { }
                            vboxConstraints { margin = margins }
                        }
                        vbox {
                            label("Фильтр 1")
                            par1Val = textfield {  }
                            label("Фильтр 2")
                            par2Val = textfield {  }
                            vboxConstraints { margin = margins }
                        }
                        vbox {
                            label("Применить к...")
                            parRes = combobox(values = values) {  }
                            vboxConstraints { margin = margins }
                        }
                        vbox {
                            label("Значение")
                            parResVal = textfield{}
                            vboxConstraints { margin = margins }
                        }
                        vboxConstraints { margin = margins }
                    }
                    vbox{
                        label("Осторожно! Формат значений должен строго совпадать с табличным во избежание потери данных"){
                            vboxConstraints { maxWidth = 400.0 }

                        }
                        button("Применить") {
                            action{
                                controller.executeUtil(
                                    par1Key!!.value to par1Val!!.text,
                                    par2Key!!.value to par2Val!!.text,
                                    parRes!!.value to parResVal!!.text)
                            }

                        }
                        vboxConstraints { margin = margins }
                    }
                }

            }


        }

        //progressBar.attachTo(this)
        //progressBar.prefWidth = this.width
        progressBar = progressbar {
            prefWidth = tableView!!.width
            style(true) {
                borderColor += box(Color.RED)
            }

           /* progressProperty().addListener {
                observableValue, old, new -> print("VALUE: $observableValue $old $new")
            }*/

            //isVisible = false


        }
    }

    private fun preSaveCheck(): Boolean{
        val list = controller.tableData.filter { it.categoryProtection == "0" || it.number == 0 }
        if(list.isNotEmpty()) {
            alert(Alert.AlertType.ERROR,
                "Ошибка",
                "Категория защитности или номер выдела не проставлены в кварталах: ${list.map { it.numberKv }.distinct().joinToString(", "){ it.toString()}}"  )
            return false
        }
        return true
    }


}

class Modal(message: String) : Fragment(){
    override val root = stackpane {
        label(message)

    }
}

class AdvancedStringConverter(private val dataMap: Map<String, String>): DefaultStringConverter(){
    override fun toString(value: String): String {
        return super.toString(dataMap[value])
    }

    override fun fromString(value: String?): String {
        return dataMap.filterValues { it == value }.iterator().next().key
    }
}

class IntToStringConverter(private val dataMap: Map<Int, String>): IntegerStringConverter(){
    override fun toString(value: Int?): String {
        return dataMap[value]!!
    }

    override fun fromString(value: String?): Int {
        return dataMap.filterValues { it == value }.iterator().next().key
    }
}

