package com.example.storyappasandy.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyappasandy.data.api.ApiService
import com.example.storyappasandy.data.api.ListStoryItem


class StoryPagingSource (private val apiService: ApiService, private val token: String): PagingSource<Int, ListStoryItem>(){

    private companion object {
        const val INITIAL_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_INDEX
            val responseData = apiService.getStories(token ,position, params.loadSize)
            val stories = responseData.listStory

            LoadResult.Page(
                data = stories,
                prevKey = if (position == INITIAL_INDEX) null else position -1,
                nextKey = if (stories.isEmpty()) null else position + 1
            )
        }catch (exception: Exception){
            return LoadResult.Error(exception)
        }
    }

}