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
    private static String API_KEY;

    static
    {
        init();
    }

    public static void init()
    {
        API_KEY = PrefManager.getApiKey();
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

    public static Call<Collection<Resource<Assignment>>> getAssignments()
    {
        return service.getAssignments("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c");
    }

    public static Call<Resource<Assignment>> getAssignment(int id)
    {
        return service.getAssignment("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c",
                Integer.toString(id));
    }

    public static Call<Resource<Assignment>> startAssignment(int id)
    {
        return service.startAssignment("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c",
                Integer.toString(id));
    }

    public static Call<Collection<Resource<LevelProgression>>> getLevelProgressions()
    {
        return service.getLevelProgressions("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c");
    }

    public static Call<Resource<LevelProgression>> getLevelProgression(int id)
    {
        return service.getLevelProgression("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c",
                Integer.toString(id));
    }

    public static Call<Collection<Resource<Reset>>> getResets()
    {
        return service.getResets("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c");
    }

    public static Call<Resource<Reset>> getReset(int id)
    {
        return service.getReset("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c",
                Integer.toString(id));
    }

    public static Call<Collection<Resource<Review>>> getReviews()
    {
        return service.getReviews("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c");
    }

    public static Call<Resource<Review>> getReview(int id)
    {
        return service.getReview("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c",
                Integer.toString(id));
    }

    public static Call<Resource<ReviewCreateResponse>> createReview(ReviewCreate review)
    {
        return service.createReview("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c", review);
    }

    public static Call<Collection<Resource<ReviewStatistic>>> getReviewStatistics()
    {
        return service.getReviewStatistics("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c");
    }

    public static Call<Resource<ReviewStatistic>> getReviewStatistic(int id)
    {
        return service.getReviewStatistic("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c",
                Integer.toString(id));
    }

    public static Call<Collection<Resource<SpacedRepetitionSystem>>> getSpacedRepetitionSystems()
{
    return service.getSpacedRepetitionSystems("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c");
}

    public static Call<Resource<SpacedRepetitionSystem>> getSpacedRepetitionSystem(int id)
    {
        return service.getSpacedRepetitionSystem("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c",
                Integer.toString(id));
    }

    public static Call<Collection<Resource<StudyMaterial>>> getStudyMaterials()
    {
        return service.getStudyMaterials("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c");
    }

    public static Call<Resource<StudyMaterial>> getStudyMaterial(int id)
    {
        return service.getStudyMaterial("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c",
                Integer.toString(id));
    }

    public static Call<Resource<StudyMaterial>> createStudyMaterial(StudyMaterialCreate studyMaterial)
    {
        return service.createStudyMaterial("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c",
                studyMaterial);
    }

    public static Call<Resource<StudyMaterial>> updateStudyMaterial(
            int id, StudyMaterialCreate studyMaterial)
    {
        return service.updateStudyMaterial("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c",
                Integer.toString(id), studyMaterial);
    }

    public static Call<Collection<Resource<Subject>>> getSubjects()
    {
        return service.getSubjects("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c");
    }

    public static Call<Resource<Subject>> getSubject(int id)
    {
        return service.getSubject("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c",
                Integer.toString(id));
    }

    public static Call<BaseResponse<Summary>> getSummary()
    {
        return service.getSummary("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c");
    }

    public static Call<Resource<User>> getUser()
    {
        return service.getUser("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c");
    }

    public static Call<Resource<User>> updateUser(UserUpdateRequest update)
    {
        return service.updateUser("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c", update);
    }

    public static Call<Collection<Resource<VoiceActor>>> getVoiceActors()
    {
        return service.getVoiceActors("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c");
    }

    public static Call<Resource<VoiceActor>> getVoiceActor(int id)
    {
        return service.getVoiceActor("Bearer d0cd0451-2fb5-445c-9eac-7fa17828242c",
                Integer.toString(id));
    }
}
