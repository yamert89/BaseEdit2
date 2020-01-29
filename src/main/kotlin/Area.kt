import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*

class Area(number: Int, numberKv: Int, area: Double, categoryArea: Int, categoryProtection: Int, ozu: Int, lesb: Int) {
    var numberProperty = SimpleIntegerProperty(this, "number", number)
    var number by numberProperty

    var numberKvProperty = SimpleIntegerProperty(this, "numberKv", numberKv)
    var numberKv by numberKvProperty

    var areaProperty = SimpleDoubleProperty(this, "area", area)
    var area by areaProperty

    var categoryAreaProperty = SimpleIntegerProperty(this, "catArea", categoryArea)
    var categoryArea by categoryAreaProperty

    var categoryProtectionProperty = SimpleIntegerProperty(this, "catProt", categoryArea)
    var categoryProtection by categoryProtectionProperty

    var ozuProperty = SimpleIntegerProperty(this, "ozu", ozu)
    var ozu by ozuProperty

    var lesbProperty = SimpleIntegerProperty(this, "lesb", lesb)
    var lesb by lesbProperty
}

class AreaModel: ItemViewModel<Area>(){
    val number = bind(Area::numberProperty) as IntegerProperty
    val numberKv = bind(Area::numberKvProperty) as IntegerProperty
    val area = bind(Area::areaProperty) as DoubleProperty
    val categoryArea = bind(Area::categoryAreaProperty) as IntegerProperty
    val categoryProtection = bind(Area::categoryProtectionProperty) as IntegerProperty
    val ozu = bind(Area::ozuProperty) as IntegerProperty
    val lesb = bind(Area::lesbProperty) as IntegerProperty
}



