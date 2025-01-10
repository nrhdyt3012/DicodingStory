package com.dicoding.picodiploma.loginwithanimation.view.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.picodiploma.loginwithanimation.DataDummy
import com.dicoding.picodiploma.loginwithanimation.MainDispatcherRule
import com.dicoding.picodiploma.loginwithanimation.adapter.StoryAdapter
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.mockStatic
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    private val testDispatcher = TestCoroutineDispatcher()
    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var mockedLog: MockedStatic<Log>

    @SuppressLint("CheckResult")
    @Before
    fun setUp() {
        // Open the static mock here
        mockedLog = mockStatic(android.util.Log::class.java)
        mockedLog.`when`<Boolean> { Log.isLoggable(anyString(), anyInt()) }.thenReturn(true)
    }

    @After
    fun tearDown() {
        // Close the static mock to deregister it
        mockedLog.close()
    }


    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest{
            val dummyStory = DataDummy.generateDummyStories()
            val data: PagingData<ListStoryItem> = StoryPagingSources.snapshot(dummyStory)
            val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
            expectedStory.value = data
            val dummyUserModel = UserModel(
            email = "charlievalentino17@gmail.com",
            token = dummyToken,
            isLogin = true
            )
        val dummyFlow = flowOf(dummyUserModel)
        Mockito.`when`(userRepository.getSession()).thenReturn(dummyFlow)
        Mockito.`when`(userRepository.getPagingStories(dummyToken)).thenReturn(expectedStory)
        val mainViewModel = MainViewModel(userRepository)
        mainViewModel.stories
        val actualQuote: PagingData<ListStoryItem> = mainViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
                diffCallback = StoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main,
            )
            differ.submitData(actualQuote)

            assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0], differ.snapshot()[0])

    }
    companion object {
        private const val dummyToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWRlX29wd1dmN0s4QVhNSTkiLCJpYXQiOjE2NjcwNDYxMzN9._TmFRjqq3BL8R--c1mIDTB2UwaJfFDCv_kt8lxNfnMU"
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data
        val dummyUserModel = UserModel(email ="charlievalentino17@gmail.com" , token = dummyToken, isLogin = true)
        val dummyFlow = flowOf(dummyUserModel)

        Mockito.`when`(userRepository.getSession()).thenReturn(dummyFlow)
        Mockito.`when`(userRepository.getPagingStories(dummyToken)).thenReturn(expectedStory)
        val mainViewModel = MainViewModel(userRepository)
        mainViewModel.stories
        val actualStory: PagingData<ListStoryItem> = mainViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)
        assertEquals(0, differ.snapshot().size)
    }

    class StoryPagingSources : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
        companion object {
            fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int? {
            return null
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>>{
            return LoadResult.Page(emptyList(), null, null)
        }
    }

     val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }



}

