package hu.ait.shoppinglist.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import hu.ait.shoppinglist.data.ShoppingItem
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.data.ItemType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    modifier: Modifier = Modifier,
    shoppingViewModel: ShoppingListViewModel = hiltViewModel()
) {
    var showAddItemDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val itemList by shoppingViewModel.getAllShoppingList().collectAsState(emptyList())

    var itemToEdit: ShoppingItem? by rememberSaveable {
        mutableStateOf(null)
    }

    Column {
        TopAppBar(
            title = {
                Text(stringResource(R.string.top_app_bar_text))
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            actions = {
                IconButton(onClick = {
                    shoppingViewModel.clearAllItems()
                }) {
                    Icon(Icons.Filled.Delete, null)
                }
                IconButton(onClick = {
                    itemToEdit = null
                    showAddItemDialog = true
                }) {
                    Icon(Icons.Filled.AddCircle, null)
                }
            })

        Column(modifier = modifier.padding(10.dp)) {

            if (showAddItemDialog) {
                AddNewTodoForm(
                    shoppingViewModel,
                    { showAddItemDialog = false },
                    itemToEdit
                )
            }

            if (itemList.isEmpty())
                Text(text = stringResource(R.string.no_items_text))
            else {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    items(sortByPrice(itemList)) {
                        ItemCard(shoppingItem = it,
                            onRemoveItem = { shoppingViewModel.removeItem(it) },
                            onBoughtCheckChange = { checkState ->
                                shoppingViewModel.changeBoughtState(it, checkState)
                            },
                            onEditItem = { editedItem ->
                                itemToEdit = editedItem
                                showAddItemDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
}

fun sortByPrice(shoppingList: List<ShoppingItem>): List<ShoppingItem> {
    return shoppingList.sortedWith { a, b ->
        when {
            a.price.toInt() > b.price.toInt() -> 1
            a.price.toInt() < b.price.toInt() -> -1
            else -> 0
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddNewTodoForm(
    shoppingViewModel: ShoppingListViewModel,
    onDialogDismiss: () -> Unit = {},
    itemToEdit: ShoppingItem? = null
) {
    var context = LocalContext.current

    var nameErrorState by rememberSaveable {
        mutableStateOf(false)
    }
    var descriptionErrorState by rememberSaveable {
        mutableStateOf(false)
    }
    var priceErrorState by rememberSaveable {
        mutableStateOf(false)
    }
    var errorText by rememberSaveable {
        mutableStateOf("")
    }
    var nameErrorText by rememberSaveable {
        mutableStateOf("")
    }
    var descriptionErrorText by rememberSaveable {
        mutableStateOf("")
    }

    fun validatePrice(text: String) {
        val allDigits = text.all { char -> char.isDigit() }
        errorText = context.getString(R.string.price_error_text)
        priceErrorState = !allDigits || text.length > 9 || text.trim() == ""
    }

    fun validateTitle(text: String) {
        if (text.trim() == "" || text.length > 30) {
            nameErrorState = true
            nameErrorText = context.getString(R.string.name_error_text)
        } else {
            nameErrorState = false
        }
    }

    fun validateDescription(text: String) {
        if (text.trim() == "" || text.length > 30) {
            descriptionErrorState = true
            descriptionErrorText = context.getString(R.string.description_error_text)
        } else {
            descriptionErrorState = false
        }
    }

    Dialog(
        onDismissRequest = onDialogDismiss
    ) {
        var itemTitle by rememberSaveable {
            mutableStateOf(itemToEdit?.title ?: "")
        }
        var itemType by rememberSaveable {
            mutableStateOf(itemToEdit?.type ?: ItemType.FOOD)
        }
        var itemBought by rememberSaveable {
            mutableStateOf(itemToEdit?.isPurchased ?: false)
        }
        var itemPrice by rememberSaveable {
            mutableStateOf(itemToEdit?.price ?: "")
        }
        var itemDescription by rememberSaveable {
            mutableStateOf(itemToEdit?.description ?: "")
        }

        Column(
            Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(10.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = itemTitle,
                singleLine = true,
                trailingIcon = {
                    if (nameErrorState) {
                        Icon(
                            Icons.Filled.Warning, stringResource(R.string.content_description_error),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onValueChange = {
                    itemTitle = it
                    validateTitle(itemTitle)
                },
                label = { Text(text = stringResource(R.string.item_name_text)) }
            )
            if (nameErrorState) {
                Text(
                    text = nameErrorText,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = itemPrice,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                trailingIcon = {
                    if (priceErrorState) {
                        Icon(
                            Icons.Filled.Warning, stringResource(R.string.content_description_error),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onValueChange = {
                    itemPrice = it
                    validatePrice(itemPrice)
                },
                label = { Text(text = stringResource(R.string.price_text)) }
            )
            if (priceErrorState) {
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = itemDescription,
                singleLine = true,
                trailingIcon = {
                    if (descriptionErrorState) {
                        Icon(
                            Icons.Filled.Warning, stringResource(R.string.content_description_error),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onValueChange = {
                    itemDescription = it
                    validateDescription(itemDescription)
                },
                label = { Text(text = stringResource(R.string.description_text)) }
            )
            if (descriptionErrorState) {
                Text(
                    text = descriptionErrorText,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            SpinnerSample(
                listOf(stringResource(R.string.food_dropdown_text),
                    stringResource(R.string.electronic_dropdown_text),
                    stringResource(R.string.household_dropdown_text)
                ),
                preselected = stringResource(R.string.food_dropdown_text),
                onSelectionChanged = {
                    if(it == context.getString(R.string.food_dropdown_text)) {
                        itemType = ItemType.FOOD
                    } else if(it == context.getString(R.string.electronic_dropdown_text)) {
                        itemType = ItemType.ELECTRONIC
                    } else {
                        itemType = ItemType.HOUSEHOLD
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = itemBought, onCheckedChange = {
                    itemBought = it
                })
                Text(text = stringResource(R.string.bought_checkbox_text))
            }

            Row {
                Button(onClick = {
                    validateDescription(itemDescription)
                    validatePrice(itemPrice)
                    validateTitle(itemTitle)
                    if(!nameErrorState && !priceErrorState && !descriptionErrorState) {
                        if (itemToEdit == null) {
                            shoppingViewModel.addToList(
                                ShoppingItem(
                                    0,
                                    itemTitle,
                                    itemDescription,
                                    itemPrice,
                                    itemBought,
                                    itemType
                                )
                            )
                        } else {
                            var itemEdited = itemToEdit.copy(
                                title = itemTitle,
                                description = itemDescription,
                                price = itemPrice,
                                isPurchased = itemBought,
                                type = itemType
                            )
                            shoppingViewModel.editItem(itemEdited)
                        }
                        onDialogDismiss()
                        itemTitle = "" //reset fields to empty
                    }
                }) {
                    Text(text = stringResource(R.string.save_button_text))
                }

            }
        }
    }
}

@Composable
fun ItemCard(
    shoppingItem: ShoppingItem,
    onBoughtCheckChange: (Boolean) -> Unit = {},
    onRemoveItem: () -> Unit = {},
    onEditItem: (ShoppingItem) -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    Modifier.fillMaxWidth(0.6f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = shoppingItem.type.getIcon()),
                        contentDescription = stringResource(R.string.content_description_type),
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 10.dp)
                    )
                    Column {
                        Text(shoppingItem.title)
                        Text(
                            text = shoppingItem.description,
                            style = TextStyle(
                                fontSize = 12.sp,
                            )
                        )
                        Text(
                            text = stringResource(R.string.dollar_sign_text) + shoppingItem.price,
                            style = TextStyle(
                                fontSize = 12.sp,
                            )
                        )
                    }
                    Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                }
                Checkbox(
                    checked = shoppingItem.isPurchased,
                    onCheckedChange = { onBoughtCheckChange(it) }
                )
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.content_description_delete),
                    modifier = Modifier.clickable {
                        onRemoveItem()
                    },
                    tint = Color.Red
                )
                Icon(
                    imageVector = Icons.Filled.Build,
                    contentDescription = stringResource(R.string.content_description_edit),
                    modifier = Modifier.clickable {
                        onEditItem(shoppingItem)
                    },
                    tint = Color.Blue
                )
            }
        }
    }
}

@Composable
fun SpinnerSample(
    list: List<String>,
    preselected: String,
    onSelectionChanged: (myData: String) -> Unit, modifier: Modifier = Modifier
){
    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) } // initial value
    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        } ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = selected,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp))
            Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth()
            ) {
                list.forEach { listEntry ->
                    DropdownMenuItem(
                        onClick = {
                            selected = listEntry
                            expanded = false
                            onSelectionChanged (selected)
                        },
                        text = {
                            Text(
                                text = listEntry,
                                modifier = Modifier
                            )
                        },
                    )
                }
            }
        }
    }
}
