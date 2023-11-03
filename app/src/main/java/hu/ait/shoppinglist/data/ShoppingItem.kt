package hu.ait.shoppinglist.data

import hu.ait.shoppinglist.R


data class ShoppingItem(
    val id: String,
    val title:String,
    val description:String,
    val price:String,
    var isPurchased: Boolean,
    var type:ItemType)

enum class ItemType {
    FOOD, ELECTRONIC, HOUSEHOLD;

    fun getIcon(): Int {
        return if (this == FOOD) R.drawable.apple else if (this == ELECTRONIC) R.drawable.laptop else R.drawable.household

    }
}
