package tr.xip.wanikani.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import tr.xip.wanikani.client.v2.Filter;
import tr.xip.wanikani.database.table.CriticalItemsTable;
import tr.xip.wanikani.database.table.ItemsTable;
import tr.xip.wanikani.database.table.LevelProgressionTable;
import tr.xip.wanikani.database.table.NotificationsTable;
import tr.xip.wanikani.database.table.RecentUnlocksTable;
import tr.xip.wanikani.database.table.SRSDistributionTable;
import tr.xip.wanikani.database.table.StudyQueueTable;
import tr.xip.wanikani.database.table.UsersTable;
import tr.xip.wanikani.database.v2.AssignmentsTable;
import tr.xip.wanikani.database.v2.LastUpdatedTable;
import tr.xip.wanikani.database.v2.SummaryTable;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.models.CriticalItem;
import tr.xip.wanikani.models.CriticalItemsList;
import tr.xip.wanikani.models.ItemsList;
import tr.xip.wanikani.models.LevelProgression;
import tr.xip.wanikani.models.Notification;
import tr.xip.wanikani.models.RecentUnlocksList;
import tr.xip.wanikani.models.SRSDistribution;
import tr.xip.wanikani.models.StudyQueue;
import tr.xip.wanikani.models.UnlockItem;
import tr.xip.wanikani.models.User;
import tr.xip.wanikani.models.v2.reviews.Assignment;
import tr.xip.wanikani.models.v2.reviews.AssignmentData;
import tr.xip.wanikani.models.v2.reviews.Lesson;
import tr.xip.wanikani.models.v2.reviews.Summary;
import tr.xip.wanikani.models.v2.reviews.SummaryData;

public class DatabaseManager {
    private static final String TAG = "Database Manager";

    private static SQLiteDatabase db;

    private static ReadWriteLock AssignmentsLock = new ReentrantReadWriteLock();
    private static ReadWriteLock LastUpdatedLock = new ReentrantReadWriteLock();
    private static ReadWriteLock SummaryLock = new ReentrantReadWriteLock();

    public static void init(Context context) {
        if (db == null) {
            db = DatabaseHelper.getInstance(context).getWritableDatabase();
        }
    }

    private static void addItem(BaseItem item) {
        ContentValues values = new ContentValues();
        values.put(ItemsTable.COLUMN_NAME_CHARACTER, item.getCharacter());
        values.put(ItemsTable.COLUMN_NAME_KANA, item.getKana());
        values.put(ItemsTable.COLUMN_NAME_MEANING, item.getMeaning());
        values.put(ItemsTable.COLUMN_NAME_IMAGE, item.getImage());
        values.put(ItemsTable.COLUMN_NAME_ONYOMI, item.getOnyomi());
        values.put(ItemsTable.COLUMN_NAME_KUNYOMI, item.getKunyomi());
        values.put(ItemsTable.COLUMN_NAME_IMPORTANT_READING, item.getImportantReading());
        values.put(ItemsTable.COLUMN_NAME_LEVEL, item.getLevel());
        values.put(ItemsTable.COLUMN_NAME_ITEM_TYPE, item.getType().toString());
        values.put(ItemsTable.COLUMN_NAME_SRS, item.getSrsLevel());
        values.put(ItemsTable.COLUMN_NAME_UNLOCKED_DATE, item.getUnlockDateInSeconds());
        values.put(ItemsTable.COLUMN_NAME_AVAILABLE_DATE, item.getAvailableDateInSeconds());
        values.put(ItemsTable.COLUMN_NAME_BURNED, item.isBurned() ? 1 : 0);
        values.put(ItemsTable.COLUMN_NAME_BURNED_DATE, item.getBurnedDateInSeconds());
        values.put(ItemsTable.COLUMN_NAME_MEANING_CORRECT, item.getMeaningCorrect());
        values.put(ItemsTable.COLUMN_NAME_MEANING_INCORRECT, item.getMeaningIncorrect());
        values.put(ItemsTable.COLUMN_NAME_MEANING_MAX_STREAK, item.getMeaningMaxStreak());
        values.put(ItemsTable.COLUMN_NAME_MEANING_CURRENT_STREAK, item.getMeaningCurrentStreak());
        values.put(ItemsTable.COLUMN_NAME_READING_CORRECT, item.getReadingCorrect());
        values.put(ItemsTable.COLUMN_NAME_READING_INCORRECT, item.getReadingIncorrect());
        values.put(ItemsTable.COLUMN_NAME_READING_MAX_STREAK, item.getReadingMaxStreak());
        values.put(ItemsTable.COLUMN_NAME_READING_CURRENT_STREAK, item.getReadingCurrentStreak());
        values.put(ItemsTable.COLUMN_NAME_MEANING_NOTE, item.getMeaningNote());
        values.put(ItemsTable.COLUMN_NAME_USER_SYNONYMS, item.getUserSynonymsAsString());
        values.put(ItemsTable.COLUMN_NAME_READING_NOTE, item.getReadingNote());

        db.insert(ItemsTable.TABLE_NAME, ItemsTable.COLUMN_NAME_NULLABLE, values);
    }

    public static void saveItems(List<BaseItem> list, BaseItem.ItemType type) {
        deleteAllItemsFromSameLevelAndType(list, type);

        for (BaseItem item : list)
            addItem(item);
    }

    public static ItemsList getItems(BaseItem.ItemType itemType, int[] levels) {
        ItemsList list = new ItemsList();

        for (int level : levels) {
            String whereClause = ItemsTable.COLUMN_NAME_ITEM_TYPE + " = ?" + " AND "
                    + ItemsTable.COLUMN_NAME_LEVEL + " = ?";
            String[] whereArgs = {
                    itemType.toString(),
                    String.valueOf(level)
            };

            Cursor c = null;

            try {
                c = db.query(
                        ItemsTable.TABLE_NAME,
                        ItemsTable.COLUMNS,
                        whereClause,
                        whereArgs,
                        null,
                        null,
                        null
                );

                if (c != null && c.moveToFirst()) {
                    do {
                        BaseItem item = new BaseItem(
                                c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_ID)),
                                c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_CHARACTER)),
                                c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_KANA)),
                                c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_MEANING)),
                                c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_IMAGE)),
                                c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_ONYOMI)),
                                c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_KUNYOMI)),
                                c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_IMPORTANT_READING)),
                                c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_LEVEL)),
                                c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_ITEM_TYPE)),
                                c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_SRS)),
                                c.getLong(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_UNLOCKED_DATE)),
                                c.getLong(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_AVAILABLE_DATE)),
                                c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_BURNED)) == 1,
                                c.getLong(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_BURNED_DATE)),
                                c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_MEANING_CORRECT)),
                                c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_MEANING_INCORRECT)),
                                c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_MEANING_MAX_STREAK)),
                                c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_MEANING_CURRENT_STREAK)),
                                c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_READING_CORRECT)),
                                c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_READING_INCORRECT)),
                                c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_READING_MAX_STREAK)),
                                c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_READING_CURRENT_STREAK)),
                                c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_MEANING_NOTE)),
                                c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_USER_SYNONYMS)) != null
                                        ? c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_USER_SYNONYMS)).split(",")
                                        : null,
                                c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_READING_NOTE))
                        );
                        list.add(item);
                    } while (c.moveToNext());
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }

        return list.size() != 0 ? list : null;
    }

    private static void deleteAllItemsFromSameLevelAndType(List<BaseItem> list, BaseItem.ItemType type) {
        HashSet<Integer> levels = new HashSet();

        for (BaseItem item : list)
            levels.add(item.getLevel());

        for (Integer level : levels) {
            String whereClause = ItemsTable.COLUMN_NAME_LEVEL + " = ?" + " AND "
                    + ItemsTable.COLUMN_NAME_ITEM_TYPE + " = ?";
            String[] whereArgs = {
                    String.valueOf(level),
                    type.toString()
            };

            db.delete(ItemsTable.TABLE_NAME, whereClause, whereArgs);
        }
    }

    private static void addRecentUnlock(UnlockItem item) {
        ContentValues values = new ContentValues();
        values.put(RecentUnlocksTable.COLUMN_NAME_CHARACTER, item.getCharacter());
        values.put(RecentUnlocksTable.COLUMN_NAME_KANA, item.getKana());
        values.put(RecentUnlocksTable.COLUMN_NAME_MEANING, item.getMeaning());
        values.put(RecentUnlocksTable.COLUMN_NAME_IMAGE, item.getImage());
        values.put(RecentUnlocksTable.COLUMN_NAME_ONYOMI, item.getOnyomi());
        values.put(RecentUnlocksTable.COLUMN_NAME_KUNYOMI, item.getKunyomi());
        values.put(RecentUnlocksTable.COLUMN_NAME_IMPORTANT_READING, item.getImportantReading());
        values.put(RecentUnlocksTable.COLUMN_NAME_LEVEL, item.getLevel());
        values.put(RecentUnlocksTable.COLUMN_NAME_ITEM_TYPE, item.getType().toString());
        values.put(RecentUnlocksTable.COLUMN_NAME_SRS, item.getSrsLevel());
        values.put(RecentUnlocksTable.COLUMN_NAME_UNLOCKED_DATE, item.getUnlockDateInSeconds());
        values.put(RecentUnlocksTable.COLUMN_NAME_AVAILABLE_DATE, item.getAvailableDateInSeconds());
        values.put(RecentUnlocksTable.COLUMN_NAME_BURNED, item.isBurned() ? 1 : 0);
        values.put(RecentUnlocksTable.COLUMN_NAME_BURNED_DATE, item.getBurnedDateInSeconds());
        values.put(RecentUnlocksTable.COLUMN_NAME_MEANING_CORRECT, item.getMeaningCorrect());
        values.put(RecentUnlocksTable.COLUMN_NAME_MEANING_INCORRECT, item.getMeaningIncorrect());
        values.put(RecentUnlocksTable.COLUMN_NAME_MEANING_MAX_STREAK, item.getMeaningMaxStreak());
        values.put(RecentUnlocksTable.COLUMN_NAME_MEANING_CURRENT_STREAK, item.getMeaningCurrentStreak());
        values.put(RecentUnlocksTable.COLUMN_NAME_READING_CORRECT, item.getReadingCorrect());
        values.put(RecentUnlocksTable.COLUMN_NAME_READING_INCORRECT, item.getReadingIncorrect());
        values.put(RecentUnlocksTable.COLUMN_NAME_READING_MAX_STREAK, item.getReadingMaxStreak());
        values.put(RecentUnlocksTable.COLUMN_NAME_READING_CURRENT_STREAK, item.getReadingCurrentStreak());
        values.put(RecentUnlocksTable.COLUMN_NAME_MEANING_NOTE, item.getMeaningNote());
        values.put(RecentUnlocksTable.COLUMN_NAME_USER_SYNONYMS, item.getUserSynonymsAsString());
        values.put(RecentUnlocksTable.COLUMN_NAME_READING_NOTE, item.getReadingNote());

        db.insert(RecentUnlocksTable.TABLE_NAME, RecentUnlocksTable.COLUMN_NAME_NULLABLE, values);
    }

    public static void saveRecentUnlocks(List<UnlockItem> list) {
        deleteSameRecentUnlocks(list);

        for (UnlockItem item : list)
            addRecentUnlock(item);
    }

    public static RecentUnlocksList getRecentUnlocks(int limit) {
        RecentUnlocksList list = new RecentUnlocksList();

        Cursor c = null;

        try {
            c = db.query(
                    RecentUnlocksTable.TABLE_NAME,
                    RecentUnlocksTable.COLUMNS,
                    null,
                    null,
                    null,
                    null,
                    null,
                    String.valueOf(limit)
            );

            if (c != null && c.moveToFirst()) {
                do {
                    UnlockItem item = new UnlockItem(
                            c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_ID)),
                            c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_CHARACTER)),
                            c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_KANA)),
                            c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_MEANING)),
                            c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_IMAGE)),
                            c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_ONYOMI)),
                            c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_KUNYOMI)),
                            c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_IMPORTANT_READING)),
                            c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_LEVEL)),
                            c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_ITEM_TYPE)),
                            c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_SRS)),
                            c.getLong(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_UNLOCKED_DATE)),
                            c.getLong(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_AVAILABLE_DATE)),
                            c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_BURNED)) == 1,
                            c.getLong(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_BURNED_DATE)),
                            c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_MEANING_CORRECT)),
                            c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_MEANING_INCORRECT)),
                            c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_MEANING_MAX_STREAK)),
                            c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_MEANING_CURRENT_STREAK)),
                            c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_READING_CORRECT)),
                            c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_READING_INCORRECT)),
                            c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_READING_MAX_STREAK)),
                            c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_READING_CURRENT_STREAK)),
                            c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_MEANING_NOTE)),
                            c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_USER_SYNONYMS)) != null
                                    ? c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_USER_SYNONYMS)).split(",")
                                    : null,
                            c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_READING_NOTE))
                    );
                    list.add(item);
                } while (c.moveToNext());
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return list.size() != 0 ? list : null;
    }

    public static void deleteSameRecentUnlocks(List<UnlockItem> list) {
        for (UnlockItem item : list) {
            String whereClause = item.getImage() == null
                    ? RecentUnlocksTable.COLUMN_NAME_CHARACTER + " = ?"
                    : RecentUnlocksTable.COLUMN_NAME_IMAGE + " = ?";
            String[] whereArgs = {
                    item.getImage() == null ? String.valueOf(item.getCharacter()) : item.getImage()
            };

            db.delete(RecentUnlocksTable.TABLE_NAME, whereClause, whereArgs);
        }
    }

    private static void addCriticalItem(CriticalItem item) {
        if (item == null) return; // WANIKANI API SMH

        ContentValues values = new ContentValues();
        values.put(CriticalItemsTable.COLUMN_NAME_CHARACTER, item.getCharacter());
        values.put(CriticalItemsTable.COLUMN_NAME_KANA, item.getKana());
        values.put(CriticalItemsTable.COLUMN_NAME_MEANING, item.getMeaning());
        values.put(CriticalItemsTable.COLUMN_NAME_IMAGE, item.getImage());
        values.put(CriticalItemsTable.COLUMN_NAME_ONYOMI, item.getOnyomi());
        values.put(CriticalItemsTable.COLUMN_NAME_KUNYOMI, item.getKunyomi());
        values.put(CriticalItemsTable.COLUMN_NAME_IMPORTANT_READING, item.getImportantReading());
        values.put(CriticalItemsTable.COLUMN_NAME_LEVEL, item.getLevel());
        values.put(CriticalItemsTable.COLUMN_NAME_ITEM_TYPE, item.getType().toString());
        values.put(CriticalItemsTable.COLUMN_NAME_SRS, item.getSrsLevel());
        values.put(CriticalItemsTable.COLUMN_NAME_UNLOCKED_DATE, item.getUnlockDateInSeconds());
        values.put(CriticalItemsTable.COLUMN_NAME_AVAILABLE_DATE, item.getAvailableDateInSeconds());
        values.put(CriticalItemsTable.COLUMN_NAME_BURNED, item.isBurned() ? 1 : 0);
        values.put(CriticalItemsTable.COLUMN_NAME_BURNED_DATE, item.getBurnedDateInSeconds());
        values.put(CriticalItemsTable.COLUMN_NAME_MEANING_CORRECT, item.getMeaningCorrect());
        values.put(CriticalItemsTable.COLUMN_NAME_MEANING_INCORRECT, item.getMeaningIncorrect());
        values.put(CriticalItemsTable.COLUMN_NAME_MEANING_MAX_STREAK, item.getMeaningMaxStreak());
        values.put(CriticalItemsTable.COLUMN_NAME_MEANING_CURRENT_STREAK, item.getMeaningCurrentStreak());
        values.put(CriticalItemsTable.COLUMN_NAME_READING_CORRECT, item.getReadingCorrect());
        values.put(CriticalItemsTable.COLUMN_NAME_READING_INCORRECT, item.getReadingIncorrect());
        values.put(CriticalItemsTable.COLUMN_NAME_READING_MAX_STREAK, item.getReadingMaxStreak());
        values.put(CriticalItemsTable.COLUMN_NAME_READING_CURRENT_STREAK, item.getReadingCurrentStreak());
        values.put(CriticalItemsTable.COLUMN_NAME_MEANING_NOTE, item.getMeaningNote());
        values.put(CriticalItemsTable.COLUMN_NAME_USER_SYNONYMS, item.getUserSynonymsAsString());
        values.put(CriticalItemsTable.COLUMN_NAME_READING_NOTE, item.getReadingNote());
        values.put(CriticalItemsTable.COLUMN_NAME_PERCENTAGE, item.getPercentage());

        db.insert(CriticalItemsTable.TABLE_NAME, CriticalItemsTable.COLUMN_NAME_NULLABLE, values);
    }

    public static void saveCriticalItems(CriticalItemsList list) {
        deleteSameCriticalItems(list);

        for (CriticalItem item : list)
            addCriticalItem(item);
    }

    public static CriticalItemsList getCriticalItems(int percentage) {
        CriticalItemsList list = new CriticalItemsList();

        String whereClause = CriticalItemsTable.COLUMN_NAME_PERCENTAGE + " < ?";
        String[] whereArgs = {
                String.valueOf(percentage)
        };

        Cursor c = null;

        try {
            c = db.query(
                    CriticalItemsTable.TABLE_NAME,
                    CriticalItemsTable.COLUMNS,
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    null
            );

            if (c != null && c.moveToFirst()) {
                do {
                    CriticalItem item = new CriticalItem(
                            c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_ID)),
                            c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_CHARACTER)),
                            c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_KANA)),
                            c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_MEANING)),
                            c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_IMAGE)),
                            c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_ONYOMI)),
                            c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_KUNYOMI)),
                            c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_IMPORTANT_READING)),
                            c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_LEVEL)),
                            c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_ITEM_TYPE)),
                            c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_SRS)),
                            c.getLong(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_UNLOCKED_DATE)),
                            c.getLong(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_AVAILABLE_DATE)),
                            c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_BURNED)) == 1,
                            c.getLong(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_BURNED_DATE)),
                            c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_MEANING_CORRECT)),
                            c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_MEANING_INCORRECT)),
                            c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_MEANING_MAX_STREAK)),
                            c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_MEANING_CURRENT_STREAK)),
                            c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_READING_CORRECT)),
                            c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_READING_INCORRECT)),
                            c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_READING_MAX_STREAK)),
                            c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_READING_CURRENT_STREAK)),
                            c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_MEANING_NOTE)),
                            c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_USER_SYNONYMS)) != null
                                    ? c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_USER_SYNONYMS)).split(",")
                                    : null,
                            c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_READING_NOTE)),
                            c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_PERCENTAGE))
                    );
                    list.add(item);
                } while (c.moveToNext());
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return list;
    }

    private static void deleteSameCriticalItems(List<CriticalItem> list) {
        for (CriticalItem item : list) {
            if (item == null) continue;

            String whereClause = item.getImage() == null
                    ? CriticalItemsTable.COLUMN_NAME_CHARACTER + " = ?"
                    : CriticalItemsTable.COLUMN_NAME_IMAGE + " = ?";
            String[] whereArgs = {
                    item.getImage() == null ? String.valueOf(item.getCharacter()) : item.getImage()
            };

            db.delete(CriticalItemsTable.TABLE_NAME, whereClause, whereArgs);
        }
    }

    public static void saveSummary(Summary summary) {
        SummaryLock.writeLock().lock();

        try {
            deleteSummary();

            for (Lesson lesson : summary.data.lessons) {
                InsertLesson(lesson, lesson.subject_ids, "lesson");
            }

            for (Lesson review : summary.data.reviews) {
                InsertLesson(review, review.subject_ids, "review");
            }
        }
        finally {
            SummaryLock.writeLock().unlock();
        }
    }

    private static void InsertLesson(Lesson lesson, ArrayList<Integer> ids, String type) {
        ContentValues values = new ContentValues();

        values.put(SummaryTable.COLUMN_NAME_SUBJECT_ID, Filter.encodeParamsArray(ids.toArray()));
        values.put(SummaryTable.COLUMN_NAME_AVAILABLE_AT, lesson.available_at.getMillis());
        values.put(SummaryTable.COLUMN_NAME_TYPE, type);

        db.insert(SummaryTable.TABLE_NAME, SummaryTable.COLUMN_NAME_NULLABLE, values);
    }

    private static void deleteSummary() {
        db.delete(SummaryTable.TABLE_NAME, null, null);
    }

    public static Summary getSummary() {
        SummaryLock.readLock().lock();

        Cursor c = null;

        try {
            c = db.query(
                    SummaryTable.TABLE_NAME,
                    SummaryTable.COLUMNS,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (c == null || !c.moveToFirst())
            {
                Log.e(TAG, "No summary found; returning null");
                return null;
            }

            ArrayList<Lesson> lessons = new ArrayList<>();
            ArrayList<Lesson> reviews = new ArrayList<>();
            DateTime nextReview = new DateTime(0);

            do {
                String type = c.getString(c.getColumnIndexOrThrow(SummaryTable.COLUMN_NAME_TYPE));

                switch (type)
                {
                    case "lesson":
                        lessons.add(getLesson(c));
                        break;
                    case "review":
                        reviews.add(getLesson(c));
                        break;
                }
            } while (c.moveToNext());

            return new Summary(null, null, null,
                    new SummaryData(lessons, nextReview, reviews));
        } finally {
            SummaryLock.readLock().unlock();

            if (c != null) {
                c.close();
            }
        }
    }

    private static Lesson getLesson(Cursor c)
    {
        DateTime availableAt = new DateTime(
                c.getInt(c.getColumnIndexOrThrow(SummaryTable.COLUMN_NAME_AVAILABLE_AT)));

        String ids = c.getString(c.getColumnIndexOrThrow(SummaryTable.COLUMN_NAME_SUBJECT_ID));

        ArrayList<Integer> intIds = new ArrayList<>();

        if (!TextUtils.isEmpty(ids)) {
            String[] idsArray = ids.split(",");

            for (String id : idsArray) {
                intIds.add(Integer.parseInt(id));
            }
        }

        return new Lesson(availableAt, intIds);
    }

    public static void saveLastUpdated(String queryName, DateTime updateTime)
    {
        LastUpdatedLock.writeLock().lock();

        try {
            String whereClause = LastUpdatedTable.COLUMN_NAME_QUERY_NAME + " = ?";
            String[] whereArgs = {queryName};
            db.delete(LastUpdatedTable.TABLE_NAME, whereClause, whereArgs);

            ContentValues values = new ContentValues();

            values.put(LastUpdatedTable.COLUMN_NAME_QUERY_NAME, queryName);
            values.put(LastUpdatedTable.COLUMN_NAME_QUERY_DATE, updateTime.getMillis());

            db.insert(LastUpdatedTable.TABLE_NAME, LastUpdatedTable.COLUMN_NAME_NULLABLE, values);
        }
        finally {
            LastUpdatedLock.writeLock().unlock();
        }
    }

    public static DateTime getLastUpdated(String queryName)
    {
        LastUpdatedLock.readLock().lock();

        try {
            String whereClause = LastUpdatedTable.COLUMN_NAME_QUERY_NAME + " = ?";
            String[] whereArgs = {queryName};

            Cursor c = null;

            try {
                c = db.query(
                        LastUpdatedTable.TABLE_NAME,
                        LastUpdatedTable.COLUMNS,
                        whereClause,
                        whereArgs,
                        null,
                        null,
                        null
                );

                if (c == null || !c.moveToFirst()) {
                    Log.e(TAG, "No last update time found for " + queryName + "; returning minimal time");
                    return new DateTime(0);
                }

                return new DateTime(c.getLong(c.getColumnIndexOrThrow(LastUpdatedTable.COLUMN_NAME_QUERY_DATE)));

            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }
        finally {
            LastUpdatedLock.readLock().unlock();
        }
    }

    public static void saveAssignments(ArrayList<Assignment> assignments)
    {
        AssignmentsLock.writeLock().lock();

        try {
            deleteAssignments(assignments);

            for (Assignment assignment :
                    assignments) {
                saveAssignment(assignment.data);
            }
        }
        finally {
            AssignmentsLock.writeLock().unlock();
        }
    }

    private static void deleteAssignments(ArrayList<Assignment> assignments)
    {
        ArrayList<Integer> subjectIds = new ArrayList<>();

        for (Assignment assignment :
                assignments) {
            subjectIds.add(assignment.data.subject_id);
        }

        String whereClause = AssignmentsTable.COLUMN_NAME_SUBJECT_ID + " IN (?)";
        String[] whereArgs = { TextUtils.join(",", subjectIds) };

        db.delete(AssignmentsTable.TABLE_NAME, whereClause, whereArgs);
    }

    private static void saveAssignment(AssignmentData assignment)
    {
        ContentValues values = new ContentValues();

        values.put(AssignmentsTable.COLUMN_NAME_SUBJECT_ID, assignment.subject_id);
        values.put(AssignmentsTable.COLUMN_NAME_SUBJECT_TYPE, assignment.subject_type);
        values.put(AssignmentsTable.COLUMN_NAME_SRS_STAGE, assignment.srs_stage);
        values.put(AssignmentsTable.COLUMN_NAME_CREATED_AT, assignment.created_at.getMillis());

        if (assignment.unlocked_at == null) values.putNull(AssignmentsTable.COLUMN_NAME_UNLOCKED_AT);
        else values.put(AssignmentsTable.COLUMN_NAME_UNLOCKED_AT, assignment.unlocked_at.getMillis());

        if (assignment.started_at == null) values.putNull(AssignmentsTable.COLUMN_NAME_STARTED_AT);
        else values.put(AssignmentsTable.COLUMN_NAME_STARTED_AT, assignment.started_at.getMillis());

        if (assignment.passed_at == null) values.putNull(AssignmentsTable.COLUMN_NAME_PASSED_AT);
        else values.put(AssignmentsTable.COLUMN_NAME_PASSED_AT, assignment.passed_at.getMillis());

        if (assignment.burned_at == null) values.putNull(AssignmentsTable.COLUMN_NAME_BURNED_AT);
        else values.put(AssignmentsTable.COLUMN_NAME_BURNED_AT, assignment.burned_at.getMillis());

        if (assignment.available_at == null) values.putNull(AssignmentsTable.COLUMN_NAME_AVAILABLE_AT);
        else values.put(AssignmentsTable.COLUMN_NAME_AVAILABLE_AT, assignment.available_at.getMillis());

        if (assignment.resurrected_at == null) values.putNull(AssignmentsTable.COLUMN_NAME_RESURRECTED_AT);
        else values.put(AssignmentsTable.COLUMN_NAME_RESURRECTED_AT, assignment.resurrected_at.getMillis());

        db.insert(AssignmentsTable.TABLE_NAME, AssignmentsTable.COLUMN_NAME_NULLABLE, values);
    }

    public static void saveStudyQueue(StudyQueue queue) {
        deleteStudyQueue();

        ContentValues values = new ContentValues();
        values.put(StudyQueueTable.COLUMN_NAME_LESSONS_AVAILABLE, queue.lessons_available);
        values.put(StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE, queue.reviews_available);
        values.put(StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE_NEXT_HOUR, queue.reviews_available_next_hour);
        values.put(StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE_NEXT_DAY, queue.reviews_available_next_day);
        values.put(StudyQueueTable.COLUMN_NAME_NEXT_REVIEW_DATE, queue.next_review_date);

        db.insert(StudyQueueTable.TABLE_NAME, StudyQueueTable.COLUMN_NAME_NULLABLE, values);
    }

    public static StudyQueue getStudyQueue() {
        Cursor c = null;

        try {
            c = db.query(
                    StudyQueueTable.TABLE_NAME,
                    StudyQueueTable.COLUMNS,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (c != null && c.moveToFirst()) {
                return new StudyQueue(
                        c.getInt(c.getColumnIndexOrThrow(StudyQueueTable.COLUMN_NAME_ID)),
                        c.getInt(c.getColumnIndexOrThrow(StudyQueueTable.COLUMN_NAME_LESSONS_AVAILABLE)),
                        c.getInt(c.getColumnIndexOrThrow(StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE)),
                        c.getInt(c.getColumnIndexOrThrow(StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE_NEXT_HOUR)),
                        c.getInt(c.getColumnIndexOrThrow(StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE_NEXT_DAY)),
                        c.getLong(c.getColumnIndexOrThrow(StudyQueueTable.COLUMN_NAME_NEXT_REVIEW_DATE))
                );
            } else {
                Log.e(TAG, "No study queue found; returning null");
                return null;
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public static void deleteStudyQueue() {
        db.delete(StudyQueueTable.TABLE_NAME, null, null);
    }

    public static void saveLevelProgression(LevelProgression progression) {
        deleteLevelProgression();

        ContentValues values = new ContentValues();
        values.put(LevelProgressionTable.COLUMN_NAME_RADICALS_PROGRESS, progression.radicals_progress);
        values.put(LevelProgressionTable.COLUMN_NAME_RADICALS_TOTAL, progression.radicals_total);
        values.put(LevelProgressionTable.COLUMN_NAME_REVIEWS_KANJI_PROGRESS, progression.kanji_progress);
        values.put(LevelProgressionTable.COLUMN_NAME_REVIEWS_KANJI_TOTAL, progression.kanji_total);

        db.insert(LevelProgressionTable.TABLE_NAME, LevelProgressionTable.COLUMN_NAME_NULLABLE, values);
    }

    public static LevelProgression getLevelProgression() {
        Cursor c = null;

        try {
            c = db.query(
                    LevelProgressionTable.TABLE_NAME,
                    LevelProgressionTable.COLUMNS,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (c != null && c.moveToFirst()) {
                return new LevelProgression(
                        c.getInt(c.getColumnIndexOrThrow(LevelProgressionTable.COLUMN_NAME_ID)),
                        c.getInt(c.getColumnIndexOrThrow(LevelProgressionTable.COLUMN_NAME_RADICALS_PROGRESS)),
                        c.getInt(c.getColumnIndexOrThrow(LevelProgressionTable.COLUMN_NAME_RADICALS_TOTAL)),
                        c.getInt(c.getColumnIndexOrThrow(LevelProgressionTable.COLUMN_NAME_REVIEWS_KANJI_PROGRESS)),
                        c.getInt(c.getColumnIndexOrThrow(LevelProgressionTable.COLUMN_NAME_REVIEWS_KANJI_TOTAL))
                );
            } else {
                Log.e(TAG, "No study queue found; returning null");
                return null;
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public static void deleteLevelProgression() {
        db.delete(SRSDistributionTable.TABLE_NAME, null, null);
    }

    public static void saveSrsDistribution(SRSDistribution distribution) {
        deleteSrsDistribution();

        ContentValues values = new ContentValues();
        values.put(SRSDistributionTable.COLUMN_NAME_APPRENTICE_RADICALS, distribution.apprentice.radicals);
        values.put(SRSDistributionTable.COLUMN_NAME_APPRENTICE_KANJI, distribution.apprentice.kanji);
        values.put(SRSDistributionTable.COLUMN_NAME_APPRENTICE_VOCABULARY, distribution.apprentice.vocabulary);
        values.put(SRSDistributionTable.COLUMN_NAME_GURU_RADICALS, distribution.guru.radicals);
        values.put(SRSDistributionTable.COLUMN_NAME_GURU_KANJI, distribution.guru.kanji);
        values.put(SRSDistributionTable.COLUMN_NAME_GURU_VOCABULARY, distribution.guru.vocabulary);
        values.put(SRSDistributionTable.COLUMN_NAME_MASTER_RADICALS, distribution.master.radicals);
        values.put(SRSDistributionTable.COLUMN_NAME_MASTER_KANJI, distribution.master.kanji);
        values.put(SRSDistributionTable.COLUMN_NAME_MASTER_VOCABULARY, distribution.master.vocabulary);
        values.put(SRSDistributionTable.COLUMN_NAME_ENLIGHTENED_RADICALS, distribution.enlighten.radicals);
        values.put(SRSDistributionTable.COLUMN_NAME_ENLIGHTENED_KANJI, distribution.enlighten.kanji);
        values.put(SRSDistributionTable.COLUMN_NAME_ENLIGHTENED_VOCABULARY, distribution.enlighten.vocabulary);
        values.put(SRSDistributionTable.COLUMN_NAME_BURNED_RADICALS, distribution.burned.radicals);
        values.put(SRSDistributionTable.COLUMN_NAME_BURNED_KANJI, distribution.burned.kanji);
        values.put(SRSDistributionTable.COLUMN_NAME_BURNED_VOCABULARY, distribution.burned.vocabulary);

        db.insert(SRSDistributionTable.TABLE_NAME, SRSDistributionTable.COLUMN_NAME_NULLABLE, values);
    }

    public static SRSDistribution getSrsDistribution() {
        AssignmentsLock.readLock().lock();

        try {
            String whereClause = AssignmentsTable.COLUMN_NAME_SUBJECT_TYPE + " = ? AND "
                    + AssignmentsTable.COLUMN_NAME_SRS_STAGE + " >= ? AND "
                    + AssignmentsTable.COLUMN_NAME_SRS_STAGE + " <= ?";

            return new SRSDistribution(
                    0,
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"radical", "1", "4"}),
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"kanji", "1", "4"}),
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"vocabulary", "1", "4"}),
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"radical", "5", "6"}),
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"kanji", "5", "6"}),
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"vocabulary", "5", "6"}),
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"radical", "7", "7"}),
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"kanji", "7", "7"}),
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"vocabulary", "7", "7"}),
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"radical", "8", "8"}),
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"kanji", "8", "8"}),
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"vocabulary", "8", "8"}),
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"radical", "9", "9"}),
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"kanji", "9", "9"}),
                    (int) getRowCount(AssignmentsTable.TABLE_NAME, whereClause, new String[]{"vocabulary", "9", "9"})
            );
        }
        finally {
            AssignmentsLock.readLock().unlock();
        }
    }

    private static long getRowCount(String table, String where, String[] whereArgs)
    {
        return DatabaseUtils.queryNumEntries(db, table, where, whereArgs);
    }

    public static void deleteSrsDistribution() {
        db.delete(SRSDistributionTable.TABLE_NAME, null, null);
    }

    private static void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(UsersTable.COLUMN_NAME_USERNAME, user.username);
        values.put(UsersTable.COLUMN_NAME_GRAVATAR, user.gravatar);
        values.put(UsersTable.COLUMN_NAME_LEVEL, user.level);
        values.put(UsersTable.COLUMN_NAME_TITLE, user.title);
        values.put(UsersTable.COLUMN_NAME_ABOUT, user.about);
        values.put(UsersTable.COLUMN_NAME_WEBSITE, user.website);
        values.put(UsersTable.COLUMN_NAME_TWITTER, user.twitter);
        values.put(UsersTable.COLUMN_NAME_TOPICS_COUNT, user.topics_count);
        values.put(UsersTable.COLUMN_NAME_POSTS_COUNT, user.posts_count);
        values.put(UsersTable.COLUMN_NAME_CREATION_DATE, user.creation_date);
        values.put(UsersTable.COLUMN_NAME_VACATION_DATE, user.vacation_date);
        db.insert(UsersTable.TABLE_NAME, UsersTable.COLUMN_NAME_NULLABLE, values);
    }

    public static void saveUser(User user) {
        deleteUsers();

        addUser(user);
    }

    public static User getUser() {
        Cursor c = null;

        try {
            c = db.query(
                    UsersTable.TABLE_NAME,
                    UsersTable.COLUMNS,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (c != null && c.moveToFirst()) {
                return new User(
                        c.getString(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_USERNAME)),
                        c.getString(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_GRAVATAR)),
                        c.getInt(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_LEVEL)),
                        c.getString(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_TITLE)),
                        c.getString(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_ABOUT)),
                        c.getString(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_WEBSITE)),
                        c.getString(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_TWITTER)),
                        c.getInt(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_TOPICS_COUNT)),
                        c.getInt(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_POSTS_COUNT)),
                        c.getLong(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_CREATION_DATE)),
                        c.getLong(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_VACATION_DATE))
                );
            } else {
                Log.e(TAG, "No users found; returning null");
                return null;
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public static void deleteUsers() {
        db.delete(UsersTable.TABLE_NAME, null, null);
    }

    public static void saveNotification(Notification item) {
        db.delete(
                NotificationsTable.TABLE_NAME,
                NotificationsTable.COLUMN_NAME_ID + " = ?",
                new String[]{String.valueOf(item.getId())}
        );

        ContentValues values = new ContentValues();
        values.put(NotificationsTable.COLUMN_NAME_ID, item.getId());
        values.put(NotificationsTable.COLUMN_NAME_TITLE, item.getTitle());
        values.put(NotificationsTable.COLUMN_NAME_SHORT_TEXT, item.getShortText());
        values.put(NotificationsTable.COLUMN_NAME_TEXT, item.getText());
        values.put(NotificationsTable.COLUMN_NAME_IMAGE, item.getImage());
        values.put(NotificationsTable.COLUMN_NAME_ACTION_URL, item.getActionUrl());
        values.put(NotificationsTable.COLUMN_NAME_READ, item.isRead() ? 1 : 0);
        values.put(NotificationsTable.COLUMN_NAME_ACTION_TEXT, item.getActionText());

        db.insert(NotificationsTable.TABLE_NAME, NotificationsTable.COLUMN_NAME_NULLABLE, values);
    }

    public static void saveNotifications(List<Notification> list) {
        deleteSameNotifications(list);

        for (Notification item : list)
            saveNotification(item);
    }

    public static List<Notification> getNotifications() {
        List<Notification> list = new ArrayList<>();

        String whereClause = NotificationsTable.COLUMN_NAME_READ + " = ?";
        String[] whereArgs = {
                "0"
        };

        Cursor c = null;

        try {
            c = db.query(
                    NotificationsTable.TABLE_NAME,
                    NotificationsTable.COLUMNS,
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    null
            );

            if (c != null && c.moveToFirst()) {
                do {
                    Notification item = new Notification(
                            c.getInt(c.getColumnIndexOrThrow(NotificationsTable.COLUMN_NAME_ID)),
                            c.getString(c.getColumnIndexOrThrow(NotificationsTable.COLUMN_NAME_TITLE)),
                            c.getString(c.getColumnIndexOrThrow(NotificationsTable.COLUMN_NAME_SHORT_TEXT)),
                            c.getString(c.getColumnIndexOrThrow(NotificationsTable.COLUMN_NAME_TEXT)),
                            c.getString(c.getColumnIndexOrThrow(NotificationsTable.COLUMN_NAME_IMAGE)),
                            c.getString(c.getColumnIndexOrThrow(NotificationsTable.COLUMN_NAME_ACTION_URL)),
                            c.getString(c.getColumnIndexOrThrow(NotificationsTable.COLUMN_NAME_ACTION_TEXT)),
                            c.getInt(c.getColumnIndexOrThrow(NotificationsTable.COLUMN_NAME_READ)) == 1
                    );
                    list.add(item);
                } while (c.moveToNext());
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return list;
    }

    private static void deleteSameNotifications(List<Notification> list) {
        for (Notification item : list) {
            String whereClause = NotificationsTable.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {
                    String.valueOf(item.getId())
            };

            db.delete(NotificationsTable.TABLE_NAME, whereClause, whereArgs);
        }
    }
}