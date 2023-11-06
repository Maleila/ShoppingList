package hu.ait.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.ait.shoppinglist.R
import java.io.Serializable

@Entity(tableName = "itemtable")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title:String,
    @ColumnInfo(name = "description")val description:String,
    @ColumnInfo(name = "price") val price:String,
    @ColumnInfo(name = "isPurchased") var isPurchased: Boolean,
    @ColumnInfo(name = "type") var type:ItemType): Serializable

enum class ItemType {
    FOOD, ELECTRONIC, HOUSEHOLD;

    fun getIcon(): Int {
        return if (this == FOOD) R.drawable.apple else if (this == ELECTRONIC) R.drawable.laptop else R.drawable.household

    }
}
