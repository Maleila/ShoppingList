package hu.ait.shoppinglist.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.lifecycle.viewmodel.compose.viewModel
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
import java.util.UUID
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.KeyboardType
import hu.ait.shoppinglist.data.ItemType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    modifier: Modifier = Modifier,
    shoppingViewModel: ShoppingListViewModel = viewModel()
) {
    var showAddItemDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Column {
        TopAppBar(
            title = {
                Text("Shopping List")
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
//                IconButton(onClick = {
//                    onNavigateToSummary(
//                        todoViewModel.getAllTodoNum(),
//                        todoViewModel.getImportantTodoNum()
//                    )
//                }) {
//                    Icon(Icons.Filled.Info, null)
//                }
                IconButton(onClick = {
                    showAddItemDialog = true
                }) {
                    Icon(Icons.Filled.AddCircle, null)
                }
            })

        Column(modifier = modifier.padding(10.dp)) {

            if (showAddItemDialog) {
                AddNewTodoForm(shoppingViewModel,
                    { showAddItemDialog = false }
                )
            }
            if (shoppingViewModel.getAllShoppingList().isEmpty())
                Text(text = "No items")
            else {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    items(shoppingViewModel.getAllShoppingList()) {
                        ItemCard(shoppingItem = it,
                            onRemoveItem = { shoppingViewModel.removeItem(it) },
                            onBoughtCheckChange = { checkState ->
                                shoppingViewModel.changeBoughtState(it, checkState)
                            },
                            onEditItem = { editedItem ->
                                shoppingViewModel.editItem(it, editedItem)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddNewTodoForm(
    shoppingViewModel: ShoppingListViewModel,
    onDialogDismiss: () -> Unit = {}
) {
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
        errorText = "This field can be number only"
        if(!allDigits || text.trim() =="") {
            priceErrorState = true
        } else {
            priceErrorState = false
        }
    }

    fun validateTitle(text: String) {
        if (text.trim() == "") {
            nameErrorState = true
            nameErrorText = "Please enter an item name"
        } else {
            nameErrorState = false
        }
    }

    fun validateDescription(text: String) {
        if (text.trim() == "") {
            descriptionErrorState = true
            descriptionErrorText = "Please enter an item name"
        } else {
            descriptionErrorState = false
        }
    }

    Dialog(
        onDismissRequest = onDialogDismiss
    ) {
        var itemTitle by rememberSaveable {
            mutableStateOf("")
        }
        var itemType by rememberSaveable {
            mutableStateOf(ItemType.FOOD)
        }
        var itemBought by rememberSaveable {
            mutableStateOf(false)
        }
        var itemPrice by rememberSaveable {
            mutableStateOf("")
        }
        var itemDescription by rememberSaveable {
            mutableStateOf("")
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
                trailingIcon = {
                    if (nameErrorState) {
                        Icon(
                            Icons.Filled.Warning, "error",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onValueChange = {
                    itemTitle = it
                    validateTitle(itemTitle)
                },
                label = { Text(text = "Item name") }
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
                trailingIcon = {
                    if (priceErrorState) {
                        Icon(
                            Icons.Filled.Warning, "error",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onValueChange = {
                    itemPrice = it
                    validatePrice(itemPrice)
                },
                label = { Text(text = "Price") }
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
                trailingIcon = {
                    if (descriptionErrorState) {
                        Icon(
                            Icons.Filled.Warning, "error",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onValueChange = {
                    itemDescription = it
                    validateDescription(itemDescription)
                },
                label = { Text(text = "Description") }
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
                listOf("Food", "Electronic", "Household"),
                preselected = "Food",
                onSelectionChanged = {
                    if(it == "Food") {
                        itemType = ItemType.FOOD
                    } else if(it == "Electronic") {
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
                Text(text = "Bought")
            }

            Row {
                Button(onClick = {
                    validateDescription(itemDescription)
                    validatePrice(itemPrice)
                    validateTitle(itemTitle)
                    if(!nameErrorState && !priceErrorState && !descriptionErrorState) {
                        shoppingViewModel.addToList(
                            ShoppingItem(
                                UUID.randomUUID().toString(),
                                itemTitle,
                                itemDescription,
                                itemPrice,
                                if (itemBought) true else false,
                                itemType
                            )
                        )
                        onDialogDismiss()
                        itemTitle = "" //reset fields to empty
                    }
                }) {
                    Text(text = "Save")
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
        modifier = Modifier.padding(5.dp)
    ) {
        var expanded by rememberSaveable {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = shoppingItem.type.getIcon()),
                    contentDescription = "Type",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp)
                )
                Column {
                    Text(shoppingItem.title, modifier = Modifier.fillMaxWidth(0.2f))
                    Text(
                        text = shoppingItem.description,
                        style = TextStyle(
                            fontSize = 12.sp,
                        )
                    )
                    Text(
                        text = "$" + shoppingItem.price.toString(),
                        style = TextStyle(
                            fontSize = 12.sp,
                        )
                    )

                }
                Spacer(modifier = Modifier.fillMaxSize(0.35f))
                Checkbox(
                    checked = shoppingItem.isPurchased,
                    onCheckedChange = { onBoughtCheckChange(it) }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.clickable {
                        onRemoveItem()
                    },
                    tint = Color.Red
                )
                Icon(
                    imageVector = Icons.Filled.Build,
                    contentDescription = "Edit",
                    modifier = Modifier.clickable {
                        onEditItem(shoppingItem)
                    },
                    tint = Color.Blue
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded)
                            Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) {
                            "Less"
                        } else {
                            "More"
                        }
                    )
                }
            }

            if (expanded) {
                Text(
                    text = "expanded. not sure what goes here tho",
                    style = TextStyle(
                        fontSize = 12.sp,
                    )
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
