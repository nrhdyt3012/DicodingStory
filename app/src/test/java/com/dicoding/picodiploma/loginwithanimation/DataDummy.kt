package com.dicoding.picodiploma.loginwithanimation

import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem

object DataDummy {
    fun generateDummyStories(): List<ListStoryItem> {
        val listStory = ArrayList<ListStoryItem>()
        for (i in 1..20) {
            val story = ListStoryItem(
                createdAt = "2024-12-24T24:24:24Z",
                description = "Deskripsi $i",
                id = "id_$i",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10,
                name = "Nama $i",
                photoUrl = "https://i.ytimg.com/vi/BdMP7OZG1DE/maxresdefault.jpg"
            )
            listStory.add(story)
        }
        return listStory
    }
}