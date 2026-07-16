package com.scalo.stocklify.core.util.constants

object Constants {

    // Network
    const val BASE_URL = "https://api.stocklify.com/"
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L

    // Pagination
    const val PAGE_SIZE = 500
    const val INITIAL_PAGE = 1
}

object ImageUploadConfig {
    // Longest edge, in pixels, that an uploaded product image is downscaled to.
    const val MAX_DIMENSION_PX = 1024
    const val WEBP_QUALITY = 80
}