package com.kshitijchauhan.haroldadmin.moviedb.core

import org.junit.Assert.*
import org.junit.Test

class ResourceTest {

    @Test
    fun `check whether data of resource is the data that was actually given to it`() {
        val successResource = Resource.Success<Unit>(Unit)
        assertEquals(successResource.data, Unit)
    }

    @Test
    fun `check whether error message of resource is the one that was given to it`() {
        val errorMessage = "Error"
        val errorResource = Resource.Error<Unit>(errorMessage)
        assertEquals(errorResource.errorMessage, errorMessage)
    }
}