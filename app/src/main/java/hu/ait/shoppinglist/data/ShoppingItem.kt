package hu.ait.shoppinglist.data

data class ShoppingItem(
    val id: String,
    val title:String,
    val description:String,
    val price:Int,
    var isPurchased: Boolean)
