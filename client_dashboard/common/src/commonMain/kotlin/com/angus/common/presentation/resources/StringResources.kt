package com.angus.common.presentation.resources

data class StringResources(
    //region Login
    val login: String = "Login",
    val loginTitle: String = "Use Admin account to login",
    val loginUsername: String = "Username",
    val loginPassword: String = "Password",
    val loginButton: String = "Login",
    val loginKeepMeLoggedIn: String = "Keep me logged in",
    //endregion Login

    //region Restaurant
    val searchForRestaurants: String = "Search for restaurants",
    val export: String = "Export",
    val addCuisine: String = "Add category",
    val newRestaurant: String = "New Restaurant",
    val restaurant: String = "restaurant",
    val save: String = "Save",
    val cancel: String = "Cancel",
    val priceLevel: String = "Price level",
    val rating: String = "Rating",
    val filter: String = "Filter",
    val restaurantName: String = "Restaurant name",
    val ownerUsername: String = "Owner username",
    val phoneNumber: String = "Phone number",
    val workingHours: String = "Working hours",
    val location: String = "Location",
    val create: String = "Create",
    val workStartHourHint: String = "1:00",
    val workEndHourHint: String = "24:00",
    val restaurants: String = "Restaurants",
    val cuisines: String = "Cuisines",
    val enterCuisineName: String = "Enter cuisine name",
    val enterOfferName: String = "Enter Offer name",
    val add: String = "Add",
    val update: String = "Update",
    val updateRestaurant: String = "Update Restaurant",
    val offers: String = "Offers",
    val addOffer: String = "Add Offer",
    val noOffers: String = "No offers add yet",
    //endregion Restaurant

    //region Taxi
    val searchForTaxis: String = "Search for Taxis",
    val newTaxi: String = "New Taxi",
    val taxi: String = "taxi",
    val downloadSuccessMessage: String = "Your file download was successful.",
    val seats: String = "Seats",
    val status: String = "Status",
    val carModel: String = "Car Model",
    val carColor: String = "Car Color",
    val driverUsername: String = "Driver Username",
    val taxiPlateNumber: String = "Taxi Plate Number",
    val createNewTaxi: String = "Create new Taxi",
    val taxis: String = "Taxis",
    val offline: String = "Offline",
    val online: String = "Online",
    val onRide: String = "On ride",
    //endregion Taxi

    //region User
    val user: String = "user",
    val permission: String = "Permission",
    val country: String = "Country",
    val searchForUsers: String = "Search for users",
    val edit: String = "Edit",
    val delete: String = "Delete",
    val disable: String = "Disable",
    val permissions: String = "Permissions",
    val users: String = "Users",
    val argentina: String = "Argentina",
    //endregion User

    //region scaffold
    val logout: String = "Logout",
    val darkTheme: String = "Dark theme",
    val dropDownMenu: String = "DropDownMenu",
    //endregion scaffold

    //region table
    val outOf: String = "out of",
    val pluralLetter: String = "s",
    val number: String = "No.",
    val username: String = "Username",
    val email: String = "Email",
    val plateNumber: String = "Plate number",
    val trips: String = "Trips",
    val name: String = "Name",
    val phone: String = "Phone",
    val rate: String = "Rate",
    val price: String = "Price",
        //endregion table

    //region overview
    val overview: String = "Overview",
    val revenueShare: String = "Revenue share",
    val viewMore: String = "View more",
    val taxiLabel: String = "Taxi",
    val restaurantLabel: String = "Restaurant",
    val restaurantPermission: String = "Restaurant Owner",
    val taxiPermission: String = "Taxi Driver",
    val endUserPermission: String = "End User",
    val supportPermission: String = "Support",
    val deliveryPermission: String = "Delivery",
    val adminPermission: String = "Admin",
    val revenue: String = "Revenue",
    val earnings: String = "Earnings",
    val trip: String = "Trip",
    val accepted: String = "Accepted",
    val pending: String = "Pending",
    val rejected: String = "Rejected",
    val canceled: String = "Canceled",
    val completed: String = "Completed",
    val inTheWay: String = "In the way",
    val orders: String = "Orders",
    val monthly: String = "Monthly",
    val weekly: String = "Weekly",
    val daily: String = "Daily",

    // endregion overview

    val clearAll: String = "Clear all",
    val noMatchesFound: String = "Oops, No matches found",
    val invalidPlateNumber: String = "Invalid plate number",
    val invalidCarModel: String = "Invalid car model",

    val noInternet: String = "No internet connection!",
    val unKnownError: String = "Unknown error please retry again!",
)

val English: StringResources = StringResources()

val Spanish: StringResources = StringResources(
    //region Login
    login = "Iniciar sesión",
    loginTitle = "Usar cuenta de Administrador para iniciar sesión",
    loginUsername = "Nombre de usuario",
    loginPassword = "Contraseña",
    loginButton = "Iniciar sesión",
    loginKeepMeLoggedIn = "Mantener sesión iniciada",
    //endregion Login

    //region Restaurant
    searchForRestaurants = "Buscar restaurantes",
    export = "Exportar",
    addCuisine = "Añadir cocina",
    newRestaurant = "Nuevo restaurante",
    restaurant = "restaurante",
    save = "Guardar",
    cancel = "Cancelar",
    priceLevel = "Nivel de precios",
    rating = "Calificación",
    filter = "Filtrar",
    restaurantName = "Nombre del restaurante",
    ownerUsername = "Nombre de usuario del propietario",
    phoneNumber = "Número de teléfono",
    workingHours = "Horario de trabajo",
    location = "Ubicación",
    create = "Crear",
    workStartHourHint = "1:00",
    workEndHourHint = "24:00",
    restaurants = "Restaurantes",
    cuisines = "Cocinas",
    enterCuisineName = "Ingresar nombre de la cocina",
    enterOfferName = "Ingresar nombre de la oferta",
    add = "Añadir",
    update = "Actualizar",
    updateRestaurant = "Actualizar restaurante",
    offers = "Ofertas",
    addOffer = "Añadir oferta",
    noOffers = "No hay ofertas aún",
    //endregion Restaurant

    //region Taxi
    searchForTaxis = "Buscar taxis",
    newTaxi = "Taxi nuevo",
    taxi = "taxi",
    downloadSuccessMessage = "La descarga del archivo fue exitosa.",
    seats = "Asientos",
    status = "Estado",
    carModel = "Modelo del coche",
    carColor = "Color del coche",
    driverUsername = "Nombre de usuario del conductor",
    taxiPlateNumber = "Número de placa del taxi",
    createNewTaxi = "Crear nuevo Taxi",
    taxis = "Taxis",
    offline = "Desconectado",
    online = "En línea",
    onRide = "En viaje",
    //endregion Taxi

    //region User
    user = "usuario",
    permission = "Permiso",
    country = "País",
    searchForUsers = "Buscar usuarios",
    edit = "Editar",
    delete = "Eliminar",
    disable = "Deshabilitar",
    permissions = "Permisos",
    users = "Usuarios",
    argentina = "Argentina",
    //endregion User

    //region scaffold
    logout = "Cerrar sesión",
    darkTheme = "Tema oscuro",
    dropDownMenu = "Menú desplegable",
    //endregion scaffold

    //region table
    outOf = "de",
    pluralLetter = "s",
    number = "Nº",
    username = "Nombre de usuario",
    email = "Correo electrónico",
    plateNumber = "Número de placa",
    trips = "Viajes",
    name = "Nombre",
    phone = "Teléfono",
    rate = "Tarifa",
    price = "Precio",
    //endregion table

    //region overview
    overview = "Resumen",
    revenueShare = "Participación en los ingresos",
    viewMore = "Ver más",
    taxiLabel = "Taxi",
    restaurantLabel = "Restaurante",
    restaurantPermission = "Propietario del restaurante",
    taxiPermission = "Conductor de taxi",
    endUserPermission = "Usuario final",
    supportPermission = "Soporte",
    deliveryPermission = "Entrega",
    adminPermission = "Admin",
    revenue = "Ingresos",
    earnings = "Ganancias",
    trip = "Viaje",
    accepted = "Aceptado",
    pending = "Pendiente",
    rejected = "Rechazado",
    canceled = "Cancelado",
    completed = "Completado",
    inTheWay = "En camino",
    orders = "Pedidos",
    monthly = "Mensual",
    weekly = "Semanal",
    daily = "Diario",
    // endregion overview

    clearAll = "Limpiar todo",
    noMatchesFound = "Vaya, no se encontraron coincidencias",
    invalidPlateNumber = "Número de placa inválido",
    invalidCarModel = "Modelo de coche inválido",

    noInternet = "¡Sin conexión a Internet!",
    unKnownError = "Error desconocido, ¡por favor intente de nuevo!",
)
