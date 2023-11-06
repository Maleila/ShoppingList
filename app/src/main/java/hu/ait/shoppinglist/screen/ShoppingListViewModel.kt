package hu.ait.shoppinglist.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.shoppinglist.data.ShoppingDAO
import hu.ait.shoppinglist.data.ShoppingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val shoppingDAO: ShoppingDAO
) : ViewModel() {

    fun getAllShoppingList(): Flow<List<ShoppingItem>> {
        return shoppingDAO.getAllShoppingList()
    }

    suspend fun getAllItemsNum(): Int {
        return shoppingDAO.getAllItemsNum()
    }

    fun addToList(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            shoppingDAO.insert(shoppingItem)
        }
    }

    fun removeItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            shoppingDAO.delete(shoppingItem)
        }
    }

    fun editItem(editedItem: ShoppingItem) {
        viewModelScope.launch {
            shoppingDAO.update(editedItem)
        }
    }

    fun changeBoughtState(shoppingItem: ShoppingItem, value: Boolean) {
        val newItem = shoppingItem.copy()
        newItem.isPurchased = value
        viewModelScope.launch {
            shoppingDAO.update(newItem)
        }
    }

    fun clearAllItems() {
        viewModelScope.launch {
            shoppingDAO.deleteAllItems()
        }
    }
}