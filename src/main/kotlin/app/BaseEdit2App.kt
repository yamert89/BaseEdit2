package main.kotlin.app

import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.util.converter.DefaultStringConverter
import javafx.util.converter.IntegerStringConverter
import tornadofx.*
import app.*
import javafx.animation.FadeTransition
import javafx.animation.TranslateTransition
import javafx.event.EventType
import javafx.scene.AccessibleAction
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.scene.input.KeyCharacterCombination
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.layout.BackgroundFill
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import java.time.Duration


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
    var status = Label()
    init {
        primaryStage.setOnCloseRequest {
            if (controller.tableData.isEmpty()) return@setOnCloseRequest
            confirm("Подтверждение", "Сохранить?", cancelButton = ButtonType.NO){
                if (!preSaveCheck()) return@setOnCloseRequest
                controller.save(null)
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
                    var item = selected!!
                    item = Area(0, item.numberKv, 0.0, item.categoryArea, "-", item.ozu, item.lesb, item.rawData)

                    controller.tableData.add(selectedRow, item)



                    tableView!!.selectionModel!!.select(selectedRow,  tableView!!.columns[1])


                    val tableCellCombo = colum!!.cellFactory.call(colum!!)
                    val fieldsCombo = tableCellCombo::class.java
                    tableCellCombo.setOnMouseClicked { print("click") }


                    //val combo = fieldCombo.get(colum!!.cellFactory.call(colum!!)) as ComboBox<String>
                    //combo.executeAccessibleAction(AccessibleAction.EXPAND)





                    try{
                        //colum?.cellFactoryProperty().
                        //colum?.cellFactory.toProperty().get().call(colum).startEdit()
                        //val tableData = tableView?.selectionModel?.selectedCells[0]
                        val cell = colum?.cellFactory.toProperty().value.call(colum)

                        //val gr = cell.graphic as ComboBox<String>
                        //gr.executeAccessibleAction()
                        cell.updateTableColumn(colum)
                        cell.updateTableView(tableView)
                       cell.executeAccessibleAction(AccessibleAction.EXPAND)



                    }catch (e: Exception){
                        e.printStackTrace()
                    }




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
                    style(true) {
                        borderColor += box(Color.GRAY)
                    }
                    isEditable = true
                    // readonlyColumn("№", )
                    readonlyColumn("Кв", Area::numberKv)
                     column("Выд", Area::number).makeEditable().setOnEditCommit {
                         if (it.newValue > 999 || it.newValue < 0) {
                             error("Невалидное значение")
                             editModel.rollbackSelected()
                         }
                         //tableView!!.selectionModel.select(selectedRow, tableView!!.columns[3])

                     }
                    column("Площадь", Area::area).makeEditable().setOnEditCommit {
                        if(it.newValue > 9999 || it.newValue < 0){
                            error("Невалидное значение")
                            editModel.rollbackSelected()
                        }
                    }
                    colum = column("К. защитности", Area::categoryProtection).makeEditable().useComboBox(dataTypes.categoryProtection.values.toList().asObservable())
                   /* colum!!.isEditable = true
                    colum!!.cellFactory = ComboBoxTableCell.forTableColumn()*/


                    //dataTypes.categoryProtection.values.toList().asObservable()


                    //useComboBox<Int>(dataTypes.categoryProtection.keys.toList().asObservable())
                    readonlyColumn("К. земель", Area::categoryArea)
                    column("ОЗУ", Area::ozu).makeEditable().useComboBox(dataTypes.ozu.values.toList().asObservable())
                    column("lesb", Area::lesb).makeEditable().setOnEditCommit {
                        if(it.newValue.length > 4){
                            error("Невалидное значение")
                            editModel.rollbackSelected()
                        }
                    }
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


                    /*areaColumn.setOnEditCommit {
                        if (it.newValue < 0) alert(Alert.AlertType.ERROR, "Error", "Отрицательная площадь")
                    }*/






                    //column("Кат. защ")

                }
            }

            tab("Пакетное обновление"){
                var par1Key: ComboBox<String>? = null
                var par2Key: ComboBox<String>? = null
                var par1Val: TextField? = null
                var par2Val: TextField? = null
                var parRes: ComboBox<String>? = null
                var parResVal: TextField? = null

                //isClosable = false
                val margins = Insets(20.0)
                vbox {
                    hbox{
                        val filterParameters = dataTypes.filterParameters
                        vbox {

                            label("Параметр 1")
                            par1Key = combobox(values = filterParameters) { }
                            label("Параметр 2")
                            par2Key = combobox(values = filterParameters) { }
                            vboxConstraints { margin = margins }
                        }
                        vbox {
                            label("Значение 1")
                            par1Val = textfield {  }
                            label("Значение 2")
                            par2Val = textfield {  }
                            vboxConstraints { margin = margins }
                        }
                        vbox {
                            label("Применить к...")
                            parRes = combobox(values = dataTypes.executeParameters) {  }
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
                                if(parRes!!.value == null || (par1Key!!.value == null && par2Key!!.value == null)){
                                    error("Значения не выбраны")
                                    return@action
                                }

                                controller.executeUtil(
                                    par1Key!!.value to par1Val!!.text,
                                    par2Key!!.value to par2Val!!.text,
                                    parRes!!.value to parResVal!!.text)
                                status.text = "Операция выполнена" //todo ani

                                    //tableViewEditModel.commit()
                            }

                        }
                        vboxConstraints { margin = margins }
                    }
                }

            }


        }

        status = label{
            val fade = FadeTransition()
            fade.node = this
            fade.fromValue = 0.0
            fade.toValue = 1.0
            fade.isAutoReverse = true
            fade.cycleCount = 2
            fade.duration = javafx.util.Duration(500.0)
            vboxConstraints {
                margin = Insets(5.0)
                minWidth = 500.0
            }
            /*style{
                backgroundColor += c(255, 123, 123, 1.0)
            }*/
            textProperty().onChange {
                fade.playFromStart()
            }
        }

        /*//progressBar.attachTo(this)
        //progressBar.prefWidth = this.width
        progressBar = progressbar {
            prefWidth = tableView!!.width
            style(true) {
                borderColor += box(Color.RED)
            }

           *//* progressProperty().addListener {
                observableValue, old, new -> print("VALUE: $observableValue $old $new")
            }*//*
            //isVisible = false
        }*/
    }

    private fun preSaveCheck(): Boolean{
        val dublicate = arrayListOf<Area>()
        val catProt = arrayListOf<Area>()
        val skipped = arrayListOf<Int>()
        val zeroNumber = arrayListOf<Area>()
        val map = mutableMapOf<Int, MutableList<Int>>()
        controller.tableData.forEach {
            if (it.categoryProtection == "-") catProt.add(it)
            if(it.number == 0) zeroNumber.add(it)
            if (!map.containsKey(it.numberKv)) map[it.numberKv] = mutableListOf()
            map[it.numberKv]!!.add(it.number)
        }
        if (map.isNotEmpty()){
            map.forEach {
                if (it.value.distinct().size != it.value.size) dublicate.plus(it)
                if(it.value.containsSkipped()) skipped.add(it.key)
            }
        }

        var message = ""

        if (catProt.size > 0){
            message += "Категория защитности не проставлена в ${catProt.joinToString(", "){"кв: ${it.numberKv} выд: ${it.number}"}}"
        }
        if (zeroNumber.size > 0){
            message += "\nНомер выдела не проставлен в кв ${zeroNumber.joinToString(", "){ it.numberKv.toString()}}"
        }
        if (dublicate.size > 0){
            message += "\nДубликаты в ${dublicate.joinToString { "кв: ${it.numberKv} выд: ${it.number}"}}"
        }
        /*if (skipped.isNotEmpty()){
            message += "\nПропущены выдела в кв ${skipped.joinToString { it.toString() }}"
        }*/

        if (message.isNotBlank()){
            if (message.startsWith("\nПропущены выдела")) confirm("Сохринть?", content = message){
                return true
            } else alert(Alert.AlertType.ERROR, "Ошибка",  message  )
            return false
        }

        return true
    }

    private fun List<Int>.containsSkipped(): Boolean{//fixme

        val sorted = this.sorted()
        var num = sorted.last()
        for(i in (sorted.size - 2) downTo 0){
            if(sorted[i] - num != 1) {
                return true
            }
            num = sorted[i]
        }
        return false
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

