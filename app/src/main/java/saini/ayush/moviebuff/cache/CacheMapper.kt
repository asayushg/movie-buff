package saini.ayush.moviebuff.cache


import saini.ayush.moviebuff.model.Movie
import javax.inject.Inject

class CacheMapper
@Inject
constructor() :
    EntityMapper<MovieCacheEntity, Movie> {
    override fun mapFromEntity(entity: MovieCacheEntity): Movie {
        return Movie(
            title = entity.title,
            overview = entity.overview,
            popularity = entity.popularity,
            release_date = entity.release_date,
            poster_path = entity.poster_path,
            backdrop_path = entity.backdrop_path,
            vote_average = entity.vote_average
        )
    }

    override fun mapToEntity(domainModel: Movie): MovieCacheEntity {
        return MovieCacheEntity(
            id = domainModel.id,
            title = domainModel.title,
            overview = domainModel.overview,
            popularity = domainModel.popularity,
            release_date = domainModel.release_date,
            poster_path = domainModel.poster_path,
            backdrop_path = domainModel.backdrop_path ?: "",
            vote_average = domainModel.vote_average
        )
    }

    fun mapFromEntityList(entities: List<MovieCacheEntity>): List<Movie> {
        return entities.map { mapFromEntity(it) }
    }

    fun mapToEntityList(entities: List<Movie>): List<MovieCacheEntity> {
        return entities.map { mapToEntity(it) }
    }

}