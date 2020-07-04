package tr.xip.wanikani.client.v2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.joda.time.DateTime;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tr.xip.wanikani.BuildConfig;
import tr.xip.wanikani.managers.PrefManager;
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

public abstract class WaniKaniApiV2
{

    private static WaniKaniServiceV2 service;
    private static String authorizationToken;

    static
    {
        init();
    }

    public static void init()
    {
        authorizationToken = "Bearer " + PrefManager.getV2ApiKey();
        setupService();
    }

    private static void setupService()
    {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG)
        {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(httpLoggingInterceptor);
        }

        Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new JsonDeserializer<DateTime>() {
            @Override
            public DateTime deserialize(
                    JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext)
                    throws JsonParseException
            {
                return DateTime.parse(json.getAsJsonPrimitive().getAsString());
            }
        }).create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://api.wanikani.com/v2/")
                .build();

        service = retrofit.create(WaniKaniServiceV2.class);
    }

    public static Call<Collection<Resource<Assignment>>> getAssignments(Filter filters)
    {
        return service.getAssignments(authorizationToken,
                filters.filters);
    }

    public static Call<Resource<Assignment>> getAssignment(int id)
    {
        return service.getAssignment(authorizationToken,
                Integer.toString(id));
    }

    public static Call<Resource<Assignment>> startAssignment(int id)
    {
        return service.startAssignment(authorizationToken,
                Integer.toString(id));
    }

    public static Call<Collection<Resource<LevelProgression>>> getLevelProgressions(Filter filters)
    {
        return service.getLevelProgressions(authorizationToken,
                filters.filters);
    }

    public static Call<Resource<LevelProgression>> getLevelProgression(int id)
    {
        return service.getLevelProgression(authorizationToken,
                Integer.toString(id));
    }

    public static Call<Collection<Resource<Reset>>> getResets(Filter filters)
    {
        return service.getResets(authorizationToken,
                filters.filters);
    }

    public static Call<Resource<Reset>> getReset(int id)
    {
        return service.getReset(authorizationToken,
                Integer.toString(id));
    }

    public static Call<Collection<Resource<Review>>> getReviews(Filter filters)
    {
        return service.getReviews(authorizationToken,
                filters.filters);
    }

    public static Call<Resource<Review>> getReview(int id)
    {
        return service.getReview(authorizationToken,
                Integer.toString(id));
    }

    public static Call<Resource<ReviewCreateResponse>> createReview(ReviewCreate review)
    {
        return service.createReview(authorizationToken, review);
    }

    public static Call<Collection<Resource<ReviewStatistic>>> getReviewStatistics(Filter filters)
    {
        return service.getReviewStatistics(authorizationToken,
                filters.filters);
    }

    public static Call<Resource<ReviewStatistic>> getReviewStatistic(int id)
    {
        return service.getReviewStatistic(authorizationToken,
                Integer.toString(id));
    }

    public static Call<Collection<Resource<SpacedRepetitionSystem>>> getSpacedRepetitionSystems(
            Filter filters)
    {
        return service.getSpacedRepetitionSystems(authorizationToken,
            filters.filters);
    }

    public static Call<Resource<SpacedRepetitionSystem>> getSpacedRepetitionSystem(int id)
    {
        return service.getSpacedRepetitionSystem(authorizationToken,
                Integer.toString(id));
    }

    public static Call<Collection<Resource<StudyMaterial>>> getStudyMaterials(Filter filters)
    {
        return service.getStudyMaterials(authorizationToken,
                filters.filters);
    }

    public static Call<Resource<StudyMaterial>> getStudyMaterial(int id)
    {
        return service.getStudyMaterial(authorizationToken,
                Integer.toString(id));
    }

    public static Call<Resource<StudyMaterial>> createStudyMaterial(StudyMaterialCreate studyMaterial)
    {
        return service.createStudyMaterial(authorizationToken,
                studyMaterial);
    }

    public static Call<Resource<StudyMaterial>> updateStudyMaterial(
            int id, StudyMaterialCreate studyMaterial)
    {
        return service.updateStudyMaterial(authorizationToken,
                Integer.toString(id), studyMaterial);
    }

    public static Call<Collection<Resource<Subject>>> getSubjects(Filter filters)
    {
        return service.getSubjects(authorizationToken,
                filters.filters);
    }

    public static Call<Resource<Subject>> getSubject(int id)
    {
        return service.getSubject(authorizationToken,
                Integer.toString(id));
    }

    public static Call<BaseResponse<Summary>> getSummary()
    {
        return service.getSummary(authorizationToken);
    }

    public static Call<Resource<User>> getUser()
    {
        return service.getUser(authorizationToken);
    }

    public static Call<Resource<User>> updateUser(UserUpdateRequest update)
    {
        return service.updateUser(authorizationToken, update);
    }

    public static Call<Collection<Resource<VoiceActor>>> getVoiceActors(Filter filters)
    {
        return service.getVoiceActors(authorizationToken,
                filters.filters);
    }

    public static Call<Resource<VoiceActor>> getVoiceActor(int id)
    {
        return service.getVoiceActor(authorizationToken,
                Integer.toString(id));
    }
}
