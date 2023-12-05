package com.example.storyappasandy

import com.example.storyappasandy.data.api.ListStoryItem

object DataDummy {

    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                i.toString(),
                "createdAt + $i",
                "name $i",
            )
            items.add(quote)
        }
        return items
    }
}