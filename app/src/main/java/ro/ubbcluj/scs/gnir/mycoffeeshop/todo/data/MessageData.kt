package ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data

data class MessageData(var event: String, var payload: ItemJson) {
    data class ItemJson(var plant: Item)
}