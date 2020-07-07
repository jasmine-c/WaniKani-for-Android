package tr.xip.wanikani.client.v2;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import tr.xip.wanikani.models.v2.BaseResponse;
import tr.xip.wanikani.models.v2.reviews.Assignment;
import tr.xip.wanikani.models.v2.Collection;
import tr.xip.wanikani.models.v2.reviews.Summary;
import tr.xip.wanikani.models.v2.srs.LevelProgression;
import tr.xip.wanikani.models.v2.srs.Reset;
import tr.xip.wanikani.models.v2.Resource;
import tr.xip.wanikani.models.v2.reviews.Review;
import tr.xip.wanikani.models.v2.reviews.ReviewCreate;
import tr.xip.wanikani.models.v2.reviews.ReviewCreateResponse;
import tr.xip.wanikani.models.v2.reviews.ReviewStatistic;
import tr.xip.wanikani.models.v2.srs.SpacedRepetitionSystem;
import tr.xip.wanikani.models.v2.subjects.StudyMaterial;
import tr.xip.wanikani.models.v2.subjects.StudyMaterialCreate;
import tr.xip.wanikani.models.v2.subjects.Subject;
import tr.xip.wanikani.models.v2.subjects.VoiceActor;
import tr.xip.wanikani.models.v2.user.User;
import tr.xip.wanikani.models.v2.user.UserUpdateRequest;

public interface WaniKaniServiceV2
{
    @Headers({"Wanikani-Revision: 20170710"})
    @GET("assignments")
    Call<Collection<Resource<Assignment>>> getAssignments(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("assignments/{id}")
    Call<Resource<Assignment>> getAssignment(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({"Wanikani-Revision: 20170710"})
    @PUT("assignments/{id}/start")
    Call<Resource<Assignment>> startAssignment(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("level_progressions")
    Call<Collection<Resource<LevelProgression>>> getLevelProgressions(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("level_progressions/{id}")
    Call<Resource<LevelProgression>> getLevelProgression(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("resets")
    Call<Collection<Resource<Reset>>> getResets(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("resets/{id}")
    Call<Resource<Reset>> getReset(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("reviews")
    Call<Collection<Resource<Review>>> getReviews(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("reviews/{id}")
    Call<Resource<Review>> getReview(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({
            "Wanikani-Revision: 20170710",
            "Content-Type: application/json; charset=utf-8"})
    @POST("reviews")
    Call<Resource<ReviewCreateResponse>> createReview(
            @Header("Authorization") String api_key,
            @Body ReviewCreate review);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("review_statistics")
    Call<Collection<Resource<ReviewStatistic>>> getReviewStatistics(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("review_statistics/{id}")
    Call<Resource<ReviewStatistic>> getReviewStatistic(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("spaced_repetition_systems")
    Call<Collection<Resource<SpacedRepetitionSystem>>> getSpacedRepetitionSystems(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("spaced_repetition_systems/{id}")
    Call<Resource<SpacedRepetitionSystem>> getSpacedRepetitionSystem(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("study_materials")
    Call<Collection<Resource<StudyMaterial>>> getStudyMaterials(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("study_materials/{id}")
    Call<Resource<StudyMaterial>> getStudyMaterial(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({
            "Wanikani-Revision: 20170710",
            "Content-Type: application/json; charset=utf-8"})
    @POST("study_materials")
    Call<Resource<StudyMaterial>> createStudyMaterial(
            @Header("Authorization") String api_key,
            @Body StudyMaterialCreate study_material);

    @Headers({
            "Wanikani-Revision: 20170710",
            "Content-Type: application/json; charset=utf-8"})
    @PUT("study_materials/{id}")
    Call<Resource<StudyMaterial>> updateStudyMaterial(
            @Header("Authorization") String api_key,
            @Path("id") String id,
            @Body StudyMaterialCreate study_material);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("subjects")
    Call<Collection<Resource<Subject>>> getSubjects(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("subjects/{id}")
    Call<Resource<Subject>> getSubject(
            @Header("Authorization") String api_key,
            @Path("id") String id);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("summary")
    Call<Summary> getSummary(
            @Header("Authorization") String api_key);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("user")
    Call<Resource<User>> getUser(
            @Header("Authorization") String api_key);

    @Headers({
            "Wanikani-Revision: 20170710",
            "Content-Type: application/json; charset=utf-8"})
    @PUT("user")
    Call<Resource<User>> updateUser(
            @Header("Authorization") String api_key,
            @Body UserUpdateRequest update);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("voice_actors")
    Call<Collection<Resource<VoiceActor>>> getVoiceActors(
            @Header("Authorization") String api_key,
            @QueryMap Map<String, String> filters);

    @Headers({"Wanikani-Revision: 20170710"})
    @GET("voice_actors/{id}")
    Call<Resource<VoiceActor>> getVoiceActor(
            @Header("Authorization") String api_key,
            @Path("id") String id);
}
