package com.farhan.storyapp

import com.farhan.storyapp.data.response.AddStoryResponse
import com.farhan.storyapp.data.response.Login
import com.farhan.storyapp.data.response.Register
import com.farhan.storyapp.data.response.Story
import com.farhan.storyapp.data.response.User

object DataDummy {

    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                i.toString(),
                "createdAt",
                "name",
                "description",
                "id",
                i.toDouble(),
                i.toDouble()

            )
            items.add(story)
        }
        return items
    }

    fun generateDummyLogin(): Login {
        return Login(
            error = false,
            message = "success",
            loginResult = User(
                userId = "user-yj5pc_LARC_AgK61",
                name = "Arif Faizin",
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
            )
        )
    }


    fun generateDummyRegister(): Register {
        return Register(
            error = false,
            message = "User Created"
        )
    }
}