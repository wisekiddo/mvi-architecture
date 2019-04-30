package com.wisekiddo.stored

import com.wisekiddo.models.RepositoryModel
import com.wisekiddo.repository.DataStored
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import com.wisekiddo.stored.db.ProjectDatabase
import com.wisekiddo.application.mapper.StoredEntityMapper
import javax.inject.Inject

/**
 * Cached implementation for retrieving and saving DomainModel instances. This class implements the
 * [DataStored] from the DomainModel layer as it is that layers responsibility for defining the
 * operations in which data store implementation layers can carry out.
 */
class StoredImpl @Inject constructor(val projectDatabase: ProjectDatabase,
                                     private val storedEntityMapper: StoredEntityMapper,
                                     private val preferencesHelper: PreferencesHelper) :
    DataStored {

    private val EXPIRATION_TIME = (60 * 10 * 1000).toLong()

    /**
     * Retrieve an instance from the projectDatabase, used for tests.
     */
    internal fun getDatabase(): ProjectDatabase {
        return projectDatabase
    }

    /**
     * Remove all the data from all the tables in the projectDatabase.
     */
    override fun clearDataList(): Completable {
        return Completable.defer {
            projectDatabase.cachedDao().clearData()
            Completable.complete()
        }
    }

    /**
     * Save the given list of [RepositoryModel] instances to the projectDatabase.
     */
    override fun saveDataList(repositoryList: List<RepositoryModel>): Completable {
        return Completable.defer {
            repositoryList.forEach {
                projectDatabase.cachedDao().insertData(
                    storedEntityMapper.mapToCached(it))
            }
            Completable.complete()
        }
    }

    /**
     * Retrieve a list of [RepositoryModel] instances from the projectDatabase.
     */
    override fun getDataList(): Flowable<List<RepositoryModel>> {
        return Flowable.defer {
            Flowable.just(projectDatabase.cachedDao().getData())
        }.map {
            it.map { storedEntityMapper.mapFromCached(it) }
        }
    }

    /**
     * Check whether there are instances of [CachedModel] stored in the cache.
     */
    override fun isCached(): Single<Boolean> {
        return Single.defer {
            Single.just(projectDatabase.cachedDao().getData().isNotEmpty())
        }
    }

    /**
     * Set a point in time at when the cache was last updated.
     */
    override fun setLastCacheTime(lastCache: Long) {
        preferencesHelper.lastCacheTime = lastCache
    }

    /**
     * Check whether the current cached data exceeds the defined [EXPIRATION_TIME] time.
     */
    override fun isExpired(): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastUpdateTime = this.getLastCacheUpdateTimeMillis()
        return currentTime - lastUpdateTime > EXPIRATION_TIME
    }

    /**
     * Get in millis, the last time the cache was accessed.
     */
    private fun getLastCacheUpdateTimeMillis(): Long {
        return preferencesHelper.lastCacheTime
    }

}