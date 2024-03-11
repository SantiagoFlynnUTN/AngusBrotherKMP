package com.angus.service_restaurant.data.gateway

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Updates
import org.bson.Document
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.aggregate
import com.angus.service_restaurant.data.DataBaseContainer
import com.angus.service_restaurant.data.collection.CartCollection
import com.angus.service_restaurant.data.collection.MealCollection
import com.angus.service_restaurant.data.collection.OrderCollection
import com.angus.service_restaurant.data.collection.mapper.*
import com.angus.service_restaurant.data.collection.relationModels.OrderWithRestaurant
import com.angus.service_restaurant.domain.entity.Cart
import com.angus.service_restaurant.domain.entity.Order
import com.angus.service_restaurant.domain.gateway.IRestaurantManagementGateway
import com.angus.service_restaurant.domain.utils.exceptions.MultiErrorException
import com.angus.service_restaurant.domain.utils.exceptions.NOT_FOUND

class RestaurantManagementGateway(private val container: DataBaseContainer) : IRestaurantManagementGateway {

    //region Order
    override suspend fun addOrder(order: Order): Order? {
        val insertedOrder = order.toCollection()
        container.orderCollection.insertOne(insertedOrder).wasAcknowledged()

        val pipeline = listOf(
            match(OrderCollection::id eq insertedOrder.id),
            lookup(
                from = "restaurant",
                localField = OrderCollection::restaurantId.name,
                foreignField = "_id",
                newAs = "restaurant"
            ),
            unwind("\$restaurant"),
            project(
                OrderWithRestaurant::id from "\$_id",
                OrderWithRestaurant::userId from "\$userId",
                OrderWithRestaurant::restaurant from "\$restaurant",
                OrderWithRestaurant::meals from "\$meals",
                OrderWithRestaurant::totalPrice from "\$totalPrice",
                OrderWithRestaurant::createdAt from "\$createdAt",
                OrderWithRestaurant::orderStatus from "\$orderStatus"
            )
        )

        val createdOrder = container.orderCollection.aggregate<OrderWithRestaurant>(pipeline).first()
        return createdOrder?.toOrderEntity()

    }

    override suspend fun getOrdersByRestaurantId(restaurantId: String): List<Order> {
        return container.orderCollection.find(
            OrderCollection::restaurantId eq ObjectId(restaurantId)
        ).toList().toEntity()
    }

    override suspend fun getActiveOrdersByRestaurantId(restaurantId: String): List<Order> {
        return container.orderCollection.find(
            and(
                OrderCollection::restaurantId eq ObjectId(restaurantId),
                OrderCollection::orderStatus nin listOf(Order.Status.DONE.statusCode, Order.Status.CANCELED.statusCode)
            )
        ).toList().toEntity()
    }

    override suspend fun getActiveOrdersForUser(userId: String): List<Order> {
        val pipeline = listOf(
            match(
                and(
                    OrderCollection::userId eq ObjectId(userId),
                    OrderCollection::orderStatus `in` listOf(
                        Order.Status.PENDING.statusCode,
                        Order.Status.APPROVED.statusCode,
                        Order.Status.IN_COOKING.statusCode
                    )
                )
            ),
            lookup(
                from = "restaurant",
                localField = OrderCollection::restaurantId.name,
                foreignField = "_id",
                newAs = "restaurant"
            ),
            unwind("\$restaurant"),
            project(
                OrderWithRestaurant::id from "\$_id",
                OrderWithRestaurant::userId from "\$userId",
                OrderWithRestaurant::restaurant from "\$restaurant",
                OrderWithRestaurant::meals from "\$meals",
                OrderWithRestaurant::totalPrice from "\$totalPrice",
                OrderWithRestaurant::createdAt from "\$createdAt",
                OrderWithRestaurant::orderStatus from "\$orderStatus",
                computed(
                    "restaurantName",
                    "\$restaurant.name"
                ),
                computed(
                    "restaurantImage",
                    "\$restaurant.restaurantImage"
                ),
                computed(
                    "currency",
                    "\$restaurant.currency"
                )
            )
        )

        return container.orderCollection.aggregate<OrderWithRestaurant>(pipeline).toList().map { it.toOrderEntity() }
    }

    override suspend fun getOrderById(orderId: String): Order? =
        container.orderCollection.findOneById(ObjectId(orderId))?.toEntity()

    override suspend fun getOrderStatus(orderId: String): Int {
        return container.orderCollection.findOne(OrderCollection::id eq ObjectId(orderId))?.orderStatus
            ?: throw MultiErrorException(listOf(NOT_FOUND))
    }

    override suspend fun isOrderExisted(orderId: String): Boolean {
        val order = container.orderCollection.findOne(
            and(
                OrderCollection::id eq ObjectId(orderId),
                OrderCollection::orderStatus eq Order.Status.CANCELED.statusCode
            )
        )
        return order != null
    }

    override suspend fun updateOrderStatus(orderId: String, status: Int): Order? {
        val updateOperation = setValue(OrderCollection::orderStatus, status)

        container.orderCollection.updateOne(
            filter = OrderCollection::id eq ObjectId(orderId),
            update = updateOperation
        )

        val pipeline = listOf(
            match(OrderCollection::id eq ObjectId(orderId)),
            lookup(
                from = "restaurant",
                localField = OrderCollection::restaurantId.name,
                foreignField = "_id",
                newAs = "restaurant"
            ),
            unwind("\$restaurant"),
            project(
                OrderWithRestaurant::id from "\$_id",
                OrderWithRestaurant::userId from "\$userId",
                OrderWithRestaurant::restaurant from "\$restaurant",
                OrderWithRestaurant::meals from "\$meals",
                OrderWithRestaurant::totalPrice from "\$totalPrice",
                OrderWithRestaurant::createdAt from "\$createdAt",
                OrderWithRestaurant::orderStatus from "\$orderStatus",
                computed(
                    "restaurantName",
                    "\$restaurant.name"
                ),
                computed(
                    "restaurantImage",
                    "\$restaurant.restaurantImage"
                ),
                computed(
                    "currency",
                    "\$restaurant.currency"
                )
            )
        )

        val updatedOrderWithRestaurant = container.orderCollection.aggregate<OrderWithRestaurant>(pipeline).first()
        return updatedOrderWithRestaurant?.toOrderEntity()
    }

    override suspend fun cancelOrder(orderId: String): Order? {
        val updateOperation = setValue(OrderCollection::orderStatus, Order.Status.CANCELED.statusCode)

        container.orderCollection.updateOne(
            filter = OrderCollection::id eq ObjectId(orderId),
            update = updateOperation
        )

        val pipeline = listOf(
            match(OrderCollection::id eq ObjectId(orderId)),
            lookup(
                from = "restaurant",
                localField = OrderCollection::restaurantId.name,
                foreignField = "_id",
                newAs = "restaurant"
            ),
            unwind("\$restaurant"),
            project(
                OrderWithRestaurant::id from "\$_id",
                OrderWithRestaurant::userId from "\$userId",
                OrderWithRestaurant::restaurant from "\$restaurant",
                OrderWithRestaurant::meals from "\$meals",
                OrderWithRestaurant::totalPrice from "\$totalPrice",
                OrderWithRestaurant::createdAt from "\$createdAt",
                OrderWithRestaurant::orderStatus from "\$orderStatus",
                computed(
                    "restaurantName",
                    "\$restaurant.name"
                ),
                computed(
                    "restaurantImage",
                    "\$restaurant.restaurantImage"
                ),
                computed(
                    "currency",
                    "\$restaurant.currency"
                )
            )
        )

        val updatedOrderWithRestaurant = container.orderCollection.aggregate<OrderWithRestaurant>(pipeline).first()
        return updatedOrderWithRestaurant?.toOrderEntity()
    }


    override suspend fun getNumberOfOrdersHistoryInRestaurant(restaurantId: String): Long {
        return container.orderCollection.countDocuments(
            and(
                OrderCollection::restaurantId eq ObjectId(restaurantId),
                OrderCollection::orderStatus `in` listOf(
                    Order.Status.DONE.statusCode,
                    Order.Status.CANCELED.statusCode
                )
            )
        )
    }

    override suspend fun getNumberOfOrdersHistoryForUser(userId: String): Long {
        return container.orderCollection.countDocuments(
            and(
                OrderCollection::userId eq ObjectId(userId),
                OrderCollection::orderStatus eq Order.Status.DONE.statusCode,
            )
        )
    }

    override suspend fun getOrdersHistoryForRestaurant(
        restaurantId: String, page: Int, limit: Int
    ): List<Order> {
        return container.orderCollection.aggregate<OrderWithRestaurant>(
            match(
                and(
                    OrderCollection::restaurantId eq ObjectId(restaurantId),
                    OrderCollection::orderStatus `in` listOf(
                        Order.Status.DONE.statusCode,
                        Order.Status.CANCELED.statusCode
                    )
                )
            ),
            lookup(
                from = DataBaseContainer.RESTAURANT_COLLECTION,
                localField = OrderCollection::restaurantId.name,
                foreignField = "_id",
                newAs = "restaurant"
            ),
            unwind("\$restaurant"),
            project(
                OrderWithRestaurant::id from "\$_id",
                OrderWithRestaurant::userId from "\$userId",
                OrderWithRestaurant::restaurant from "\$restaurant",
                OrderWithRestaurant::meals from "\$meals",
                OrderWithRestaurant::totalPrice from "\$totalPrice",
                OrderWithRestaurant::createdAt from "\$createdAt",
                OrderWithRestaurant::orderStatus from "\$orderStatus"
            ),
            sort(descending(OrderWithRestaurant::createdAt)),
            skip((page - 1) * limit),
            limit(limit)
        ).toList().toHistoryEntity()
    }

    override suspend fun getOrdersHistoryForUser(userId: String, page: Int, limit: Int): List<Order> {
        return container.orderCollection.aggregate<OrderWithRestaurant>(
            match(
                OrderCollection::userId eq ObjectId(userId),
                OrderCollection::orderStatus eq Order.Status.DONE.statusCode
            ),
            lookup(
                from = DataBaseContainer.RESTAURANT_COLLECTION,
                localField = OrderCollection::restaurantId.name,
                foreignField = "_id",
                newAs = "restaurant"
            ),
            unwind("\$restaurant"),

            project(
                OrderWithRestaurant::id from "\$_id",
                OrderWithRestaurant::userId from "\$userId",
                OrderWithRestaurant::restaurant from "\$restaurant",
                OrderWithRestaurant::meals from "\$meals",
                OrderWithRestaurant::totalPrice from "\$totalPrice",
                OrderWithRestaurant::createdAt from "\$createdAt",
                OrderWithRestaurant::orderStatus from "\$orderStatus"
            ),
            sort(descending(OrderWithRestaurant::createdAt)),
            skip((page - 1) * limit),
            limit(limit)
        ).toList().toHistoryEntity()
    }

    //endregion

    //region Cart
    override suspend fun getCart(userId: String): Cart {
        var cart = container.cartCollection.findOne(CartCollection::userId eq ObjectId(userId))
        return if (cart == null) {
            cart = CartCollection(userId = ObjectId(userId))
            container.cartCollection.insertOne(cart)
            cart.toCartDetails().toEntity()
        } else {
            val restaurant = cart.restaurantId?.let { container.restaurantCollection.findOneById(it) }
            cart.toCartDetails(restaurant).toEntity()
        }
    }

    override suspend fun updateCart(cart: Cart): Cart {
        val restaurant = cart.restaurantId?.let { container.restaurantCollection.findOneById(it) }
        val mealIds = cart.meals?.map { ObjectId(it.meadId) } ?: emptyList()
        val meals = container.mealCollection.find(filter = MealCollection::id `in` mealIds).toList()
        val updatedCart = container.cartCollection.findOneAndUpdate(
            CartCollection::id eq ObjectId(cart.id),
            update = Updates.combine(
                Updates.set("restaurantId", ObjectId(cart.restaurantId)),
                Updates.set("meals", meals)
            ),
            options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        ) ?: throw MultiErrorException(listOf(NOT_FOUND))
        return updatedCart.toCartDetails(restaurant).toEntity()
    }

    override suspend fun updateCart(cartId: String, restaurantId: String, mealId: String, quantity: Int): Cart {
        val meal =
            container.mealCollection.findOneById(ObjectId(mealId)) ?: throw MultiErrorException(listOf(NOT_FOUND))

        val restaurant = container.restaurantCollection.findOneById(ObjectId(restaurantId))

        container.cartCollection.findOneAndUpdate(
            and(
                CartCollection::id eq ObjectId(cartId),
                CartCollection::meals.elemMatch(CartCollection.MealCollection::mealId eq meal.id)
            ),
            Updates.pull("meals", Document("mealId", ObjectId(mealId)))
        )

        return if (quantity == 0) {
            val cart = deleteFromCart(cartId, meal, restaurantId)
            if (cart.restaurantId == null) {
                cart.toCartDetails().toEntity()
            } else {
                cart.toCartDetails(restaurant).toEntity()
            }
        } else {
            addToCart(cartId, meal, restaurantId, quantity).toCartDetails(restaurant).toEntity()
        }

    }

    private suspend fun addToCart(
        cartId: String, meal: MealCollection, restaurantId: String, quantity: Int
    ): CartCollection {
        return container.cartCollection.findOneAndUpdate(
            CartCollection::id eq ObjectId(cartId),
            update = Updates.combine(
                Updates.set("restaurantId", ObjectId(restaurantId)),
                Updates.addToSet("meals", meal.toMealInCart(quantity))
            ),
            options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        ) ?: throw MultiErrorException(listOf(NOT_FOUND))
    }

    private suspend fun deleteFromCart(
        cartId: String, meal: MealCollection, restaurantId: String
    ): CartCollection {

        var updatedCart = container.cartCollection.findOneAndUpdate(
            CartCollection::id eq ObjectId(cartId),
            update = Updates.combine(
                Updates.set("restaurantId", ObjectId(restaurantId)),
                Updates.pull("meals", Filters.eq("mealId", meal.id))
            ),
            options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        )

        if (updatedCart != null && updatedCart.meals.isEmpty()) {
            updatedCart = container.cartCollection.findOneAndUpdate(
                CartCollection::id eq ObjectId(cartId),
                Updates.set("restaurantId", null),
                options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
            )
        }
        return updatedCart ?: throw MultiErrorException(listOf(NOT_FOUND))
    }

    override suspend fun deleteCart(userId: String) {
        container.cartCollection.updateMany(
            filter = CartCollection::userId eq ObjectId(userId),
            update = Updates.combine(
                Updates.unset("restaurantId"),
                Updates.unset("meals")
            )
        )
    }

    override suspend fun isCartEmpty(userId: String): Boolean {
        val cart = container.cartCollection.findOne(filter = CartCollection::userId eq ObjectId(userId))
        return cart?.restaurantId == null
    }

    //endregion
}