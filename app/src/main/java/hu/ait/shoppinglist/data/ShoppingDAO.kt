package hu.ait.shoppinglist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDAO {
    @Query("SELECT * from itemtable")
    fun getAllShoppingList(): Flow<List<ShoppingItem>>

    @Query("SELECT * from itemtable WHERE id = :id")
    fun getItem(id: Int): Flow<ShoppingItem>

    @Query("SELECT COUNT(*) from itemtable")
    suspend fun getAllItemsNum(): Int

//    @Query("""SELECT COUNT(*) from itemtable WHERE todopriority="HIGH"""")
//    suspend fun getImportantTodosNum(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ShoppingItem)

    @Update
    suspend fun update(item: ShoppingItem)

    @Delete
    suspend fun delete(item: ShoppingItem)

    @Query("DELETE from itemtable")
    suspend fun deleteAllItems()
}