
import com.dennis.newsapp.api.NewsApi
import com.dennis.newsapp.api.RetrofitInstance
import com.dennis.newsapp.database.ArticleDatabase
import com.dennis.newsapp.models.NewsResponse
import com.dennis.newsapp.models.SourceResponse
import com.dennis.newsapp.repository.NewsRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NewsRepositoryTest {

    private lateinit var repository: NewsRepository
    private lateinit var mockDatabase: ArticleDatabase
    lateinit var newsApi: NewsApi

    @Before
    fun setup() {
        mockDatabase = Mockito.mock(ArticleDatabase::class.java)
        newsApi = RetrofitInstance.newsApi
        repository = NewsRepository(newsApi,mockDatabase)
    }

    @Test
    fun testGetHeadlines() = runBlocking {
        val countryCode = "us"
        val result: Response<NewsResponse> = repository.getHeadlines(countryCode)

        assertNotNull(result)
        assertEquals("ok", result.body()!!.status)
        assertNotNull(result.body()!!.articles)
    }

    @Test
    fun testGetHeadlinesFromSources() = runBlocking {
        val sources = "cnn"
        val result: Response<NewsResponse> = repository.getHeadlinesFromSources(sources)

        assertNotNull(result)
        assertEquals("ok", result.body()!!.status)
        assertEquals("cnn", result.body()!!.articles.first().source.id)
    }

    @Test
    fun testGetNewsSources() = runBlocking {
        val language = "en"
        val result: Response<SourceResponse> = repository.getNewsSources(language)

        assertNotNull(result)
        assertEquals("ok", result.body()!!.status)
        assertNotNull(result.body()!!.sources)
    }
}
