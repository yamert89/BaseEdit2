class DataTypes {
    val categoryArea: MutableMap<String, Int> = mutableMapOf()
    val categoryProtection: MutableMap<String, Int> = mutableMapOf()
    val ozu: MutableMap<String, Int> = mutableMapOf()
    init {
        categoryArea["естественные"] = 1101
        categoryArea["лк"] = 1108

        categoryProtection.putAll(mapOf("экспл" to 304601, "во" to 121400))
        ozu["бобры"] = 445
        ozu["берегозащитные"] = 62

    }
}