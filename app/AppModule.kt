//
//import com.apollographql.apollo3.ApolloClient
//import com.apollographql.apollo3.network.okHttpClient
//import com.example.graphql.repositories.data.ApolloRepositoriesClient
//import com.example.graphql.repositories.domain.GetRepositoriesUseCase
//import com.example.graphql.repositories.domain.RepositoriesClient
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import okhttp3.OkHttpClient
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//    @Provides
//    @Singleton
//    fun provideApolloClient(): ApolloClient {
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor { chain ->
//                val original = chain.request()
//                val builder = original.newBuilder().method(original.method, original.body)
//                builder.header("Authorization", "Bearer <API_TOKEN_GOES_HERE>")
//                chain.proceed(builder.build())
//            }
//            .build()
//        return ApolloClient.Builder()
//            .serverUrl("https://api.github.com/graphql")
//            .okHttpClient(okHttpClient)
//            .build()
//    }
//    @Provides
//    @Singleton
//    fun provideRepositoriesClient(apolloClient: ApolloClient): RepositoriesClient {
//        return ApolloRepositoriesClient(apolloClient)
//    }
//    @Provides
//    @Singleton
//    fun provideGetRepositoriesUseCase(countryClient: RepositoriesClient): GetRepositoriesUseCase {
//        return GetRepositoriesUseCase(countryClient)
//    }
//}