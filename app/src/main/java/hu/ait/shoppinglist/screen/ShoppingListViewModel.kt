package hu.ait.shoppinglist.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import hu.ait.shoppinglist.data.ShoppingItem

class ShoppingListViewModel : ViewModel() {

    //underscore indicates private var
    private var _shoppingList =
        mutableStateListOf<ShoppingItem>()

    fun getAllShoppingList(): List<ShoppingItem> {
        return _shoppingList
    }

    fun getAllItemsNum(): Int {
        return _shoppingList.size
    }

    fun addToList(shoppingItem: ShoppingItem) {
        _shoppingList.add(shoppingItem)
    }

    fun removeItem(shoppingItem: ShoppingItem) {
        _shoppingList.remove(shoppingItem)
    }

    fun editItem(originalItem: ShoppingItem, editedItem: ShoppingItem) {
        val index = _shoppingList.indexOf(originalItem)
        _shoppingList[index] = editedItem
    }

    fun changeBoughtState(shoppingItem: ShoppingItem, value: Boolean) {
        val index = _shoppingList.indexOf(shoppingItem)

        val newItem = shoppingItem.copy(
            title = shoppingItem.title,
            description = shoppingItem.description,
            price = shoppingItem.price,
            isPurchased = value
        )

        _shoppingList[index] = newItem
    }

    fun clearAllItems() {
        _shoppingList.clear()
    }
}