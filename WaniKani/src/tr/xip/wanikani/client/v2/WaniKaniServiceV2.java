package tr.xip.wanikani.client.v2;

import java.util.Map;
import rx.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import tr.xip.wanikani.models.v2.reviews.Assignment;
import tr.xip.wanikani.models.v2.reviews.AssignmentCollection;
import tr.xip.wanikani.models.v2.Collection;
import tr.xip.wanikani.models.v2.reviews.ReviewStatisticCollection;
import tr.xip.wanikani.models.v2.reviews.Summary;
import tr.xip.wanikani.models.v2.srs.LevelProgression;
import tr.xip.wanikani.models.v2.srs.Reset;
import tr.xip.wanikani.models.v2.Resource;
import tr.xip.wanikani.models.v2.reviews.Review;
import tr.xip.wanikani.models.v2.reviews.ReviewCreate;
import tr.xip.wanikani.models.v2.reviews.ReviewCreateResponse;
import tr.xip.wanikani.models.v2.reviews.ReviewStatisticData;
import tr.xip.wanikani.models.v2.srs.SpacedRepetitionSystem;
import tr.xip.wanikani.models.v2.subjects.StudyMaterial;
import tr.xip.wanikani.models.v2.subjects.StudyMaterialCreate;
import tr.xip.wanikani.models.v2.subjects.Subject;
import tr.xip.wanikani.models.v2.subjects.SubjectCollection;
import tr.xip.wanikani.models.v2.subjects.VoiceActor;
import tr.xip.wanikani.models.v2.user.User;
import tr.xip.wanikani.models.v2.user.UserUpdateRequest;

public interface WaniKaniServiceV2
{
    @Headers({"Wanikani-Revision: 20170710"})
    @GET("assignments")
    Observable<AssignmentCollection> getAssignments(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("assignments/{id}")
    Observable<Assignment> getAssignment(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({"Wanikani-Revision: 20170710"})
    @PUT("assignments/{id}/start")
    Observable<Assignment> startAssignment(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("level_progressions")
    Observable<Collection<Resource<LevelProgression>>> getLevelProgressions(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("level_progressions/{id}")
    Observable<Resource<LevelProgression>> getLevelProgression(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("resets")
    Observable<Collection<Resource<Reset>>> getResets(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("resets/{id}")
    Observable<Resource<Reset>> getReset(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("reviews")
    Observable<Collection<Resource<Review>>> getReviews(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("reviews/{id}")
    Observable<Resource<Review>> getReview(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({
            "Wanikani-Revision: 20170710",
            "Content-Type: application/json; charset=utf-8"})
    @POST("reviews")
    Observable<Resource<ReviewCreateResponse>> createReview(
            @Header("Authorization") String api_key,
            @Body ReviewCreate review);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("review_statistics")
    Observable<ReviewStatisticCollection> getReviewStatistics(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("review_statistics/{id}")
    Observable<Resource<ReviewStatisticData>> getReviewStatistic(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("spaced_repetition_systems")
    Observable<Collection<Resource<SpacedRepetitionSystem>>> getSpacedRepetitionSystems(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("spaced_repetition_systems/{id}")
    Observable<Resource<SpacedRepetitionSystem>> getSpacedRepetitionSystem(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("study_materials")
    Observable<Collection<Resource<StudyMaterial>>> getStudyMaterials(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("study_materials/{id}")
    Observable<Resource<StudyMaterial>> getStudyMaterial(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({
            "Wanikani-Revision: 20170710",
            "Content-Type: application/json; charset=utf-8"})
    @POST("study_materials")
    Observable<Resource<StudyMaterial>> createStudyMaterial(
            @Header("Authorization") String api_key,
            @Body StudyMaterialCreate study_material);

    @Headers({
            "Wanikani-Revision: 20170710",
            "Content-Type: application/json; charset=utf-8"})
    @PUT("study_materials/{id}")
    Observable<Resource<StudyMaterial>> updateStudyMaterial(
            @Header("Authorization") String api_key,
            @Path("id") String id,
            @Body StudyMaterialCreate study_material);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("subjects")
    Observable<SubjectCollection> getSubjects(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("subjects/{id}")
    Observable<Subject> getSubject(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("summary")
    Observable<Summary> getSummary(
            @Header("Authorization") String api_key);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("user")
    Observable<Resource<User>> getUser(
            @Header("Authorization") String api_key);

    @Headers({
            "Wanikani-Revision: 20170710",
            "Content-Type: application/json; charset=utf-8"})
    @PUT("user")
    Observable<Resource<User>> updateUser(
            @Header("Authorization") String api_key,
            @Body UserUpdateRequest update);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("voice_actors")
    Observable<Collection<Resource<VoiceActor>>> getVoiceActors(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("voice_actors/{id}")
    Observable<Resource<VoiceActor>> getVoiceActor(
            @Header("Authorization") String api_key,
            @Path("id") String id);
}
