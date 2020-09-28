package tr.xip.wanikani.client.v2;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import tr.xip.wanikani.BuildConfig;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.models.v2.Collection;
import tr.xip.wanikani.models.v2.Resource;
import tr.xip.wanikani.models.v2.reviews.Assignment;
import tr.xip.wanikani.models.v2.reviews.AssignmentCollection;
import tr.xip.wanikani.models.v2.reviews.Review;
import tr.xip.wanikani.models.v2.reviews.ReviewCreate;
import tr.xip.wanikani.models.v2.reviews.ReviewCreateResponse;
import tr.xip.wanikani.models.v2.reviews.ReviewStatisticCollection;
import tr.xip.wanikani.models.v2.reviews.ReviewStatisticData;
import tr.xip.wanikani.models.v2.reviews.Summary;
import tr.xip.wanikani.models.v2.srs.LevelProgression;
import tr.xip.wanikani.models.v2.srs.Reset;
import tr.xip.wanikani.models.v2.srs.SpacedRepetitionSystem;
import tr.xip.wanikani.models.v2.subjects.StudyMaterial;
import tr.xip.wanikani.models.v2.subjects.StudyMaterialCreate;
import tr.xip.wanikani.models.v2.subjects.Subject;
import tr.xip.wanikani.models.v2.subjects.SubjectCollection;
import tr.xip.wanikani.models.v2.subjects.VoiceActor;
import tr.xip.wanikani.models.v2.user.User;
import tr.xip.wanikani.models.v2.user.UserUpdateRequest;

public abstract class WaniKaniApiV2 {

	private static WaniKaniServiceV2 service;
	private static String authorizationToken;

	static {
		init();
	}

	public static void init() {
		authorizationToken = "Bearer " + PrefManager.getV2ApiKey();
		setupService();
	}

	private static void setupService() {
		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
		if (BuildConfig.DEBUG) {
			HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
			httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
			clientBuilder.addInterceptor(httpLoggingInterceptor);
		}

		Gson gson = new GsonBuilder()
			.registerTypeAdapter(DateTime.class, new JsonDeserializer<DateTime>() {
				@Override
				public DateTime deserialize(
					JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext)
					throws JsonParseException {
					return DateTime.parse(json.getAsJsonPrimitive().getAsString());
				}
			})
			.registerTypeAdapter(Subject.class, new JsonDeserializer<Subject>() {
				@Override
				public Subject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
					return Subject.ParseSubject(json, context);
				}
			})
			.create();

		Retrofit retrofit = new Retrofit.Builder()
			.client(clientBuilder.build())
			.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
			.addConverterFactory(GsonConverterFactory.create(gson))
			.baseUrl("https://api.wanikani.com/v2/")
			.build();

		service = retrofit.create(WaniKaniServiceV2.class);
	}

	public static Observable<AssignmentCollection> getAssignments(final Filter filters) {
		return service.getAssignments(authorizationToken, filters.filters)
			.concatMap(new Func1<AssignmentCollection, Observable<AssignmentCollection>>() {

				@Override
				public Observable<AssignmentCollection> call(AssignmentCollection response) {
					// Terminal case.
					if (response.pages.next_url == null) {
						return Observable.just(response);
					}

					Uri url = Uri.parse(response.pages.next_url);

					return Observable.just(response)
						.concatWith(getAssignments(new Filter()
							.page_after_id(Integer.parseInt(url.getQueryParameter("page_after_id")))));
				}
			})
			.reduce(new Func2<AssignmentCollection, AssignmentCollection, AssignmentCollection>() {
				@Override
				public AssignmentCollection call(AssignmentCollection assignmentCollection, AssignmentCollection assignmentCollection2) {
					assignmentCollection.data.addAll(assignmentCollection2.data);
					return assignmentCollection;
				}
			});
	}

	public static Observable<Assignment> getAssignment(int id) {
		return service.getAssignment(authorizationToken,
			Integer.toString(id));
	}

	public static Observable<Assignment> startAssignment(int id) {
		return service.startAssignment(authorizationToken,
			Integer.toString(id));
	}

	public static Observable<Collection<Resource<LevelProgression>>> getLevelProgressions(Filter filters) {
		return service.getLevelProgressions(authorizationToken,
			filters.filters);
	}

	public static Observable<Resource<LevelProgression>> getLevelProgression(int id) {
		return service.getLevelProgression(authorizationToken,
			Integer.toString(id));
	}

	public static Observable<Collection<Resource<Reset>>> getResets(Filter filters) {
		return service.getResets(authorizationToken,
			filters.filters);
	}

	public static Observable<Resource<Reset>> getReset(int id) {
		return service.getReset(authorizationToken,
			Integer.toString(id));
	}

	public static Observable<Collection<Resource<Review>>> getReviews(Filter filters) {
		return service.getReviews(authorizationToken,
			filters.filters);
	}

	public static Observable<Resource<Review>> getReview(int id) {
		return service.getReview(authorizationToken,
			Integer.toString(id));
	}

	public static Observable<Resource<ReviewCreateResponse>> createReview(ReviewCreate review) {
		return service.createReview(authorizationToken, review);
	}

	public static Observable<ReviewStatisticCollection> getReviewStatistics(Filter filters) {
		return service.getReviewStatistics(authorizationToken,
			filters.filters);
	}

	public static Observable<Resource<ReviewStatisticData>> getReviewStatistic(int id) {
		return service.getReviewStatistic(authorizationToken,
			Integer.toString(id));
	}

	public static Observable<Collection<Resource<SpacedRepetitionSystem>>> getSpacedRepetitionSystems(
		Filter filters) {
		return service.getSpacedRepetitionSystems(authorizationToken,
			filters.filters);
	}

	public static Observable<Resource<SpacedRepetitionSystem>> getSpacedRepetitionSystem(int id) {
		return service.getSpacedRepetitionSystem(authorizationToken,
			Integer.toString(id));
	}

	public static Observable<Collection<Resource<StudyMaterial>>> getStudyMaterials(Filter filters) {
		return service.getStudyMaterials(authorizationToken,
			filters.filters);
	}

	public static Observable<Resource<StudyMaterial>> getStudyMaterial(int id) {
		return service.getStudyMaterial(authorizationToken,
			Integer.toString(id));
	}

	public static Observable<Resource<StudyMaterial>> createStudyMaterial(StudyMaterialCreate studyMaterial) {
		return service.createStudyMaterial(authorizationToken,
			studyMaterial);
	}

	public static Observable<Resource<StudyMaterial>> updateStudyMaterial(
		int id, StudyMaterialCreate studyMaterial) {
		return service.updateStudyMaterial(authorizationToken,
			Integer.toString(id), studyMaterial);
	}

	public static Observable<SubjectCollection> getSubjects(Filter filters) {
		return service.getSubjects(authorizationToken, filters.filters)
			.concatMap(new Func1<SubjectCollection, Observable<SubjectCollection>>() {

				@Override
				public Observable<SubjectCollection> call(SubjectCollection response) {
					// Terminal case.
					if (response.pages.next_url == null) {
						return Observable.just(response);
					}

					Uri url = Uri.parse(response.pages.next_url);

					return Observable
						.just(response)
						.concatWith(getSubjects(new Filter().page_after_id(Integer.parseInt(url.getQueryParameter("page_after_id")))));
				}
			})
			.reduce(new Func2<SubjectCollection, SubjectCollection, SubjectCollection>() {
				@Override
				public SubjectCollection call(SubjectCollection subjectCollection, SubjectCollection subjectCollection2) {
					subjectCollection.data.addAll(subjectCollection2.data);
					return subjectCollection;
				}
			});
	}

	public static Observable<Subject> getSubject(int id) {
		return service.getSubject(authorizationToken,
			Integer.toString(id));
	}

	public static Observable<Summary> getSummary() {
		return service.getSummary(authorizationToken);
	}

	public static Observable<Resource<User>> getUser() {
		return service.getUser(authorizationToken);
	}

	public static Observable<Resource<User>> updateUser(UserUpdateRequest update) {
		return service.updateUser(authorizationToken, update);
	}

	public static Observable<Collection<Resource<VoiceActor>>> getVoiceActors(Filter filters) {
		return service.getVoiceActors(authorizationToken,
			filters.filters);
	}

	public static Observable<Resource<VoiceActor>> getVoiceActor(int id) {
		return service.getVoiceActor(authorizationToken,
			Integer.toString(id));
	}
}
