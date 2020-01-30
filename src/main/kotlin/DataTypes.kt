class DataTypes {
    val categoryArea: MutableMap<String, Int> = mutableMapOf()
    val categoryProtection: MutableMap<Int?, String> = mutableMapOf()
    val ozu: MutableMap<String, Int> = mutableMapOf()
    init {
        categoryArea["естественные"] = 1101
        categoryArea["лк"] = 1108

        categoryProtection.putAll(mapOf(304601 to "экспл", 121400 to "во"))
        ozu["бобры"] = 445
        ozu["берегозащитные"] = 62

    }
}